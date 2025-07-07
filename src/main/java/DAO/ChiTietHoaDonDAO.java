/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author DUNG LE
 */
import Model.ChiTietHoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private final Connection conn;

    public ChiTietHoaDonDAO(Connection conn) {
        this.conn = conn;
    }


    public void save(ChiTietHoaDon chiTiet) throws SQLException {
        String sql = "INSERT INTO chi_tiet_hoa_don (ma_hoa_don, ma_san_pham, so_luong, don_gia, giam_gia,ma_lo_hang) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaHoaDon());
            pstmt.setInt(2, chiTiet.getMaSanPham());
            pstmt.setInt(3, chiTiet.getSoLuong());
            pstmt.setBigDecimal(4, chiTiet.getDonGia());
            pstmt.setBigDecimal(5, chiTiet.getGiamGia());
            pstmt.setInt(6, chiTiet.getMaLo());
            pstmt.executeUpdate();
        }
    }


    public boolean insert(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO chi_tiet_hoa_don (ma_hoa_don, ma_san_pham, so_luong, don_gia, giam_gia) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cthd.getMaHoaDon());
            stmt.setInt(2, cthd.getMaSanPham());
            stmt.setInt(3, cthd.getSoLuong());
            stmt.setBigDecimal(4, cthd.getDonGia());
            stmt.setBigDecimal(5, cthd.getGiamGia());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDon> getByMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM chi_tiet_hoa_don WHERE ma_hoa_don = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maHoaDon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setMaHoaDon(rs.getInt("ma_hoa_don"));
                    cthd.setMaSanPham(rs.getInt("ma_san_pham"));
                    cthd.setSoLuong(rs.getInt("so_luong"));
                    cthd.setDonGia(rs.getBigDecimal("don_gia"));
                    cthd.setGiamGia(rs.getBigDecimal("giam_gia"));
                    list.add(cthd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteByMaHoaDon(int maHoaDon) {
        String sql = "DELETE FROM chi_tiet_hoa_don WHERE ma_hoa_don = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maHoaDon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
