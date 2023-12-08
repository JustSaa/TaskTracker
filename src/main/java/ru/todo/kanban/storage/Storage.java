package ru.todo.kanban.storage;

import ru.todo.kanban.model.Task;

import java.util.List;

public interface Storage {
    Task add(Task task);

    List<Task> getAll();

    Task update(Task task);

    void delete(Integer taskId);

    Task getById(Integer taskId);
}
