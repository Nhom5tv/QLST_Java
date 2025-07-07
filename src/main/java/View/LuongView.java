package View;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class LuongView extends JPanel {
    public JTextField txtTimKiem, txtLuongMoiGio, txtGhiChu, txtThuongPhat;
    public JComboBox<String> cboThang, cboNam;
    public JButton btnSua, btnXuatExcel, btnReset, btnLuuTatCa;
    public JTable tblLuong;
    public DefaultTableModel tableModel;

    public LuongView() {
        setLayout(new BorderLayout());

        // Top panel: tìm kiếm + nút
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTimKiem = new JTextField(20);
        cboThang = new JComboBox<>();
        cboNam = new JComboBox<>();
        btnSua = new JButton("Lưu");
        btnLuuTatCa = new JButton("Lưu tất cả");
        btnXuatExcel = new JButton("Xuất Excel");
        btnReset = new JButton("Reset");

        topPanel.add(new JLabel("Tìm:"));
        topPanel.add(txtTimKiem);
        topPanel.add(new JLabel("Tháng:"));
        topPanel.add(cboThang);
        topPanel.add(new JLabel("Năm:"));
        topPanel.add(cboNam);
        topPanel.add(btnSua);
        topPanel.add(btnLuuTatCa);
        topPanel.add(btnXuatExcel);
        topPanel.add(btnReset);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Mã NV", "Họ tên", "Tổng giờ làm", "Lương mỗi giờ", "Thưởng/Phạt", "Lương", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0);
        tblLuong = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblLuong);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom form
        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        txtLuongMoiGio = new JTextField();
        txtGhiChu = new JTextField();
        txtThuongPhat = new JTextField();
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Thông tin lương"));

        bottomPanel.add(new JLabel("Lương mỗi giờ:"));
        bottomPanel.add(txtLuongMoiGio);
        bottomPanel.add(new JLabel("Ghi chú:"));
        bottomPanel.add(txtGhiChu);
        bottomPanel.add(new JLabel("Thưởng/Phạt:"));
        bottomPanel.add(txtThuongPhat);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
