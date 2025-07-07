package Controller;

import DAO.DanhMucSanPhamDAO;
import model.DanhMucSanPham;
import View.DanhMucView;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DanhMucController {
    private DanhMucView view;
    private DanhMucSanPhamDAO dao;
    private boolean isEditMode = false;
    private int editingId = -1;

    public DanhMucController(DanhMucView view, Connection conn) {
        this.view = view;
        this.dao = new DanhMucSanPhamDAO(conn);

        initController();
        loadTree();
        loadComboDanhMucCha(null);
    }

    private void initController() {
        view.getBtnThem().addActionListener(e -> clearForm());
        view.getBtnLuu().addActionListener(e -> saveOrUpdate());
        view.getBtnXoa().addActionListener(e -> deleteSelectedNodes());
        view.getTreeDanhMuc().addTreeSelectionListener(e -> onNodeSelected());
        view.getTxtTimKiem().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            timKiemDanhMuc();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            timKiemDanhMuc();
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            timKiemDanhMuc();
        }
    });
        view.getBtnExcel().addActionListener(e -> {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem importItem = new JMenuItem("📥 Nhập từ Excel");
        JMenuItem exportItem = new JMenuItem("📤 Xuất ra Excel");

        importItem.addActionListener(ev -> importDanhMucFromExcel());
        exportItem.addActionListener(ev -> exportDanhMucToExcel());

        menu.add(importItem);
        menu.add(exportItem);

        // Hiển thị menu ngay dưới nút Excel
        menu.show(view.getBtnExcel(), 0, view.getBtnExcel().getHeight());
    });

    }
    private void loadComboDanhMucCha(Integer excludeId) {
        view.getCboDanhMucCha().removeAllItems();
        view.getCboDanhMucCha().addItem("Danh mục gốc");

        for (DanhMucSanPham d : dao.getAll()) {
            if (excludeId == null || d.getMaDanhMuc() != excludeId) {
                view.getCboDanhMucCha().addItem(d.getTenDanhMuc());
            }
        }
    }

    private void loadTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh Mục");
        Map<Integer, DefaultMutableTreeNode> nodeMap = new HashMap<>();

        for (DanhMucSanPham d : dao.getAll()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(d);
            nodeMap.put(d.getMaDanhMuc(), node);
        }

        for (DanhMucSanPham d : dao.getAll()) {
            DefaultMutableTreeNode node = nodeMap.get(d.getMaDanhMuc());
            if (d.getMaCha() == null) {
                root.add(node);
            } else {
                DefaultMutableTreeNode parent = nodeMap.get(d.getMaCha());
                if (parent != null) parent.add(node);
                else root.add(node); // fallback
            }
        }

        view.getTreeDanhMuc().setModel(new DefaultTreeModel(root));
        for (int i = 0; i < view.getTreeDanhMuc().getRowCount(); i++) {
            view.getTreeDanhMuc().expandRow(i);
        }
    }

    private void clearForm() {
        isEditMode = false;
        editingId = -1;
        view.getTxtTenDanhMuc().setText("");
        view.getTxtMaKyHieu().setText("");
        view.getTxtMoTa().setText("");
        view.getCboDanhMucCha().setSelectedIndex(0);
        loadComboDanhMucCha(null);
    }

    private void onNodeSelected() {
        TreePath path = view.getTreeDanhMuc().getSelectionPath();
        if (path == null) return;
        Object selected = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

        if (selected instanceof DanhMucSanPham d) {
            isEditMode = true;
            editingId = d.getMaDanhMuc();

            view.getTxtTenDanhMuc().setText(d.getTenDanhMuc());
            view.getTxtMaKyHieu().setText(d.getMaKyHieu());
            view.getTxtMoTa().setText(d.getMoTa());

            // ✅ 1. Load combo trước khi set
            loadComboDanhMucCha(editingId);

            // ✅ 2. Sau đó set đúng danh mục cha nếu có
            if (d.getMaCha() == null) {
                view.getCboDanhMucCha().setSelectedItem("Danh mục gốc");
            } else {
                for (DanhMucSanPham p : dao.getAll()) {
                    if (Objects.equals(p.getMaDanhMuc(), d.getMaCha())) {
                        view.getCboDanhMucCha().setSelectedItem(p.getTenDanhMuc());
                        break;
                    }
                }
            }
        }
    }

    private String generateMaKyHieu(String tenDanhMuc, int maDanhMuc) {
        String[] words = tenDanhMuc.trim().toUpperCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.charAt(0));
            }
        }
        return result.toString() + maDanhMuc;
    }


    // ==== SAVE OR UPDATE ==== 
    private void saveOrUpdate() {
        String ten = view.getTxtTenDanhMuc().getText().trim();
        String ma = view.getTxtMaKyHieu().getText().trim();
        boolean autoGenerateMa = ma.isEmpty();
        String moTa = view.getTxtMoTa().getText().trim();

        // ==== VALIDATION ==== 
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(view, "❌ Tên danh mục không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (DanhMucSanPham existing : dao.getAll()) {
            if (existing.getTenDanhMuc().equalsIgnoreCase(ten)
                && (!isEditMode || existing.getMaDanhMuc() != editingId)) {
                JOptionPane.showMessageDialog(view, "⚠️ Danh mục với tên này đã tồn tại!", "Trùng tên", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (!autoGenerateMa) {
            for (DanhMucSanPham existing : dao.getAll()) {
                if (existing.getMaKyHieu() != null && existing.getMaKyHieu().equalsIgnoreCase(ma)
                    && (!isEditMode || existing.getMaDanhMuc() != editingId)) {
                    JOptionPane.showMessageDialog(view, "⚠️ Mã ký hiệu đã tồn tại, vui lòng chọn mã khác hoặc để trống để hệ thống tự sinh!", "Trùng mã", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        Integer maCha = null;
        String selectedCha = Objects.toString(view.getCboDanhMucCha().getSelectedItem(), "");
        if (!selectedCha.equalsIgnoreCase("Danh mục gốc")) {
            for (DanhMucSanPham d : dao.getAll()) {
                if (d.getTenDanhMuc().equalsIgnoreCase(selectedCha)) {  // chặn không chọn danh mục cha
                    maCha = d.getMaDanhMuc();
                    break;
                }
            }
        }

        if (maCha != null && isEditMode && maCha.equals(editingId)) {
            JOptionPane.showMessageDialog(view, "❌ Không thể chọn chính nó làm danh mục cha!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DanhMucSanPham d = new DanhMucSanPham(
            editingId,
            ten,
            autoGenerateMa ? null : ma,
            maCha,
            moTa,
            1
        );

        boolean success;
        if (isEditMode) {
            success = dao.update(d);
        } else {
            int newId = dao.insertAndReturnId(d);
            success = newId != -1;
            if (success && autoGenerateMa) {
                String autoMa = generateMaKyHieu(ten, newId);
                d.setMaDanhMuc(newId);
                d.setMaKyHieu(autoMa);
                dao.update(d);
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(view, "Lưu thành công!");
            clearForm();
            loadTree();
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==== DELETE ==== 
    private void deleteDanhMucVaCon(int maDanhMuc) {
        // Xoá danh mục con trước
        for (DanhMucSanPham dm : dao.getAll()) {
            if (dm.getMaCha() != null && dm.getMaCha() == maDanhMuc) {
                deleteDanhMucVaCon(dm.getMaDanhMuc());
            }
        }

        dao.delete(maDanhMuc); // Xoá chính nó cuối cùng
    }

    private void deleteSelectedNodes() {
        TreePath[] paths = view.getTreeDanhMuc().getSelectionPaths();
        if (paths == null || paths.length == 0) {
            JOptionPane.showMessageDialog(view, "⚠️ Vui lòng chọn ít nhất một danh mục để xoá.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xoá các danh mục đã chọn (và các danh mục con nếu có)?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean hasError = false;

        for (TreePath path : paths) {
            Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
            if (obj instanceof DanhMucSanPham d) {
                try {
                    deleteDanhMucVaCon(d.getMaDanhMuc());
                } catch (Exception ex) {
                    hasError = true;
                    JOptionPane.showMessageDialog(view, "❌ Không thể xoá danh mục '" + d.getTenDanhMuc() + "'. Có thể đang được sử dụng ở nơi khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (!hasError) {
            JOptionPane.showMessageDialog(view, "✅ Đã xoá thành công!");
        }

        loadTree();
        clearForm();
        loadComboDanhMucCha(null);
    }

    private void timKiemDanhMuc() {
        String keyword = view.getTxtTimKiem().getText().trim().toLowerCase();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh Mục");

        Map<Integer, DefaultMutableTreeNode> nodeMap = new HashMap<>();

        for (DanhMucSanPham d : dao.getAll()) {
            String text = (d.getTenDanhMuc() + " " + d.getMaKyHieu()).toLowerCase();
            if (keyword.isEmpty() || text.contains(keyword)) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(d);
                nodeMap.put(d.getMaDanhMuc(), node);
            }
        }

        for (DanhMucSanPham d : dao.getAll()) {
            if (!nodeMap.containsKey(d.getMaDanhMuc())) continue;

            DefaultMutableTreeNode node = nodeMap.get(d.getMaDanhMuc());

            if (d.getMaCha() == null || !nodeMap.containsKey(d.getMaCha())) {
                root.add(node);
            } else {
                nodeMap.get(d.getMaCha()).add(node);
            }
        }

        view.getTreeDanhMuc().setModel(new DefaultTreeModel(root));
        for (int i = 0; i < view.getTreeDanhMuc().getRowCount(); i++) {
            view.getTreeDanhMuc().expandRow(i);
        }
    }
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }

    private void exportDanhMucToExcel() {
        List<DanhMucSanPham> list = dao.getAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("DanhMuc");

            String[] columns = {"Mã danh mục", "Tên danh mục", "Mã ký hiệu", "Mã cha", "Mô tả", "Trạng thái (0=Ẩn, 1=Hiện)"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            for (int i = 0; i < list.size(); i++) {
                DanhMucSanPham d = list.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(d.getMaDanhMuc());
                row.createCell(1).setCellValue(d.getTenDanhMuc());
                row.createCell(2).setCellValue(d.getMaKyHieu());
                if (d.getMaCha() != null) {
                    row.createCell(3).setCellValue(d.getMaCha());
                } else {
                    row.createCell(3).setCellValue(""); 
                }
                row.createCell(4).setCellValue(d.getMoTa());
                row.createCell(5).setCellValue(d.getTrangThai());
            }

            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("danhmuc.xlsx"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try (FileOutputStream fos = new FileOutputStream(fc.getSelectedFile())) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(null, "Xuất Excel thành công!");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void importDanhMucFromExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel để nhập danh mục");

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(fileToOpen);
                 XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                XSSFSheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ dòng tiêu đề
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String ten = getCellValue(row.getCell(0)).trim();
                    String maKyHieu = getCellValue(row.getCell(1)).trim();
                    String maChaStr = getCellValue(row.getCell(2)).trim();
                    String moTa = getCellValue(row.getCell(3)).trim();
                    String trangThaiStr = getCellValue(row.getCell(4)).trim();
                    
                    
                    if (ten.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                            "❌ Thiếu tên danh mục tại dòng " + (i + 1),
                            "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Integer maCha = null;
                    if (!maChaStr.isEmpty()) {
                        try {
                            maCha = Integer.parseInt(maChaStr);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,
                                "⚠️ Mã cha không hợp lệ tại dòng " + (i + 1) + ": '" + maChaStr + "'\nVui lòng nhập số nguyên hoặc để trống.",
                                "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    int trangThai = 1;
                    if (!trangThaiStr.isEmpty()) {
                        try {
                            trangThai = Integer.parseInt(trangThaiStr);
                            if (trangThai != 0 && trangThai != 1) throw new NumberFormatException();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,
                                "⚠️ Trạng thái không hợp lệ tại dòng " + (i + 1) + ": '" + trangThaiStr + "'\nChỉ được nhập 0 hoặc 1.",
                                "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    DanhMucSanPham d = new DanhMucSanPham(0, ten, maKyHieu.isEmpty() ? null : maKyHieu, maCha, moTa, trangThai);

                    if (maKyHieu.isEmpty()) {
                        int newId = dao.insertAndReturnId(d);
                        if (newId != -1) {
                            String autoMa = generateMaKyHieu(ten, newId);
                            d.setMaDanhMuc(newId);
                            d.setMaKyHieu(autoMa);
                            dao.update(d);
                        }
                    } else {
                        dao.insert(d);
                    }
                }

                JOptionPane.showMessageDialog(null, "✅ Nhập Excel thành công!");
                loadTree();
                clearForm();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi đọc file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    



    

}

