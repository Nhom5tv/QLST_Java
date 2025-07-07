package view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;

public class NhanVienDialog extends JDialog {
    private JTextField txtMaNV, txtHoTen, txtCCCD, txtSDT, txtChucVu, txtEmail;
    private JComboBox<String> cbGioiTinh;
    private JTextArea txtGhiChu;
    private JButton btnLuu, btnHuy, btnChonAnh;
    private JLabel lblImage;
    private BufferedImage employeeImage;
    private boolean confirmed = false;
    private byte[] imageBytes;

    public NhanVienDialog(JFrame parent, String title) {
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
        setSize(600, 600);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ảnh + nút chọn ảnh (bên trái)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        lblImage = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(240, 300));
        lblImage.setBorder(BorderFactory.createTitledBorder("Ảnh nhân viên"));
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonAnh.setMaximumSize(new Dimension(150, 35));
        btnChonAnh.addActionListener(e -> chooseImage());

        leftPanel.add(lblImage);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(btnChonAnh);

        // Form bên phải
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(8, 8, 8, 8);
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;

        JLabel[] labels = {
                new JLabel("Mã NV:"), new JLabel("Họ tên:"), new JLabel("CCCD:"),
                new JLabel("SĐT:"), new JLabel("Chức vụ:"), new JLabel("Email:"),
                new JLabel("Giới tính:"), new JLabel("Ghi chú:")
        };

        txtMaNV = new JTextField();
        txtMaNV.setEnabled(false);
        txtHoTen = new JTextField();
        txtCCCD = new JTextField();
        txtSDT = new JTextField();
        txtChucVu = new JTextField();
        txtEmail = new JTextField();
        cbGioiTinh = new JComboBox<>(new String[]{"---Chọn Giới Tính---","Nam", "Nữ", "Khác"});
        cbGioiTinh.setSelectedIndex(0);
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);

        JComponent[] fields = {
                txtMaNV, txtHoTen, txtCCCD, txtSDT, txtChucVu,
                txtEmail, cbGioiTinh, scrollGhiChu
        };

        for (int i = 0; i < labels.length; i++) {
            gbcRight.gridx = 0;
            gbcRight.weightx = 0.3;
            rightPanel.add(labels[i], gbcRight);

            gbcRight.gridx = 1;
            gbcRight.weightx = 0.7;
            rightPanel.add(fields[i], gbcRight);

            gbcRight.gridy++;
        }

        // Panel nút chức năng
        btnLuu = new JButton("💾 Lưu");
        btnHuy = new JButton("❌ Hủy");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);

        btnLuu.addActionListener(e -> {
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

        // Thêm panel
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh nhân viên");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    employeeImage = img;
                    // Resize ảnh cho vừa label (tùy chỉnh nếu muốn)
                    Image scaled = img.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaled));
                    lblImage.setText(null);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi đọc ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Set dữ liệu lên form, bao gồm ảnh (từ byte[])
     public void setFields(int ma_nv, String hoTen, String cccd, String sdt,
                      String chucvu, String email, String gioitinh,
                      String ghichu, byte[] imageBytes) {
    this.imageBytes = imageBytes;

    txtMaNV.setText(String.valueOf(ma_nv));
    txtHoTen.setText(hoTen);
    txtCCCD.setText(cccd);
    txtSDT.setText(sdt);
    txtChucVu.setText(chucvu);
    txtEmail.setText(email);
   if (gioitinh == null || gioitinh.trim().isEmpty()) {
    cbGioiTinh.setSelectedIndex(0);
    } else {
        cbGioiTinh.setSelectedItem(gioitinh);
    }
    txtGhiChu.setText(ghichu);

    // 👇 Resize ảnh với kích thước cố định (ví dụ: 100x120)
    if (imageBytes != null) {
        ImageIcon icon = new ImageIcon(imageBytes);
        Image scaledImage = icon.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(scaledImage));
        lblImage.setText(null);
    } else {
        lblImage.setIcon(null); // không có ảnh
        lblImage.setText("Chưa có ảnh");
    }
}

    // Lấy byte[] ảnh để lưu vào database mediumblob
    public byte[] getImageBytes() {
        if (employeeImage == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(employeeImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Các getter khác
    public int getMaNV() {
        try {
            return Integer.parseInt(txtMaNV.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public String getHoTen() { return txtHoTen.getText().trim(); }
    public String getCCCD() { return txtCCCD.getText().trim(); }
    public String getSDT() { return txtSDT.getText().trim(); }
    public String getChucVu() { return txtChucVu.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getGioiTinh() { return (String) cbGioiTinh.getSelectedItem(); }
    public String getGhiChu() { return txtGhiChu.getText().trim(); }
    public boolean isConfirmed() { return confirmed; }
}
