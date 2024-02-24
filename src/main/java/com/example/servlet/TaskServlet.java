package com.example.servlet;

import com.example.db.ConnectionManager;
import com.example.db.PostgresConnectionManager;
import com.example.dto.TaskDTO;
import com.example.repository.TaskRepositoryImpl;
import com.example.repository.UserRepositoryImpl;
import com.example.service.TaskService;
import com.example.service.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {
    ConnectionManager connectionManager = new PostgresConnectionManager();
    private final TaskService taskService = new TaskServiceImpl(new TaskRepositoryImpl(connectionManager));
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<TaskDTO> tasks = taskService.getAllTasks();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(tasks));
        } else {
            String[] pathParts = pathInfo.split("/");
            String action = pathParts[1];
            switch (action) {
                case "getTaskById":
                    Long taskId = Long.parseLong(pathParts[2]);
                    TaskDTO task = taskService.getTaskById(taskId);
                    if (task != null) {
                        resp.setContentType("application/json");
                        resp.getWriter().write(objectMapper.writeValueAsString(task));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                case "getTasksByUserId":
                    Long userId = Long.parseLong(pathParts[2]);
                    List<TaskDTO> tasksByUser = taskService.getTasksByUserId(userId);
                    resp.setContentType("application/json");
                    resp.getWriter().write(objectMapper.writeValueAsString(tasksByUser));
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    break;
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        TaskDTO newTask = objectMapper.readValue(req.getReader(), TaskDTO.class);
        newTask = taskService.createTask(newTask);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(newTask));
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long taskId = Long.parseLong(pathParts[1]);
        TaskDTO updatedTask = objectMapper.readValue(req.getReader(), TaskDTO.class);
        updatedTask = taskService.updateTask(taskId, updatedTask);
        if (updatedTask != null) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(updatedTask));
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long taskId = Long.parseLong(pathParts[1]);
        taskService.deleteTask(taskId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
