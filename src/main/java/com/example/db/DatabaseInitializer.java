package com.example.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection connection = new PostgresConnectionManager().getConnection();
             Statement statement = connection.createStatement()) {

            // Удаление таблиц, если они существуют
            String dropUserTableSQL = "DROP TABLE IF EXISTS users CASCADE ";
            statement.executeUpdate(dropUserTableSQL);

            String dropTaskTableSQL = "DROP TABLE IF EXISTS tasks CASCADE ";
            statement.executeUpdate(dropTaskTableSQL);
            // Создание таблицы пользователей
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(100) NOT NULL,"
                    + "email VARCHAR(100) NOT NULL"
                    + ")";
            statement.executeUpdate(createUserTableSQL);

            // Создание таблицы задач
            String createTaskTableSQL = "CREATE TABLE IF NOT EXISTS tasks ("
                    + "id SERIAL PRIMARY KEY,"
                    + "title VARCHAR(100) NOT NULL,"
                    + "description TEXT,"
                    + "user_id INT REFERENCES users(id)"
                    + ")";
            statement.executeUpdate(createTaskTableSQL);

            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
