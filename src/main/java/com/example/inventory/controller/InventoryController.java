package com.example.inventory.controller;

import com.example.inventory.InventoryApplication;
import com.example.inventory.model.Item;
import com.example.inventory.repository.ItemRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;

public class InventoryController {
    @FXML private Label userLabel;
    @FXML private Label messageLabel;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TableView<Item> itemTable;
    @FXML private TableColumn<Item, Integer> idColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, Integer> quantityColumn;
    @FXML private TableColumn<Item, Double> priceColumn;

    private final ItemRepository itemRepository = new ItemRepository();
    private final ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        itemTable.setItems(items);

        itemTable.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, item) -> {
            if (item != null) {
                nameField.setText(item.getName());
                categoryField.setText(item.getCategory());
                quantityField.setText(String.valueOf(item.getQuantity()));
                priceField.setText(String.valueOf(item.getPrice()));
            }
        });

        loadItems();
    }

    public void setCurrentUser(String username) {
        userLabel.setText("Logged in as " + username);
    }

    @FXML
    private void addItem() {
        try {
            Item item = readForm(null);
            itemRepository.create(item);
            clearForm();
            loadItems();
            showMessage("Item added.");
        } catch (IllegalArgumentException | SQLException ex) {
            showMessage(ex.getMessage());
        }
    }

    @FXML
    private void updateItem() {
        Item selected = itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Select an item to update.");
            return;
        }

        try {
            Item item = readForm(selected.getId());
            itemRepository.update(item);
            clearForm();
            loadItems();
            showMessage("Item updated.");
        } catch (IllegalArgumentException | SQLException ex) {
            showMessage(ex.getMessage());
        }
    }

    @FXML
    private void deleteItem() {
        Item selected = itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Select an item to delete.");
            return;
        }

        try {
            itemRepository.delete(selected.getId());
            clearForm();
            loadItems();
            showMessage("Item deleted.");
        } catch (SQLException ex) {
            showMessage(ex.getMessage());
        }
    }

    @FXML
    private void refreshItems() {
        loadItems();
        showMessage("Inventory refreshed.");
    }

    @FXML
    private void clearForm() {
        itemTable.getSelectionModel().clearSelection();
        nameField.clear();
        categoryField.clear();
        quantityField.clear();
        priceField.clear();
    }

    @FXML
    private void logout() throws IOException {
        InventoryApplication.showLoginView();
    }

    private void loadItems() {
        try {
            items.setAll(itemRepository.findAll());
        } catch (SQLException ex) {
            showMessage("Unable to load inventory: " + ex.getMessage());
        }
    }

    private Item readForm(Integer id) {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isBlank() || category.isBlank() || quantityText.isBlank() || priceText.isBlank()) {
            throw new IllegalArgumentException("Complete all item fields.");
        }

        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityText);
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Quantity and price must be numbers.");
        }

        if (quantity < 0 || price < 0) {
            throw new IllegalArgumentException("Quantity and price cannot be negative.");
        }

        return new Item(id, name, category, quantity, price);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }
}
