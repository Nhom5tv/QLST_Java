package view;



import DAO.KhachHangDAO;
import DAO.NhanVienDAOplus;
import DAO.TaiKhoanDAO;
import Model.KhachHang;
import Model.TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import Model.NhanVien;

public class TaiKhoanDialog extends JDialog {

    private JTextField txtMaTaiKhoan;
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboQuyen;
    private JComboBox<Object> cboLienKet; // nhân viên hoặc khách hàng
    private JButton btnSave;
    private JButton btnCancel;

    private TaiKhoan taiKhoanEdit;

    public TaiKhoanDialog(Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        setTitle("Quản lý Tài Khoản");
        loadQuyen();
        addEvents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaTaiKhoan = new JLabel("Mã Tài Khoản:");
        txtMaTaiKhoan = new JTextField(15);
        txtMaTaiKhoan.setEnabled(false);

        JLabel lblTenDangNhap = new JLabel("Tên Đăng Nhập:");
        txtTenDangNhap = new JTextField(15);

        JLabel lblMatKhau = new JLabel("Mật Khẩu:");
        txtMatKhau = new JPasswordField(15);

        JLabel lblQuyen = new JLabel("Quyền:");
        cboQuyen = new JComboBox<>();

        JLabel lblLienKet = new JLabel("Liên Kết:");
        cboLienKet = new JComboBox<>();

        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        // Row 0: Mã Tài Khoản
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        add(lblMaTaiKhoan, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        add(txtMaTaiKhoan, gbc);

        // Row 1: Tên Đăng Nhập
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        add(lblTenDangNhap, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        add(txtTenDangNhap, gbc);

        // Row 2: Mật Khẩu
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        add(lblMatKhau, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        add(txtMatKhau, gbc);

        // Row 3: Quyền
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        add(lblQuyen, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        add(cboQuyen, gbc);

        // Row 4: Liên Kết
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        add(lblLienKet, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        add(cboLienKet, gbc);

        // Row 5: Nút Lưu và Hủy
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.5;
        add(btnSave, gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        add(btnCancel, gbc);

        setSize(400, 320);
    }

    private void loadQuyen() {
        cboQuyen.removeAllItems();
        
        cboQuyen.addItem("nhanvien");
        cboQuyen.addItem("khachhang");

        cboQuyen.addActionListener(e -> {
            loadLienKetByQuyen((String) cboQuyen.getSelectedItem());
        });
    }

    private void loadLienKetByQuyen(String quyen) {
        cboLienKet.removeAllItems();
        switch (quyen) {
            case "nhanvien":
                NhanVienDAOplus daoNV = new NhanVienDAOplus();
                List<NhanVien> dsNV = daoNV.getAllNhanVien();

                for (NhanVien nv : dsNV) {
                    cboLienKet.addItem(nv);
                }
                cboLienKet.setEnabled(true);
                break;
            case "khachhang":
                KhachHangDAO daoKH = new KhachHangDAO();
                List<KhachHang> dsKH = daoKH.getAllKhachHang();
              
                for (KhachHang kh : dsKH) {
                    cboLienKet.addItem(kh);
                }
                cboLienKet.setEnabled(true);
                break;
            default:
                cboLienKet.setEnabled(false);
                break;
        }
    }

    private void addEvents() {
        btnSave.addActionListener(e -> {
            if (validateForm()) {
                if (taiKhoanEdit == null) {
                    insertTaiKhoan();
                } else {
                    updateTaiKhoan();
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    private boolean validateForm() {
        if (txtTenDangNhap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được để trống!");
            txtTenDangNhap.requestFocus();
            return false;
        }
        if (txtMatKhau.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!");
            txtMatKhau.requestFocus();
            return false;
        }
        if (cboQuyen.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Phải chọn quyền!");
            cboQuyen.requestFocus();
            return false;
        }
        String quyen = (String) cboQuyen.getSelectedItem();
        if ((quyen.equals("nhanvien") || quyen.equals("khachhang")) && cboLienKet.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Phải chọn liên kết nhân viên hoặc khách hàng!");
            cboLienKet.requestFocus();
            return false;
        }
        return true;
    }

    private void insertTaiKhoan() {
        try {
            TaiKhoan tk = new TaiKhoan();
            Object lienKet = cboLienKet.getSelectedItem();
            if (lienKet instanceof NhanVien) {
                tk.setIdLienKet(((NhanVien) lienKet).getma_nv());
            } else if (lienKet instanceof KhachHang) {
                tk.setIdLienKet(((KhachHang) lienKet).getMaKH());
            } else {
                tk.setIdLienKet(null);
            }
            tk.setTenDangNhap(txtTenDangNhap.getText().trim());
            tk.setMatKhau(new String(txtMatKhau.getPassword()));
            tk.setQuyen((String) cboQuyen.getSelectedItem());

            boolean success = TaiKhoanDAO.insert(tk);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void updateTaiKhoan() {
        try {
            taiKhoanEdit.setTenDangNhap(txtTenDangNhap.getText().trim());
            taiKhoanEdit.setMatKhau(new String(txtMatKhau.getPassword()));
            taiKhoanEdit.setQuyen((String) cboQuyen.getSelectedItem());

            Object lienKet = cboLienKet.getSelectedItem();
            if (lienKet instanceof NhanVien) {
                taiKhoanEdit.setIdLienKet(((NhanVien) lienKet).getma_nv());
            } else if (lienKet instanceof KhachHang) {
                taiKhoanEdit.setIdLienKet(((KhachHang) lienKet).getMaKH());
            } else {
                taiKhoanEdit.setIdLienKet(null);
            }

            boolean success = TaiKhoanDAO.update(taiKhoanEdit);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    public void setTaiKhoanEdit(TaiKhoan tk) {
        this.taiKhoanEdit = tk;
        if (tk == null) {
            txtMaTaiKhoan.setText("");
            txtTenDangNhap.setText("");
            txtMatKhau.setText("");
            cboQuyen.setSelectedIndex(0);
            cboLienKet.removeAllItems();
            cboLienKet.setEnabled(false);
            return;
        }

        txtMaTaiKhoan.setText(String.valueOf(tk.getMaTaiKhoan()));
        txtTenDangNhap.setText(tk.getTenDangNhap());
        txtMatKhau.setText(tk.getMatKhau());
        cboQuyen.setSelectedItem(tk.getQuyen());
        loadLienKetByQuyen(tk.getQuyen());

        if ("nhanvien".equalsIgnoreCase(tk.getQuyen())) {
            NhanVienDAOplus daoNV = new NhanVienDAOplus();
                List<NhanVien> dsNV = daoNV.getAllNhanVien();

            for (NhanVien nv : dsNV) {
                if (tk.getIdLienKet() != null && nv.getma_nv()== tk.getIdLienKet()) {
                    cboLienKet.setSelectedItem(nv);
                    break;
                }
            }
        } else if ("khachhang".equalsIgnoreCase(tk.getQuyen())) {
              KhachHangDAO daoKH = new KhachHangDAO();
                List<KhachHang> dsKH = daoKH.getAllKhachHang();
            for (KhachHang kh : dsKH) {
                if (tk.getIdLienKet() != null && kh.getMaKH() == tk.getIdLienKet()) {
                    cboLienKet.setSelectedItem(kh);
                    break;
                }
            }
        } else {
            cboLienKet.setSelectedItem(null);
            cboLienKet.setEnabled(false);
        }
    }
}
