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

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton manageSellersButton = new JButton("Manage Sellers");
        JButton manageProductsButton = new JButton("Manage Products");

        manageSellersButton.addActionListener(e -> new SellerManagement());
        manageProductsButton.addActionListener(e -> new ProductManagement());

        add(manageSellersButton);
        add(manageProductsButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
