/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 * @author Admin
 */

import Bridge.DBConnection;
import DAO.ChiTietHoaDonOrderDAO;
import DAO.GioHangDAO;
import DAO.HoaDonOrderDAO;
import DAO.TonKhoDAO;
import Model.GioHang;
import View.GioHangView;
import View.HoaDonOrderView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;


public class GioHangController {
    private GioHangView view;
    private GioHangDAO dao;
    private int maKH;
    private TonKhoDAO tonKhoDAO;

    public GioHangController(GioHangView view, GioHangDAO dao, int maKH) {
        this.view = view;
        this.dao = dao;
        this.maKH = maKH;
        attachEventListeners();
        loadCartData();
    }

    private void attachEventListeners() {
        view.setRemoveListener(removeListener);
        // Lắng nghe sự thay đổi số lượng
        view.setQuantityChangeListener((maGH, newQty, maSP) -> {
            try {
                tonKhoDAO = new TonKhoDAO(DBConnection.getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int khaDung = tonKhoDAO.getTongSoLuongKhaDung(maSP);

            if (newQty > khaDung) {
                JOptionPane.showMessageDialog(view, "Chỉ còn " + khaDung + " sản phẩm khả dụng.");
                loadCartData();
                return;
            }

            boolean success = dao.updateQuantity(maGH, newQty);
            if (!success) {
                JOptionPane.showMessageDialog(view, "Không thể cập nhật số lượng!");
            } else {
                view.updateLocalQuantity(maGH, newQty);
            }
        });
        view.addCheckoutListener(e -> {
            List<GioHang> selectedItems = view.getSelectedCartItems();
            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm để thanh toán.");
                return;
            }
            for (GioHang item : selectedItems) {
                dao.updateQuantity(item.getMaGH(), item.getSoLuong());
            }
            HoaDonOrderView orderView = new HoaDonOrderView(selectedItems, view, view, maKH);
            new HoaDonOrderController(new HoaDonOrderDAO(), new ChiTietHoaDonOrderDAO(), orderView, maKH);
            orderView.setVisible(true);
        });
        view.getCbxAll().addActionListener(e -> {
            boolean isSelected = view.getCbxAll().isSelected();
            // Đặt trạng thái chọn cho tất cả các item trong giỏ hàng
            for (Component comp : view.getPnProducts().getComponents()) {
                if (comp instanceof JPanel productPanel) {
                    Component[] productComponents = productPanel.getComponents();
                    for (Component inner : productComponents) {
                        if (inner instanceof JPanel leftPanel) {
                            for (Component sub : leftPanel.getComponents()) {
                                if (sub instanceof JCheckBox cbxItem) {
                                    cbxItem.setSelected(isSelected);
                                }
                            }
                        }
                    }
                }
            }
            // Cập nhật tổng tiền
            view.updateTotal(view.getCartItems());
        });
    }

    private final ActionListener removeListener = new ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int maGH = Integer.parseInt(e.getActionCommand());
            int confirm = javax.swing.JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                boolean success = dao.deleteItemById(maGH);
                if (success) {
                    javax.swing.JOptionPane.showMessageDialog(view, "Xóa thành công!");
                    loadCartData(); // gọi lại để cập nhật View
                } else {
                    javax.swing.JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                }
            }
        }
    };

    private void loadCartData() {
        List<GioHang> cartItems = dao.getALLGioHangByID(maKH);
        view.displayCartItems(cartItems, removeListener); // fix: truyền listener
    }

}