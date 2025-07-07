/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class NhanVien {
    private int ma_nv;
    private String hoten, cccd, sdt, chucvu, email, gioitinh, ghichu;
    private byte[] anh;

    public NhanVien() {
    }

    public NhanVien(String hoten, String cccd, String sdt, String chucvu, String email, String gioitinh, String ghichu, byte[] anh) {
        this.hoten = hoten;
        this.cccd = cccd;
        this.sdt = sdt;
        this.chucvu = chucvu;
        this.email = email;
        this.gioitinh = gioitinh;
        this.ghichu = ghichu;
        this.anh = anh;
    }
    public NhanVien(int ma_nv,String hoten, String cccd, String sdt, String chucvu, String email, String gioitinh, String ghichu, byte[] anh) {
        this.ma_nv = ma_nv;
        this.hoten = hoten;
        this.cccd = cccd;
        this.sdt = sdt;
        this.chucvu = chucvu;
        this.email = email;
        this.gioitinh = gioitinh;
        this.ghichu = ghichu;
        this.anh = anh;
    }
    public int getma_nv() {
        return ma_nv;
    }

    public void setma_nv(int ma_nv) {
        this.ma_nv = ma_nv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    @Override
    public String toString() {
        return hoten;
    }
}
