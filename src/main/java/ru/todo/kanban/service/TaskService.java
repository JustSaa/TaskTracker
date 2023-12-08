package ru.todo.kanban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.todo.kanban.model.Task;
import ru.todo.kanban.storage.TaskDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskDbStorage taskDbStorage;

    @Autowired
    public TaskService(TaskDbStorage taskDbStorage) {
        this.taskDbStorage = taskDbStorage;
    }

    public Task addTask(Task task) {
        return taskDbStorage.add(task);
    }

    public Task updateTask(Task task) {
        return taskDbStorage.update(task);
    }

    public void deleteTask(Integer taskId) {
        taskDbStorage.delete(taskId);
    }

    public List<Task> getAllTasks() {
        return taskDbStorage.getAll();
    }

    public List<Task> getAllTasksByFilter(String dateFilter, Boolean complete) {
        List<Task> listTasksByComplete = taskDbStorage.getAllTasksByFilter(complete);
        return filteredByDate(dateFilter, listTasksByComplete);
    }

    public Task getTaskById(Integer taskId) {
        return taskDbStorage.getById(taskId);
    }

    public void toggleTaskCompletion(Integer taskId) {
        taskDbStorage.toggleTaskCompletion(taskId);
    }

    private List<Task> filteredByDate(String date, List<Task> tasks) {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();
        switch (date) {
            case "today":
                break;
            case "week":
                to = to.plusWeeks(1);
                break;
            case "month":
                to = to.plusMonths(1);
                break;
        }

        LocalDate finalTo = to;
        tasks = tasks.stream().filter(task -> task.getDueDate().isAfter(from) && task.getDueDate().isBefore(finalTo))
                .sorted((task1, task2) -> task1.getDueDate().compareTo(task2.getDueDate()))
                .collect(Collectors.toList());
        return tasks;
    }
}
