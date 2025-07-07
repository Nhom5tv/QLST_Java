package Controller;

import DAO.NhaCungCapDao;
import DAO.PhieuNhapDAO;
import Model.NhaCungCap;
import Model.PhieuNhap;
import View.PhieuNhapDialog;
import View.PhieuNhapView;
import View.ThongBaodialog;
import View.TrangChuView;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class PhieuNhapController {
    private PhieuNhapView pnview;
    private PhieuNhapDAO pn_mgm;
    private NhaCungCapDao ncc_mgm;
    private TrangChuView stmv;

    public PhieuNhapController(TrangChuView view, PhieuNhapView panel) {
        this.stmv = view;
        this.pnview = panel;
        this.pn_mgm = new PhieuNhapDAO();
        this.ncc_mgm = new NhaCungCapDao();

        // Gắn sự kiện
        panel.addAddListener(e -> thembtnclicked());
        panel.addDeleteListener(e -> xoabtnclicked());

        panel.addSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiemTheoNgayNhap();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiemTheoNgayNhap();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiemTheoNgayNhap();
            }
        });

        // Bắt sự kiện click 2 lần để sửa
        pnview.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suabtnclicked();
                }
            }
        });

        // Tick checkbox khi chọn dòng
        pnview.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = pnview.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    pnview.getTableModel().setValueAt(true, selectedRow, 0);
                }
            }
        });
        
        loadTableData();
    }
   private void loadTableData() {
    DefaultTableModel model = pnview.getTableModel();
    model.setRowCount(0); // Xóa bảng cũ

    for (PhieuNhap pn : pn_mgm.getAll()) {
        model.addRow(new Object[]{
            false,
            pn.getMaPhieu(),
            pn.getTenNcc(), // <-- Dùng trực tiếp
            pn.getNgayNhap(),
            pn.getGhiChu()
        });
    }
}


   private void thembtnclicked() {
    List<NhaCungCap> dsNCC = ncc_mgm.getAll();
    PhieuNhapDialog dialog = new PhieuNhapDialog(stmv, "Thêm Phiếu Nhập");
    dialog.setComboNCCOptions(dsNCC);  // Load NCC vào combo (hiển thị tên, lưu mã)

    while (true) {
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) break;

        // Lấy mã NCC người dùng chọn
        String selectedNccId = dialog.getNCC();
        Date ngayNhap = (Date) dialog.getDate(); // Lấy ngày từ JDatePickerImpl
        String ghichu = dialog.getGhiChu();

        try {
            // Tạo phiếu nhập mới - mã phiếu null để DB tự tạo
            PhieuNhap pn = new PhieuNhap(null, selectedNccId, ngayNhap, ghichu);

            if (pn_mgm.insert(pn)) {
                loadTableData();  // Load lại bảng, trong đó cột NCC hiển thị tên, không phải mã
                ThongBaodialog.showsuccessDialog(pnview, "Xác Nhận", "Thêm thành công!");
            } else {
                ThongBaodialog.showErrorDialog(pnview, "Lỗi", "Thêm thất bại!");
            }
        } catch (Exception ex) {
            ThongBaodialog.showErrorDialog(dialog, "Lỗi", ex.getMessage());
            ex.printStackTrace();
        }
        break;
    }
}

private void suabtnclicked() {
    int row = pnview.getTable().getSelectedRow();
    if (row == -1) {
        ThongBaodialog.showWarningDialog(pnview, "Cảnh báo", "Chọn dòng để sửa!");
        return;
    }

    String maPhieu = pnview.getTableModel().getValueAt(row, 1).toString();
    String nccName = pnview.getTableModel().getValueAt(row, 2).toString();  // Tên NCC đang hiển thị
    String ngayNhapStr = pnview.getTableModel().getValueAt(row, 3).toString();
    String ghichu = pnview.getTableModel().getValueAt(row, 4).toString();

    List<NhaCungCap> dsNCC = ncc_mgm.getAll();
    PhieuNhapDialog dialog = new PhieuNhapDialog(stmv, "Sửa Phiếu Nhập");
    dialog.setComboNCCOptions(dsNCC);  // Load combo box (hiển thị tên, lưu mã)

    dialog.setFields(nccName, ngayNhapStr, ghichu);  // Set dữ liệu cũ

    while (true) {
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) break;

        // Lấy mã NCC người dùng chọn trong dialog
        String selectedNccId = dialog.getNCC();
        Date newNgayNhap = (Date) dialog.getDate();
        String newGhiChu = dialog.getGhiChu();

        try {
            PhieuNhap updated = new PhieuNhap(maPhieu, selectedNccId, newNgayNhap, newGhiChu);

            if (pn_mgm.update(updated)) {
                DefaultTableModel model = pnview.getTableModel();

                // Tìm tên NCC theo mã mới từ danh sách NCC để hiển thị
                String selectedNccName = dsNCC.stream()
                        .filter(ncc -> ncc.getNccid().equals(selectedNccId))
                        .map(NhaCungCap::getNccname)
                        .findFirst()
                        .orElse(selectedNccId);  // Nếu không tìm thấy, hiển thị mã luôn

                model.setValueAt(selectedNccName, row, 2);  // Cập nhật tên NCC trong bảng
                model.setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(updated.getNgayNhap()), row, 3);
                model.setValueAt(updated.getGhiChu(), row, 4);

                ThongBaodialog.showsuccessDialog(pnview, "Xác Nhận", "Sửa thành công!");
            } else {
                ThongBaodialog.showErrorDialog(pnview, "Lỗi", "Sửa thất bại!");
            }
        } catch (Exception ex) {
            ThongBaodialog.showErrorDialog(dialog, "Lỗi", ex.getMessage());
            ex.printStackTrace();
        }
        break;
    }
}


    private void xoabtnclicked() {
    DefaultTableModel model = pnview.getTableModel();
    int rowCount = model.getRowCount();

    List<Integer> rowsToDelete = new ArrayList<>();
    for (int i = 0; i < rowCount; i++) {
        Boolean checked = (Boolean) model.getValueAt(i, 0);
        if (checked != null && checked) {
            rowsToDelete.add(i);
        }
    }

    if (!rowsToDelete.isEmpty()) {
        boolean confirmed = ThongBaodialog.showConfirmDialog(pnview, "Xác Nhận Xoá", "Bạn có chắc chắn muốn xoá " + rowsToDelete.size() + " dòng?");
        if (confirmed) {
            for (int i = rowsToDelete.size() - 1; i >= 0; i--) {
                int row = rowsToDelete.get(i);
                String id = model.getValueAt(row, 1).toString();
                if (pn_mgm.delete(id)) {
                    model.removeRow(row);
                } else {
                    ThongBaodialog.showErrorDialog(pnview, "Lỗi", "Xoá thất bại dòng ID: " + id);
                }
            }
            // Cập nhật lại mã phiếu nhập
            updateMaPhieuNhap(model);
            ThongBaodialog.showsuccessDialog(pnview, "Xác Nhận", "Xoá thành công!");
        }
    } else {
        int selectedRow = pnview.getTable().getSelectedRow();
        if (selectedRow != -1) {
            boolean confirmed = ThongBaodialog.showConfirmDialog(pnview, "Xác Nhận Xoá", "Bạn có muốn xoá dòng đã chọn?");
            if (confirmed) {
                String id = model.getValueAt(selectedRow, 1).toString();
                if (pn_mgm.delete(id)) {
                    model.removeRow(selectedRow);
                    // Cập nhật lại mã phiếu nhập
                    updateMaPhieuNhap(model);
                    ThongBaodialog.showsuccessDialog(pnview, "Xác Nhận", "Xoá thành công!");
                } else {
                    ThongBaodialog.showErrorDialog(pnview, "Lỗi", "Xoá thất bại!");
                }
            }
        } else {
            ThongBaodialog.showWarningDialog(pnview, "Thông Báo", "Vui lòng tick hoặc chọn dòng cần xoá!");
        }
    }
}

private void updateMaPhieuNhap(DefaultTableModel model) {
    for (int i = 0; i < model.getRowCount(); i++) {
        int newMaPhieu = i + 1;
        String oldMaPhieu = model.getValueAt(i, 1).toString();

        // Cập nhật trong DB
        if (pn_mgm.updateMaPhieuNhap(oldMaPhieu, newMaPhieu)) {
            // Nếu thành công thì cập nhật trong bảng
            model.setValueAt(newMaPhieu, i, 1);
        } else {
            ThongBaodialog.showErrorDialog(pnview, "Lỗi", "Không thể cập nhật mã phiếu từ " + oldMaPhieu + " -> " + newMaPhieu);
        }
    }
}


    // ✅ Tìm kiếm theo ngày nhập
    private void timKiemTheoNgayNhap() {
    String keyword = pnview.getSearchText().trim().toLowerCase();
    DefaultTableModel model = pnview.getTableModel();
    model.setRowCount(0);

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    List<NhaCungCap> dsNCC = ncc_mgm.getAll();

    for (PhieuNhap pn : pn_mgm.getAll()) {
        String formattedDate = sdf.format(pn.getNgayNhap());
        if (formattedDate.toLowerCase().contains(keyword)) {
            String tenNcc = dsNCC.stream()
                    .filter(ncc -> ncc.getNccid().equals(pn.getMaNCC()))
                    .map(NhaCungCap::getNccname)
                    .findFirst()
                    .orElse(pn.getMaNCC());

            model.addRow(new Object[]{
                false,
                pn.getMaPhieu(),
                tenNcc, // thay vì mã NCC
                formattedDate,
                pn.getGhiChu()
            });
        }
    }
}

}
