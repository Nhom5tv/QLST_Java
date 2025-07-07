package view;


import DAO.KhachHangDAO;
import Controller.SignUpController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SignUpView extends JFrame {

    public JTextField usernameField;
    public JTextField phoneField;
    public JTextField emailField;
    public JTextArea addressArea;
    public JPasswordField passwordField;
    public JPasswordField confirmPasswordField;
    public JCheckBox termsCheckBox;
    public JButton signUpButton;
    public JButton resetButton;
    public JButton backButton;

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

    public SignUpView() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
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

        // Input fields
        usernameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        addressArea = new JTextArea(3, 20);
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        RoundedBorder roundBorder = new RoundedBorder(25);

        usernameField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Họ và Tên"));
        phoneField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Số điện thoại"));
        emailField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Email"));
        addressArea.setBorder(BorderFactory.createTitledBorder(roundBorder, "Địa chỉ"));
        passwordField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Password"));
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder(roundBorder, "Confirm Password"));

        Dimension fieldSize = new Dimension(300, 50);
        Font fieldFont = new Font("Arial", Font.PLAIN, 15);

        for (JTextField field : new JTextField[]{usernameField, phoneField, emailField, passwordField, confirmPasswordField}) {
            field.setMaximumSize(fieldSize);
            field.setFont(fieldFont);
            formPanel.add(field);
            formPanel.add(Box.createVerticalStrut(10));
        }

        addressArea.setMaximumSize(new Dimension(300, 90));
        addressArea.setFont(fieldFont);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        formPanel.add(addressArea);
        formPanel.add(Box.createVerticalStrut(10));

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

        formPanel.add(Box.createVerticalStrut(10));

        // ✅ Thêm nút Back to Login
       backButton = new JButton("← Back to Login");
backButton.setFont(new Font("Arial", Font.BOLD, 14));
backButton.setForeground(new Color(255, 105, 180));
backButton.setBorderPainted(false);
backButton.setContentAreaFilled(false);
backButton.setFocusPainted(false);
backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(backButton);

        rightPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    // Getters
    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPhone() {
        return phoneField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getAddress() {
        return addressArea.getText().trim();
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

    // Listener methods
    public void addSignUpListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
