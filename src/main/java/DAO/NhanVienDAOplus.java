/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;

/**
 *
 * @author DUNG LE
 */
public class NhanVienDAOplus {
     public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setma_nv(rs.getInt("ma_nv"));
                nv.setHoten(rs.getString("ten_nv"));
                nv.setCccd(rs.getString("CCCD"));
                nv.setSdt(rs.getString("sdt"));
                nv.setChucvu(rs.getString("chucvu"));
                nv.setEmail(rs.getString("email"));
                nv.setGioitinh(rs.getString("gioitinh"));
                nv.setGhichu(rs.getString("ghichu"));
                Blob blob = rs.getBlob("anh");
                if (blob != null) {
                    nv.setAnh(blob.getBytes(1, (int) blob.length()));
                } else {
                    nv.setAnh(null);
                }

                list.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm mới nhân viên
    public boolean insertNhanVien(NhanVien nv) {
    String sql = "INSERT INTO nhanvien(ten_nv, CCCD, sdt, chucvu, email, gioitinh, ghichu, anh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, nv.getHoten());
        stmt.setString(2, nv.getCccd());
        stmt.setString(3, nv.getSdt());
        stmt.setString(4, nv.getChucvu());
        stmt.setString(5, nv.getEmail());
        stmt.setString(6, nv.getGioitinh());
        stmt.setString(7, nv.getGhichu());

        if (nv.getAnh() != null) {
            stmt.setBlob(8, new java.io.ByteArrayInputStream(nv.getAnh()));
        } else {
            stmt.setNull(8, java.sql.Types.BLOB);
        }

        int affected = stmt.executeUpdate();
        if (affected > 0) {
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    nv.setma_nv(generatedId); // Gán ID mới vào đối tượng
                }
            }
            return true;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


    // Cập nhật nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE nhanvien SET ten_nv=?, CCCD=?, sdt=?, chucvu=?, email=?, gioitinh=?, ghichu=?, anh=? WHERE ma_nv=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nv.getHoten());
            stmt.setString(2, nv.getCccd());
            stmt.setString(3, nv.getSdt());
            stmt.setString(4, nv.getChucvu());
            stmt.setString(5, nv.getEmail());
            stmt.setString(6, nv.getGioitinh());
            stmt.setString(7, nv.getGhichu());

            if (nv.getAnh() != null) {
                stmt.setBlob(8, new java.io.ByteArrayInputStream(nv.getAnh()));
            } else {
                stmt.setNull(8, java.sql.Types.BLOB);
            }

            stmt.setInt(9, nv.getma_nv());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa nhân viên theo ID
public boolean deleteNhanVien(int ma_nv) {
    String sql = "DELETE FROM nhanvien WHERE ma_nv=?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, ma_nv);
        return stmt.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

// Xóa tất cả nhân viên
public boolean deleteAllNhanVien() {
    String sql = "DELETE FROM nhanvien";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        int rowsDeleted = stmt.executeUpdate();
        return rowsDeleted > 0;  // true nếu có dòng bị xóa

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    // Tìm nhân viên theo từ khóa (họ tên, cccd, sdt)
    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE LOWER(ten_nv) LIKE ? OR LOWER(CCCD) LIKE ? OR LOWER(sdt) LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            stmt.setString(3, likeKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setma_nv(rs.getInt("ma_nv"));
                    nv.setHoten(rs.getString("ten_nv"));
                    nv.setCccd(rs.getString("CCCD"));
                    nv.setSdt(rs.getString("sdt"));
                    nv.setChucvu(rs.getString("chucvu"));
                    nv.setEmail(rs.getString("email"));
                    nv.setGioitinh(rs.getString("gioitinh"));
                    nv.setGhichu(rs.getString("ghichu"));
                    Blob blob = rs.getBlob("anh");
                    if (blob != null) {
                        nv.setAnh(blob.getBytes(1, (int) blob.length()));
                    } else {
                        nv.setAnh(null);
                    }
                    list.add(nv);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy nhân viên theo ma_nv (để sửa)
    public NhanVien getNhanVienById(int ma_nv) {
        String sql = "SELECT * FROM nhanvien WHERE ma_nv=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ma_nv);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setma_nv(rs.getInt("ma_nv"));
                    nv.setHoten(rs.getString("ten_nv"));
                    nv.setCccd(rs.getString("CCCD"));
                    nv.setSdt(rs.getString("sdt"));
                    nv.setChucvu(rs.getString("chucvu"));
                    nv.setEmail(rs.getString("email"));
                    nv.setGioitinh(rs.getString("gioitinh"));
                    nv.setGhichu(rs.getString("ghichu"));
                    Blob blob = rs.getBlob("anh");
                    if (blob != null) {
                        nv.setAnh(blob.getBytes(1, (int) blob.length()));
                    } else {
                        nv.setAnh(null);
                    }
                    return nv;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
