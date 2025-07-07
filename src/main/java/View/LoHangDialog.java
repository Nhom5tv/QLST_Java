/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author DUNG LE
 */
import Model.LoHang;
import Model.PhieuNhap;
import Model.SanPham;
import com.formdev.flatlaf.FlatLightLaf;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class LoHangDialog extends JDialog {

    private JComboBox<PhieuNhap> comboPhieuNhap;
    private JComboBox<SanPham> comboSanPham;
    private JTextField txtSoLuong, txtDonGia, txtGhiChu;
    private JDatePickerImpl dateNSX, dateHSD;
    private JButton btnLuu, btnHuy;
    private boolean confirmed = false;

    public LoHangDialog(JFrame parent, String title) {
        super(parent, title, true);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 480);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboPhieuNhap = new JComboBox<>();
        comboSanPham = new JComboBox<>();
        txtSoLuong = new JTextField();
        txtDonGia = new JTextField();
        txtGhiChu = new JTextField();

        dateNSX = createDatePicker();
        dateHSD = createDatePicker();

        JLabel[] labels = {
            new JLabel("Phiếu nhập:"),
            new JLabel("Sản phẩm:"),
            new JLabel("Số lượng:"),
            new JLabel("Đơn giá:"),
            new JLabel("Ngày sản xuất:"),
            new JLabel("Hạn sử dụng:"),
            new JLabel("Ghi chú:")
        };

        JComponent[] fields = {
            comboPhieuNhap,
            comboSanPham,
            txtSoLuong,
            txtDonGia,
            dateNSX,
            dateHSD,
            txtGhiChu
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(labels[i], gbc);
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        // Buttons
        btnLuu = new JButton("💾 Lưu");
        btnHuy = new JButton("❌ Hủy");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.add(btnLuu);
        btnPanel.add(btnHuy);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnHuy.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmed = false;
            }
        });
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Calendar cal = Calendar.getInstance();
        model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        model.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    // === GETTERS ===

    public String getPhieuNhap() {
//        return (PhieuNhap) comboPhieuNhap.getSelectedItem();
        PhieuNhap selected = (PhieuNhap) comboPhieuNhap.getSelectedItem();
        return selected != null ? selected.getMaPhieu(): "";
    }

    public int getSanPham() {
    SanPham selected = (SanPham) comboSanPham.getSelectedItem();
    return selected != null ? selected.getMaSanPham() : -1; // hoặc 0 tùy bạn
}


    public int getSoLuong() {
        return Integer.parseInt(txtSoLuong.getText().trim());
    }

    public double getDonGia() {
        return Double.parseDouble(txtDonGia.getText().trim());
    }

    public Date getNgaySanXuat() {
        return (Date) dateNSX.getModel().getValue();
    }

    public Date getHanSuDung() {
        return (Date) dateHSD.getModel().getValue();
    }

    public String getGhiChu() {
        return txtGhiChu.getText().trim();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    // === SETTER COMBOBOX ===

    public void setComboPhieuNhap(List<PhieuNhap> list) {
        comboPhieuNhap.removeAllItems();
        for (PhieuNhap pn : list) comboPhieuNhap.addItem(pn);
    }

    public void setComboSanPham(List<SanPham> list) {
        comboSanPham.removeAllItems();
        for (SanPham sp : list) comboSanPham.addItem(sp);
    }

    // === Formatter nội bộ không tách ra ===
    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
    public void setf(String maPhieuNhap, String tenSanPham, int soLuong, double donGia,
                 String ngaySXStr, String hanSDStr, String ghiChu) {
    // ✅ chọn lại item combo box đúng với dữ liệu
    selectPhieuNhap(maPhieuNhap);
    selectSanPham(tenSanPham); // hoặc dùng mã sản phẩm nếu có

    txtSoLuong.setText(String.valueOf(soLuong));
    txtDonGia.setText(String.valueOf(donGia));
    txtGhiChu.setText(ghiChu);

    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nsx = sdf.parse(ngaySXStr);
        Date hsd = sdf.parse(hanSDStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(nsx);
        dateNSX.getModel().setDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        );
        dateNSX.getModel().setSelected(true);
        cal.setTime(hsd);
        dateHSD.getModel().setDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        );
        dateHSD.getModel().setSelected(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void selectPhieuNhap(String maPhieuNhap) {
    for (int i = 0; i < comboPhieuNhap.getItemCount(); i++) {
        PhieuNhap pn = comboPhieuNhap.getItemAt(i);
        if (pn.getMaPhieu().equals(maPhieuNhap)) {
            comboPhieuNhap.setSelectedIndex(i);
            break;
        }
    }
}

private void selectSanPham(String tenSanPham) {
    for (int i = 0; i < comboSanPham.getItemCount(); i++) {
        SanPham sp = comboSanPham.getItemAt(i);
        if (sp.getTenSanPham().equalsIgnoreCase(tenSanPham)) {
            comboSanPham.setSelectedIndex(i);
            break;
        }
    }
}

}

