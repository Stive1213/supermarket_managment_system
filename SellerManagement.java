/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermarketmanagementsystem;

/**
 *
 * @author hp
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class SellerManagement extends JFrame {
    private JTable sellerTable;
    private JTextField usernameField, passwordField;

    public SellerManagement() {
        setTitle("Seller Management");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sellerTable = new JTable();
        loadSellers();
        JScrollPane scrollPane = new JScrollPane(sellerTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        passwordField = new JTextField();
        inputPanel.add(passwordField);

        JButton addButton = new JButton("Add Seller");
        addButton.addActionListener(e -> addSeller());

        JButton deleteButton = new JButton("Delete Seller");
        deleteButton.addActionListener(e -> deleteSeller());

        JButton editButton = new JButton("Edit Seller");
        editButton.addActionListener(e -> editSeller());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        sellerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = sellerTable.getSelectedRow();
                usernameField.setText(sellerTable.getValueAt(row, 1).toString());
                passwordField.setText(sellerTable.getValueAt(row, 2).toString());
            }
        });

        setVisible(true);
    }

    private void loadSellers() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE role='seller'");

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            sellerTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading sellers: " + ex.getMessage());
        }
    }

    private void addSeller() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'seller')");
            ps.setString(1, usernameField.getText());
            ps.setString(2, passwordField.getText());
            ps.executeUpdate();
            con.close();
            loadSellers();
            clearFields();
            JOptionPane.showMessageDialog(this, "Seller added successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding seller: " + ex.getMessage());
        }
    }

    private void deleteSeller() {
        try {
            int row = sellerTable.getSelectedRow();
            if (row >= 0) {
                String username = sellerTable.getValueAt(row, 1).toString();
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
                PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE username=?");
                ps.setString(1, username);
                ps.executeUpdate();
                con.close();
                loadSellers();
                clearFields();
                JOptionPane.showMessageDialog(this, "Seller deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Select a seller to delete.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting seller: " + ex.getMessage());
        }
    }

    private void editSeller() {
        try {
            int row = sellerTable.getSelectedRow();
            if (row >= 0) {
                String oldUsername = sellerTable.getValueAt(row, 1).toString();
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
                PreparedStatement ps = con.prepareStatement("UPDATE users SET username=?, password=? WHERE username=?");
                ps.setString(1, usernameField.getText());
                ps.setString(2, passwordField.getText());
                ps.setString(3, oldUsername);
                ps.executeUpdate();
                con.close();
                loadSellers();
                clearFields();
                JOptionPane.showMessageDialog(this, "Seller updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Select a seller to edit.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing seller: " + ex.getMessage());
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}

