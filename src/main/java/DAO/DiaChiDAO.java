/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Admin
 */
import Bridge.DBConnection;
import Model.DiaChi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiaChiDAO {

    // Lấy tất cả địa chỉ của khách hàng theo mã khách hàng
    public static ArrayList<DiaChi> getAddressesByCustomerId(int customerId) {
        ArrayList<DiaChi> addresses = new ArrayList<>();
        String sql = "SELECT * FROM diachigiaohang WHERE MaKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DiaChi address = new DiaChi(
                        rs.getInt("Ma_dia_chi"),
                        rs.getString("TenNguoiNhan"),
                        rs.getString("SoDienThoai"),
                        rs.getString("DiaChiChiTiet"),
                        rs.getInt("MaKH")
                );
                addresses.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    // Thêm địa chỉ mới
    public boolean addAddress(DiaChi address) {
        String sql = "INSERT INTO diachigiaohang (TenNguoiNhan, SoDienThoai, DiaChiChiTiet, MaKH) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getName());
            ps.setString(2, address.getPhoneNumber());
            ps.setString(3, address.getDetailAddress());
            ps.setInt(4, address.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật địa chỉ
    public boolean updateAddress(DiaChi address) {
        String sql = "UPDATE diachigiaohang SET TenNguoiNhan = ?, SoDienThoai = ?, DiaChiChiTiet = ?, MaKH = ? WHERE Ma_dia_chi = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getName());
            ps.setString(2, address.getPhoneNumber());
            ps.setString(3, address.getDetailAddress());
            ps.setInt(4, address.getCustomerId());
            ps.setInt(5, address.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa địa chỉ
    public boolean deleteAddress(int addressId) {
        String sql = "DELETE FROM diachigiaohang WHERE Ma_dia_chi = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addressId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Lấy địa chỉ đầu tiên làm địa chỉ mặc định (có thể sửa lại nếu có cột is_default)
    public DiaChi getDefaultDiaChi(int customerId) {
        String sql = "SELECT * FROM diachigiaohang WHERE MaKH = ? ORDER BY Ma_dia_chi ASC";// hoặc ORDER BY NgayTao DESC nếu có
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new DiaChi(
                        rs.getInt("Ma_dia_chi"),
                        rs.getString("TenNguoiNhan"),
                        rs.getString("SoDienThoai"),
                        rs.getString("DiaChiChiTiet"),
                        rs.getInt("MaKH")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
