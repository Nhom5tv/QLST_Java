/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Bridge.DBConnection;
import Model.NhaCungCap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DUNG LE
 */
public class NhaCungCapDao {
    public ArrayList<NhaCungCap> getallNCC(){
        ArrayList<NhaCungCap> ds = new ArrayList<>();
        String sql = "Select * From ncc";
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                NhaCungCap ncc = new NhaCungCap(
                    rs.getString("ncc_id"),
                    rs.getString("tenncc"),
                    rs.getString("tendaidien"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("diachincc"));
                ds.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
    public boolean themnhacungcap(NhaCungCap ncc){
        String sql = "Insert into ncc (ncc_id,tenncc,tendaidien,email,phone,diachincc) Values (?,?,?,?,?,?) ";
        try(Connection conn = DBConnection.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setString(1, ncc.getNccid());
            ps.setString(2, ncc.getNccname());
            ps.setString(3, ncc.getNcclh());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getPhone());
            ps.setString(6, ncc.getDiachincc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();;
        }
        return false;
    }
    public boolean updatenhacungcap(NhaCungCap ncc){
        String sql = "Update ncc set tenncc = ?,tendaidien = ?,email = ?,phone = ?,diachincc = ? where ncc_id = ?";
         try(Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getNccname());
            ps.setString(2, ncc.getNcclh());
            ps.setString(3, ncc.getEmail());
            ps.setString(4, ncc.getPhone());
            ps.setString(5, ncc.getDiachincc());
            ps.setString(6, ncc.getNccid());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();;
        }
        return false;
    }
    public boolean xoanhacungcap(String id){
        String sql = "Delete From ncc where ncc_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<NhaCungCap> timkiemncc(String tukhoa){
        ArrayList<NhaCungCap> ds = new ArrayList<>();
        String sql = "Select * from ncc where tenncc like ? or tendaidien like ? ";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            String kw = "%" + tukhoa + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                NhaCungCap ncc = new NhaCungCap(
                    rs.getString("ncc_id"),
                    rs.getString("tenncc"),
                    rs.getString("tendaidien"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("diachincc")
                );
                ds.add(ncc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
}
   public List<NhaCungCap> getAll() {
    List<NhaCungCap> list = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM ncc");
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new NhaCungCap(
                rs.getString("ncc_id"),
                rs.getString("tenncc"),
                rs.getString("tendaidien"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("diachincc")
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
