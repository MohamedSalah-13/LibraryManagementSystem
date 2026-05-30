package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.database.DatabaseConnection;
import com.library.librarymanagementsystem.models.User;
import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.*;

public class LoginController {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;
    @FXML private Button        loginButton;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        // الضغط على Enter يُشغّل تسجيل الدخول
        passwordField.setOnKeyPressed(this::handleEnterKey);
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) handleLogin();
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // التحقق من الحقول الفارغة
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠ Please fill in all fields.");
            return;
        }

        // التحقق من قاعدة البيانات
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // تخزين بيانات المستخدم
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                SceneManager.currentUser = user;
                errorLabel.setText("");
                SceneManager.switchScene("home.fxml");
            } else {
                errorLabel.setText("❌ Invalid username or password.");
                passwordField.clear();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            errorLabel.setText("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
