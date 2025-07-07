/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ChamCongDAO;
import DAO.LuongDAO;
import DAO.TaiKhoanDAO;
import Model.TaiKhoan;
import Model.ChamCong;
import Model.Luong;
import View.ChamCongView;
import View.TrangChuView;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author banhb
 */
public class ChamCongController {
    private ChamCongView ccview;
    private ChamCongDAO chamCongDAO;
    private TrangChuView tcv;
    private int ma_nv;
    
    public ChamCongController(TrangChuView view,ChamCongView panel, int ma_nv) {
       this.tcv = view;
       this.ccview = panel;
       this.chamCongDAO = new ChamCongDAO();
       this.ma_nv = ma_nv;
       phanQuyenTheoChucVu();
       loadTable();
       addEvents();
    }

    private void loadTable() {
        List<ChamCong> danhSach = chamCongDAO.getDanhSachChamCong();
        DefaultTableModel model = ccview.getTableModel();
        model.setRowCount(0);

        for (ChamCong cc : danhSach) {
            model.addRow(new Object[]{
                    cc.getMa_cham_cong(),
                    cc.getMa_nv(),
                    cc.getNgay(),
                    cc.getCheck_in(),
                    cc.getCheck_out(),
                    cc.getGhi_chu()
            });
        }
    }

    private void addEvents() {
        ccview.getBtnCheckIn().addActionListener(this::xuLyCheckIn);
        ccview.getBtnCheckOut().addActionListener(this::xuLyCheckOut);
        ccview.getBtnSua().addActionListener(e -> suaChamCong());
        ccview.getBtnXoa().addActionListener(e -> xoaChamCong());
        
         // ===== Tìm kiếm theo tên =====
        ccview.getTxtSearch().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = ccview.getTxtSearch().getText().trim();
                List<ChamCong> list = chamCongDAO.getChamCongTheoTen(keyword);
                showTable(list);
            }
        });
        
        // ===== Lọc theo tháng/năm =====
        ccview.getCbThang().addActionListener(e -> locTheoThangNam());
        ccview.getCbNam().addActionListener(e -> locTheoThangNam());
        
        // ===== Xuất Excel =====
        ccview.getBtnExport().addActionListener(e -> ExportExcel.exportTable(ccview.getTable()));

        ccview.getTable().getSelectionModel().addListSelectionListener(e -> {
            int row = ccview.getTable().getSelectedRow();
            if (row >= 0) {
                ccview.getTxtMaChamCong().setText(ccview.getTable().getValueAt(row, 0).toString());
                ccview.getTxtMaNv().setText(ccview.getTable().getValueAt(row, 1).toString());
                ccview.getTxtNgay().setText(ccview.getTable().getValueAt(row, 2).toString());
                ccview.getTxtCheckIn().setText(ccview.getTable().getValueAt(row, 3) != null ? ccview.getTable().getValueAt(row, 3).toString() : "");
                ccview.getTxtCheckOut().setText(ccview.getTable().getValueAt(row, 4) != null ? ccview.getTable().getValueAt(row, 4).toString() : "");
                ccview.getTxtGhiChu().setText(ccview.getTable().getValueAt(row, 5).toString());
            }
        });
        
        ccview.getTable().getSelectionModel().addListSelectionListener(e -> {
            int row = ccview.getTable().getSelectedRow();
            if (row >= 0) {
                ccview.getTxtMaChamCong().setText(ccview.getTable().getValueAt(row, 0).toString());
                ccview.getTxtMaNv().setText(ccview.getTable().getValueAt(row, 1).toString());
                ccview.getTxtNgay().setText(ccview.getTable().getValueAt(row, 2).toString());
                ccview.getTxtCheckIn().setText(ccview.getTable().getValueAt(row, 3) != null ? ccview.getTable().getValueAt(row, 3).toString() : "");
                ccview.getTxtCheckOut().setText(ccview.getTable().getValueAt(row, 4) != null ? ccview.getTable().getValueAt(row, 4).toString() : "");
                ccview.getTxtGhiChu().setText(ccview.getTable().getValueAt(row, 5).toString());
            }
        });
        
    }
    
    private void phanQuyenTheoChucVu() {
        String chucVu = chamCongDAO.getChucVuNhanVien(ma_nv);
        System.out.println(chucVu);
        if (!chucVu.equalsIgnoreCase("admin") && !chucVu.equalsIgnoreCase("kế toán")) {
            ccview.getBtnXoa().setVisible(false);
            ccview.getBtnSua().setVisible(false);
            ccview.getBtnExport().setVisible(false);
            ccview.getTxtSearch().setVisible(false);
            ccview.getCbThang().setVisible(false);
            ccview.getCbNam().setVisible(false);
        }
    }

    private void xuLyCheckIn(ActionEvent e) {
        try {
            //int ma_nv = Integer.parseInt(ccview.getTxtMaNv().getText().trim());
            LocalDate ngay = LocalDate.now();
            LocalTime gio = LocalTime.now();

            if (chamCongDAO.CheckedIn(ma_nv, ngay)) {
                JOptionPane.showMessageDialog(tcv, "Bạn đã check-in hôm nay!");
                return;
            }

            ChamCong cc = new ChamCong();
            cc.setMa_nv(ma_nv);
            cc.setNgay(ngay);
            cc.setCheck_in(gio);
            cc.setGhi_chu(ccview.getTxtGhiChu().getText());

            if (chamCongDAO.checkIn(cc)) {
                JOptionPane.showMessageDialog(tcv, "Check-in thành công!");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(tcv, "Check-in thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tcv, "Lỗi check-in: " + ex.getMessage());
        }
    }
    
    

    private void xuLyCheckOut(ActionEvent e) {
        try {
           
            LocalDate ngay = LocalDate.now();
            LocalTime gio = LocalTime.now();

            if (!chamCongDAO.CheckedIn(ma_nv, ngay)) {
                JOptionPane.showMessageDialog(tcv, "Bạn chưa check-in hôm nay!");
                return;
            }

            if (chamCongDAO.checkOut(ma_nv, ngay, gio)) {
                JOptionPane.showMessageDialog(tcv, "Check-out thành công!");         
                LocalDate today = LocalDate.now();
                int thang = today.getMonthValue();
                int nam = today.getYear();

                // Tính lại tổng giờ làm trong tháng từ chấm công
                int tongGioLam = chamCongDAO.tinhTongGio(ma_nv, thang, nam);
                int luongMoiGio = 20000;  // hoặc lấy từ nơi khác nếu cho phép chỉnh
                int thuongPhat = 0;
                int luong = tongGioLam * luongMoiGio + thuongPhat;
                String ghiChu = "Tự động cập nhật sau chấm công";

                Luong l = new Luong(ma_nv, tongGioLam, luongMoiGio, luong, "", ghiChu, thuongPhat);
                new LuongDAO().saveOrUpdateLuong(l, thang, nam);
                loadTable();
            } else {
                JOptionPane.showMessageDialog(tcv, "Check-out thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tcv, "Lỗi check-out: " + ex.getMessage());
        }
    }

    private void suaChamCong() {
        try {
            ChamCong cc = new ChamCong();
            cc.setMa_cham_cong(Integer.parseInt(ccview.getTxtMaChamCong().getText().trim()));
            cc.setMa_nv(Integer.parseInt(ccview.getTxtMaNv().getText().trim()));
            cc.setNgay(LocalDate.parse(ccview.getTxtNgay().getText().trim()));
            cc.setCheck_in(LocalTime.parse(ccview.getTxtCheckIn().getText().trim()));
            cc.setCheck_out(LocalTime.parse(ccview.getTxtCheckOut().getText().trim()));
            cc.setGhi_chu(ccview.getTxtGhiChu().getText().trim());

            if (chamCongDAO.updateChamCong(cc)) {
                JOptionPane.showMessageDialog(ccview, "Cập nhật thành công!");
                loadTable();
            } else {
                JOptionPane.showMessageDialog(ccview, "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ccview, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void xoaChamCong() {
        try {
            int maCC = Integer.parseInt(ccview.getTxtMaChamCong().getText().trim());
            int confirm = JOptionPane.showConfirmDialog(tcv, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (chamCongDAO.deleteChamCong(maCC)) {
                    JOptionPane.showMessageDialog(tcv, "Xóa thành công!");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(tcv, "Xóa thất bại!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(tcv, "Lỗi xóa: " + e.getMessage());
        }
    }
    
    private void locTheoThangNam() {
        String thangStr = ccview.getCbThang().getSelectedItem().toString();
        String namStr = ccview.getCbNam().getSelectedItem().toString();

        if ("Tất cả".equals(thangStr) || "Tất cả".equals(namStr)) {
            loadTable();
            return;
        }

        int thang = Integer.parseInt(thangStr.replace("Tháng ", ""));
        int nam = Integer.parseInt(namStr);

        List<ChamCong> list = chamCongDAO.getChamCongTheoThangNam(thang, nam);
        showTable(list);
    }
    
    private void showTable(List<ChamCong> danhSach) {
        DefaultTableModel model = ccview.getTableModel();
        model.setRowCount(0);
        for (ChamCong cc : danhSach) {
            model.addRow(new Object[]{
                cc.getMa_cham_cong(),
                cc.getMa_nv(),
                cc.getNgay(),
                cc.getCheck_in(),
                cc.getCheck_out(),
                cc.getGhi_chu()
            });
        }
    }
    
}
