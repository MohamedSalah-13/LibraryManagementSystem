package com.library.librarymanagementsystem.utils;

import com.library.librarymanagementsystem.Main;
import com.library.librarymanagementsystem.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class SceneManager {

    // المستخدم الحالي بعد تسجيل الدخول
    public static User currentUser = null;

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource(fxmlFile)
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Main.class.getResource("styles.css").toExternalForm()
            );
            Main.primaryStage.setScene(scene);
            Main.primaryStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
