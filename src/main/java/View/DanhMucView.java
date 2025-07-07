package View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class DanhMucView extends JPanel {

    private JTree treeDanhMuc;
    private JTextField txtTimKiem;
    private JTextField txtTenDanhMuc;
    private JTextField txtMaKyHieu;
    private JTextArea txtMoTa;
    private JComboBox<String> cboDanhMucCha;
    private JButton btnThem,btnLuu,btnXoa,btnExcel;

    public DanhMucView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setOpaque(false);

        // ==== PANEL TRÁI ====
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setOpaque(false);

        // ==== Panel Tìm kiếm ====
        JPanel panelTimKiem = new JPanel(new BorderLayout(10, 10));
        panelTimKiem.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblSearch = new JLabel("🔍");

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(100, 35));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                txtTimKiem.getBorder(), new EmptyBorder(5, 10, 5, 10)));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tìm Kiếm...");
        txtTimKiem.setToolTipText("Tìm kiếm theo tên danh mục và mã ký hiệu");

        panelTimKiem.add(lblSearch, BorderLayout.WEST);
        panelTimKiem.add(txtTimKiem, BorderLayout.CENTER);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Danh Mục");
        DefaultMutableTreeNode noiChao = new DefaultMutableTreeNode("Nồi chảo");
        noiChao.add(new DefaultMutableTreeNode("Đồ điện"));
        root.add(noiChao);
        root.add(new DefaultMutableTreeNode("Mỹ phẩm"));
        root.add(new DefaultMutableTreeNode("Điện lạnh"));
        root.add(new DefaultMutableTreeNode("Thực phẩm"));

        treeDanhMuc = new JTree(new DefaultTreeModel(root));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(new ImageIcon(getClass().getResource("/icons/folder-open.png")));
        renderer.setClosedIcon(new ImageIcon(getClass().getResource("/icons/folder-close.png")));
        renderer.setLeafIcon(new ImageIcon(getClass().getResource("/icons/file.png")));
        treeDanhMuc.setCellRenderer(renderer);

        JScrollPane treeScroll = new JScrollPane(treeDanhMuc);
        treeScroll.setPreferredSize(new Dimension(220, 400));

        leftPanel.add(panelTimKiem, BorderLayout.NORTH);
        leftPanel.add(treeScroll, BorderLayout.CENTER);

        // ==== PANEL PHẢI ====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)), new EmptyBorder(15, 20, 15, 20)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setBackground(Color.WHITE);

        btnThem = new JButton("➕ Thêm");
        btnThem.setBackground(new Color(50, 205, 50));
        btnThem.setForeground(Color.WHITE);

        btnXoa = new JButton("❌ Xóa");
        btnXoa.setBackground(new Color(255, 0, 0));
        btnXoa.setForeground(Color.WHITE);

        btnExcel = new JButton("📊 Excel");
        btnExcel.setBackground(new Color(30, 144, 255));
        btnExcel.setForeground(Color.WHITE);
        

        for (JButton btn : new JButton[]{btnThem, btnXoa, btnExcel}) {
            btn.setPreferredSize(new Dimension(100, 35));
            
        }

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnExcel);
       

        // 👇 Thêm vào rightPanel TRƯỚC lblHeader
        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createVerticalStrut(10)); // Khoảng cách dưới nút

        JLabel lblHeader = new JLabel("Chi tiết danh mục");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(lblHeader);
        rightPanel.add(Box.createVerticalStrut(15));

        cboDanhMucCha = new JComboBox<>(new String[]{
                "Danh mục gốc", "Hàng gia dụng", "Mỹ phẩm", "Điện lạnh"
        });
        decorateComboBox(cboDanhMucCha);
        cboDanhMucCha.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(createField("Danh mục cha", cboDanhMucCha));
        rightPanel.add(Box.createVerticalStrut(10));

        txtTenDanhMuc = new JTextField();
        txtMaKyHieu = new JTextField();
        txtMoTa = new JTextArea(5, 20);
        decorateInput(txtTenDanhMuc);
        decorateInput(txtMaKyHieu);
        decorateArea(txtMoTa);

        txtTenDanhMuc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMaKyHieu.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        rightPanel.add(createField("Tên danh mục", txtTenDanhMuc));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createField("Mã ký hiệu", txtMaKyHieu));
        txtMaKyHieu.setToolTipText("Nếu để trống, hệ thống sẽ tự sinh mã");
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createField("Mô tả", new JScrollPane(txtMoTa)));
        rightPanel.add(Box.createVerticalStrut(20));
        
        // ==== Nút cập nhật cuối cùng, nằm phải đều với form ====
        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.X_AXIS));
        updatePanel.setOpaque(false);
        updatePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        updatePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // đẩy nút sang phải
        updatePanel.add(Box.createHorizontalGlue());

        btnLuu = new JButton("🔁 Lưu");
        btnLuu.setPreferredSize(new Dimension(120, 35));
        btnLuu.setMaximumSize(new Dimension(120, 35)); // ✅ ép giữ kích thước
        btnLuu.setMinimumSize(new Dimension(120, 35)); // ✅ tránh co nhỏ
        btnLuu.setBackground(new Color(0, 123, 255));
        btnLuu.setForeground(Color.WHITE);

        updatePanel.add(btnLuu);
        rightPanel.add(updatePanel);


        // ==== CHIA ĐÔI ====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(240);
        splitPane.setResizeWeight(0);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createField(String label, JComponent input) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);
        return panel;
    }

    private void decorateComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)), new EmptyBorder(4, 10, 4, 10)));
        comboBox.setPreferredSize(new Dimension(200, 28));
    }

    private void decorateInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)), new EmptyBorder(6, 10, 6, 10)));
        field.setPreferredSize(new Dimension(200, 25));
    }

    private void decorateArea(JTextArea area) {
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)), new EmptyBorder(6, 10, 6, 10)));
    }
    public JTree getTreeDanhMuc() {
        return treeDanhMuc;
    }

    public JTextField getTxtTenDanhMuc() {
        return txtTenDanhMuc;
    }

    public JTextField getTxtMaKyHieu() {
        return txtMaKyHieu;
    }
    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JTextArea getTxtMoTa() {
        return txtMoTa;
    }

    public JComboBox<String> getCboDanhMucCha() {
        return cboDanhMucCha;
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnLuu() {
        return btnLuu;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public JButton getBtnExcel() {
        return btnExcel;
    }


   
}
