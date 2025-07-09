package View;

import Model.ChiTietHoaDonOrder;
import Model.HoaDonOrder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DonHangCard extends JPanel {
    private JButton btnHuy;
    private final HoaDonOrder order;

    public DonHangCard(HoaDonOrder order) {
        this.order = order;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel maHoaDonLabel = new JLabel("Đơn hàng #" + order.getMaHoaDon());
        maHoaDonLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel statusLabel = new JLabel(order.getTrangThai());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(getStatusColor(order.getTrangThai()));
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(maHoaDonLabel, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        // Chi tiết sản phẩm
        JPanel productPanel = new JPanel(new GridLayout(order.getChiTietHoaDons().size(), 1, 5, 5));
        productPanel.setBackground(Color.WHITE);

        for (ChiTietHoaDonOrder item : order.getChiTietHoaDons()) {
            JLabel productLabel = new JLabel(String.format("• %s x%d - %,.0fđ",
                    item.getTenSanPham(),
                    item.getSoLuong(),
                    item.getDonGia() * item.getSoLuong()));
            productLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            productPanel.add(productLabel);
        }

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);

        JLabel totalLabel = new JLabel("Tổng: " + String.format("%,.0fđ", order.getTongTien()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLabel.setForeground(Color.RED);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        footerPanel.add(totalLabel, BorderLayout.CENTER);

        // Thêm nút Hủy nếu trạng thái là "Đang xử lý"
        if ("Đang xử lý".equals(order.getTrangThai())) {
            btnHuy = new JButton("Hủy đơn");
            btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnHuy.setBackground(new Color(255, 87, 87));
            btnHuy.setForeground(Color.WHITE);
            btnHuy.setFocusPainted(false);
            btnHuy.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(btnHuy);

            footerPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(productPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    private Color getStatusColor(String status) {
        return switch (status) {
            case "Đang xử lý" -> new Color(255, 153, 0); // Cam
            case "Đang giao" -> new Color(0, 102, 204); // Xanh dương
            case "Hoàn thành" -> new Color(0, 153, 0); // Xanh lá
            case "Hủy" -> new Color(204, 0, 0); // Đỏ
            default -> Color.BLACK;
        };
    }

    public void addCancelListener(ActionListener listener) {
        if (btnHuy != null) {
            btnHuy.addActionListener(listener);
        }
    }

    public int getMaHoaDon() {
        return order.getMaHoaDon();
    }
}