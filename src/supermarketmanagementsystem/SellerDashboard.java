package supermarketmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.awt.print.*;

public class SellerDashboard extends JFrame {
    private JTable itemTable;
    private JTextField quantityField;
    private JTextArea billArea;
    private JComboBox<String> categoryDropdown; // ComboBox for categories
    private double totalAmount;

    public SellerDashboard() {
        setTitle("Seller Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Set a soft background color
        getContentPane().setBackground(new Color(230, 230, 255));

        // Header
        JLabel welcomeLabel = new JLabel("Welcome to the Seller Dashboard!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(75, 0, 130));
        add(welcomeLabel, BorderLayout.NORTH);

        // Category dropdown
        JPanel categoryPanel = new JPanel();
        categoryPanel.setBackground(new Color(240, 240, 255));
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Select Product Category"));
        categoryDropdown = new JComboBox<>();
        loadCategories(); // Load available categories into the dropdown
        categoryDropdown.addActionListener(e -> loadItems()); // Reload items based on selected category
        categoryPanel.add(categoryDropdown);
        add(categoryPanel, BorderLayout.WEST);

        itemTable = new JTable();
        loadItems(); // Load items for the initial category
        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(240, 240, 255));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Section"));
        quantityField = new JTextField(5);
        JButton addButton = createStyledButton("Add");
        JButton clearBillButton = createStyledButton("Clear Bill");
        JButton printBillButton = createStyledButton("Print Bill");

        addButton.addActionListener(e -> addToBill());
        clearBillButton.addActionListener(e -> clearBill());
        printBillButton.addActionListener(e -> printBill());

        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(clearBillButton);
        inputPanel.add(printBillButton);
        add(inputPanel, BorderLayout.SOUTH);

        billArea = new JTextArea(10, 30);
        billArea.setEditable(false);
        JScrollPane billScrollPane = new JScrollPane(billArea);
        billScrollPane.setBorder(BorderFactory.createTitledBorder("Bill Summary"));
        add(billScrollPane, BorderLayout.EAST);

        setVisible(true);
    }

    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEtchedBorder());
        return button;
    }

    // Method to load categories from the database into the JComboBox
    private void loadCategories() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM products");

            while (rs.next()) {
                categoryDropdown.addItem(rs.getString("category"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
        }
    }

    // Method to load items based on selected category
    private void loadItems() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
            String selectedCategory = categoryDropdown.getSelectedItem().toString();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM products WHERE category = ?");
            ps.setString(1, selectedCategory);
            ResultSet rs = ps.executeQuery();

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

    // Method to add the selected item to the bill and reduce its quantity in the database
    private void addToBill() {
        int row = itemTable.getSelectedRow();
        if (row >= 0) {
            try {
                String itemName = itemTable.getValueAt(row, 1).toString();
                double itemPrice = Double.parseDouble(itemTable.getValueAt(row, 2).toString());
                int availableQuantity = Integer.parseInt(itemTable.getValueAt(row, 3).toString());
                int quantity = Integer.parseInt(quantityField.getText());

                if (quantity <= 0 || quantity > availableQuantity) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity. Available quantity: " + availableQuantity);
                    return;
                }

                double subtotal = itemPrice * quantity;
                totalAmount += subtotal;

                billArea.append(itemName + " x " + quantity + " = $" + subtotal + "\n");
                billArea.append("Total Amount: $" + totalAmount + "\n");

                // Update the quantity in the database
                updateProductQuantity(itemName, availableQuantity - quantity);

                quantityField.setText("");
                loadItems(); // Reload the items to reflect the new quantities
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to add.");
        }
    }

    // Method to update the product quantity in the database
    private void updateProductQuantity(String itemName, int newQuantity) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", "");
            PreparedStatement ps = con.prepareStatement("UPDATE products SET quantity = ? WHERE name = ?");
            ps.setInt(1, newQuantity);
            ps.setString(2, itemName);
            ps.executeUpdate();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating quantity: " + ex.getMessage());
        }
    }

    private void clearBill() {
        totalAmount = 0;
        billArea.setText("");
        quantityField.setText("");
        JOptionPane.showMessageDialog(this, "Bill cleared.");
    }

    // Method to handle printing the bill
    private void printBill() {
        try {
            boolean complete = billArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing Completed!", "Printer", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing Cancelled!", "Printer", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, "Error during printing: " + pe.getMessage(), "Printer", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SellerDashboard();
    }
}
