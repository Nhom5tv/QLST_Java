package DAO;

import Bridge.DBConnection;
import Model.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM qlkhuyenmai";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getString("ma_khuyen_mai"),
                        rs.getString("ten_khuyen_mai"),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getInt("phan_tram_giam"),
                        rs.getInt("so_luong"),
                        rs.getBigDecimal("tong_tien_toi_thieu"),
                        rs.getObject("so_luong_sp_toi_thieu") != null ? rs.getInt("so_luong_sp_toi_thieu") : null,
                        rs.getString("ghi_chu")
                );
                list.add(km);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO qlkhuyenmai(ma_khuyen_mai, ten_khuyen_mai, ngay_bat_dau, ngay_ket_thuc, phan_tram_giam, so_luong, tong_tien_toi_thieu, so_luong_sp_toi_thieu, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setDate(3, km.getNgayBatDau());
            ps.setDate(4, km.getNgayKetThuc());
            ps.setInt(5, km.getPhanTramGiam());
            ps.setInt(6, km.getSoLuong());
            ps.setBigDecimal(7, km.getTongTienToiThieu());

            if (km.getSoLuongSpToiThieu() != null)
                ps.setInt(8, km.getSoLuongSpToiThieu());
            else
                ps.setNull(8, Types.INTEGER);

            ps.setString(9, km.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE qlkhuyenmai SET ten_khuyen_mai=?, ngay_bat_dau=?, ngay_ket_thuc=?, " +
                "phan_tram_giam=?, so_luong=?, tong_tien_toi_thieu=?, so_luong_sp_toi_thieu=?, ghi_chu=? " +
                "WHERE ma_khuyen_mai=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getTenKhuyenMai());
            ps.setDate(2, km.getNgayBatDau());
            ps.setDate(3, km.getNgayKetThuc());
            ps.setInt(4, km.getPhanTramGiam());
            ps.setInt(5, km.getSoLuong());
            ps.setBigDecimal(6, km.getTongTienToiThieu());

            if (km.getSoLuongSpToiThieu() != null)
                ps.setInt(7, km.getSoLuongSpToiThieu());
            else
                ps.setNull(7, Types.INTEGER);

            ps.setString(8, km.getGhiChu());
            ps.setString(9, km.getMaKhuyenMai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteKhuyenMai(String maKhuyenMai) {
        String sql = "DELETE FROM qlkhuyenmai WHERE ma_khuyen_mai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhuyenMai);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public KhuyenMai getById(String maKhuyenMai) {
        String sql = "SELECT * FROM qlkhuyenmai WHERE ma_khuyen_mai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhuyenMai);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhuyenMai(
                            rs.getString("ma_khuyen_mai"),
                            rs.getString("ten_khuyen_mai"),
                            rs.getDate("ngay_bat_dau"),
                            rs.getDate("ngay_ket_thuc"),
                            rs.getInt("phan_tram_giam"),
                            rs.getInt("so_luong"),
                            rs.getBigDecimal("tong_tien_toi_thieu"),
                            rs.getObject("so_luong_sp_toi_thieu") != null ? rs.getInt("so_luong_sp_toi_thieu") : null,
                            rs.getString("ghi_chu")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<KhuyenMai> searchKhuyenMai(String keyword) {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM qlkhuyenmai WHERE ma_khuyen_mai LIKE ? OR ten_khuyen_mai LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhuyenMai km = new KhuyenMai(
                            rs.getString("ma_khuyen_mai"),
                            rs.getString("ten_khuyen_mai"),
                            rs.getDate("ngay_bat_dau"),
                            rs.getDate("ngay_ket_thuc"),
                            rs.getInt("phan_tram_giam"),
                            rs.getInt("so_luong"),
                            rs.getBigDecimal("tong_tien_toi_thieu"),
                            rs.getObject("so_luong_sp_toi_thieu") != null ? rs.getInt("so_luong_sp_toi_thieu") : null,
                            rs.getString("ghi_chu")
                    );
                    list.add(km);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean ghiNhanSuDungVoucher(String maKhuyenMai, int maKhachHang) {
        String sqlKiemTra = "SELECT so_luong FROM qlkhuyenmai WHERE ma_khuyen_mai = ?";
        String sqlInsert = "INSERT INTO taikhoanvoucher(ma_khuyen_mai, ma_khach_hang, ngay_su_dung) VALUES (?, ?, CURRENT_DATE)";
        String sqlCapNhat = "UPDATE qlkhuyenmai SET so_luong = so_luong - 1 WHERE ma_khuyen_mai = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            int soLuong = 0;

            try (PreparedStatement psKiemTra = conn.prepareStatement(sqlKiemTra)) {
                psKiemTra.setString(1, maKhuyenMai);
                try (ResultSet rs = psKiemTra.executeQuery()) {
                    if (rs.next()) {
                        soLuong = rs.getInt("so_luong");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            if (soLuong <= 0) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setString(1, maKhuyenMai);
                psInsert.setInt(2, maKhachHang);
                psInsert.executeUpdate();
            }

            try (PreparedStatement psCapNhat = conn.prepareStatement(sqlCapNhat)) {
                psCapNhat.setString(1, maKhuyenMai);
                psCapNhat.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<KhuyenMai> getAllKhuyenMaiKhachHang() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM qlkhuyenmai WHERE so_luong > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getString("ma_khuyen_mai"),
                        rs.getString("ten_khuyen_mai"),
                        rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"),
                        rs.getInt("phan_tram_giam"),
                        rs.getInt("so_luong"),
                        rs.getBigDecimal("tong_tien_toi_thieu"),
                        rs.getObject("so_luong_sp_toi_thieu") != null ? rs.getInt("so_luong_sp_toi_thieu") : null,
                        rs.getString("ghi_chu")
                );
                list.add(km);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
