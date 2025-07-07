package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;

public class NhanVienView1 extends JPanel {
    private JTable nvTable;
    private DefaultTableModel nvModel;
    private JButton btnThem, btnXoa;
    private JTextField txtSearch;

    public NhanVienView1() {
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
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                txtSearch.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblSearch = new JLabel("🔍 Tìm Kiếm:");
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo tên nhân viên");

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

        // Cột dữ liệu (thêm cột "Ảnh")
        String[] columnNames = {"", "Mã NV", "Họ tên", "CCCD", "SĐT", "Chức vụ", "Email", "Giới tính", "Ghi chú", "Ảnh"};
        nvModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                if (column == 10) return ImageIcon.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        nvTable = new JTable(nvModel);
        nvTable.setRowHeight(60); // Tăng chiều cao để hiển thị ảnh
        nvTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nvTable.setSelectionBackground(new Color(220, 240, 255));
        nvTable.setShowGrid(false);
        nvTable.setIntercellSpacing(new Dimension(2, 2));

        JTableHeader header = nvTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(row % 2 == 0 ? new Color(220, 255, 200) : Color.WHITE);
                c.setFont(c.getFont().deriveFont(table.getSelectedRow() == row ? Font.BOLD : Font.PLAIN));
                return c;
            }
        };

        // Renderer cho các cột văn bản
        for (int i = 1; i < nvTable.getColumnCount() - 1; i++) {
            nvTable.getColumnModel().getColumn(i).setCellRenderer(textRenderer);
        }

        // Renderer cho cột ảnh
        nvTable.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel lbl = new JLabel();
                    lbl.setHorizontalAlignment(JLabel.CENTER);
                    lbl.setIcon((ImageIcon) value);
                    return lbl;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        // Checkbox chọn hàng
        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        nvTable.getColumnModel().getColumn(0).setMaxWidth(40);
        nvTable.getColumnModel().getColumn(0).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerCheckBox);

        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (header.columnAtPoint(e.getPoint()) == 0) {
                    boolean selected = !headerCheckBox.isSelected();
                    headerCheckBox.setSelected(selected);
                    for (int i = 0; i < nvModel.getRowCount(); i++) {
                        nvModel.setValueAt(selected, i, 0);
                    }
                    nvTable.clearSelection();
                    header.repaint();
                }
            }
        });

        nvTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        nvModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (e.getColumn() == 0) {
                Boolean checked = (Boolean) nvModel.getValueAt(row, 0);
                if (checked != null && checked) {
                    nvTable.setRowSelectionInterval(row, row);
                } else {
                    nvTable.removeRowSelectionInterval(row, row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(nvTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }

    // Public Getters
    public JTable getTable() {
        return nvTable;
    }

    public DefaultTableModel getTableModel() {
        return nvModel;
    }

    public String getSearchText() {
        return txtSearch.getText().trim();
    }

    public void addAddListener(java.awt.event.ActionListener listener) {
        btnThem.addActionListener(listener);
    }

    public void addDeleteListener(java.awt.event.ActionListener listener) {
        btnXoa.addActionListener(listener);
    }

    public void addSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }

    // Tiện ích: thu nhỏ ảnh
    public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
