/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import Model.ChiTietSanPham;
import Model.GioHang;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class ChiTietSPDAO {

    public boolean addCart(int maKhachHang, int maSanPham, int soLuong) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        GioHangDAO gioHangDAO = new GioHangDAO();
        GioHang existingItem = gioHangDAO.getGioHangItemByMaKHAndMaSP(maKhachHang, maSanPham);

        if (existingItem != null) {
            // Nếu đã có thì cập nhật số lượng
            int newQuantity = existingItem.getSoLuong() + soLuong;
            return gioHangDAO.updateQuantity(existingItem.getMaGH(), newQuantity);
        } else {
            // Nếu chưa có thì thêm mới
            String sql = "INSERT INTO giohang (MaKH,ma_san_pham,so_luong) VALUES (?,?,?)";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, maKhachHang);
                ps.setInt(2, maSanPham);
                ps.setInt(3, soLuong);
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
