/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author DUNG LE
 */
import Model.ChiTietHoaDon;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChiTietHoaDonDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;

    public ChiTietHoaDonDialog(JFrame parent, int maHoaDon, List<ChiTietHoaDon> chiTietList) {
        super(parent, "Chi Tiết Hóa Đơn #" + maHoaDon, true);
        setSize(650, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initUI(chiTietList);
    }

    private void initUI(List<ChiTietHoaDon> list) {
        String[] columns = {"Mã Sản Phẩm", "Mã Lô Hàng", "Số Lượng", "Đơn Giá", "Giảm Giá"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 2 -> Integer.class;    
                    case 3, 4 -> Double.class;     
                    case 1 -> Integer.class;       
                    default -> Object.class;
                };
            }
        };

        // Giả sử bạn đã có danh sách chi tiết hóa đơn
        for (ChiTietHoaDon ct : list) {
            model.addRow(new Object[]{
                ct.getMaSanPham(),                 
                ct.getMaLo(),                   
                ct.getSoLuong(),                    
                ct.getDonGia().doubleValue(),       
                ct.getGiamGia().doubleValue()      
            });
        }


        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(2, 2));
        table.setSelectionBackground(new Color(220, 240, 255));

        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);
        table.setDefaultEditor(Object.class, null);

        // Zebra stripe render
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? new Color(240, 255, 240) : Color.WHITE);
                c.setFont(c.getFont().deriveFont(row == table.getSelectedRow() ? Font.BOLD : Font.PLAIN));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Đóng");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

