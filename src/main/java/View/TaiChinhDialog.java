package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField.AbstractFormatter;

import com.formdev.flatlaf.FlatLightLaf;
import model.TaiChinh;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import Model.NhanVien;

public class TaiChinhDialog extends JDialog {

    private JTextField txtMaGiaoDich, txtSoTien;
    private JTextArea txtMoTa;
    private JComboBox<String> cbLoaiGiaoDich;
    private JComboBox<NhanVien> cbNhanVien;
    private JDatePickerImpl datePicker;
    private JButton btnLuu, btnHuy;
     private boolean isSucceeded = false; 
     public boolean isSucceeded() {
    return isSucceeded;
}
// Thêm biến này để báo trạng thái thao tác thành công hay chưa


    public TaiChinhDialog(Frame owner, List<NhanVien> dsNhanVien) {
        super(owner, "Quản lý Tài Chính", true);
        initLookAndFeel();
        initComponents(dsNhanVien);
        initEvents();
        setSize(480, 420);
        setLocationRelativeTo(owner);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}
    }

    private void initComponents(List<NhanVien> dsNhanVien) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các thành phần
        txtMaGiaoDich = new JTextField();
        txtMaGiaoDich.setEditable(false);

        txtSoTien = new JTextField();

        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane moTaScroll = new JScrollPane(txtMoTa);

        cbLoaiGiaoDich = new JComboBox<>(new String[]{"Thu", "Chi"});

        cbNhanVien = new JComboBox<>();
        if (dsNhanVien != null) {
            for (NhanVien nv : dsNhanVien) {
                cbNhanVien.addItem(nv);
            }
        }
        cbNhanVien.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            Component c = new DefaultListCellRenderer().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof NhanVien nv) {
                ((JLabel) c).setText(nv.getHoten());
            }
            return c;
        });

        datePicker = createDatePicker();

        String[] labels = {"Mã Giao dịch:", "Ngày:", "Loại giao dịch:", "Số tiền:", "Mô tả:", "Nhân viên:"};
        Component[] components = {txtMaGiaoDich, datePicker, cbLoaiGiaoDich, txtSoTien, moTaScroll, cbNhanVien};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            formPanel.add(components[i], gbc);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");

        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnHuy.setBackground(new Color(231, 76, 60));
        btnHuy.setForeground(Color.WHITE);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnLuu);
        btnPanel.add(btnHuy);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void initEvents() {
        btnHuy.addActionListener(e -> {
            isSucceeded = false;  // Hủy thì báo không thành công
            dispose();
        });

        btnLuu.addActionListener(e -> {
            if (validateForm()) {
                isSucceeded = true;   // Cài cờ thành công khi dữ liệu hợp lệ
                dispose();            // Đóng dialog
            }
        });
    }

    private boolean validateForm() {
        if (getLocalDateFromDatePicker() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giao dịch.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String soTienStr = txtSoTien.getText().trim();
        if (soTienStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            BigDecimal soTien = new BigDecimal(soTienStr);
            if (soTien.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cbNhanVien.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private LocalDate getLocalDateFromDatePicker() {
        Date date = (Date) datePicker.getModel().getValue();
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public TaiChinh getGiaoDichFromForm() {
        TaiChinh gd = new TaiChinh();
        try {
            gd.setMaGiaoDich(!txtMaGiaoDich.getText().isBlank() ? Integer.parseInt(txtMaGiaoDich.getText().trim()) : 0);


            gd.setNgay(java.sql.Date.valueOf(getLocalDateFromDatePicker()));
            gd.setLoaiGiaoDich(cbLoaiGiaoDich.getSelectedItem().toString());
            gd.setSoTien(new BigDecimal(txtSoTien.getText().trim()));
            gd.setMoTa(txtMoTa.getText().trim());
            gd.setNhanVien((NhanVien) cbNhanVien.getSelectedItem());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu từ form!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return gd;
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
   public void setTaiChinhEdit(TaiChinh tc) {
    if (tc == null) {
        txtMaGiaoDich.setText("");
        datePicker.getModel().setValue(null);
        cbLoaiGiaoDich.setSelectedIndex(0);
        txtSoTien.setText("");
        txtMoTa.setText("");
        cbNhanVien.setSelectedIndex(-1);
    } else {
        txtMaGiaoDich.setText(String.valueOf(tc.getMaGiaoDich()));
        // Thiết lập ngày cho datePicker
        if (tc.getNgay() != null) {
            java.util.Date date = new java.util.Date(tc.getNgay().getTime());
            datePicker.getModel().setDate(date.getYear() + 1900, date.getMonth(), date.getDate());
            datePicker.getModel().setSelected(true);
        } else {
            datePicker.getModel().setSelected(false);
        }
        cbLoaiGiaoDich.setSelectedItem(tc.getLoaiGiaoDich());
        txtSoTien.setText(tc.getSoTien().toString());
        txtMoTa.setText(tc.getMoTa());

        // Chọn nhân viên trong combobox theo đối tượng
        NhanVien nv = tc.getNhanVien();
        if (nv != null) {
            for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                if (cbNhanVien.getItemAt(i). getma_nv() == nv. getma_nv()) {
                    cbNhanVien.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            cbNhanVien.setSelectedIndex(-1);
        }
    }
}

public TaiChinh getTaiChinh() {
    return getGiaoDichFromForm();
}



}
