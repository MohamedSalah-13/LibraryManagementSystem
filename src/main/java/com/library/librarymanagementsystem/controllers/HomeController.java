package com.library.librarymanagementsystem.controllers;


import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class HomeController {

    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;

    @FXML
    public void initialize() {
        if (SceneManager.currentUser != null) {
            welcomeLabel.setText("Welcome, " + SceneManager.currentUser.getFullName() + " ");
            roleLabel.setText("Role: " + SceneManager.currentUser.getRole());
        }
    }

    @FXML public void goToDashboard()  { SceneManager.switchScene("dashboard.fxml"); }
    @FXML public void goToBooks()      { SceneManager.switchScene("books.fxml");     }
    @FXML public void goToMembers()    { SceneManager.switchScene("members.fxml");   }
    @FXML public void goToProfile()    { SceneManager.switchScene("profile.fxml");   }

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