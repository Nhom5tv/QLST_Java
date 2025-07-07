package DAO;

import Bridge.DBConnection;
import Model.LoHang;
import java.sql.*;
import java.util.ArrayList;

public class LoHangDAO {

    public ArrayList<LoHang> getAllLoHang() {
        ArrayList<LoHang> list = new ArrayList<>();
        String sql = "SELECT lh.*, sp.ten_san_pham " +
                     "FROM lo_hang lh " +
                     "JOIN san_pham sp ON lh.ma_san_pham = sp.ma_san_pham";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LoHang lh = new LoHang(
                    rs.getInt("ma_lo_hang"),
                    rs.getString("ma_phieu_nhap"),
                    rs.getInt("ma_san_pham"),
                     rs.getString("ten_san_pham"),
                    rs.getInt("so_luong"),
                    rs.getDouble("don_gia"),
                    rs.getDate("ngay_san_xuat"),
                    rs.getDate("han_su_dung"),
                    rs.getString("ghi_chu")
                     // Thêm tên sản phẩm
                );
                list.add(lh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themLoHang(LoHang lh) {
        String sql = "INSERT INTO lo_hang (ma_lo_hang,ma_phieu_nhap, ma_san_pham, so_luong, don_gia, ngay_san_xuat, han_su_dung, ghi_chu) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lh.getMaLoHang());
            ps.setString(2, lh.getMaPhieuNhap());
            ps.setInt(3, lh.getMaSanPham());
            ps.setInt(4, lh.getSoLuong());
            ps.setDouble(5, lh.getDonGia());
            ps.setDate(6, new java.sql.Date(lh.getNgaySanXuat().getTime()));
            ps.setDate(7, new java.sql.Date(lh.getHanSuDung().getTime()));
            ps.setString(8, lh.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatLoHang(LoHang lh) {
        String sql = "UPDATE lo_hang SET ma_phieu_nhap=?, ma_san_pham=?, so_luong=?, don_gia=?, ngay_san_xuat=?, han_su_dung=?, ghi_chu=? WHERE ma_lo_hang=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lh.getMaPhieuNhap());
            ps.setInt(2, lh.getMaSanPham());
            ps.setInt(3, lh.getSoLuong());
            ps.setDouble(4, lh.getDonGia());
            ps.setDate(5, new java.sql.Date(lh.getNgaySanXuat().getTime()));
            ps.setDate(6, new java.sql.Date(lh.getHanSuDung().getTime()));
            ps.setString(7, lh.getGhiChu());
            ps.setInt(8, lh.getMaLoHang());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaLoHang(int maLoHang) {
        String sql = "DELETE FROM lo_hang WHERE ma_lo_hang=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maLoHang);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

   public ArrayList<LoHang> timKiemLoHang(String keyword) {
    ArrayList<LoHang> list = new ArrayList<>();
    String sql = "SELECT lh.*, sp.ten_san_pham " +
                 "FROM lo_hang lh " +
                 "JOIN san_pham sp ON lh.ma_san_pham = sp.ma_san_pham " +
                 "WHERE CAST(lh.ma_lo_hang AS CHAR) LIKE ? OR LOWER(sp.ten_san_pham) LIKE ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        String searchPattern = "%" + keyword.toLowerCase() + "%";
        ps.setString(1, searchPattern); // Tìm theo mã lô hàng
        ps.setString(2, searchPattern); // Tìm theo tên sản phẩm

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LoHang lh = new LoHang(
                rs.getInt("ma_lo_hang"),
                rs.getString("ma_phieu_nhap"),
                rs.getInt("ma_san_pham"),
                rs.getString("ten_san_pham"),
                rs.getInt("so_luong"),
                rs.getDouble("don_gia"),
                rs.getDate("ngay_san_xuat"),
                rs.getDate("han_su_dung"),
                rs.getString("ghi_chu")
            );
            list.add(lh);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


    public static int generateNextMaLoHang(Connection conn) {
        int nextId = 1;
        try {
            String query = "SELECT MAX(ma_lo_hang) FROM lo_hang";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    public boolean updateMaLoHang(String oldId, int newId) {
        String sql = "UPDATE lo_hang SET ma_lo_hang = ? WHERE ma_lo_hang = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newId);
            ps.setInt(2, Integer.parseInt(oldId));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
