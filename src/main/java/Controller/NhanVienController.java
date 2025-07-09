/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import DAO.NhanVienDAOplus;
import View.TrangChuView;
import Model.NhanVien;
import view.NhanVienView;

public class NhanVienController {
    private NhanVienView nvview;
    private NhanVienDAOplus dao;
    private TrangChuView stmv;
    private List<NhanVien> danhSachNhanVien;
    private byte[] currentImageBytes = null;

    public NhanVienController(TrangChuView view, NhanVienView panel) {
        this.stmv = view;
        this.nvview = panel;
        this.dao = new NhanVienDAOplus();
        init();
    }

    private void init() {
        loadList();

        nvview.listNhanVien.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = nvview.listNhanVien.getSelectedIndex();
                if (index >= 0 && index < danhSachNhanVien.size()) {
                    showNhanVien(danhSachNhanVien.get(index));
                }
            }
        });

        nvview.btnChonAnh.addActionListener(e -> chonAnh());
        nvview.btnAdd.addActionListener(e -> themNhanVien());
        nvview.btnUpdate.addActionListener(e -> capNhatNhanVien());
        nvview.btnDelete.addActionListener(e -> xoaNhanVien());
    }

    private void loadList() {
        danhSachNhanVien = dao.getAllNhanVien();
        DefaultListModel model = nvview.listModel;
        model.clear();
        for (NhanVien nv : danhSachNhanVien) {
            model.addElement(nv.getHoten());
        }
    }

    private void showNhanVien(NhanVien nv) {
        nvview.txtId.setText(String.valueOf(nv.getma_nv()));
        nvview.txtHoten.setText(nv.getHoten());
        nvview.txtCCCD.setText(nv.getCccd());
        nvview.txtSDT.setText(nv.getSdt());
        nvview.txtChucvu.setText(nv.getChucvu());
        nvview.txtEmail.setText(nv.getEmail());
        nvview.cbGioitinh.setSelectedItem(nv.getGioitinh());
        nvview.txtGhichu.setText(nv.getGhichu());
        currentImageBytes = nv.getAnh();
        showImage(currentImageBytes);
    }

    private void showImage(byte[] imageData) {
        if (imageData != null) {
            try {
                InputStream in = new ByteArrayInputStream(imageData);
                Image img = ImageIO.read(in).getScaledInstance(
                        nvview.lblImage.getWidth(), nvview.lblImage.getHeight(), Image.SCALE_SMOOTH);
                nvview.lblImage.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            nvview.lblImage.setIcon(null);
        }
    }

    private void chonAnh() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(nvview); // nvview là JPanel
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                FileInputStream fis = new FileInputStream(file);
                currentImageBytes = fis.readAllBytes();
                showImage(currentImageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void themNhanVien() {
        NhanVien nv = getNhanVienFromForm();
        if (dao.insertNhanVien(nv)) {
            JOptionPane.showMessageDialog(nvview, "Thêm thành công!");
            loadList();
        } else {
            JOptionPane.showMessageDialog(nvview, "Thêm thất bại!");
        }
    }

    private void capNhatNhanVien() {
        if (nvview.txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(nvview, "Vui lòng chọn nhân viên để cập nhật!");
            return;
        }
        NhanVien nv = getNhanVienFromForm();
        nv.setma_nv(Integer.parseInt(nvview.txtId.getText()));
        if (dao.updateNhanVien(nv)) {
            JOptionPane.showMessageDialog(nvview, "Cập nhật thành công!");
            loadList();
        } else {
            JOptionPane.showMessageDialog(nvview, "Cập nhật thất bại!");
        }
    }

    private void xoaNhanVien() {
        if (nvview.txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(nvview, "Vui lòng chọn nhân viên để xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(nvview, "Bạn có chắc chắn muốn xóa?");
        if (confirm == JOptionPane.YES_OPTION) {
            int ma_nv = Integer.parseInt(nvview.txtId.getText());
            if (dao.deleteNhanVien(ma_nv)) {
                JOptionPane.showMessageDialog(nvview, "Xóa thành công!");
                loadList();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(nvview, "Xóa thất bại!");
            }
        }
    }

    private NhanVien getNhanVienFromForm() {
        NhanVien nv = new NhanVien();
        nv.setHoten(nvview.txtHoten.getText());
        nv.setCccd(nvview.txtCCCD.getText());
        nv.setSdt(nvview.txtSDT.getText());
        nv.setChucvu(nvview.txtChucvu.getText());
        nv.setEmail(nvview.txtEmail.getText());
        nv.setGioitinh(nvview.cbGioitinh.getSelectedItem().toString());
        nv.setGhichu(nvview.txtGhichu.getText());
        nv.setAnh(currentImageBytes);
        return nv;
    }

    private void clearForm() {
        nvview.txtId.setText("");
        nvview.txtHoten.setText("");
        nvview.txtCCCD.setText("");
        nvview.txtSDT.setText("");
        nvview.txtChucvu.setText("");
        nvview.txtEmail.setText("");
        nvview.cbGioitinh.setSelectedIndex(0);
        nvview.txtGhichu.setText("");
        nvview.lblImage.setIcon(null);
        currentImageBytes = null;
    }
}
