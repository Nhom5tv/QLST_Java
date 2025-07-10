package View;

import Model.NhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class TrangChuView extends JFrame {
    private JPanel sidebar;
    private JPanel content;
    private JLabel lblTitle;

    private boolean sidebarVisible = true;
    private Timer animationTimer;
    private final int sidebarWidth = 260;
    private final int ANIMATION_STEP = 10;
    private final int ANIMATION_DELAY = 5;
    
    private JButton btnLogout;

    private JLabel lblAvatar, lblTenNhanVien, lblChucVu;

    // Labels quản lý
    private JPanel lblDanhMuc, lblSanPham, lblLoHang, lblPhieuNhap, lblTonKho, lblNhaCungCap;
    private JPanel lblNhanVien, lblChamCong, lblLuong;
    private JPanel lblHDOffline, lblCTHDOffline, lblHDOnline, lblCTHDOnline;
    private JPanel lblTaiKhoanSection, lblBanHangSection, lblKhachHangSection, lblTaiChinhSection,lblKhuyenMaiSection, lblThongKeSection, lblSanPhamSection;

    public TrangChuView(NhanVien nhanVien) {
        setTitle("Trang chủ quản lý siêu thị");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        lblTitle = new JLabel("Trang Chủ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton menuButton = new JButton("\u2630");
        menuButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        menuButton.setFocusPainted(false);
        menuButton.setBorderPainted(false);
        menuButton.setBackground(Color.WHITE);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton.setPreferredSize(new Dimension(50, 40));
        menuButton.addActionListener(e -> toggleSidebarAnimated());

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(scaledLogo));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(menuButton);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(lblTitle);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(lblLogo);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(223, 246, 255));
        header.add(leftPanel, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== CONTENT CENTER =====
        content = new JPanel(new BorderLayout());
        add(content, BorderLayout.CENTER);

        // ===== SIDEBAR =====
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(sidebarWidth, getHeight()));
        sidebar.setBackground(new Color(223, 246, 255));

              // Thông tin nhân viên (gom lại thành 1 panel)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(223, 246, 255)); // cùng màu sidebar
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ảnh đại diện
        lblAvatar = new JLabel();
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (nhanVien.getAnh() != null) {
            ImageIcon avatarIcon = new ImageIcon(nhanVien.getAnh());
            Image scaledAvatar = getScaledImage(avatarIcon.getImage(), 64, 64);
            lblAvatar.setIcon(new ImageIcon(scaledAvatar));
        }
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblAvatar);

        // Tên nhân viên
        lblTenNhanVien = new JLabel(nhanVien.getHoten());
        lblTenNhanVien.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTenNhanVien.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblTenNhanVien);

        // Chức vụ
        lblChucVu = new JLabel(nhanVien.getChucvu());
        lblChucVu.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblChucVu.setForeground(Color.GRAY);
        lblChucVu.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblChucVu);

        infoPanel.add(Box.createVerticalStrut(10));
        sidebar.add(infoPanel);

        

        

        // MENU
        lblSanPhamSection = createSectionLabel("🗂 1. Quản lý sản phẩm");
        sidebar.add(lblSanPhamSection);
        lblDanhMuc = createMenuLabel("▸ Danh mục sản phẩm");
        lblSanPham = createMenuLabel("▸ Sản phẩm");
        lblTonKho = createMenuLabel("▸ Tồn kho");
        lblLoHang = createMenuLabel("▸ Lô hàng");
        lblPhieuNhap = createMenuLabel("▸ Phiếu nhập");
        lblNhaCungCap = createMenuLabel("▸ Nhà cung cấp");
        sidebar.add(lblDanhMuc);
        sidebar.add(lblSanPham);
        sidebar.add(lblTonKho);
        sidebar.add(lblLoHang);
        sidebar.add(lblPhieuNhap);
        sidebar.add(lblNhaCungCap);

        sidebar.add(createSectionLabel("👥 2. Quản lý nhân sự"));
        lblNhanVien = createMenuLabel("▸ Hồ sơ nhân viên");
        lblChamCong = createMenuLabel("▸ Chấm công");
        lblLuong = createMenuLabel("▸ Lương");
        sidebar.add(lblNhanVien);
        sidebar.add(lblChamCong);
        sidebar.add(lblLuong);

        lblBanHangSection = createSectionLabel("📇 3. Quản lý bán hàng");
        sidebar.add(lblBanHangSection);
        lblHDOffline = createMenuLabel("▸ Hóa đơn tại siêu thị");
        lblCTHDOffline = createMenuLabel("▸ Bán Hàng POS");
        lblHDOnline = createMenuLabel("▸ Hóa đơn đặt online");
        sidebar.add(lblHDOffline);
        sidebar.add(lblCTHDOffline);
        sidebar.add(lblHDOnline);



        lblTaiKhoanSection = createSectionLabel("🔐 4. Quản lý tài khoản");
        sidebar.add(lblTaiKhoanSection);

        lblKhachHangSection = createSectionLabel("💻 5. Quản lý khách hàng");
        sidebar.add(lblKhachHangSection);

        lblTaiChinhSection = createSectionLabel("💰 6. Quản lý tài chính");
        sidebar.add(lblTaiChinhSection);

        lblKhuyenMaiSection = createSectionLabel("🎁 7. Quản lý khuyến mãi");
        sidebar.add(lblKhuyenMaiSection);

        lblThongKeSection = createSectionLabel("📊 8. Thống kê");
//        sidebar.add(lblThongKeSection);
        
        //nut logout
        ImageIcon icon = new ImageIcon(getClass().getResource("/imageBanHang/logout.png"));
        Image scaled = getScaledImage(icon.getImage(), 16, 16);
        btnLogout = new JButton(new ImageIcon(scaled));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setToolTipText("Đăng xuất");

        // Set kích thước và căn giữa
        btnLogout.setPreferredSize(new Dimension(32, 32));
        btnLogout.setMaximumSize(new Dimension(32, 32));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        sidebar.add(Box.createVerticalGlue()); // đẩy các thành phần trên lên
        sidebar.add(Box.createVerticalStrut(5));
        JPanel panelLogout = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLogout.setOpaque(false);
        panelLogout.add(btnLogout);
        sidebar.add(panelLogout);
        sidebar.add(Box.createVerticalStrut(5));

        add(sidebar, BorderLayout.WEST);
    }

    private void toggleSidebarAnimated() {
        if (animationTimer != null && animationTimer.isRunning()) return;

        int startWidth = sidebar.getWidth();
        int endWidth = sidebarVisible ? 0 : sidebarWidth;
        int step = (endWidth - startWidth) / ANIMATION_STEP;

        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int newWidth = sidebar.getWidth() + step;
                sidebar.setPreferredSize(new Dimension(newWidth, getHeight()));
                sidebar.revalidate();
                TrangChuView.this.repaint();
                currentStep++;
                if (currentStep >= ANIMATION_STEP) {
                    animationTimer.stop();
                    sidebarVisible = !sidebarVisible;
                    sidebar.setPreferredSize(new Dimension(endWidth, getHeight()));
                    sidebar.revalidate();
                }
            }
        });
        animationTimer.start();
    }

    private JPanel createMenuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0)); // Lề trên và dưới
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Không thêm khoảng cách giữa các panel
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 21)); // Fix chiều cao
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 21));
        panel.setMinimumSize(new Dimension(0, 21));
        panel.setOpaque(false);
        panel.add(label);
        return panel;
    }

    private JPanel createSectionLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Lề lớn hơn cho section

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Không thêm khoảng cách giữa các panel
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 21)); // Fix chiều cao
        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 21));
        panel.setMinimumSize(new Dimension(0, 21));
        panel.setOpaque(false);
        panel.add(label);
        return panel;
    }


    public void setTitleText(String title) {
        lblTitle.setText(title);
    }

    public void setCenterPanel(JPanel panel) {
        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }
    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    // GETTERS cho controller
    public JPanel getFormDanhMucLabel() { return lblDanhMuc; }
    public JPanel getFormSanPhamLabel() { return lblSanPham; }
    public JPanel getFormLoHangLabel() { return lblLoHang; }
    public JPanel getFormPhieuNhapLabel() { return lblPhieuNhap; }
    public JPanel getFormTonKhoLabel() { return lblTonKho; }
    public JPanel getFormNhaCungCapLabel() { return lblNhaCungCap; }
    public JPanel getFormNhanVienLabel() { return lblNhanVien; }
    public JPanel getFormChamCongLabel() { return lblChamCong; }
    public JPanel getFormLuongLabel() { return lblLuong; }
    public JPanel getFormHDOfflineLabel() { return lblHDOffline; }
    public JPanel getFormCTHDOfflineLabel() { return lblCTHDOffline; }
    public JPanel getFormHDOnlineLabel() { return lblHDOnline; }
    public JPanel getFormCTHDOnlineLabel() { return lblCTHDOnline; }
    public JPanel getFormTaiKhoanSectionLabel() { return lblTaiKhoanSection; }
    public JPanel getFormKhachHangSectionLabel() { return lblKhachHangSection; }
    public JPanel getFormTaiChinhSectionLabel() { return lblTaiChinhSection; }
    public JPanel getFormKhuyenMaiSectionLabel() { return lblKhuyenMaiSection; }
    public JPanel getFormThongKeSectionLabel() { return lblThongKeSection; }
    public JPanel getFormSanPhamSectionLabel() { return lblSanPhamSection; }
    public JPanel getLblBanHangSection() {
        return lblBanHangSection;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JLabel getlblAvatar() {
        return lblAvatar;
    }


}
