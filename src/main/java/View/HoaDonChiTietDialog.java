/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import DAO.ChiTietHoaDonOrderDAO;
import Model.ChiTietHoaDonOrder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class HoaDonChiTietDialog extends JDialog {

    public HoaDonChiTietDialog(JFrame parent, int maHoaDon) {
        super(parent, "Chi tiết hóa đơn #" + maHoaDon, true);
        setSize(900, 500);
        setLocationRelativeTo(parent);

        ChiTietHoaDonOrderDAO dao = new ChiTietHoaDonOrderDAO();
        List<ChiTietHoaDonOrder> chiTietList = dao.getByMaHoaDon(maHoaDon);

        String[] columnNames = {
            "Ảnh", "Mã SP", "Tên sản phẩm", "Mã Lô hàng",
            "Số lượng", "Đơn giá", "Thành tiền"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 ->
                        ImageIcon.class;
                    case 1, 3, 4 ->
                        Integer.class;
                    case 5, 6 ->
                        Double.class;
                    default ->
                        String.class;
                };
            }
        };

        for (ChiTietHoaDonOrder item : chiTietList) {
            ImageIcon icon = null;
            if (item.getAnh() != null) {
                Image img = new ImageIcon(item.getAnh()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            } else {
                icon = new ImageIcon(new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB));
            }

            model.addRow(new Object[]{
                icon,
                item.getMaSanPham(),
                item.getTenSanPham(),
                item.getMaLoHang(),
                item.getSoLuong(),
                item.getDonGia(),
                item.getSoLuong() * item.getDonGia()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(80);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        // Render tiền VND
        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText(String.format("%,.0f VND", ((Number) value).doubleValue()));
                } else {
                    super.setValue(value);
                }
            }
        };
        table.getColumnModel().getColumn(5).setCellRenderer(currencyRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(currencyRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
