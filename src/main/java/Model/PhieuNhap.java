/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

public class PhieuNhap {
    private String maPhieu;
    private Date ngayNhap;
    private String maNCC;
    private String tenNCC;
    private String ghiChu;

    public PhieuNhap() {
    }

    public PhieuNhap(String maPhieu, String maNCC, String tenNCC, Date ngayNhap, String ghiChu) {
    this.maPhieu = maPhieu;
    this.maNCC = maNCC;
    this.tenNCC = tenNCC;
    this.ngayNhap = ngayNhap;
    this.ghiChu = ghiChu;
}

    public PhieuNhap(String maPhieu,String maNCC,Date ngayNhap, String ghiChu) {
        this.maPhieu = maPhieu;
        this.ngayNhap = ngayNhap;
        this.maNCC = maNCC;
        this.ghiChu = ghiChu;
    }
    
    
    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public String getTenNcc() {
    return tenNCC;
}

    public void setTenNcc(String tenNcc) {
        this.tenNCC = tenNcc;
    }

    @Override
    public String toString() {
        return maPhieu + "-" + tenNCC ;
    }
    
}
