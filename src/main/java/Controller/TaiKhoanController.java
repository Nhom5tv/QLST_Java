package Controller;



import DAO.KhachHangDAO;
import DAO.NhanVienDAOplus;
import DAO.TaiKhoanDAO;
import Model.KhachHang;
import Model.TaiKhoan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import model.NhanVien;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.TaiKhoanDialog;
import View.TaiKhoanView;

public class TaiKhoanController {
    private TaiKhoanView view;

    public TaiKhoanController(TaiKhoanView view) {
        this.view = view;
        addEventHandlers();
        loadTableData(null);
    }

    private void addEventHandlers() {
        view.getBtnTimKiem().addActionListener(e -> {
            String keyword = view.getTxtTimKiem().getText();
            loadTableData(keyword);
        });

        view.getBtnThem().addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(view);
            Frame frame = (window instanceof Frame) ? (Frame) window : null;
            TaiKhoanDialog dialog = new TaiKhoanDialog(frame);
            dialog.setTaiKhoanEdit(null);  // Thêm mới
            dialog.setVisible(true);
            loadTableData(null);
        });

        view.getBtnXoa().addActionListener(e -> {
            int row = view.getTblTaiKhoan().getSelectedRow();
            DefaultTableModel model = view.getModelTable();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản cần xóa!");
                return;
            }

            int maTaiKhoan = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = TaiKhoanDAO.delete(maTaiKhoan);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Xóa tài khoản thành công!");
                    loadTableData(null);
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa tài khoản thất bại!");
                }
            }
        });

        view.getTblTaiKhoan().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = view.getTblTaiKhoan().getSelectedRow();
                    if (row != -1) {
                        int maTaiKhoan = (int) view.getModelTable().getValueAt(row, 0);
                        TaiKhoan tk = TaiKhoanDAO.getById(maTaiKhoan);
                        if (tk != null) {
                            Window window = SwingUtilities.getWindowAncestor(view);
                            Frame frame = (window instanceof Frame) ? (Frame) window : null;
                            TaiKhoanDialog dialog = new TaiKhoanDialog(frame);
                            dialog.setTaiKhoanEdit(tk);
                            dialog.setVisible(true);
                            loadTableData(null);
                        } else {
                            JOptionPane.showMessageDialog(view, "Tài khoản không tồn tại!");
                        }
                    }
                }
            }
        });

        view.getBtnXuatExcel().addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            int userSelection = fileChooser.showSaveDialog(view);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                boolean success = exportToExcel(filePath);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Xuất Excel thành công!");
                } else {
                    JOptionPane.showMessageDialog(view, "Xuất Excel thất bại!");
                }
            }
        });
    }

    private void loadTableData(String keyword) {
        DefaultTableModel modelTable = view.getModelTable();
        modelTable.setRowCount(0);  // clear table

        List<TaiKhoan> list;
        if (keyword == null || keyword.trim().isEmpty()) {
            list = TaiKhoanDAO.getAllTaiKhoan();
        } else {
            list = TaiKhoanDAO.searchTaiKhoan(keyword.trim());
        }

        NhanVienDAOplus nhanVienDAO = new NhanVienDAOplus();
        KhachHangDAO khachHangDAO = new KhachHangDAO();

        for (TaiKhoan tk : list) {
            String tenLienKet = "";
            if (tk.getIdLienKet() != null) {
                if ("nhanvien".equalsIgnoreCase(tk.getQuyen())) {
                    NhanVien nv = nhanVienDAO.getNhanVienById(tk.getIdLienKet());
                    tenLienKet = (nv != null) ? nv.getHoten(): "";
                } else if ("khachhang".equalsIgnoreCase(tk.getQuyen())) {
                    KhachHang kh = khachHangDAO.getKhachHangById(tk.getIdLienKet());
                    tenLienKet = (kh != null) ? kh.getHoTen() : "";
                }
            }

            modelTable.addRow(new Object[]{
                    tk.getMaTaiKhoan(),
                    tenLienKet,
                    tk.getTenDangNhap(),
                    tk.getMatKhau(),
                    tk.getQuyen()
            });
        }
    }

    private boolean exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách tài khoản");

        // Tạo header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Mã Tài Khoản");
        header.createCell(1).setCellValue("Tên liên kết");
        header.createCell(2).setCellValue("Tên đăng nhập");
        header.createCell(3).setCellValue("Mật khẩu");
        header.createCell(4).setCellValue("Quyền");

        DefaultTableModel model = view.getModelTable();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.createRow(i + 1);  // +1 vì hàng đầu là header
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                row.createCell(j).setCellValue(value != null ? value.toString() : "");
            }
        }

        // Tự động căn chỉnh độ rộng cột
        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            workbook.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
