/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoHangView extends JPanel {
    private JTable tableLoHang;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnXoa;
    private JTextField txtSearch;

    public LoHangView() {
        setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo mã, tên sản phẩm...");

        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(50, 205, 50));
        btnThem.setForeground(Color.WHITE);
        btnXoa = new JButton("❌ Xóa");
        btnXoa.setBackground(new Color(255, 0, 0));
        btnXoa.setForeground(Color.WHITE);
        for (JButton btn : new JButton[]{btnThem, btnXoa}) {
            btn.setPreferredSize(new Dimension(100, 35));
        }

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.setBackground(Color.WHITE);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        searchPanel.setBackground(Color.WHITE);

        // ==== Table Setup ====
        String[] columnNames = {"", "Mã Lô Hàng", "Mã Phiếu Nhập", "Tên Sản Phẩm", "Số Lượng","Đơn Giá","Ngày Sản Xuất", "Hạn Sử Dụng","Ghi Chú"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        tableLoHang = new JTable(tableModel);
        tableLoHang.setRowHeight(28);
        tableLoHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableLoHang.setSelectionBackground(new Color(220, 240, 255));
        tableLoHang.setShowGrid(false);
        tableLoHang.setIntercellSpacing(new Dimension(2, 2));

        JTableHeader header = tableLoHang.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        // Zebra stripe rendering
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(row % 2 == 0 ? new Color(127, 255, 0) : Color.WHITE);
                c.setFont(table.getSelectedRow() == row ? c.getFont().deriveFont(Font.BOLD) : c.getFont().deriveFont(Font.PLAIN));
                return c;
            }
        };

        for (int i = 1; i < tableLoHang.getColumnCount(); i++) {
            tableLoHang.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        tableLoHang.getColumnModel().getColumn(0).setMaxWidth(40);
        tableLoHang.getColumnModel().getColumn(0).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerCheckBox);

        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = header.columnAtPoint(e.getPoint());
                if (col == 0) {
                    boolean selectAll = !headerCheckBox.isSelected();
                    headerCheckBox.setSelected(selectAll);
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        tableModel.setValueAt(selectAll, i, 0);
                    }
                    tableLoHang.clearSelection();
                    header.repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableLoHang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }

    // Getters for controller
    public String getSearchText() {
        return txtSearch.getText().trim();
    }

    public JTable getTable() {
        return tableLoHang;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void addAddListener(ActionListener listener) {
        btnThem.addActionListener(listener);
    }

    public void addDeleteListener(ActionListener listener) {
        btnXoa.addActionListener(listener);
    }

    public void addSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }
}
