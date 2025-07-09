/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Model.HoaDonOrder;
/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HoaDonOrderDAO {

    public List<HoaDonOrder> getAll() {
        List<HoaDonOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM hdorder ORDER BY ma_hoa_don DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HoaDonOrder hoaDon = new HoaDonOrder();
                hoaDon.setMaHoaDon(rs.getInt("ma_hoa_don"));
                hoaDon.setMaKhachHang(rs.getInt("ma_khach_hang"));
                hoaDon.setNgayTao(rs.getTimestamp("ngay_tao"));
                hoaDon.setTongTien(rs.getBigDecimal("tong_tien"));
                hoaDon.setMakhuyenmai(rs.getString("ma_khuyen_mai"));
                hoaDon.setTrangThai(rs.getString("trang_thai"));

                list.add(hoaDon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int insert(HoaDonOrder hoaDon) {
        String sql = "INSERT INTO hdorder (ma_khach_hang, ngay_tao, tong_tien,ma_khuyen_mai, trang_thai) VALUES (?, ?, ?, ?, ?)";
        String trangThai = hoaDon.getTrangThai() != null ? hoaDon.getTrangThai() : "Đang xử lý";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            stmt.setInt(1, hoaDon.getMaKhachHang());
            stmt.setTimestamp(2, currentTime);
            stmt.setBigDecimal(3, hoaDon.getTongTien());
            stmt.setString(4, hoaDon.getMakhuyenmai());
            stmt.setString(5, trangThai);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean updateStatus(int maHoaDon, String trangThai) {
        String sql = "UPDATE hdorder SET trang_thai = ? WHERE ma_hoa_don = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trangThai);
            stmt.setInt(2, maHoaDon);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }
   public List<HoaDonOrder> getByMaKhachHang(int maKH) {
        List<HoaDonOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM hdorder WHERE ma_khach_hang = ? ORDER BY ngay_tao DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maKH);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDonOrder hd = new HoaDonOrder();
                hd.setMaHoaDon(rs.getInt("ma_hoa_don"));
                hd.setMaKhachHang(rs.getInt("ma_khach_hang"));
                hd.setNgayTao(rs.getTimestamp("ngay_tao"));
                hd.setTongTien(rs.getBigDecimal("tong_tien"));
                hd.setTrangThai(rs.getString("trang_thai"));

                list.add(hd); // CHƯA gán chi tiết ở đây
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}

