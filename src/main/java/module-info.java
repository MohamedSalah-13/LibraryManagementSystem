module com.library.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j; // verify exact automatic module name

    opens com.library.librarymanagementsystem to javafx.fxml;
    opens com.library.librarymanagementsystem.controllers to javafx.fxml;

    exports com.library.librarymanagementsystem;
    exports com.library.librarymanagementsystem.models;
    exports com.library.librarymanagementsystem.utils;
    exports com.library.librarymanagementsystem.database;
}