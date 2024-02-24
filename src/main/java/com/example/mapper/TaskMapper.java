package com.example.mapper;

import com.example.dto.TaskDTO;
import com.example.entity.Task;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskMapper {
    public Task toEntity(TaskDTO taskDTO) {
        return Task.builder().description(taskDTO.getDescription()).title(taskDTO.getTitle()).userId(taskDTO.getUserId()).build();
    }

    public TaskDTO toDto(Task task) {
        return TaskDTO.builder().description(task.getDescription()).userId(task.getUserId()).title(task.getTitle()).build();
    }
}
