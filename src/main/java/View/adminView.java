package View;

import javax.swing.*;
import java.awt.*;

public class adminView extends JFrame {
    public JButton btnOpenSignUp;

    public adminView() {
        setTitle("Trang Admin");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        btnOpenSignUp = new JButton("Tạo tài khoản nhân viên");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(btnOpenSignUp);

        add(panel);
    }
    public void addSignUpListener(java.awt.event.ActionListener listener) {
        btnOpenSignUp.addActionListener(listener);
    }
}
