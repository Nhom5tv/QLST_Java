/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NhanVienView extends JPanel {
    public JList listNhanVien;
    public DefaultListModel listModel;
    public JLabel lblImage;

    public JTextField txtId, txtHoten, txtCCCD, txtSDT, txtChucvu, txtEmail;
    public JComboBox<String> cbGioitinh;
    public JTextArea txtGhichu;
    public JButton btnAdd, btnUpdate, btnDelete, btnChonAnh;

    public NhanVienView() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 500));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Panel trái
        JPanel leftPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel();
        listNhanVien = new JList(listModel);
        JScrollPane scrollList = new JScrollPane(listNhanVien);
        scrollList.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setPreferredSize(new Dimension(250, 200));
        lblImage.setBorder(BorderFactory.createTitledBorder("Ảnh nhân viên"));

        leftPanel.add(scrollList, BorderLayout.CENTER);
        leftPanel.add(lblImage, BorderLayout.SOUTH);

        // Panel phải
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField();
        txtId.setEnabled(false);

        txtHoten = new JTextField();
        txtCCCD = new JTextField();
        txtSDT = new JTextField();
        txtChucvu = new JTextField();
        txtEmail = new JTextField();
        cbGioitinh = new JComboBox<>(new String[] {"Nam", "Nữ"});
        txtGhichu = new JTextArea(3, 20);
        JScrollPane scrollGhichu = new JScrollPane(txtGhichu);

        btnChonAnh = new JButton("Chọn ảnh");
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");

        rightPanel.add(createFormRow("Mã NV:", txtId));
        rightPanel.add(createFormRow("Họ tên:", txtHoten));
        rightPanel.add(createFormRow("CCCD:", txtCCCD));
        rightPanel.add(createFormRow("SĐT:", txtSDT));
        rightPanel.add(createFormRow("Chức vụ:", txtChucvu));
        rightPanel.add(createFormRow("Email:", txtEmail));
        rightPanel.add(createFormRow("Giới tính:", cbGioitinh));
        rightPanel.add(createFormRow("Ghi chú:", scrollGhichu));
        rightPanel.add(createFormRow("Ảnh:", btnChonAnh));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete);
        rightPanel.add(pnlButtons);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createFormRow(String label, Component comp) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(100, 25));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(comp, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return panel;
    }
}
