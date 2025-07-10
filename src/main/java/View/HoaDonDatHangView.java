/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.JPanel;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 *
 * @author Admin
 */
public class HoaDonDatHangView extends JPanel{
    private JTable tableHoaDon;
    private DefaultTableModel tableModel;
    private JButton btnCapNhat;
    private JTextField txtSearch;
    public HoaDonDatHangView(){
        setLayout(new BorderLayout());
        initUI();
    }
    private void initUI() {
        // ==== Search Panel ====
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                txtSearch.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo mã hóa đơn, ngày tạo");

        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnCapNhat = new JButton("\u2705 Cập nhật trạng thái");
        btnCapNhat.setBackground(new Color(30, 144, 255));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setPreferredSize(new Dimension(180, 35));

        buttonPanel.add(btnCapNhat);
        buttonPanel.setBackground(Color.WHITE);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        searchPanel.setBackground(Color.WHITE);

        // ==== Table Setup ====
        String[] columnNames = {"Mã Hóa Đơn", "Mã Khách Hàng", "Ngày Tạo", "Tổng Tiền", "Mã KM", "Trạng Thái"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 1 -> Integer.class;
                    case 2 -> String.class;
                    case 3 -> Double.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô
            }
        };

        tableHoaDon = new JTable(tableModel);
        tableHoaDon.setRowHeight(28);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHoaDon.setSelectionBackground(new Color(220, 240, 255));
        tableHoaDon.setShowGrid(false);
        tableHoaDon.setIntercellSpacing(new Dimension(2, 2));

        JTableHeader header = tableHoaDon.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(row % 2 == 0 ? new Color(240, 255, 240) : Color.WHITE);
                c.setFont(table.getSelectedRow() == row
                        ? c.getFont().deriveFont(Font.BOLD)
                        : c.getFont().deriveFont(Font.PLAIN));
                return c;
            }
        };

        for (int i = 0; i < tableHoaDon.getColumnCount(); i++) {
            tableHoaDon.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableHoaDon);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }
    // === Getters ===
    public String getSearchText() {
        return txtSearch.getText().trim();
    }

    public JTable getTable() {
        return tableHoaDon;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void addUpdateStatusListener(ActionListener listener) {
        btnCapNhat.addActionListener(listener);
    }

    public void addSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }
    public void addRowDoubleClickListener(java.awt.event.MouseListener listener) {
        tableHoaDon.addMouseListener(listener);
    }
}
