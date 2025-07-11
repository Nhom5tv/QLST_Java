/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Bridge.DBConnection;
import DAO.*;
import Model.ChiTietHoaDonOrder;
import Model.HoaDonOrder;
import Model.GioHang;
import Model.DiaChi;
import View.DiaChiView;
import View.GiaoDienSieuThi;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

import View.HoaDonOrderView;

import java.io.FileOutputStream;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.ArrayList;

import Controller.DiaChiController;
import Model.KhachHang;
import Model.TonKho;
import View.GioHangView;
import View.HoaDonChiTietDialog;
import View.HoaDonDatHangView;
import View.HoaDonTrangThaiDialog;
import View.TrangChuView;
import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.pdf.BaseFont;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * @author Admin
 */
public class HoaDonOrderController {

    private HoaDonOrderDAO hoaDonDAO;
    private ChiTietHoaDonOrderDAO cthdDAO;
    private HoaDonOrderView view;
    private HoaDonDatHangView hdview;
    private int MaKH;
    private String MaKM;
    private int discountValue;
    private TrangChuView stmv;
    private TonKhoController tkController;
    private TonKhoDAO tkDAO;
    private TaiChinhController taiChinhController;

    //Khach hang
    public HoaDonOrderController(HoaDonOrderDAO hoaDonDAO, ChiTietHoaDonOrderDAO cthdDAO, HoaDonOrderView view, int MaKH) {
        this.hoaDonDAO = new HoaDonOrderDAO();
        this.cthdDAO = new ChiTietHoaDonOrderDAO();
        this.view = view;
        this.MaKH = MaKH;
        this.MaKM = MaKM;
        this.taiChinhController = taiChinhController;
        loadDefaultAddress();
        try {
            this.tkDAO = new TonKhoDAO(DBConnection.getConnection());
            this.tkController = tkController;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        view.getOrderBtn().addActionListener(e -> {
            if (view.getDiaChi() == null) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn địa chỉ nhận hàng.");
                return;
            }

            List<GioHang> selectedItems = view.getSelectedItems();

            for (GioHang item : selectedItems) {
                int maSP = item.getMaSP();
                int soLuong = item.getSoLuong();

                int khaDung = tkDAO.getTongSoLuongKhaDung(maSP);
                if (soLuong > khaDung) {
                    JOptionPane.showMessageDialog(view, "Sản phẩm \"" + item.getTenSp() + "\" chỉ còn " + khaDung + " sản phẩm khả dụng để đặt.");
                    return;
                }
            }

            HoaDonOrder hoaDonOrder = new HoaDonOrder();
            handleOrderButtonClick(selectedItems, hoaDonOrder);
        });

    }

    //admin
    public HoaDonOrderController(TrangChuView stmv, HoaDonDatHangView hdview, TonKhoController tkController) {
        this.hoaDonDAO = new HoaDonOrderDAO();
        this.cthdDAO = new ChiTietHoaDonOrderDAO();
        this.hdview = hdview;
        this.stmv = stmv;
        try {
            this.tkDAO = new TonKhoDAO(DBConnection.getConnection());
            this.tkController = tkController;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadTableData();
        hdview.addUpdateStatusListener(e -> handleUpdateStatus());
        // Thêm phần này trong constructor hoặc nơi bạn cần xử lý tìm kiếm
        hdview.addSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
        hdview.addRowDoubleClickListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable table = hdview.getTable();
                int row = table.rowAtPoint(evt.getPoint());

                if (evt.getClickCount() == 2 && row != -1) {
                    int modelRow = table.convertRowIndexToModel(row); // tránh lỗi khi sort
                    int maHoaDon = (int) hdview.getTableModel().getValueAt(modelRow, 0);

                    HoaDonChiTietDialog dialog = new HoaDonChiTietDialog(null, maHoaDon);
                    dialog.setVisible(true);
                }
            }
        });

    }

    private void loadTableData() {
        List<HoaDonOrder> list = hoaDonDAO.getAll();
        DefaultTableModel model = hdview.getTableModel();
        model.setRowCount(0);

        for (HoaDonOrder hd : list) {
            model.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getMaKhachHang(),
                    hd.getNgayTao(),
                    hd.getTongTien(),
                    hd.getMakhuyenmai(),
                    hd.getTrangThai()
            });
        }
    }

    private void loadDefaultAddress() {
        int maKH = view.getDiaChi() != null ? view.getDiaChi().getCustomerId() : view.getSelectedItems().get(0).getMaKH();
        DiaChiDAO diaChiDAO = new DiaChiDAO();
        DiaChi diaChi = diaChiDAO.getDefaultDiaChi(maKH);
        if (diaChi != null) {
            view.setAddressInfo(diaChi);
        }
    }


    public boolean themHoaDon(List<ChiTietHoaDonOrder> chiTietHoaDons, HoaDonOrder hoaDon) {
        hoaDon.setMaKhachHang(MaKH);
        hoaDon.setTongTien(view.getTotalAmount());
        String maKM = view.getMaKhuyenMai();
        hoaDon.setMakhuyenmai(maKM);
        // Thêm hóa đơn vào bảng hoa_don
        int maHoaDon = hoaDonDAO.insert(hoaDon);
        if (maHoaDon == -1) {
            return false; // Nếu thêm hóa đơn không thành công, trả về false
        }
        hoaDon.setMaHoaDon(maHoaDon);
        hoaDon.setNgayTao(new Timestamp(System.currentTimeMillis()));
        // Cập nhật mã hóa đơn vào chi tiết hóa đơn
        for (ChiTietHoaDonOrder chiTiet : chiTietHoaDons) {
            chiTiet.setMaHoaDon(maHoaDon);
            boolean result = cthdDAO.insert(chiTiet);
            if (!result) {
                return false; // Nếu thêm chi tiết hóa đơn không thành công, trả về false
            }
        }
        return true;
    }

    public void generateInvoicePDF(HoaDonOrder hoaDonOrder, List<GioHang> selectedItems) {
        try {
            File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
            File folder = new File(desktopDir, "HoaDonPDF");

            // Tạo thư mục nếu chưa tồn tại
            if (!folder.exists() && !folder.mkdirs()) {
                throw new IOException("Không thể tạo thư mục lưu hóa đơn: " + folder.getAbsolutePath());
            }
            
            String fileName = "HoaDon_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String filePath = new File(folder, fileName).getAbsolutePath();
            // 1. Tạo đường dẫn lưu ra Desktop
          

            // 2. Nhúng font Arial hỗ trợ tiếng Việt
            String fontPath = "src/main/resources/fonts/arial.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(baseFont, 16, Font.BOLD);
            Font fontContent = new Font(baseFont, 12);

            // 3. Mở document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // 4. Lấy thông tin hóa đơn
            String ngayTaoString;
            if (hoaDonOrder.getNgayTao() != null) {
                ngayTaoString = hoaDonOrder.getNgayTao().toString();
            } else {
                ngayTaoString = "Không xác định";  // Hoặc có thể đưa ra thông báo rõ ràng hơn
            }

            DiaChi diaChi = hoaDonOrder.getDiaChi();

            // 5. Thêm thông tin chung
            document.add(new Paragraph("HÓA ĐƠN MUA HÀNG", fontTitle));
            document.add(new Paragraph("Mã hóa đơn: " + hoaDonOrder.getMaHoaDon(), fontContent));
//            if (diaChi != null) {
//                document.add(new Paragraph("Khách hàng: " + diaChi.getName(), fontContent));
//                document.add(new Paragraph("Địa chỉ: " + diaChi.getDetailAddress(), fontContent));
//                 document.add(new Paragraph("Sđt: " + diaChi.getPhoneNumber(), fontContent));
//            } else {
//                document.add(new Paragraph("Địa chỉ và tên khách hàng không có sẵn", fontContent));
//            }
       
            document.add(new Paragraph("Người nhận: " + hoaDonOrder.getTenNguoiNhan(), fontContent));
            document.add(new Paragraph("Sđt: " + hoaDonOrder.getSoDienThoai(), fontContent));
            document.add(new Paragraph("Địa chỉ: " + hoaDonOrder.getDiaChiChiTiet(), fontContent));
            document.add(new Paragraph("Ngày lập: " + ngayTaoString, fontContent));
            document.add(Chunk.NEWLINE);

            // 6. Bảng chi tiết sản phẩm
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(new Phrase("Sản phẩm", fontContent));
            table.addCell(new Phrase("Số lượng", fontContent));
            table.addCell(new Phrase("Đơn giá", fontContent));
            table.addCell(new Phrase("Thành tiền", fontContent));

            List<ChiTietHoaDonOrder> cthds = hoaDonOrder.getChiTietHoaDons();
            if (cthds != null) {
                for (ChiTietHoaDonOrder chiTiet : cthds) {
                    GioHang item = getItemFromCart(selectedItems, chiTiet);
                    if (item != null) {
                        String tenSanPham = item.getTenSp(); // Lấy tên sản phẩm từ GioHang
                        table.addCell(new Phrase(tenSanPham, fontContent));  // Dùng tên sản phẩm thay vì mã sản phẩm
                        table.addCell(new Phrase(String.valueOf(chiTiet.getSoLuong()), fontContent));
                        table.addCell(new Phrase(String.format("%,.0f VND", chiTiet.getDonGia()), fontContent));
                        table.addCell(new Phrase(String.format("%,.0f VND", chiTiet.getSoLuong() * chiTiet.getDonGia()), fontContent));
                    }
                }
            } else {
                document.add(new Paragraph("❌ Không có chi tiết sản phẩm để hiển thị.", fontContent));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // 7. Tổng tiền
            document.add(new Paragraph("Tổng tiền hàng: " + String.format("%,.0f VND", hoaDonOrder.getTongTien()), fontContent));

            // 8. Đóng file và mở
            document.close();
            JOptionPane.showMessageDialog(null, "✅ Hóa đơn đã được lưu trên Desktop!");

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(new File(filePath));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "❌ Lỗi khi mở file PDF: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Lỗi khi xuất PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Xử lý khi nhấn nút Đặt hàng
    public void handleOrderButtonClick(List<GioHang> selectedItems, HoaDonOrder hoaDonOrder) {

        GioHangDAO gioHangDAO = new GioHangDAO();
        List<ChiTietHoaDonOrder> chiTietHoaDons = new ArrayList<>();

        for (GioHang item : selectedItems) {
            int maSP = item.getMaSP();
            int soLuong = item.getSoLuong();

            List<TonKho> lichTru = tkDAO.getLoTruTon_TonKho(maSP, soLuong);
            int tongCo = lichTru.stream().mapToInt(TonKho::getSoLuongTon).sum();
            System.out.println("Tong co: "+tongCo);
            if (tongCo < soLuong) {
                JOptionPane.showMessageDialog(view, "Không đủ tồn kho khả dụng cho sản phẩm: " + item.getTenSp(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (TonKho tk : lichTru) {
                if (!tkDAO.capNhatSauDatHang(maSP, tk.getMaLo(), tk.getSoLuongTon())) {
                    JOptionPane.showMessageDialog(view, "Lỗi cập nhật tồn kho cho SP: " + maSP + ", Lô: " + tk.getMaLo(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ChiTietHoaDonOrder ct = new ChiTietHoaDonOrder();
                ct.setMaSanPham(maSP);
                ct.setMaLoHang(tk.getMaLo());
                ct.setSoLuong(tk.getSoLuongTon());
                ct.setDonGia(item.getGiaban());
                chiTietHoaDons.add(ct);
            }
        }
        // 
                    DiaChi diaChi = view.getDiaChi();
               if (diaChi != null) {
                   hoaDonOrder.setTenNguoiNhan(diaChi.getName()); 
                   hoaDonOrder.setSoDienThoai(diaChi.getPhoneNumber());
                   hoaDonOrder.setDiaChiChiTiet(diaChi.getDetailAddress()); 
               }


        boolean orderSuccess = themHoaDon(chiTietHoaDons, hoaDonOrder);
        if (orderSuccess) {
            JOptionPane.showMessageDialog(view, "Đặt hàng thành công!");

            Date ngayTao = hoaDonOrder.getNgayTao();
            Timestamp timestampNgayTao = (ngayTao != null) ? new Timestamp(ngayTao.getTime()) : null;
            hoaDonOrder.setNgayTao(timestampNgayTao);
            hoaDonOrder.setChiTietHoaDons(chiTietHoaDons);
            hoaDonOrder.setDiaChi(view.getDiaChi());

            generateInvoicePDF(hoaDonOrder, selectedItems);
            boolean thuInserted = new TaiChinhDAO().insertThuTuTatCaHoaDonVaDonHang();
            if (thuInserted) {
                System.out.println("✅ Thêm khoản thu thành công!");
                if (taiChinhController != null) {
                    taiChinhController.loadData(null);
                }
            } else {
                System.out.println("⚠️ Không có khoản thu nào được thêm.");
            }

            for (GioHang item : selectedItems) {
                item.setIsPaid(true);
                gioHangDAO.deleteItemById(item.getMaGH());
            }

            if (view.getParentView() != null) {
                view.getParentView().removePaidItems(selectedItems);
            }

            view.dispose();
            if (GiaoDienSieuThi.instance != null) {
                GiaoDienSieuThi.instance.dispose();
            }

            try {
                UIManager.setLookAndFeel(new FlatLightLaf()); //
                KhachHangDAO khachHangDAO = new KhachHangDAO();
                KhachHang kh = khachHangDAO.getKhachHangById(MaKH);
                if (kh != null) {
                    GiaoDienSieuThi ui = new GiaoDienSieuThi(kh);
                    Connection conn = DBConnection.getConnection();
                    new GiaoDienSieuThiController(ui, conn, kh);
                    ui.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Có lỗi xảy ra, vui lòng thử lại.");
        }
    }

    // Tạo danh sách ChiTietHoaDonOrder từ giỏ hàng
    private List<ChiTietHoaDonOrder> createChiTietHoaDons(List<GioHang> selectedItems) {
        List<ChiTietHoaDonOrder> chiTietHoaDons = new ArrayList<>();
        for (GioHang item : selectedItems) {
            ChiTietHoaDonOrder chiTiet = new ChiTietHoaDonOrder();
            chiTiet.setMaSanPham(item.getMaSP());
            chiTiet.setSoLuong(item.getSoLuong());
            chiTiet.setDonGia(item.getGiaban());
            chiTietHoaDons.add(chiTiet);
        }
        return chiTietHoaDons;
    }

    private BigDecimal tinhTongTien(List<ChiTietHoaDonOrder> chiTietHoaDons, int discountValue) {
        BigDecimal tongTienHang = BigDecimal.ZERO;

        for (ChiTietHoaDonOrder chiTiet : chiTietHoaDons) {
            BigDecimal donGia = BigDecimal.valueOf(chiTiet.getDonGia());
            BigDecimal soLuong = BigDecimal.valueOf(chiTiet.getSoLuong());
            tongTienHang = tongTienHang.add(donGia.multiply(soLuong));
        }

        // Trừ giảm giá
        BigDecimal giamGia = BigDecimal.valueOf(discountValue);
        BigDecimal tongTien = tongTienHang.subtract(giamGia);

        // Đảm bảo không âm
        if (tongTien.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return tongTien;
    }

    private GioHang getItemFromCart(List<GioHang> selectedItems, ChiTietHoaDonOrder chiTiet) {
        for (GioHang item : selectedItems) {
            if (item.getMaSP() == chiTiet.getMaSanPham()) { // Giả sử MaSP trong GioHang và ChiTietHoaDonOrder là giống nhau
                return item;
            }
        }
        return null; // Nếu không tìm thấy sản phẩm trong giỏ hàng
    }

    // Phương thức lọc bảng
    private void filterTable() {
        try {
            String searchText = hdview.getSearchText().toLowerCase();
            DefaultTableModel model = (DefaultTableModel) hdview.getTable().getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            hdview.getTable().setRowSorter(sorter);

            if (searchText.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                // Lọc theo tất cả các cột
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateStatus() {
        int row = hdview.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(hdview, "Vui lòng chọn một hóa đơn để cập nhật trạng thái!");
            return;
        }

        int maHD = (int) hdview.getTableModel().getValueAt(row, 0);
        String currentStatus = (String) hdview.getTableModel().getValueAt(row, 5);

        // Kiểm tra nếu trạng thái hiện tại là "Hoàn thành" hoặc "Hủy"
        if ("Hoàn thành".equals(currentStatus) || "Hủy".equals(currentStatus)) {
            JOptionPane.showMessageDialog(hdview, "Không thể thay đổi trạng thái của hóa đơn đã " + currentStatus.toLowerCase() + "!");
            return;
        }

        HoaDonTrangThaiDialog dialog = new HoaDonTrangThaiDialog(stmv, "Cập nhật trạng thái");
        dialog.setCurrentStatus(currentStatus); // Sử dụng phương thức mới thay vì setSelectedTrangThai
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String newStatus = dialog.getSelectedTrangThai();
            if (!newStatus.equals(currentStatus)) {
//                boolean success = hoaDonDAO.updateStatus(maHD, newStatus);
//                if (success) {
//                    hdview.getTableModel().setValueAt(newStatus, row, 5);
//                    JOptionPane.showMessageDialog(hdview, "Cập nhật trạng thái thành công!");
//
//                    List<ChiTietHoaDonOrder> dsCT = cthdDAO.getByMaHoaDon(maHD);
//                    for (ChiTietHoaDonOrder ct : dsCT) {
//                        int maSP = ct.getMaSanPham();
//                        int maLo = ct.getMaLoHang();
//                        int soLuong = ct.getSoLuong();
//
//                        if (newStatus.equals("Hoàn thành")) {
//                            tkDAO.hoanTatDonHang(maSP, maLo, soLuong);
//                        } else if (newStatus.equals("Hủy")) {
//                            tkDAO.huyDonHang(maSP, maLo, soLuong);
//                        }
//                    }
//                    if (tkController != null) {
//                        tkController.loadTonKhoTable();
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(hdview, "Lỗi khi cập nhật trạng thái!");
//                }
                boolean success = hoaDonDAO.updateStatus(maHD, newStatus);
                if (success) {
                    hdview.getTableModel().setValueAt(newStatus, row, 5);
                    JOptionPane.showMessageDialog(hdview, "Cập nhật trạng thái thành công!");

                    List<ChiTietHoaDonOrder> dsCT = cthdDAO.getByMaHoaDon(maHD);
                    for (ChiTietHoaDonOrder ct : dsCT) {
                        int maSP = ct.getMaSanPham();
                        int maLo = ct.getMaLoHang();
                        int soLuong = ct.getSoLuong();

                        if (newStatus.equals("Hoàn thành")) {
                            tkDAO.hoanTatDonHang(maSP, maLo, soLuong);
                        } else if (newStatus.equals("Hủy")) {
                            tkDAO.huyDonHang(maSP, maLo, soLuong);
                        }
                    }
                    if (tkController != null) {
                        tkController.loadTonKhoTable();
                    }

                    // 👉 THÊM ĐOẠN SAU ĐÂY để xuất PDF nếu trạng thái là "Đang giao"
                    if (newStatus.equals("Đang giao")) {
                        List<ChiTietHoaDonOrder> chiTietList = cthdDAO.getByMaHoaDon(maHD);
                        HoaDonOrder hoaDon = hoaDonDAO.getById(maHD);
                        hoaDon.setChiTietHoaDons(chiTietList);

//                        // Lấy tên và địa chỉ khách hàng từ bảng khachhang
//                        KhachHangDAO khDAO = new KhachHangDAO();
//                        KhachHang kh = khDAO.getKhachHangById(hoaDon.getMaKhachHang());
//
//                        if (kh != null) {
//                            DiaChi diaChi = new DiaChi();
//                            diaChi.setCustomerId(kh.getMaKH());
//                            diaChi.setName(kh.getHoTen());
//                            diaChi.setDetailAddress(kh.getDiaChi());
//                            hoaDon.setDiaChi(diaChi);
//                        }

                        // Lấy danh sách sản phẩm từ mã sản phẩm (chỉ để lấy tên hiển thị trong PDF)
                        List<GioHang> items = new ArrayList<>();
                        SanPhamDAO spDAO = null;
                        try {
                            spDAO = new SanPhamDAO(DBConnection.getConnection());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(hdview, "Không thể kết nối để lấy tên sản phẩm!");
                            return;
                        }

                        for (ChiTietHoaDonOrder ct : chiTietList) {
                            GioHang item = new GioHang();
                            item.setMaSP(ct.getMaSanPham());
                            item.setSoLuong(ct.getSoLuong());
                            item.setGiaban(ct.getDonGia());

                            String tenSP = spDAO.getTenSanPhamById(ct.getMaSanPham());
                            item.setTenSp(tenSP);

                            items.add(item);
                        }

                        generateInvoicePDF(hoaDon, items);
                    }
                }

            }
        }
    }
}
