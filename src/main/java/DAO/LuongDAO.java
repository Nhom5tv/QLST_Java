/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import Model.Luong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author banhb
 */
public class LuongDAO {
    public List<Luong> getDanhSachLuong(int thang, int nam, int luongMoiGioMacDinh) {
        List<Luong> ds = new ArrayList<>();

        String sql = """
            SELECT nv.ma_nv, nv.ten_nv, 
                   SUM(TIMESTAMPDIFF(HOUR, cc.check_in, cc.check_out)) AS tong_gio_lam
            FROM nhanvien nv
            JOIN cham_cong cc ON nv.ma_nv = cc.ma_nv
            WHERE MONTH(cc.ngay) = ? AND YEAR(cc.ngay) = ?
            GROUP BY nv.ma_nv, nv.ten_nv
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, thang);
            stmt.setInt(2, nam);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ma_nv = rs.getInt("ma_nv");
                    String hoten = rs.getString("ten_nv");
                    int tong_gio_lam = rs.getInt("tong_gio_lam");
                    int luong = tong_gio_lam * luongMoiGioMacDinh;
                    int thuong_phat = 0; // Mặc định
                    String ghi_chu = "";

                    Luong l = new Luong(ma_nv, tong_gio_lam, luongMoiGioMacDinh, luong, hoten, ghi_chu, thuong_phat);
                    ds.add(l);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
    
    public void saveOrUpdateLuong(Luong l, int thang, int nam) {
    String sql = """
        INSERT INTO luong(ma_nv, tong_gio_lam, luong_moi_gio, thuong_phat, luong, ghi_chu, thang, nam)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE 
            tong_gio_lam = VALUES(tong_gio_lam),
            luong_moi_gio = VALUES(luong_moi_gio),
            thuong_phat = VALUES(thuong_phat),
            luong = VALUES(luong),
            ghi_chu = VALUES(ghi_chu)
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, l.getMa_nv());
        stmt.setInt(2, l.getTong_gio_lam());
        stmt.setInt(3, l.getLuong_moi_gio());
        stmt.setInt(4, l.getThuong_phat());
        stmt.setInt(5, l.getLuong());
        stmt.setString(6, l.getGhi_chu());
        stmt.setInt(7, thang);
        stmt.setInt(8, nam);

        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}   

