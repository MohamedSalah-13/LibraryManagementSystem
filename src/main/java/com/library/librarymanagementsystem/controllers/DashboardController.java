package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.database.DatabaseConnection;
import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.sql.*;

public class DashboardController {

    @FXML private Label    totalBooksLabel;
    @FXML private Label    totalMembersLabel;
    @FXML private Label    borrowedLabel;
    @FXML private BarChart<String, Number> statsChart;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            Connection conn = DatabaseConnection.getConnection();

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML public void goHome() { SceneManager.switchScene("home.fxml"); }
}
