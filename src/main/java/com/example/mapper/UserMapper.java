package com.example.mapper;

import com.example.dto.UserDTO;
import com.example.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public User toEntity(UserDTO userDTO) {
        return User.builder().name(userDTO.getName()).email(userDTO.getEmail()).build();
    }

    public UserDTO toDto(User user) {
        return UserDTO.builder().name(user.getName()).email(user.getEmail()).build();
    }
}
