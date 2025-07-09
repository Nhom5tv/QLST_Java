/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.GioHangController;
import Controller.HoaDonOrderController;
import DAO.ChiTietHoaDonOrderDAO;
import DAO.GioHangDAO;
import DAO.HoaDonOrderDAO;
import Model.GioHang;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
/**
 *
 * @author Admin
 */
public class GioHangView extends JFrame {

    private JLabel lblTitle;
    private JPanel pnProducts;
    private JPanel pnSummary;
    private JLabel lblTotal;
    private JCheckBox cbxAll;
    private JButton btnCheckout;
    private List<GioHang> cartItems;
    private QuantityChangeListener quantityChangeListener;
    private int MaKH;
    

    public List<GioHang> getCartItems() {
        return cartItems;
    }

    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    public GioHangView(int MaKH) {
        this.MaKH = MaKH;
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // NORTH - Tiêu đề
        JPanel pnNorth = new JPanel();
        lblTitle = new JLabel("Giỏ hàng của bạn()");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        pnNorth.add(lblTitle);
        this.add(pnNorth, BorderLayout.NORTH);

        // CENTER - Danh sách sản phẩm
        pnProducts = new JPanel();
        pnProducts.setLayout(new BoxLayout(pnProducts, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(pnProducts);
        this.add(scrollPane, BorderLayout.CENTER);

        // SOUTH - Tổng kết
        pnSummary = new JPanel(new BorderLayout());
        cbxAll = new JCheckBox("Tất cả");
        pnSummary.add(cbxAll, BorderLayout.WEST);
        lblTotal = new JLabel("Tổng cộng: 0 VND");
        btnCheckout = new JButton("Thanh toán");
//        btnCheckout.addActionListener(e -> {
//            List<GioHang> selectedItems = getSelectedCartItems();
//
//            if (selectedItems.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để thanh toán.");
//            } else {
//                // Pass the selected items to the HoaDonOrderView
//                HoaDonOrderView orderView = new HoaDonOrderView(selectedItems, this, this, this.MaKH);
//                new HoaDonOrderController(new HoaDonOrderDAO(), new ChiTietHoaDonOrderDAO(), orderView, this.MaKH);
//                orderView.setVisible(true);
//            }
//        });

        JPanel rightPanel = new JPanel();
        rightPanel.add(lblTotal);
        rightPanel.add(btnCheckout);
        pnSummary.add(rightPanel, BorderLayout.EAST);
        this.add(pnSummary, BorderLayout.SOUTH);
    }

    public void displayCartItems(List<GioHang> cartItems) {
        this.cartItems = cartItems;
        System.out.println("Số lượng sản phẩm trong giỏ hàng: " + cartItems.size()); // Gỡ lỗi
        lblTitle.setText("Giỏ hàng của bạn(" + cartItems.size() + ")");
        pnProducts.removeAll();
        for (GioHang item : cartItems) {
            System.out.println("Sản phẩm: " + item.getTenSp() + ", Số lượng: " + item.getSoLuong()); // Gỡ lỗi
            JPanel productPanel = createProductPanel(item);
            productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            pnProducts.add(productPanel);
            JPanel leftPanel = (JPanel) productPanel.getComponent(0);
            JCheckBox itemCheckBox = (JCheckBox) leftPanel.getComponent(0);
            addItemCheckBoxListener(itemCheckBox, item);
        }
        updateTotal(cartItems);
        pnProducts.revalidate();
        pnProducts.repaint();
    }

    private JPanel createProductPanel(GioHang item) {
        JPanel productPanel = new JPanel(new BorderLayout());
        JCheckBox cbxItem = new JCheckBox();

        // === Ảnh sản phẩm ===
        JLabel lblImage = new JLabel();
        if (item.getAnh() != null) {
            ImageIcon originalIcon = new ImageIcon(item.getAnh());
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        }
        JPanel leftPanel = new JPanel();
        leftPanel.add(cbxItem);
        leftPanel.add(lblImage);
        productPanel.add(leftPanel, BorderLayout.WEST);

        // === Phần thông tin chính ===
        JPanel pnInfo = new JPanel();
        pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.X_AXIS));
        pnInfo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Thông tin sản phẩm
        JPanel pnText = new JPanel();
        pnText.setLayout(new BoxLayout(pnText, BoxLayout.Y_AXIS));
        JLabel lblProductName = new JLabel(item.getTenSp());
        JLabel lblPrice = new JLabel(String.format("%,.0f VND", item.getGiaban()));
        pnText.add(lblProductName);
        pnText.add(Box.createVerticalStrut(5));
        pnText.add(lblPrice);
        pnText.setPreferredSize(new Dimension(200, 60));

        // Số lượng
        int min = 1;
        final int[] quantity = {item.getSoLuong()};
        JLabel lblQuantity = new JLabel(String.valueOf(quantity[0]), JLabel.CENTER);
        lblQuantity.setFont(new Font("Arial", Font.PLAIN, 14));
        lblQuantity.setPreferredSize(new Dimension(30, 24));

        JButton btnMinus = new JButton("−");
        btnMinus.setFont(new Font("Arial", Font.BOLD, 14));
        btnMinus.setPreferredSize(new Dimension(24, 24));
        btnMinus.addActionListener(e -> {
            if (quantity[0] > min) {
                quantity[0]--;
                lblQuantity.setText(String.valueOf(quantity[0]));
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(item.getMaGH(), quantity[0], item.getMaSP());
                }
                updateTotal(List.of(item));
            }
        });

        JButton btnPlus = new JButton("+");
        btnPlus.setFont(new Font("Arial", Font.BOLD, 14));
        btnPlus.setPreferredSize(new Dimension(24, 24));
        btnPlus.addActionListener(e -> {
            if (quantity[0] < 1000) {
                quantity[0]++;
                lblQuantity.setText(String.valueOf(quantity[0]));
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(item.getMaGH(), quantity[0],item.getMaSP());
                }
                updateTotal(List.of(item));
            }
        });

        JPanel customSpinnerPanel = new JPanel();
        customSpinnerPanel.setLayout(new BoxLayout(customSpinnerPanel, BoxLayout.X_AXIS));
        customSpinnerPanel.add(btnMinus);
        customSpinnerPanel.add(Box.createHorizontalStrut(5));
        customSpinnerPanel.add(lblQuantity);
        customSpinnerPanel.add(Box.createHorizontalStrut(5));
        customSpinnerPanel.add(btnPlus);
        customSpinnerPanel.setMaximumSize(new Dimension(120, 30));

        // Nút xóa
        JButton btnRemove = new JButton("Xóa");
        btnRemove.setActionCommand(String.valueOf(item.getMaGH()));
        btnRemove.setFont(new Font("Arial", Font.PLAIN, 13));
        btnRemove.setPreferredSize(new Dimension(60, 30));

        JPanel removePanel = new JPanel();
        removePanel.add(btnRemove);
        removePanel.setMaximumSize(new Dimension(80, 30));

        // Thêm vào panel chính
        pnInfo.add(pnText);
        pnInfo.add(Box.createHorizontalGlue());
        pnInfo.add(customSpinnerPanel);
        pnInfo.add(Box.createHorizontalStrut(20));
        pnInfo.add(removePanel);

        productPanel.add(pnInfo, BorderLayout.CENTER);
        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, productPanel.getPreferredSize().height));
        productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return productPanel;
    }

    public void updateTotal(List<GioHang> cartItems) {
        double total = 0;
        for (int i = 0; i < pnProducts.getComponentCount(); i++) {
            JPanel productPanel = (JPanel) pnProducts.getComponent(i);
            JPanel leftPanel = (JPanel) productPanel.getComponent(0);
            JCheckBox itemCheckBox = (JCheckBox) leftPanel.getComponent(0);

            if (itemCheckBox.isSelected()) {
                JPanel infoPanel = (JPanel) productPanel.getComponent(1);
                JPanel quantityPanel = (JPanel) infoPanel.getComponent(2); // panel chứa nút - số + 

                JLabel lblQuantity = (JLabel) quantityPanel.getComponent(2); // số lượng là component giữa
                int soLuong = Integer.parseInt(lblQuantity.getText());

                GioHang item = this.cartItems.get(i); // lấy đúng item tương ứng
                total += item.getGiaban() * soLuong;
            }
        }

        if (total > 0) {
            lblTotal.setText(String.format("Tổng cộng: %,.0f VND", total));
        } else {
            lblTotal.setText("Tổng cộng: 0 VND");
        }
    }

    public List<GioHang> getSelectedCartItems() {
        List<GioHang> selectedItems = new ArrayList<>();

        // Lặp qua tất cả các sản phẩm trong giỏ hàng
        for (int i = 0; i < pnProducts.getComponentCount(); i++) {
            JPanel productPanel = (JPanel) pnProducts.getComponent(i);
            JPanel leftPanel = (JPanel) productPanel.getComponent(0);
            JCheckBox itemCheckBox = (JCheckBox) leftPanel.getComponent(0);

            // Kiểm tra xem sản phẩm có được chọn hay không
            if (itemCheckBox.isSelected()) {
                selectedItems.add(cartItems.get(i)); // Thêm sản phẩm vào danh sách
            }
        }

        return selectedItems;
    }

    public void addRemoveItemListener(ActionListener listener) {
        for (java.awt.Component comp : pnProducts.getComponents()) {
            if (comp instanceof JPanel productPanel) {
                java.awt.Component[] innerComps = productPanel.getComponents();
                for (java.awt.Component innerComp : innerComps) {
                    if (innerComp instanceof JPanel infoPanel) {
                        for (java.awt.Component subComp : infoPanel.getComponents()) {
                            if (subComp instanceof JPanel removePanel) {
                                for (java.awt.Component btnComp : removePanel.getComponents()) {
                                    if (btnComp instanceof JButton button && "Xóa".equals(button.getText())) {
                                        button.addActionListener(listener);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addCheckAllListener() {
        cbxAll.addActionListener(e -> {
            boolean isSelected = cbxAll.isSelected();

            for (Component comp : pnProducts.getComponents()) {
                if (comp instanceof JPanel productPanel) {
                    Component[] productComponents = productPanel.getComponents();
                    for (Component inner : productComponents) {
                        if (inner instanceof JPanel leftPanel) {
                            for (Component sub : leftPanel.getComponents()) {
                                if (sub instanceof JCheckBox cbxItem) {
                                    System.out.println("Setting cbxItem = " + isSelected);
                                    cbxItem.setSelected(isSelected); // ✅ Đặt lại trạng thái
                                }
                            }
                        }
                    }
                }
            }

            updateTotal(cartItems); // ✅ Cập nhật lại tổng giá
        });
    }

    private void addItemCheckBoxListener(JCheckBox cbxItem, GioHang item) {
        cbxItem.addActionListener(e -> updateTotal(getCartItems()));
    }

    public interface QuantityChangeListener {

        void onQuantityChanged(int maGH, int newQuantity, int maSP);
    }

    public void removePaidItems(List<GioHang> paidItems) {
        cartItems.removeAll(paidItems); // Xóa các mục đã thanh toán khỏi danh sách giỏ hàng
        displayCartItems(cartItems); // Cập nhật lại giao diện
    }

    public void addCheckoutListener(ActionListener listener) {
        btnCheckout.addActionListener(listener);
    }

    // Thêm getter cho nút thanh toán (nếu cần)
    public JButton getCheckoutButton() {
        return btnCheckout;
    }

    public void updateLocalQuantity(int maGH, int newQty) {
        for (GioHang gh : cartItems) {
            if (gh.getMaGH() == maGH) {
                gh.setSoLuong(newQty);
                break;
            }
        }
    }
}
