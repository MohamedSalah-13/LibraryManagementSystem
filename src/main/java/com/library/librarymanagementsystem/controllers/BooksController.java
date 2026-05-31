package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.database.DatabaseConnection;
import com.library.librarymanagementsystem.models.Book;
import com.library.librarymanagementsystem.utils.SceneManager;
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
        if (!SceneManager.requireLogin()) return;

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
        if (titleField.getText().trim().isEmpty() || authorField.getText().trim().isEmpty()) {
            showStatus("⚠ Title and Author are required.");
            return false;
        }

        if (!isInteger(quantityField.getText(), true)) {
            showStatus("⚠ Quantity must be a valid number.");
            showErrorAlert("Invalid Quantity", "Please enter a valid numeric value for quantity.");
            return false;
        }

        if (!isDouble(priceField.getText(), true)) {
            showStatus("⚠ Price must be a valid number.");
            showErrorAlert("Invalid Price", "Please enter a valid numeric value for price.");
            return false;
        }

        if (!isInteger(yearField.getText(), true)) {
            showStatus("⚠ Published year must be a valid number.");
            showErrorAlert("Invalid Year", "Please enter a valid numeric value for published year.");
            return false;
        }

        return true;
    }

    private boolean isInteger(String value, boolean allowEmpty) {
        if (value == null || value.trim().isEmpty()) return allowEmpty;
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String value, boolean allowEmpty) {
        if (value == null || value.trim().isEmpty()) return allowEmpty;
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setStatementValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, titleField.getText().trim());
        ps.setString(2, authorField.getText().trim());
        ps.setString(3, isbnField.getText().trim());
        ps.setString(4, categoryField.getText().trim());
        ps.setInt(5, Integer.parseInt(quantityField.getText().trim().isEmpty() ? "1" : quantityField.getText().trim()));
        ps.setInt(6, Integer.parseInt(quantityField.getText().trim().isEmpty() ? "1" : quantityField.getText().trim()));
        ps.setDouble(7, Double.parseDouble(priceField.getText().trim().isEmpty() ? "0" : priceField.getText().trim()));
        ps.setInt(8, Integer.parseInt(yearField.getText().trim().isEmpty() ? "2024" : yearField.getText().trim()));
    }

    private void showStatus(String msg) {
        statusLabel.setText(msg);
    }
}
