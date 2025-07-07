/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;


import DAO.ChiTietSPDAO;
import DAO.DanhMucSanPhamDAO;
import DAO.GioHangDAO;
import DAO.KhachHangDAO;
import DAO.SanPhamDAO;
import Model.KhachHang;
import Model.SanPham;
import View.ChiTietSanPhamBH;
import View.GiaoDienSieuThi;
import View.GioHangView;
import View.KhachHangDialog;
import View.LichSuMuaHangView;
import controller.LoginController;
import model.DanhMucSanPham;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;
import view.LoginView;

public class GiaoDienSieuThiController {
    private GiaoDienSieuThi view;
    private SanPhamDAO spDao;
    private DanhMucSanPhamDAO dmDao;
    private KhachHang kh;
    private Connection conn;
    public GiaoDienSieuThiController(GiaoDienSieuThi view, Connection conn,KhachHang kh) {
        this.view = view;
        this.conn = conn;
        this.spDao = new SanPhamDAO(conn);
        this.dmDao = new DanhMucSanPhamDAO(conn);
        this.kh = kh;
        

        loadDanhMuc();
        addEvent();
        loadSanPhamTheoDanhMuc("Tất cả");
        

    }

    private void loadDanhMuc() {
        List<String> danhMucList = dmDao.getAll()
            .stream()
            .map(DanhMucSanPham::getTenDanhMuc)
            .collect(Collectors.toList());

        danhMucList.add(0, "Tất cả");
        view.getCategoryList().setListData(danhMucList.toArray(new String[0]));
    }

    private void addEvent() {
        view.getBtnSearch().addActionListener(e -> timKiemSanPham());
        
        view.getBtnLogout().addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose(); // đóng TrangChuView
           //mở lại loginview
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        }
    });
        view.getCategoryList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = view.getCategoryList().getSelectedValue();
                loadSanPhamTheoDanhMuc(selected);
            }
        });
        view.getBtnCart().addActionListener(e -> {
            GioHangView view = new GioHangView(kh.getMaKH());
        GioHangDAO dao = new GioHangDAO();
        GioHangController controller = new GioHangController(view, dao, kh.getMaKH());
        view.setVisible(true);
        });
        
        JPopupMenu menu = new JPopupMenu();

        JMenuItem xemThongTin = new JMenuItem("Thông tin khách hàng");
        JMenuItem lichSuMuaHang = new JMenuItem("Lịch sử mua hàng");

        xemThongTin.addActionListener(e -> {
            // gọi trang xem thông tin khách hàng
            KhachHangDialog dialog = new KhachHangDialog(view, "Thông tin khách hàng");
            dialog.anTruongMaKH();
            dialog.setFields(
                String.valueOf(kh.getMaKH()),  // ✅ ép int về String đúng cách
                kh.getHoTen(),
                kh.getSoDienThoai(),
                kh.getDiaChi(),
                kh.getEmail()
            );
            
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
            // Cập nhật object kh
            kh.setHoTen(dialog.getHoTen());
            kh.setSoDienThoai(dialog.getSoDienThoai());
            kh.setDiaChi(dialog.getDiaChi());
            kh.setEmail(dialog.getEmail());

            if (new KhachHangDAO().updateKhachHang(kh)) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        });

        lichSuMuaHang.addActionListener(e -> {
            JDialog dialog = new JDialog(view, "Lịch sử mua hàng", true);
            LichSuMuaHangView lichSuView = new LichSuMuaHangView();
            new LichSuMuaHangController(lichSuView, kh.getMaKH());
            dialog.getContentPane().add(lichSuView);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(view);
            dialog.setVisible(true);
        });

        menu.add(xemThongTin);
        menu.add(lichSuMuaHang);
         // Gắn popup cho avatar và username
        MouseAdapter showPopup = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        };

        view.getAvatarIcon().addMouseListener(showPopup);
        view.getUsernameLabel().addMouseListener(showPopup);

    }
        private void loadSanPhamTheoDanhMuc(String tenDanhMuc) {
        List<SanPham> list = spDao.searchWithFilters("", tenDanhMuc, -1);
        JPanel panel = view.getProductPanel();
        panel.removeAll();

        for (SanPham sp : list) {
            JPanel card = view.createProductCard(sp);

            // Gắn sự kiện click vào card
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String ten = sp.getTenSanPham();
                    String gia = "₫" + sp.getGiaBan().intValue();

                    // Xử lý ảnh
                    ImageIcon hinhAnh;
                    if (sp.getHinhAnh() != null) {
                        Image img = new ImageIcon(sp.getHinhAnh()).getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                        hinhAnh = new ImageIcon(img);
                    } else {
                        hinhAnh = new ImageIcon(); // fallback
                    }

                    // Dữ liệu bảng chi tiết
                    String[][] chiTiet = {
                        {"Thương hiệu", sp.getThuongHieu()},
                        {"Xuất xứ", sp.getXuatXu()},
                        {"Thành phần", sp.getThanhPhan()},
                        {"Hướng dẫn", sp.getHuongDanSuDung()},
                        {"Bảo quản", sp.getBaoQuan()}
                        
                        
                    };

                    // Mở dialog
                    ChiTietSanPhamBH dialog = new ChiTietSanPhamBH(
                        null, 
                        sp.getTenSanPham(), 
                        "₫" + sp.getGiaBan().intValue(), 
                        hinhAnh, 
                        chiTiet, 
                        sp.getMaSanPham(), 
                        sp.getSoLuongKhaDung()
                    );
                    new ChiTietSPController(dialog, new ChiTietSPDAO(), kh.getMaKH(), conn);

                    dialog.setVisible(true);
                }
            });

            panel.add(card);
        }

        panel.revalidate();
        panel.repaint();
    }

        private void timKiemSanPham() {
        String keyword = view.getSearchField().getText().trim().toLowerCase();
        List<SanPham> result = spDao.searchWithFilters(keyword, "Tất cả", -1);

        JPanel panel = view.getProductPanel();
        panel.removeAll();

        for (SanPham sp : result) {
            JPanel card = view.createProductCard(sp);

            // Gắn click mở chi tiết – giống như loadSanPhamTheoDanhMuc
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String ten = sp.getTenSanPham();
                    String gia = "₫" + sp.getGiaBan().intValue();

                    ImageIcon hinhAnh;
                    if (sp.getHinhAnh() != null) {
                        Image img = new ImageIcon(sp.getHinhAnh()).getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                        hinhAnh = new ImageIcon(img);
                    } else {
                        hinhAnh = new ImageIcon();
                    }

                    String[][] chiTiet = {
                        {"Thương hiệu", sp.getThuongHieu()},
                        {"Xuất xứ", sp.getXuatXu()},
                        {"Thành phần", sp.getThanhPhan()},
                        {"Hướng dẫn", sp.getHuongDanSuDung()},
                        {"Bảo quản", sp.getBaoQuan()}                        
                    };

                    ChiTietSanPhamBH dialog = new ChiTietSanPhamBH(
                        null,
                        ten,
                        gia,
                        hinhAnh,
                        chiTiet,
                        sp.getMaSanPham(),
                        sp.getSoLuongKhaDung()
                    );
                    new ChiTietSPController(dialog, new ChiTietSPDAO(), kh.getMaKH(), conn);
                    dialog.setVisible(true);
                }
            });

            panel.add(card);
        }

        panel.revalidate();
        panel.repaint();
    }




}

