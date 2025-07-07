/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.NhaCungCapDao;
import Model.NhaCungCap;
import View.NhaCungCapView;
import View.NhaCungCapDialog;
import View.TrangChuView;
import View.ThongBaodialog;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;



/**
 *
 * @author DUNG LE
 */
public class NhaCungCapController {
    private NhaCungCapView nccview;
    private NhaCungCapDao ncc_mgm;
    private TrangChuView stmv;
     private String generateRandomId() {
    int randomNum = (int)(Math.random() * 90000) + 10000; // 7 số ngẫu nhiên từ 1000000 đến 9999999
    return "NCC" + randomNum;
}
    public NhaCungCapController(TrangChuView view,NhaCungCapView panel){
        this.stmv = view;
        this.nccview = panel;
        this.ncc_mgm = new NhaCungCapDao();
        
        panel.addAddListener(e -> thembtnclicked());
        panel.addDeleteListener(e -> xoabtnclicked());
         panel.addImportExcelListener(e -> ImportToExcel());
        panel.addExportExcelListener(e -> exportToExcel());
        panel.addExportPdfListener(e -> exportToPdf());
        panel.addSearchListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
        timkiem();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        timkiem();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        timkiem();
        }
});
        nccview.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) { // click 2 lần để sửa
            suabtnclicked(); // gọi lại phương thức sửa đã viết
        }
    }
   

});
        // Bắt sự kiện chọn dòng (chọn 1 dòng thì tick checkbox cột 0 của dòng đó)
nccview.getTable().getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        int selectedRow = nccview.getTable().getSelectedRow();
        if (selectedRow != -1) {
            // Đặt checkbox cột 0 = true cho dòng được chọn
            nccview.getTableModel().setValueAt(true, selectedRow, 0);
        }
    }
});

        loadTableData();
    }
    private boolean isInputValid(NhaCungCapDialog dialog) {
    String nccId = dialog.getId();
    String nccName = dialog.getName();
    String ncclh = dialog.getLhName();
    String email = dialog.getEmail();
    String phone = dialog.getPhone();
    String diachi = dialog.getAddress();
    if (nccId.isEmpty() || nccName.isEmpty() || ncclh.isEmpty() || email.isEmpty() || phone.isEmpty() || diachi.isEmpty()) {
        ThongBaodialog.showErrorDialog(nccview, "Vui lòng điền đầy đủ thông tin", "Lỗi");
        return false;
    }
    if (!nccName.matches("[a-zA-ZÀ-ỹ\\s]+") || !ncclh.matches("[a-zA-ZÀ-ỹ\\s]+")) {
        ThongBaodialog.showErrorDialog(nccview, "Tên không hợp lệ. Vui lòng chỉ nhập chữ cái.", "Lỗi");
        return false;
    }
    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
        ThongBaodialog.showWarningDialog(nccview, "Email không hợp lệ. Vui lòng nhập đúng định dạng.", "Lỗi");
        return false;
    }
    if (phone.length() < 10 || !phone.matches("\\d+")) {
        ThongBaodialog.showErrorDialog(nccview, "Số điện thoại phải đủ 10 chữ số và chỉ được nhập số", "Lỗi");
        return false;
    }
    return true;
}

    private void loadTableData(){
        for(NhaCungCap ncc : ncc_mgm.getallNCC()){
            nccview.getTableModel().addRow(new Object[]{
                false,
                ncc.getNccid(),
                ncc.getNccname(),
                ncc.getNcclh(),
                ncc.getEmail(),
                ncc.getPhone(),
                ncc.getDiachincc()
            });
        }
    }
   private void thembtnclicked() {
    NhaCungCapDialog dialog = new NhaCungCapDialog(stmv, "Thêm Nhà Cung Cấp");
    
    while (true) {
        dialog.setVisible(true); // Mở dialog

        if (!dialog.isConfirmed()) {
            break; // Người dùng bấm Cancel → thoát
        }

        String nccId = generateRandomId();
        String nccName = dialog.getName();
        String ncclh = dialog.getLhName();
        String email = dialog.getEmail();
        String phone = dialog.getPhone();
        String diachi = dialog.getAddress();

        // Kiểm tra hợp lệ
        if (nccId.isEmpty() || nccName.isEmpty() || ncclh.isEmpty() || email.isEmpty() || phone.isEmpty() || diachi.isEmpty()) {
            ThongBaodialog.showErrorDialog(dialog,"Lỗi","Vui lòng điền đầy đủ thông tin");
            continue; // quay lại vòng lặp → giữ dialog mở
        }

        if (!nccName.matches("[a-zA-ZÀ-ỹ\\s]+") || !ncclh.matches("[a-zA-ZÀ-ỹ\\s]+")) {
            ThongBaodialog.showErrorDialog(dialog,"Lỗi", "Tên không hợp lệ. Vui lòng chỉ nhập chữ cái.");
            continue;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            ThongBaodialog.showErrorDialog(dialog,"Lỗi", "Email không hợp lệ.");
            continue;
        }

        if (phone.length() < 10 || !phone.matches("\\d+")) {
            ThongBaodialog.showErrorDialog(dialog, "Lỗi","Số điện thoại phải đủ 10 chữ số và chỉ được nhập số");
            continue;
        }

        // Nếu hợp lệ → thoát vòng lặp
        NhaCungCap ncc = new NhaCungCap(nccId, nccName, ncclh, email, phone, diachi);
        boolean success = ncc_mgm.themnhacungcap(ncc);
        if (success) {
            nccview.getTableModel().addRow(new Object[]{
                false, ncc.getNccid(), ncc.getNccname(), ncc.getNcclh(),
                ncc.getEmail(), ncc.getPhone(), ncc.getDiachincc()
            });
            ThongBaodialog.showsuccessDialog(nccview,"Xác Nhận", "Thêm thành công!");
        } else {
            ThongBaodialog.showErrorDialog(nccview,"Lỗi", "Thêm thất bại!");
        }
        break; // Thêm xong thì thoát vòng lặp
    }
}

    private void suabtnclicked() {
    int row = nccview.getTable().getSelectedRow();
    if (row == -1) {
        ThongBaodialog.showWarningDialog(nccview,"Cảnh Cáo","Chọn dòng để sửa!");
        return;
    }
   String id = nccview.getTableModel().getValueAt(row, 1).toString();
    String ten = nccview.getTableModel().getValueAt(row, 2).toString();
    String lh = nccview.getTableModel().getValueAt(row, 3).toString();
    String email = nccview.getTableModel().getValueAt(row, 4).toString();
    String sdt = nccview.getTableModel().getValueAt(row, 5).toString();
    String diaChi = nccview.getTableModel().getValueAt(row, 6).toString();
    while (true) {
        NhaCungCapDialog dialog = new NhaCungCapDialog(stmv, "Sửa Nhà Cung Cấp");
        dialog.setFields(ten, lh, email, sdt, diaChi);
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            // Người dùng bấm "Hủy" hoặc đóng dialog
            break;
        }
        // Lấy thông tin mới
        String newTen = dialog.getName();
        String newLh = dialog.getLhName();
        String newEmail = dialog.getEmail();
        String newPhone = dialog.getPhone();
        String newDiaChi = dialog.getAddress();

        // Kiểm tra hợp lệ
        if (newTen.isEmpty() || newLh.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty() || newDiaChi.isEmpty()) {
            ThongBaodialog.showErrorDialog(dialog, "Lỗi","Vui lòng điền đầy đủ thông tin");
            continue;
        }

        if (!newTen.matches("[a-zA-ZÀ-ỹ\\s]+") || !newLh.matches("[a-zA-ZÀ-ỹ\\s]+")) {
            ThongBaodialog.showErrorDialog(dialog,"Lỗi", "Tên không hợp lệ. Vui lòng chỉ nhập chữ cái.");
            continue;
        }

        if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            ThongBaodialog.showErrorDialog(dialog,"Lỗi", "Email không hợp lệ.");
            continue;
        }

        if (newPhone.length() < 10 || !newPhone.matches("\\d+")) {
            ThongBaodialog.showErrorDialog(dialog,"Lỗi", "Số điện thoại phải đủ 10 chữ số và chỉ được nhập số");
            continue;
        }

        // Hợp lệ → cập nhật model
        NhaCungCap updated = new NhaCungCap(id, newTen, newLh, newEmail, newPhone, newDiaChi);

        if (ncc_mgm.updatenhacungcap(updated)) {
            nccview.getTableModel().setValueAt(updated.getNccname(), row, 2);
            nccview.getTableModel().setValueAt(updated.getNcclh(), row, 3);
            nccview.getTableModel().setValueAt(updated.getEmail(), row, 4);
            nccview.getTableModel().setValueAt(updated.getPhone(), row, 5);
            nccview.getTableModel().setValueAt(updated.getDiachincc(), row, 6);
            ThongBaodialog.showsuccessDialog(nccview,"Xác Nhận", "Sửa thành công!");
        } else {
            ThongBaodialog.showErrorDialog(nccview,"Lỗi", "Sửa thất bại!");
        }
        break; // Thoát vòng lặp khi sửa xong
    }
}

    private void xoabtnclicked() {
    DefaultTableModel model = nccview.getTableModel();
    int rowCount = model.getRowCount();

    // Tìm danh sách các dòng được tick
    java.util.List<Integer> rowsToDelete = new java.util.ArrayList<>();
    for (int i = 0; i < rowCount; i++) {
        Boolean checked = (Boolean) model.getValueAt(i, 0);
        if (checked != null && checked) {
            rowsToDelete.add(i);
        }
    }

    if (!rowsToDelete.isEmpty()) {
        // Gọi phương thức xác nhận xóa
        boolean confirmed = ThongBaodialog.showConfirmDialog(nccview, "Xác Nhận Xoá", "Bạn có chắc chắn muốn xoá " + rowsToDelete.size() + " dòng đã chọn?");
        if (confirmed) {
            // Xóa từ dưới lên trên để tránh lỗi index
            for (int i = rowsToDelete.size() - 1; i >= 0; i--) {
                int row = rowsToDelete.get(i);
                String id = model.getValueAt(row, 1).toString();
                if (ncc_mgm.xoanhacungcap(id)) {
                    model.removeRow(row);
                } else {
                    ThongBaodialog.showErrorDialog(nccview, "Lỗi", "Xoá thất bại dòng ID: " + id);
                }
            }
            ThongBaodialog.showsuccessDialog(nccview, "Xác Nhận", "Xoá thành công!");
        }
    } else {
        // Nếu không có dòng tick, kiểm tra dòng đang chọn
        int selectedRow = nccview.getTable().getSelectedRow();
        if (selectedRow != -1) {
            boolean confirmed = ThongBaodialog.showConfirmDialog(nccview, "Xác Nhận Xoá", "Bạn có chắc chắn muốn xoá dòng đã chọn?");
            if (confirmed) {
                String id = model.getValueAt(selectedRow, 1).toString();
                if (ncc_mgm.xoanhacungcap(id)) {
                    model.removeRow(selectedRow);
                    ThongBaodialog.showsuccessDialog(nccview, "Xác Nhận", "Xoá thành công!");
                } else {
                    ThongBaodialog.showErrorDialog(nccview, "Lỗi", "Xoá thất bại!");
                }
            }
        } else {
            ThongBaodialog.showWarningDialog(nccview, "Thông Báo", "Vui lòng tick hoặc chọn dòng cần xoá!");
        }
    }
}

    
    private void timkiem(){
         String keyword = nccview.getSearchText().toLowerCase();
        DefaultTableModel model = nccview.getTableModel();
        model.setRowCount(0);

        for (NhaCungCap ncc : ncc_mgm.getallNCC()) {
            if (ncc.getNccid().toLowerCase().contains(keyword)
                    || ncc.getNccname().toLowerCase().contains(keyword)
                    || ncc.getNcclh().toLowerCase().contains(keyword)
                    || ncc.getEmail().toLowerCase().contains(keyword)
                    || ncc.getPhone().toLowerCase().contains(keyword)
                    || ncc.getDiachincc().toLowerCase().contains(keyword)) {

                model.addRow(new Object[]{
                    false,
                    ncc.getNccid(),
                    ncc.getNccname(),
                    ncc.getNcclh(),
                    ncc.getEmail(),
                    ncc.getPhone(),
                    ncc.getDiachincc()
                });
            }
        }
    }
    private void ImportToExcel(){
         JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Chọn file Excel để nhập dữ liệu");

    int userSelection = fileChooser.showOpenDialog(null);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToOpen = fileChooser.getSelectedFile();
        try (FileInputStream fis = new FileInputStream(fileToOpen);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            DefaultTableModel model = nccview.getTableModel();
            model.setRowCount(0); // Clear existing data

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ dòng tiêu đề
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String id = getCellValue(row.getCell(0));
                String name = getCellValue(row.getCell(1));
                String daiDien = getCellValue(row.getCell(2));
                String email = getCellValue(row.getCell(3));
                String sdt = getCellValue(row.getCell(4));
                String diaChi = getCellValue(row.getCell(5));

                model.addRow(new Object[]{false, id, name, daiDien, email, sdt, diaChi});
            }

            JOptionPane.showMessageDialog(null, "Nhập Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi đọc file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
private String getCellValue(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf((long) cell.getNumericCellValue());
            }
        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
            return cell.getCellFormula();
        case BLANK:
        default:
            return "";
    }
    }
    private void exportToExcel() {
        List<NhaCungCap> list = ncc_mgm.getAll();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("NCC");
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Tên NCC", "Người Đại Diện", "Email", "SĐT", "Địa chỉ"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                NhaCungCap ncc = list.get(i);
                row.createCell(0).setCellValue(ncc.getNccid());
                row.createCell(1).setCellValue(ncc.getNccname());
                row.createCell(2).setCellValue(ncc.getNcclh());
                row.createCell(3).setCellValue(ncc.getEmail());
                row.createCell(4).setCellValue(ncc.getPhone());
                row.createCell(5).setCellValue(ncc.getDiachincc());
            }

            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("nhacungcap.xlsx"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try (FileOutputStream fos = new FileOutputStream(fc.getSelectedFile())) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(null, "Xuất Excel thành công");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void exportToPdf() {
    List<NhaCungCap> list = ncc_mgm.getAll();
    try {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("nhacungcap.pdf"));
        if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fc.getSelectedFile()));
        document.open();

        // Load font Unicode (Arial)
      BaseFont bf = BaseFont.createFont(NhaCungCapController.class.getResource("/fonts/arial.ttf").toString(),
    BaseFont.IDENTITY_H,
    BaseFont.EMBEDDED
);


        Font font = new Font(bf, 12);  // Font dùng để hiển thị tiếng Việt

        document.add(new Paragraph("DANH SÁCH NHÀ CUNG CẤP", new Font(bf, 14, Font.BOLD)));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        String[] headers = {"ID", "Tên NCC", "Người Đại Diện", "Email", "SĐT", "Địa chỉ"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, font));
            table.addCell(cell);
        }

        for (NhaCungCap ncc : list) {
            table.addCell(new Phrase(ncc.getNccid(), font));
            table.addCell(new Phrase(ncc.getNccname(), font));
            table.addCell(new Phrase(ncc.getNcclh(), font));
            table.addCell(new Phrase(ncc.getEmail(), font));
            table.addCell(new Phrase(ncc.getPhone(), font));
            table.addCell(new Phrase(ncc.getDiachincc(), font));
        }

        document.add(table);
        document.close();
        JOptionPane.showMessageDialog(null, "Xuất PDF thành công");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi xuất PDF");
    }
}

}

