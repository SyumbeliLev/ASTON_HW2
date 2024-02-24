package com.example.service;

import com.example.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO getTaskById(Long id);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getTasksByUserId(Long userId);

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    void deleteTask(Long id);

    void deleteAllTasksByUserId(Long id);
}
