package repository;

import com.example.db.ConnectionManager;
import com.example.entity.User;
import com.example.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserRepositoryImplTest {

    private ConnectionManager connectionManager;
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        connectionManager = mock(ConnectionManager.class);
        userRepository = new UserRepositoryImpl(connectionManager);
    }

    @Test
    void findById_ValidId_ReturnsUser() throws SQLException {
        Long userId = 1L;
        User expectedUser = new User(userId, "John Doe", "john@example.com");
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        doReturn(userId).when(resultSet).getLong("id"); // Use doReturn for stubbing
        when(resultSet.getString("name")).thenReturn(expectedUser.getName());
        when(resultSet.getString("email")).thenReturn(expectedUser.getEmail());

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        User result = userRepository.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(expectedUser.getName(), result.getName());
        assertEquals(expectedUser.getEmail(), result.getEmail());
    }


    @Test
    void save_ValidUser_ReturnsSavedUser() throws SQLException {
        User userToSave = new User(null, "John Doe", "john@example.com");
        ResultSet generatedKeys = mock(ResultSet.class);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeUpdate()).thenReturn(1);
        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);

        User result = userRepository.save(userToSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(userToSave.getName(), result.getName());
        assertEquals(userToSave.getEmail(), result.getEmail());
    }

    @Test
    void delete_ValidId_DeletesUser() throws SQLException {
        Long userId = 1L;
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeUpdate()).thenReturn(1);

        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connectionManager.getConnection()).thenReturn(connection);
        userRepository.delete(userId);

        verify(statement, times(1)).executeUpdate();
    }
}
