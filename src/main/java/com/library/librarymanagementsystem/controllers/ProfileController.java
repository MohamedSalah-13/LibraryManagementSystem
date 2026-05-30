package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.database.DatabaseConnection;
import com.library.librarymanagementsystem.models.User;
import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class ProfileController {

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        User u = SceneManager.currentUser;
        if (u != null) {
            usernameField.setText(u.getUsername());
            usernameField.setDisable(true);   // لا يمكن تغيير اسم المستخدم
            fullNameField.setText(u.getFullName());
            emailField.setText(u.getEmail());
        }
    }

    @FXML
    public void handleSave() {
        String fullName = fullNameField.getText().trim();
        String email    = emailField.getText().trim();
        String newPass  = newPasswordField.getText().trim();

        if (fullName.isEmpty() || email.isEmpty()) {
            statusLabel.setText("⚠ Name and Email are required.");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql;
            PreparedStatement ps;

            if (!newPass.isEmpty()) {
                sql = "UPDATE users SET full_name=?, email=?, password=? WHERE id=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setString(3, newPass);
                ps.setInt(4, SceneManager.currentUser.getId());
            } else {
                sql = "UPDATE users SET full_name=?, email=? WHERE id=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setInt(3, SceneManager.currentUser.getId());
            }

            ps.executeUpdate();
            ps.close();

            // تحديث البيانات في الذاكرة
            SceneManager.currentUser.setFullName(fullName);
            SceneManager.currentUser.setEmail(email);

            statusLabel.setText("✅ Profile updated successfully!");
        } catch (SQLException e) {
            statusLabel.setText("❌ Error: " + e.getMessage());
        }
    }

    @FXML public void goHome() { SceneManager.switchScene("home.fxml"); }
}
