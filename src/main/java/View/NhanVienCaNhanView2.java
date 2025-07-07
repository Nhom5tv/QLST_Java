/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import model.NhanVien;

/**
 *
 * @author banhb
 */
public class NhanVienCaNhanView2 extends JPanel {
    private JTextField txtId, txtHoTen, txtCCCD, txtSDT, txtChucVu, txtEmail;
    private JRadioButton rbMr, rbMs;
    private JTextArea txtGhiChu;
    private JLabel lblAnh;
    private JButton btnChonAnh, btnCapNhat, btnHuy;

    public NhanVienCaNhanView2() {
        setLayout(new BorderLayout(20, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        setBackground(Color.WHITE);

        // ==== COMPONENTS ====
        txtId = new JTextField(20); txtId.setEditable(false); txtId.setBackground(Color.LIGHT_GRAY);
        txtHoTen = new JTextField(20); txtHoTen.setEditable(false); txtHoTen.setBackground(Color.LIGHT_GRAY);
        txtCCCD = new JTextField(20); txtCCCD.setEditable(false); txtCCCD.setBackground(Color.LIGHT_GRAY);
        txtSDT = new JTextField(20);
        txtChucVu = new JTextField(20); txtChucVu.setEditable(false); txtChucVu.setBackground(Color.LIGHT_GRAY);
        txtEmail = new JTextField(20);
        rbMr = new JRadioButton("Nam"); rbMr.setBackground(Color.WHITE);
        rbMs = new JRadioButton("Nữ"); rbMs.setBackground(Color.WHITE);
        txtGhiChu = new JTextArea(3, 20);
        JScrollPane ghiChuScroll = new JScrollPane(txtGhiChu);

        lblAnh = new JLabel("Photo", SwingConstants.CENTER);
        lblAnh.setPreferredSize(new Dimension(100, 50));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnChonAnh = new JButton("Choose"); btnChonAnh.setBackground(Color.WHITE);

        btnCapNhat = new JButton("Save");
        btnCapNhat.setBackground(new Color(0, 120, 215));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFocusPainted(false);

        btnHuy = new JButton("Cancel");
        btnHuy.setBackground(Color.WHITE);
        btnHuy.setForeground(Color.BLACK);

        // ==== FORM PANEL ====
        JPanel gioitinhPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        gioitinhPanel.setBackground(Color.WHITE);
        gioitinhPanel.add(rbMr);
        gioitinhPanel.add(rbMs);
        ButtonGroup bggioitinh = new ButtonGroup();
        bggioitinh.add(rbMr);
        bggioitinh.add(rbMs);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addRow(formPanel, gbc, row++, "Mã nhân viên:", txtId);
        addRow(formPanel, gbc, row++, "Họ và tên:", txtHoTen);
        addRow(formPanel, gbc, row++, "CCCD:", txtCCCD);
        addRow(formPanel, gbc, row++, "Số điện thoại:", txtSDT);
        addRow(formPanel, gbc, row++, "Chức vụ:", txtChucVu);
        addRow(formPanel, gbc, row++, "Email:", txtEmail);
        addRow(formPanel, gbc, row++, "Giới tính:", gioitinhPanel);
        addRow(formPanel, gbc, row++, "Ghi chú:", ghiChuScroll);

        // ==== IMAGE PANEL ====
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(lblAnh, BorderLayout.CENTER);
        imagePanel.add(btnChonAnh, BorderLayout.SOUTH);

        // ==== BUTTON PANEL ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        btnCapNhat.setPreferredSize(new Dimension(90, 30));
        btnHuy.setPreferredSize(new Dimension(90, 30));
        buttonPanel.add(btnCapNhat);
        //buttonPanel.add(btnHuy);

        // ==== ADD TO MAIN PANEL ====
        add(formPanel, BorderLayout.CENTER);
//        add(imagePanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // ==== GETTERS ====
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtHoTen() { return txtHoTen; }
    public JTextField getTxtCCCD() { return txtCCCD; }
    public JTextField getTxtSDT() { return txtSDT; }
    public JTextField getTxtChucVu() { return txtChucVu; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextArea getTxtGhiChu() { return txtGhiChu; }
    public JLabel getLblAnh() { return lblAnh; }
    public void setThongTinNhanVien(NhanVien nv) {
    txtId.setText(String.valueOf(nv.getma_nv()));
    txtHoTen.setText(nv.getHoten());
    txtCCCD.setText(nv.getCccd());
    txtSDT.setText(nv.getSdt());
    txtChucVu.setText(nv.getChucvu());
    txtEmail.setText(nv.getEmail());
    txtGhiChu.setText(nv.getGhichu());
    if (nv.getGioitinh().equalsIgnoreCase("Nam")) {
        rbMr.setSelected(true);
    } else if (nv.getGioitinh().equalsIgnoreCase("Nữ")) {
        rbMs.setSelected(true);
    }
}
    public JButton getBtnCapNhat() { return btnCapNhat; }
    public JRadioButton getRbMr() {return rbMr;}
}
