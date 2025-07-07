package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class nccviewframe extends JFrame {
    private JTextField txtnccid, txtnccname, txtphone, txtemail, txtdiachi;
    private JTable ncctable;
    private DefaultTableModel nccmodel;
    private JButton btnThem, btnXoa;
    private JTextField txtSearch;

    public nccviewframe() {
        setTitle("Quản Lý Nhà Cung Cấp");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Áp dụng FlatLaf
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

        JLabel lblSearch = new JLabel("Tìm kiếm: ");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // ==== Buttons Panel ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");

        for (JButton btn : new JButton[]{btnThem, btnXoa}) {
            btn.setPreferredSize(new Dimension(100, 35));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        // ==== Table Setup ====
        String[] columnNames = {"", "ID", "Tên NCC", "Người Đại Diện", "Email", "SĐT", "Địa chỉ"};
        nccmodel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        ncctable = new JTable(nccmodel);
        // Tạo renderer mặc định với màu chữ đen
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setForeground(Color.BLACK); // Đặt màu chữ thành đen
        return c;
    }
};

// Áp dụng renderer cho tất cả các cột (trừ cột checkbox)
for (int i = 1; i < ncctable.getColumnCount(); i++) {
    ncctable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
}

        ncctable.setRowHeight(28);
        ncctable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ncctable.setSelectionBackground(new Color(220, 240, 255));
        ncctable.setShowGrid(false);
        ncctable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = ncctable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        // Checkbox header
        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        ncctable.getColumnModel().getColumn(0).setMaxWidth(40);
        ncctable.getColumnModel().getColumn(1).setMaxWidth(80);

        // Renderer for checkbox in header
        ncctable.getColumnModel().getColumn(0).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerCheckBox);

        // Click listener for header checkbox
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = header.columnAtPoint(e.getPoint());
                if (col == 0) {
                    boolean selectAll = !headerCheckBox.isSelected();
                    headerCheckBox.setSelected(selectAll);
                    for (int i = 0; i < nccmodel.getRowCount(); i++) {
                        nccmodel.setValueAt(selectAll, i, 0);
                    }
                    header.repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(ncctable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==== Layout ====
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ===== GETTERS for Controller =====
    public String getSearchText() {
        return txtSearch.getText().trim();
    }

    public JTable getTable() {
        return ncctable;
    }

    public DefaultTableModel getTableModel() {
        return nccmodel;
    }

    public String getNccId() {
        return txtnccid != null ? txtnccid.getText().trim() : "";
    }

    public String getNccName() {
        return txtnccname != null ? txtnccname.getText().trim() : "";
    }

    public String getEmail() {
        return txtemail != null ? txtemail.getText().trim() : "";
    }

    public String getPhone() {
        return txtphone != null ? txtphone.getText().trim() : "";
    }

    public String getAddress() {
        return txtdiachi != null ? txtdiachi.getText().trim() : "";
    }

    // ===== ACTION LISTENERS =====
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
