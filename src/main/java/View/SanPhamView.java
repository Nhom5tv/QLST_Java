package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List; 

public class SanPhamView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnThem, btnXoa, btnExcel,btnResetFilter;
    private JComboBox<String> cboDanhMucFilter, cboTrangThaiFilter;

    public SanPhamView() {
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
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                txtSearch.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblSearch = new JLabel("\uD83D\uDD0D");
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo tên, mã sản phẩm, mã vạch");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(50, 205, 50));
        btnThem.setForeground(Color.WHITE);
        btnXoa = new JButton("❌ Xóa");
        btnXoa.setBackground(new Color(255, 0, 0));
        btnXoa.setForeground(Color.WHITE);
        btnExcel = new JButton("📊 Excel");
        btnExcel.setBackground(new Color(30, 144, 255));
        btnExcel.setForeground(Color.WHITE);

        for (JButton btn : new JButton[]{btnThem, btnXoa,btnExcel}) {
            btn.setPreferredSize(new Dimension(100, 35));
        }
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnExcel);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setBackground(Color.WHITE);
        searchPanel.setBackground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);
        JLabel lblFilter = new JLabel("Lọc theo");
        cboDanhMucFilter = new JComboBox();
        cboTrangThaiFilter = new JComboBox<>(new String[]{"Tất cả", "Đang bán", "Ngừng bán"});
        
        btnResetFilter = new JButton("🔄 Reset");
        btnResetFilter.setBackground(new Color(100, 149, 237));
        btnResetFilter.setForeground(Color.WHITE);
        btnResetFilter.setPreferredSize(new Dimension(100, 30));
        


        filterPanel.add(lblFilter);
        filterPanel.add(new JLabel("Danh mục:"));
        filterPanel.add(cboDanhMucFilter);
        filterPanel.add(new JLabel("Trạng thái:"));
        filterPanel.add(cboTrangThaiFilter);
       
        filterPanel.add(btnResetFilter);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        String[] columnNames = {"", "ID", "Ảnh", "Mã SP", "Tên sản phẩm", "Giá gốc", "Giá bán", "Đơn vị", "Tồn kho"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return Boolean.class; // Checkbox
                    case 2: return ImageIcon.class; // Ảnh
                    case 5:
                    case 6: return BigDecimal.class; // Giá gốc, Giá bán
                    case 8: return Integer.class;    // Tồn kho
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(50);
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(2, 2));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(row % 2 == 0 ? new Color(240, 255, 240) : Color.WHITE);
                c.setFont(c.getFont().deriveFont(table.getSelectedRow() == row ? Font.BOLD : Font.PLAIN));
                if (value != null && c instanceof JComponent) {
                    ((JComponent) c).setToolTipText(value.toString());
                }
                return c;
            }
        };
        
        for (int i = 1; i < table.getColumnCount(); i++) {
            if (i != 2) table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
         // ẩn cột id
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setWidth(0);
        // đặt kích thước cho cột ảnh
        table.getColumnModel().getColumn(2).setMaxWidth(60);
        
        table.getColumnModel().getColumn(0).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerCheckBox);
        // kích thước cột tên sản phẩm to ra
        table.getColumnModel().getColumn(4).setPreferredWidth(220);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }
    public void setDanhMucFilterOptions(List<String> list) {
        cboDanhMucFilter.removeAllItems();
        cboDanhMucFilter.addItem("Tất cả");
        for (String s : list) {
            cboDanhMucFilter.addItem(s);
        }
    }

   
    public JTable getTable() {
        return table;
    }
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    public String getSearchText() {
        return txtSearch.getText().trim();
    }
    public JTextField getSearchTextField() {
        return txtSearch;
    }
    public JComboBox<String> getCboDanhMucFilter() {
        return cboDanhMucFilter;
    }
    
    public JComboBox<String> getCboTrangThaiFilter() {
        return cboTrangThaiFilter;
    }
    public JButton getBtnThem() {
        return btnThem;
    }
    public JButton getBtnXoa() {
        return btnXoa;
    }
    public JButton getBtnExcel() {
        return btnExcel;
    }
    public JButton getBtnResetFilter() {
        return btnResetFilter;
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
    public void addExportExcelListener(ActionListener listener) {
        btnExcel.addActionListener(listener);
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
