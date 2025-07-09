package Controller;

import DAO.SanPhamDAO;
import Model.SanPham;
import View.GiaoDienBanHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// Import your HoaDon and ChiTietHoaDon model classes
import Model.HoaDon;
import Model.ChiTietHoaDon;

// Import your HoaDonDAO and ChiTietHoaDonDAO classes (or create them)
import DAO.HoaDonDAO;
import DAO.ChiTietHoaDonDAO;
import model.NhanVien;
import Controller.HoaDonController;
import DAO.TaiChinhDAO;
import DAO.TonKhoDAO;
import Model.TonKho;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Rectangle;

import javax.swing.*;
import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.filechooser.FileSystemView;

public class GiaoDienBanHangController {

    private double tinhThanhTien(double donGia, int soLuong, double giamGiaPhanTram) {
        double giamGia = donGia * soLuong * (giamGiaPhanTram / 100.0);
        return donGia * soLuong - giamGia;
    }

    private final GiaoDienBanHang view;
    private final SanPhamDAO sanPhamDAO;
    private JTable tableGioHang;
    private final Connection conn; // Store the connection
    private NhanVien nhanvien;
    private HoaDonController hoaDonController;
    private TonKhoDAO tkDAO;

    public GiaoDienBanHangController(GiaoDienBanHang view, Connection conn, NhanVien nhanvien, HoaDonController hoaDonController) {
        this.view = view;
        this.sanPhamDAO = new SanPhamDAO(conn);
        this.tkDAO = new TonKhoDAO(conn);
        this.conn = conn; // Store the connection
        this.hoaDonController = hoaDonController;
        this.nhanvien = nhanvien;
        initView();
        addEvents();
    }

    private void initView() {
        themHoaDonMoi(null);
        taoTabThemHoaDon();
        view.setVisible(true);
    }

    private void addEvents() {
        view.txtTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                tinhTienVaTienThua();
            }

            public void removeUpdate(DocumentEvent e) {
                tinhTienVaTienThua();
            }

            public void changedUpdate(DocumentEvent e) {
                tinhTienVaTienThua();
            }
        });

        JPopupMenu suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);
        JList<SanPham> suggestionList = new JList<>();
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SanPham sp) {
                    setText(sp.getTenSanPham() + " - Giá: " + sp.getGiaBan());
                }
                return comp;
            }
        });
        suggestionPopup.add(new JScrollPane(suggestionList));

        view.txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            private void showSuggestions() {
                String keyword = view.txtTimKiem.getText().trim();
                if (keyword.isEmpty() || keyword.equals("Tìm theo mã vạch hoặc tên sản phẩm")) {
                    suggestionPopup.setVisible(false);
                    return;
                }

                java.util.List<SanPham> suggestions = sanPhamDAO.timKiem(keyword);
                if (suggestions.isEmpty()) {
                    suggestionPopup.setVisible(false);
                    return;
                }

                suggestionList.setListData(suggestions.toArray(new SanPham[0]));

                suggestionList.setSelectedIndex(0);

                Point location;
                try {
                    location = view.txtTimKiem.getLocationOnScreen();
                    suggestionPopup.setPopupSize(view.txtTimKiem.getWidth(), 150);
                    suggestionPopup.show(view.txtTimKiem, 0, view.txtTimKiem.getHeight());
                } catch (IllegalComponentStateException ex) {
                    // Ignored (in case component not yet shown)
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showSuggestions();
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SanPham selected = suggestionList.getSelectedValue();
                if (selected != null) {
                    themSanPhamVaoGio(selected);
                    view.txtTimKiem.setText(""); // ✅ Xóa nội dung tìm kiếm
                    suggestionPopup.setVisible(false);
                }
            }
        });
        view.getTxtTienKhachDua().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhTienVaTienThua();
            }
        });

        // Add ActionListener to the "Thanh toán" button
        view.btnThanhToan.addActionListener(e -> {
            xuatHoaDonPDF();
            luuHoaDonVaChiTiet();
        });
        view.btnIn.addActionListener(e -> {
            xuatHoaDonPDF();
        });

    }

    private void xuatHoaDonPDF() {
        try {
            // Lấy Desktop
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            File folder = new File(desktopDir, "HoaDonPDF");

            // Tạo thư mục nếu chưa tồn tại
            if (!folder.exists() && !folder.mkdirs()) {
                throw new IOException("Không thể tạo thư mục lưu hóa đơn: " + folder.getAbsolutePath());
            }

            // Tên file theo thời gian
            String fileName = "HoaDon_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String filePath = new File(folder, fileName).getAbsolutePath();

            // Khởi tạo document
            com.itextpdf.text.Rectangle pageSize = new com.itextpdf.text.Rectangle(226.77f, 1000f);
            Document document = new Document(pageSize, 10, 10, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Load font Arial
            InputStream fontStream = getClass().getResourceAsStream("/fonts/arial.ttf");
            if (fontStream == null) {
                throw new FileNotFoundException("Không tìm thấy font arial.ttf trong resources/fonts");
            }
            byte[] fontBytes = fontStream.readAllBytes();
            BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null);
            Font fontTitle = new Font(baseFont, 14, Font.BOLD);
            Font fontNormal = new Font(baseFont, 9, Font.NORMAL);
            Font fontBold = new Font(baseFont, 10, Font.BOLD);

            // Logo
            InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
            if (logoStream != null) {
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = Image.getInstance(logoBytes);
                logo.scaleToFit(60, 60);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            }

            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Ngày & Nhân viên
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            document.add(new Paragraph("Ngày: " + sdf.format(new Date()), fontNormal));
            document.add(new Paragraph("Nhân viên: " + (nhanvien != null ? nhanvien.getHoten() : "N/A"), fontNormal));
            document.add(new Paragraph(" "));

            // Bảng chi tiết hóa đơn
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.5f, 4f, 1.5f, 2.5f, 2.5f, 3f});

            String[] headers = {"Mã SP", "Tên SP", "SL", "Đơn giá", "Giảm Giá", "Thành tiền"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontNormal));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3);
                table.addCell(cell);
            }

            for (int i = 0; i < tableGioHang.getRowCount(); i++) {
                for (int j = 0; j < tableGioHang.getColumnCount(); j++) {
                    Object val = tableGioHang.getValueAt(i, j);
                    PdfPCell cell = new PdfPCell(new Phrase(val != null ? val.toString() : "", fontNormal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(3);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Bảng tổng kết căn phải
            PdfPTable summaryTable = new PdfPTable(1);
            summaryTable.setWidthPercentage(100);

            String tongTien = view.getLblTongTien().getText().replace("Tổng Tiền:", "").trim();
            PdfPCell cellTongTien = new PdfPCell(new Phrase("Tổng tiền hàng: " + tongTien, fontNormal));
            cellTongTien.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellTongTien.setBorder(PdfPCell.NO_BORDER);
            summaryTable.addCell(cellTongTien);

            String tienKhach = view.getTxtTienKhachDua().getText();
            if (tienKhach != null && !tienKhach.trim().isEmpty()) {
                PdfPCell cellTienKhach = new PdfPCell(new Phrase("Tiền khách đưa: " + formatCurrency(tienKhach), fontNormal));
                cellTienKhach.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellTienKhach.setBorder(PdfPCell.NO_BORDER);
                summaryTable.addCell(cellTienKhach);
            }

            String tienThua = view.getLblTienThua().getText().replace("Tiền Thừa Trả Khách:", "").trim();
            PdfPCell cellTienThua = new PdfPCell(new Phrase("Tiền thừa trả khách: " + tienThua, fontNormal));
            cellTienThua.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellTienThua.setBorder(PdfPCell.NO_BORDER);
            summaryTable.addCell(cellTienThua);

            String ghiChu = view.txtGhiChu.getText();
            if (ghiChu != null && !ghiChu.trim().isEmpty()) {
                PdfPCell cellGhiChu = new PdfPCell(new Phrase("Ghi chú: " + ghiChu, fontNormal));
                cellGhiChu.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellGhiChu.setBorder(PdfPCell.NO_BORDER);
                summaryTable.addCell(cellGhiChu);
            }

            document.add(summaryTable);

            // QR Code
            InputStream qrStream = getClass().getResourceAsStream("/imageBanHang/qrbanhang.jpg");
            if (qrStream != null) {
                byte[] qrBytes = qrStream.readAllBytes();
                Image qrImage = Image.getInstance(qrBytes);
                qrImage.scaleAbsolute(80f, 80f);
                qrImage.setAlignment(Image.ALIGN_CENTER);
                document.add(new Paragraph(" "));
                document.add(qrImage);
            }

            // Dòng cảm ơn
            document.add(new Paragraph(" "));
            Paragraph thanks = new Paragraph("CẢM ƠN QUÝ KHÁCH!", fontBold);
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            // Đóng document
            document.close();

            // Mở file PDF
            File pdfFile = new File(filePath);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }

            JOptionPane.showMessageDialog(null, "Xuất hóa đơn thành công!\n" + pdfFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất hóa đơn: " + ex.getMessage());
        }
    }

    private String formatCurrency(String value) {
        try {
            BigDecimal amount = new BigDecimal(value.replaceAll("[^\\d.]", ""));
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return formatter.format(amount);
        } catch (Exception e) {
            return value;
        }
    }

    private void themSanPhamVaoGio(SanPham sp) {
        Component comp = view.tabbedPane.getSelectedComponent();
        if (!(comp instanceof JPanel panel)) {
            return;
        }

        JSplitPane splitPane = (JSplitPane) panel.getComponent(0);
        JScrollPane scrollGioHang = (JScrollPane) splitPane.getTopComponent();
        tableGioHang = (JTable) scrollGioHang.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) tableGioHang.getModel();

        boolean daTonTai = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(sp.getMaSanPham())) {
                int soLuongHienTai = (int) model.getValueAt(i, 2);
                model.setValueAt(soLuongHienTai + 1, i, 2);
                daTonTai = true;
                capNhatThanhTien(model, i);
                break;
            }
        }

        if (!daTonTai) {
            model.addRow(new Object[]{
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    1,
                    sp.getGiaBan(),
                    0.0, // Giảm giá mặc định là 0%
                    tinhThanhTien(sp.getGiaBan(), 1, 0.0)
            });
// Cập nhật tổng tiền sau khi thêm sản phẩm
            tinhTienVaTienThua();
        }
    }

    class CustomQuantityEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel;
        private final JButton btnPlus;
        private final JButton btnMinus;
        private final JTextField txtValue;
        private int value;
        private JTable table;
        private DefaultTableModel model;
        private int row;

        public CustomQuantityEditor(JTable table, DefaultTableModel model) {
            this.table = table;
            this.model = model;

            panel = new JPanel(new BorderLayout(5, 0));
            btnPlus = new JButton("+");
            btnMinus = new JButton("–");
            txtValue = new JTextField("1", 3);
            txtValue.setHorizontalAlignment(SwingConstants.CENTER);

            btnPlus.setFocusable(false);
            btnMinus.setFocusable(false);

            panel.add(btnMinus, BorderLayout.WEST);
            panel.add(txtValue, BorderLayout.CENTER);
            panel.add(btnPlus, BorderLayout.EAST);

            btnPlus.addActionListener(e -> {
                try {
                    int val = Integer.parseInt(txtValue.getText().trim());
                    val++;
                    txtValue.setText(String.valueOf(val));
                    updateThanhTien();
                } catch (NumberFormatException ex) {
                    txtValue.setText("1");
                }
            });

            btnMinus.addActionListener(e -> {
                try {
                    int val = Integer.parseInt(txtValue.getText().trim());
                    if (val > 1) {
                        val--;
                        txtValue.setText(String.valueOf(val));
                        updateThanhTien();
                    }
                } catch (NumberFormatException ex) {
                    txtValue.setText("1");
                }
            });

            txtValue.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    stopCellEditing(); // Apply value when user leaves the cell
                }
            });
            txtValue.addActionListener(e -> {
                updateThanhTien();
                stopCellEditing();
            });
        }

        private void updateThanhTien() {
            try {
                int soLuong = Integer.parseInt(txtValue.getText().trim());
                if (soLuong < 1) {
                    soLuong = 1;
                    txtValue.setText("1");
                }
                BigDecimal donGia = (BigDecimal) model.getValueAt(row, 3);
                Double giamGia = (Double) model.getValueAt(row, 4);

                BigDecimal thanhTien = tinhThanhTien(donGia, soLuong, giamGia);
                model.setValueAt(soLuong, row, 2);
                model.setValueAt(thanhTien, row, 5);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Số lượng không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtValue.setText("1");
            }
        }

        @Override
        public Object getCellEditorValue() {
            try {
                value = Integer.parseInt(txtValue.getText().trim());
                if (value < 1) {
                    value = 1;
                }
            } catch (NumberFormatException ex) {
                value = 1;
            }
            return value;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.value = (Integer) value;
            this.row = row;
            txtValue.setText(String.valueOf(this.value));
            return panel;
        }
    }

    class CustomQuantityRenderer implements TableCellRenderer {

        private final JPanel panel;
        private final JButton btnPlus;
        private final JButton btnMinus;
        private final JLabel lblValue;

        public CustomQuantityRenderer() {
            panel = new JPanel(new BorderLayout(5, 0));
            btnPlus = new JButton("+");
            btnMinus = new JButton("–");
            lblValue = new JLabel("", SwingConstants.CENTER);

            btnPlus.setFocusable(false);
            btnPlus.setEnabled(false); // Disable in render mode
            btnMinus.setFocusable(false);
            btnMinus.setEnabled(false);

            panel.add(btnMinus, BorderLayout.WEST);
            panel.add(lblValue, BorderLayout.CENTER);
            panel.add(btnPlus, BorderLayout.EAST);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof Integer) {
                lblValue.setText(value.toString());
            } else {
                lblValue.setText("1");
            }

            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }

            return panel;
        }
    }

    class DiscountEditor extends AbstractCellEditor implements TableCellEditor {

        private final JTextField textField;
        private int row;
        private DefaultTableModel model;
        private JTable table;

        public DiscountEditor(JTable table, DefaultTableModel model) {
            this.table = table;
            this.model = model;
            textField = new JTextField();
            textField.setHorizontalAlignment(SwingConstants.CENTER);

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    updateThanhTien();
                    stopCellEditing();
                }
            });
            textField.addActionListener(e -> {
                updateThanhTien();
                stopCellEditing();
            });
        }

        private void updateThanhTien() {
            try {
                Double giamGia = Double.parseDouble(textField.getText());
                BigDecimal donGia = (BigDecimal) model.getValueAt(row, 3);
                int soLuong = (int) model.getValueAt(row, 2);

                BigDecimal thanhTien = tinhThanhTien(donGia, soLuong, giamGia);
                model.setValueAt(giamGia, row, 4);
                model.setValueAt(thanhTien, row, 5);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(textField, "Giảm giá không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                textField.setText("0.0");
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            textField.setText(String.valueOf(value));
            return textField;
        }

        @Override
        public Object getCellEditorValue() {
            try {
                return Double.parseDouble(textField.getText());
            } catch (NumberFormatException e) {
                return 0.0; // Default discount
            }
        }
    }

    private void themHoaDonMoi(ActionEvent e) {
        JPanel panelHoaDon = new JPanel(new BorderLayout());

        String[] colNames = {"Mã SP", "Tên SP", "SL", "Đơn giá", "Giảm Giá(%)", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 4; // Chỉ cho phép sửa cột Số lượng và Giảm giá
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) {
                    return Integer.class; // Số lượng
                }
                if (column == 3) {
                    return BigDecimal.class; // Đơn giá
                }
                if (column == 4) {
                    return Double.class; // Giảm giá
                }
                if (column == 5) {
                    return BigDecimal.class; // Thành tiền
                }
                return Object.class;
            }
        };
        tableGioHang = new JTable(model);

        ((DefaultTableModel) tableGioHang.getModel()).addTableModelListener(ea -> {
            int row = ea.getFirstRow();
            int col = ea.getColumn();

            if (col == 2 || col == 4) { // Cột số lượng hoặc giảm giá
                try {
                    int soLuong = Integer.parseInt(tableGioHang.getValueAt(row, 2).toString());
                    double donGia = Double.parseDouble(tableGioHang.getValueAt(row, 3).toString());
                    double giam = Double.parseDouble(tableGioHang.getValueAt(row, 4).toString());

                    double thanhTien = tinhThanhTien(donGia, soLuong, giam);
                    tableGioHang.setValueAt(thanhTien, row, 5);

                    tinhTienVaTienThua();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        tableGioHang.setRowSelectionAllowed(false);
        tableGioHang.setColumnSelectionAllowed(false);

        // Gán JSpinner cho cột Số lượng
        tableGioHang.getColumnModel().getColumn(2).setCellEditor(new CustomQuantityEditor(tableGioHang, model));
        tableGioHang.getColumnModel().getColumn(2).setCellRenderer(new CustomQuantityRenderer());
        // Gán editor cho cột Giảm giá
        tableGioHang.getColumnModel().getColumn(4).setCellEditor(new DiscountEditor(tableGioHang, model));

        // Cập nhật Thành tiền khi thay đổi số lượng hoặc giảm giá
        model.addTableModelListener(e1 -> {
            int row = e1.getFirstRow();
            int col = e1.getColumn();
            if (col == 2 || col == 4) {
                capNhatThanhTien(model, row);
            }
        });

        // Menu chuột phải để xóa
        tableGioHang.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tableGioHang.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < tableGioHang.getRowCount()) {
                        tableGioHang.setRowSelectionInterval(row, row);
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem deleteItem = new JMenuItem("Xóa sản phẩm này");
                        deleteItem.addActionListener(ev -> model.removeRow(row));
                        tinhTienVaTienThua();
                        popup.add(deleteItem);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane scrollGioHang = new JScrollPane(tableGioHang);

        JPanel panelSanPham = new JPanel(new GridLayout(0, 5, 10, 10));
        java.util.List<SanPham> danhSachSP = sanPhamDAO.getAll();

        for (SanPham sp : danhSachSP) {
            JButton btn = new JButton(sp.getTenSanPham());
            btn.setToolTipText("Giá: " + sp.getGiaBan());
            btn.addActionListener(ev -> {
                // Kiểm tra xem đã có SP trong giỏ chưa, nếu có thì tăng số lượng
                boolean daTonTai = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(sp.getMaSanPham())) {
                        int soLuongHienTai = (int) model.getValueAt(i, 2);
                        model.setValueAt(soLuongHienTai + 1, i, 2);
                        daTonTai = true;
                        capNhatThanhTien(model, i);
                        break;
                    }
                }
                if (!daTonTai) {
                    model.addRow(new Object[]{
                            sp.getMaSanPham(),
                            sp.getTenSanPham(),
                            1,
                            sp.getGiaBan(),
                            0.0, // Giảm giá mặc định là 0%
                            tinhThanhTien(sp.getGiaBan(), 1, 0.0)
                    });
                }
            });
            panelSanPham.add(btn);
        }

        JScrollPane scrollSanPham = new JScrollPane(panelSanPham);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollGioHang, scrollSanPham);
        splitPane.setResizeWeight(0.5);
        panelHoaDon.add(splitPane, BorderLayout.CENTER);

        int soTab = view.tabbedPane.getTabCount();
        if (soTab > 0 && view.tabbedPane.getTitleAt(soTab - 1).equals("+")) {
            soTab--;
        }

        String tenHoaDon = "Hóa đơn " + (soTab + 1);
        themTabVoiNutX(tenHoaDon, panelHoaDon);

        if (view.lblTongTien != null) {
            view.lblTongTien.setText("0 đ");
        }
        if (view.lblTienThua != null) {
            view.lblTienThua.setText("");
        }
        if (view.txtTienKhachDua != null) {
            view.txtTienKhachDua.setText("");
        }

        view.tabbedPane.setSelectedIndex(view.tabbedPane.getTabCount() - 2);
    }

    private void capNhatThanhTien(DefaultTableModel model, int row) {
        try {
            int soLuong = (int) model.getValueAt(row, 2);
            BigDecimal donGia = (BigDecimal) model.getValueAt(row, 3);
            Double giamGia = (Double) model.getValueAt(row, 4);

            BigDecimal thanhTien = tinhThanhTien(donGia, soLuong, giamGia);
            model.setValueAt(thanhTien, row, 5);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật thành tiền", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal tinhThanhTien(BigDecimal donGia, int soLuong, double giamGia) {
        BigDecimal soLuongBigDecimal = BigDecimal.valueOf(soLuong);
        BigDecimal discountPercentage = BigDecimal.valueOf(giamGia / 100.0);
        BigDecimal discountAmount = donGia.multiply(discountPercentage);
        BigDecimal discountedPrice = donGia.subtract(discountAmount);
        return discountedPrice.multiply(soLuongBigDecimal);
    }

    private void themTabVoiNutX(String title, Component content) {
        int indexTabPlus = view.tabbedPane.getTabCount();
        if (indexTabPlus > 0 && view.tabbedPane.getTitleAt(indexTabPlus - 1).equals("+")) {
            indexTabPlus--;
        }

        view.tabbedPane.insertTab(title, null, content, null, indexTabPlus);

        JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title + "  ");
        JButton btnClose = new JButton("x");
        btnClose.setMargin(new Insets(0, 2, 0, 2));
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        btnClose.setContentAreaFilled(false);

        btnClose.addActionListener(e -> {
            int i = view.tabbedPane.indexOfTabComponent(pnlTab);
            if (i != -1) {
                view.tabbedPane.remove(i);
                capNhatLaiTieuDeHoaDon();
            }
        });

        pnlTab.add(lblTitle);
        pnlTab.add(btnClose);
        view.tabbedPane.setTabComponentAt(indexTabPlus, pnlTab);
    }

    private void capNhatLaiTieuDeHoaDon() {
        for (int i = 0; i < view.tabbedPane.getTabCount(); i++) {
            Component tabComponent = view.tabbedPane.getTabComponentAt(i);
            if (tabComponent instanceof JPanel) {
                JPanel pnl = (JPanel) tabComponent;
                for (Component comp : pnl.getComponents()) {
                    if (comp instanceof JLabel) {
                        ((JLabel) comp).setText("Hóa đơn " + (i + 1) + "  ");
                    }
                }
            }
        }
    }

    private void taoTabThemHoaDon() {
        JPanel panelThem = new JPanel();
        view.tabbedPane.addTab("+", panelThem);
        view.tabbedPane.addChangeListener(e -> {
            int index = view.tabbedPane.getSelectedIndex();
            if (index == view.tabbedPane.getTabCount() - 1) {
                view.tabbedPane.removeChangeListener(view.tabbedPane.getChangeListeners()[0]);
                view.tabbedPane.removeTabAt(index);
                themHoaDonMoi(null);
                taoTabThemHoaDon();
            }
        });
    }

    private void tinhTienVaTienThua() {
        double tongTien = 0;

        // 1. Tính tổng tiền từ cột "Thành tiền" trong bảng giỏ hàng
        DefaultTableModel model = (DefaultTableModel) tableGioHang.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 5); // Cột "Thành tiền" (index = 5)
            if (val != null) {
                try {
                    tongTien += Double.parseDouble(val.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // hoặc log
                }
            }
        }
        view.getLblTongTien().setText("Tổng Tiền: " + String.format("%,.0f đ", tongTien));
        // 2. Lấy tiền khách đưa
        String tienKhachStr = view.getTxtTienKhachDua().getText().trim();
        double tienKhachDua = 0;
        try {
            tienKhachDua = Double.parseDouble(tienKhachStr);
        } catch (NumberFormatException e) {
            // Hiển thị lỗi nếu cần
            view.getLblTienThua().setText("Nhập sai định dạng tiền");
            return;
        }

        // 3. Tính tiền thừa
        double tienThua = tienKhachDua - tongTien;

        // 4. Hiển thị kết quả
        if (tienThua < 0) {
            view.getLblTienThua().setText("Tiền Thừa Trả Khách: " + String.format("%,.0f đ", -tienThua));
        } else if (tienThua == 0) {
            view.getLblTienThua().setText("Tiền Thừa Trả Khách: " + String.format("%,.0f đ", tienThua));
        } else {
            view.getLblTienThua().setText("Tiền Thừa Trả Khách: " + String.format("%,.0f đ", +tienThua));
        }

    }

    private void luuHoaDonVaChiTiet() {
        DefaultTableModel model = (DefaultTableModel) tableGioHang.getModel();
        int rowCount = model.getRowCount();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(view, "Không có sản phẩm nào trong giỏ hàng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tạo hóa đơn
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setMaNV(nhanvien.getma_nv());
        hoaDon.setHinhThucThanhToan("Tiền mặt");

        double tongTien = 0;
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            int maSP = (Integer) model.getValueAt(i, 0);
            int soLuong = (Integer) model.getValueAt(i, 2);
            BigDecimal donGia = (BigDecimal) model.getValueAt(i, 3);
            double giamGia = (Double) model.getValueAt(i, 4);

            // Lấy danh sách lô phù hợp theo FIFO
            List<TonKho> lichTru = tkDAO.getDanhSachLoTruTon_TonKho(maSP, soLuong);
            int tongCo = lichTru.stream().mapToInt(TonKho::getSoLuongTon).sum();
            if (tongCo < soLuong) {
                JOptionPane.showMessageDialog(view, "Không đủ tồn kho cho SP: " + maSP, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (TonKho tk : lichTru) {
                if (!tkDAO.giamTonTheoTonKho(tk)) {
                    JOptionPane.showMessageDialog(view, "Lỗi trừ tồn kho cho SP: " + maSP + ", Lô: " + tk.getMaLo(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                chiTiet.setMaSanPham(maSP);
                chiTiet.setMaLo(tk.getMaLo());
                chiTiet.setSoLuong(tk.getSoLuongTon());
                chiTiet.setDonGia(donGia);
                chiTiet.setGiamGia(BigDecimal.valueOf(giamGia));
                chiTietHoaDons.add(chiTiet);

                double thanhTien = tk.getSoLuongTon() * donGia.doubleValue() * (1 - giamGia / 100);
                tongTien += thanhTien;
            }
        }

        hoaDon.setTongTien(BigDecimal.valueOf(tongTien));

        HoaDonDAO hoaDonDAO = new HoaDonDAO(conn);
        ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO(conn);

        try {
            int maHoaDon = hoaDonDAO.save(hoaDon);

            for (ChiTietHoaDon chiTiet : chiTietHoaDons) {
                chiTiet.setMaHoaDon(maHoaDon);
                chiTietHoaDonDAO.save(chiTiet);
            }

            JOptionPane.showMessageDialog(view, "Lưu hóa đơn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // 🌟 Gọi hàm thêm khoản thu
            boolean thuInserted = new TaiChinhDAO().insertThuTuTatCaHoaDonVaDonHang();
            if (thuInserted) {
                System.out.println("✅ Thêm khoản thu thành công!");
            } else {
                System.out.println("⚠️ Không có khoản thu nào được thêm.");
            }
            model.setRowCount(0);
            tinhTienVaTienThua();

            if (hoaDonController != null) {
                hoaDonController.loadTableData();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
