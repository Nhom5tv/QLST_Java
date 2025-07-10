package DAO;

import Model.SanPham;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class SanPhamDAO implements DAO<SanPham> {

    private Connection conn;

    public SanPhamDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, COALESCE(SUM(tk.so_luong_ton), 0) AS so_luong_ton FROM san_pham sp LEFT JOIN ton_kho tk ON sp.ma_san_pham = tk.ma_san_pham GROUP BY sp.ma_san_pham";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPham sp = extract(rs);
                sp.setSoLuongTon(rs.getInt("so_luong_ton"));
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteByMaNgoai(String maNgoai) {
        String sql = "DELETE FROM san_pham WHERE ma_ngoai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNgoai);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<SanPham> getById(int id) {
        String sql = "SELECT sp.*, COALESCE(SUM(tk.so_luong_ton), 0) AS so_luong_ton " +
                "FROM san_pham sp " +
                "LEFT JOIN ton_kho tk ON sp.ma_san_pham = tk.ma_san_pham " +
                "WHERE sp.ma_san_pham = ? " +
                "GROUP BY sp.ma_san_pham";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = extract(rs);
                    sp.setSoLuongTon(rs.getInt("so_luong_ton"));
                    return Optional.of(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean insert(SanPham sp) {
        String sql = "INSERT INTO san_pham (ma_ngoai, ten_san_pham, ma_danh_muc, gia_goc, gia_ban, ma_vach, hinh_anh, don_vi_tinh, mo_ta, trang_thai, thuong_hieu, xuat_xu, thanh_phan, huong_dan_su_dung, bao_quan) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, sp);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertAndReturnId(SanPham sp) {
        String sql = "INSERT INTO san_pham (ma_ngoai, ten_san_pham, ma_danh_muc, gia_goc, gia_ban, ma_vach, hinh_anh, don_vi_tinh, mo_ta, trang_thai, thuong_hieu, xuat_xu, thanh_phan, huong_dan_su_dung, bao_quan) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillParams(ps, sp);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateMaNgoai(SanPham sp) {
        String sql = "UPDATE san_pham SET ma_ngoai = ? WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getMaNgoai());
            ps.setInt(2, sp.getMaSanPham());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(SanPham sp) {
        String sql = "UPDATE san_pham SET ma_ngoai = ?,ten_san_pham=?, ma_danh_muc=?, gia_goc=?, gia_ban=?, ma_vach=?, hinh_anh=?, don_vi_tinh=?, mo_ta=?, trang_thai=?, thuong_hieu=?, xuat_xu=?, thanh_phan=?, huong_dan_su_dung=?, bao_quan=? WHERE ma_san_pham=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParamsForUpdate(ps, sp);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM san_pham WHERE ma_san_pham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<SanPham> searchWithTonKho(String keyword) {
        List<SanPham> list = new ArrayList<>();
        String sql = """
        SELECT sp.*, 
               COALESCE(SUM(tk.so_luong_ton), 0) AS so_luong_ton,
               COALESCE(SUM(tk.so_luong_kha_dung), 0) AS so_luong_kha_dung
        FROM san_pham sp 
        LEFT JOIN ton_kho tk ON sp.ma_san_pham = tk.ma_san_pham 
        WHERE LOWER(sp.ten_san_pham) LIKE ? 
           OR LOWER(sp.ma_ngoai) LIKE ? 
           OR LOWER(sp.ma_vach) LIKE ? 
        GROUP BY sp.ma_san_pham
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = extract(rs);
                    sp.setSoLuongTon(rs.getInt("so_luong_ton")); // vẫn giữ để hiển thị nội bộ
                    sp.setSoLuongKhaDung(rs.getInt("so_luong_kha_dung")); // mới thêm vào
                    list.add(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SanPham> timKiem(String keyword) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM san_pham WHERE ten_san_pham LIKE ? OR ma_san_pham LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            stmt.setString(1, key);
            stmt.setString(2, key);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham(
                        rs.getInt("ma_san_pham"),
                        rs.getString("ten_san_pham"),
                        rs.getBigDecimal("gia_goc"),
                        rs.getBigDecimal("gia_ban")
                );
                list.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private SanPham extract(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getInt("ma_san_pham"));
        sp.setMaNgoai(rs.getString("ma_ngoai"));
        sp.setTenSanPham(rs.getString("ten_san_pham"));
        sp.setMaDanhMuc(rs.getInt("ma_danh_muc"));
        sp.setGiaGoc(rs.getBigDecimal("gia_goc"));
        sp.setGiaBan(rs.getBigDecimal("gia_ban"));
        sp.setMaVach(rs.getString("ma_vach"));
        byte[] hinhAnh = rs.getBytes("hinh_anh");
        sp.setHinhAnh(hinhAnh);
        sp.setDonViTinh(rs.getString("don_vi_tinh"));
        sp.setMoTa(rs.getString("mo_ta"));
        sp.setTrangThai(rs.getInt("trang_thai"));
        sp.setThuongHieu(rs.getString("thuong_hieu"));
        sp.setXuatXu(rs.getString("xuat_xu"));
        sp.setThanhPhan(rs.getString("thanh_phan"));
        sp.setHuongDanSuDung(rs.getString("huong_dan_su_dung"));
        sp.setBaoQuan(rs.getString("bao_quan"));
        return sp;
    }

    private void fillParams(PreparedStatement ps, SanPham sp) throws SQLException {
        ps.setString(1, sp.getMaNgoai());
        ps.setString(2, sp.getTenSanPham());
        ps.setInt(3, sp.getMaDanhMuc());
        ps.setBigDecimal(4, sp.getGiaGoc());
        ps.setBigDecimal(5, sp.getGiaBan());
        ps.setString(6, sp.getMaVach());
        ps.setBytes(7, sp.getHinhAnh());
        ps.setString(8, sp.getDonViTinh());
        ps.setString(9, sp.getMoTa());
        ps.setInt(10, sp.getTrangThai());
        ps.setString(11, sp.getThuongHieu());
        ps.setString(12, sp.getXuatXu());
        ps.setString(13, sp.getThanhPhan());
        ps.setString(14, sp.getHuongDanSuDung());
        ps.setString(15, sp.getBaoQuan());
    }

    private void fillParamsForUpdate(PreparedStatement ps, SanPham sp) throws SQLException {
        fillParams(ps, sp); // dùng lại 1-15
        ps.setInt(16, sp.getMaSanPham()); // thêm id ở vị trí 16
    }

    public List<SanPham> getByTrangThai(int trangThai) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM san_pham WHERE trang_thai = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trangThai);
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

    public List<SanPham> searchWithFilters(String keyword, String tenDanhMuc, int trangThai) {
        List<SanPham> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT sp.*, 
               COALESCE(SUM(tk.so_luong_ton), 0) AS so_luong_ton,
               COALESCE(SUM(tk.so_luong_kha_dung), 0) AS so_luong_kha_dung,
               dm.ten_danh_muc
        FROM san_pham sp
        LEFT JOIN ton_kho tk ON sp.ma_san_pham = tk.ma_san_pham
        LEFT JOIN danh_muc_san_pham dm ON sp.ma_danh_muc = dm.ma_danh_muc
        WHERE (LOWER(sp.ten_san_pham) LIKE ? 
            OR LOWER(sp.ma_ngoai) LIKE ? 
            OR LOWER(sp.ma_vach) LIKE ?)
    """);

        if (!tenDanhMuc.equals("Tất cả")) {
            sql.append(" AND dm.ten_danh_muc = ?");
        }
        if (trangThai != -1) {
            sql.append(" AND sp.trang_thai = ?");
        }

        sql.append(" GROUP BY sp.ma_san_pham");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            String like = "%" + keyword.toLowerCase() + "%";
            ps.setString(index++, like);
            ps.setString(index++, like);
            ps.setString(index++, like);

            if (!tenDanhMuc.equals("Tất cả")) {
                ps.setString(index++, tenDanhMuc);
            }
            if (trangThai != -1) {
                ps.setInt(index++, trangThai);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = extract(rs);
                    sp.setSoLuongTon(rs.getInt("so_luong_ton"));
                    sp.setSoLuongKhaDung(rs.getInt("so_luong_kha_dung"));
                    sp.setTenDanhMuc(rs.getString("ten_danh_muc"));
                    list.add(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
