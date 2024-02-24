package com.example.repository;

import com.example.entity.Task;

import java.util.List;

public interface TaskRepository {
    Task findById(Long id);

    List<Task> findAll();

    Task update(Task task);

    List<Task> findByUserId(Long userId);

    Task save(Task task);

    void delete(Long id);

    void deleteAllTasksByUserId(Long id);
}
