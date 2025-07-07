/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDon {
    private int maHoaDon;
    private LocalDateTime ngayLap;
    private int maNV;
    private String tenNV; // Consider removing this from the model, fetch from DB when needed
    private BigDecimal tongTien;
    private String hinhThucThanhToan;
    private String ghiChu;

    public HoaDon() {
    }

    public HoaDon(int maHoaDon, LocalDateTime ngayLap, int maNV, BigDecimal tongTien, String hinhThucThanhToan, String ghiChu) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tongTien = tongTien;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.ghiChu = ghiChu;
    }

    // Consider removing this constructor
    public HoaDon(int maHoaDon, LocalDateTime ngayLap, int maNV, String tenNV, BigDecimal tongTien, String hinhThucThanhToan, String ghiChu) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.tongTien = tongTien;
        this.hinhThucThanhToan = hinhThucThanhToan;
        this.ghiChu = ghiChu;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getHinhThucThanhToan() {
        return hinhThucThanhToan;
    }

    public void setHinhThucThanhToan(String hinhThucThanhToan) {
        this.hinhThucThanhToan = hinhThucThanhToan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return maNV + "-" + tenNV;
    }
}
