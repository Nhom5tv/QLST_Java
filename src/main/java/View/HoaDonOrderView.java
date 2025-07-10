package View;

import Controller.DiaChiController;
import Controller.HoaDonOrderController;
import Controller.KhuyenMaiKhachHangController;
import DAO.ChiTietHoaDonOrderDAO;
import DAO.HoaDonOrderDAO;
import DAO.KhuyenMaiDAO;
import Model.ChiTietHoaDonOrder;
import Model.DiaChi;
import Model.GioHang;
import Model.HoaDonOrder;
import Model.KhuyenMai;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

public class HoaDonOrderView extends JFrame {

    private JLabel nameLabel;
    private JLabel addressLabel;
    private JLabel voucherCodeLabel;
    private JLabel discountAmountLabel;
    private JLabel totalAmountLabel;
    private JFrame parentFrame;
    private JButton changeBtn, orderBtn;
    private List<GioHang> selectedItems;
    private int MaKH;
    private int discountValue = 0;
    private DiaChi selectedDiaChi;
    private GioHangView parentView;

    public HoaDonOrderView(List<GioHang> selectedItems, GioHangView parentView, JFrame parent, int MaKH) {
        this.selectedItems = selectedItems;
        this.parentView = parentView;
        this.parentFrame = parent;
        if (parentFrame != null) {
            parentFrame.dispose();
        }
        this.MaKH = MaKH;
        initUI();
    }

    private int getTotalItemAmount() {
        int total = 0;
        for (GioHang item : selectedItems) {
            total += item.getGiaban() * item.getSoLuong();
        }
        return total;
    }

    private void initUI() {
        setTitle("Thanh toán");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        mainPanel.add(createAddressPanel(), BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(createProductPanel()), BorderLayout.CENTER);
        mainPanel.add(createCheckoutPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createAddressPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Địa chỉ nhận hàng"));

        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addressLabel = new JLabel("");

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(addressLabel);

        changeBtn = new JButton("Thay đổi");
        changeBtn.setPreferredSize(new Dimension(100, 30));
        changeBtn.addActionListener(e -> {
            DiaChiView dialog = new DiaChiView(this, MaKH, actionEvent -> {
                String[] parts = actionEvent.getActionCommand().split(";");
                nameLabel.setText(parts[0] + " (" + parts[1] + ")");
                addressLabel.setText(parts[2]);
                DiaChi diaChi = new DiaChi();
                diaChi.setName(parts[0]);
                diaChi.setPhoneNumber(parts[1]);
                diaChi.setDetailAddress(parts[2]);
                selectedDiaChi = diaChi;
            });
            new DiaChiController(dialog);
            dialog.setVisible(true);
        });

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(changeBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Sản phẩm"));

        String[] columns = {"", "Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        Object[][] data = new Object[selectedItems.size()][5];

        for (int i = 0; i < selectedItems.size(); i++) {
            GioHang item = selectedItems.get(i);
            if (item.getAnh() != null) {
                ImageIcon originalIcon = new ImageIcon(item.getAnh());
                Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                data[i][0] = new ImageIcon(scaledImage);
            }
            data[i][1] = item.getTenSp();
            data[i][2] = String.format("%,.0f VND", item.getGiaban());
            data[i][3] = item.getSoLuong();
            data[i][4] = String.format("%,.0f VND", item.getGiaban() * item.getSoLuong());
        }

        JTable table = new JTable(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    return new JLabel((ImageIcon) value);
                } else {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });

        table.setRowHeight(80);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Thanh toán"));

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel voucherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        voucherPanel.setBorder(new TitledBorder("Mã giảm giá"));

        voucherCodeLabel = new JLabel("Chưa chọn");
        discountAmountLabel = new JLabel("0đ");

        JButton selectVoucherBtn = new JButton("Chọn Voucher");
        selectVoucherBtn.addActionListener(e -> openVoucherDialog());

        voucherPanel.add(new JLabel("Voucher:"));
        voucherPanel.add(voucherCodeLabel);

        voucherPanel.add(selectVoucherBtn);

        topPanel.add(voucherPanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        summaryPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        summaryPanel.add(new JLabel("Tổng tiền hàng:"));
        JLabel tongTienLabel = new JLabel(String.format("%,dđ", getTotalItemAmount()));
        tongTienLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        summaryPanel.add(tongTienLabel);

        // Dòng giảm giá
        summaryPanel.add(new JLabel("Giảm giá:"));
        discountAmountLabel = new JLabel(String.format("-%,dđ", discountValue));
        discountAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        summaryPanel.add(discountAmountLabel);

        // Tổng thanh toán
        summaryPanel.add(new JLabel("Tổng thanh toán:"));
        totalAmountLabel = new JLabel("", SwingConstants.RIGHT);
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setForeground(Color.RED);
        summaryPanel.add(totalAmountLabel);

        updateTotalAmount();

        orderBtn = new JButton("ĐẶT HÀNG");
        orderBtn.setBackground(new Color(255, 102, 0));
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setFont(new Font("Arial", Font.BOLD, 16));
        orderBtn.setPreferredSize(new Dimension(200, 40));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(orderBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void openVoucherDialog() {
        KhuyenMaiKhachHangView kmView = new KhuyenMaiKhachHangView();
        final JDialog dialog = new JDialog(this, "Chọn khuyến mãi", true);

        KhuyenMaiKhachHangController controller = new KhuyenMaiKhachHangController(kmView, selectedItems, MaKH);
        controller.setOnVoucherAppliedListener(maKM -> {
            KhuyenMaiDAO dao = new KhuyenMaiDAO();
            KhuyenMai km = dao.getById(maKM);
            if (km != null) {
                voucherCodeLabel.setText(maKM);
                int phanTramGiam = km.getPhanTramGiam();
                discountValue = (getTotalItemAmount() * phanTramGiam) / 100;
                discountAmountLabel.setText(String.format("-%,dđ", discountValue));
                HoaDonOrder hoaDonOrder = new HoaDonOrder();
                hoaDonOrder.setMakhuyenmai(maKM);
                updateTotalAmount();
            }
            dialog.dispose();
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(kmView);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateTotalAmount() {
        int total = getTotalItemAmount() - discountValue;
        total = Math.max(total, 0);
        totalAmountLabel.setText(String.format("%,dđ", total));
    }

    public GioHangView getParentView() {
        return parentView;
    }

    public DiaChi getDiaChi() {
        return selectedDiaChi;
    }

    public JButton getChangeBtn() {
        return changeBtn;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getAddressLabel() {
        return addressLabel;
    }

    public JButton getOrderBtn() {
        return orderBtn;
    }

    public List<GioHang> getSelectedItems() {
        return selectedItems;
    }
    
    public void setOrderButtonListener(ActionListener listener) {
        orderBtn.addActionListener(listener);
    }

    public String getMaKhuyenMai() {
        String text = voucherCodeLabel.getText();
        return (text != null && !text.equalsIgnoreCase("Chưa chọn")) ? text : null;
    }

    public void setAddressInfo(DiaChi diaChi) {
        if (diaChi != null) {
            selectedDiaChi = diaChi;
            nameLabel.setText(diaChi.getName() + " (" + diaChi.getPhoneNumber() + ")");
            addressLabel.setText(diaChi.getDetailAddress());
        }
    }


    public BigDecimal getTotalAmount() {
        int total = Math.max(getTotalItemAmount() - discountValue, 0);
        return BigDecimal.valueOf(total);
    }
}
