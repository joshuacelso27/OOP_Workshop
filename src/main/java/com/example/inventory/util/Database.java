package com.example.inventory.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        String url = getRequired("DB_URL", "jdbc:sqlite:inventory.db");
        String user = DOTENV.get("DB_USER", "");
        String password = DOTENV.get("DB_PASSWORD", "");

        if (user.isBlank()) {
            return DriverManager.getConnection(url);
        }

        return DriverManager.getConnection(url, user, password);
    }

    public static void initialize() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL
                    )
                    """);
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        category TEXT NOT NULL,
                        quantity INTEGER NOT NULL DEFAULT 0,
                        price REAL NOT NULL DEFAULT 0,
                        created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to initialize database.", ex);
        }
    }

    private static String getRequired(String key, String fallback) {
        String value = DOTENV.get(key, fallback);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(key + " is required.");
        }
        return value;
    }
}
