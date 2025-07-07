/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ChiTietSPDAO;
import View.ChiTietSanPhamBH;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;

/**
 *
 * @author Admin
 */
public class ChiTietSPController {

    private ChiTietSanPhamBH view;
    private ChiTietSPDAO dao;
    private int maKhachHang;  // Giả sử bạn lấy từ session đăng nhập
    private Connection connection;

    public ChiTietSPController(ChiTietSanPhamBH view, ChiTietSPDAO dao, int maKhachHang, Connection connection) {
        this.view = view;
        this.dao = new ChiTietSPDAO();
        this.maKhachHang = maKhachHang;
        this.connection = connection;
        addEvents();
    }

    private void addEvents() {
        view.getBtnThem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int maSanPham = view.getMaSanPham();
                    int soLuong = Integer.parseInt(view.getTxtSoLuong().getText().trim());

                    if (soLuong <= 0) {
                        JOptionPane.showMessageDialog(view, "Số lượng phải lớn hơn 0!");
                        return;
                    }
                    if (view.getSoLuongTon() == 0) {
                        JOptionPane.showMessageDialog(view, "Sản phẩm này đã hết hàng!");
                        return;
                    }
                    boolean result = dao.addCart(maKhachHang, maSanPham, soLuong);
                    if (result) {
                        JOptionPane.showMessageDialog(view, "Thêm vào giỏ hàng thành công!");
                        view.dispose(); // Đóng dialog nếu cần
                    } else {
                        JOptionPane.showMessageDialog(view, "Không thể thêm vào giỏ hàng.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Số lượng không hợp lệ!");
                }
            }
        });

        // xử lý button tăng giảm số lượng
        view.getBtnCong().addActionListener(e -> {
            try {

                int current = Integer.parseInt(view.getTxtSoLuong().getText().trim());
                if (current < view.getSoLuongTon()) {
                    view.getTxtSoLuong().setText(String.valueOf(current + 1));
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Không thể vượt quá số lượng tồn kho (" + view.getSoLuongTon() + ")",
                            "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                view.getTxtSoLuong().setText("1");
            }
        });

        view.getBtnTru().addActionListener(e -> {
            try {
                int current = Integer.parseInt(view.getTxtSoLuong().getText().trim());
                if (current > 1) {
                    view.getTxtSoLuong().setText(String.valueOf(current - 1));
                }
            } catch (NumberFormatException ex) {
                view.getTxtSoLuong().setText("1");
            }
        });

    }
}
