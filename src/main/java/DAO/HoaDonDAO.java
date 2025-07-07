package DAO;

import Bridge.DBConnection;
import Model.HoaDon;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class HoaDonDAO {
    private final Connection conn;

    public HoaDonDAO(Connection conn) {
        this.conn = conn;
    }

    public int save(HoaDon hoaDon) throws SQLException {
        String sql = "INSERT INTO hoa_don(ma_hoa_don, ngay_lap, ma_nv, tong_tien, hinh_thuc_thanh_toan,ghi_chu) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, hoaDon.getMaHoaDon());
            pstmt.setTimestamp(2, Timestamp.valueOf(hoaDon.getNgayLap()));
            pstmt.setInt(3, hoaDon.getMaNV());
            pstmt.setBigDecimal(4, hoaDon.getTongTien());
            pstmt.setString(5, hoaDon.getHinhThucThanhToan());
            pstmt.setString(6, hoaDon.getGhiChu());
            System.out.println("Đang lưu hóa đơn với mã nhân viên: " + hoaDon.getMaNV());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }
        }
    }

    // Lấy tất cả hóa đơn có JOIN tên nhân viên
  public List<HoaDon> getAllHoaDon() {
    List<HoaDon> list = new ArrayList<>();
    String sql = "SELECT hd.*, nv.ten_nv FROM hoa_don hd JOIN nhanvien nv ON hd.ma_nv = nv.ma_nv";

    System.out.println("[DEBUG] Chuẩn bị thực thi câu SQL: " + sql);
    
    try (
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()
    ) {
        System.out.println("[DEBUG] Đã thực thi xong câu lệnh SQL");

        int count = 0;

        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setMaHoaDon(rs.getInt("ma_hoa_don"));
            hd.setNgayLap(rs.getTimestamp("ngay_lap").toLocalDateTime());
            hd.setMaNV(rs.getInt("ma_nv"));
            hd.setTenNV(rs.getString("ten_nv")); // tên nhân viên
            hd.setTongTien(rs.getBigDecimal("tong_tien"));
            hd.setHinhThucThanhToan(rs.getString("hinh_thuc_thanh_toan"));
            hd.setGhiChu(rs.getString("ghi_chu"));
            list.add(hd);
            count++;

            System.out.println("[DEBUG] Đọc được hóa đơn: " + hd.getMaHoaDon() + ", nhân viên: " + hd.getTenNV());
        }

        System.out.println("[DEBUG] Tổng số hóa đơn lấy được: " + count);

    } catch (SQLException e) {
        System.out.println("[ERROR] Lỗi SQL khi truy vấn hóa đơn:");
        e.printStackTrace();
    }

    return list;
}

    // Thêm hóa đơn mới
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO hoa_don(ma_hoa_don, ngay_lap, ma_nv, tong_tien, hinh_thuc_thanh_toan,ghi_chu) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hd.getMaHoaDon());
            stmt.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            stmt.setInt(3, hd.getMaNV());
            stmt.setBigDecimal(4, hd.getTongTien());
            stmt.setString(5, hd.getHinhThucThanhToan());
            stmt.setString(6, hd.getGhiChu());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateHoaDon(HoaDon hd) {
        String sql = "UPDATE hoa_don SET ngay_lap = ?, ma_nv = ?, tong_tien = ?, hinh_thuc_thanh_toan = ?, ghi_chu = ? WHERE ma_hoa_don = ?";
        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(hd.getNgayLap()));
            stmt.setInt(2, hd.getMaNV());
            stmt.setBigDecimal(3, hd.getTongTien());
            stmt.setString(4, hd.getHinhThucThanhToan());
            stmt.setString(5, hd.getGhiChu());
            stmt.setInt(6, hd.getMaHoaDon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteHoaDon(int maHoaDon) {
        String sql = "DELETE FROM hoa_don WHERE ma_hoa_don = ?";
        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maHoaDon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HoaDon> searchHoaDon(String keyword) {
        List<HoaDon> list = new ArrayList<>();
        String sql = """
        SELECT hd.*, nv.ten_nv 
        FROM hoa_don hd 
        JOIN nhanvien nv ON hd.ma_nv = nv.ma_nv 
        WHERE CAST(hd.ma_hoa_don AS CHAR) LIKE ? 
           OR DATE(hd.ngay_lap) = ?
        """;
        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");

            try {
                // Nếu keyword là ngày hợp lệ thì set vào dạng yyyy-MM-dd
                java.sql.Date date = java.sql.Date.valueOf(keyword);
                stmt.setDate(2, date);
            } catch (IllegalArgumentException e) {
                stmt.setDate(2, null); // nếu không phải ngày -> null
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon(rs.getInt("ma_hoa_don"));
                    hd.setNgayLap(rs.getTimestamp("ngay_lap").toLocalDateTime());
                    hd.setMaNV(rs.getInt("ma_nv"));
                    hd.setTenNV(rs.getString("ten_nv"));
                    hd.setTongTien(rs.getBigDecimal("tong_tien"));
                    hd.setHinhThucThanhToan(rs.getString("hinh_thuc_thanh_toan"));
                    hd.setGhiChu(rs.getString("ghi_chu"));
                    list.add(hd);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
