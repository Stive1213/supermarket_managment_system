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
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public LoginForm() {
        setTitle("Login");
        setLayout(null);
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI Components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 165, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 165, 25);
        add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(10, 80, 80, 25);
        add(roleLabel);

        roleComboBox = new JComboBox<>(new String[]{"admin", "seller"});
        roleComboBox.setBounds(100, 80, 165, 25);
        add(roleComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 110, 80, 25);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setVisible(true);
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket_db", "root", ""); // Change to your DB credentials
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (role.equals("admin")) {
                    new AdminDashboard();
                } else {
                    new SellerDashboard();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
