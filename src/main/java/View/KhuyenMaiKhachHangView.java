package View;

import Model.KhuyenMai;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

public class KhuyenMaiKhachHangView extends JPanel {

    private JTable tblKhuyenMai;
    private DefaultTableModel modelTable;
    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JButton btnApDung;
    private Consumer<String> onVoucherAppliedListener;

    public KhuyenMaiKhachHangView() {
    initLookAndFeel();
    initUI();
}



    public KhuyenMaiKhachHangView(ActionListener applyListener) {
       

        // Gán sự kiện cho nút Áp dụng
        btnApDung.addActionListener(applyListener);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 20);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception ignored) {
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập tên hoặc mã khuyến mãi...");
        txtTimKiem.setPreferredSize(new Dimension(220, 35));

        btnTimKiem = createButton("🔎 Tìm", new Color(52, 152, 219));
        btnApDung = createButton("✅ Áp dụng", new Color(46, 204, 113));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(lblSearch);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnApDung);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Mã", "Bắt đầu", "Kết thúc", "Giảm (%)", "Ghi chú"};

        modelTable = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblKhuyenMai = new JTable(modelTable);
        tblKhuyenMai.setRowHeight(30);
        tblKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhuyenMai.setShowHorizontalLines(false);
        tblKhuyenMai.setShowVerticalLines(false);
        tblKhuyenMai.setIntercellSpacing(new Dimension(0, 0));
        tblKhuyenMai.setGridColor(new Color(230, 230, 230));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(41, 128, 185));
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
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tblKhuyenMai);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
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

    public void addKhuyenMaiToTable(List<KhuyenMai> list) {
        modelTable.setRowCount(0);
        for (KhuyenMai km : list) {
            modelTable.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getNgayBatDau(),
                    km.getNgayKetThuc(),
                    km.getPhanTramGiam(),
                    km.getGhiChu()
            });
        }
    }

    // Getters
    public JTable getTblKhuyenMai() {
        return tblKhuyenMai;
    }

    public DefaultTableModel getModelTable() {
        return modelTable;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JButton getBtnTimKiem() {
        return btnTimKiem;
    }

    public JButton getBtnApDung() {
        return btnApDung;
    }

    public String getSearchKeyword() {
        return txtTimKiem.getText().trim();
    }
   

   public void setOnVoucherAppliedListener(Consumer<String> listener) {
    this.onVoucherAppliedListener = listener;
}

    public void addApplyListener(ActionListener listener) {
        btnApDung.addActionListener(listener);
    }
  public void addSearchListener(ActionListener listener) {
    btnTimKiem.addActionListener(listener);
}


    // Trả về mã khuyến mãi đang được chọn
    public String getSelectedVoucher() {
        int row = tblKhuyenMai.getSelectedRow();
        if (row >= 0) {
            return tblKhuyenMai.getValueAt(row, 0).toString();
        }
        return null;
    }
}
