package View;

import Model.NhaCungCap;
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


public class PhieuNhapDialog extends JDialog {

    private JComboBox<NhaCungCap> comboNCC;
    private JTextField txtGhiChu;
    private JButton btnLuu, btnHuy;
    private boolean confirmed = false;
    private JDatePickerImpl datePicker;

    public PhieuNhapDialog(JFrame parent, String title) {
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
        setSize(420, 350);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
         comboNCC = new JComboBox<>();
        JLabel[] labels = {
            new JLabel("Nhà cung cấp:"),
            new JLabel("Ngày nhập (yyyy-MM-dd):"),
            new JLabel("Ghi chú:")
        };

        txtGhiChu = new JTextField();
        txtGhiChu.setPreferredSize(new Dimension(500, 30)); // Rộng 500px, cao 30px
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);

        // Khởi tạo Date Picker
        UtilDateModel model = new UtilDateModel();
        Calendar cal = Calendar.getInstance();
        model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        model.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Hôm nay");
        p.put("text.month", "Tháng");
        p.put("text.year", "Năm");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // Add components to layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(labels[0], gbc);
        gbc.gridx = 1;
        mainPanel.add(comboNCC, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(labels[1], gbc);
        gbc.gridx = 1;
        mainPanel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(labels[2], gbc);
        gbc.gridx = 1;
        mainPanel.add(scrollGhiChu, gbc);

        // Button Panel
        btnLuu = new JButton("💾 Lưu");
        btnHuy = new JButton("❌ Hủy");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        btnLuu.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnHuy.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmed = false;
            }
        });
    }

    public void setFields(String ncc, String ngayNhap, String ghiChu) {
        for (int i = 0; i < comboNCC.getItemCount(); i++) {
            NhaCungCap item = comboNCC.getItemAt(i);
            if (item.getNccid().equals(ncc)) {
                comboNCC.setSelectedIndex(i);
                break;
            }
        }

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(ngayNhap);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            datePicker.getModel().setDate(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
            datePicker.getModel().setSelected(true);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtGhiChu.setText(ghiChu);
    }

    public String getNgayNhap() {
        Date selectedDate = (Date) datePicker.getModel().getValue();
        if (selectedDate != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
        }
        return "";
    }
    public Date getDate() {
    return (Date) datePicker.getModel().getValue();
}


    public String getNCC() {
        NhaCungCap selected = (NhaCungCap) comboNCC.getSelectedItem();
        return selected != null ? selected.getNccid() : "";
    }

    public String getGhiChu() {
        return txtGhiChu.getText().trim();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setComboNCCOptions(List<NhaCungCap> nccList) {
        comboNCC.removeAllItems();
        for (NhaCungCap ncc : nccList) {
            comboNCC.addItem(ncc);
        }
    }

    // === Formatter nội bộ không tách ra ===
    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}

