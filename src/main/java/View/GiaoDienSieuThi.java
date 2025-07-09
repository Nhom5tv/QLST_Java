package View;

import Bridge.DBConnection;
import Controller.GiaoDienSieuThiController;
import Model.KhachHang;
import Model.SanPham;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class GiaoDienSieuThi extends JFrame {

    private JList<String> categoryList;
    private JPanel productPanel;
    private JPanel productWrapper;
    private JTextField searchField;
    private JButton btnLogout, btnSearch, cartButton;

    private int maKH;
    private String tenKH;
    private JLabel username;
    private JLabel avatarIcon;
    private KhachHang kh;
    public static GiaoDienSieuThi instance;

    public GiaoDienSieuThi(KhachHang kh) {
        instance = this;
        this.kh = kh;
        this.maKH = kh.getMaKH();
        this.tenKH = kh.getHoTen();
        setTitle("BÁN HÀNG SIÊU THỊ");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Mở toàn màn hình, tự điều chỉnh
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // ==== HEADER (DEVELOP BY - AVATAR + USERNAME + LOGOUT) ====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 223, 100));
        headerPanel.setPreferredSize(new Dimension(0, 30));

        JLabel devLabel = new JLabel("Develop by Nhóm 1");
        devLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        headerPanel.add(devLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        userPanel.setOpaque(false);

        avatarIcon = new JLabel(resizeImage("/imageBanHang/user.png", 16, 16));
        avatarIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        username = new JLabel("Xin chào, " + tenKH);
        username.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnLogout = new JButton(resizeImage("/imageBanHang/logout.png", 16, 16));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setToolTipText("Đăng xuất");

        userPanel.add(avatarIcon);
        userPanel.add(username);
        userPanel.add(btnLogout);

        headerPanel.add(userPanel, BorderLayout.EAST);

        // ==== SEARCH BAR WITH LOGO AND CART ====
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        searchPanel.setPreferredSize(new Dimension(0, 45));

        JLabel logoLabel = new JLabel(resizeImage("/images/logo.png", 80, 40));
        logoLabel.setPreferredSize(new Dimension(122, 50));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 40));

        JPanel centerSearch = new JPanel(new BorderLayout(10, 0));
        centerSearch.setBackground(Color.WHITE);
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo tên sản phẩm");
        btnSearch = new JButton("🔍");
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setContentAreaFilled(true);
        btnSearch.setOpaque(true);
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cartButton = new JButton("🛒");
        cartButton.setFocusPainted(false);
        cartButton.setBorderPainted(false);
        cartButton.setContentAreaFilled(false);
        cartButton.setOpaque(false);
        cartButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cartButton.setPreferredSize(new Dimension(50, 30));

        JPanel rightSearchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightSearchPanel.setOpaque(false);
        rightSearchPanel.add(btnSearch);
        rightSearchPanel.add(cartButton);

        centerSearch.add(searchField, BorderLayout.CENTER);
        searchPanel.add(logoLabel, BorderLayout.WEST);
        searchPanel.add(centerSearch, BorderLayout.CENTER);
        searchPanel.add(rightSearchPanel, BorderLayout.EAST);

        // ==== BANNER IMAGE (FIXED FULL WIDTH) ====
        JLabel bannerImage = new JLabel(resizeImage("/imageBanHang/banner_test.jpg", 800, 180));
        bannerImage.setHorizontalAlignment(JLabel.CENTER);
        bannerImage.setPreferredSize(new Dimension(800, 150));
        JPanel bannerWrapper = new JPanel(new BorderLayout());
        bannerWrapper.setBackground(Color.WHITE);
        bannerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));
        bannerWrapper.add(bannerImage, BorderLayout.CENTER);

        // ==== DANH MỤC ====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBackground(new Color(255, 140, 50));

        JLabel dmLabel = new JLabel("DANH MỤC", JLabel.CENTER);
        dmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dmLabel.setForeground(Color.WHITE);
        dmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        categoryList = new JList<>();
        categoryList.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryList.setFixedCellHeight(30);

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(dmLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(new JScrollPane(categoryList));

        // ==== DANH SÁCH SẢN PHẨM ====
        productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        productPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));
        productPanel.setBackground(new Color(245, 245, 245));
        productPanel.setPreferredSize(new Dimension(700, 650));

        productWrapper = new JPanel();
        productWrapper.setLayout(new BoxLayout(productWrapper, BoxLayout.Y_AXIS));
        productWrapper.setBackground(new Color(245, 245, 245));
        productWrapper.add(productPanel);

        JScrollPane scrollPane = new JScrollPane(productWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel mainContent = new JPanel(new BorderLayout(5, 5));
        mainContent.setBackground(new Color(245, 245, 245));

        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        leftWrapper.setOpaque(false);
        leftWrapper.add(leftPanel, BorderLayout.CENTER);

        mainContent.add(leftWrapper, BorderLayout.WEST);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.add(bannerWrapper, BorderLayout.NORTH);
        centerPanel.add(mainContent, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());

        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BorderLayout());
        headerWrapper.add(headerPanel, BorderLayout.NORTH);
        headerWrapper.add(searchPanel, BorderLayout.SOUTH);

        getContentPane().add(headerWrapper, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
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

    public JPanel createProductCard(SanPham sp) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(195, 260));
        card.setMinimumSize(new Dimension(150, 230));
        card.setMaximumSize(new Dimension(150, 230));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Tag yêu thích
        JLabel yeuThich = new JLabel("Yêu thích");
        yeuThich.setForeground(Color.WHITE);
        yeuThich.setBackground(new Color(255, 51, 0));
        yeuThich.setOpaque(true);
        yeuThich.setFont(new Font("Arial", Font.BOLD, 11));
        yeuThich.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JPanel topTag = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topTag.setOpaque(false);
        topTag.add(yeuThich);
        card.add(topTag, BorderLayout.NORTH);

        // Ảnh
        JLabel imgLabel;
        if (sp.getHinhAnh() != null) {
            ImageIcon icon = new ImageIcon(sp.getHinhAnh());
            Image scaled = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(scaled));
        } else {
            imgLabel = new JLabel("No Image", SwingConstants.CENTER);
        }
        imgLabel.setPreferredSize(new Dimension(130, 130));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imgLabel, BorderLayout.CENTER);

        // Tên sản phẩm
        JLabel tenSp = new JLabel("<html><div style='width:140px;'>" + sp.getTenSanPham() + "</div></html>");
        tenSp.setFont(new Font("Arial", Font.PLAIN, 12));
        tenSp.setForeground(Color.BLACK);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setOpaque(false);
        namePanel.add(tenSp);

        // Mô tả
        JLabel moTa = new JLabel(sp.getMoTa() != null ? sp.getMoTa() : "");
        moTa.setFont(new Font("Arial", Font.ITALIC, 11));
        moTa.setForeground(Color.GRAY);

        JPanel moTaPanel = new JPanel();
        moTaPanel.setLayout(new BoxLayout(moTaPanel, BoxLayout.X_AXIS));
        moTaPanel.setOpaque(false);
        moTaPanel.add(moTa);

        // Giá
        JLabel giaGoc = new JLabel("<html><strike>₫" + sp.getGiaGoc() + "</strike></html>");
        giaGoc.setFont(new Font("Arial", Font.PLAIN, 12));
        giaGoc.setForeground(Color.GRAY);

        JLabel giaMoi = new JLabel("₫" + sp.getGiaBan());
        giaMoi.setForeground(new Color(231, 0, 0));
        giaMoi.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pricePanel.setOpaque(false);
        pricePanel.add(giaMoi);
        pricePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        pricePanel.add(giaGoc);

        // Panel chứa thông tin
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(namePanel);
        infoPanel.add(moTaPanel);
        infoPanel.add(pricePanel);

        card.add(infoPanel, BorderLayout.SOUTH);
        return card;
    }

    // Các getter methods
    public JList<String> getCategoryList() {
        return categoryList;
    }

    public JPanel getProductPanel() {
        return productPanel;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JButton getBtnCart() {
        return cartButton;
    }

    public int getMaKH() {
        return maKH;
    }

    public JLabel getUsernameLabel() {
        return username;
    }

    public JLabel getAvatarIcon() {
        return avatarIcon;
    }

    public KhachHang getKhachHang() {
        return kh;
    }

    // Phương thức thêm sản phẩm mới
    public void addProductToPanel(SanPham sp) {
        JPanel card = createProductCard(sp);
        productPanel.add(card);

        // Tính toán lại kích thước ưu tiên
        int componentCount = productPanel.getComponentCount();
        int rows = (int) Math.ceil(componentCount / 5.0);
        productPanel.setPreferredSize(new Dimension(
                5 * (180 + 10) + 20, // 5 sản phẩm * (width + spacing) + margin
                rows * (230 + 10) + 20
        ));

        productPanel.revalidate();
        productPanel.repaint();
    }
}
