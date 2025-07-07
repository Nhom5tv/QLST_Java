package  View;

import com.formdev.flatlaf.FlatLightLaf;
import  DAO.KhuyenMaiDAO;
import  Model.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.JDatePanelImpl;

public class KhuyenMaiDialog extends JDialog {
    private JTextField txtMaKM, txtTenKM, txtPhanTramGiam, txtSoLuong;
    private JTextArea txtGhiChu;
    private JButton btnLuu, btnHuy;
    private JTextField txtTongTienToiThieu, txtSoLuongSpToiThieu;

    private JDatePickerImpl datePickerNgayBatDau, datePickerNgayKetThuc;

    private KhuyenMai khuyenMaiEdit;

    public KhuyenMaiDialog(Frame owner) {
        super(owner, true);
        initLookAndFeel();
        initUI();
        setTitle("Quản lý Khuyến mãi");
        setSize(520, 480);
        setLocationRelativeTo(owner);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}
    }


    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel[] labels = {
            new JLabel("Mã Khuyến mãi:"),
            new JLabel("Tên Khuyến mãi:"),
            new JLabel("Ngày Bắt Đầu:"),
            new JLabel("Ngày Kết Thúc:"),
            new JLabel("Phần trăm giảm:"),
            new JLabel("Số lượng:"),
            new JLabel("Tổng tiền tối thiểu:"),
            new JLabel("Số lượng SP tối thiểu:"),
            new JLabel("Ghi chú:")
        };

        txtMaKM = new JTextField(20);
        txtTenKM = new JTextField(20);
        datePickerNgayBatDau = createDatePicker();
        datePickerNgayKetThuc = createDatePicker();
        txtPhanTramGiam = new JTextField(20);
        txtSoLuong = new JTextField(20);
        txtTongTienToiThieu = new JTextField(20);
        txtSoLuongSpToiThieu = new JTextField(20);
        txtGhiChu = new JTextArea(4, 20);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);

        JComponent[] fields = {
            txtMaKM, txtTenKM, datePickerNgayBatDau, datePickerNgayKetThuc,
            txtPhanTramGiam, txtSoLuong, txtTongTienToiThieu, txtSoLuongSpToiThieu, scrollGhiChu
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            formPanel.add(fields[i], gbc);
        }

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");

        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnHuy.setBackground(new Color(231, 76, 60));
        btnHuy.setForeground(Color.WHITE);

        btnLuu.setFocusPainted(false);
        btnHuy.setFocusPainted(false);
        btnLuu.setPreferredSize(new Dimension(80, 35));
        btnHuy.setPreferredSize(new Dimension(80, 35));

        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        addEvents();
    }

    private void addEvents() {
        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            if (!validateForm()) return;

            try {
                KhuyenMai km = (khuyenMaiEdit == null) ? new KhuyenMai() : khuyenMaiEdit;
                km.setMaKhuyenMai(txtMaKM.getText().trim());
                km.setTenKhuyenMai(txtTenKM.getText().trim());

                LocalDate bd = getLocalDateFromDatePicker(datePickerNgayBatDau);
                LocalDate kt = getLocalDateFromDatePicker(datePickerNgayKetThuc);

                km.setNgayBatDau(java.sql.Date.valueOf(bd));
                km.setNgayKetThuc(java.sql.Date.valueOf(kt));

                km.setPhanTramGiam(Integer.parseInt(txtPhanTramGiam.getText().trim()));
                km.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));

                // Parse BigDecimal cho tongTienToiThieu
                String tongTienStr = txtTongTienToiThieu.getText().trim();
                if (tongTienStr.isEmpty()) {
                    km.setTongTienToiThieu(BigDecimal.ZERO);
                } else {
                    km.setTongTienToiThieu(new BigDecimal(tongTienStr));
                }

                // Parse Integer hoặc null cho soLuongSpToiThieu
                String soLuongSpStr = txtSoLuongSpToiThieu.getText().trim();
                if (soLuongSpStr.isEmpty()) {
                    km.setSoLuongSpToiThieu(null);
                } else {
                    km.setSoLuongSpToiThieu(Integer.parseInt(soLuongSpStr));
                }

                km.setGhiChu(txtGhiChu.getText().trim());

                KhuyenMaiDAO dao = new KhuyenMaiDAO();

                boolean success = (khuyenMaiEdit == null)
                        ? dao.insertKhuyenMai(km)
                        : dao.updateKhuyenMai(km);


                if (success) {
                    JOptionPane.showMessageDialog(this,
                            (khuyenMaiEdit == null) ? "Thêm mới thành công!" : "Cập nhật thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            (khuyenMaiEdit == null) ? "Thêm mới thất bại!" : "Cập nhật thất bại!");
                }

            } catch (NumberFormatException ex) {
                showError("Phần trăm giảm, số lượng và các trường số phải là số hợp lệ!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xảy ra: " + ex.getMessage());
            }
        });
    }

    private boolean validateForm() {
        String ten = txtTenKM.getText().trim();
        String ma = txtMaKM.getText().trim();

        if (ma.isEmpty()) {
            showError("Mã khuyến mãi không được để trống!", txtMaKM);
            return false;
        }
        if (ten.isEmpty()) {
            showError("Tên khuyến mãi không được để trống!", txtTenKM);
            return false;
        }

        LocalDate bd = getLocalDateFromDatePicker(datePickerNgayBatDau);
        LocalDate kt = getLocalDateFromDatePicker(datePickerNgayKetThuc);

        if (bd == null) {
            showError("Ngày bắt đầu không hợp lệ!", datePickerNgayBatDau);
            return false;
        }
        if (kt == null) {
            showError("Ngày kết thúc không hợp lệ!", datePickerNgayKetThuc);
            return false;
        }

        if (kt.isBefore(bd)) {
            showError("Ngày kết thúc phải sau ngày bắt đầu!");
            return false;
        }

        String ptgStr = txtPhanTramGiam.getText().trim();
if (!ptgStr.isEmpty()) {
    try {
        int ptg = Integer.parseInt(ptgStr);
        if (ptg < 0 || ptg > 100) {
            showError("Phần trăm giảm phải từ 0 đến 100!", txtPhanTramGiam);
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Phần trăm giảm phải là số nguyên!", txtPhanTramGiam);
        return false;
    }
}


        try {
            int sl = Integer.parseInt(txtSoLuong.getText().trim());
            if (sl < 0) {
                showError("Số lượng phải >= 0!", txtSoLuong);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Số lượng phải là số nguyên!", txtSoLuong);
            return false;
        }

        String tongTienStr = txtTongTienToiThieu.getText().trim();
        if (!tongTienStr.isEmpty()) {
            try {
                BigDecimal tt = new BigDecimal(tongTienStr);
                if (tt.compareTo(BigDecimal.ZERO) < 0) {
                    showError("Tổng tiền tối thiểu phải >= 0!", txtTongTienToiThieu);
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("Tổng tiền tối thiểu phải là số hợp lệ!", txtTongTienToiThieu);
                return false;
            }
        }

        String soLuongSpStr = txtSoLuongSpToiThieu.getText().trim();
        if (!soLuongSpStr.isEmpty()) {
            try {
                int slsp = Integer.parseInt(soLuongSpStr);
                if (slsp < 0) {
                    showError("Số lượng SP tối thiểu phải >= 0!", txtSoLuongSpToiThieu);
                    return false;
                }
            } catch (NumberFormatException e) {
                showError("Số lượng SP tối thiểu phải là số nguyên!", txtSoLuongSpToiThieu);
                return false;
            }
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showError(String message, Component comp) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        comp.requestFocus();
    }

    private LocalDate getLocalDateFromDatePicker(JDatePickerImpl datePicker) {
        Date selected = (Date) datePicker.getModel().getValue();
        if (selected == null) return null;
        return selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

  public void setKhuyenMaiEdit(KhuyenMai km) {
    this.khuyenMaiEdit = km;
    if (km == null) return;

    txtMaKM.setText(km.getMaKhuyenMai());
    txtMaKM.setEditable(false); // Khóa mã khi sửa
    txtTenKM.setText(km.getTenKhuyenMai());

    if (km.getNgayBatDau() != null) {
        java.util.Date date = new java.util.Date(km.getNgayBatDau().getTime());
        datePickerNgayBatDau.getModel().setDate(
                date.getYear() + 1900,
                date.getMonth(),
                date.getDate());
        datePickerNgayBatDau.getModel().setSelected(true);
    } else {
        datePickerNgayBatDau.getModel().setSelected(false);
    }

    if (km.getNgayKetThuc() != null) {
        java.util.Date date = new java.util.Date(km.getNgayKetThuc().getTime());
        datePickerNgayKetThuc.getModel().setDate(
                date.getYear() + 1900,
                date.getMonth(),
                date.getDate());
        datePickerNgayKetThuc.getModel().setSelected(true);
    } else {
        datePickerNgayKetThuc.getModel().setSelected(false);
    }

    txtPhanTramGiam.setText(String.valueOf(km.getPhanTramGiam()));
    txtSoLuong.setText(String.valueOf(km.getSoLuong()));

    txtTongTienToiThieu.setText(
            km.getTongTienToiThieu() == null ? "" : km.getTongTienToiThieu().toString());

    txtSoLuongSpToiThieu.setText(
            km.getSoLuongSpToiThieu() == null ? "" : km.getSoLuongSpToiThieu().toString());

    txtGhiChu.setText(km.getGhiChu());
}



     private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");
        org.jdatepicker.impl.JDatePanelImpl datePanel = new org.jdatepicker.impl.JDatePanelImpl(model, p);
return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

   public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws java.text.ParseException {
        if (text == null || text.isEmpty()) return null;
        return dateFormat.parse(text);
    }

    @Override
    public String valueToString(Object value) throws java.text.ParseException {
        if (value != null) {
            if (value instanceof java.util.GregorianCalendar) {
                java.util.GregorianCalendar cal = (java.util.GregorianCalendar) value;
                java.util.Date date = cal.getTime(); // chuyển Calendar -> Date
                return dateFormat.format(date);
            } else if (value instanceof java.util.Date) {
                java.util.Date date = (java.util.Date) value;
                return dateFormat.format(date);
            }
        }
        return "";
    }
}
}
