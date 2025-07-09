/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author Admin
 */
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class KhachHangView extends JPanel {
    private JTextField txtMaKH, txtMaTK , txtHoTen, txtSDT, txtEmail, txtDiaChi;
    private JTable khTable;
    private DefaultTableModel khModel;
    private JButton btnThem, btnXoa, btnXuatExcel;
    private JTextField txtSearch;

    public KhachHangView() {
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
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm Kiếm Theo Tên Khách Hàng");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(50, 205, 50));
        btnThem.setForeground(Color.WHITE);
        btnXoa = new JButton("❌ Xóa");
        btnXoa.setBackground(new Color(255, 0, 0));
        btnXoa.setForeground(Color.WHITE);
        btnXuatExcel = new JButton("📊 Xuất Excel"); // Nút xuất Excel
        btnXuatExcel.setBackground(new Color(30, 144, 255));
        btnXuatExcel.setForeground(Color.WHITE);

        for (JButton btn : new JButton[]{btnThem, btnXoa, btnXuatExcel}) {
            btn.setPreferredSize(new Dimension(100, 35));
        }
//        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnXuatExcel);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setBackground(Color.WHITE);
        searchPanel.setBackground(Color.WHITE);

        // Table setup
        String[] columnNames = {"", "MaKH", "HoTen", "SoDienThoai", "DiaChi", "Email"};
        khModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        khTable = new JTable(khModel);
        khTable.setRowHeight(28);
        khTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        khTable.setSelectionBackground(new Color(220, 240, 255));
        khTable.setShowGrid(false);
        khTable.setIntercellSpacing(new Dimension(2, 2));

        JTableHeader header = khTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(row % 2 == 0 ? new Color(220, 255, 200) : Color.WHITE);
                c.setFont(c.getFont().deriveFont(table.getSelectedRow() == row ? Font.BOLD : Font.PLAIN));
                return c;
            }
        };

        for (int i = 1; i < khTable.getColumnCount(); i++) {
            khTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        khTable.getColumnModel().getColumn(0).setMaxWidth(40);
        khTable.getColumnModel().getColumn(0).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerCheckBox);

        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (header.columnAtPoint(e.getPoint()) == 0) {
                    boolean selected = !headerCheckBox.isSelected();
                    headerCheckBox.setSelected(selected);
                    for (int i = 0; i < khModel.getRowCount(); i++) {
                        khModel.setValueAt(selected, i, 0);
                    }
                    khTable.clearSelection();
                    header.repaint();
                }
            }
        });

        khTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        khModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (e.getColumn() == 0) {
                Boolean checked = (Boolean) khModel.getValueAt(row, 0);
                if (checked != null && checked) {
                    khTable.setRowSelectionInterval(row, row);
                } else {
                    khTable.removeRowSelectionInterval(row, row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(khTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }

    // Getters
    public JTable getTable() {
        return khTable;
    }

    public DefaultTableModel getTableModel() {
        return khModel;
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
    
    public void addExportListener(java.awt.event.ActionListener listener){
        btnXuatExcel.addActionListener(listener);
    }

    public void addSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
}
