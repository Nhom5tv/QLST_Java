/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import Model.ChamCong;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author banhb
 */
public class ChamCongDAO {
    
    public Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public List<ChamCong> getDanhSachChamCong() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT c.*, nv.ten_nv FROM cham_cong c JOIN nhanvien nv ON c.ma_nv = nv.ma_nv ORDER BY c.ngay DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ChamCong cc = new ChamCong();
                cc.setMa_cham_cong(rs.getInt("ma_cham_cong"));
                cc.setMa_nv(rs.getInt("ma_nv"));
                cc.setNgay(rs.getDate("ngay").toLocalDate());

                Time in = rs.getTime("check_in");
                Time out = rs.getTime("check_out");
                cc.setCheck_in(in != null ? in.toLocalTime() : null);
                cc.setCheck_out(out != null ? out.toLocalTime() : null);

                String hoten = rs.getString("ten_nv");
                String ghichu = rs.getString("ghi_chu");
                cc.setGhi_chu((hoten != null ? hoten : "") + " - " + (ghichu != null ? ghichu : ""));
 
                System.out.println("→ Java đang kết nối tới DB: " + conn.getCatalog());
                System.out.println("→ Lấy được bản ghi: " + cc.getMa_cham_cong());
                list.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkIn(ChamCong cc) {
        String sql = "INSERT INTO cham_cong (ma_nv, ngay, check_in, check_out, ghi_chu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cc.getMa_nv());
            stmt.setDate(2, Date.valueOf(cc.getNgay()));
            stmt.setTime(3, cc.getCheck_in() != null ? Time.valueOf(cc.getCheck_in()) : null);
            stmt.setTime(4, cc.getCheck_out() != null ? Time.valueOf(cc.getCheck_out()) : null);
            stmt.setString(5, cc.getGhi_chu());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi check-in: " + e.getMessage());
            return false;
        }
    }

    public boolean checkOut(int ma_nv, LocalDate ngay, LocalTime check_out) {
        String sql = "UPDATE cham_cong SET check_out = ? WHERE ma_nv = ? AND ngay = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTime(1, Time.valueOf(check_out));
            stmt.setInt(2, ma_nv);
            stmt.setDate(3, Date.valueOf(ngay));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi check-out: " + e.getMessage());
            return false;
        }
    }

    public boolean CheckedIn(int ma_nv, LocalDate ngay) {
        String sql = "SELECT * FROM cham_cong WHERE ma_nv = ? AND ngay = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ma_nv);
            stmt.setDate(2, Date.valueOf(ngay));
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra check-in: " + e.getMessage());
            return false;
        }
    }

    public boolean updateChamCong(ChamCong cc) {
        String sql = "UPDATE cham_cong SET check_in = ?, check_out = ?, ghi_chu = ? WHERE ma_cham_cong = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTime(1, cc.getCheck_in() != null ? Time.valueOf(cc.getCheck_in()) : null);
            stmt.setTime(2, cc.getCheck_out() != null ? Time.valueOf(cc.getCheck_out()) : null);
            stmt.setString(3, cc.getGhi_chu());
            stmt.setInt(4, cc.getMa_cham_cong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật chấm công: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteChamCong(int maChamCong) {
        String sql = "DELETE FROM cham_cong WHERE ma_cham_cong = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maChamCong);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi xóa chấm công: " + e.getMessage());
            return false;
        }
    } 
    
    public List<ChamCong> getChamCongTheoThangNam(int thang, int nam) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT c.*, nv.ten_nv FROM cham_cong c " +
                     "JOIN nhanvien nv ON c.ma_nv = nv.ma_nv " +
                     "WHERE MONTH(c.ngay) = ? AND YEAR(c.ngay) = ? ORDER BY c.ngay DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChamCong cc = new ChamCong();
                cc.setMa_cham_cong(rs.getInt("ma_cham_cong"));
                cc.setMa_nv(rs.getInt("ma_nv"));
                cc.setNgay(rs.getDate("ngay").toLocalDate());
                cc.setCheck_in(rs.getTime("check_in") != null ? rs.getTime("check_in").toLocalTime() : null);
                cc.setCheck_out(rs.getTime("check_out") != null ? rs.getTime("check_out").toLocalTime() : null);
                cc.setGhi_chu(rs.getString("ten_nv") + " - " + rs.getString("ghi_chu"));
                list.add(cc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<ChamCong> getChamCongTheoTen(String keyword) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT c.*, nv.ten_nv FROM cham_cong c " +
                     "JOIN nhanvien nv ON c.ma_nv = nv.ma_nv " +
                     "WHERE nv.hoten LIKE ? ORDER BY c.ngay DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChamCong cc = new ChamCong();
                cc.setMa_cham_cong(rs.getInt("ma_cham_cong"));
                cc.setMa_nv(rs.getInt("ma_nv"));
                cc.setNgay(rs.getDate("ngay").toLocalDate());
                cc.setCheck_in(rs.getTime("check_in") != null ? rs.getTime("check_in").toLocalTime() : null);
                cc.setCheck_out(rs.getTime("check_out") != null ? rs.getTime("check_out").toLocalTime() : null);
                cc.setGhi_chu(rs.getString("ten_nv") + " - " + rs.getString("ghi_chu"));
                list.add(cc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public String getChucVuNhanVien(int ma_nv) {
        String chucVu = "";
        String sql = "SELECT chucvu FROM nhanvien WHERE ma_nv = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ma_nv);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                chucVu = rs.getString("chucvu");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chucVu;
    }
    
    public int tinhTongGio(int ma_nv, int thang, int nam) {
        String sql = """
            SELECT SUM(TIMESTAMPDIFF(HOUR, check_in, check_out)) AS tong_gio
            FROM cham_cong
            WHERE ma_nv = ? AND MONTH(ngay) = ? AND YEAR(ngay) = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ma_nv);
            stmt.setInt(2, thang);
            stmt.setInt(3, nam);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tong_gio");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
