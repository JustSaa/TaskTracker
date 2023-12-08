package ru.todo.kanban.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.todo.kanban.exeptions.NotFoundException;
import ru.todo.kanban.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class TaskDbStorage implements Storage {

    private final JdbcTemplate jdbcTemplate;
    private int idTask = 0;

    public TaskDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task add(Task task) {
        String sqlQuery = "INSERT INTO tasks(id, title, description, startDate, dueDate, completed) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, updateTaskId(), task.getTitle(), task.getDescription(), task.getStartDate(),
                task.getDueDate(), false);
        String sqlQueryForGetTask = "SELECT * FROM tasks t WHERE t.id = ?";
        SqlRowSet usersSet = jdbcTemplate.queryForRowSet(sqlQueryForGetTask, idTask);
        return getTaskFromDB(usersSet);
    }

    @Override
    public List<Task> getAll() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, this::mapRowToTask);
    }

    public List<Task> getAllTasksByFilter(Boolean complete) {
        String sql = "SELECT * FROM tasks WHERE completed IS true";
        return jdbcTemplate.query(sql, this::mapRowToTask);
    }

    private Task mapRowToTask(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
        LocalDate dueDate = resultSet.getDate("dueDate").toLocalDate();
        boolean completed = resultSet.getBoolean("completed");

        return new Task(id, title, description, startDate, dueDate, completed);
    }

    @Override
    public Task update(Task task) {
        String sqlQuery = "UPDATE tasks SET title = ?, description=?, startDate=?, dueDate=?, completed=? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, task.getTitle(), task.getDescription(), task.getStartDate(), task.getDueDate(),
                task.getCompleted(), task.getId());
        String sqlQueryForGetUser = "SELECT * FROM tasks WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQueryForGetUser, task.getId());
        return getTaskFromDB(sqlRowSet);
    }

    @Override
    public void delete(Integer taskId) {
        String sqlQuery = "DELETE FROM tasks WHERE id = ?";
        jdbcTemplate.update(sqlQuery, taskId);
    }

    @Override
    public Task getById(Integer taskId) {
        String sqlQueryForGetUser = "SELECT * FROM tasks WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQueryForGetUser, taskId);
        return getTaskFromDB(sqlRowSet);
    }

    public void toggleTaskCompletion(Integer taskId) {
        String sqlQuery = "UPDATE tasks SET completed = NOT completed WHERE id = ?";
        jdbcTemplate.update(sqlQuery, taskId);
    }

    private int updateTaskId() {
        return ++idTask;
    }

    private Task getTaskFromDB(SqlRowSet taskRows) {
        if (taskRows.next()) {
            Long taskId = taskRows.getLong("id");
            return Task.builder()
                    .id(taskId)
                    .title(taskRows.getString("title"))
                    .description(taskRows.getString("description"))
                    .startDate(taskRows.getDate("startDate").toLocalDate())
                    .dueDate(taskRows.getDate("dueDate").toLocalDate())
                    .completed(taskRows.getBoolean("completed"))
                    .build();
        } else {
            throw new NotFoundException("Задача не найдена в Базе Данных");
        }
    }
}
