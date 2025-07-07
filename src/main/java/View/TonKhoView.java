package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TonKhoView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnExcel, btnChuyenHang, btnDieuChinh, btnReset;
    private JComboBox<String> cboHSDFilter;
    private JCheckBox chkCanhBao;
    private JSpinner spnSoNgaySapHetHan;

    public TonKhoView() {
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
        // === DÒNG 1: Tìm kiếm + Excel ===
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo tên sản phẩm, mã lô");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                txtSearch.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblSearch = new JLabel("🔍");
        lblSearch.setPreferredSize(new Dimension(30, 35));
        lblSearch.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        btnExcel = new JButton("📊 Xuất Excel");
        btnExcel.setPreferredSize(new Dimension(110, 35));
        btnExcel.setBackground(new Color(30, 144, 255));
        btnExcel.setForeground(Color.WHITE);

        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnExcel, BorderLayout.EAST);

        // === DÒNG 2: Lọc HSD + cảnh báo + reset ===
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(Color.WHITE);

        cboHSDFilter = new JComboBox<>(new String[]{"Tất cả", "Còn hạn", "Hết hạn", "Sắp hết hạn"});
        spnSoNgaySapHetHan = new JSpinner(new SpinnerNumberModel(7, 1, 365, 1));
        chkCanhBao = new JCheckBox("Chỉ lọc sản phẩm sắp hết hàng");
        
        btnReset = new JButton("🔄 Reset");
        btnReset.setBackground(new Color(100, 149, 237));
        btnReset.setForeground(Color.WHITE);
        btnReset.setPreferredSize(new Dimension(100, 30));

        filterPanel.add(new JLabel("Lọc HSD:"));
        filterPanel.add(cboHSDFilter);
        filterPanel.add(spnSoNgaySapHetHan);
        filterPanel.add(new JLabel("ngày"));
        filterPanel.add(chkCanhBao);
        filterPanel.add(btnReset);

        // Gộp 2 dòng trên thành 1 khối
        JPanel topPanel = new JPanel(new BorderLayout(0, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        // === BẢNG TỒN KHO ===
        String[] columnNames = {
                "Mã SP", "Tên sản phẩm", "Mã lô", "HSD",
                "Tồn tổng", "Trên kệ", "Trong kho",
                "Khả dụng", "Đang giao dịch", "Cảnh báo"
        };

        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 4, 5, 6, 7, 8, 9 -> Integer.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int khaDung = (int) getValueAt(row, 7);
                int nguong = (int) getValueAt(row, 9);
                if (!isRowSelected(row)) {
                    if (khaDung < nguong) {
                        c.setBackground(new Color(255, 235, 230));
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(240, 255, 240) : Color.WHITE);
                    }
                } else {
                    c.setBackground(getSelectionBackground());
                }
                return c;
            }
        };

        TableColumn colMaSP = table.getColumnModel().getColumn(0);
        colMaSP.setMinWidth(0);
        colMaSP.setMaxWidth(0);
        colMaSP.setPreferredWidth(0);

        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(220, 240, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === PANEL DƯỚI: Chức năng ===
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JLabel lblHuongDan = new JLabel("Chọn dòng lô hàng trong bảng, sau đó nhấn nút bên dưới để thao tác.");
        lblHuongDan.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHuongDan.setForeground(Color.GRAY);
        lblHuongDan.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnChuyenHang = new JButton("📦 Xuất hàng ra kệ");
        btnDieuChinh = new JButton("🛠 Điều chỉnh tồn kho");
        bottomPanel.add(btnChuyenHang);
        bottomPanel.add(btnDieuChinh);

        actionPanel.add(lblHuongDan, BorderLayout.NORTH);
        actionPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
        setBackground(Color.WHITE);
    }

    // === GETTER ===
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public String getSearchText() { return txtSearch.getText().trim(); }
    public JButton getBtnExcel() { return btnExcel; }
    public JButton getBtnChuyenHang() { return btnChuyenHang; }
    public JButton getBtnDieuChinh() { return btnDieuChinh; }
    public JTextField getTxtSearch() { return txtSearch; }
    public JComboBox<String> getCboHSDFilter() { return cboHSDFilter; }
    public JCheckBox getChkCanhBao() { return chkCanhBao; }
    public JButton getBtnReset() { return btnReset; }
    public JSpinner getSpnSoNgaySapHetHan() { return spnSoNgaySapHetHan; }

    public void addSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }

    public void addExportExcelListener(ActionListener listener) {
        btnExcel.addActionListener(listener);
    }
}
