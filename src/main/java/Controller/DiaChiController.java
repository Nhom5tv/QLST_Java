/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Admin
 */
import DAO.DiaChiDAO;
import Model.DiaChi;
import View.DiaChiView;
import View.DiachiDialog;
import View.ThongBaodialog;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class DiaChiController {
    private DiaChiView diaChiView;  
    private DiaChiDAO addressDAO;  
    
    // Constructor
    public DiaChiController(DiaChiView view) {
        this.diaChiView = view;
        this.addressDAO = new DiaChiDAO();
        
        // Thêm sự kiện cho các nút
        diaChiView.getAddBtn().addActionListener(e -> themDiaChi());
        //diaChiView.getConfirmBtn().addActionListener(e -> xacNhanDiaChi());
        view.setConfirmBtnListener(e -> {
            // Kiểm tra các giá trị đã chọn
            if (view.getSelectedName() != null && view.getSelectedPhone() != null && view.getSelectedAddress() != null) {
                // Truyền thông tin địa chỉ đã chọn vào hành động
                view.onAddressSelected.actionPerformed(new ActionEvent(view, ActionEvent.ACTION_PERFORMED, 
                        view.getSelectedName() + ";" + view.getSelectedPhone() + ";" + view.getSelectedAddress()));
                // Đóng dialog sau khi xác nhận
                view.dispose();
            } else {
                view.showMessage("Vui lòng chọn một địa chỉ.");
            }
        });
        loadAddresses();
    }
    private void loadAddresses() {
        int customerId = diaChiView.getCustomerId();
        System.out.println("Customer ID: " + customerId);
        ArrayList<DiaChi> addresses = DiaChiDAO.getAddressesByCustomerId(customerId);       
        diaChiView.clearAddresses();
        for (DiaChi dc : addresses) {
            diaChiView.addAddressItem(
                dc,
                e -> {
                    diaChiView.showMessage("Đã chọn địa chỉ: " + dc.getName());
                },
                e -> capNhatDiaChi(dc),  
                e -> xoaDiaChi(dc)       
            );
        }
    }
    
    private void themDiaChi() {
        DiachiDialog dialog = new DiachiDialog((JFrame)diaChiView.getParent(), "Thêm Địa Chỉ Mới");
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            DiaChi newAddress = new DiaChi(
                0, 
                dialog.getName(),
                dialog.getPhone(),
                dialog.getAddress(),
                diaChiView.getCustomerId()
            );
            
            if (addressDAO.addAddress(newAddress)) {
                ThongBaodialog.showsuccessDialog(diaChiView, "Thêm địa chỉ thành công", "Thông báo");
                loadAddresses(); // Tải lại danh sách địa chỉ
            } else {
                ThongBaodialog.showErrorDialog(diaChiView, "Thêm địa chỉ thất bại", "Lỗi");
            }
        }
    }
    private void capNhatDiaChi(DiaChi dc) {
        DiachiDialog dialog = new DiachiDialog((JFrame)diaChiView.getParent(), "Cập nhật Địa Chỉ");
        dialog.setFields(dc.getName(), dc.getPhoneNumber(), dc.getDetailAddress());
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            dc.setName(dialog.getName());
            dc.setPhoneNumber(dialog.getPhone());
            dc.setDetailAddress(dialog.getAddress());
            
            if (addressDAO.updateAddress(dc)) {
                ThongBaodialog.showsuccessDialog(diaChiView, "Cập nhật địa chỉ thành công", "Thông báo");
                loadAddresses(); // Tải lại danh sách địa chỉ
            } else {
                ThongBaodialog.showErrorDialog(diaChiView, "Cập nhật địa chỉ thất bại", "Lỗi");
            }
        }
    }
    
    private void xoaDiaChi(DiaChi dc) {
        boolean confirm = ThongBaodialog.showConfirmDialog(
            diaChiView, 
            "Xác nhận xóa", 
            "Bạn có chắc chắn muốn xóa địa chỉ này?"
        );
        
        if (confirm) {
            if (addressDAO.deleteAddress(dc.getId())) {
                ThongBaodialog.showsuccessDialog(diaChiView, "Xóa địa chỉ thành công", "Thông báo");
                loadAddresses(); // Tải lại danh sách địa chỉ
            } else {
                ThongBaodialog.showErrorDialog(diaChiView, "Xóa địa chỉ thất bại", "Lỗi");
            }
        }
    }
    
//    private void xacNhanDiaChi() {
//        boolean isAddressSelected = false;
//        String selectedName = diaChiView.getSelectedName();
//        String selectedPhone = diaChiView.getSelectedPhone();
//        String selectedAddress = diaChiView.getSelectedAddress();
//
//        if (selectedName != null && selectedPhone != null && selectedAddress != null) {
//            String selectedAddressInfo = "Name: " + selectedName + ", Phone: " + selectedPhone + ", Address: " + selectedAddress;
//            JOptionPane.showMessageDialog(diaChiView, "Địa chỉ đã chọn:\n" + selectedAddressInfo);
//            // Gọi phương thức onAddressSelected từ view
//            if (diaChiView.onAddressSelected != null) {
//                diaChiView.onAddressSelected.actionPerformed(new java.awt.event.ActionEvent(diaChiView, 0, selectedAddressInfo));
//            }
//            diaChiView.dispose();
//        } else {
//            ThongBaodialog.showErrorDialog(diaChiView, "Bạn chưa chọn địa chỉ nào.", "Cảnh báo");
//        }
//    }
}
