package controller;


import Bridge.DBConnection;
import Controller.GiaoDienSieuThiController;
import DAO.KhachHangDAO;
import DAO.TaiKhoanDAO;
import Model.TaiKhoan;
import View.TrangChuView;
import View.adminView;
import  Controller.SignUpController;
import Controller.TrangChuController;
import DAO.NhanVienDAOplus;
import Model.KhachHang;
import View.GiaoDienSieuThi;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import Model.NhanVien;
import view.LoginView;
import view.SignUpView;

public class LoginController {
    private LoginView view;
    private TaiKhoanDAO acc_mgm;

    public LoginController(LoginView view) {
        this.view = view;
        this.acc_mgm = new TaiKhoanDAO();

        // Gắn sự kiện đăng nhập
        view.addSignInListener(e -> signInBtnClicked());

        // Gắn sự kiện đăng ký
       view.addSignUpListener(e -> {
            SignUpView signUpView = new SignUpView();
            KhachHangDAO khachHangDAO = new KhachHangDAO(); // Hoặc dùng singleton nếu cần
            new SignUpController(signUpView, khachHangDAO); // GẮN CONTROLLER!
            signUpView.setVisible(true);
            view.dispose();
        });

    }

    private void signInBtnClicked() {
        try {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Hãy Điền Đầy Đủ Thông Tin!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TaiKhoan account = new TaiKhoan(username, password, null);
            boolean success = acc_mgm.SignIn(account);

            if (success) {
                String role = acc_mgm.getRoleByUsername(username);
                TaiKhoan fullAccount = acc_mgm.getByUsername(username);
                
                if ("admin".equalsIgnoreCase(role) || "nhanvien".equalsIgnoreCase(role)) {
                    System.out.println("maTaiKhoan: " + account.getMaTaiKhoan());
                    UIManager.setLookAndFeel(new FlatLightLaf()); //
                    NhanVienDAOplus nvDAO = new NhanVienDAOplus();
                    NhanVien nhanVien= nvDAO.getNhanVienById(fullAccount.getIdLienKet());
                    JOptionPane.showMessageDialog(view, "Đăng Nhập Thành Công", "Success", JOptionPane.INFORMATION_MESSAGE);
                    TrangChuView mainView = new TrangChuView(nhanVien); // Truyền model vào
                    new TrangChuController(mainView, nhanVien); // truyền view + nhân viên vào controller

                    mainView.setVisible(true); 
                    mainView.setVisible(true);
                } 
                else if("khachhang".equalsIgnoreCase(role)) {
                    try {
                        UIManager.setLookAndFeel(new FlatLightLaf()); //
                        KhachHangDAO khachHangDAO = new KhachHangDAO();
                        KhachHang kh = khachHangDAO.getKhachHangById(fullAccount.getIdLienKet());
                        if (kh != null) {
                            GiaoDienSieuThi ui = new GiaoDienSieuThi(kh);
                            Connection conn = DBConnection.getConnection();
                            new GiaoDienSieuThiController(ui, conn, kh); // nếu controller nhận maKH
                            ui.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

                view.dispose(); // Đóng form login sau khi vào hệ thống
            } else {
                JOptionPane.showMessageDialog(view, "Đăng Nhập Thất Bại", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
