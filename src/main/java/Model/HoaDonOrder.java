/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HoaDonOrder {
    private int maHoaDon;
    private int maKhachHang;
    private Timestamp ngayTao;
    private BigDecimal tongTien;
    private String makhuyenmai;
    private String trangThai;
    private DiaChi diaChi;
    private List<ChiTietHoaDonOrder> chiTietHoaDons;
    
    // lưu địa chỉ là text cứng
    private String tenNguoiNhan;
    private String soDienThoai;
    private String diaChiChiTiet;
    
    public HoaDonOrder() {
        this.chiTietHoaDons = new ArrayList<>();
    }
    
    public HoaDonOrder(List<ChiTietHoaDonOrder> chiTietHoaDons) {
        this.chiTietHoaDons = chiTietHoaDons != null ? chiTietHoaDons : new ArrayList<>();
    }

    public HoaDonOrder(int maHoaDon, int maKhachHang, Timestamp ngayTao, BigDecimal tongTien, String makhuyenmai, String trangThai, DiaChi diaChi, List<ChiTietHoaDonOrder> chiTietHoaDons) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.makhuyenmai = makhuyenmai;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
        this.chiTietHoaDons = chiTietHoaDons != null ? chiTietHoaDons : new ArrayList<>();
    }

    public HoaDonOrder(int maKhachHang, Timestamp ngayTao, BigDecimal tongTien, String makhuyenmai, String trangThai, DiaChi diaChi, List<ChiTietHoaDonOrder> chiTietHoaDons) {
        this.maKhachHang = maKhachHang;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.makhuyenmai = makhuyenmai;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
        this.chiTietHoaDons = chiTietHoaDons != null ? chiTietHoaDons : new ArrayList<>();
    }

    public String getMakhuyenmai() {
        return makhuyenmai;
    }

    public void setMakhuyenmai(String makhuyenmai) {
        this.makhuyenmai = makhuyenmai;
    }

    public List<ChiTietHoaDonOrder> getChiTietHoaDons() {
        if (this.chiTietHoaDons == null) {
            this.chiTietHoaDons = new ArrayList<>();
        }
        return this.chiTietHoaDons;
    }

    public void setChiTietHoaDons(List<ChiTietHoaDonOrder> chiTietHoaDons) {
        this.chiTietHoaDons = chiTietHoaDons != null ? chiTietHoaDons : new ArrayList<>();
    }

    public DiaChi getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DiaChi diaChi) {
        this.diaChi = diaChi;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Timestamp getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Timestamp ngayTao) {
        this.ngayTao = ngayTao;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChiChiTiet() {
        return diaChiChiTiet;
    }

    public void setDiaChiChiTiet(String diaChiChiTiet) {
        this.diaChiChiTiet = diaChiChiTiet;
    }
    
}
