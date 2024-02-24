package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getUserById_ExistingId_ReturnsUserDTO() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserDTO expectedUserDTO = UserMapper.toDto(user);
        when(userRepository.findById(userId)).thenReturn(user);


        UserDTO result = userService.getUserById(userId);


        assertNotNull(result);
        assertEquals(expectedUserDTO, result);
    }

    @Test
    void getUserById_NonExistingId_ReturnsNull() {

        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(null);


        UserDTO result = userService.getUserById(userId);


        assertNull(result);
    }

    @Test
    void getAllUsers_ReturnsListOfUserDTOs() {

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);


        List<UserDTO> result = userService.getAllUsers();


        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }


    @Test
    void updateUser_NonExistingId_ReturnsNull() {

        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        when(userRepository.findById(userId)).thenReturn(null);


        UserDTO result = userService.updateUser(userId, userDTO);


        assertNull(result);
    }

    @Test
    void deleteUser_ExistingId_RemovesUser() {

        Long userId = 1L;


        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(userId);
    }
}
