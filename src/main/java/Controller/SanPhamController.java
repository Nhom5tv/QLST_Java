package Controller;

import DAO.DanhMucSanPhamDAO;
import DAO.NhaCungCapDao;
import DAO.SanPhamDAO;
import Model.NhaCungCap;
import Model.SanPham;
import View.ChiTietSanPhamDialog;
import View.SanPhamView;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import model.DanhMucSanPham;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SanPhamController {
    private SanPhamView view;
    private SanPhamDAO dao;
    private DanhMucSanPhamDAO dmDao;
  

    public SanPhamController(SanPhamView view, Connection conn) {
        this.view = view;
        this.dao = new SanPhamDAO(conn);
        this.dmDao = new DanhMucSanPhamDAO(conn);
        initController();
        loadSanPhamToTable();
    }

    private void initController() {
        // thêm mới
        view.getBtnThem().addActionListener(e -> onAdd());
        
        view.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() == 2) {
                onEdit(); // double click để sửa
            }
        }
    });


        view.addDeleteListener(e -> onDelete());
        
        //tìm kiếm
        view.addSearchListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            onSearch();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onSearch();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onSearch();
        }
    });
        // đổ dữ liệu vào combo
        List<String> tenDanhMucs = dmDao.getAll()
            .stream()
            .map(DanhMucSanPham::getTenDanhMuc)
            .toList(); 

        view.setDanhMucFilterOptions(tenDanhMucs);
        
        

        //lọc
        view.getBtnResetFilter().addActionListener(e -> {
        view.getCboDanhMucFilter().setSelectedIndex(0);
        view.getCboTrangThaiFilter().setSelectedIndex(0);
        view.getSearchTextField().setText(""); 
        onFilter(); // gọi lại hàm lọc để reset về trạng thái ban đầu
    });
        view.getCboDanhMucFilter().addActionListener(e -> onFilter());
        view.getCboTrangThaiFilter().addActionListener(e -> onFilter());

        view.getBtnResetFilter().addActionListener(e -> {
            view.getCboDanhMucFilter().setSelectedIndex(0);
            view.getCboTrangThaiFilter().setSelectedIndex(0);
            view.getSearchTextField().setText("");
            onFilter();
        });

        // nhập xuất Excel
        view.getBtnExcel().addActionListener(e -> {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem importItem = new JMenuItem("📥 Nhập từ Excel");
        JMenuItem exportItem = new JMenuItem("📤 Xuất ra Excel");

        importItem.addActionListener(ev -> importSanPhamFromExcel());
        exportItem.addActionListener(ev -> exportSanPhamToExcel());

        menu.add(importItem);
        menu.add(exportItem);

        // Hiển thị menu ngay dưới nút Excel
        menu.show(view.getBtnExcel(), 0, view.getBtnExcel().getHeight());
    });
    }



   private void loadSanPhamToTable() {
        List<SanPham> list = dao.getAll();
        populateTable(list);
    }

    private void populateTable(List<SanPham> list) {
     DefaultTableModel model = view.getTableModel();
     model.setRowCount(0);

     for (SanPham sp : list) {
         // Xử lý ảnh (byte[] → ImageIcon)
         ImageIcon icon = null;
         byte[] imageBytes = sp.getHinhAnh();
         if (imageBytes != null) {
             ImageIcon temp = new ImageIcon(imageBytes);
             Image scaled = temp.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
             icon = new ImageIcon(scaled);
         }

         // Thêm vào bảng
         model.addRow(new Object[]{
             false,
             sp.getMaSanPham(),  // mã sp ẩn id tự tăng
             icon,
             sp.getMaNgoai(),
             sp.getTenSanPham(),
             sp.getGiaGoc(),
             sp.getGiaBan(),
             sp.getDonViTinh(),
             sp.getSoLuongTon()
         });
     }
 }

    private void onAdd() {
        ChiTietSanPhamDialog dialog = new ChiTietSanPhamDialog(null);
        dialog.getBtnHuy().addActionListener(e -> dialog.dispose());
        // ẩn mã ngoại sp đi 
        dialog.getTxtMaSP().setVisible(false);
        dialog.getTxtMaSP().setText("");
        dialog.getTxtTonKho().setEditable(false);


        // Gọi DAO để lấy danh sách danh mục từ DB
        List<DanhMucSanPham> danhMucList = dmDao.getAll();
        dialog.setDanhMucOptions(danhMucList);

        dialog.getBtnLuu().addActionListener(ev -> {
            SanPham sp = getSanPhamFromDialog(dialog); // Validate và gán dữ liệu
            if (sp == null) return;

            // 👉 1. Insert sản phẩm (chưa có maNgoai)
            int generatedId = dao.insertAndReturnId(sp);
            if (generatedId > 0) {
                // 👉 2. Sinh mã ngoài: SPxxx
                String maNgoai = "SP" + String.format("%03d", generatedId);
                sp.setMaSanPham(generatedId);  // cần để update đúng sản phẩm
                sp.setMaNgoai(maNgoai);

                // 👉 3. Update lại mã ngoài (chỉ cập nhật cột `ma_ngoai`)
                dao.updateMaNgoai(sp);

                JOptionPane.showMessageDialog(view, "✔️ Đã thêm sản phẩm thành công!");
                dialog.dispose();
                loadSanPhamToTable();
            } else {
                JOptionPane.showMessageDialog(view, "❌ Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }


    private void onEdit() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "⚠️ Vui lòng chọn sản phẩm để sửa.");
            return;
        }

        int maSanPham = (int) view.getTableModel().getValueAt(selectedRow, 1);
        Optional<SanPham> opt = dao.getById(maSanPham);

        if (opt.isEmpty()) {
            JOptionPane.showMessageDialog(view, "❌ Không tìm thấy sản phẩm để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
       

        SanPham sp = opt.get();
        ChiTietSanPhamDialog dialog = new ChiTietSanPhamDialog(null);
        dialog.getBtnHuy().addActionListener(e -> dialog.dispose());
        dialog.setDanhMucOptions(dmDao.getAll()); // nạp danh mục
        dialog.getTxtTonKho().setEditable(false);

        // ✏️ Đổ dữ liệu vào dialog
        dialog.setMaSanPham(sp.getMaSanPham()); // Lưu lại ID
        dialog.getTxtMaSP().setText(sp.getMaNgoai());
        dialog.getTxtMaSP().setEditable(false);  // không sửa mã ngoại
        dialog.getTxtMaSP().setFocusable(false); 
        dialog.getTxtTenSP().setText(sp.getTenSanPham());
        dialog.getTxtDonVi().setText(sp.getDonViTinh());
        dialog.getTxtGiaGoc().setText(sp.getGiaGoc().toString());
        dialog.getTxtGiaBan().setText(sp.getGiaBan().toString());
        dialog.getTxtMaVach().setText(sp.getMaVach());
        dialog.getTxtMoTa().setText(sp.getMoTa());
        dialog.getTxtTonKho().setText(String.valueOf(sp.getSoLuongTon()));
        dialog.getTxtThuongHieu().setText(sp.getThuongHieu());
        dialog.getTxtXuatXu().setText(sp.getXuatXu());
        dialog.getTxtThanhPhan().setText(sp.getThanhPhan());
        dialog.getTxtHuongDan().setText(sp.getHuongDanSuDung());
        dialog.getTxtBaoQuan().setText(sp.getBaoQuan());
        System.out.println("Bao quan"+ sp.getBaoQuan());
        dialog.setTrangThai(sp.getTrangThai());

        // ảnh
        byte[] hinh = sp.getHinhAnh();
        if (hinh != null) {
            ImageIcon icon = new ImageIcon(hinh);
            Image scaled = icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
            dialog.getLblPreviewImage().setIcon(new ImageIcon(scaled));
        }

        // chọn danh mục tương ứng
        for (int i = 0; i < dialog.getCboDanhMuc().getItemCount(); i++) {
            if (dialog.getCboDanhMuc().getItemAt(i).getMaDanhMuc() == sp.getMaDanhMuc()) {
                dialog.getCboDanhMuc().setSelectedIndex(i);
                break;
            }
        }

        // Xử lý khi nhấn Lưu
        dialog.getBtnLuu().addActionListener(ev -> {
            SanPham updated = getSanPhamFromDialog(dialog);
            if (updated != null) {
                
                updated.setMaSanPham(sp.getMaSanPham()); // cần để update đúng dòng
                updated.setMaNgoai(sp.getMaNgoai());
                System.out.println("bao quan update"+ updated.getBaoQuan());
                dao.update(updated);
                dialog.dispose();
                loadSanPhamToTable();
                JOptionPane.showMessageDialog(view, "✔️ Cập nhật sản phẩm thành công!");
            }
        });

        dialog.setVisible(true);
    }

    private void onDelete() {
        JTable table = view.getTable();
        DefaultTableModel model = view.getTableModel();
        int rowCount = model.getRowCount();
        int deleted = 0;

        int confirm = JOptionPane.showConfirmDialog(
            view, "Bạn có chắc chắn muốn xoá các sản phẩm đã chọn?", "Xác nhận xoá", JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        for (int i = rowCount - 1; i >= 0; i--) {
            Boolean checked = (Boolean) model.getValueAt(i, 0);
            if (Boolean.TRUE.equals(checked)) {
                int maSanPham = (int) model.getValueAt(i, 1);  // Cột ID là cột 1
                boolean success = dao.delete(maSanPham);       // Gọi xoá theo id
                if (success) {
                    model.removeRow(i);
                    deleted++;
                }
            }
        }

        if (deleted > 0) {
            JOptionPane.showMessageDialog(view, "✔️ Đã xoá " + deleted + " sản phẩm.");
        } else {
            JOptionPane.showMessageDialog(view, "⚠️ Không có sản phẩm nào được chọn để xoá.");
        }
    }
    private void onSearch() {
        String keyword = view.getSearchText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadSanPhamToTable(); // Nếu rỗng thì load toàn bộ
        } else {
            List<SanPham> result = dao.searchWithTonKho(keyword);
            populateTable(result);
        }
    }


    private byte[] convertImageToBytes(Image image) {
        try {
            BufferedImage buffered = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = buffered.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buffered, "jpg", baos); // hoặc "png"
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private SanPham getSanPhamFromDialog(ChiTietSanPhamDialog dialog) {
        // Lấy từng giá trị và kiểm tra rỗng
        String maNgoai = dialog.getTxtMaSP().getText().trim();
        String tenSP = dialog.getTxtTenSP().getText().trim();
        String donVi = dialog.getTxtDonVi().getText().trim();
        String giaGocStr = dialog.getTxtGiaGoc().getText().trim();
        String giaBanStr = dialog.getTxtGiaBan().getText().trim();
        String maVach = dialog.getTxtMaVach().getText().trim();
        String moTa = dialog.getTxtMoTa().getText().trim();
        String baoQuan= dialog.getTxtBaoQuan().getText().trim();
        String thuongHieu = dialog.getTxtThuongHieu().getText().trim();
        String xuatXu = dialog.getTxtXuatXu().getText().trim();

        if (tenSP.isEmpty() || donVi.isEmpty() || giaGocStr.isEmpty() ||
            giaBanStr.isEmpty() ) {
            JOptionPane.showMessageDialog(dialog, "⚠️ Vui lòng nhập đầy đủ thông tin bắt buộc!",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        double giaGoc, giaBan;
        

        try {
            giaGoc = Double.parseDouble(giaGocStr);
            giaBan = Double.parseDouble(giaBanStr);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "⚠️ Giá phải là số hợp lệ!",
                    "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Tạo đối tượng
        SanPham sp = new SanPham();
        sp.setMaSanPham(dialog.getMaSanPham());
        sp.setMaNgoai(maNgoai);
        sp.setTenSanPham(tenSP);
        DanhMucSanPham selected = (DanhMucSanPham) dialog.getCboDanhMuc().getSelectedItem();
        if (selected != null) {
            sp.setMaDanhMuc(selected.getMaDanhMuc());
        }
        System.out.println("Selected danh muc: " + dialog.getCboDanhMuc().getSelectedItem());
System.out.println("Class: " + dialog.getCboDanhMuc().getSelectedItem().getClass());
        sp.setDonViTinh(donVi);
        sp.setMoTa(moTa);
        sp.setGiaGoc(BigDecimal.valueOf(giaGoc));
        sp.setGiaBan(BigDecimal.valueOf(giaBan));
        sp.setMaVach(maVach);
        sp.setTrangThai(dialog.getTrangThai());
        sp.setThuongHieu(thuongHieu);
        sp.setXuatXu(xuatXu);
        sp.setThanhPhan(dialog.getTxtThanhPhan().getText().trim());
        sp.setHuongDanSuDung(dialog.getTxtHuongDan().getText().trim());
        sp.setBaoQuan(baoQuan);

        // Xử lý ảnh (nếu có)
       Icon icon = dialog.getLblPreviewImage().getIcon();
        if (icon instanceof ImageIcon imageIcon && imageIcon.getIconWidth() > 0 && imageIcon.getIconHeight() > 0) {
            Image img = imageIcon.getImage();
            sp.setHinhAnh(convertImageToBytes(img));
        } else {
            sp.setHinhAnh(null);  // hoặc giữ nguyên ảnh cũ nếu đang cập nhật
        }


        return sp;
    }
     private void onFilter() {
        String keyword = view.getSearchText().trim().toLowerCase();
        String selectedDanhMuc = (String) view.getCboDanhMucFilter().getSelectedItem();
        String selectedTrangThai = (String) view.getCboTrangThaiFilter().getSelectedItem();

        int trangThai = -1;
        if (selectedTrangThai.equals("Đang bán")) trangThai = 1;
        else if (selectedTrangThai.equals("Ngừng bán")) trangThai = 0;

        List<SanPham> filtered = dao.searchWithFilters(keyword, selectedDanhMuc, trangThai);
        populateTable(filtered);
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

    private void exportSanPhamToExcel() {
        // Lấy dữ liệu theo bộ lọc đang áp dụng
        String keyword = view.getSearchText().trim().toLowerCase();
        String selectedDanhMuc = (String) view.getCboDanhMucFilter().getSelectedItem();
        String selectedTrangThai = (String) view.getCboTrangThaiFilter().getSelectedItem();

        int trangThai = -1;
        if ("Đang bán".equals(selectedTrangThai)) trangThai = 1;
        else if ("Ngừng bán".equals(selectedTrangThai)) trangThai = 0;

        List<SanPham> list = dao.searchWithFilters(keyword, selectedDanhMuc, trangThai);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("SanPham");

            String[] columns = {
                "Mã SP", "Tên sản phẩm", "Mã danh mục", "Giá gốc", "Giá bán",
                "Mã vạch", "Đơn vị tính", "Mô tả", "Trạng thái", "Thương hiệu",
                "Xuất xứ", "Thành phần", "HDSD", "Bảo quản", "Tồn kho"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            for (int i = 0; i < list.size(); i++) {
                SanPham sp = list.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(sp.getMaNgoai());
                row.createCell(1).setCellValue(sp.getTenSanPham());
                row.createCell(2).setCellValue(sp.getMaDanhMuc());
                row.createCell(3).setCellValue(sp.getGiaGoc().doubleValue());
                row.createCell(4).setCellValue(sp.getGiaBan().doubleValue());
                row.createCell(5).setCellValue(sp.getMaVach() != null ? sp.getMaVach() : "");
                row.createCell(6).setCellValue(sp.getDonViTinh());
                row.createCell(7).setCellValue(sp.getMoTa() != null ? sp.getMoTa() : "");
                row.createCell(8).setCellValue(sp.getTrangThai());
                row.createCell(9).setCellValue(sp.getThuongHieu());
                row.createCell(10).setCellValue(sp.getXuatXu());
                row.createCell(11).setCellValue(sp.getThanhPhan());
                row.createCell(12).setCellValue(sp.getHuongDanSuDung());
                row.createCell(13).setCellValue(sp.getBaoQuan());
                row.createCell(14).setCellValue(sp.getSoLuongTon());
            }

            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("sanpham_loc.xlsx"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try (FileOutputStream fos = new FileOutputStream(fc.getSelectedFile())) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(null, "✔️ Xuất Excel theo bộ lọc thành công!");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi xuất Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importSanPhamFromExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel để nhập sản phẩm");

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(fileToOpen);
                 XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    String ten = getCellValue(row.getCell(0));                // Tên sản phẩm
                    String maDmStr = getCellValue(row.getCell(1));            // Mã danh mục
                    String giaGocStr = getCellValue(row.getCell(2));          // Giá gốc
                    String giaBanStr = getCellValue(row.getCell(3));          // Giá bán
                    String maVach = getCellValue(row.getCell(4));             // Mã vạch
                    String donVi = getCellValue(row.getCell(5));              // Đơn vị tính
                    String moTa = getCellValue(row.getCell(6));               // Mô tả
                    String trangThaiStr = getCellValue(row.getCell(7));       // Trạng thái
                    String thuongHieu = getCellValue(row.getCell(8));         // Thương hiệu
                    String xuatXu = getCellValue(row.getCell(9));             // Xuất xứ
                    String thanhPhan = getCellValue(row.getCell(10));         // Thành phần
                    String huongDan = getCellValue(row.getCell(11));          // HDSD
                    String baoQuan = getCellValue(row.getCell(12));           // Bảo quản
                    String tonKhoStr = getCellValue(row.getCell(13));         // Tồn kho


                    if (ten.isEmpty() || maDmStr.isEmpty() || giaGocStr.isEmpty() || giaBanStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "❌ Dòng " + (i + 1) + " thiếu dữ liệu bắt buộc!");
                        return;
                    }

                    SanPham sp = new SanPham();
                    sp.setMaNgoai("");
                    sp.setTenSanPham(ten);
                    sp.setMaDanhMuc(Integer.parseInt(maDmStr));
                    sp.setGiaGoc(new BigDecimal(giaGocStr));
                    sp.setGiaBan(new BigDecimal(giaBanStr));
                    sp.setMaVach(maVach);
                    sp.setDonViTinh(donVi);
                    sp.setMoTa(moTa);
                    sp.setTrangThai(trangThaiStr.equals("0") ? 0 : 1);
                    sp.setThuongHieu(thuongHieu);
                    sp.setXuatXu(xuatXu);
                    sp.setThanhPhan(thanhPhan);
                    sp.setHuongDanSuDung(huongDan);
                    sp.setBaoQuan(baoQuan);
                    sp.setSoLuongTon(tonKhoStr.isEmpty() ? 0 : Integer.parseInt(tonKhoStr));

                    int newId = dao.insertAndReturnId(sp); // thêm SP chưa có ma_ngoai
                    if (newId > 0) {
                        String autoCode = "SP" + String.format("%03d", newId);
                        sp.setMaSanPham(newId);
                        sp.setMaNgoai(autoCode);
                        dao.updateMaNgoai(sp); // cập nhật mã ngoại
                    }
                                    }

                JOptionPane.showMessageDialog(null, "✅ Nhập sản phẩm từ Excel thành công!");
                loadSanPhamToTable(); // Refresh bảng
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "❌ Lỗi khi đọc file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    

  
}
