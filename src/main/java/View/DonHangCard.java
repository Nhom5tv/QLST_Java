/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.ChiTietHoaDonOrder;
import Model.HoaDonOrder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Admin
 */
public class DonHangCard extends JPanel {
    public DonHangCard(HoaDonOrder order) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JLabel header = new JLabel("Mã hóa đơn: #" + order.getMaHoaDon() + "  |  " + order.getTrangThai());
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel content = new JPanel(new GridLayout(order.getChiTietHoaDons().size(), 1));
        
        content.setBackground(Color.WHITE);
        for (ChiTietHoaDonOrder item : order.getChiTietHoaDons()) {
            JLabel label = new JLabel("• " + item.getTenSanPham() + " - x" + item.getSoLuong() + " - "
                    + String.format("%,.0f VND", item.getDonGia() * item.getSoLuong()));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            content.add(label);
        }

        JLabel footer = new JLabel("Thành tiền: " + String.format("%,.0f VND", order.getTongTien()));
        footer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        footer.setForeground(Color.RED);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        footer.setHorizontalAlignment(SwingConstants.RIGHT);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        this.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));
        this.setAlignmentX(LEFT_ALIGNMENT);
    }
}
