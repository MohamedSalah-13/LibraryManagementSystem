package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.database.DatabaseConnection;
import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.sql.*;

public class DashboardController {

    @FXML private Label    totalBooksLabel;
    @FXML private Label    totalMembersLabel;
    @FXML private Label    borrowedLabel;
    @FXML private BarChart<String, Number> statsChart;

    @FXML
    public void initialize() {
        if (!SceneManager.requireLogin()) return;
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            Connection conn = DatabaseConnection.getConnection();

            statsChart.getData().clear();

            // إجمالي الكتب
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM books");
            if (rs1.next()) totalBooksLabel.setText(String.valueOf(rs1.getInt(1)));

            // إجمالي الأعضاء
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM members");
            if (rs2.next()) totalMembersLabel.setText(String.valueOf(rs2.getInt(1)));

            // الكتب المستعارة
            ResultSet rs3 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM borrowings WHERE status='borrowed'");
            if (rs3.next()) borrowedLabel.setText(String.valueOf(rs3.getInt(1)));

            // رسم بياني
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Library Stats");
            series.getData().add(new XYChart.Data<>("Total Books",   Integer.parseInt(totalBooksLabel.getText())));
            series.getData().add(new XYChart.Data<>("Total Members", Integer.parseInt(totalMembersLabel.getText())));
            series.getData().add(new XYChart.Data<>("Borrowed",      Integer.parseInt(borrowedLabel.getText())));
            statsChart.getData().add(series);

            rs1.close();
            rs2.close();
            rs3.close();

        } catch (SQLException e) {
            showAlert("Database Error", "Could not load dashboard statistics:\n" + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh() {
        loadStatistics();
        showAlert("Dashboard Refreshed", "The latest library statistics have been loaded successfully.");
    }

    @FXML
    public void showReport() {
        String report = """
                Library Summary Report

                Total Books: %s
                Total Members: %s
                Currently Borrowed: %s
                """.formatted(
                totalBooksLabel.getText(),
                totalMembersLabel.getText(),
                borrowedLabel.getText()
        );

        showAlert("Library Report", report);
    }

    @FXML public void goHome()    { SceneManager.switchScene("home.fxml"); }
    @FXML public void goBooks()   { SceneManager.switchScene("books.fxml"); }
    @FXML public void goMembers() { SceneManager.switchScene("members.fxml"); }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}