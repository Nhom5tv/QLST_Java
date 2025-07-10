/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */

public class GioHang {
    private int MaGH;
    private int MaKH;
    private int MaSP;
    private int SoLuong;
    private String TenSp;
    private double giaban;
    private byte[] anh;
    private boolean isPaid;

    public GioHang() {
    }

    public GioHang(int MaGH, int MaKH, int MaSP, int SoLuong, String TenSp, double giaban, byte[] anh) {
        this.MaGH = MaGH;
        this.MaKH = MaKH;
        this.MaSP = MaSP;
        this.SoLuong = SoLuong;
        this.TenSp = TenSp;
        this.giaban = giaban;
        this.anh = anh;
    }

    public int getMaGH() {
        return MaGH;
    }

    public void setMaGH(int MaGH) {
        this.MaGH = MaGH;
    }

    public int getMaKH() {
        return MaKH;
    }

    public void setMaKH(int MaKH) {
        this.MaKH = MaKH;
    }

    public int getMaSP() {
        return MaSP;
    }

    public void setMaSP(int MaSP) {
        this.MaSP = MaSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public String getTenSp() {
        return TenSp;
    }

    public void setTenSp(String TenSp) {
        this.TenSp = TenSp;
    }

    public double getGiaban() {
        return giaban;
    }

    public void setGiaban(double giaban) {
        this.giaban = giaban;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}


