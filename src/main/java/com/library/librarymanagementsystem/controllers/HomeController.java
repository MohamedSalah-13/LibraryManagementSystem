package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.Main;
import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeController {

    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label windowSizeLabel;

    @FXML
    public void initialize() {
        if (!SceneManager.requireLogin()) return;

        if (SceneManager.currentUser != null) {
            welcomeLabel.setText("Welcome, " + SceneManager.currentUser.getFullName() + " ");
            roleLabel.setText("Role: " + SceneManager.currentUser.getRole());
        }

        updateDateTime();

        if (Main.primaryStage != null) {
            updateWindowSize();
            Main.primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> updateWindowSize());
            Main.primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> updateWindowSize());
        }
    }

    private void updateDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dateTimeLabel.setText("Today: " + LocalDateTime.now().format(formatter));
    }

    private void updateWindowSize() {
        int width = (int) Main.primaryStage.getWidth();
        int height = (int) Main.primaryStage.getHeight();
        windowSizeLabel.setText("Window Size: " + width + " x " + height);
    }

    @FXML public void goToDashboard()  { SceneManager.switchScene("dashboard.fxml"); }
    @FXML public void goToBooks()      { SceneManager.switchScene("books.fxml");     }
    @FXML public void goToMembers()    { SceneManager.switchScene("members.fxml");   }
    @FXML public void goToProfile()    { SceneManager.switchScene("profile.fxml");   }

    @FXML
    public void handleToggleTheme() {
        SceneManager.toggleTheme();
    }

    @FXML
    public void handleFullScreen() {
        Main.primaryStage.setFullScreen(!Main.primaryStage.isFullScreen());
    }

    @FXML
    public void showNotifications() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifications");
        alert.setHeaderText("Library Updates");
        alert.setContentText("Welcome back! Check the dashboard for the latest library statistics.");
        alert.showAndWait();
    }

    @FXML
    public void handleLogout() {
        SceneManager.currentUser = null;
        SceneManager.switchScene("login.fxml");
    }

    // مثال على حدث تحريك الفأرة
    @FXML
    public void handleMouseEnter(MouseEvent event) {
        System.out.println("Mouse entered: " + event.getSource());
    }
}