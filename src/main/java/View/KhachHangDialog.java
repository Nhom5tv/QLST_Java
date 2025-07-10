/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author Admin
 */

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class KhachHangDialog extends JDialog{
    private JTextField txtMaKH, txtHoTen, txtSoDienThoai, txtEmail, txtDiaChi;
    private JButton btnLuu, btnHuy;
    private boolean confirmed = false;
    private JLabel lblMaKH;


    public KhachHangDialog(JFrame parent, String title) {
        super(parent, title, true);

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(420, 380);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        lblMaKH = new JLabel("Mã KH:");
        JLabel[] labels = {
                lblMaKH, new JLabel("Họ Tên:"), new JLabel("Số điện thoại:"),
                new JLabel("Địa chỉ:"), new JLabel("Email:") 
        };

        txtMaKH = new JTextField();
        txtMaKH.setEditable(false);
        txtHoTen = new JTextField();
        txtSoDienThoai = new JTextField();
        txtDiaChi = new JTextField();
        txtEmail = new JTextField();
       
        JTextField[] fields = {txtMaKH, txtHoTen, txtSoDienThoai,txtDiaChi, txtEmail};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            mainPanel.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            mainPanel.add(fields[i], gbc);
        }

        // Button Panel
        btnLuu = new JButton("💾 Lưu");
        btnHuy = new JButton("❌ Hủy");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        btnLuu.addActionListener(e -> {
            if (!isValidPhoneNumber(txtSoDienThoai.getText())) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ. Vui lòng nhập 10 chữ số và bắt đầu bằng 0.");
                return;
            }

            if (!isValidEmail(txtEmail.getText())) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ. Vui lòng nhập đúng định dạng email.");
                return;
            }
            confirmed = true;
            setVisible(false);
        });

        btnHuy.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmed = false;
            }
        });
    }

    // Thiết lập dữ liệu khi sửa
    public void setFields(String maKH, String hoTen, String soDienThoai, String diaChi, String email) {
        txtMaKH.setText(maKH);
        txtHoTen.setText(hoTen);
        txtSoDienThoai.setText(soDienThoai);
        txtDiaChi.setText(diaChi);
        txtEmail.setText(email);
    }

    // Getter dữ liệu người dùng nhập
    public String getMaKH() {
        return txtMaKH.getText().trim();
    }

    public String getHoTen() {
        return txtHoTen.getText().trim();
    }

    public String getSoDienThoai() {
        return txtSoDienThoai.getText().trim();
    }

    public String getEmail() {
        return txtEmail.getText().trim();
    }

    public String getDiaChi() {
        return txtDiaChi.getText().trim();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    public void anTruongMaKH() {
        lblMaKH.setVisible(false);
        txtMaKH.setVisible(false);
    }

    private boolean isValidPhoneNumber(String phone) {
        // SĐT Việt Nam: bắt đầu bằng 0, có 10 chữ số
        return phone.matches("^0\\d{9}$");
    }

    private boolean isValidEmail(String email) {
        // Định dạng email cơ bản
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

}
