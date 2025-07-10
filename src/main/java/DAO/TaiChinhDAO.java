package DAO;

import Bridge.DBConnection;
import Model.NhanVien;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.TaiChinh;

public class TaiChinhDAO {

    public ArrayList<TaiChinh> getAllGiaoDich() {
        ArrayList<TaiChinh> list = new ArrayList<>();
        String sql = "SELECT gd.ma_giao_dich, gd.ngay, gd.loai_giao_dich, gd.so_tien, gd.mo_ta, " +
                     "nv.ma_nv, nv.ten_nv, nv.cccd, nv.sdt, nv.chucvu, nv.email, " +
                     "nv.gioitinh, nv.ghichu, nv.anh " +
                     "FROM qltaichinh gd JOIN nhanvien nv ON gd.ma_nv = nv.ma_nv";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getInt("ma_nv"),
                    rs.getString("ten_nv"),
                    rs.getString("cccd"),
                    rs.getString("sdt"),
                    rs.getString("chucvu"),
                    rs.getString("email"),
                    rs.getString("gioitinh"),
                    rs.getString("ghichu"),
                    rs.getBytes("anh")
                );

                TaiChinh tc = new TaiChinh(
                    rs.getInt("ma_giao_dich"),
                    rs.getDate("ngay"),
                    rs.getString("loai_giao_dich"),
                    rs.getBigDecimal("so_tien"),
                    rs.getString("mo_ta"),
                    nv
                );
                list.add(tc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertGiaoDich(TaiChinh tc) {
        String sql = "INSERT INTO qltaichinh (ngay, loai_giao_dich, so_tien, mo_ta, ma_nv) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new Date(tc.getNgay().getTime()));
            ps.setString(2, tc.getLoaiGiaoDich());
            ps.setBigDecimal(3, tc.getSoTien());
            ps.setString(4, tc.getMoTa());
            ps.setInt(5, tc.getNhanVien().getma_nv());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateGiaoDich(TaiChinh tc) {
        String sql = "UPDATE qltaichinh SET ngay = ?, loai_giao_dich = ?, so_tien = ?, mo_ta = ?, ma_nv = ? WHERE ma_giao_dich = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new Date(tc.getNgay().getTime()));
            ps.setString(2, tc.getLoaiGiaoDich());
            ps.setBigDecimal(3, tc.getSoTien());
            ps.setString(4, tc.getMoTa());
            ps.setInt(5, tc.getNhanVien().getma_nv());
            ps.setInt(6, tc.getMaGiaoDich());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteGiaoDich(int maGiaoDich) {
        String sql = "DELETE FROM qltaichinh WHERE ma_giao_dich = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maGiaoDich);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public TaiChinh getById(int maGiaoDich) {
        String sql = "SELECT gd.ma_giao_dich, gd.ngay, gd.loai_giao_dich, gd.so_tien, gd.mo_ta, " +
                     "nv.ma_nv, nv.ten_nv, nv.cccd, nv.sdt, nv.chucvu, nv.email, " +
                     "nv.gioitinh, nv.ghichu, nv.anh " +
                     "FROM qltaichinh gd JOIN nhanvien nv ON gd.ma_nv = nv.ma_nv WHERE gd.ma_giao_dich = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maGiaoDich);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getInt("ma_nv"),
                        rs.getString("ten_nv"),
                        rs.getString("cccd"),
                        rs.getString("sdt"),
                        rs.getString("chucvu"),
                        rs.getString("email"),
                        rs.getString("gioitinh"),
                        rs.getString("ghichu"),
                        rs.getBytes("anh")
                    );

                    return new TaiChinh(
                        rs.getInt("ma_giao_dich"),
                        rs.getDate("ngay"),
                        rs.getString("loai_giao_dich"),
                        rs.getBigDecimal("so_tien"),
                        rs.getString("mo_ta"),
                        nv
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<TaiChinh> searchGiaoDich(String keyword) {
        ArrayList<TaiChinh> list = new ArrayList<>();
        String sql = "SELECT gd.ma_giao_dich, gd.ngay, gd.loai_giao_dich, gd.so_tien, gd.mo_ta, " +
                     "nv.ma_nv, nv.ten_nv, nv.cccd, nv.sdt, nv.chucvu, nv.email, " +
                     "nv.gioitinh, nv.ghichu, nv.anh " +
                     "FROM qltaichinh gd JOIN nhanvien nv ON gd.ma_nv = nv.ma_nv " +
                     "WHERE nv.ten_nv LIKE ? OR gd.mo_ta LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword + "%";
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getInt("ma_nv"),
                        rs.getString("ten_nv"),
                        rs.getString("cccd"),
                        rs.getString("sdt"),
                        rs.getString("chucvu"),
                        rs.getString("email"),
                        rs.getString("gioitinh"),
                        rs.getString("ghichu"),
                        rs.getBytes("anh")
                    );

                    TaiChinh tc = new TaiChinh(
                        rs.getInt("ma_giao_dich"),
                        rs.getDate("ngay"),
                        rs.getString("loai_giao_dich"),
                        rs.getBigDecimal("so_tien"),
                        rs.getString("mo_ta"),
                        nv
                    );
                    list.add(tc);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
public boolean insertChiTuTatCaPhieuNhap() {
    try (Connection conn = DBConnection.getConnection()) {
        int maNvQuanLyKho = -1;
        String sqlGetMaNv = "SELECT ma_nv FROM nhanvien WHERE chucvu IN ('Kho', 'Admin') LIMIT 1";
        try (PreparedStatement stmtMaNv = conn.prepareStatement(sqlGetMaNv);
             ResultSet rsMaNv = stmtMaNv.executeQuery()) {
            if (rsMaNv.next()) {
                maNvQuanLyKho = rsMaNv.getInt("ma_nv");
            } else {
                System.out.println("❌ Không tìm thấy nhân viên với chức vụ 'kho'");
                return false;
            }
        }

        String sqlSelect = """
            SELECT 
                DATE(pn.ngay_nhap) AS ngay,
                SUM(lh.so_luong * lh.don_gia) AS tong_chi_ngay
            FROM 
                lo_hang lh
            JOIN 
                phieu_nhap pn ON lh.ma_phieu_nhap = pn.ma_phieu_nhap
            GROUP BY DATE(pn.ngay_nhap)
        """;

        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
             ResultSet rs = stmtSelect.executeQuery()) {

            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                float tongChi = rs.getFloat("tong_chi_ngay");
                String moTa = "Tổng chi phí nhập hàng ngày " + ngay;

                // UPDATE có thêm điều kiện mo_ta
                String sqlUpdate = """
                    UPDATE qltaichinh
                    SET so_tien = so_tien + ?
                    WHERE loai_giao_dich = 'Chi' AND ngay = ? AND mo_ta = ?
                """;
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setFloat(1, tongChi);
                    stmtUpdate.setDate(2, ngay);
                    stmtUpdate.setString(3, moTa);
                    int rowsUpdated = stmtUpdate.executeUpdate();

                    if (rowsUpdated == 0) {
                        String sqlInsert = """
                            INSERT INTO qltaichinh (loai_giao_dich, so_tien, ngay, mo_ta, ma_nv)
                            VALUES ('Chi', ?, ?, ?, ?)
                        """;
                        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                            stmtInsert.setFloat(1, tongChi);
                            stmtInsert.setDate(2, ngay);
                            stmtInsert.setString(3, moTa);
                            stmtInsert.setInt(4, maNvQuanLyKho);
                            stmtInsert.executeUpdate();
                        }
                    }
                }
            }
        }

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public boolean insertThuTuTatCaHoaDonVaDonHang() {
    try (Connection conn = DBConnection.getConnection()) {

        // === Lấy mã nhân viên 'admin' cho đơn hàng online ===
        int maNvAdmin = -1;
        String sqlAdmin = "SELECT ma_nv FROM nhanvien WHERE chucvu = 'admin' LIMIT 1";
        try (PreparedStatement stmtAdmin = conn.prepareStatement(sqlAdmin);
             ResultSet rsAdmin = stmtAdmin.executeQuery()) {
            if (rsAdmin.next()) {
                maNvAdmin = rsAdmin.getInt("ma_nv");
            } else {
                System.out.println("❌ Không tìm thấy nhân viên admin");
                return false;
            }
        }

        // === THU TỪ HÓA ĐƠN OFFLINE (chia theo ngày + từng nhân viên) ===
        String sqlHD = """
            SELECT DATE(ngay_lap) AS ngay, ma_nv, SUM(tong_tien) AS tong_thu
            FROM hoa_don
            GROUP BY DATE(ngay_lap), ma_nv
        """;
        try (PreparedStatement stmtHD = conn.prepareStatement(sqlHD);
             ResultSet rsHD = stmtHD.executeQuery()) {

            while (rsHD.next()) {
                Date ngay = rsHD.getDate("ngay");
                int maNv = rsHD.getInt("ma_nv");
                float tongThu = rsHD.getFloat("tong_thu");
                String moTa = "Tổng thu từ hóa đơn ngày " + ngay + " - NV: " + maNv;

                // Nếu đã tồn tại, cập nhật lại
                String sqlUpdate = """
                    UPDATE qltaichinh
                    SET so_tien = ?
                    WHERE loai_giao_dich = 'Thu' AND ngay = ? AND mo_ta = ?
                """;
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setFloat(1, tongThu);
                    stmtUpdate.setDate(2, ngay);
                    stmtUpdate.setString(3, moTa);
                    int rowsUpdated = stmtUpdate.executeUpdate();

                    // Nếu chưa có thì chèn mới
                    if (rowsUpdated == 0) {
                        String sqlInsert = """
                            INSERT INTO qltaichinh (loai_giao_dich, so_tien, ngay, mo_ta, ma_nv)
                            VALUES ('Thu', ?, ?, ?, ?)
                        """;
                        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                            stmtInsert.setFloat(1, tongThu);
                            stmtInsert.setDate(2, ngay);
                            stmtInsert.setString(3, moTa);
                            stmtInsert.setInt(4, maNv);
                            stmtInsert.executeUpdate();
                        }
                    }
                }
            }
        }

        // === THU TỪ ĐƠN HÀNG ONLINE (chia theo ngày, mặc định NV là admin) ===
        String sqlOrder = """
            SELECT DATE(ngay_tao) AS ngay, SUM(tong_tien) AS tong_thu
            FROM hdorder
            GROUP BY DATE(ngay_tao)
        """;
        try (PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder);
             ResultSet rsOrder = stmtOrder.executeQuery()) {

            while (rsOrder.next()) {
                Date ngay = rsOrder.getDate("ngay");
                float tongThu = rsOrder.getFloat("tong_thu");
                String moTa = "Tổng thu từ đơn hàng online ngày " + ngay;

                // Nếu đã tồn tại, cập nhật lại
                String sqlUpdate = """
                    UPDATE qltaichinh
                    SET so_tien = ?, ma_nv = ?
                    WHERE loai_giao_dich = 'Thu' AND ngay = ? AND mo_ta = ?
                """;
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setFloat(1, tongThu);
                    stmtUpdate.setInt(2, maNvAdmin);
                    stmtUpdate.setDate(3, ngay);
                    stmtUpdate.setString(4, moTa);
                    int rowsUpdated = stmtUpdate.executeUpdate();

                    // Nếu chưa có thì chèn mới
                    if (rowsUpdated == 0) {
                        String sqlInsert = """
                            INSERT INTO qltaichinh (loai_giao_dich, so_tien, ngay, mo_ta, ma_nv)
                            VALUES ('Thu', ?, ?, ?, ?)
                        """;
                        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                            stmtInsert.setFloat(1, tongThu);
                            stmtInsert.setDate(2, ngay);
                            stmtInsert.setString(3, moTa);
                            stmtInsert.setInt(4, maNvAdmin);
                            stmtInsert.executeUpdate();
                        }
                    }
                }
            }
        }

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
