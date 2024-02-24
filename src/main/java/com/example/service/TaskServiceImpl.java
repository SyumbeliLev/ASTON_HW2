package com.example.service;

import com.example.dto.TaskDTO;
import com.example.entity.Task;
import com.example.mapper.TaskMapper;
import com.example.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id);
        if (task != null) {
            return TaskMapper.toDto(task);
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = TaskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return TaskMapper.toDto(task);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id);
        if (existingTask != null) {
            Task updatedTask = TaskMapper.toEntity(taskDTO);
            updatedTask.setId(id);
            updatedTask = taskRepository.update(updatedTask);
            return TaskMapper.toDto(updatedTask);
        }
        return null;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.delete(id);
    }

    @Override
    public void deleteAllTasksByUserId(Long id) {
        taskRepository.deleteAllTasksByUserId(id);
    }


}
