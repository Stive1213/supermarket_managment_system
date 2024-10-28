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

public class SellerDashboard extends JFrame {
    private JTable itemTable;
    private JTextField quantityField;
    private JTextArea billArea;
    private double totalAmount;

    public SellerDashboard() {
        setTitle("Seller Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Seller Dashboard!", JLabel.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        itemTable = new JTable();
        loadItems();
        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        quantityField = new JTextField(5);
        JButton addButton = new JButton("Add");
        JButton clearBillButton = new JButton("Clear Bill");

        addButton.addActionListener(e -> addToBill());
        clearBillButton.addActionListener(e -> clearBill());

        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(clearBillButton);
        add(inputPanel, BorderLayout.SOUTH);

        billArea = new JTextArea(5, 30);
        billArea.setEditable(false);
        add(new JScrollPane(billArea), BorderLayout.EAST);

        setVisible(true);
    }

    private void loadItems() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

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

            itemTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading items: " + ex.getMessage());
        }
    }

    private void addToBill() {
        int row = itemTable.getSelectedRow();
        if (row >= 0) {
            try {
                String itemName = itemTable.getValueAt(row, 1).toString();
                double itemPrice = Double.parseDouble(itemTable.getValueAt(row, 2).toString());
                int quantity = Integer.parseInt(quantityField.getText());

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
                    return;
                }

                double subtotal = itemPrice * quantity;
                totalAmount += subtotal;

                billArea.append(itemName + " x " + quantity + " = $" + subtotal + "\n");
                billArea.append("Total Amount: $" + totalAmount + "\n");
                quantityField.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to add.");
        }
    }

    private void clearBill() {
        totalAmount = 0;
        billArea.setText("");
        quantityField.setText("");
        JOptionPane.showMessageDialog(this, "Bill cleared.");
    }

    public static void main(String[] args) {
        new SellerDashboard();
    }
}
