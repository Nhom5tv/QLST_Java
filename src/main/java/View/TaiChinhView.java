package View;
//hiphanh

import com.formdev.flatlaf.FlatLightLaf;
import model.TaiChinh;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class TaiChinhView extends JPanel {
    private JTable tblTaiChinh;
    private DefaultTableModel modelTable;
    private JTextField txtTimKiem;
    private JButton btnThem, btnXoa, btnTimKiem, btnXuatExcel , btnXemBieuDo;

    public TaiChinhView() {
        initLookAndFeel();
        initUI();
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ignored) {}
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblSearch = new JLabel("\uD83D\uDD0D Tìm kiếm:");
        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo mô tả hoặc mã...");
        txtTimKiem.setPreferredSize(new Dimension(220, 35));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(lblSearch);
        searchPanel.add(txtTimKiem);

        btnTimKiem = createButton("\uD83D\uDD0E Tìm", new Color(52, 152, 219));
        btnThem = createButton("➕ Thêm", new Color(46, 204, 113));
        btnXoa = createButton("❌ Xoá", new Color(231, 76, 60));
        btnXuatExcel = createButton("\uD83D\uDCC1 Xuất Excel", new Color(52, 152, 219));
        btnXemBieuDo = createButton("\uD83D\uDCCA Xem Biểu đồ", new Color(155, 89, 182));
       


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnXuatExcel);
        buttonPanel.add(btnXemBieuDo);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
            "Mã Giao Dịch",
            "Ngày",
            "Loại Giao Dịch",
            "Số Tiền",
            "Mô Tả",
            "Tên Nhân Viên"
        };

        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTaiChinh = new JTable(modelTable);
        tblTaiChinh.setRowHeight(28);
        tblTaiChinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblTaiChinh.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiChinh.setShowGrid(false);
        tblTaiChinh.setIntercellSpacing(new Dimension(0, 0));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(52, 152, 219));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };

        for (int i = 0; i < tblTaiChinh.getColumnCount(); i++) {
            tblTaiChinh.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JTableHeader header = tblTaiChinh.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        return new JScrollPane(tblTaiChinh);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    // Dữ liệu lên bảng
    public void setTaiChinhData(List<TaiChinh> list) {
        modelTable.setRowCount(0);
        for (TaiChinh tc : list) {
            modelTable.addRow(new Object[]{
                tc.getMaGiaoDich(),
                tc.getNgay(),
                tc.getLoaiGiaoDich(),
                tc.getSoTien(),
                tc.getMoTa(),
                tc.getNhanVien(). getHoten() // đúng với model có NhanVien
            });
        }
    }

    // Lấy text tìm kiếm
    public String getSearchKeyword() {
        return txtTimKiem.getText().trim();
    }

    // Lấy các hàng được chọn (nhiều hàng)
    public int[] getSelectedRows() {
        return tblTaiChinh.getSelectedRows();
    }

    // Lấy 1 hàng được chọn (cho double click)
    public int getSelectedRow() {
        return tblTaiChinh.getSelectedRow();
    }

    // Lấy model bảng
    public DefaultTableModel getModelTable() {
        return modelTable;
    }

    // Thêm listener cho các nút
    public void addSearchListener(ActionListener listener) {
        btnTimKiem.addActionListener(listener);
    }

    public void addAddListener(ActionListener listener) {
        btnThem.addActionListener(listener);
    }

    public void addDeleteListener(ActionListener listener) {
        btnXoa.addActionListener(listener);
    }

    public void addTableMouseListener(MouseListener listener) {
        tblTaiChinh.addMouseListener(listener);
    }

   public JButton getBtnXuatExcel() {
    return btnXuatExcel;
}

public void addExportExcelListener(ActionListener listener) {
    btnXuatExcel.addActionListener(listener);
}
public void addChartListener(ActionListener listener) {
    btnXemBieuDo.addActionListener(listener);
}
public JButton getBtnXemBieuDo() {
    return btnXemBieuDo;
}


    // Main để chạy thử
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý Tài Chính");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(950, 600);
            frame.setLocationRelativeTo(null);

            TaiChinhView view = new TaiChinhView();
            new Controller.TaiChinhController(view);

            frame.setContentPane(view);
            frame.setVisible(true);
        });
    }
}
