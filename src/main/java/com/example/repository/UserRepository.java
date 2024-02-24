package com.example.repository;

import com.example.entity.User;

import java.util.List;

public interface UserRepository {
    User findById(Long id);

    List<User> findAll();

    User save(User user);

    User update(User user);

    void delete(Long id);
}
