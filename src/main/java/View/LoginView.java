package view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import controller.LoginController;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;
    private JCheckBox rememberBox;

    public LoginView() {
        // Đặt FlatLaf Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.focusWidth", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
    }

    private void initComponents() {
        setTitle("Sign In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
        Font subTitleFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font smallFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(400, 500));
        leftPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Title
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(Color.WHITE);
        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.insets = new Insets(0, 0, 5, 0);

        JLabel welcomeLabel = new JLabel("WELCOME!");
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setForeground(new Color(220, 53, 69));
        titlePanel.add(welcomeLabel, titleGbc);

        titleGbc.gridy++;
        JLabel subtitleLabel = new JLabel("Login to your account");
        subtitleLabel.setFont(subTitleFont);
        subtitleLabel.setForeground(Color.GRAY);
        titlePanel.add(subtitleLabel, titleGbc);

        gbc.gridy = 0;
        leftPanel.add(titlePanel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        gbc.gridy = 1;
        leftPanel.add(usernameLabel, gbc);

        usernameField = new JTextField();
        usernameField.setFont(fieldFont);
        gbc.gridy = 2;
        leftPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        gbc.gridy = 3;
        leftPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        gbc.gridy = 4;
        leftPanel.add(passwordField, gbc);

        // Remember checkbox
        rememberBox = new JCheckBox("Remember password.");
        rememberBox.setBackground(Color.WHITE);
        rememberBox.setFont(smallFont);
        gbc.gridy = 5;
        leftPanel.add(rememberBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(Color.WHITE);

       signInButton = new JButton("Sign In");
signInButton.setFont(fieldFont);
signInButton.setBackground(new Color(220, 53, 69));
signInButton.setForeground(Color.WHITE);
signInButton.putClientProperty("FlatLaf.style", "background: #dc3545; foreground: #FFFFFF");

signUpButton = new JButton("Sign Up");  // Khởi tạo đúng nút signUpButton
signUpButton.setFont(fieldFont);
signUpButton.setBackground(new Color(220, 53, 69));
signUpButton.setForeground(Color.WHITE);
signUpButton.putClientProperty("FlatLaf.style", "background: #dc3545; foreground: #FFFFFF");


        
        buttonPanel.add(signInButton);
                buttonPanel.add(signUpButton);


        gbc.gridy = 6;
        leftPanel.add(buttonPanel, gbc);

        // Forgot password
        JLabel forgotLabel = new JLabel("Forgot Password", SwingConstants.CENTER);
        forgotLabel.setForeground(new Color(240, 112, 122));
        forgotLabel.setFont(smallFont);
        gbc.gridy = 7;
        leftPanel.add(forgotLabel, gbc);

        add(leftPanel, BorderLayout.WEST);

        // Right panel with background image
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                java.net.URL imgUrl = getClass().getResource("/images/anhhong.jpg");
                if (imgUrl != null) {
                    ImageIcon imageIcon = new ImageIcon(imgUrl);
                    g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.GRAY);
                    g.drawString("Image not found", 10, 20);
                }
            }
        };
        rightPanel.setBackground(Color.LIGHT_GRAY);
        add(rightPanel, BorderLayout.CENTER);
    }

    // ===== PUBLIC METHODS for Controller to access =====

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isRememberChecked() {
        return rememberBox.isSelected();
    }

    public void addSignInListener(java.awt.event.ActionListener listener) {
        signInButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    public void addSignUpListener(java.awt.event.ActionListener listener) {
    signUpButton.addActionListener(listener);
}

    public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
        LoginView view = new LoginView();
           LoginController controller = new LoginController(view);  // Khởi tạo controller và gán listener
        view.setVisible(true);
    });

}

}
