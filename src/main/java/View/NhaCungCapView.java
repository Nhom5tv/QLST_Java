package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

// Refactored from NhaCungCapView to be a JPanel for embedding
public class NhaCungCapView extends JPanel {
    private JTextField txtnccid, txtnccname, txtphone, txtemail, txtdiachi;
    private JTable ncctable;
    private DefaultTableModel nccmodel;
    private JButton btnThem, btnXoa, btnExportPdf, btnExportExcel,btnImportExcel;
    private JTextField txtSearch;

    public NhaCungCapView() {
        setLayout(new BorderLayout());

        // Áp dụng FlatLaf (optional, recommend to be set in JFrame)
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
//        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
//        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
//        lblTitle.setForeground(Color.WHITE);
//        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 0));
//
//        ImageIcon logoIcon = new ImageIcon("C:\\Users\\DUNG LE\\OneDrive\\Desktop\\Java_Big_HomeWork\\src\\main\\resources\\images\\logo_demo.png");
//        Image scaledImage = getScaledImage(logoIcon.getImage(), 100, 100);
//        logoIcon = new ImageIcon(scaledImage);
//        JLabel lblLogo = new JLabel(logoIcon);
//        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

//        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
//        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
//        titlePanel.setBackground(new Color(33, 150, 243));
//        titlePanel.add(lblLogo);
//        titlePanel.add(lblTitle);

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

        JLabel lblSearch = new JLabel("🔍 Tìm Kiếm:");
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm Kiếm Theo Nhà Cung Cấp Và Tên Người Đại Diện");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(50, 205, 50));
        btnThem.setForeground(Color.WHITE);
        btnXoa = new JButton("❌ Xóa");
        btnXoa.setBackground(new Color(255, 0, 0));
        btnXoa.setForeground(Color.WHITE);
        btnImportExcel = new JButton("📥 Nhập Excel");
        btnExportExcel = new JButton("📊 Excel");
        btnExportPdf = new JButton("📄 PDF");
        btnImportExcel.setBackground(new Color(60, 179, 113)); // màu xanh lục
        btnExportExcel.setBackground(new Color(30, 144, 255));
        btnExportPdf.setBackground(new Color(255, 165, 0));
        btnImportExcel.setForeground(Color.WHITE);
        btnExportExcel.setForeground(Color.WHITE);
        btnExportPdf.setForeground(Color.WHITE);
        

        for (JButton btn : new JButton[]{btnThem, btnXoa}) {
            btn.setPreferredSize(new Dimension(100, 35));
        }
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnImportExcel);
        buttonPanel.add(btnExportExcel);
        buttonPanel.add(btnExportPdf);
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setBackground(Color.WHITE);
        searchPanel.setBackground(Color.WHITE);

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
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                // Zebra background
                if (row % 2 == 0) {
                    c.setBackground(new Color(127, 255, 0));
                } else {
                    c.setBackground(Color.WHITE);
                }
                // Bold for selected row
                if (table.getSelectedRow() == row) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
                
                return c;
            }
        };
        for (int i = 1; i < ncctable.getColumnCount(); i++) {
            ncctable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        ncctable.setRowHeight(28);
        ncctable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ncctable.setSelectionBackground(new Color(220, 240, 255));
       // Cấu hình lưới bảng
        ncctable.setShowGrid(false);
        ncctable.setIntercellSpacing(new Dimension(2, 2));
        JTableHeader header = ncctable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        JCheckBox headerCheckBox = new JCheckBox();
        headerCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
        ncctable.getColumnModel().getColumn(0).setMaxWidth(40);
        ncctable.getColumnModel().getColumn(1).setMaxWidth(80);
        ncctable.getColumnModel().getColumn(0).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> headerCheckBox);

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
                    ncctable.clearSelection();
                    header.repaint();
                }
            }
        });

        ncctable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        nccmodel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 0) {
                Boolean checked = (Boolean) nccmodel.getValueAt(row, col);
                if (checked != null && checked) {
                    ncctable.setRowSelectionInterval(row, row);
                } else {
                    ncctable.removeRowSelectionInterval(row, row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(ncctable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }

    // Getter methods for controller
    public String getSearchText() {
        return txtSearch.getText().trim();
    }
    public JTable getTable() {
        return ncctable;
    }
    public DefaultTableModel getTableModel() {
        return nccmodel;
    }

    // Access button listeners
    public void addAddListener(java.awt.event.ActionListener listener) {
        btnThem.addActionListener(listener);
    }
    public void addDeleteListener(java.awt.event.ActionListener listener) {
        btnXoa.addActionListener(listener);
    }
    public void addSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }
    public void addImportExcelListener(ActionListener listener) {
    btnImportExcel.addActionListener(listener);
}
    public void addExportExcelListener(ActionListener listener) {
    btnExportExcel.addActionListener(listener);
}
    public void addExportPdfListener(ActionListener listener) {
    btnExportPdf.addActionListener(listener);
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

