package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiTietSanPhamBH extends JDialog {

    private JButton btnThem, btnCong, btnTru;
    private JTextField txtSoLuong;
    private int maSanPham;
    private int soLuongTon;

    public ChiTietSanPhamBH(JFrame parent, String ten, String gia, ImageIcon hinhAnh, String[][] bangChiTiet, int maSanPham, int soLuongTon) {
        super(parent, "Chi tiết sản phẩm", true);
        this.maSanPham = maSanPham;
        this.soLuongTon = soLuongTon;
        setSize(600, 450);
        setLocationRelativeTo(parent);

        JPanel contentPane = new JPanel(new BorderLayout(20, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        // === BÊN TRÁI ===
        JLabel imgLabel = new JLabel(hinhAnh);
        imgLabel.setBorder(null);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(imgLabel, BorderLayout.CENTER);

        // === BÊN PHẢI ===
        JLabel tenLabel = new JLabel("<html><b style='font-size:18px'>" + ten + "</b></html>");
        JLabel giaLabel = new JLabel(gia);
        giaLabel.setForeground(new Color(255, 102, 0));
        giaLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Tăng/giảm số lượng
        btnTru = new JButton("-");
        btnCong = new JButton("+");
        txtSoLuong = new JTextField("1", 2);
        txtSoLuong.setHorizontalAlignment(JTextField.CENTER);
        txtSoLuong.setEditable(false);

        JPanel quantityPanel = new JPanel();
        quantityPanel.add(btnTru);
        quantityPanel.add(txtSoLuong);
        quantityPanel.add(btnCong);

        // Nút thêm vào giỏ
        btnThem = new JButton("\uD83D\uDED2 Thêm vào giỏ");
        btnThem.setBackground(new Color(255, 204, 0));
        btnThem.setFocusPainted(false);
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Label hiện số lượng tồn
        JLabel lblTonKho = new JLabel("Còn lại: " + soLuongTon + " sản phẩm");
        lblTonKho.setForeground(Color.GRAY);
        lblTonKho.setFont(new Font("Arial", Font.ITALIC, 12));

        // Panel chứa số lượng và tồn kho
        JPanel quantityWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        quantityWrap.add(quantityPanel);
        quantityWrap.add(lblTonKho);

        // Panel chứa nút Thêm vào giỏ – tách xuống dưới
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnWrap.add(btnThem);

        // Bảng thông tin
        String[] columnNames = {"Thông tin", "Chi tiết"};
        JTable table = new JTable(new DefaultTableModel(bangChiTiet, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(30);
        table.setEnabled(false);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        rightPanel.setOpaque(false);

        tenLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        giaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantityWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(tenLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(giaLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(quantityWrap);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnWrap);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(scrollPane);

        contentPane.add(leftPanel, BorderLayout.WEST);
        contentPane.add(rightPanel, BorderLayout.CENTER);
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnCong() {
        return btnCong;
    }

    public JButton getBtnTru() {
        return btnTru;
    }

    public JTextField getTxtSoLuong() {
        return txtSoLuong;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public ImageIcon resizeImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image originalImage = icon.getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + path);
            return new ImageIcon();
        }
    }

}
