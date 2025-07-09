package Controller;

import DAO.NhanVienDAOplus;
import DAO.TaiChinhDAO;
import View.TaiChinhChartView;
import View.TaiChinhDialog;
import View.TaiChinhView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import Model.NhanVien;
import model.TaiChinh;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TaiChinhController {
    private final TaiChinhView view;
    private final TaiChinhDAO taiChinhDAO;
    private final NhanVienDAOplus nhanVienDAO;

    public TaiChinhController(TaiChinhView view) {
        this.view = view;
        this.taiChinhDAO = new TaiChinhDAO();
        this.nhanVienDAO = new NhanVienDAOplus();

        loadData(null);
        initController();
    }

    private void initController() {
        // Tìm kiếm
        view.addSearchListener(e -> loadData(view.getSearchKeyword()));

        // Thêm mới
        view.addAddListener(e -> {
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(view);
            List<NhanVien> dsNhanVien = nhanVienDAO.getAllNhanVien();

            // Giả sử TaiChinhDialog đã có constructor nhận dsNhanVien
            TaiChinhDialog dialog = new TaiChinhDialog(frame, dsNhanVien);
            dialog.setTaiChinhEdit(null); // báo là thêm mới
            dialog.setVisible(true);

            if (dialog.isSucceeded()) {
                TaiChinh newGD = dialog.getTaiChinh();
                if (taiChinhDAO.insertGiaoDich(newGD)) {
                    JOptionPane.showMessageDialog(view, "Thêm giao dịch thành công!");
                    loadData(null);
                } else {
                    JOptionPane.showMessageDialog(view, "Thêm giao dịch thất bại!");
                }
            }
        });

        // Xóa
        view.addDeleteListener(e -> {
            int[] selectedRows = view.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn ít nhất một giao dịch để xoá.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view,
                    "Bạn có chắc chắn muốn xoá các giao dịch đã chọn?",
                    "Xác nhận xoá",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            DefaultTableModel model = view.getModelTable();
            boolean allDeleted = true;
            for (int row : selectedRows) {
                int maGD = Integer.parseInt(model.getValueAt(row, 0).toString());
                if (!taiChinhDAO.deleteGiaoDich(maGD)) {
                    allDeleted = false;
                }
            }

            if (allDeleted) {
                JOptionPane.showMessageDialog(view, "Xoá thành công!");
            } else {
                JOptionPane.showMessageDialog(view, "Có một số giao dịch không thể xoá.");
            }
            loadData(null);
        });

        // Double click để sửa
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = view.getSelectedRow();
                    if (row == -1) return;

                    DefaultTableModel model = view.getModelTable();
                    int maGD = Integer.parseInt(model.getValueAt(row, 0).toString());
                    TaiChinh tc = taiChinhDAO.getById(maGD);
                    if (tc == null) {
                        JOptionPane.showMessageDialog(view, "Giao dịch không tồn tại!");
                        return;
                    }

                    Frame frame = (Frame) SwingUtilities.getWindowAncestor(view);
                    List<NhanVien> dsNhanVien = nhanVienDAO.getAllNhanVien();
                    TaiChinhDialog dialog = new TaiChinhDialog(frame, dsNhanVien);
                    dialog.setTaiChinhEdit(tc);
                    dialog.setVisible(true);

                    if (dialog.isSucceeded()) {
                        TaiChinh updated = dialog.getTaiChinh();
                        if (taiChinhDAO.updateGiaoDich(updated)) {
                            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                            loadData(null);
                        } else {
                            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
                        }
                    }
                }
            }
        });
        // Xuất Excel
view.addExportExcelListener(e -> {
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
// Xem biểu đồ
view.addChartListener(e -> {
    List<TaiChinh> dsGiaoDich = taiChinhDAO.getAllGiaoDich();
    TaiChinhChartView chartView = new TaiChinhChartView(dsGiaoDich);
    chartView.setVisible(true);
});


    }
    private boolean exportToExcel(String filePath) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Giao Dịch");

    // Tạo header
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("Mã Giao Dịch");
    header.createCell(1).setCellValue("Ngày");
    header.createCell(2).setCellValue("Loại Giao Dịch");
    header.createCell(3).setCellValue("Số Tiền");
    header.createCell(4).setCellValue("Mô Tả");
    header.createCell(5).setCellValue("Nhân Viên");

    DefaultTableModel model = view.getModelTable();
    int rowCount = model.getRowCount();

    for (int i = 0; i < rowCount; i++) {
        Row row = sheet.createRow(i + 1); // Dòng tiếp theo header
        for (int j = 0; j < model.getColumnCount(); j++) {
            Object value = model.getValueAt(i, j);
            row.createCell(j).setCellValue(value != null ? value.toString() : "");
        }
    }

    // Auto resize cột
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

   public void loadData(String keyword) {
    List<TaiChinh> list = (keyword == null || keyword.trim().isEmpty())
            ? taiChinhDAO.getAllGiaoDich()
            : taiChinhDAO.searchGiaoDich(keyword.trim());

    DefaultTableModel model = view.getModelTable();
    model.setRowCount(0);

    for (TaiChinh tc : list) {
        model.addRow(new Object[]{
                tc.getMaGiaoDich(),
                tc.getNgay(),
                tc.getLoaiGiaoDich(),
                tc.getSoTien(),
                tc.getMoTa(),
                tc.getNhanVien() != null ? tc.getNhanVien().getHoten() : ""
        });
    }
}

}
