package View;

import com.formdev.flatlaf.FlatLightLaf;
import Controller.KhuyenMaiController;
import Model.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class KhuyenMaiView extends JPanel {

    private JTable tblKhuyenMai;
    private DefaultTableModel modelTable;
    private JTextField txtTimKiem;
    private JButton btnThem,  btnXoa, btnTimKiem, btnXuatExcel;


    public KhuyenMaiView() {
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

        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo tên hoặc mã...");
        txtTimKiem.setPreferredSize(new Dimension(220, 35));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(lblSearch);
        searchPanel.add(txtTimKiem);

        btnTimKiem = createButton("🔎 Tìm", new Color(52, 152, 219));
        btnThem = createButton("➕ Thêm", new Color(46, 204, 113));
        btnXoa = createButton("❌ Xoá", new Color(231, 76, 60));
        btnXuatExcel = createButton("📁 Xuất Excel", new Color(52, 152, 219));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnXuatExcel);


        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = { "Mã", "Tên", "Bắt đầu", "Kết thúc", "Giảm (%)", "Số lượng", "Tổng tiền tối thiểu", "SL SP tối thiểu", "Ghi chú" };

        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // không cho sửa trực tiếp
            }
        };

        tblKhuyenMai = new JTable(modelTable);
        tblKhuyenMai.setRowHeight(28);
        tblKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhuyenMai.setShowGrid(false);
        tblKhuyenMai.setIntercellSpacing(new Dimension(0, 0));

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

        for (int i = 0; i < tblKhuyenMai.getColumnCount(); i++) {
            tblKhuyenMai.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JTableHeader header = tblKhuyenMai.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        return new JScrollPane(tblKhuyenMai);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    public void addKhuyenMaiToTable(List<KhuyenMai> list) {
        modelTable.setRowCount(0);
        for (KhuyenMai km : list) {
            modelTable.addRow(new Object[] {
                km.getMaKhuyenMai(),
                km.getTenKhuyenMai(),
                km.getNgayBatDau(),
                km.getNgayKetThuc(),
                km.getPhanTramGiam(),
                km.getSoLuong(),
                km.getTongTienToiThieu(),
                km.getSoLuongSpToiThieu(),
                km.getGhiChu()

                
            });
        }
    }
   

// Lấy kết nối thật từ nơi bạn quản lý


    // Getters cho Controller
    public JTable getTblKhuyenMai() { return tblKhuyenMai; }
    public DefaultTableModel getModelTable() { return modelTable; }
    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public JButton getBtnXuatExcel() { return btnXuatExcel; }
   



    // Các method thêm listener để Controller đăng ký sự kiện
    public void addSearchListener(ActionListener listener) {
        btnTimKiem.addActionListener(listener);
    }

    public void addAddListener(ActionListener listener) {
        btnThem.addActionListener(listener);
    }


    public void addDeleteListener(ActionListener listener) {
        btnXoa.addActionListener(listener);
    }

    public void addTableDoubleClickListener(MouseListener listener) {
        tblKhuyenMai.addMouseListener(listener);
    }
     public static void main(String[] args) {
        // Để giao diện đẹp, chạy trên EDT
        SwingUtilities.invokeLater(() -> {
            // Tạo frame chính
            JFrame frame = new JFrame("Quản lý Khuyến Mãi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            // Tạo view và controller
            KhuyenMaiView view = new KhuyenMaiView();
            new KhuyenMaiController(view);

            // Đưa view vào frame
            frame.setContentPane(view);

            // Hiển thị
            frame.setVisible(true);
        });
     }
}
