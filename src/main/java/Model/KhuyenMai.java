/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int phanTramGiam;
    private int soLuong;
    private BigDecimal tongTienToiThieu;
    private Integer soLuongSpToiThieu; // có thể null
    private String ghiChu;

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, Date ngayBatDau, Date ngayKetThuc,
                     int phanTramGiam, int soLuong, BigDecimal tongTienToiThieu,
                     Integer soLuongSpToiThieu, String ghiChu) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.phanTramGiam = phanTramGiam;
        this.soLuong = soLuong;
        this.tongTienToiThieu = tongTienToiThieu;
        this.soLuongSpToiThieu = soLuongSpToiThieu;
        this.ghiChu = ghiChu;
    }

    // Getters và Setters

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(int phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getTongTienToiThieu() {
        return tongTienToiThieu;
    }

    public void setTongTienToiThieu(BigDecimal tongTienToiThieu) {
        this.tongTienToiThieu = tongTienToiThieu;
    }

    public Integer getSoLuongSpToiThieu() {
        return soLuongSpToiThieu;
    }

    public void setSoLuongSpToiThieu(Integer soLuongSpToiThieu) {
        this.soLuongSpToiThieu = soLuongSpToiThieu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
}
