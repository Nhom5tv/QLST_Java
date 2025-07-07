/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import Model.GioHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class GioHangDAO {

    public ArrayList<GioHang> getALLGioHangByID(int maKH) {
        ArrayList<GioHang> list = new ArrayList<>();
        String sql = "SELECT gh.*, sp.ten_san_pham, sp.gia_ban, sp.hinh_anh "
                + "FROM giohang gh "
                + "JOIN san_pham sp ON gh.ma_san_pham = sp.ma_san_pham "
                + "WHERE gh.MaKH = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKH);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                GioHang gh = new GioHang();
                gh.setMaGH(rs.getInt("ma_gio_hang"));
                gh.setMaKH(rs.getInt("MaKH"));
                gh.setMaSP(rs.getInt("ma_san_pham"));
                gh.setSoLuong(rs.getInt("So_Luong"));
                gh.setTenSp(rs.getString("ten_san_pham"));
                gh.setGiaban(rs.getDouble("gia_ban"));
                gh.setAnh(rs.getBytes("hinh_anh"));

                list.add(gh);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi lấy dữ liệu giỏ hàng: " + e.getMessage());
        }
        return list;
    }

    public boolean deleteItemById(int maGioHang) {
        String sql = "DELETE FROM giohang WHERE ma_gio_hang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maGioHang);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateQuantity(int maGioHang, int soLuong) {
        String sql = "UPDATE giohang SET So_Luong = ? WHERE ma_gio_hang = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, soLuong);
            pstmt.setInt(2, maGioHang);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertGioHang(int maKH, int maSP, int soLuong, double giaBan) {
        String sql = "INSERT INTO giohang (MaKH, ma_san_pham, So_Luong) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKH);
            pstmt.setInt(2, maSP);
            pstmt.setInt(3, soLuong);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public GioHang getGioHangItemByMaKHAndMaSP(int maKH, int maSP) {
        String sql = "SELECT * FROM giohang WHERE MaKH = ? AND ma_san_pham = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maKH);
            pstmt.setInt(2, maSP);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                GioHang gh = new GioHang();
                gh.setMaGH(rs.getInt("ma_gio_hang"));
                gh.setMaKH(rs.getInt("MaKH"));
                gh.setMaSP(rs.getInt("ma_san_pham"));
                gh.setSoLuong(rs.getInt("So_Luong"));
                return gh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
