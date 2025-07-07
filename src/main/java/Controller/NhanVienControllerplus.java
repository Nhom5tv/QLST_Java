package Controller;
import DAO.NhanVienDAOplus;
import View.NhanVienCaNhanView2;
import View.NhanVienView1;
import View.ThongBaodialog;
import View.TrangChuView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import model.NhanVien;
import view.NhanVienDialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienControllerplus {
    private TrangChuView mainView;
    private NhanVienView1 nvView;
    private NhanVienDAOplus nvDAO;
    private int ma_nv;
    private NhanVienCaNhanView2 nvcnV2;
    
    public NhanVienControllerplus(TrangChuView mainView,NhanVienCaNhanView2 nvcnV2, int ma_nv){
        this.mainView = mainView;
        this.nvcnV2 = nvcnV2;
        this.nvDAO = new NhanVienDAOplus();
        this.ma_nv = ma_nv;
        nvcnV2.getBtnCapNhat().addActionListener(e -> UpdateTTCN());
    }

    public NhanVienControllerplus(TrangChuView mainView, NhanVienView1 nvView) {
        this.mainView = mainView;
        this.nvView = nvView;
        this.nvDAO = new NhanVienDAOplus();

        nvView.addAddListener(e -> themNhanVien());
        nvView.addDeleteListener(e -> xoaNhanVien());
        nvView.addSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { timKiem(); }
            @Override
            public void removeUpdate(DocumentEvent e) { timKiem(); }
            @Override
            public void changedUpdate(DocumentEvent e) { timKiem(); }
        });

        nvView.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suaNhanVien();
                }
            }
        });

        loadTableData();
    }

    private void loadTableData() {
        DefaultTableModel model = nvView.getTableModel();
        model.setRowCount(0);
        for (NhanVien nv : nvDAO.getAllNhanVien()) {
            model.addRow(new Object[]{
                    false,
                    nv.getma_nv(),          // hoặc nv.getMaNV() tuỳ thuộc model của bạn
                    nv.getHoten(),
                    nv.getCccd(),
                    nv.getSdt(),
                    nv.getChucvu(),
                    nv.getEmail(),
                    nv.getGioitinh(),
                    nv.getGhichu(),
                    NhanVienView1.scaleImageIcon(convertByteArrayToImageIcon(nv.getAnh()), 40, 50)
            });
        }
    }
    public void UpdateTTCN() {
    System.out.println("=== UpdateTTCN được gọi ==="); // Debug log
     try {
        NhanVien nv = new NhanVien();
        nv.setma_nv(Integer.parseInt(nvcnV2.getTxtId().getText()));
        nv.setHoten(nvcnV2.getTxtHoTen().getText());
        nv.setCccd(nvcnV2.getTxtCCCD().getText());
        nv.setSdt(nvcnV2.getTxtSDT().getText());
        nv.setChucvu(nvcnV2.getTxtChucVu().getText());
        nv.setEmail(nvcnV2.getTxtEmail().getText());
        nv.setGhichu(nvcnV2.getTxtGhiChu().getText());
        String gioiTinh = nvcnV2.getRbMr().isSelected() ? "Nam" : "Nữ";
        nv.setGioitinh(gioiTinh);
        byte[] anhHienTai = getCurrentImageBytes(); // Lấy ảnh hiện tại
            if (anhHienTai != null) {
                nv.setAnh(anhHienTai);
            } else {
                // Nếu không có ảnh mới, lấy ảnh cũ từ DB
                int maNV = Integer.parseInt(nvcnV2.getTxtId().getText());
                NhanVien nvCu = nvDAO.getNhanVienById(maNV);
                if (nvCu != null) {
                    nv.setAnh(nvCu.getAnh());
                }
            }

            System.out.println("Đang cập nhật nhân viên ID: " + nv.getma_nv());
            boolean success = nvDAO.updateNhanVien(nv);

            if (success) {
                JOptionPane.showMessageDialog(nvcnV2, "Cập nhật thành công!");
            } else {
                JOptionPane.showMessageDialog(nvcnV2, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(nvcnV2, "Mã nhân viên không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(nvcnV2, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ⭐ Thêm method helper để lấy ảnh hiện tại
    private byte[] getCurrentImageBytes() {
        // Nếu có chức năng chọn ảnh mới, return ảnh mới
        // Hiện tại return null để giữ nguyên ảnh cũ
        return null;
    }

    private void themNhanVien() {
        NhanVienDialog dialog = new NhanVienDialog(mainView, "Thêm Nhân Viên");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            // Lấy dữ liệu từ dialog
            String hoTen = dialog.getHoTen();
            String cccd = dialog.getCCCD();
            String soDienThoai = dialog.getSDT();
            String chucvu = dialog.getChucVu();
            String email = dialog.getEmail();
            String gioitinh = dialog.getGioiTinh();
            String ghichu = dialog.getGhiChu();
            byte[] anh = dialog.getImageBytes();  // Phải có phương thức getAnhBytes() trả về byte[] ảnh trong dialog

            // Tạo đối tượng NhanVien mới
            NhanVien nv = new NhanVien(hoTen, cccd, soDienThoai, chucvu, email, gioitinh, ghichu, anh);

            // Thêm nhân viên qua DAO
            if (nvDAO.insertNhanVien(nv)) {
                // Thêm vào bảng giao diện
                nvView.getTableModel().addRow(new Object[]{
                        false,
                        nv.getma_nv(),  // ID mới (nếu trả về từ DB sau khi thêm)
                        nv.getHoten(),
                        nv.getCccd(),
                        nv.getSdt(),
                        nv.getChucvu(),
                        nv.getEmail(),
                        nv.getGioitinh(),
                        nv.getGhichu(),
                        NhanVienView1.scaleImageIcon(convertByteArrayToImageIcon(nv.getAnh()), 40, 50)
                });
                ThongBaodialog.showsuccessDialog(nvView, "Xác Nhận", "Thêm thành công!");
            } else {
                ThongBaodialog.showErrorDialog(nvView, "Lỗi", "Thêm thất bại!");
            }
        }
    }

   private void suaNhanVien() {
    int row = nvView.getTable().getSelectedRow();
    if (row == -1) {
        ThongBaodialog.showWarningDialog(nvView, "Cảnh báo", "Chọn dòng để sửa!");
        return;
    }

    int ma_nv = (int) nvView.getTableModel().getValueAt(row, 1);

    // 🔁 Truy vấn lại từ DAO để lấy đúng byte[] ảnh
    NhanVien nv = nvDAO.getNhanVienById(ma_nv);
    if (nv == null) {
        ThongBaodialog.showErrorDialog(nvView, "Lỗi", "Không tìm thấy dữ liệu nhân viên.");
        return;
    }

    NhanVienDialog dialog = new NhanVienDialog(mainView, "Sửa Nhân Viên");
    dialog.setFields(
        nv.getma_nv(),
        nv.getHoten(),
        nv.getCccd(),
        nv.getSdt(),
        nv.getChucvu(),
        nv.getEmail(),
        nv.getGioitinh(),
        nv.getGhichu(),
        nv.getAnh()  // <-- ảnh thật từ CSDL
    );
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
        NhanVien updatedNV = new NhanVien(
                ma_nv,
                dialog.getHoTen(),
                dialog.getCCCD(),
                dialog.getSDT(),
                dialog.getChucVu(),
                dialog.getEmail(),
                dialog.getGioiTinh(),
                dialog.getGhiChu(),
                dialog.getImageBytes()
        );

        if (nvDAO.updateNhanVien(updatedNV)) {
            loadTableData();
            ThongBaodialog.showsuccessDialog(nvView, "Thành công", "Sửa thành công!");
        } else {
            ThongBaodialog.showErrorDialog(nvView, "Lỗi", "Sửa thất bại!");
        }
    }
}
   private void xoaNhanVien() {
    DefaultTableModel model = nvView.getTableModel();
    JTable table = nvView.getTable();
    List<Integer> selectedRows = new ArrayList<>();

    // ✅ Tìm các dòng đã được tick chọn
    for (int i = 0; i < model.getRowCount(); i++) {
        Boolean checked = (Boolean) model.getValueAt(i, 0);
        if (Boolean.TRUE.equals(checked)) {
            selectedRows.add(i);
        }
    }

    if (selectedRows.isEmpty()) {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            ThongBaodialog.showWarningDialog(nvView, "Cảnh báo", "Chọn dòng để xoá!");
            return;
        }

        int modelRow = table.convertRowIndexToModel(selected);
        int ma_nv = (int) model.getValueAt(modelRow, 1);
        boolean confirm = ThongBaodialog.showConfirmDialog(nvView, "Xác nhận xoá", "Bạn có chắc muốn xoá nhân viên này?");
        if (confirm && nvDAO.deleteNhanVien(ma_nv)) {
            model.removeRow(modelRow);
            ThongBaodialog.showsuccessDialog(nvView, "Xoá", "Xoá thành công!");
        } else {
            ThongBaodialog.showErrorDialog(nvView, "Lỗi", "Xoá thất bại!");
        }
    } else {
        boolean confirm = ThongBaodialog.showConfirmDialog(nvView, "Xác nhận xoá", "Bạn có chắc muốn xoá " + selectedRows.size() + " nhân viên?");
        if (confirm) {
            selectedRows.sort((a, b) -> b - a); // Duyệt ngược
            for (int row : selectedRows) {
                int ma_nv = (int) model.getValueAt(row, 1);
                boolean deleted = nvDAO.deleteNhanVien(ma_nv);
                System.out.println("Xoá mã: " + ma_nv + " -> " + deleted);
                if (deleted) {
                    model.removeRow(row);
                }
            }
            ThongBaodialog.showsuccessDialog(nvView, "Xoá", "Đã xoá thành công!");
        }
    }
}



    private void timKiem() {
        String keyword = nvView.getSearchText().toLowerCase();
        List<NhanVien> list = nvDAO.searchNhanVien(keyword);
        DefaultTableModel model = nvView.getTableModel();
        model.setRowCount(0);

        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                    false,
                    nv.getma_nv(),
                    nv.getHoten(),
                    nv.getCccd(),
                    nv.getSdt(),
                    nv.getChucvu(),
                    nv.getEmail(),
                    nv.getGioitinh(),
                    nv.getGhichu(),
                    NhanVienView1.scaleImageIcon(convertByteArrayToImageIcon(nv.getAnh()), 40, 50)
            });
        }
    }

    private ImageIcon convertByteArrayToImageIcon(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) return null;
        try {
            Image img = Toolkit.getDefaultToolkit().createImage(imageBytes);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
