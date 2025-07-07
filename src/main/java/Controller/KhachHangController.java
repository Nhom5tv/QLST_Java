/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Admin
 */
import DAO.KhachHangDAO;
import Model.KhachHang;
import View.KhachHangView;
import View.ThongBaodialog;
import View.KhachHangDialog;
import View.TrangChuView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KhachHangController {

    private KhachHangView khview;
    private KhachHangDAO kh_mgm;
    private TrangChuView stmv;

    public KhachHangController(TrangChuView view, KhachHangView panel) {
        this.stmv = view;
        this.khview = panel;
        this.kh_mgm = new KhachHangDAO();

        panel.addAddListener(e -> themBtnClicked());
        panel.addDeleteListener(e -> xoaBtnClicked());
        panel.addExportListener(e -> exportToExcel());
        panel.addSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiem();
            }
        });

        khview.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suaBtnClicked();
                }
            }
        });

        loadTableData();
    }

    private void loadTableData() {
        for (KhachHang kh : kh_mgm.getAllKhachHang()) {
            khview.getTableModel().addRow(new Object[]{
                false, kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(),
                kh.getDiaChi(), kh.getEmail()
            });
        }
    }

    private void themBtnClicked() {
        KhachHangDialog dialog = new KhachHangDialog(stmv, "Thêm Khách Hàng");
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            //Integer maKH = dialog.getMaKH();
            String hoTen = dialog.getHoTen();
            String soDienThoai = dialog.getSoDienThoai();
            String diaChi = dialog.getDiaChi();
            String email = dialog.getEmail();

            if (hoTen.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty() || email.isEmpty()) {
                ThongBaodialog.showErrorDialog(khview, "Vui lòng điền đầy đủ thông tin", "Lỗi");
                return;
            }

            KhachHang kh = new KhachHang(hoTen, soDienThoai, diaChi, email);
            if (kh_mgm.addKhachHang(kh)) {
                khview.getTableModel().addRow(new Object[]{
                    false, kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(),
                    kh.getDiaChi(), kh.getEmail()
                });
                ThongBaodialog.showsuccessDialog(khview, "Xác Nhận", "Thêm thành công!");
            } else {
                ThongBaodialog.showErrorDialog(khview, "Lỗi", "Thêm thất bại!");
            }
        }
    }

    private void suaBtnClicked() {
        int viewRow = khview.getTable().getSelectedRow();
        if (viewRow == -1) {
            ThongBaodialog.showWarningDialog(khview, "Cảnh Báo", "Chọn dòng để sửa!");
            return;
        }

        int row = khview.getTable().convertRowIndexToModel(viewRow);

        String maKH = khview.getTableModel().getValueAt(row, 1).toString();
        String hoTen = khview.getTableModel().getValueAt(row, 2).toString();
        String soDienThoai = khview.getTableModel().getValueAt(row, 3).toString();
        String diaChi = khview.getTableModel().getValueAt(row, 4).toString();
        String email = khview.getTableModel().getValueAt(row, 5).toString();

        KhachHangDialog dialog = new KhachHangDialog(stmv, "Sửa Khách Hàng");
        dialog.setFields(maKH, hoTen, soDienThoai, diaChi, email);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            int Makh = Integer.parseInt(maKH);
            KhachHang kh = new KhachHang(Makh, dialog.getHoTen(), dialog.getSoDienThoai(), dialog.getDiaChi(), dialog.getEmail());
            if (kh_mgm.updateKhachHang(kh)) {
                khview.getTableModel().setValueAt(kh.getHoTen(), row, 2);
                khview.getTableModel().setValueAt(kh.getSoDienThoai(), row, 3);
                khview.getTableModel().setValueAt(kh.getDiaChi(), row, 4);
                khview.getTableModel().setValueAt(kh.getEmail(), row, 5);
                ThongBaodialog.showsuccessDialog(khview, "Xác Nhận", "Sửa thành công!");
            } else {
                ThongBaodialog.showErrorDialog(khview, "Lỗi", "Sửa thất bại!");
            }
        }
    }

    private void xoaBtnClicked() {
        DefaultTableModel model = khview.getTableModel();
        int rowCount = model.getRowCount();

        java.util.List<Integer> rowsToDelete = new java.util.ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Boolean checked = (Boolean) model.getValueAt(i, 0);
            if (checked != null && checked) {
                rowsToDelete.add(i);
            }
        }

        if (!rowsToDelete.isEmpty()) {
            boolean confirmed = ThongBaodialog.showConfirmDialog(khview, "Xác Nhận Xoá", "Bạn có chắc chắn muốn xoá " + rowsToDelete.size() + " dòng đã chọn?");
            if (confirmed) {
                for (int i = rowsToDelete.size() - 1; i >= 0; i--) {
                    int row = rowsToDelete.get(i);
                    String maKH = model.getValueAt(row, 1).toString();
                    if (kh_mgm.deleteKhachHang(maKH)) {
                        model.removeRow(row);
                    } else {
                        ThongBaodialog.showErrorDialog(khview, "Lỗi", "Xoá thất bại dòng ID: " + maKH);
                    }
                }
                ThongBaodialog.showsuccessDialog(khview, "Xác Nhận", "Xoá thành công!");
            }
        } else {
            int selectedRow = khview.getTable().getSelectedRow();
            if (selectedRow != -1) {
                String maKH = model.getValueAt(selectedRow, 1).toString();
                boolean confirmed = ThongBaodialog.showConfirmDialog(khview, "Xác Nhận Xoá", "Bạn có chắc chắn muốn xoá dòng đã chọn?");
                if (confirmed) {
                    if (kh_mgm.deleteKhachHang(maKH)) {
                        model.removeRow(selectedRow);
                        ThongBaodialog.showsuccessDialog(khview, "Xác Nhận", "Xoá thành công!");
                    } else {
                        ThongBaodialog.showErrorDialog(khview, "Lỗi", "Xoá thất bại!");
                    }
                }
            }
        }
    }

    private void timKiem() {
        String keyword = khview.getSearchText().toLowerCase();
        DefaultTableModel model = khview.getTableModel();
        model.setRowCount(0);

        for (KhachHang kh : kh_mgm.searchKhachHang(keyword)) {
            model.addRow(new Object[]{
                false, kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(),
                kh.getDiaChi(), kh.getEmail()
            });
        }
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel");
        fileChooser.setSelectedFile(new File("DanhSachKhachHang.xlsx"));
        int userSelection = fileChooser.showSaveDialog(khview);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Khách Hàng");
                JTable khTable = khview.getTable();
                // Tạo header cho file Excel
                Row headerRow = sheet.createRow(0);
                for (int i = 1; i < khTable.getColumnCount(); i++) { // Bỏ qua cột checkbox
                    headerRow.createCell(i - 1).setCellValue(khTable.getColumnName(i));
                }

                // Thêm dữ liệu từ bảng vào file Excel
                for (int i = 0; i < khTable.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 1; j < khTable.getColumnCount(); j++) { // Bỏ qua cột checkbox
                        Object value = khTable.getValueAt(i, j);
                        row.createCell(j - 1).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Tự động điều chỉnh độ rộng cột
                for (int i = 0; i < khTable.getColumnCount() - 1; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Ghi file
                try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                    workbook.write(outputStream);
                    JOptionPane.showMessageDialog(khview, "Xuất Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(khview, "Lỗi khi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
