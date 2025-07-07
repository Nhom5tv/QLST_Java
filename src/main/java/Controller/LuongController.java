/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.LuongDAO;
import Model.Luong;
import View.LuongView;
import View.TrangChuView;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author banhb
 */
public class LuongController {
    private LuongView lview;
    private LuongDAO dao;
    private Luong luongDangChon = null;
    private TrangChuView tcv;
    public LuongController(LuongView panel,TrangChuView tcview) {
        this.lview = panel;
        this.tcv = tcview;
        this.dao = new LuongDAO();
        init();
    }

    private void init() {
        for (int i = 1; i <= 12; i++) lview.cboThang.addItem(String.valueOf(i));
        for (int i = 2023; i <= 2030; i++) lview.cboNam.addItem(String.valueOf(i));

        lview.cboThang.setSelectedItem(String.valueOf(java.time.LocalDate.now().getMonthValue()));
        lview.cboNam.setSelectedItem(String.valueOf(java.time.LocalDate.now().getYear()));

        loadLuongTheoThangNam();

        lview.btnReset.addActionListener(e -> loadLuongTheoThangNam());
        
        lview.txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            filterTable();
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            filterTable();
        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            filterTable();
        }
    });

        // Sự kiện chọn dòng
        lview.tblLuong.getSelectionModel().addListSelectionListener(e -> {
            int row = lview.tblLuong.getSelectedRow();
            if (row >= 0) {
                int ma_nv = (int) lview.tblLuong.getValueAt(row, 0);
                String hoten = lview.tblLuong.getValueAt(row, 1).toString();
                int tong_gio = (int) lview.tblLuong.getValueAt(row, 2);
                int luong_moi_gio = (int) lview.tblLuong.getValueAt(row, 3);
                int thuong_phat = (int) lview.tblLuong.getValueAt(row, 4);
                int luong = (int) lview.tblLuong.getValueAt(row, 5);
                String ghi_chu = lview.tblLuong.getValueAt(row, 6).toString();

                luongDangChon = new Luong(ma_nv, tong_gio, luong_moi_gio, luong, hoten, ghi_chu, thuong_phat);
                lview.txtLuongMoiGio.setText(String.valueOf(luong_moi_gio));
                lview.txtThuongPhat.setText(String.valueOf(thuong_phat));
                lview.txtGhiChu.setText(ghi_chu);
            }
        });

        // Sửa lương mỗi giờ + thưởng/phạt + ghi chú
        lview.btnSua.addActionListener(e -> {
            if (luongDangChon == null) {
                JOptionPane.showMessageDialog(tcv, "Vui lòng chọn nhân viên cần lưu.");
                return;
            }

            try {
                int luongMoi = Integer.parseInt(lview.txtLuongMoiGio.getText());
                int thuongPhat = Integer.parseInt(lview.txtThuongPhat.getText());
                String ghiChuMoi = lview.txtGhiChu.getText();

                luongDangChon.setLuong_moi_gio(luongMoi);
                luongDangChon.setThuong_phat(thuongPhat);
                luongDangChon.setGhi_chu(ghiChuMoi);

                int tongGio = luongDangChon.getTong_gio_lam();
                int tongLuong = tongGio * luongMoi + thuongPhat;
                luongDangChon.setLuong(tongLuong);

                int row = lview.tblLuong.getSelectedRow();
                lview.tableModel.setValueAt(luongMoi, row, 3);
                lview.tableModel.setValueAt(thuongPhat, row, 4);
                lview.tableModel.setValueAt(tongLuong, row, 5);
                lview.tableModel.setValueAt(ghiChuMoi, row, 6);

                int thang = Integer.parseInt(lview.cboThang.getSelectedItem().toString());
                int nam = Integer.parseInt(lview.cboNam.getSelectedItem().toString());

                dao.saveOrUpdateLuong(luongDangChon, thang, nam);

                JOptionPane.showMessageDialog(lview, "Đã lưu thông tin lương vào CSDL.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(lview, "Lương mỗi giờ và Thưởng/Phạt phải là số nguyên.");
            }
        });
        
        lview.btnLuuTatCa.addActionListener(e -> {
            int thang = Integer.parseInt(lview.cboThang.getSelectedItem().toString());
            int nam = Integer.parseInt(lview.cboNam.getSelectedItem().toString());

            DefaultTableModel model = lview.tableModel;
            int rowCount = model.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                try {
                    int ma_nv = (int) model.getValueAt(i, 0);
                    String hoten = model.getValueAt(i, 1).toString();
                    int tongGio = (int) model.getValueAt(i, 2);
                    int luongMoi = (int) model.getValueAt(i, 3);
                    int thuongPhat = (int) model.getValueAt(i, 4);
                    int luong = tongGio * luongMoi + thuongPhat;
                    String ghiChu = model.getValueAt(i, 6).toString();

                    Luong l = new Luong(ma_nv, tongGio, luongMoi, luong, hoten, ghiChu, thuongPhat);
                    dao.saveOrUpdateLuong(l, thang, nam);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            JOptionPane.showMessageDialog(lview, "Đã lưu tất cả thông tin lương vào CSDL.");
        });
        
        lview.btnXuatExcel.addActionListener(e -> {
            LuongExcel.export(lview.tblLuong);
        });

    }

    private void loadLuongTheoThangNam() {
        int thang = Integer.parseInt(lview.cboThang.getSelectedItem().toString());
        int nam = Integer.parseInt(lview.cboNam.getSelectedItem().toString());
        int luongMoiGio = 20000; // Giá trị mặc định

        List<Luong> ds = dao.getDanhSachLuong(thang, nam, luongMoiGio);

        DefaultTableModel model = lview.tableModel;
        model.setRowCount(0);
        for (Luong l : ds) {
            model.addRow(new Object[]{
                l.getMa_nv(), l.getHoten(), l.getTong_gio_lam(),
                l.getLuong_moi_gio(), l.getThuong_phat(), l.getLuong(), l.getGhi_chu()
            });
        }
    }
    
    private void filterTable() {
        String keyword = lview.txtTimKiem.getText().toLowerCase();
        DefaultTableModel model = lview.tableModel;
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        lview.tblLuong.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1)); // Cột 1 là Họ tên
    }
}
