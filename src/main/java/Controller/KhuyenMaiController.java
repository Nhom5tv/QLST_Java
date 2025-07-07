package Controller;

import DAO.KhuyenMaiDAO;
import Model.KhuyenMai;
import View.KhuyenMaiDialog;
import View.KhuyenMaiView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KhuyenMaiController {

    private final KhuyenMaiView view;
    private final KhuyenMaiDAO dao;

    public KhuyenMaiController(KhuyenMaiView view) {
        this.view = view;
        this.dao = new KhuyenMaiDAO();  // Tạo instance DAO
        loadData(null);
        initController();
    }

    private void initController() {
        view.addSearchListener(e -> loadData(view.getTxtTimKiem().getText()));

        view.addAddListener(e -> openKhuyenMaiDialog(null));

        view.getTblKhuyenMai().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = view.getTblKhuyenMai().getSelectedRow();
                    if (row != -1) {
                        String maKM = (String) view.getModelTable().getValueAt(row, 0);
                        KhuyenMai km = dao.getById(maKM);
                        if (km != null) {
                            openKhuyenMaiDialog(km);
                        } else {
                            JOptionPane.showMessageDialog(view, "Không tìm thấy khuyến mãi!");
                        }
                    }
                }
            }
        });

        view.getBtnXuatExcel().addActionListener(e -> exportExcelHandler());

        view.addDeleteListener(e -> {
            int row = view.getTblKhuyenMai().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn khuyến mãi cần xoá!");
                return;
            }
            String maKM = (String) view.getModelTable().getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(view,
                    "Bạn có chắc muốn xoá khuyến mãi mã: " + maKM + "?",
                    "Xác nhận xoá", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteKhuyenMai(maKM)) {
                    JOptionPane.showMessageDialog(view, "Xoá thành công!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(view, "Xoá thất bại!");
                }
            }
        });
    }

    private void openKhuyenMaiDialog(KhuyenMai km) {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(view);
        KhuyenMaiDialog dialog = new KhuyenMaiDialog(frame);
        dialog.setKhuyenMaiEdit(km);
        dialog.setVisible(true);
        loadData(null);
    }

    private void exportExcelHandler() {
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
    }

    private boolean exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách khuyến mãi");

        // Tạo tiêu đề
        Row header = sheet.createRow(0);
        String[] columns = {
            "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Ngày Bắt Đầu", "Ngày Kết Thúc",
            "Phần Trăm Giảm", "Số Lượng", "Tổng Tiền Tối Thiểu", "Số Lượng SP Tối Thiểu", "Ghi Chú"
        };

        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        // Ghi dữ liệu từ bảng
        DefaultTableModel model = view.getModelTable();
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                row.createCell(j).setCellValue(value != null ? value.toString() : "");
            }
        }

        // Tự căn chỉnh độ rộng cột
        for (int i = 0; i < columns.length; i++) {
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

    private void loadData(String keyword) {
        List<KhuyenMai> danhSach = (keyword == null || keyword.isBlank())
                ? dao.getAllKhuyenMai()
                : dao.searchKhuyenMai(keyword);
        view.addKhuyenMaiToTable(danhSach);
    }

    // ✅ Thêm vào bên trong class
    public String kiemTraDieuKienApDung(String maKhuyenMai, BigDecimal tongTienHoaDon, Integer soLuongSanPham) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            return "Mã khuyến mãi không được để trống.";
        }

        KhuyenMai km = dao.getById(maKhuyenMai);
        if (km == null) {
            return "Mã khuyến mãi không tồn tại.";
        }

        Date now = new Date();
        if (now.before(km.getNgayBatDau()) || now.after(km.getNgayKetThuc())) {
            return "Mã khuyến mãi không còn hiệu lực.";
        }

        if (km.getSoLuong() == 0) {
            return "Mã khuyến mãi đã hết lượt sử dụng.";
        }

        if (km.getSoLuong() < 0) {
            return "Số lượng mã khuyến mãi không hợp lệ.";
        }

        if (km.getTongTienToiThieu() != null && tongTienHoaDon.compareTo(km.getTongTienToiThieu()) < 0) {
            return "Tổng tiền hóa đơn chưa đạt điều kiện tối thiểu để áp dụng mã giảm giá.";
        }

        if (km.getSoLuongSpToiThieu() != null && (soLuongSanPham == null || soLuongSanPham < km.getSoLuongSpToiThieu())) {
            return "Số lượng sản phẩm chưa đạt điều kiện tối thiểu để áp dụng mã giảm giá.";
        }

        return null; // Hợp lệ
    }

}
