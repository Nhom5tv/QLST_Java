package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ChamCongView extends JPanel {
    private JTextField txtSearch;
    private JComboBox<String> cbThang, cbNam;
    private JButton btnCheckIn, btnCheckOut, btnSua, btnXoa, btnExport;
    private JTable table;
    private DefaultTableModel tableModel;

    // Các trường nhập liệu cho controller dùng
    private JTextField txtMaChamCong, txtMaNv, txtNgay, txtCheckIn, txtCheckOut, txtGhiChu;

    public ChamCongView() {
        setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 14);

        // ========== TOP PANEL ==========
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.decode("#f0f4f7"));

        txtSearch = new JTextField(15);
        txtSearch.setFont(font);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(5, 10, 5, 10)
        ));

        cbThang = new JComboBox<>();
        cbThang.addItem("Tất cả");
        for (int i = 1; i <= 12; i++) cbThang.addItem("Tháng " + i);
        cbThang.setFont(font);

        cbNam = new JComboBox<>();
        cbNam.addItem("Tất cả");
        cbNam.addItem("2024");
        cbNam.addItem("2025");
        cbNam.setFont(font);

        btnCheckIn = createStyledButton("Check In", new Color(46, 204, 113));
        btnCheckOut = createStyledButton("Check Out", new Color(52, 152, 219));
        btnSua = createStyledButton("Sửa", new Color(241, 196, 15));
        btnXoa = createStyledButton("Xóa", new Color(231, 76, 60));
        btnExport = createStyledButton("Xuất Excel", new Color(155, 89, 182));

        topPanel.add(new JLabel("🔍"));
        topPanel.add(txtSearch);
        topPanel.add(new JLabel("Tháng:"));
        topPanel.add(cbThang);
        topPanel.add(new JLabel("Năm:"));
        topPanel.add(cbNam);
        topPanel.add(btnCheckIn);
        topPanel.add(btnCheckOut);
        topPanel.add(btnSua);
        topPanel.add(btnXoa);
        topPanel.add(btnExport);

        add(topPanel, BorderLayout.NORTH);

        // ========== FORM PANEL ==========
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMaChamCong = new JTextField(); txtMaChamCong.setEnabled(false);
        txtMaNv = new JTextField();
        txtNgay = new JTextField();
        txtCheckIn = new JTextField();
        txtCheckOut = new JTextField();
        txtGhiChu = new JTextField();

        formPanel.add(new JLabel("Mã CC:")); formPanel.add(txtMaChamCong);
        formPanel.add(new JLabel("Mã NV:")); formPanel.add(txtMaNv);
        formPanel.add(new JLabel("Ngày:")); formPanel.add(txtNgay);

        formPanel.add(new JLabel("Check In:")); formPanel.add(txtCheckIn);
        formPanel.add(new JLabel("Check Out:")); formPanel.add(txtCheckOut);
        formPanel.add(new JLabel("Ghi chú:")); formPanel.add(txtGhiChu);

        add(formPanel, BorderLayout.SOUTH);

        // ========== TABLE ==========
        String[] columns = {"Mã CC", "Mã NV", "Ngày", "Check In", "Check Out", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(font);
        table.setRowHeight(28);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(Color.decode("#dfe6e9"));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(100, 32));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }

    // ===== GETTERS =====
    public JTextField getTxtSearch() { return txtSearch; }
    public JComboBox<String> getCbThang() { return cbThang; }
    public JComboBox<String> getCbNam() { return cbNam; }
    public JButton getBtnCheckIn() { return btnCheckIn; }
    public JButton getBtnCheckOut() { return btnCheckOut; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public JTextField getTxtMaChamCong() { return txtMaChamCong; }
    public JTextField getTxtMaNv() { return txtMaNv; }
    public JTextField getTxtNgay() { return txtNgay; }
    public JTextField getTxtCheckIn() { return txtCheckIn; }
    public JTextField getTxtCheckOut() { return txtCheckOut; }
    public JTextField getTxtGhiChu() { return txtGhiChu; }
    public JButton getBtnExport() { return btnExport; }
}
