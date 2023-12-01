package ru.todo.kanban.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    protected Long id;
    protected String title;
    protected String description;
    protected LocalDateTime startDate;
    protected LocalDateTime dueDate;
    protected boolean completed;
}
