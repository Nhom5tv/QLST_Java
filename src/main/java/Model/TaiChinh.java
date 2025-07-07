package model;

import java.math.BigDecimal;
import java.util.Date;

public class TaiChinh {

    private int maGiaoDich;
    private Date ngay;
    private String loaiGiaoDich; // "Thu" hoặc "Chi"
    private BigDecimal soTien;
    private String moTa;
    private NhanVien nhanVien;

    public TaiChinh() {}

    public TaiChinh(int maGiaoDich, Date ngay, String loaiGiaoDich, BigDecimal soTien, String moTa, NhanVien nhanVien) {
        this.maGiaoDich = maGiaoDich;
        this.ngay = ngay;
        this.loaiGiaoDich = loaiGiaoDich;
        this.soTien = soTien;
        this.moTa = moTa;
        this.nhanVien = nhanVien;
    }

    public int getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(int maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public String getLoaiGiaoDich() {
        return loaiGiaoDich;
    }

    public void setLoaiGiaoDich(String loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    // ✅ Sửa lại phương thức này để tương thích với getHoten()
    public String getTenNhanVien() {
        return (nhanVien != null) ? nhanVien.getHoten() : "Không xác định";
    }

    @Override
    public String toString() {
        return "TaiChinh{" +
                "maGiaoDich=" + maGiaoDich +
                ", ngay=" + ngay +
                ", loaiGiaoDich='" + loaiGiaoDich + '\'' +
                ", soTien=" + soTien +
                ", moTa='" + moTa + '\'' +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getma_nv() : "null") +
                '}';
    }
}
