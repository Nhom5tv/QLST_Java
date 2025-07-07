package DAO;

import Bridge.DBConnection;
import Model.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    public static boolean insert(PhieuNhap pn) {
        String sql = "INSERT INTO phieu_nhap (ma_phieu_nhap, ncc_id, ngay_nhap, ghi_chu) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Tự động sinh mã phiếu mới
            int nextId = generateNextMaPhieuNhap(conn);
            pn.setMaPhieu(String.valueOf(nextId));  // Gán vào đối tượng để tiện sử dụng tiếp

            // Thiết lập tham số cho PreparedStatement
            ps.setInt(1, nextId);
            ps.setString(2, pn.getMaNCC());
            ps.setDate(3, new java.sql.Date(pn.getNgayNhap().getTime()));
            ps.setString(4, pn.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(PhieuNhap pn) {
        String sql = "UPDATE phieu_nhap SET ngay_nhap=?, ncc_id=?, ghi_chu=? WHERE ma_phieu_nhap=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(pn.getNgayNhap().getTime()));
            ps.setString(2, pn.getMaNCC());
            ps.setString(3, pn.getGhiChu());
            ps.setString(4, pn.getMaPhieu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(String maPhieu) {
        String sql = "DELETE FROM phieu_nhap WHERE ma_phieu_nhap=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<PhieuNhap> findByNgayNhap(java.util.Date ngayNhap) {
    List<PhieuNhap> list = new ArrayList<>();
    String sql = "SELECT pn.ma_phieu_nhap, pn.ncc_id, ncc.ten_ncc, pn.ngay_nhap, pn.ghi_chu " +
                 "FROM phieu_nhap pn " +
                 "JOIN nha_cung_cap ncc ON pn.ncc_id = ncc.ncc_id " +
                 "WHERE pn.ngay_nhap = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, new java.sql.Date(ngayNhap.getTime()));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PhieuNhap pn = new PhieuNhap(
                    rs.getString("ma_phieu_nhap"),
                    rs.getString("ncc_id"),
                    rs.getString("ten_ncc"),
                    rs.getDate("ngay_nhap"),
                    rs.getString("ghi_chu")
            );
            list.add(pn);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


    public static List<PhieuNhap> getAll() {
    List<PhieuNhap> list = new ArrayList<>();
    String sql = "SELECT pn.ma_phieu_nhap, pn.ncc_id, ncc.tenncc AS ten_ncc, " +
                 "pn.ngay_nhap, pn.ghi_chu " +
                 "FROM phieu_nhap pn " +
                 "JOIN ncc ncc ON pn.ncc_id = ncc.ncc_id " +
                 "ORDER BY pn.ngay_nhap DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            PhieuNhap pn = new PhieuNhap(
                    rs.getString("ma_phieu_nhap"),
                    rs.getString("ncc_id"),
                    rs.getString("ten_ncc"),
                    rs.getDate("ngay_nhap"),
                    rs.getString("ghi_chu")
            );
            list.add(pn);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}



    public static int generateNextMaPhieuNhap(Connection conn) {
        int nextId = 1;
        try {
            String query = "SELECT MAX(ma_phieu_nhap) FROM phieu_nhap"; // Sửa tên bảng
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
    public boolean updateMaPhieuNhap(String oldId, int newId) {
    String sql = "UPDATE phieu_nhap SET ma_phieu_nhap = ? WHERE ma_phieu_nhap = ?";
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
