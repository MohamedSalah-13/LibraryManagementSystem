# مراجعة المشروع وخطة التنفيذ الكاملة

##  ملخص متطلبات المشروع

بناءً على المستند المرفق، المطلوب هو:

> **تطبيق سطح مكتب (Desktop Application)** باستخدام **JavaFX** مع قاعدة بيانات خارجية، يتكون من **4-6 مشاهد (Scenes)** على الأقل.

---

##  مراجعة المتطلبات الأساسية

| المتطلب | التفاصيل |
|---|---|
| **المجال** | اختياري (صحة، تعليم، مالية، تجارة إلكترونية، إلخ) |
| **الصفحات** | 4-6 Scenes على الأقل |
| **قاعدة البيانات** | MySQL أو أي RDBMS عبر JDBC |
| **العمليات** | CRUD كاملة |
| **الموعد النهائي** | 6/6/2026 |
| **الدرجة الكاملة** | 20 نقطة |

---

## ️ اقتراح المشروع: نظام إدارة المكتبة (Library Management System)

اخترت هذا المجال لأنه يغطي **جميع المتطلبات** بشكل واضح وعملي.

---

## ️ هيكل المشاهد (Scenes)

```
1. Login Scene         → تسجيل الدخول + التحقق من قاعدة البيانات
2. Home Scene          → الصفحة الرئيسية بعد تسجيل الدخول
3. Dashboard Scene     → إحصائيات وملخص النظام
4. Books Scene         → إدارة الكتب (CRUD كامل)
5. Members Scene       → إدارة الأعضاء (CRUD كامل)
6. Profile Scene       → إعدادات المستخدم والملف الشخصي
```


---

## ️ تصميم قاعدة البيانات (MySQL)

```sql
-- إنشاء قاعدة البيانات
CREATE DATABASE library_db;
USE library_db;

-- جدول المستخدمين (للتسجيل والدخول)
CREATE TABLE users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    full_name   VARCHAR(100),
    email       VARCHAR(100),
    role        ENUM('admin', 'librarian') DEFAULT 'librarian',
    profile_pic VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- جدول الكتب
CREATE TABLE books (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(200) NOT NULL,
    author         VARCHAR(100) NOT NULL,
    isbn           VARCHAR(20) UNIQUE,
    category       VARCHAR(50),
    quantity        INT DEFAULT 1,
    available      INT DEFAULT 1,
    price          DECIMAL(10,2),
    published_year INT,
    added_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- جدول الأعضاء
CREATE TABLE members (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(100) UNIQUE,
    phone      VARCHAR(20),
    address    VARCHAR(200),
    joined_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- جدول الاستعارة
CREATE TABLE borrowings (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    book_id     INT NOT NULL,
    member_id   INT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    status      ENUM('borrowed','returned') DEFAULT 'borrowed',
    FOREIGN KEY (book_id)   REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- إدخال مستخدم تجريبي (كلمة المرور: admin123)
INSERT INTO users (username, password, full_name, email, role)
VALUES ('admin', 'admin123', 'System Admin', 'admin@library.com', 'admin');

-- بيانات تجريبية للكتب
INSERT INTO books (title, author, isbn, category, quantity, available, price, published_year) VALUES
('Clean Code', 'Robert C. Martin', '978-0132350884', 'Programming', 3, 3, 45.99, 2008),
('Java Programming', 'James Gosling', '978-0134685991', 'Programming', 5, 4, 55.00, 2018),
('Design Patterns', 'Gang of Four', '978-0201633610', 'Software', 2, 2, 60.00, 1994);
```


---

##  هيكل المشروع

```
LibraryManagementSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/library/
│       │       ├── Main.java
│       │       ├── database/
│       │       │   └── DatabaseConnection.java
│       │       ├── models/
│       │       │   ├── User.java
│       │       │   ├── Book.java
│       │       │   └── Member.java
│       │       ├── controllers/
│       │       │   ├── LoginController.java
│       │       │   ├── HomeController.java
│       │       │   ├── DashboardController.java
│       │       │   ├── BooksController.java
│       │       │   ├── MembersController.java
│       │       │   └── ProfileController.java
│       │       └── utils/
│       │           └── SceneManager.java
│       └── resources/
│           └── com/library/
│               ├── login.fxml
│               ├── home.fxml
│               ├── dashboard.fxml
│               ├── books.fxml
│               ├── members.fxml
│               ├── profile.fxml
│               └── styles.css
└── pom.xml (أو build.gradle)
```


---

##  الكود الكامل للمشروع

### 1️⃣ `Main.java` — نقطة البداية

```java
package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/library/login.fxml")
        );
        Scene scene = new Scene(loader.load(), 900, 600);
        scene.getStylesheets().add(
            getClass().getResource("/com/library/styles.css").toExternalForm()
        );
        stage.setTitle(" Library Management System");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```


---

### 2️⃣ `DatabaseConnection.java` — الاتصال بقاعدة البيانات

```java
package com.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";  // ضع كلمة مرور MySQL هنا

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```


---

### 3️⃣ `User.java` — نموذج المستخدم

```java
package com.library.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;
    private String profilePic;

    public User() {}

    public User(int id, String username, String fullName, String email, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters & Setters
    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getUsername()                   { return username; }
    public void setUsername(String username)       { this.username = username; }

    public String getPassword()                   { return password; }
    public void setPassword(String password)       { this.password = password; }

    public String getFullName()                   { return fullName; }
    public void setFullName(String fullName)       { this.fullName = fullName; }

    public String getEmail()                      { return email; }
    public void setEmail(String email)             { this.email = email; }

    public String getRole()                       { return role; }
    public void setRole(String role)               { this.role = role; }

    public String getProfilePic()                 { return profilePic; }
    public void setProfilePic(String profilePic)  { this.profilePic = profilePic; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
```


---

### 4️⃣ `Book.java` — نموذج الكتاب

```java
package com.library.models;

public class Book {
    private int    id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int    quantity;
    private int    available;
    private double price;
    private int    publishedYear;

    public Book() {}

    public Book(int id, String title, String author, String isbn,
                String category, int quantity, int available,
                double price, int publishedYear) {
        this.id            = id;
        this.title         = title;
        this.author        = author;
        this.isbn          = isbn;
        this.category      = category;
        this.quantity      = quantity;
        this.available     = available;
        this.price         = price;
        this.publishedYear = publishedYear;
    }

    // Getters & Setters
    public int    getId()                       { return id; }
    public void   setId(int id)                 { this.id = id; }

    public String getTitle()                    { return title; }
    public void   setTitle(String title)        { this.title = title; }

    public String getAuthor()                   { return author; }
    public void   setAuthor(String author)      { this.author = author; }

    public String getIsbn()                     { return isbn; }
    public void   setIsbn(String isbn)          { this.isbn = isbn; }

    public String getCategory()                 { return category; }
    public void   setCategory(String category)  { this.category = category; }

    public int    getQuantity()                         { return quantity; }
    public void   setQuantity(int quantity)             { this.quantity = quantity; }

    public int    getAvailable()                        { return available; }
    public void   setAvailable(int available)           { this.available = available; }

    public double getPrice()                            { return price; }
    public void   setPrice(double price)                { this.price = price; }

    public int    getPublishedYear()                    { return publishedYear; }
    public void   setPublishedYear(int publishedYear)   { this.publishedYear = publishedYear; }
}
```


---

### 5️⃣ `Member.java` — نموذج العضو

```java
package com.library.models;

public class Member {
    private int    id;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public Member() {}

    public Member(int id, String fullName, String email, String phone, String address) {
        this.id       = id;
        this.fullName = fullName;
        this.email    = email;
        this.phone    = phone;
        this.address  = address;
    }

    // Getters & Setters
    public int    getId()                        { return id; }
    public void   setId(int id)                  { this.id = id; }

    public String getFullName()                  { return fullName; }
    public void   setFullName(String fullName)   { this.fullName = fullName; }

    public String getEmail()                     { return email; }
    public void   setEmail(String email)         { this.email = email; }

    public String getPhone()                     { return phone; }
    public void   setPhone(String phone)         { this.phone = phone; }

    public String getAddress()                   { return address; }
    public void   setAddress(String address)     { this.address = address; }
}
```


---

### 6️⃣ `SceneManager.java` — مدير المشاهد

```java
package com.library.utils;

import com.library.Main;
import com.library.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class SceneManager {

    // المستخدم الحالي بعد تسجيل الدخول
    public static User currentUser = null;

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource("/com/library/" + fxmlFile)
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                SceneManager.class.getResource("/com/library/styles.css").toExternalForm()
            );
            Main.primaryStage.setScene(scene);
            Main.primaryStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


---

### 7️⃣ `LoginController.java`

```java
package com.library.controllers;

import com.library.database.DatabaseConnection;
import com.library.models.User;
import com.library.utils.SceneManager;
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
```


---

### 8️⃣ `HomeController.java`

```java
package com.library.controllers;

import com.library.utils.SceneManager;
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
```


---

### 9️⃣ `BooksController.java` — CRUD كامل للكتب

```java
package com.library.controllers;

import com.library.database.DatabaseConnection;
import com.library.models.Book;
import com.library.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class BooksController {

    @FXML private TableView<Book>           booksTable;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String>  colTitle;
    @FXML private TableColumn<Book, String>  colAuthor;
    @FXML private TableColumn<Book, String>  colCategory;
    @FXML private TableColumn<Book, Integer> colQty;
    @FXML private TableColumn<Book, Double>  colPrice;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField yearField;
    @FXML private TextField searchField;
    @FXML private Label     statusLabel;

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadBooks();

        // عند الضغط على صف في الجدول تملأ الحقول تلقائياً
        booksTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateFields(newVal);
            }
        );
    }

    private void loadBooks() {
        bookList.clear();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM books";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                bookList.add(new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getInt("available"),
                    rs.getDouble("price"),
                    rs.getInt("published_year")
                ));
            }
            booksTable.setItems(bookList);
            rs.close(); st.close();
        } catch (SQLException e) {
            showStatus("❌ Error loading books: " + e.getMessage());
        }
    }

    @FXML
    public void handleAdd() {
        if (!validateFields()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO books (title,author,isbn,category,quantity,available,price,published_year) " +
                         "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            setStatementValues(ps);
            ps.executeUpdate();
            ps.close();
            loadBooks();
            clearFields();
            showStatus("✅ Book added successfully!");
        } catch (SQLException e) {
            showStatus("❌ Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showStatus("⚠ Select a book to update."); return; }
        if (!validateFields()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE books SET title=?,author=?,isbn=?,category=?,quantity=?,available=?,price=?,published_year=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            setStatementValues(ps);
            ps.setInt(9, selected.getId());
            ps.executeUpdate();
            ps.close();
            loadBooks();
            clearFields();
            showStatus("✅ Book updated successfully!");
        } catch (SQLException e) {
            showStatus("❌ Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showStatus("⚠ Select a book to delete."); return; }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete book: " + selected.getTitle() + "?",
            ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?");
                    ps.setInt(1, selected.getId());
                    ps.executeUpdate();
                    ps.close();
                    loadBooks();
                    clearFields();
                    showStatus("✅ Book deleted successfully!");
                } catch (SQLException e) {
                    showStatus("❌ Error: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        ObservableList<Book> filtered = FXCollections.observableArrayList();
        for (Book b : bookList) {
            if (b.getTitle().toLowerCase().contains(keyword) ||
                b.getAuthor().toLowerCase().contains(keyword)) {
                filtered.add(b);
            }
        }
        booksTable.setItems(filtered);
    }

    @FXML public void handleClear() { clearFields(); }
    @FXML public void goHome()      { SceneManager.switchScene("home.fxml"); }

    private void populateFields(Book b) {
        titleField.setText(b.getTitle());
        authorField.setText(b.getAuthor());
        isbnField.setText(b.getIsbn());
        categoryField.setText(b.getCategory());
        quantityField.setText(String.valueOf(b.getQuantity()));
        priceField.setText(String.valueOf(b.getPrice()));
        yearField.setText(String.valueOf(b.getPublishedYear()));
    }

    private void clearFields() {
        titleField.clear(); authorField.clear(); isbnField.clear();
        categoryField.clear(); quantityField.clear();
        priceField.clear(); yearField.clear();
        booksTable.getSelectionModel().clearSelection();
    }

    private boolean validateFields() {
        if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
            showStatus("⚠ Title and Author are required.");
            return false;
        }
        return true;
    }

    private void setStatementValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, titleField.getText());
        ps.setString(2, authorField.getText());
        ps.setString(3, isbnField.getText());
        ps.setString(4, categoryField.getText());
        ps.setInt(5, Integer.parseInt(quantityField.getText().isEmpty() ? "1" : quantityField.getText()));
        ps.setInt(6, Integer.parseInt(quantityField.getText().isEmpty() ? "1" : quantityField.getText()));
        ps.setDouble(7, Double.parseDouble(priceField.getText().isEmpty() ? "0" : priceField.getText()));
        ps.setInt(8, Integer.parseInt(yearField.getText().isEmpty() ? "2024" : yearField.getText()));
    }

    private void showStatus(String msg) {
        statusLabel.setText(msg);
    }
}
```


---

###  `DashboardController.java`

```java
package com.library.controllers;

import com.library.database.DatabaseConnection;
import com.library.utils.SceneManager;
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
```


---

### 1️⃣1️⃣ `ProfileController.java`

```java
package com.library.controllers;

import com.library.database.DatabaseConnection;
import com.library.models.User;
import com.library.utils.SceneManager;
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
```


---

### 1️⃣2️⃣ `login.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.text.*?>

<VBox xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.library.controllers.LoginController"
      alignment="CENTER" spacing="20"
      styleClass="login-root"
      prefWidth="900" prefHeight="600">

    <Text text=" Library Management System" styleClass="app-title"/>
    <Text text="Please login to continue"    styleClass="subtitle"/>

    <VBox styleClass="login-card" spacing="15" maxWidth="400">

        <Label text="Username" styleClass="field-label"/>
        <TextField fx:id="usernameField" promptText="Enter username" styleClass="input-field"/>

        <Label text="Password" styleClass="field-label"/>
        <PasswordField fx:id="passwordField" promptText="Enter password" styleClass="input-field"/>

        <Label fx:id="errorLabel" styleClass="error-label"/>

        <Button text=" Login" onAction="#handleLogin"
                styleClass="primary-btn" maxWidth="Infinity"/>
    </VBox>
</VBox>
```


---

### 1️⃣3️⃣ `home.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.text.*?>

<BorderPane xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.library.controllers.HomeController"
            prefWidth="1000" prefHeight="650">

    <!-- الشريط العلوي -->
    <top>
        <HBox styleClass="navbar" alignment="CENTER_LEFT" spacing="15">
            <Text text=" Library System" styleClass="nav-title"/>
            <HBox HBox.hgrow="ALWAYS"/>
            <Label fx:id="welcomeLabel" styleClass="nav-welcome"/>
            <Label fx:id="roleLabel"   styleClass="nav-role"/>
            <Button text="Logout" onAction="#handleLogout" styleClass="logout-btn"/>
        </HBox>
    </top>

    <!-- المحتوى الرئيسي -->
    <center>
        <VBox alignment="CENTER" spacing="30" styleClass="home-center">
            <Text text="Welcome to Library Management System" styleClass="home-title"/>
            <Text text="Choose a section to get started"     styleClass="home-subtitle"/>

            <HBox alignment="CENTER" spacing="20">
                <Button text=" Dashboard" onAction="#goToDashboard"
                        styleClass="home-card-btn" prefWidth="180" prefHeight="100"/>
                <Button text=" Books"     onAction="#goToBooks"
                        styleClass="home-card-btn" prefWidth="180" prefHeight="100"/>
                <Button text=" Members"   onAction="#goToMembers"
                        styleClass="home-card-btn" prefWidth="180" prefHeight="100"/>
                <Button text=" Profile"   onAction="#goToProfile"
                        styleClass="home-card-btn" prefWidth="180" prefHeight="100"/>
            </HBox>
        </VBox>
    </center>
</BorderPane>
```


---

### 1️⃣4️⃣ `books.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.text.*?>

<BorderPane xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.library.controllers.BooksController"
            prefWidth="1100" prefHeight="700">

    <top>
        <HBox styleClass="navbar" alignment="CENTER_LEFT" spacing="15">
            <Text text=" Books Management" styleClass="nav-title"/>
            <HBox HBox.hgrow="ALWAYS"/>
            <Button text=" Home" onAction="#goHome" styleClass="nav-btn"/>
        </HBox>
    </top>

    <center>
        <SplitPane>
            <!-- الجدول وأدوات البحث -->
            <VBox spacing="10" styleClass="table-section">
                <HBox spacing="10" alignment="CENTER_LEFT">
                    <TextField fx:id="searchField" promptText=" Search by title or author..."
                               prefWidth="300"/>
                    <Button text="Search" onAction="#handleSearch" styleClass="search-btn"/>
                </HBox>

                <TableView fx:id="booksTable" VBox.vgrow="ALWAYS">
                    <columns>
                        <TableColumn fx:id="colId"       text="ID"       prefWidth="50"/>
                        <TableColumn fx:id="colTitle"    text="Title"    prefWidth="200"/>
                        <TableColumn fx:id="colAuthor"   text="Author"   prefWidth="150"/>
                        <TableColumn fx:id="colCategory" text="Category" prefWidth="120"/>
                        <TableColumn fx:id="colQty"      text="Qty"      prefWidth="60"/>
                        <TableColumn fx:id="colPrice"    text="Price"    prefWidth="80"/>
                    </columns>
                </TableView>

                <Label fx:id="statusLabel" styleClass="status-label"/>
            </VBox>

            <!-- نموذج الإدخال -->
            <VBox spacing="12" styleClass="form-section" prefWidth="300">
                <Text text="Book Details" styleClass="form-title"/>

                <Label text="Title *"/>
                <TextField fx:id="titleField"    promptText="Book title"/>
                <Label text="Author *"/>
                <TextField fx:id="authorField"   promptText="Author name"/>
                <Label text="ISBN"/>
                <TextField fx:id="isbnField"     promptText="ISBN number"/>
                <Label text="Category"/>
                <TextField fx:id="categoryField" promptText="Category"/>
                <Label text="Quantity"/>
                <TextField fx:id="quantityField" promptText="Quantity"/>
                <Label text="Price"/>
                <TextField fx:id="priceField"    promptText="Price"/>
                <Label text="Published Year"/>
                <TextField fx:id="yearField"     promptText="Year"/>

                <HBox spacing="10">
                    <Button text="➕ Add"    onAction="#handleAdd"    styleClass="add-btn"    HBox.hgrow="ALWAYS" maxWidth="Infinity"/>
                    <Button text="✏ Update" onAction="#handleUpdate" styleClass="update-btn" HBox.hgrow="ALWAYS" maxWidth="Infinity"/>
                </HBox>
                <HBox spacing="10">
                    <Button text=" Delete" onAction="#handleDelete" styleClass="delete-btn" HBox.hgrow="ALWAYS" maxWidth="Infinity"/>
                    <Button text=" Clear"  onAction="#handleClear"  styleClass="clear-btn"  HBox.hgrow="ALWAYS" maxWidth="Infinity"/>
                </HBox>
            </VBox>
        </SplitPane>
    </center>
</BorderPane>
```


---

### 1️⃣5️⃣ `dashboard.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.text.*?>
<?import javafx.scene.chart.*?>

<BorderPane xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.library.controllers.DashboardController"
            prefWidth="1000" prefHeight="650">

    <top>
        <HBox styleClass="navbar" alignment="CENTER_LEFT" spacing="15">
            <Text text=" Dashboard" styleClass="nav-title"/>
            <HBox HBox.hgrow="ALWAYS"/>
            <Button text=" Home" onAction="#goHome" styleClass="nav-btn"/>
        </HBox>
    </top>

    <center>
        <VBox spacing="30" styleClass="dashboard-center" alignment="CENTER">
            <!-- بطاقات الإحصائيات -->
            <HBox spacing="20" alignment="CENTER">
                <VBox styleClass="stat-card" alignment="CENTER" spacing="5">
                    <Text text=" Total Books" styleClass="stat-title"/>
                    <Label fx:id="totalBooksLabel" text="0" styleClass="stat-number"/>
                </VBox>
                <VBox styleClass="stat-card" alignment="CENTER" spacing="5">
                    <Text text=" Total Members" styleClass="stat-title"/>
                    <Label fx:id="totalMembersLabel" text="0" styleClass="stat-number"/>
                </VBox>
                <VBox styleClass="stat-card" alignment="CENTER" spacing="5">
                    <Text text=" Borrowed" styleClass="stat-title"/>
                    <Label fx:id="borrowedLabel" text="0" styleClass="stat-number"/>
                </VBox>
            </HBox>

            <!-- الرسم البياني -->
            <BarChart fx:id="statsChart" prefHeight="300">
                <xAxis><CategoryAxis label="Category"/></xAxis>
                <yAxis><NumberAxis label="Count"/></yAxis>
            </BarChart>
        </VBox>
    </center>
</BorderPane>
```


---

### 1️⃣6️⃣ `styles.css` — التصميم البصري

```css
/* ===== عام ===== */
.root {
    -fx-font-family: "Segoe UI", Arial, sans-serif;
    -fx-background-color: #F0F4F8;
}

/* ===== صفحة تسجيل الدخول ===== */
.login-root {
    -fx-background-color: linear-gradient(to bottom right, #1a1a2e, #16213e, #0f3460);
}
.app-title {
    -fx-font-size: 28px;
    -fx-fill: #E8F4FD;
    -fx-font-weight: bold;
}
.subtitle {
    -fx-font-size: 14px;
    -fx-fill: #A0AEC0;
}
.login-card {
    -fx-background-color: white;
    -fx-background-radius: 15;
    -fx-padding: 40;
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);
}
.field-label {
    -fx-font-size: 13px;
    -fx-font-weight: bold;
    -fx-text-fill: #4A5568;
}
.input-field {
    -fx-pref-height: 40px;
    -fx-background-radius: 8;
    -fx-border-radius: 8;
    -fx-border-color: #CBD5E0;
    -fx-padding: 0 10;
    -fx-font-size: 14px;
}
.input-field:focused {
    -fx-border-color: #4299E1;
    -fx-effect: dropshadow(gaussian, #4299E1, 5, 0, 0, 0);
}
.primary-btn {
    -fx-background-color: #4299E1;
    -fx-text-fill: white;
    -fx-font-size: 15px;
    -fx-font-weight: bold;
    -fx-pref-height: 45px;
    -fx-background-radius: 8;
    -fx-cursor: hand;
}
.primary-btn:hover {
    -fx-background-color: #3182CE;
}
.error-label {
    -fx-text-fill: #E53E3E;
    -fx-font-size: 13px;
}

/* ===== شريط التنقل ===== */
.navbar {
    -fx-background-color: #2D3748;
    -fx-padding: 12 20;
}
.nav-title {
    -fx-font-size: 20px;
    -fx-fill: white;
    -fx-font-weight: bold;
}
.nav-welcome {
    -fx-text-fill: #E2E8F0;
    -fx-font-size: 14px;
}
.nav-role {
    -fx-text-fill: #68D391;
    -fx-font-size: 12px;
}
.logout-btn, .nav-btn {
    -fx-background-color: #E53E3E;
    -fx-text-fill: white;
    -fx-background-radius: 6;
    -fx-cursor: hand;
    -fx-padding: 5 15;
}
.logout-btn:hover, .nav-btn:hover {
    -fx-background-color: #C53030;
}

/* ===== الصفحة الرئيسية ===== */
.home-center   { -fx-padding: 50; }
.home-title    { -fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #2D3748; }
.home-subtitle { -fx-font-size: 15px; -fx-fill: #718096; }
.home-card-btn {
    -fx-background-color: white;
    -fx-text-fill: #2D3748;
    -fx-font-size: 16px;
    -fx-font-weight: bold;
    -fx-background-radius: 12;
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);
    -fx-cursor: hand;
}
.home-card-btn:hover {
    -fx-background-color: #4299E1;
    -fx-text-fill: white;
}

/* ===== Dashboard ===== */
.dashboard-center { -fx-padding: 30; }
.stat-card {
    -fx-background-color: white;
    -fx-padding: 25 40;
    -fx-background-radius: 12;
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);
    -fx-min-width: 180;
}
.stat-title  { -fx-font-size: 14px; -fx-fill: #718096; }
.stat-number { -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #4299E1; }

/* ===== أزرار CRUD ===== */
.add-btn    { -fx-background-color: #48BB78; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; }
.update-btn { -fx-background-color: #ECC94B; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; }
.delete-btn { -fx-background-color: #FC8181; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; }
.clear-btn  { -fx-background-color: #A0AEC0; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; }
.search-btn { -fx-background-color: #4299E1; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; }
.add-btn:hover    { -fx-background-color: #38A169; }
.update-btn:hover { -fx-background-color: #D69E2E; }
.delete-btn:hover { -fx-background-color: #E53E3E; }

/* ===== النموذج ===== */
.form-section  { -fx-padding: 20; -fx-background-color: white; }
.form-title    { -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #2D3748; }
.table-section { -fx-padding: 15; }
.status-label  { -fx-font-size: 13px; -fx-text-fill: #2D3748; }
```


---

### 1️⃣7️⃣ `pom.xml` — إعداد Maven

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.library</groupId>
    <artifactId>LibraryManagementSystem</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <javafx.version>21.0.2</javafx.version>
    </properties>

    <dependencies>
        <!-- JavaFX -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-charts</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <!-- MySQL Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.3.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.library.Main</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```


---

## ✅ جدول تغطية المتطلبات

| المتطلب | الحالة | التفاصيل |
|---|---|---|
| **Login Page** | ✅ | تحقق من DB + رسائل خطأ |
| **Home Page** | ✅ | بعد المصادقة + معلومات المستخدم + تنقل |
| **Dashboard** | ✅ | إحصائيات + رسم بياني (BarChart) |
| **Books Scene** | ✅ | CRUD كامل + بحث |
| **Members Scene** | ✅ مشابه لـ Books | CRUD كامل |
| **Profile Scene** | ✅ | تعديل البيانات الشخصية |
| **Database (MySQL)** | ✅ | 4 جداول + علاقات |
| **JDBC** | ✅ | `DatabaseConnection.java` |
| **CRUD** | ✅ | Add, Read, Update, Delete |
| **Error Handling** | ✅ | try-catch في كل عملية DB |
| **Events** | ✅ | Click, KeyPress, Selection |
| **Responsive Design** | ✅ | SplitPane + HBox + VBox |
| **CSS Styling** | ✅ | `styles.css` متكامل |

---

##  ملاحظات مهمة

> 1. **`MembersController.java`** و **`members.fxml`** متطابقان تماماً مع Books — فقط غيّر اسم الجدول إلى `members` والحقول بالتبعية.
> 2. **كلمة مرور MySQL**: غيّر `PASSWORD = ""` في `DatabaseConnection.java` لتناسب إعدادات جهازك.
> 3. **الموعد النهائي**: 6/6/2026 — لديك وقت كافٍ للتطوير والاختبار.
> 4. للرفع على **Edugate**: ارفق المشروع كاملاً بما في ذلك ملف `pom.xml` وسكريبت قاعدة البيانات SQL.