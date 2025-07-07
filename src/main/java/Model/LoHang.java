/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;



/**
 *
 * @author DUNG LE
 */
public class LoHang {
    private int maLoHang;
    private String maPhieuNhap;
    private int maSanPham;
    private String tenSanPham;
    private int soLuong;
    private double DonGia;
    private Date ngaySanXuat;
    private Date hanSuDung;
    private String ghiChu;

    public LoHang() {
    }

    public LoHang(int maLoHang, String maPhieuNhap, int maSanPham, int soLuong, double DonGia, Date ngaySanXuat, Date hanSuDung, String ghiChu) {
        this.maLoHang = maLoHang;
        this.maPhieuNhap = maPhieuNhap;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.DonGia = DonGia;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
        this.ghiChu = ghiChu;
    }

    public LoHang(int maLoHang, String maPhieuNhap, int maSanPham, String tenSanPham, int soLuong, double DonGia, Date ngaySanXuat, Date hanSuDung, String ghiChu) {
        this.maLoHang = maLoHang;
        this.maPhieuNhap = maPhieuNhap;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.DonGia = DonGia;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
        this.ghiChu = ghiChu;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
    

    public int getMaLoHang() {
        return maLoHang;
    }

    public void setMaLoHang(int maLoHang) {
        this.maLoHang = maLoHang;
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public Date getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(Date ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
}
