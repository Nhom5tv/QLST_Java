/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Model.ChiTietHoaDonOrder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ChiTietHoaDonOrderDAO {

    public boolean insert(ChiTietHoaDonOrder hoaDonChiTiet) {
        String sql = "INSERT INTO cthdorder (ma_hoa_don, ma_san_pham, so_luong, don_gia, thanh_tien, ma_lo_hang) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            double thanhTien = hoaDonChiTiet.getSoLuong() * hoaDonChiTiet.getDonGia();
            stmt.setInt(1, hoaDonChiTiet.getMaHoaDon());
            stmt.setInt(2, hoaDonChiTiet.getMaSanPham());
            stmt.setInt(3, hoaDonChiTiet.getSoLuong());
            stmt.setDouble(4, hoaDonChiTiet.getDonGia());
            stmt.setDouble(5, thanhTien);
            stmt.setObject(6, hoaDonChiTiet.getMaLoHang()); // Giả sử maLoHang có thể null

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDonOrder> getByMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDonOrder> chiTietList = new ArrayList<>();

        String sql = """
        SELECT c.ma_san_pham, s.ten_san_pham, s.hinh_anh, 
               c.so_luong, c.don_gia, c.ma_lo_hang
        FROM cthdorder c
        JOIN san_pham s ON c.ma_san_pham = s.ma_san_pham
        WHERE c.ma_hoa_don = ?
    """;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maHoaDon);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDonOrder chiTiet = new ChiTietHoaDonOrder();

                    chiTiet.setMaSanPham(rs.getInt("ma_san_pham"));
                    chiTiet.setTenSanPham(rs.getString("ten_san_pham"));
                    chiTiet.setAnh(rs.getBytes("hinh_anh"));
                    chiTiet.setSoLuong(rs.getInt("so_luong"));
                    chiTiet.setDonGia(rs.getDouble("don_gia"));
                    chiTiet.setMaLoHang(rs.getInt("ma_lo_hang"));

                    chiTietList.add(chiTiet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chiTietList;
    }

}
