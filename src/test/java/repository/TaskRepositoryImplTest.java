package repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.db.ConnectionManager;
import com.example.entity.Task;
import com.example.repository.TaskRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import java.util.List;

class TaskRepositoryImplTest {

    private ConnectionManager connectionManager;
    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        connectionManager = mock(ConnectionManager.class);
        taskRepository = new TaskRepositoryImpl(connectionManager);
    }

    @Test
    void findById_ValidId_ReturnsTask() throws SQLException {
        Long taskId = 1L;
        Task expectedTask = new Task(taskId, "Test Task", "Test Description", 1L);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(expectedTask.getId());
        when(resultSet.getString("title")).thenReturn(expectedTask.getTitle());
        when(resultSet.getString("description")).thenReturn(expectedTask.getDescription());
        when(resultSet.getLong("user_id")).thenReturn(expectedTask.getUserId());

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        Task result = taskRepository.findById(taskId);

        assertNotNull(result);
        assertEquals(expectedTask.getId(), result.getId());
        assertEquals(expectedTask.getTitle(), result.getTitle());
        assertEquals(expectedTask.getDescription(), result.getDescription());
        assertEquals(expectedTask.getUserId(), result.getUserId());
    }

    @Test
    void findAll_ReturnsListOfTasks() throws SQLException {
        Task expectedTask = new Task(1L, "Test Task", "Test Description", 1L);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(expectedTask.getId());
        when(resultSet.getString("title")).thenReturn(expectedTask.getTitle());
        when(resultSet.getString("description")).thenReturn(expectedTask.getDescription());
        when(resultSet.getLong("user_id")).thenReturn(expectedTask.getUserId());

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        Connection connection = mock(Connection.class);
        Statement stmtMock = mock(Statement.class);
        when(connection.createStatement()).thenReturn(stmtMock);
        when(stmtMock.executeQuery(anyString())).thenReturn(resultSet); // изменение тут

        when(connectionManager.getConnection()).thenReturn(connection);

        List<Task> result = taskRepository.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        Task task = result.get(0);
        assertEquals(expectedTask.getId(), task.getId());
        assertEquals(expectedTask.getTitle(), task.getTitle());
        assertEquals(expectedTask.getDescription(), task.getDescription());
        assertEquals(expectedTask.getUserId(), task.getUserId());
    }

    @Test
    void findByUserId_ValidUserId_ReturnsListOfTasks() throws SQLException {
        Long userId = 1L;
        Task expectedTask = new Task(1L, "Test Task", "Test Description", userId);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(expectedTask.getId());
        when(resultSet.getString("title")).thenReturn(expectedTask.getTitle());
        when(resultSet.getString("description")).thenReturn(expectedTask.getDescription());
        when(resultSet.getLong("user_id")).thenReturn(expectedTask.getUserId());

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        List<Task> result = taskRepository.findByUserId(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        Task task = result.get(0);
        assertEquals(expectedTask.getId(), task.getId());
        assertEquals(expectedTask.getTitle(), task.getTitle());
        assertEquals(expectedTask.getDescription(), task.getDescription());
        assertEquals(expectedTask.getUserId(), task.getUserId());
    }

    @Test
    void save_ValidTask_ReturnsSavedTask() throws SQLException {
        Task taskToSave = new Task(null, "Test Task", "Test Description", 1L);
        ResultSet generatedKeys = mock(ResultSet.class);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeUpdate()).thenReturn(1);
        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        Task result = taskRepository.save(taskToSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(taskToSave.getTitle(), result.getTitle());
        assertEquals(taskToSave.getDescription(), result.getDescription());
        assertEquals(taskToSave.getUserId(), result.getUserId());
    }

    @Test
    void update_ValidTask_ReturnsUpdatedTask() throws SQLException {
        Task taskToUpdate = new Task(1L, "Updated Task", "Updated Description", 2L);
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeUpdate()).thenReturn(1);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        Task result = taskRepository.update(taskToUpdate);

        assertNotNull(result);
        assertEquals(taskToUpdate.getId(), result.getId());
        assertEquals(taskToUpdate.getTitle(), result.getTitle());
        assertEquals(taskToUpdate.getDescription(), result.getDescription());
        assertEquals(taskToUpdate.getUserId(), result.getUserId());
    }

    @Test
    void delete_ValidId_DeletesTask() throws SQLException {
        Long taskId = 1L;
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeUpdate()).thenReturn(1);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        taskRepository.delete(taskId);

        verify(statement, times(1)).executeUpdate();
    }
}
