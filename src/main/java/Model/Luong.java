/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author banhb
 */
public class Luong {
    private int ma_nv, tong_gio_lam, luong_moi_gio,luong, thuong_phat;
    private String hoten, ghi_chu;

    public Luong(int ma_nv, int tong_gio_lam, int luong_moi_gio, int luong,String hoten, String ghi_chu,int thuong_phat) {
        this.ma_nv = ma_nv;
        this.tong_gio_lam = tong_gio_lam;
        this.luong_moi_gio = luong_moi_gio;
        this.luong = luong;
        this.hoten = hoten;
        this.ghi_chu = ghi_chu;
        this.thuong_phat = thuong_phat;
    }

    public int getThuong_phat() {
        return thuong_phat;
    }

    public void setThuong_phat(int thuong_phat) {
        this.thuong_phat = thuong_phat;
    }

    public int getMa_nv() {
        return ma_nv;
    }

    public void setMa_nv(int ma_nv) {
        this.ma_nv = ma_nv;
    }

    public int getTong_gio_lam() {
        return tong_gio_lam;
    }

    public void setTong_gio_lam(int tong_gio_lam) {
        this.tong_gio_lam = tong_gio_lam;
    }

    public int getLuong_moi_gio() {
        return luong_moi_gio;
    }

    public void setLuong_moi_gio(int luong_moi_gio) {
        this.luong_moi_gio = luong_moi_gio;
    }

    public int getLuong() {
        return luong;
    }

    public void setLuong(int luong) {
        this.luong = luong;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }
 
    
}
