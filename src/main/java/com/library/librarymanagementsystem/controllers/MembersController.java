package com.library.librarymanagementsystem.controllers;

import com.library.librarymanagementsystem.database.DatabaseConnection;
import com.library.librarymanagementsystem.models.Member;
import com.library.librarymanagementsystem.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class MembersController {

    private final ObservableList<Member> memberList = FXCollections.observableArrayList();
    @FXML
    private TableView<Member> membersTable;
    @FXML
    private TableColumn<Member, Integer> colId;
    @FXML
    private TableColumn<Member, String> colName;
    @FXML
    private TableColumn<Member, String> colEmail;
    @FXML
    private TableColumn<Member, String> colPhone;
    @FXML
    private TableColumn<Member, String> colAddress;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField searchField;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        if (!SceneManager.requireLogin()) return;

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadMembers();

        // عند الضغط على صف في الجدول تملأ الحقول تلقائياً
        membersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) populateFields(newVal);
                }
        );
    }

    private void loadMembers() {
        memberList.clear();
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM members");
            while (rs.next()) {
                memberList.add(new Member(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
            membersTable.setItems(memberList);
            rs.close();
            st.close();
        } catch (SQLException e) {
            showStatus("❌ Error loading members: " + e.getMessage());
        }
    }

    @FXML
    public void handleAdd() {
        if (!validateFields()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO members (full_name, email, phone, address) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            setStatementValues(ps);
            ps.executeUpdate();
            ps.close();
            loadMembers();
            clearFields();
            showStatus("✅ Member added successfully!");
        } catch (SQLException e) {
            showStatus("❌ Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate() {
        Member selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("⚠ Select a member to update.");
            return;
        }
        if (!validateFields()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE members SET full_name=?, email=?, phone=?, address=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            setStatementValues(ps);
            ps.setInt(5, selected.getId());
            ps.executeUpdate();
            ps.close();
            loadMembers();
            clearFields();
            showStatus("✅ Member updated successfully!");
        } catch (SQLException e) {
            showStatus("❌ Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleDelete() {
        Member selected = membersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("⚠ Select a member to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete member: " + selected.getFullName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM members WHERE id=?");
                    ps.setInt(1, selected.getId());
                    ps.executeUpdate();
                    ps.close();
                    loadMembers();
                    clearFields();
                    showStatus("✅ Member deleted successfully!");
                } catch (SQLException e) {
                    showStatus("❌ Error: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        ObservableList<Member> filtered = FXCollections.observableArrayList();
        for (Member m : memberList) {
            if (m.getFullName().toLowerCase().contains(keyword) ||
                    m.getEmail().toLowerCase().contains(keyword)) {
                filtered.add(m);
            }
        }
        membersTable.setItems(filtered);
    }

    @FXML
    public void handleClear() {
        clearFields();
    }

    @FXML
    public void goHome() {
        SceneManager.switchScene("home.fxml");
    }

    private void populateFields(Member m) {
        fullNameField.setText(m.getFullName());
        emailField.setText(m.getEmail());
        phoneField.setText(m.getPhone());
        addressField.setText(m.getAddress());
    }

    private void clearFields() {
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        membersTable.getSelectionModel().clearSelection();
        showStatus("");
    }

    private boolean validateFields() {
        if (fullNameField.getText().trim().isEmpty()) {
            showStatus("⚠ Full Name is required.");
            return false;
        }

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.contains("@")) {
            showStatus("⚠ Please enter a valid email address.");
            return false;
        }

        return true;
    }

    private void setStatementValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, fullNameField.getText().trim());
        ps.setString(2, emailField.getText().trim());
        ps.setString(3, phoneField.getText().trim());
        ps.setString(4, addressField.getText().trim());
    }

    private void showStatus(String msg) {
        statusLabel.setText(msg);
    }
}