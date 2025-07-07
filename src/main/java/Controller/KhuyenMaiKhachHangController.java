package Controller;

import Model.GioHang;
import Model.KhuyenMai;
import View.KhuyenMaiKhachHangView;
import DAO.KhuyenMaiDAO;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class KhuyenMaiKhachHangController {
    private final KhuyenMaiKhachHangView view;
    private final KhuyenMaiDAO dao;
    private final List<GioHang> selectedItems;
    private Consumer<String> onVoucherAppliedListener;
    private int MaKH;

    // Cho phép form chính truyền vào hàm callback nhận mã khuyến mãi
    public void setOnVoucherAppliedListener(Consumer<String> listener) {
        this.onVoucherAppliedListener = listener;
    }

    public KhuyenMaiKhachHangController(KhuyenMaiKhachHangView view, List<GioHang> selectedItems,int MaKH) {
        this.view = view;
        this.dao = new KhuyenMaiDAO();
        this.selectedItems = selectedItems;
        this.MaKH= MaKH;
        System.out.println("Mã Khach Hang: "+ MaKH);
        loadData(null);
        initController();
    }
    
    public KhuyenMaiDAO getDao() {
        return dao;
    }
    

    private void initController() {
        // Xử lý khi nhấn nút "Tìm kiếm"
        view.addSearchListener(e -> {
            String maKM = view.getTxtTimKiem().getText().trim();
            if (maKM.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập mã khuyến mãi", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loadData(maKM); // Lọc danh sách mã khuyến mãi theo mã được nhập
        });

        // Xử lý khi nhấn nút "Áp dụng"
        view.addApplyListener(e -> {
            String maKM = view.getSelectedVoucher(); // Lấy mã từ dòng đang chọn trong bảng
            if (maKM == null) {
                JOptionPane.showMessageDialog(view, "Bạn chưa chọn mã khuyến mãi trong bảng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KhuyenMai khuyenMai = dao.getById(maKM);
            if (khuyenMai == null) {
                JOptionPane.showMessageDialog(view, "Mã khuyến mãi không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal tongTien = BigDecimal.ZERO;
            int tongSoLuong = 0;
            for (GioHang item : selectedItems) {
                if (item != null) {
                    tongTien = tongTien.add(BigDecimal.valueOf(item.getGiaban())
                                  .multiply(BigDecimal.valueOf(item.getSoLuong())));
                    tongSoLuong += item.getSoLuong();
                }
            }

            // Kiểm tra điều kiện áp dụng
            String error = kiemTraDieuKienApDung(maKM, tongTien, tongSoLuong);
            if (error != null) {
                JOptionPane.showMessageDialog(view, error, "Không thể áp dụng", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = dao.ghiNhanSuDungVoucher(maKM,MaKH);
            if (success) {
                JOptionPane.showMessageDialog(view, "Áp dụng mã khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData(null); // Cập nhật lại danh sách khuyến mãi

                // Gọi callback để gửi mã về form chính
                if (onVoucherAppliedListener != null) {
                    onVoucherAppliedListener.accept(maKM);
                }

                // view.dispose(); // Bỏ comment nếu muốn đóng view
            } else {
                JOptionPane.showMessageDialog(view, "Áp dụng thất bại! Mã có thể đã hết lượt.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // ✅ Đưa hàm kiểm tra điều kiện ra ngoài initController
    public String kiemTraDieuKienApDung(String maKM, BigDecimal tongTienHoaDon, int soLuongSanPham) {
        if (maKM == null || maKM.isBlank()) {
            return "Mã khuyến mãi không được để trống.";
        }

        KhuyenMai km = dao.getById(maKM);
        if (km == null) {
            return "Mã khuyến mãi không tồn tại.";
        }

        Date now = new Date();
        if (now.before(km.getNgayBatDau()) || now.after(km.getNgayKetThuc())) {
            return "Mã khuyến mãi không còn hiệu lực.";
        }

        if (km.getSoLuong() <= 0) {
            return "Mã khuyến mãi đã hết lượt sử dụng.";
        }

        if (km.getTongTienToiThieu() != null &&
                tongTienHoaDon.compareTo(km.getTongTienToiThieu()) < 0) {
            return "Tổng tiền đơn hàng chưa đạt mức tối thiểu áp dụng.";
        }

        if (km.getSoLuongSpToiThieu() != null &&
                soLuongSanPham < km.getSoLuongSpToiThieu()) {
            return "Số lượng sản phẩm chưa đủ điều kiện áp dụng.";
        }

        return null;
    }

    private void loadData(String keyword) {
    List<KhuyenMai> danhSach;

    if (keyword == null || keyword.isBlank()) {
        // Lấy danh sách khuyến mãi còn lượt
        danhSach = dao.getAllKhuyenMaiKhachHang();
    } else {
        // Tìm kiếm, sau đó lọc chỉ những khuyến mãi còn lượt
        danhSach = dao.searchKhuyenMai(keyword)
                .stream()
                .filter(km -> km.getSoLuong() > 0)
                .toList();
    }

    view.addKhuyenMaiToTable(danhSach);
}

}
