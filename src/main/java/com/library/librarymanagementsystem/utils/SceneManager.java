package com.library.librarymanagementsystem.utils;

import com.library.librarymanagementsystem.Main;
import com.library.librarymanagementsystem.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class SceneManager {

    // المستخدم الحالي بعد تسجيل الدخول
    public static User currentUser = null;
    public static boolean darkMode = false;

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource(fxmlFile)
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            applyTheme(scene);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean requireLogin() {
        if (currentUser == null) {
            switchScene("login.fxml");
            return false;
        }
        return true;
    }

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
                Main.class.getResource("styles.css").toExternalForm()
        );

        if (darkMode) {
            scene.getRoot().getStyleClass().add("dark-root");
        }
    }

    public static void toggleTheme() {
        darkMode = !darkMode;
        if (Main.primaryStage != null && Main.primaryStage.getScene() != null) {
            applyTheme(Main.primaryStage.getScene());
        }
    }
}