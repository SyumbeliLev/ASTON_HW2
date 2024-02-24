package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.dto.TaskDTO;
import com.example.entity.Task;
import com.example.mapper.TaskMapper;
import com.example.repository.TaskRepository;
import com.example.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    void getTaskById_ExistingId_ReturnsTaskDTO() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        TaskDTO expectedTaskDTO = TaskMapper.toDto(task);
        when(taskRepository.findById(taskId)).thenReturn(task);

        TaskDTO result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(expectedTaskDTO, result);
    }

    @Test
    void getTaskById_NonExistingId_ReturnsNull() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(null);

        TaskDTO result = taskService.getTaskById(taskId);

        assertNull(result);
    }

    @Test
    void getAllTasks_ReturnsListOfTaskDTOs() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDTO> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(tasks.size(), result.size());
    }

    @Test
    void getTasksByUserId_ReturnsListOfTaskDTOs() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task());
        tasks.add(new Task());
        when(taskRepository.findByUserId(userId)).thenReturn(tasks);

        List<TaskDTO> result = taskService.getTasksByUserId(userId);

        assertNotNull(result);
        assertEquals(tasks.size(), result.size());
    }


    @Test
    void updateTask_NonExistingId_ReturnsNull() {
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        when(taskRepository.findById(taskId)).thenReturn(null);

        TaskDTO result = taskService.updateTask(taskId, taskDTO);

        assertNull(result);
    }

    @Test
    void deleteTask_ExistingId_RemovesTask() {
        Long taskId = 1L;


        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).delete(taskId);
    }

    @Test
    void deleteAllTasksByUserId_ExistingUserId_RemovesAllTasks() {
        Long userId = 1L;

        taskService.deleteAllTasksByUserId(userId);

        verify(taskRepository, times(1)).deleteAllTasksByUserId(userId);
    }
}
