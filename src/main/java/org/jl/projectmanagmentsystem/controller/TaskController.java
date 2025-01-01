package org.jl.projectmanagmentsystem.controller;

import org.jl.projectmanagmentsystem.model.Task;
import org.jl.projectmanagmentsystem.repository.TaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LogManager.getLogger(TaskController.class);

    @Autowired
    private TaskRepository taskRepository;

    // Получить все задачи
    @GetMapping
    public List<Task> getAllTasks() {
        logger.info("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        logger.info("Found {} tasks", tasks.size());
        return tasks;
    }

    // Получить задачу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("Fetching task with ID: {}", id);
        Optional<Task> task = taskRepository.findById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Task with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Создать новую задачу
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("Creating new task: {}", task.getTitle());
        Task savedTask = taskRepository.save(task);
        logger.info("Task created with ID: {}", savedTask.getId());
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // Обновить задачу
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        logger.info("Updating task with ID: {}", id);
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(taskDetails.getTitle());
            task.setStatus(taskDetails.getStatus());
            task.setProject(taskDetails.getProject());
            task.setAssignedUser(taskDetails.getAssignedUser());
            Task updatedTask = taskRepository.save(task);
            logger.info("Task with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedTask);
        } else {
            logger.warn("Task with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Удалить задачу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
        logger.info("Task with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
