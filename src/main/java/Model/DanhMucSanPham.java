/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class DanhMucSanPham {
    private int maDanhMuc;
    private String tenDanhMuc;
    private String maKyHieu;
    private Integer maCha; // Có thể null
    private String moTa;
    private int trangThai; // 1 = đang dùng, 0 = ngưng dùng

    // Constructors
    public DanhMucSanPham() {}

    public DanhMucSanPham(int maDanhMuc, String tenDanhMuc, String maKyHieu, Integer maCha, String moTa, int trangThai) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.maKyHieu = maKyHieu;
        this.maCha = maCha;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getMaKyHieu() {
        return maKyHieu;
    }

    public void setMaKyHieu(String maKyHieu) {
        this.maKyHieu = maKyHieu;
    }

    public Integer getMaCha() {
        return maCha;
    }

    public void setMaCha(Integer maCha) {
        this.maCha = maCha;
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

    @Override
    public String toString() {
        return tenDanhMuc + " (" + maKyHieu + ")";
    }
}
