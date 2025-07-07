/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DangKyView extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox termsCheckBox;
    private JButton signUpButton;
    private JButton resetButton;
    
    class RoundedBorder implements javax.swing.border.Border {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(10, 10, 10, 10);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

    public DangKyView() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left panel (image)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/avatar.jpg"));
                g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Right panel (form)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // Form content panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(15));

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        RoundedBorder roundBorder = new RoundedBorder(25);
        usernameField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Username"));
        emailField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Email"));
        passwordField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Password"));
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Confirm Password"));

        Dimension fieldSize = new Dimension(300, 50);
        Font fieldFont = new Font("Arial", Font.PLAIN, 15);

        for (JTextField field : new JTextField[]{usernameField, emailField, passwordField, confirmPasswordField}) {
            field.setMaximumSize(fieldSize);
            field.setFont(fieldFont);
            formPanel.add(field);
            formPanel.add(Box.createVerticalStrut(10));
        }

        termsCheckBox = new JCheckBox("I agree to the terms and conditions.");
        termsCheckBox.setBackground(Color.WHITE);
        termsCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(termsCheckBox);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(255, 105, 180));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(150, 45));

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setPreferredSize(new Dimension(150, 45));

        buttonPanel.add(signUpButton);
        buttonPanel.add(resetButton);
        formPanel.add(buttonPanel);

        rightPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    // Getters for user input
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public boolean isTermsChecked() {
        return termsCheckBox.isSelected();
    }

    // Methods to add action listeners
    public void addSignUpListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }
}
