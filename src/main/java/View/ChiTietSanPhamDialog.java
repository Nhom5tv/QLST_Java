package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.DanhMucSanPham;

public class ChiTietSanPhamDialog extends JDialog {
    private JTextField txtMaSP, txtTenSP, txtDonVi, txtGiaGoc, txtGiaBan, txtTonKho,txtMaVach,txtMoTa;
    private JComboBox<DanhMucSanPham> cboDanhMuc;
    private JButton btnChonHinh, btnLuu, btnHuy;
    private JTextField txtThuongHieu, txtXuatXu;
    private JTextField txtThanhPhan, txtHuongDan, txtBaoQuan;
    private JLabel lblPreviewImage;
    private JRadioButton rdDangBan, rdNgungKinhDoanh;
    private int maSanPham = -1; // -1 nếu là thêm mới


    public ChiTietSanPhamDialog(Frame parent) {
        super(parent, "Chi Tiết Sản Phẩm", true);
        setSize(600, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Chi Tiết Sản Phẩm", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(0, 123, 255));
        lblTitle.setPreferredSize(new Dimension(600, 35));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlContent.setBackground(Color.WHITE);

        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBackground(Color.WHITE);

        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        pnlRight.setBackground(Color.WHITE);

        txtMaSP = createInputField();
        txtTenSP = createInputField();
        cboDanhMuc = new JComboBox<>();
        cboDanhMuc.setMaximumSize(new Dimension(200, 28));

        btnChonHinh = new JButton("Chọn hình...");
        btnChonHinh.setFocusPainted(false);
        btnChonHinh.setBackground(new Color(240, 240, 240));
        btnChonHinh.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btnChonHinh.setMaximumSize(new Dimension(200, 28));

        lblPreviewImage = new JLabel();
        lblPreviewImage.setPreferredSize(new Dimension(160, 120));
        lblPreviewImage.setHorizontalAlignment(SwingConstants.CENTER);

        btnChonHinh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg", "bmp"));
                int result = fileChooser.showOpenDialog(ChiTietSanPhamDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image scaled = icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                    lblPreviewImage.setIcon(new ImageIcon(scaled));
                }
            }
        });

        txtDonVi = createInputField();
        txtGiaGoc = createInputField();
        txtGiaBan = createInputField();
        txtTonKho = createInputField();
        txtMaVach= createInputField();

        JPanel giaPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        giaPanel.setBackground(Color.WHITE);
        giaPanel.add(createLabeledPanel("Giá gốc:", txtGiaGoc));
        giaPanel.add(createLabeledPanel("Giá bán:", txtGiaBan));
        
        rdDangBan = new JRadioButton("Đang bán");
        rdNgungKinhDoanh = new JRadioButton("Ngừng bán");

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(rdDangBan);
        statusGroup.add(rdNgungKinhDoanh);
        rdDangBan.setSelected(true); // Mặc định là đang bán

        JPanel pnlTrangThai = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlTrangThai.setBackground(Color.WHITE);
        pnlTrangThai.setBorder(BorderFactory.createTitledBorder("Trạng thái"));
        pnlTrangThai.add(rdDangBan);
        pnlTrangThai.add(rdNgungKinhDoanh);

        
        JPanel pnlHinhAnh = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlHinhAnh.setBackground(Color.WHITE);
        pnlHinhAnh.add(new JLabel("Hình ảnh:"));
        pnlHinhAnh.add(btnChonHinh);
        
        // Panel chứa hình ảnh với kích thước cố định
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(160, 120));
        imagePanel.setMaximumSize(new Dimension(160, 120));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // JLabel chứa hình
        lblPreviewImage = new JLabel("", SwingConstants.CENTER);
        lblPreviewImage.setPreferredSize(new Dimension(160, 120));
        lblPreviewImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreviewImage.setVerticalAlignment(SwingConstants.CENTER);
        lblPreviewImage.setOpaque(false);
        imagePanel.add(lblPreviewImage, BorderLayout.CENTER);

        pnlLeft.add(createLabeledPanel("Mã SP:", txtMaSP));
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(createLabeledPanel("Tên sản phẩm:", txtTenSP));
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(createLabeledPanel("Danh mục:", cboDanhMuc));
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(pnlHinhAnh);
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(imagePanel);
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(createLabeledPanel("Mã vạch:", txtMaVach));
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(giaPanel);
        pnlLeft.add(Box.createVerticalStrut(8));
        pnlLeft.add(pnlTrangThai);
        

        txtThuongHieu = createInputField();
        txtXuatXu = createInputField();
        txtThanhPhan = createInputField();
        txtHuongDan = createInputField();
        txtBaoQuan = createInputField();
        txtMoTa= createInputField();
        
        pnlRight.add(createLabeledPanel("Tồn kho:", txtTonKho));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Đơn vị:", txtDonVi));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Thương hiệu:", txtThuongHieu));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Xuất xứ:", txtXuatXu));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Thành phần sử dụng:", txtThanhPhan));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Hướng dẫn sử dụng:", txtHuongDan));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Bảo quản:", txtBaoQuan));
        pnlRight.add(Box.createVerticalStrut(8));
        pnlRight.add(createLabeledPanel("Mô tả:", txtMoTa));

        pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlContent.add(pnlLeft);
        pnlContent.add(pnlRight);
        add(pnlContent, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(Color.WHITE);
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(0, 123, 255));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(100, 30)); 
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(220, 53, 69));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setPreferredSize(new Dimension(100, 30)); 

        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);

        add(pnlButtons, BorderLayout.SOUTH);
    }

    public void setDanhMucOptions(List<DanhMucSanPham> danhMucList) {
        cboDanhMuc.setModel(new DefaultComboBoxModel<>(danhMucList.toArray(new DanhMucSanPham[0])));
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(labelText);
        component.setMaximumSize(new Dimension(200, 28));
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JTextField createInputField() {
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(300, 28));
        return textField;
    }
    public int getMaSanPham() {
    return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public JTextField getTxtMaSP() { return txtMaSP; }
    public JTextField getTxtTenSP() { return txtTenSP; }
    public JTextField getTxtDonVi() { return txtDonVi; }
    public JTextField getTxtGiaGoc() { return txtGiaGoc; }
    public JTextField getTxtGiaBan() { return txtGiaBan; }
    public JTextField getTxtTonKho() { return txtTonKho; }
    public JTextField getTxtMaVach() { return txtMaVach; }
    
    public int getTrangThai() {
    return rdDangBan.isSelected() ? 1 : 0;
    }


    public JComboBox<DanhMucSanPham> getCboDanhMuc() { return cboDanhMuc; }

    public JTextField getTxtThuongHieu() { return txtThuongHieu; }
    public JTextField getTxtXuatXu() { return txtXuatXu; }
    public JTextField getTxtThanhPhan() { return txtThanhPhan; }
    public JTextField getTxtHuongDan() { return txtHuongDan; }
    public JTextField getTxtBaoQuan() { return txtBaoQuan; }
    public JTextField getTxtMoTa() { return txtMoTa; }

    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }

    public JLabel getLblPreviewImage() { return lblPreviewImage; }
    public void setTrangThai(int trangThai) {
    if (trangThai == 1) {
        rdDangBan.setSelected(true);
    } else {
        rdNgungKinhDoanh.setSelected(true);
    }
}

}
