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

/**
 * @author Admin
 */
public class HoaDonTrangThaiDialog extends JDialog {
    private JComboBox<String> comboTrangThai;
    private JButton btnCapNhat, btnHuy;
    private boolean confirmed = false;

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
        comboTrangThai = new JComboBox<>();

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

    public void setCurrentStatus(String currentStatus) {
        // Xóa tất cả các item hiện có
        comboTrangThai.removeAllItems();

        // Thêm các trạng thái phù hợp dựa trên trạng thái hiện tại
        switch (currentStatus) {
            case "Đang xử lý":
                comboTrangThai.addItem("Đang giao");
                comboTrangThai.addItem("Hủy");
                break;
            case "Đang giao":
                comboTrangThai.addItem("Hoàn thành");
                comboTrangThai.addItem("Hủy");
                break;
            case "Hoàn thành":
            case "Hủy":
                // Không cho phép thay đổi trạng thái
                comboTrangThai.setEnabled(false);
                btnCapNhat.setEnabled(false);
                comboTrangThai.addItem(currentStatus);
                break;
            default:
                comboTrangThai.addItem("Đang xử lý");
                comboTrangThai.addItem("Đang giao");
                comboTrangThai.addItem("Hủy");
        }

        // Nếu không có lựa chọn nào, thêm trạng thái hiện tại
        if (comboTrangThai.getItemCount() == 0) {
            comboTrangThai.addItem(currentStatus);
        }
    }

    public String getSelectedTrangThai() {
        return (String) comboTrangThai.getSelectedItem();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}