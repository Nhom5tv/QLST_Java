package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class XuatHangLenKeDialog extends JDialog {
    private JLabel lblTenSanPhamValue, lblMaLoValue, lblTrongKhoValue, lblMaxGioiHan;
    private JSpinner spnSoLuong;
    private JButton btnXacNhan, btnHuy;

    public XuatHangLenKeDialog(JFrame parent) {
        super(parent, "📦 Xuất hàng từ kho ra kệ", true);
        setSize(420, 260);
        setLocationRelativeTo(parent);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        lblTenSanPhamValue = new JLabel("[Tên sản phẩm]");
        lblMaLoValue = new JLabel("[Mã lô hàng]");
        lblTrongKhoValue = new JLabel("[Số lượng trong kho]");

        JPanel tenSPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tenSPPanel.add(new JLabel("Tên sản phẩm:"));
        tenSPPanel.add(lblTenSanPhamValue);

        JPanel maLoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maLoPanel.add(new JLabel("Mã lô hàng:"));
        maLoPanel.add(lblMaLoValue);

        JPanel khoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        khoPanel.add(new JLabel("Trong kho hiện tại:"));
        khoPanel.add(lblTrongKhoValue);

        JPanel soLuongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        soLuongPanel.add(new JLabel("Số lượng muốn xuất:"));
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        lblMaxGioiHan = new JLabel("(Tối đa 100)");
        lblMaxGioiHan.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMaxGioiHan.setForeground(Color.GRAY);
        soLuongPanel.add(spnSoLuong);
        soLuongPanel.add(lblMaxGioiHan);

        panel.add(tenSPPanel);
        panel.add(maLoPanel);
        panel.add(khoPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(soLuongPanel);

        btnXacNhan = new JButton("✅ Xác nhận");
        btnHuy = new JButton("❌ Huỷ");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnHuy);
        btnPanel.add(btnXacNhan);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    public void setThongTin(String tenSP, String maLo, int trongKho) {
        lblTenSanPhamValue.setText("[" + tenSP + "]");
        lblMaLoValue.setText("[" + maLo + "]");
        lblTrongKhoValue.setText("[" + trongKho + "]");
        spnSoLuong.setModel(new SpinnerNumberModel(1, 1, trongKho, 1));
        lblMaxGioiHan.setText("(Tối đa " + trongKho + ")");
    }

    public int getSoLuongMuonChuyen() {
        return (Integer) spnSoLuong.getValue();
    }

    public JButton getBtnXacNhan() {
        return btnXacNhan;
    }

    public JButton getBtnHuy() {
        return btnHuy;
    }
    public void addXacNhanListener(ActionListener listener) {
        btnXacNhan.addActionListener(listener);
    }

    public void addHuyListener(ActionListener listener) {
        btnHuy.addActionListener(listener);
    }

}
