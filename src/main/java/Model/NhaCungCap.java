/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author DUNG LE
 */
public class NhaCungCap {
    private String nccid;
    private String nccname;
    private String ncclh;
    private String email;
    private String phone;
    private String diachincc;

    public NhaCungCap(String nccid, String nccname, String ncclh, String email, String phone, String diachincc) {
        this.nccid = nccid;
        this.nccname = nccname;
        this.ncclh = ncclh;
        this.email = email;
        this.phone = phone;
        this.diachincc = diachincc;
    }

    public String getNccid() {
        return nccid;
    }

    public void setNccid(String nccid) {
        this.nccid = nccid;
    }

    public String getNccname() {
        return nccname;
    }

    public void setNccname(String nccname) {
        this.nccname = nccname;
    }

    public String getNcclh() {
        return ncclh;
    }

    public void setNcclh(String ncclh) {
        this.ncclh = ncclh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDiachincc() {
        return diachincc;
    }

    public void setDiachincc(String diachincc) {
        this.diachincc = diachincc;
    }
    @Override
    public String toString() {
        return nccid + " - " + nccname;
    }
    
}
