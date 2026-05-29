package com.example.inventory.controller;

import com.example.inventory.InventoryApplication;
import com.example.inventory.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            showMessage("Complete all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters.");
            return;
        }

        try {
            userRepository.createUser(username, password);
            showMessage("Account created. You can now log in.");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } catch (SQLException ex) {
            showMessage("Signup failed. Username may already exist.");
        }
    }

    @FXML
    private void goToLogin() throws IOException {
        InventoryApplication.showLoginView();
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}
