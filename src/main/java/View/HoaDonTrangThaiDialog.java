/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.JDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HoaDonTrangThaiDialog extends JDialog {
    private JComboBox<String> comboTrangThai;
    private JButton btnCapNhat, btnHuy;
    private boolean confirmed = false;

    private static final List<String> TRANG_THAI_OPTIONS = Arrays.asList("Đang xử lý", "Đang giao", "Hoàn thành", "Hủy");

    public HoaDonTrangThaiDialog(JFrame parent, String title) {
        super(parent, title, true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 200);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTrangThai = new JLabel("Trạng thái mới:");
        comboTrangThai = new JComboBox<>(TRANG_THAI_OPTIONS.toArray(new String[0]));

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblTrangThai, gbc);

        gbc.gridx = 1;
        mainPanel.add(comboTrangThai, gbc);

        btnCapNhat = new JButton("Cập nhật"); 
        btnHuy = new JButton("Hủy");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCapNhat.addActionListener(e -> {
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

    public String getSelectedTrangThai() {
        return (String) comboTrangThai.getSelectedItem();
    }

    public void setSelectedTrangThai(String trangThai) {
        comboTrangThai.setSelectedItem(trangThai);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

