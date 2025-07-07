/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author banhb
 */
public class ChamCong {
    private int ma_cham_cong,ma_nv;
    private LocalDate ngay;
    private LocalTime check_in,check_out;
    private String ghi_chu;

    public ChamCong() {
    }

    public ChamCong(int ma_cham_cong, int ma_nv, LocalDate ngay, LocalTime check_in, LocalTime check_out, String ghi_chu) {
        this.ma_cham_cong = ma_cham_cong;
        this.ma_nv = ma_nv;
        this.ngay = ngay;
        this.check_in = check_in;
        this.check_out = check_out;
        this.ghi_chu = ghi_chu;
    }

    public int getMa_cham_cong() {
        return ma_cham_cong;
    }

    public void setMa_cham_cong(int ma_cham_cong) {
        this.ma_cham_cong = ma_cham_cong;
    }

    public int getMa_nv() {
        return ma_nv;
    }

    public void setMa_nv(int ma_nv) {
        this.ma_nv = ma_nv;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }

    public LocalTime getCheck_in() {
        return check_in;
    }

    public void setCheck_in(LocalTime check_in) {
        this.check_in = check_in;
    }

    public LocalTime getCheck_out() {
        return check_out;
    }

    public void setCheck_out(LocalTime check_out) {
        this.check_out = check_out;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }
    
}
