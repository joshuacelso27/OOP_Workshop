package com.example.inventory.repository;

import com.example.inventory.model.Item;
import com.example.inventory.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    public List<Item> findAll() throws SQLException {
        String sql = "SELECT id, name, category, quantity, price FROM items ORDER BY id DESC";
        List<Item> items = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                items.add(mapItem(resultSet));
            }
        }

        return items;
    }

    public void create(Item item) throws SQLException {
        String sql = "INSERT INTO items (name, category, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getCategory());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getPrice());
            statement.executeUpdate();
        }
    }

    public void update(Item item) throws SQLException {
        String sql = "UPDATE items SET name = ?, category = ?, quantity = ?, price = ? WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getCategory());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Item mapItem(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("category"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("price")
        );
    }
}
