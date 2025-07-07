package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Dialog để thêm hoặc sửa thông tin nhà cung cấp
 * @author DUNG LE
 */
public class NhaCungCapDialog extends JDialog {

    private JTextField txtId, txtName, txtLhName, txtEmail, txtPhone, txtAddress;
    private JButton btnLuu, btnHuy;
    private boolean confirmed = false;

    public NhaCungCapDialog(JFrame parent, String title) {
        super(parent, title, true);

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(420, 380);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel[] labels = {
                
                new JLabel("Tên NCC:"),new JLabel("Người liên hệ:"), new JLabel("Email:"),
                new JLabel("Số điện thoại:"), new JLabel("Địa chỉ:")
        };

        
        txtName = new JTextField();
        txtLhName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtAddress = new JTextField();

        JTextField[] fields = {txtName, txtLhName, txtEmail, txtPhone, txtAddress};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            mainPanel.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            mainPanel.add(fields[i], gbc);
        }

        // Button Panel
        btnLuu = new JButton("💾 Lưu");
        btnHuy = new JButton("❌ Hủy");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        btnLuu.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnHuy.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmed = false;
            }
        });
    }

    // Thiết lập dữ liệu khi sửa
    public void setFields(String name, String lhName, String email, String phone, String address) {
//        txtId.setText(id);
        txtName.setText(name);
        txtLhName.setText(lhName);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtAddress.setText(address);
    }

    // Getter dữ liệu người dùng nhập
    public String getId() {
        return txtId.getText().trim();
    }
    public void setId(String id) {
    txtId.setText(id);
}
    public String getName() {
        return txtName.getText().trim();
    }

    public String getLhName() {
        return txtLhName.getText().trim();
    }

    public String getEmail() {
        return txtEmail.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public String getAddress() {
        return txtAddress.getText().trim();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
