/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author THANH AN COMPUTER
 */
import java.math.BigDecimal;
import java.sql.Blob;

public class SanPham {
    private int maSanPham;
    private String maNgoai;
    private String tenSanPham;
    private int maDanhMuc;
    private BigDecimal giaGoc;
    private BigDecimal giaBan;
    private String maVach;
    private byte[] hinhAnh;
    private String donViTinh;
    private String moTa;
    private int trangThai;

    // Chi tiết san pham
    private String thuongHieu;
    private String xuatXu;
    private String thanhPhan;
    private String huongDanSuDung;
    private String baoQuan;
    
    //join với tồn kho
    private int soLuongTon;
    private int soLuongKhaDung;
    //join danh mục
    private String tenDanhMuc;
    
    public SanPham(int maSanPham, String tenSanPham, BigDecimal giaGoc, BigDecimal giaBan) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaGoc = giaGoc;
        this.giaBan = giaBan;
    }

    public SanPham() {
        
    }
   
    

    // Getters & Setters
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaNgoai() {
        return maNgoai;
    }

    public void setMaNgoai(String maNgoai) {
        this.maNgoai = maNgoai;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public BigDecimal getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(BigDecimal giaGoc) {
        this.giaGoc = giaGoc;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public String getMaVach() {
        return maVach;
    }

    public void setMaVach(String maVach) {
        this.maVach = maVach;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public String getHuongDanSuDung() {
        return huongDanSuDung;
    }

    public void setHuongDanSuDung(String huongDanSuDung) {
        this.huongDanSuDung = huongDanSuDung;
    }

    public String getBaoQuan() {
        return baoQuan;
    }

    public void setBaoQuan(String baoQuan) {
        this.baoQuan = baoQuan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public int getSoLuongKhaDung() {
        return soLuongKhaDung;
    }

    public void setSoLuongKhaDung(int soLuongKhaDung) {
        this.soLuongKhaDung = soLuongKhaDung;
    }
    

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    @Override
    public String toString() {
         return maSanPham + " - " + tenSanPham;
    }
    
    
}
