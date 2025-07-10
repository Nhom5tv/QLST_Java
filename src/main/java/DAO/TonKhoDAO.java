package DAO;

import Model.TonKho;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TonKhoDAO implements DAO<TonKho> {
    private final Connection conn;

    public TonKhoDAO(Connection conn) {
        this.conn = conn;
    }

    // ==== 1. Truy vấn dữ liệu ====

    @Override
    public List<TonKho> getAll() {
        List<TonKho> list = new ArrayList<>();
        String sql = """
            SELECT tk.*, sp.ten_san_pham, lh.han_su_dung
            FROM ton_kho tk
            JOIN san_pham sp ON tk.ma_san_pham = sp.ma_san_pham
            JOIN lo_hang lh ON tk.ma_lo = lh.ma_lo_hang
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TonKho tk = extract(rs);
                tk.setTenSanPham(rs.getString("ten_san_pham"));
                tk.setHanSuDung(rs.getDate("han_su_dung"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TonKho> getBySanPham(int maSanPham) {
        List<TonKho> list = new ArrayList<>();
        String sql = "SELECT * FROM ton_kho WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<TonKho> getById(int id) {
        return Optional.empty(); // Khóa chính là (ma_san_pham, ma_lo)
    }

    // ==== 2. Thêm, sửa, xóa dữ liệu ====

    @Override
    public boolean insert(TonKho tk) {
        String sql = """
            INSERT INTO ton_kho (
                ma_san_pham, ma_lo, so_luong_ton, so_luong_tren_ke,
                so_luong_trong_kho, so_luong_kha_dung, so_luong_dang_giao_dich, nguong_canh_bao
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, tk);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(TonKho tk) {
        String sql = """
            UPDATE ton_kho SET
                so_luong_ton=?, so_luong_tren_ke=?, so_luong_trong_kho=?,
                so_luong_kha_dung=?, so_luong_dang_giao_dich=?, nguong_canh_bao=?
            WHERE ma_san_pham=? AND ma_lo=?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tk.getSoLuongTon());
            ps.setInt(2, tk.getSoLuongTrenKe());
            ps.setInt(3, tk.getSoLuongTrongKho());
            ps.setInt(4, tk.getSoLuongKhaDung());
            ps.setInt(5, tk.getSoLuongDangGiaoDich());
            ps.setInt(6, tk.getNguongCanhBao());
            ps.setInt(7, tk.getMaSanPham());
            ps.setInt(8, tk.getMaLo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    public boolean delete(int maSanPham, int maLo) {
        String sql = "DELETE FROM ton_kho WHERE ma_san_pham = ? AND ma_lo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSanPham);
            ps.setInt(2, maLo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==== 3. Nghiệp vụ tồn kho ====

    public boolean chuyenHangRaKe(int maSP, int maLo, int soLuong) {
        String sql = """
            UPDATE ton_kho
            SET so_luong_trong_kho = so_luong_trong_kho - ?,
                so_luong_tren_ke = so_luong_tren_ke + ?
            WHERE ma_san_pham = ? AND ma_lo = ? AND so_luong_trong_kho >= ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setInt(2, soLuong);
            ps.setInt(3, maSP);
            ps.setInt(4, maLo);
            ps.setInt(5, soLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // lấy ra danh sách các mã lô để trừ tòn kho
    public List<TonKho> getDanhSachLoTruTon_TonKho(int maSP, int soLuongCan) {
        List<TonKho> danhSach = new ArrayList<>();
        String sql = """
            SELECT tk.ma_lo, tk.so_luong_tren_ke
            FROM ton_kho tk
            JOIN lo_hang lh ON tk.ma_lo = lh.ma_lo_hang
            WHERE tk.ma_san_pham = ? AND tk.so_luong_tren_ke > 0
            ORDER BY lh.han_su_dung ASC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();

            int remaining = soLuongCan;
            while (rs.next() && remaining > 0) {
                int maLo = rs.getInt("ma_lo");
                int soLuongTrongLo = rs.getInt("so_luong_tren_ke");

                int soLuongTru = Math.min(remaining, soLuongTrongLo);
                danhSach.add(new TonKho(maSP, maLo, soLuongTru));

                remaining -= soLuongTru;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    // Giảm tồn kho sau khi nhấn nút thanh toán bên siêu thị (thu ngân)
    public boolean giamTonTheoTonKho(TonKho tk) {
        int sl = tk.getSoLuongTon(); // đang dùng làm số lượng cần trừ
        String sql = """
            UPDATE ton_kho
            SET so_luong_tren_ke = so_luong_tren_ke - ?,
                so_luong_kha_dung = so_luong_kha_dung - ?,
                so_luong_ton = so_luong_ton - ?
            WHERE ma_san_pham = ? AND ma_lo = ? AND
                  so_luong_tren_ke >= ? AND so_luong_kha_dung >= ? AND so_luong_ton >= ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 3; i++) ps.setInt(i, sl);
            ps.setInt(4, tk.getMaSanPham());
            ps.setInt(5, tk.getMaLo());
            for (int i = 6; i <= 8; i++) ps.setInt(i, sl);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // kiểm tra số lượng còn có thể đặt mua của sản phẩm
    public int getTongSoLuongKhaDung(int maSP) {
        String sql = "SELECT SUM(so_luong_kha_dung) AS tong FROM ton_kho WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("tong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    
    // Cập nhật lại kho sau khi khách hàng bấm đặt hàng online
    public boolean capNhatSauDatHang(int maSP, int maLo, int soLuong) {
        String sql = """
            UPDATE ton_kho
            SET so_luong_kha_dung = so_luong_kha_dung - ?,
                so_luong_dang_giao_dich = so_luong_dang_giao_dich + ?
            WHERE ma_san_pham = ? AND ma_lo = ? AND so_luong_kha_dung >= ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setInt(2, soLuong);
            ps.setInt(3, maSP);
            ps.setInt(4, maLo);
            ps.setInt(5, soLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // khi đơn hàng đã giao thành công trạng thái "Hoàn thành"
    public boolean hoanTatDonHang(int maSP, int maLo, int soLuong) {
        String sql = """
            UPDATE ton_kho
            SET so_luong_trong_kho = so_luong_trong_kho - ?,
                so_luong_dang_giao_dich = so_luong_dang_giao_dich - ?,
                so_luong_ton = so_luong_ton - ?
            WHERE ma_san_pham = ? AND ma_lo = ? AND
                  so_luong_trong_kho >= ? AND so_luong_dang_giao_dich >= ? AND so_luong_ton >= ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 3; i++) ps.setInt(i, soLuong);
            ps.setInt(4, maSP);
            ps.setInt(5, maLo);
            for (int i = 6; i <= 8; i++) ps.setInt(i, soLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean huyDonHang(int maSP, int maLo, int soLuong) {
        String sql = """
            UPDATE ton_kho
            SET so_luong_kha_dung = so_luong_kha_dung + ?,
                so_luong_dang_giao_dich = so_luong_dang_giao_dich - ?
            WHERE ma_san_pham = ? AND ma_lo = ? AND so_luong_dang_giao_dich >= ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLuong);
            ps.setInt(2, soLuong);
            ps.setInt(3, maSP);
            ps.setInt(4, maLo);
            ps.setInt(5, soLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Hủy đơn: SP=" + maSP + ", lô=" + maLo + ", SL=" + soLuong);
        return false;
    }

    // ==== 4. Helper ====

    private TonKho extract(ResultSet rs) throws SQLException {
        TonKho tk = new TonKho();
        tk.setMaSanPham(rs.getInt("ma_san_pham"));
        tk.setMaLo(rs.getInt("ma_lo"));
        tk.setSoLuongTon(rs.getInt("so_luong_ton"));
        tk.setSoLuongTrenKe(rs.getInt("so_luong_tren_ke"));
        tk.setSoLuongTrongKho(rs.getInt("so_luong_trong_kho"));
        tk.setSoLuongKhaDung(rs.getInt("so_luong_kha_dung"));
        tk.setSoLuongDangGiaoDich(rs.getInt("so_luong_dang_giao_dich"));
        tk.setNguongCanhBao(rs.getInt("nguong_canh_bao"));
        return tk;
    }

    private void fillParams(PreparedStatement ps, TonKho tk) throws SQLException {
        ps.setInt(1, tk.getMaSanPham());
        ps.setInt(2, tk.getMaLo());
        ps.setInt(3, tk.getSoLuongTon());
        ps.setInt(4, tk.getSoLuongTrenKe());
        ps.setInt(5, tk.getSoLuongTrongKho());
        ps.setInt(6, tk.getSoLuongKhaDung());
        ps.setInt(7, tk.getSoLuongDangGiaoDich());
        ps.setInt(8, tk.getNguongCanhBao());
    }

    // lấy ra danh sách các mã lô để trừ tòn kho
    public List<TonKho> getLoTruTon_TonKho(int maSP, int soLuongCan) {
        List<TonKho> danhSach = new ArrayList<>();
        String sql = """
            SELECT tk.ma_lo, tk.so_luong_kha_dung
            FROM ton_kho tk
            JOIN lo_hang lh ON tk.ma_lo = lh.ma_lo_hang
            WHERE tk.ma_san_pham = ? AND tk.so_luong_kha_dung > 0
            ORDER BY lh.han_su_dung ASC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maSP);
            ResultSet rs = ps.executeQuery();

            int remaining = soLuongCan;
            while (rs.next() && remaining > 0) {
                int maLo = rs.getInt("ma_lo");
                int soLuongTrongLo = rs.getInt("so_luong_kha_dung");

                int soLuongTru = Math.min(remaining, soLuongTrongLo);
                danhSach.add(new TonKho(maSP, maLo, soLuongTru));

                remaining -= soLuongTru;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }
}
