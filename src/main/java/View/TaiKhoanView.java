package View;

import com.formdev.flatlaf.FlatLightLaf;
import  Controller.TaiKhoanController;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TaiKhoanView extends JPanel {

    private JTable tblTaiKhoan;
    private DefaultTableModel modelTable;
    private JTextField txtTimKiem;
    private JButton btnThem, btnXoa, btnTimKiem, btnXuatExcel;

    public TaiKhoanView() {
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
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo tên đăng nhập...");
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
        String[] columns = {"Mã TK", "Tên nhân viên / khách hàng", "Tên đăng nhập", "Mật khẩu", "Vai trò"};
        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTaiKhoan = new JTable(modelTable);
        tblTaiKhoan.setRowHeight(28);
        tblTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiKhoan.setShowGrid(false);
        tblTaiKhoan.setIntercellSpacing(new Dimension(0, 0));

        // Renderer để đổi màu xen kẽ và khi chọn
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

        for (int i = 0; i < tblTaiKhoan.getColumnCount(); i++) {
            tblTaiKhoan.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JTableHeader header = tblTaiKhoan.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        return new JScrollPane(tblTaiKhoan);
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

    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public JButton getBtnXuatExcel() { return btnXuatExcel; }
    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JTable getTblTaiKhoan() { return tblTaiKhoan; }
    public DefaultTableModel getModelTable() { return modelTable; }
    // Phương thức main để chạy thử giao diện
    public static void main(String[] args) {
        try {
        UIManager.setLookAndFeel(new FlatLightLaf());
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 15);
        UIManager.put("TextComponent.arc", 10);
    } catch (Exception e) {
        e.printStackTrace();
    }

    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Quản lý tài khoản");
        TaiKhoanView view = new TaiKhoanView();
        new TaiKhoanController(view);  // Tạo controller để load dữ liệu và xử lý sự kiện
        frame.setContentPane(view);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    });
    }
}
