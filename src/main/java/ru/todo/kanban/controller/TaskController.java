package ru.todo.kanban.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.todo.kanban.model.Task;
import ru.todo.kanban.service.TaskService;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task addTask(@RequestBody Task task) {
        log.info("Получен запрос на добавление задачи");
        Task taskToAdd = taskService.addTask(task);
        log.info("Задача успешно добавлена. Id задачи = {}", taskToAdd.getId());
        return taskToAdd;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        log.info("Получен запрос на получение всех задач");
        List<Task> allTasks = taskService.getAllTasks();
        log.info("Получен список задач. Текущее количество :{}", allTasks.size());
        return allTasks;
    }

    @GetMapping("/{dateFilter}/{complete}")
    public List<Task> getAllTasksByFilter(@PathVariable String dateFilter, Boolean complete) {
        log.info("Получен запрос на получение задач по фильтрации: {}", dateFilter);
        List<Task> allTasks = taskService.getAllTasksByFilter(dateFilter, complete);
        log.info("Получен список задач. Текущее количество :{}", allTasks.size());
        return allTasks;
    }

    @GetMapping("/{id}")
    public Task getTaskById(Integer taskId) {
        log.info("Получен запрос на получение задачи");
        Task taskToUpdate = taskService.getTaskById(taskId);
        log.info("Задача с ID: {} получена", taskToUpdate.getId());
        return taskToUpdate;
    }

    @PutMapping
    public Task updateTask(@RequestBody Task task) {
        log.info("Получен запрос на обновление задачи с ID: {}", task.getId());
        Task taskToUpdate = taskService.updateTask(task);
        log.info("Задча с ID: {}, обновлена", task.getId());
        return task;
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Integer taskId) {
        log.info("Получен запрос на удаление задачи с ID: {}", taskId);
        taskService.deleteTask(taskId);
        log.info("Задача удалена с ID: {}", taskId);
    }

    @PatchMapping("/{id}/completion")
    public void toggleTaskCompletion(@PathVariable Integer taskId) {
        log.info("Получен запрос на обновление статуса задачи с ID: {}", taskId);
        taskService.toggleTaskCompletion(taskId);
        log.info("Статус задачи обновлен");
    }
}
