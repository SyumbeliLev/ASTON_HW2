package com.example.entity;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private List<Task> tasks;

    public User(Long userId, String johnDoe, String mail) {
    }
}