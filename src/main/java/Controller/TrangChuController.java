package Controller;

import Bridge.DBConnection;
import View.*;
import Controller.*;
import controller.LoginController;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import Model.NhanVien;
import view.LoginView;


public class TrangChuController {
    private TrangChuView view;
    private NhanVien nhanVien;


    public TrangChuController(TrangChuView view, NhanVien nhanVien) {
        this.view = view;
        this.nhanVien = nhanVien;
        initController();
    }

    private void initController() {
        try {
            Connection conn = DBConnection.getConnection();

            // 🗂 Quản lý sản phẩm (menu có con - giữ nguyên)
            DanhMucView dmView = new DanhMucView();
            new DanhMucController(dmView, conn);
            bindClick(view.getFormDanhMucLabel(), dmView, "Danh Mục Sản Phẩm");

            SanPhamView spView = new SanPhamView();
            new SanPhamController(spView, conn);
            bindClick(view.getFormSanPhamLabel(), spView, "Sản Phẩm");


            TonKhoView tkView = new TonKhoView();
            TonKhoController tkController = new TonKhoController(tkView, conn);
            bindClick(view.getFormTonKhoLabel(), tkView, "Tồn Kho");

            LoHangView lhView = new LoHangView();
            new LoHangController(view, lhView, tkController);
            bindClick(view.getFormLoHangLabel(), lhView, "Lô Hàng");

            PhieuNhapView pnView = new PhieuNhapView();
            new PhieuNhapController(view, pnView);
            bindClick(view.getFormPhieuNhapLabel(), pnView, "Phiếu Nhập");


            NhaCungCapView nccView = new NhaCungCapView();
            new NhaCungCapController(view, nccView);
            bindClick(view.getFormNhaCungCapLabel(), nccView, "Nhà Cung Cấp");

            // 👥 Quản lý nhân sự (menu có con - giữ nguyên)
            NhanVienView1 nvView = new NhanVienView1();
            new NhanVienControllerplus(view, nvView);
            bindClick(view.getFormNhanVienLabel(), nvView, "Hồ Sơ Nhân Viên");

            ChamCongView ccView = new ChamCongView();
            new ChamCongController(view, ccView, nhanVien.getma_nv());
            bindClick(view.getFormChamCongLabel(), ccView, "Chấm Công");

            LuongView lView = new LuongView();
            new LuongController(lView, view);
            bindClick(view.getFormLuongLabel(), lView, "Lương");

            // 🧾 Quản lý bán hàng (menu có con - giữ nguyên)

            HoaDonView hoaDonView = new HoaDonView();
            HoaDonController hdController = new HoaDonController(view, hoaDonView, conn);
            bindClick(view.getFormHDOfflineLabel(), hoaDonView, "Hóa đơn tại siêu thị");

            GiaoDienBanHang gdbhView = new GiaoDienBanHang();
            new GiaoDienBanHangController(gdbhView, conn, nhanVien, hdController);
            bindClick(view.getFormCTHDOfflineLabel(), gdbhView, "Bán hàng tại siêu thị");

            HoaDonDatHangView hoaDonDatHangView = new HoaDonDatHangView();
            new HoaDonOrderController(view, hoaDonDatHangView, tkController);
            bindClick(view.getFormHDOnlineLabel(), hoaDonDatHangView, "Hóa đơn Online");

            // === CÁC MENU KHÔNG CÓN CON - CLICK VÀO SECTION LABEL ===

            // 🔐 Quản lý tài khoản - click vào section label
            TaiKhoanView taiKhoanView = new TaiKhoanView();
            new TaiKhoanController(taiKhoanView);
            bindSectionClick(view.getFormTaiKhoanSectionLabel(), taiKhoanView, "Quản Lý Tài Khoản");

            // 🧑‍💻 Quản lý khách hàng - click vào section label  
            KhachHangView khView = new KhachHangView();
            new KhachHangController(view, khView);
            bindSectionClick(view.getFormKhachHangSectionLabel(), khView, "Quản Lý Khách Hàng");

            // 💰 Quản lý tài chính - click vào section label
            TaiChinhView tcView = new TaiChinhView();
            new TaiChinhController(tcView);
            bindSectionClick(view.getFormTaiChinhSectionLabel(), tcView, "Quản Lý Tài Chính");

            // 🎁 Khuyến mãi - click vào section label
            KhuyenMaiView kmView = new KhuyenMaiView();
            new KhuyenMaiController(kmView);
            bindSectionClick(view.getFormKhuyenMaiSectionLabel(), kmView, "Quản Lý Khuyến Mãi");


            // 📊 Thống kê - click vào section label
            JPanel tkPanel = new JPanel();
            tkPanel.add(new JLabel("Thống kê đang phát triển..."));
            bindSectionClick(view.getFormThongKeSectionLabel(), tkPanel, "Thống Kê");

            bindavatarClick();
            phanQuyenNhanVien();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi khởi tạo controller hoặc kết nối DB");
        }
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
    }

    // Bind click cho menu có con (giữ nguyên)
    private void bindClick(JPanel label, JPanel panel, String title) {
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                view.setTitleText(title);
                view.setCenterPanel(panel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Thêm hiệu ứng hover để người dùng biết có thể click
                label.setForeground(new java.awt.Color(0, 123, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(java.awt.Color.BLACK);
            }
        });
    }

    // Bind click cho section label (menu không có con)
    private void bindSectionClick(JPanel sectionLabel, JPanel panel, String title) {
        sectionLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                view.setTitleText(title);
                view.setCenterPanel(panel);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Thêm hiệu ứng hover để người dùng biết có thể click
                sectionLabel.setForeground(new java.awt.Color(0, 123, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sectionLabel.setForeground(java.awt.Color.BLACK);
            }
        });

        // Thêm cursor pointer để báo hiệu có thể click
        sectionLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    private void bindavatarClick() {
        JLabel avatar = view.getlblAvatar();
        avatar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NhanVienCaNhanView2 caNhanView = new NhanVienCaNhanView2();
                caNhanView.setThongTinNhanVien(nhanVien);
                NhanVienControllerplus nvcp = new NhanVienControllerplus(view, caNhanView, nhanVien.getma_nv());
                view.setCenterPanel(caNhanView);
            }

        });
    }


    private void phanQuyenNhanVien() {
        String role = nhanVien.getChucvu().trim().toLowerCase();

        switch (role) {
            case "admin":
                // Không ẩn gì cả – xem được tất cả
                view.setTitleText("Quản Lý Tài Chính");
                TaiChinhView tcView = new TaiChinhView();
                new TaiChinhController(tcView);
                view.setCenterPanel(tcView);
                break;

            case "thu ngân":
                // Chỉ được xem hóa đơn & khuyến mãi
                hide(
                        view.getFormSanPhamSectionLabel(),
                        view.getFormNhanVienLabel(),
                        view.getFormLuongLabel(),
                        view.getFormTaiKhoanSectionLabel(),
                        view.getFormTaiChinhSectionLabel(),
                        view.getFormThongKeSectionLabel(),

                        view.getFormDanhMucLabel(),
                        view.getFormSanPhamLabel(),
                        view.getFormLoHangLabel(),
                        view.getFormPhieuNhapLabel(),
                        view.getFormTonKhoLabel(),
                        view.getFormNhaCungCapLabel(),
                        view.getFormKhachHangSectionLabel(),
                        view.getFormKhuyenMaiSectionLabel()
                );
                view.setTitleText("Bán hàng tại siêu thị");
                GiaoDienBanHang gdbhView = new GiaoDienBanHang();
                HoaDonView hoaDonViewThuNgan = new HoaDonView();
                try {
                    HoaDonController hdControllerThuNgan = new HoaDonController(view, hoaDonViewThuNgan, DBConnection.getConnection());
                    new GiaoDienBanHangController(gdbhView, DBConnection.getConnection(), nhanVien, hdControllerThuNgan);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.setCenterPanel(gdbhView);
                break;

            case "kho":
                // Chỉ được xem hóa đơn & khuyến mãi
                hide(
                        view.getFormNhanVienLabel(),
                        view.getFormLuongLabel(),
                        view.getFormTaiKhoanSectionLabel(),
                        view.getFormTaiChinhSectionLabel(),
                        view.getFormThongKeSectionLabel(),
                        view.getFormKhachHangSectionLabel(),
                        view.getFormHDOfflineLabel(),
                        view.getFormCTHDOfflineLabel(),
                        view.getFormHDOnlineLabel(),
                        view.getFormCTHDOnlineLabel(),
                        view.getFormKhuyenMaiSectionLabel(),
                        view.getLblBanHangSection()
                );
                view.setTitleText("Tồn Kho");
                TonKhoView tkView = new TonKhoView();
                try {
                    TonKhoController tkController = new TonKhoController(tkView, DBConnection.getConnection());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                view.setCenterPanel(tkView);
                break;

            default:
                // Ẩn hết nếu không xác định được vai trò
                hideAll();
                break;
        }
    }

    private void hide(JComponent... components) {
        for (JComponent c : components) {
            if (c != null) c.setVisible(false);
        }
    }

    private void hideAll() {
        hide(
                view.getFormDanhMucLabel(), view.getFormSanPhamLabel(), view.getFormLoHangLabel(),
                view.getFormPhieuNhapLabel(), view.getFormTonKhoLabel(), view.getFormNhaCungCapLabel(),
                view.getFormNhanVienLabel(), view.getFormChamCongLabel(), view.getFormLuongLabel(),
                view.getFormHDOfflineLabel(), view.getFormCTHDOfflineLabel(),
                view.getFormHDOnlineLabel(), view.getFormCTHDOnlineLabel(),
                view.getFormTaiKhoanSectionLabel(), view.getFormKhachHangSectionLabel(),
                view.getFormTaiChinhSectionLabel(), view.getFormKhuyenMaiSectionLabel(),
                view.getFormThongKeSectionLabel(), view.getFormSanPhamSectionLabel(),
                view.getLblBanHangSection()
        );
    }
}