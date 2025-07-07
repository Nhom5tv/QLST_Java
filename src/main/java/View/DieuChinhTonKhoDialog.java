package View;

import javax.swing.*;
import java.awt.*;

public class DieuChinhTonKhoDialog extends JDialog {
    
    private JLabel lblTenSP, lblMaLo, lblHSD;
    private JSpinner spnTong, spnTrenKe, spnTrongKho, spnKhaDung, spnDangGiao,spnNguongCanhBao;
    private JButton btnXacNhan, btnHuy;

    public DieuChinhTonKhoDialog(JFrame parent) {
        super(parent, "🛠 Điều chỉnh tồn kho", true);
        setSize(420, 450);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        panel.add(createLabelRow("Tên sản phẩm:", lblTenSP = new JLabel()));
        panel.add(createLabelRow("Mã lô hàng:", lblMaLo = new JLabel()));
        panel.add(createLabelRow("Hạn sử dụng:", lblHSD = new JLabel()));

        panel.add(Box.createVerticalStrut(10));

        panel.add(createSpinnerRow("Tồn tổng:", spnTong = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1))));
        panel.add(createSpinnerRow("Trên kệ:", spnTrenKe = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1))));
        panel.add(createSpinnerRow("Trong kho:", spnTrongKho = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1))));
        panel.add(createSpinnerRow("Khả dụng:", spnKhaDung = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1))));
        panel.add(createSpinnerRow("Đang giao dịch:", spnDangGiao = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1))));
        panel.add(createSpinnerRow("Ngưỡng cảnh báo:", spnNguongCanhBao = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1))));

        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnXacNhan = new JButton("✅ Xác nhận");
        btnHuy = new JButton("❌ Huỷ");
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);

        panel.add(buttonPanel);
        add(panel);
    }

    private JPanel createLabelRow(String labelText, JLabel label) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(120, 30));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        row.add(lbl, BorderLayout.WEST);
        row.add(label, BorderLayout.CENTER);
        return row;
    }

    private JPanel createSpinnerRow(String labelText, JSpinner spinner) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(120, 30));
        spinner.setPreferredSize(new Dimension(100, 30));
        row.add(lbl, BorderLayout.WEST);
        row.add(spinner, BorderLayout.CENTER);
        return row;
    }

    public void setThongTin(String tenSP, String maLo, String hsd) {
        lblTenSP.setText(tenSP);
        lblMaLo.setText(maLo);
        lblHSD.setText(hsd);
    }

    public void setGiaTriTonKho(int tong, int trenKe, int trongKho, int khaDung, int dangGiao) {
        spnTong.setValue(tong);
        spnTrenKe.setValue(trenKe);
        spnTrongKho.setValue(trongKho);
        spnKhaDung.setValue(khaDung);
        spnDangGiao.setValue(dangGiao);
    }

    public int getTong() { return (int) spnTong.getValue(); }
    public int getTrenKe() { return (int) spnTrenKe.getValue(); }
    public int getTrongKho() { return (int) spnTrongKho.getValue(); }
    public int getKhaDung() { return (int) spnKhaDung.getValue(); }
    public int getDangGiao() { return (int) spnDangGiao.getValue(); }
    public void setNguongCanhBao(int nguong) {
        spnNguongCanhBao.setValue(nguong);
    }

    public int getNguongCanhBao() {
        return (int) spnNguongCanhBao.getValue();
    }


    public JButton getBtnXacNhan() { return btnXacNhan; }
    public JButton getBtnHuy() { return btnHuy; }
}
