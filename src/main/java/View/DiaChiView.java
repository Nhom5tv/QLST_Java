/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

//import Controller.DiaChiController;
import DAO.DiaChiDAO;
import Model.DiaChi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;
/**
 *
 * @author Admin
 */
public class DiaChiView extends JDialog {
    private int maKH;
    private String selectedName;
    private String selectedPhone;
    private String selectedAddress;
    public ActionListener onAddressSelected;
    private JButton updateBtn;
    private JButton addBtn;
    private JButton removeBtn;
    private JButton confirmBtn;
    private JPanel mainPanel;

    public DiaChiView(JFrame parent, int maKH, ActionListener onAddressSelected) {
        super(parent, "Địa Chỉ Của Tôi", true);
        this.maKH = maKH;
        this.onAddressSelected = onAddressSelected;
        System.out.println("MaKH in DiaChiView: " + maKH);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);

        addBtn = new JButton("+ Thêm Địa Chỉ Mới");
        confirmBtn = new JButton("Xác nhận");
        
//        confirmBtn.addActionListener(e -> {
//            // Assuming the selected radio button is the one that was clicked
//            if (selectedName != null && selectedPhone != null && selectedAddress != null) {
//                // Pass the selected address details to the onAddressSelected action
//                onAddressSelected.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, 
//                    selectedName + ";" + selectedPhone + ";" + selectedAddress));
//                // Close the DiaChiView dialog
//                this.dispose();
//            } else {
//                showMessage("Vui lòng chọn một địa chỉ.");
//            }
//        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(addBtn);
        bottomPanel.add(confirmBtn);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }
    public void setConfirmBtnListener(ActionListener listener) {
        confirmBtn.addActionListener(listener);
    }
    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getSelectedPhone() {
        return selectedPhone;
    }

    public void setSelectedPhone(String selectedPhone) {
        this.selectedPhone = selectedPhone;
    }

    public String getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(String selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    // Phương thức để thêm địa chỉ vào giao diện
    public void addAddressItem(DiaChi dc, ActionListener onSelected, ActionListener onUpdate, ActionListener onRemove) {
        ButtonGroup group = new ButtonGroup();
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        itemPanel.setPreferredSize(new Dimension(550, 80));

        JRadioButton radio = new JRadioButton();
        group.add(radio);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(dc.getName() + " (" + dc.getPhoneNumber() + ")"));
        infoPanel.add(new JLabel(dc.getDetailAddress()));

        JPanel pnButton = new JPanel();

        updateBtn = new JButton("Cập nhật");
        updateBtn.setBorderPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setForeground(Color.BLUE);
        pnButton.add(updateBtn);

        removeBtn = new JButton("Xóa");
        removeBtn.setBorderPainted(false);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setForeground(Color.RED);
        pnButton.add(removeBtn);

        radio.addActionListener(e -> {
            selectedName = dc.getName();
            selectedPhone = dc.getPhoneNumber();
            selectedAddress = dc.getDetailAddress();
        });

        updateBtn.addActionListener(onUpdate);
        removeBtn.addActionListener(onRemove);

        itemPanel.add(radio, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(pnButton, BorderLayout.EAST);

        mainPanel.add(itemPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Getter và Setter
    public JButton getAddBtn() {
        return addBtn;
    }

    public JButton getConfirmBtn() {
        return confirmBtn;
    }

    public int getCustomerId() {
        System.out.println("Customer ID from View: " + maKH);
        return maKH;
    }
    

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    public void clearAddresses() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
