package View;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class GiaoDienBanHang extends JPanel {
    public JTabbedPane tabbedPane;
    public JTextField txtTimKiem;
    public JLabel lblTongTien, lblKhachCanTra, lblTienThua;
    public JButton btnThanhToan;
    public JLabel lblGiamGia, lblThuKhac;
    public JTextField txtGiamGia, txtThuKhac, txtTienKhachDua;
    public JComboBox<String> cboPhuongThuc;
    public JTextArea txtGhiChu;
    public JButton btnIn;

    private BigDecimal tongTienHang = BigDecimal.ZERO;
    private BigDecimal tienKhachDua = BigDecimal.ZERO;

    public GiaoDienBanHang() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Không thể cài đặt FlatLaf.");
        }

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panelTrai = new JPanel(new BorderLayout());
        JPanel panelTopLeft = new JPanel(new BorderLayout());

        JLabel lblTieuDeChung = new JLabel("HÓA ĐƠN");
        lblTieuDeChung.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDeChung.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));

        txtTimKiem = new JTextField("Tìm theo mã vạch hoặc tên sản phẩm");
        txtTimKiem.setPreferredSize(new Dimension(300, 35));
        txtTimKiem.setForeground(Color.GRAY);

        JPanel panelTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelTimKiem.add(lblTimKiem);
        panelTimKiem.add(txtTimKiem);

        panelTopLeft.add(lblTieuDeChung, BorderLayout.WEST);
        panelTopLeft.add(panelTimKiem, BorderLayout.EAST);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        panelTrai.add(panelTopLeft, BorderLayout.NORTH);
        panelTrai.add(tabbedPane, BorderLayout.CENTER);

        // Panel phải (thanh toán)
        JPanel panelPhai = new JPanel();
        panelPhai.setPreferredSize(new Dimension(350, 0));
        panelPhai.setLayout(new BoxLayout(panelPhai, BoxLayout.Y_AXIS));
        panelPhai.setBorder(BorderFactory.createTitledBorder("Hóa đơn"));

        lblTongTien = new JLabel("Tổng tiền hàng: 0 đ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel khachThanhToanPanel = new JPanel();
        khachThanhToanPanel.setLayout(new BoxLayout(khachThanhToanPanel, BoxLayout.Y_AXIS));
        khachThanhToanPanel.setBorder(BorderFactory.createTitledBorder("Khách thanh toán (F8)"));
        khachThanhToanPanel.setMaximumSize(new Dimension(300, 70));

        cboPhuongThuc = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Momo"});
        cboPhuongThuc.setMaximumSize(new Dimension(280, 25));
        txtTienKhachDua = new JTextField("0");
        txtTienKhachDua.setMaximumSize(new Dimension(280, 25));

        khachThanhToanPanel.add(cboPhuongThuc);
        khachThanhToanPanel.add(Box.createVerticalStrut(5));
        khachThanhToanPanel.add(txtTienKhachDua);

        lblTienThua = new JLabel("Tiền thừa trả khách: 0 đ");
        lblTienThua.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTienThua.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel ghiChuPanel = new JPanel();
        ghiChuPanel.setLayout(new BoxLayout(ghiChuPanel, BoxLayout.Y_AXIS));
        ghiChuPanel.setBorder(BorderFactory.createTitledBorder("Ghi chú"));
        ghiChuPanel.setMaximumSize(new Dimension(300, 90));

        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane ghiChuScroll = new JScrollPane(txtGhiChu);
        ghiChuPanel.add(ghiChuScroll);

        btnIn = new JButton("In");
        btnIn.setFont(new Font("Arial", Font.BOLD, 14));
        btnIn.setBackground(new Color(150, 150, 150));
        btnIn.setForeground(Color.WHITE);
        btnIn.setFocusPainted(false);
        btnIn.setMaximumSize(new Dimension(250, 35));
        btnIn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnThanhToan = new JButton("Thanh toán (F9)");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 16));
        btnThanhToan.setBackground(new Color(76, 175, 80));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.setMaximumSize(new Dimension(250, 40));
        btnThanhToan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPhai.add(Box.createVerticalStrut(15));
        panelPhai.add(lblTongTien);
        panelPhai.add(Box.createVerticalStrut(10));
        panelPhai.add(khachThanhToanPanel);
        panelPhai.add(Box.createVerticalStrut(10));
        panelPhai.add(lblTienThua);
        panelPhai.add(Box.createVerticalStrut(10));
        panelPhai.add(ghiChuPanel);
        panelPhai.add(Box.createVerticalStrut(10));
        panelPhai.add(btnThanhToan);
        panelPhai.add(Box.createVerticalStrut(15));
        panelPhai.add(btnIn);
        panelPhai.add(Box.createVerticalGlue());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTrai, panelPhai);
        splitPane.setResizeWeight(0.75);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // Getter cho lblTongTien để Controller có thể truy cập
    public JLabel getLblTongTien() {
        return lblTongTien;
    }
    public JTextField getTxtTienKhachDua() {
    return txtTienKhachDua;
}

public JLabel getLblTienThua() {
    return lblTienThua;
}

    public JTextArea getTxtGhiChu() {
        return txtGhiChu;
    }
}
