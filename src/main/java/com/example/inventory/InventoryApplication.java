package com.example.inventory;

import com.example.inventory.util.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        Database.initialize();
        showLoginView();
        stage.setTitle("Inventory Management");
        stage.show();
    }

    public static void showLoginView() throws IOException {
        setScene("login-view.fxml", 460, 420);
    }

    public static void showSignupView() throws IOException {
        setScene("signup-view.fxml", 460, 470);
    }

    public static void showInventoryView(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(InventoryApplication.class.getResource("inventory-view.fxml"));
        Scene scene = new Scene(loader.load(), 900, 620);
        scene.getStylesheets().add(InventoryApplication.class.getResource("styles.css").toExternalForm());
        var controller = (com.example.inventory.controller.InventoryController) loader.getController();
        controller.setCurrentUser(username);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private static void setScene(String fxml, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(InventoryApplication.class.getResource(fxml));
        Scene scene = new Scene(loader.load(), width, height);
        scene.getStylesheets().add(InventoryApplication.class.getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
