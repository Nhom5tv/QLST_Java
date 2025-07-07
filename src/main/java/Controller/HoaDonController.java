package Controller;

import DAO.HoaDonDAO;
import Model.HoaDon;
import View.HoaDonView;
import View.TrangChuView;
import View.ThongBaodialog;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.sql.Connection;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class HoaDonController {
    private HoaDonView hdView;
    private HoaDonDAO hdDao;
    private TrangChuView stmv;
    private Connection conn;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final Random random = new Random();

    // Generate random unique int ID between 10000 and 99999
    private int generateRandomId() {
        // Ideally this should check for collisions in DB, omitted for simplicity
        return random.nextInt(90000) + 10000;
    }

    public HoaDonController(TrangChuView view, HoaDonView panel, Connection conn) {
        this.stmv = view;
        this.hdView = panel;
        this.conn = conn;
        this.hdDao = new HoaDonDAO(conn);

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

        loadTableData();
    }

    public void loadTableData() {
    System.out.println(">> Đang load dữ liệu hóa đơn...");
    DefaultTableModel model = hdView.getTableModel();
    model.setRowCount(0);
    List<HoaDon> list = hdDao.getAllHoaDon();
    System.out.println(">> Số hóa đơn lấy được: " + list.size());

    for (HoaDon hd : list) {
        System.out.println(">> HĐ: " + hd.getMaHoaDon() + " | NV: " + hd.getTenNV());
        model.addRow(new Object[]{
                hd.getMaHoaDon(),
                dateFormat.format(Timestamp.valueOf(hd.getNgayLap())),
                hd.getTenNV(),
                hd.getTongTien(),
                hd.getHinhThucThanhToan(),
                hd.getGhiChu()
        });
    }
}



    private void timkiem() {
        String keyword = hdView.getSearchText().toLowerCase().trim();
        DefaultTableModel model = hdView.getTableModel();
        model.setRowCount(0);

        List<HoaDon> list = hdDao.getAllHoaDon();
        for (HoaDon hd : list) {
            String dateStr = dateFormat.format(hd.getNgayLap());
            boolean matchesId = false;
            boolean matchesDate = false;

            // Search by invoice ID (int)
            try {
                if (!keyword.isEmpty()) {
                    int keywordId = Integer.parseInt(keyword);
                    matchesId = (hd.getMaHoaDon() == keywordId);
                }
            } catch (NumberFormatException e) {
                // Not a number, skip ID matching
            }

            // Search by formatted date (string contains)
            matchesDate = dateStr.contains(keyword);

            if (matchesId || matchesDate) {
                model.addRow(new Object[]{
                        hd.getMaHoaDon(), // Bỏ giá trị Boolean
                        dateStr,
                        hd.getTenNV(),
                        hd.getTongTien(),
                        hd.getHinhThucThanhToan(),
                        hd.getGhiChu()
                });
            }
        }
    }
}
