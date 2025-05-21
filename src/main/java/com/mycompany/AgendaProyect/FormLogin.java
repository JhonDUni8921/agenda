package com.mycompany.AgendaProyect;

import Clases.Clogin;
import javax.swing.*;
import java.awt.*;

public class FormLogin extends JFrame {

    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel imageLabel;

    public FormLogin() {
        setTitle("Login Agenda");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

       imageLabel = new JLabel(new ImageIcon(getClass().getResource("/Images/AgendaImage.png")));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);


        userTextField = new JTextField(90);
        passwordField = new JPasswordField(90);
        loginButton = new JButton("Ingresar");

        userTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        loginButton.addActionListener(e -> handleLogin());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        inputPanel.add(new JLabel("Nombre de usuario:"));
        inputPanel.add(userTextField);
        inputPanel.add(new JLabel("Contraseña:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(loginButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = userTextField.getText();
        String password = new String(passwordField.getPassword());

        Clogin login = new Clogin();
        login.ValidarUsuario(this, userTextField, passwordField);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormLogin loginWindow = new FormLogin();
            loginWindow.setVisible(true);
        });
    }
}
