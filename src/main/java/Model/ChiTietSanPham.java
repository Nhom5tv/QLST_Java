/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */
public class ChiTietSanPham {
    private int MaGH;
    private int MaKH;
    private int MaSP;
    private int SoLuong;

    public ChiTietSanPham() {
    }

    public ChiTietSanPham(int MaGH, int MaKH, int MaSP, int SoLuong) {
        this.MaGH = MaGH;
        this.MaKH = MaKH;
        this.MaSP = MaSP;
        this.SoLuong = SoLuong;
    }

    public ChiTietSanPham(int MaKH, int MaSP, int SoLuong) {
        this.MaKH = MaKH;
        this.MaSP = MaSP;
        this.SoLuong = SoLuong;
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
    
}
