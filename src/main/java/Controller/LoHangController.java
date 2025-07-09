/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author DUNG LE
 */
import Bridge.DBConnection;
import DAO.LoHangDAO;
import DAO.PhieuNhapDAO;
import DAO.SanPhamDAO;
import DAO.TaiChinhDAO;
import DAO.TonKhoDAO;
import Model.LoHang;
import Model.PhieuNhap;
import Model.SanPham;
import Model.TonKho;
import View.LoHangDialog;
import View.LoHangView;
import View.ThongBaodialog;
import View.TrangChuView;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class LoHangController {

    private TrangChuView trangChu;
    private LoHangView lhview;
    private LoHangDAO lhDAO;
    private SanPhamDAO spDAO;
    private PhieuNhapDAO pnDAO;
    private TonKhoDAO tkDAO;
    private TonKhoController tkController;
    private TaiChinhDAO tcDAO;
    private Connection conn;
    private TaiChinhController taiChinhController;

    public LoHangController(TrangChuView view, LoHangView panel, TonKhoController tkController) {
        this.trangChu = view;
        this.lhview = panel;
        this.lhDAO = new LoHangDAO();
        this.tcDAO=new TaiChinhDAO();
        this.taiChinhController = taiChinhController;
        try {
            this.conn = DBConnection.getConnection();
            this.spDAO = new SanPhamDAO(conn);
            this.tkDAO = new TonKhoDAO(conn);
            this.tkController = tkController;
        } catch (SQLException e) {
            e.printStackTrace();
            ThongBaodialog.showErrorDialog(lhview, "Lỗi Kết Nối", "Không thể kết nối CSDL.");
        }
        this.pnDAO = new PhieuNhapDAO();

        // Gắn sự kiện
        panel.addAddListener(e -> themLoHangbtnClicked());
        panel.addDeleteListener(e -> xoaLoHang());

        // Tìm kiếm theo mã hoặc tên sản phẩm
        panel.addSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiemSanPham();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiemSanPham();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiemSanPham();
            }
        });

        // Click 2 lần để sửa
        lhview.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suaLoHangbtnClicked();
                }
            }
        });

        // Tick checkbox khi chọn dòng
        lhview.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = lhview.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    lhview.getTableModel().setValueAt(true, selectedRow, 0);
                }
            }
        });

        loadTableData();
    }

    private void loadTableData() {
        DefaultTableModel model = lhview.getTableModel();
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (LoHang lh : lhDAO.getAllLoHang()) {
            model.addRow(new Object[]{
                false,
                lh.getMaLoHang(),
                lh.getMaPhieuNhap(),
                lh.getTenSanPham(),
                lh.getSoLuong(),
                lh.getDonGia(),
                sdf.format(lh.getNgaySanXuat()),
                sdf.format(lh.getHanSuDung()),
                lh.getGhiChu()
            });
        }
    }

    private void themLoHangbtnClicked() {
//    List<SanPham> dsSP = spDAO.getAll();
//    List<PhieuNhap> dspn = PhieuNhapDAO.getAll();
        LoHangDialog dialog = new LoHangDialog(trangChu, "Thêm Lô Hàng");
//    dialog.setComboSanPham(dsSP);
//    dialog.setComboPhieuNhap(dspn); // Đừng quên set danh sách phiếu nhập
        dialog.setComboPhieuNhap(pnDAO.getAll());
        dialog.setComboSanPham(spDAO.getAll());
        while (true) {
            dialog.setVisible(true);
            if (!dialog.isConfirmed()) {
                break;
            }

            try (Connection conn = DBConnection.getConnection()) {
                // Gọi hàm sinh mã
                int maLoHang = LoHangDAO.generateNextMaLoHang(conn);
                // Lấy dữ liệu từ dialog
                String maPhieuNhap = dialog.getPhieuNhap();
                int maSanPham = dialog.getSanPham();
                int soLuong = dialog.getSoLuong();
                double donGia = dialog.getDonGia();
                Date ngaySanXuat = new Date(dialog.getNgaySanXuat().getTime());
                Date hanSuDung = new Date(dialog.getHanSuDung().getTime());
                String ghiChu = dialog.getGhiChu();

                // Tạo đối tượng
                LoHang lh = new LoHang(maLoHang, maPhieuNhap, maSanPham, soLuong, donGia, ngaySanXuat, hanSuDung, ghiChu);

                // Gọi DAO để thêm vào DB
                if (lhDAO.themLoHang(lh)) {

                    // thêm vào tồn kho
                    TonKho tonKho = new TonKho();
                    tonKho.setMaSanPham(maSanPham);
                    tonKho.setMaLo(maLoHang);
                    tonKho.setSoLuongTon(soLuong);
                    tonKho.setSoLuongTrongKho(soLuong);
                    tonKho.setSoLuongKhaDung(soLuong);
                    tonKho.setSoLuongTrenKe(0); // mặc định, chưa ra kệ
                    tonKho.setSoLuongDangGiaoDich(0);
                    tonKho.setNguongCanhBao(10); // hoặc có thể set theo sp

                    if (!tkDAO.insert(tonKho)) {
                        ThongBaodialog.showErrorDialog(lhview, "Lỗi", "Không thể tạo dòng tồn kho!");
                        return;
                    }
                    loadTableData();
                    if (tkController != null) {
                        tkController.loadTonKhoTable();
                    }
                    ThongBaodialog.showsuccessDialog(lhview, "Xác Nhận", "Thêm lô hàng thành công!Đã cập nhật sản phẩm vào kho");

                    // 🔽 Gọi thêm khoản chi sau khi thêm lô hàng xong
                    boolean success = tcDAO.insertChiTuTatCaPhieuNhap();
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Đã tự động thêm khoản **chi** vào bảng tài chính.");
                        if (taiChinhController != null) {
                            taiChinhController.loadData(null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Không thể thêm khoản chi. Có thể đã tồn tại hoặc dữ liệu chưa đủ.");
                    }
                } else {
                    ThongBaodialog.showErrorDialog(lhview, "Lỗi", "Thêm thất bại!");
                }
            } catch (Exception ex) {
                ThongBaodialog.showErrorDialog(dialog, "Lỗi", ex.getMessage());
                ex.printStackTrace();
            }

            break;
        }
    }

    private void suaLoHangbtnClicked() {
        int row = lhview.getTable().getSelectedRow();
        if (row == -1) {
            ThongBaodialog.showWarningDialog(lhview, "Thông báo", "Vui lòng chọn dòng để sửa.");
            return;
        }

        int maLo = Integer.parseInt(lhview.getTableModel().getValueAt(row, 1).toString());
        String maPhieuNhap = lhview.getTableModel().getValueAt(row, 2).toString();
        String tenSP = lhview.getTableModel().getValueAt(row, 3).toString();
        int soLuong = Integer.parseInt(lhview.getTableModel().getValueAt(row, 4).toString());
        double donGia = Double.parseDouble(lhview.getTableModel().getValueAt(row, 5).toString());
        String ngaySXStr = lhview.getTableModel().getValueAt(row, 6).toString();
        String hanSDStr = lhview.getTableModel().getValueAt(row, 7).toString();
        String ghiChu = lhview.getTableModel().getValueAt(row, 8).toString();

//    List<SanPham> dsSP = spDAO.getAll();
        LoHangDialog dialog = new LoHangDialog(trangChu, "Sửa Lô Hàng");
        dialog.setComboPhieuNhap(pnDAO.getAll());
        dialog.setComboSanPham(spDAO.getAll());
        dialog.setf(maPhieuNhap, tenSP, soLuong, donGia, ngaySXStr, hanSDStr, ghiChu);  // Gán dữ liệu vào dialog

        while (true) {
            dialog.setVisible(true);
            if (!dialog.isConfirmed()) {
                break;
            }

            try {
                // 🟢 Lấy theo đúng thứ tự yêu cầu
                String newMaPhieuNhap = dialog.getPhieuNhap();
                int newMaSanPham = dialog.getSanPham();
                int newSoLuong = dialog.getSoLuong();
                double newDonGia = dialog.getDonGia();
                Date newNgaySX = dialog.getNgaySanXuat();
                Date newHanSD = dialog.getHanSuDung();
                String newGhiChu = dialog.getGhiChu();

                // Tạo đối tượng cập nhật
                LoHang updated = new LoHang(maLo, newMaPhieuNhap, newMaSanPham, newSoLuong, newDonGia, newNgaySX, newHanSD, newGhiChu);

                if (lhDAO.capNhatLoHang(updated)) {
                    DefaultTableModel model = lhview.getTableModel();

                    // Tìm tên SP để hiển thị thay cho mã
                    loadTableData();
                    ThongBaodialog.showsuccessDialog(lhview, "Xác Nhận", "Sửa thành công!");
                } else {
                    ThongBaodialog.showErrorDialog(lhview, "Lỗi", "Sửa thất bại!");
                }
            } catch (Exception ex) {
                ThongBaodialog.showErrorDialog(dialog, "Lỗi", ex.getMessage());
                ex.printStackTrace();
            }
            break;
        }
    }

    private void xoaLoHang() {
        DefaultTableModel model = lhview.getTableModel();
        List<Integer> rowsToDelete = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean checked = (Boolean) model.getValueAt(i, 0);
            if (checked != null && checked) {
                rowsToDelete.add(i);
            }
        }

        if (!rowsToDelete.isEmpty()) {
            boolean confirmed = ThongBaodialog.showConfirmDialog(lhview, "Xác Nhận", "Bạn có chắc muốn xoá " + rowsToDelete.size() + " dòng?");
            if (confirmed) {
                for (int i = rowsToDelete.size() - 1; i >= 0; i--) {
                    int row = rowsToDelete.get(i);
                    int maLo = Integer.parseInt(model.getValueAt(row, 1).toString());
                    if (lhDAO.xoaLoHang(maLo)) {
                        model.removeRow(row);
                    } else {
                        ThongBaodialog.showErrorDialog(lhview, "Lỗi", "Xoá thất bại mã lô: " + maLo);
                    }
                }
                updateMaLoHang(model);
                ThongBaodialog.showsuccessDialog(lhview, "Thành Công", "Xoá thành công!");
            }
        } else {
            ThongBaodialog.showWarningDialog(lhview, "Thông Báo", "Vui lòng tick chọn dòng để xoá.");
        }
    }

    private void updateMaLoHang(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            int newMaPhieu = i + 1;
            String oldMaPhieu = model.getValueAt(i, 1).toString();

            // Cập nhật trong DB
            if (lhDAO.updateMaLoHang(oldMaPhieu, newMaPhieu)) {
                // Nếu thành công thì cập nhật trong bảng
                model.setValueAt(newMaPhieu, i, 1);
            } else {
                ThongBaodialog.showErrorDialog(lhview, "Lỗi", "Không thể cập nhật mã phiếu từ " + oldMaPhieu + " -> " + newMaPhieu);
            }
        }
    }

    private void timKiemSanPham() {
        String keyword = lhview.getSearchText().trim();
        DefaultTableModel model = lhview.getTableModel();
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (LoHang lh : lhDAO.timKiemLoHang(keyword)) {
            model.addRow(new Object[]{
                false,
                lh.getMaLoHang(),
                lh.getMaPhieuNhap(),
                lh.getTenSanPham(),
                lh.getSoLuong(),
                lh.getDonGia(),
                sdf.format(lh.getNgaySanXuat()),
                sdf.format(lh.getHanSuDung()),
                lh.getGhiChu()
            });
        }
    }

}
