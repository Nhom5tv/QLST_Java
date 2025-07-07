package Model;

import java.util.Date;


public class TonKho {
    private int maSanPham;
    private int maLo;
    private int soLuongTon;
    private int soLuongTrenKe;
    private int soLuongTrongKho;
    private int soLuongKhaDung;
    private int soLuongDangGiaoDich;
    private int nguongCanhBao;
    
    //
    private String tenSanPham;
    private Date hanSuDung;
    
    public TonKho(int maSanPham, int maLo, int soLuongTru) {
        this.maSanPham = maSanPham;
        this.maLo = maLo;
        this.soLuongTon = soLuongTru; // dùng biến này để lưu tạm số lượng sẽ trừ
    }

    public TonKho() {
    }
    

    // Getter & Setter
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaLo() {
        return maLo;
    }

    public void setMaLo(int maLo) {
        this.maLo = maLo;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public int getSoLuongTrenKe() {
        return soLuongTrenKe;
    }

    public void setSoLuongTrenKe(int soLuongTrenKe) {
        this.soLuongTrenKe = soLuongTrenKe;
    }

    public int getSoLuongTrongKho() {
        return soLuongTrongKho;
    }

    public void setSoLuongTrongKho(int soLuongTrongKho) {
        this.soLuongTrongKho = soLuongTrongKho;
    }

    public int getSoLuongKhaDung() {
        return soLuongKhaDung;
    }

    public void setSoLuongKhaDung(int soLuongKhaDung) {
        this.soLuongKhaDung = soLuongKhaDung;
    }

    public int getSoLuongDangGiaoDich() {
        return soLuongDangGiaoDich;
    }

    public void setSoLuongDangGiaoDich(int soLuongDangGiaoDich) {
        this.soLuongDangGiaoDich = soLuongDangGiaoDich;
    }

    public int getNguongCanhBao() {
        return nguongCanhBao;
    }

    public void setNguongCanhBao(int nguongCanhBao) {
        this.nguongCanhBao = nguongCanhBao;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

   
    
}
