/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import model.DanhMucSanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DanhMucSanPhamDAO implements DAO<DanhMucSanPham> {
    private Connection conn;

    public DanhMucSanPhamDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<DanhMucSanPham> getAll() {
        List<DanhMucSanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM danh_muc_san_pham";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<DanhMucSanPham> getById(int id) {
        String sql = "SELECT * FROM danh_muc_san_pham WHERE ma_danh_muc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extract(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public boolean insert(DanhMucSanPham d) {
        String sql = "INSERT INTO danh_muc_san_pham (ten_danh_muc, ma_ky_hieu, ma_cha, mo_ta, trang_thai) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getTenDanhMuc());
            ps.setString(2, d.getMaKyHieu());
            if (d.getMaCha() != null) ps.setInt(3, d.getMaCha());
            else ps.setNull(3, Types.INTEGER);
            ps.setString(4, d.getMoTa());
            ps.setInt(5, d.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int insertAndReturnId(DanhMucSanPham d) {
        String sql = "INSERT INTO danh_muc_san_pham (ten_danh_muc, ma_ky_hieu, ma_cha, mo_ta, trang_thai) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getTenDanhMuc());
            ps.setString(2, d.getMaKyHieu()); // có thể null nếu sinh sau
            if (d.getMaCha() != null) {
                ps.setInt(3, d.getMaCha());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, d.getMoTa());
            ps.setInt(5, d.getTrangThai());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // trả về ma_danh_muc vừa được sinh
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // thất bại
    }


    @Override
    public boolean update(DanhMucSanPham d) {
        String sql = "UPDATE danh_muc_san_pham SET ten_danh_muc = ?, ma_ky_hieu = ?, ma_cha = ?, mo_ta = ?, trang_thai = ? WHERE ma_danh_muc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getTenDanhMuc());
            ps.setString(2, d.getMaKyHieu());
            if (d.getMaCha() != null) ps.setInt(3, d.getMaCha());
            else ps.setNull(3, Types.INTEGER);
            ps.setString(4, d.getMoTa());
            ps.setInt(5, d.getTrangThai());
            ps.setInt(6, d.getMaDanhMuc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM danh_muc_san_pham WHERE ma_danh_muc = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm lấy dữ liệu từ ResultSet chuyển đổi thành Object tương ứng
    private DanhMucSanPham extract(ResultSet rs) throws SQLException {
        return new DanhMucSanPham(
            rs.getInt("ma_danh_muc"),
            rs.getString("ten_danh_muc"),
            rs.getString("ma_ky_hieu"),
            rs.getObject("ma_cha") != null ? rs.getInt("ma_cha") : null,
            rs.getString("mo_ta"),
            rs.getInt("trang_thai")
        );
    }
}

