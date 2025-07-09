/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 * @author Admin
 */

import DAO.ChiTietHoaDonOrderDAO;
import DAO.HoaDonOrderDAO;
import Model.HoaDonOrder;
import View.DonHangCard;
import View.LichSuMuaHangView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LichSuMuaHangController {
    private final LichSuMuaHangView view;
    private final HoaDonOrderDAO hoaDonDAO = new HoaDonOrderDAO();
    private final ChiTietHoaDonOrderDAO chiTietDAO = new ChiTietHoaDonOrderDAO();
    private final int maKH;

    private final String[] statuses = {"Tất cả", "Đang xử lý", "Đang giao", "Hoàn thành", "Hủy"};
    private String currentFilter = "Tất cả";

    public LichSuMuaHangController(LichSuMuaHangView view, int maKH) {
        this.view = view;
        this.maKH = maKH;
        init();
    }

    private void init() {
        // Tabs
        for (String status : statuses) {
            JLabel tab = new JLabel(status);
            tab.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tab.setForeground(status.equals(currentFilter) ? Color.RED : Color.BLACK);
            tab.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    currentFilter = status;
                    updateTabStyles();
                    load();
                }
            });
            view.tabPanel.add(tab);
        }

        view.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                load();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                load();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        load();
    }

    private void updateTabStyles() {
        for (Component comp : view.tabPanel.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setForeground(label.getText().equals(currentFilter) ? Color.RED : Color.BLACK);
            }
        }
    }

    private void load() {
        view.orderListPanel.removeAll();
        List<HoaDonOrder> orders = hoaDonDAO.getByMaKhachHang(maKH);
        String keyword = view.searchField.getText().trim().toLowerCase();

        for (HoaDonOrder order : orders) {
            order.setChiTietHoaDons(chiTietDAO.getByMaHoaDon(order.getMaHoaDon()));

            boolean matchStatus = currentFilter.equals("Tất cả") || order.getTrangThai().equalsIgnoreCase(currentFilter);
            boolean matchSearch = keyword.isEmpty()
                    || String.valueOf(order.getMaHoaDon()).contains(keyword)
                    || order.getChiTietHoaDons().stream()
                    .anyMatch(ct -> ct.getTenSanPham().toLowerCase().contains(keyword));

            if (matchStatus && matchSearch) {
                DonHangCard card = new DonHangCard(order);

                // Thêm sự kiện cho nút hủy (nếu có)
                if ("Đang xử lý".equals(order.getTrangThai())) {
                    card.addCancelListener(e -> handleCancelOrder(card.getMaHoaDon()));
                }

                view.orderListPanel.add(card);
            }
        }

        view.revalidate();
        view.repaint();
    }

    private void handleCancelOrder(int maHoaDon) {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc chắn muốn hủy đơn hàng #" + maHoaDon + "?",
                "Xác nhận hủy đơn",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = hoaDonDAO.updateStatus(maHoaDon, "Hủy");
            if (success) {
                JOptionPane.showMessageDialog(view, "Đã hủy đơn hàng thành công!");
                load(); // Tải lại danh sách
            } else {
                JOptionPane.showMessageDialog(view, "Hủy đơn hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


