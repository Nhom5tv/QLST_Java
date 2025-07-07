package Controller;

import  DAO.KhachHangDAO;
import  DAO.TaiKhoanDAO;
import  Model.KhachHang;
import  Model.TaiKhoan;
import controller.LoginController;
import  view.LoginView;
import  view.SignUpView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SignUpController {
    private SignUpView view;
    private KhachHangDAO khachHangDAO;

    public SignUpController(SignUpView view, KhachHangDAO khachHangDAO) {
        this.view = view;
        this.khachHangDAO = khachHangDAO;

        // Gắn sự kiện nút
        this.view.addSignUpListener(new SignUpListener());
        this.view.addResetListener(e -> resetForm());
        this.view.addBackListener(e -> backToLogin());

      
       

    }
    private void backToLogin() {
   view.dispose(); // Đóng SignUpView hiện tại
    LoginView loginView = new LoginView();
    new LoginController(loginView); // Gắn controller cho login view
    loginView.setVisible(true);
}


    class SignUpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String hoTen = view.getUsername();
            String soDienThoai = view.getPhone();
            String email = view.getEmail();
            String diaChi = view.getAddress();
            String tenDangNhap = view.getUsername();
            String password = new String(view.passwordField.getPassword());
            String confirmPassword = new String(view.confirmPasswordField.getPassword());
            boolean termsChecked = view.isTermsChecked();

            if (hoTen.isEmpty() || soDienThoai.isEmpty() || email.isEmpty() || diaChi.isEmpty()
                    || tenDangNhap.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(view, "Mật khẩu và xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!termsChecked) {
                JOptionPane.showMessageDialog(view, "Bạn phải đồng ý điều khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo khách hàng
            KhachHang kh = new KhachHang();
            kh.setHoTen(hoTen);
            kh.setSoDienThoai(soDienThoai);
            kh.setEmail(email);
            kh.setDiaChi(diaChi);

            boolean success = khachHangDAO.addKhachHang(kh);

            if (success) {
                int maKH = kh.getMaKH(); // Đảm bảo DAO set đúng ID sau insert

                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setTenDangNhap(tenDangNhap);
                taiKhoan.setMatKhau(password);
                taiKhoan.setQuyen("khachhang");
                taiKhoan.setIdLienKet(maKH);

                boolean accountCreated = TaiKhoanDAO.insert(taiKhoan); // Dùng phương thức static

                if (accountCreated) {
                    JOptionPane.showMessageDialog(view, "Đăng ký thành công! Mã KH: " + maKH);
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Tạo tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Lưu khách hàng thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        view.usernameField.setText("");
        view.phoneField.setText("");
        view.emailField.setText("");
        view.addressArea.setText("");
        view.passwordField.setText("");
        view.confirmPasswordField.setText("");
        view.termsCheckBox.setSelected(false);
    }

   
}
