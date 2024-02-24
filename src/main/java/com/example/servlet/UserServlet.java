package com.example.servlet;

import com.example.db.ConnectionManager;
import com.example.db.PostgresConnectionManager;
import com.example.dto.UserDTO;
import com.example.repository.TaskRepositoryImpl;
import com.example.repository.UserRepositoryImpl;
import com.example.service.TaskService;
import com.example.service.TaskServiceImpl;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    ConnectionManager connectionManager = new PostgresConnectionManager();
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl(connectionManager));
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryImpl(connectionManager));
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<UserDTO> users = userService.getAllUsers();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(users));
        } else {
            String[] pathParts = pathInfo.split("/");
            Long userId = Long.parseLong(pathParts[1]);
            UserDTO user = userService.getUserById(userId);
            if (user != null) {
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(user));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        UserDTO newUser = objectMapper.readValue(req.getReader(), UserDTO.class);
        newUser = userService.createUser(newUser);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(newUser));
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long userId = Long.parseLong(pathParts[1]);
        UserDTO updatedUser = objectMapper.readValue(req.getReader(), UserDTO.class);
        updatedUser = userService.updateUser(userId, updatedUser);
        if (updatedUser != null) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(updatedUser));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long userId = Long.parseLong(pathParts[1]);
        try {
            taskService.deleteAllTasksByUserId(userId);
            userService.deleteUser(userId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }
}
