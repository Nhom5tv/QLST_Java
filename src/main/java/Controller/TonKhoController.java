package Controller;

import DAO.TonKhoDAO;
import Model.TonKho;
import View.DieuChinhTonKhoDialog;
import View.XuatHangLenKeDialog;
import View.TonKhoView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TonKhoController {
    private TonKhoView view;
    private TonKhoDAO dao;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private TableRowSorter<DefaultTableModel> sorter;

    public TonKhoController(TonKhoView view, Connection conn) {
        this.view = view;
        this.dao = new TonKhoDAO(conn);
        this.sorter = new TableRowSorter<>(view.getTableModel());
        this.view.getTable().setRowSorter(sorter);
        loadTonKhoTable();
        addEventListeners();
    }

    public void loadTonKhoTable() {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        List<TonKho> list = dao.getAll();
        for (TonKho tk : list) {
            model.addRow(new Object[]{
                    tk.getMaSanPham(),
                    tk.getTenSanPham(),
                    tk.getMaLo(),
                    tk.getHanSuDung(),
                    tk.getSoLuongTon(),
                    tk.getSoLuongTrenKe(),
                    tk.getSoLuongTrongKho(),
                    tk.getSoLuongKhaDung(),
                    tk.getSoLuongDangGiaoDich(),
                    tk.getNguongCanhBao()
            });
        }
    }

    private void addEventListeners() {
        view.getBtnChuyenHang().addActionListener(e -> showXuatHangLenKeDialog());
        view.getBtnDieuChinh().addActionListener(e -> showDieuChinhDialog());
        view.getBtnExcel().addActionListener(e -> exportTonKhoToExcel());

        view.getTxtSearch().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        view.getCboHSDFilter().addActionListener(e -> filter());
        view.getChkCanhBao().addActionListener(e -> filter());
        view.getSpnSoNgaySapHetHan().addChangeListener(e -> filter());
        view.getBtnReset().addActionListener(e -> resetFilter());
    }

    private void filter() {
        String keyword = view.getSearchText().toLowerCase();
        String selectedHSD = (String) view.getCboHSDFilter().getSelectedItem();
        boolean onlyCanhBao = view.getChkCanhBao().isSelected();
        int soNgay = (int) view.getSpnSoNgaySapHetHan().getValue();

        RowFilter<Object, Object> combined = new RowFilter<>() {
            @Override
            public boolean include(Entry<?, ?> e) {
                String tenSP = e.getStringValue(1).toLowerCase();
                String maLo = e.getStringValue(2).toLowerCase();
                String hsdText = e.getStringValue(3);

                boolean matchKeyword = tenSP.contains(keyword) || maLo.contains(keyword);
                boolean matchHSD = true;

                if (!"Tất cả".equals(selectedHSD) && !hsdText.isEmpty()) {
                    try {
                        Date hsd = sdf.parse(hsdText);
                        Date today = new Date();
                        long diff = (hsd.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
                        switch (selectedHSD) {
                            case "Còn hạn" -> matchHSD = hsd.after(today);
                            case "Hết hạn" -> matchHSD = hsd.before(today);
                            case "Sắp hết hạn" -> matchHSD = diff >= 0 && diff <= soNgay;
                        }
                    } catch (ParseException ex) {
                        return false;
                    }
                }

                boolean matchCanhBao = !onlyCanhBao || ((Integer) e.getValue(7)) < ((Integer) e.getValue(9));
                return matchKeyword && matchHSD && matchCanhBao;
            }
        };

        sorter.setRowFilter(combined);
    }

    private void resetFilter() {
        view.getTxtSearch().setText("");
        view.getCboHSDFilter().setSelectedIndex(0);
        view.getChkCanhBao().setSelected(false);
        view.getSpnSoNgaySapHetHan().setValue(7);
        sorter.setRowFilter(null);
    }

    private void showXuatHangLenKeDialog() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "⚠️ Vui lòng chọn dòng cần xuất.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maSP = (int) view.getTableModel().getValueAt(row, 0);
        String tenSP = view.getTableModel().getValueAt(row, 1).toString();
        int maLo = (int) view.getTableModel().getValueAt(row, 2);
        int trongKho = (int) view.getTableModel().getValueAt(row, 6);

        XuatHangLenKeDialog dialog = new XuatHangLenKeDialog(null);
        dialog.setThongTin(tenSP, String.valueOf(maLo), trongKho);

        dialog.addXacNhanListener(e -> {
            int soLuong = dialog.getSoLuongMuonChuyen();
            if (dao.chuyenHangRaKe(maSP, maLo, soLuong)) {
                JOptionPane.showMessageDialog(view, "✅ Xuất hàng thành công!");
                dialog.dispose();
                loadTonKhoTable();
            } else {
                JOptionPane.showMessageDialog(view, "❌ Không thể xuất hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.addHuyListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void showDieuChinhDialog() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "⚠️ Vui lòng chọn dòng cần điều chỉnh.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        row = view.getTable().convertRowIndexToModel(row);

        DefaultTableModel model = view.getTableModel();
        int maSP = (int) model.getValueAt(row, 0);
        String tenSP = model.getValueAt(row, 1).toString();
        int maLo = (int) model.getValueAt(row, 2);
        String hsd = model.getValueAt(row, 3).toString();

        int tong = (int) model.getValueAt(row, 4);
        int trenKe = (int) model.getValueAt(row, 5);
        int trongKho = (int) model.getValueAt(row, 6);
        int khaDung = (int) model.getValueAt(row, 7);
        int dangGiao = (int) model.getValueAt(row, 8);
        int nguongCanhBao = (int) model.getValueAt(row, 9);

        DieuChinhTonKhoDialog dialog = new DieuChinhTonKhoDialog(null);
        dialog.setThongTin(tenSP, String.valueOf(maLo), hsd);
        dialog.setGiaTriTonKho(tong, trenKe, trongKho, khaDung, dangGiao);
        dialog.setNguongCanhBao(nguongCanhBao);

        dialog.getBtnXacNhan().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TonKho tk = new TonKho();
                tk.setMaSanPham(maSP);
                tk.setMaLo(maLo);
                tk.setSoLuongTon(dialog.getTong());
                tk.setSoLuongTrenKe(dialog.getTrenKe());
                tk.setSoLuongTrongKho(dialog.getTrongKho());
                tk.setSoLuongKhaDung(dialog.getKhaDung());
                tk.setSoLuongDangGiaoDich(dialog.getDangGiao());
                tk.setNguongCanhBao(dialog.getNguongCanhBao());

                if (dao.update(tk)) {
                    JOptionPane.showMessageDialog(view, "✅ Đã cập nhật tồn kho thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "❌ Cập nhật thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
                loadTonKhoTable();
            }
        });

        dialog.getBtnHuy().addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void exportTonKhoToExcel() {
        DefaultTableModel model = view.getTableModel();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("TonKho");

            // Tạo header
            String[] columns = {
                "Mã sản phẩm", "Tên sản phẩm", "Mã lô", "Hạn sử dụng",
                "Tồn tổng", "Trên kệ", "Trong kho",
                "Khả dụng", "Đang giao dịch", "Ngưỡng cảnh báo"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Duyệt qua bảng để ghi từng dòng
            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    if (value instanceof Integer) {
                        row.createCell(j).setCellValue((Integer) value);
                    } else if (value instanceof java.util.Date) {
                        row.createCell(j).setCellValue(value.toString());
                    } else {
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }
            }

            // Hộp thoại lưu file
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("ton_kho.xlsx"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try (FileOutputStream fos = new FileOutputStream(fc.getSelectedFile())) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(null, "✅ Xuất Excel tồn kho thành công!");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi xuất Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
