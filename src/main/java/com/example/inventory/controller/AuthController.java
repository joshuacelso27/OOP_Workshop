package com.example.inventory.controller;

import com.example.inventory.InventoryApplication;
import com.example.inventory.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AuthController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showMessage("Enter username and password.");
            return;
        }

        try {
            if (userRepository.authenticate(username, password)) {
                InventoryApplication.showInventoryView(username);
            } else {
                showMessage("Invalid username or password.");
            }
        } catch (SQLException | IOException ex) {
            showMessage("Login failed: " + ex.getMessage());
        }
    }

    @FXML
    private void goToSignup() throws IOException {
        InventoryApplication.showSignupView();
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}
