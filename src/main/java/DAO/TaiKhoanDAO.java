package DAO;



import Bridge.DBConnection;
import Model.TaiKhoan;
import Model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    // Lấy tất cả tài khoản, join 2 bảng nhân viên và khách hàng để lấy tên
    private static final String SQL_SELECT_ALL = 
        "SELECT tk.*, " +
        "nv.ma_nv, nv.ten_nv, " +
        "kh.maKH, kh.hoTen AS ten_khachhang " +
        "FROM qltaikhoan tk " +
        "LEFT JOIN nhanvien nv ON tk.id_lien_ket = nv.ma_nv AND tk.quyen = 'nhanvien' " +
        "LEFT JOIN khachhang kh ON tk.id_lien_ket = kh.maKH AND tk.quyen = 'khachhang'";

    // Tìm theo ID tài khoản
    private static final String SQL_SELECT_BY_ID = 
        "SELECT tk.*, " +
        "nv.ma_nv, nv.ten_nv, " +
        "kh.maKH, kh.hoTen AS ten_khachhang " +
        "FROM qltaikhoan tk " +
        "LEFT JOIN nhanvien nv ON tk.id_lien_ket = nv.ma_nv AND tk.quyen = 'nhanvien' " +
        "LEFT JOIN khachhang kh ON tk.id_lien_ket = kh.maKH AND tk.quyen = 'khachhang' " +
        "WHERE tk.ma_tai_khoan = ?";

    // Tìm theo tên đăng nhập
    private static final String SQL_SELECT_BY_USERNAME = 
        "SELECT tk.*, " +
        "nv.ma_nv, nv.ten_nv, " +
        "kh.maKH, kh.hoTen AS ten_khachhang " +
        "FROM qltaikhoan tk " +
        "LEFT JOIN nhanvien nv ON tk.id_lien_ket = nv.ma_nv AND tk.quyen = 'nhanvien' " +
        "LEFT JOIN khachhang kh ON tk.id_lien_ket = kh.maKH AND tk.quyen = 'khachhang' " +
        "WHERE tk.ten_dang_nhap = ?";

    // Thêm mới tài khoản
    private static final String SQL_INSERT = 
        "INSERT INTO qltaikhoan (id_lien_ket, ten_dang_nhap, mat_khau, quyen) VALUES (?, ?, ?, ?)";

    // Cập nhật tài khoản
    private static final String SQL_UPDATE = 
        "UPDATE qltaikhoan SET id_lien_ket = ?, ten_dang_nhap = ?, mat_khau = ?, quyen = ? WHERE ma_tai_khoan = ?";

    // Xóa tài khoản
    private static final String SQL_DELETE = 
        "DELETE FROM qltaikhoan WHERE ma_tai_khoan = ?";

    // Lấy danh sách tất cả tài khoản
    public static List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> danhSach = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                danhSach.add(extractTaiKhoanFromResultSet(rs));
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách tài khoản: " + e.getMessage());
        }
        return danhSach;
    }

    // Tìm tài khoản theo ID
    public static TaiKhoan getById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractTaiKhoanFromResultSet(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi tìm tài khoản theo ID: " + e.getMessage());
        }
        return null;
    }

    // Tìm tài khoản theo tên đăng nhập
    public static TaiKhoan getByUsername(String username) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_USERNAME)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractTaiKhoanFromResultSet(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi tìm tài khoản theo tên đăng nhập: " + e.getMessage());
        }
        return null;
    }

    // Thêm mới tài khoản
    public static boolean insert(TaiKhoan tk) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            if (tk.getIdLienKet() != null) {
                ps.setInt(1, tk.getIdLienKet());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, tk.getTenDangNhap());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getQuyen());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tk.setMaTaiKhoan(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi thêm tài khoản: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật tài khoản
    public static boolean update(TaiKhoan tk) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            if (tk.getIdLienKet() != null) {
                ps.setInt(1, tk.getIdLienKet());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, tk.getTenDangNhap());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getQuyen());
            ps.setInt(5, tk.getMaTaiKhoan());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật tài khoản: " + e.getMessage());
            return false;
        }
    }

    // Xóa tài khoản
    public static boolean delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi xoá tài khoản: " + e.getMessage());
            return false;
        }
    }

    // Kết xuất ResultSet thành TaiKhoan object
    private static TaiKhoan extractTaiKhoanFromResultSet(ResultSet rs) throws Exception {
        TaiKhoan tk = new TaiKhoan();

        tk.setMaTaiKhoan(rs.getInt("ma_tai_khoan"));

        // Lấy id_lien_ket có thể null
        Object idLienKetObj = rs.getObject("id_lien_ket");
        if (idLienKetObj != null) {
            tk.setIdLienKet(rs.getInt("id_lien_ket"));
        } else {
            tk.setIdLienKet(null);
        }

        tk.setTenDangNhap(rs.getString("ten_dang_nhap"));
        tk.setMatKhau(rs.getString("mat_khau"));
        tk.setQuyen(rs.getString("quyen"));

        // Lấy tên liên kết tuỳ vào quyền
        String quyen = tk.getQuyen();
        if ("nhanvien".equalsIgnoreCase(quyen)) {
            tk.setHoTen(rs.getString("ten_nv"));
        } else if ("khachhang".equalsIgnoreCase(quyen)) {
            tk.setHoTen(rs.getString("ten_khachhang"));
        } else {
            tk.setHoTen(null);
        }

        return tk;
    }
    // Tìm kiếm tài khoản theo keyword (tìm trong tên đăng nhập và tên liên kết)
public static List<TaiKhoan> searchTaiKhoan(String keyword) {
    List<TaiKhoan> danhSach = new ArrayList<>();
    String sql = 
        "SELECT tk.*, " +
        "nv.ma_nv, nv.ten_nv, " +
        "kh.maKH, kh.hoTen AS ten_khachhang " +
        "FROM qltaikhoan tk " +
        "LEFT JOIN nhanvien nv ON tk.id_lien_ket = nv.ma_nv AND tk.quyen = 'nhanvien' " +
        "LEFT JOIN khachhang kh ON tk.id_lien_ket = kh.maKH AND tk.quyen = 'khachhang' " +
        "WHERE tk.ten_dang_nhap LIKE ? OR nv.ten_nv LIKE ? OR kh.hoTen LIKE ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        String likeKeyword = "%" + keyword + "%";
        ps.setString(1, likeKeyword);
        ps.setString(2, likeKeyword);
        ps.setString(3, likeKeyword);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                danhSach.add(extractTaiKhoanFromResultSet(rs));
            }
        }
    } catch (Exception e) {
        System.err.println("Lỗi khi tìm kiếm tài khoản: " + e.getMessage());
    }
    return danhSach;
}
     public boolean SignIn(TaiKhoan account) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT * FROM qltaikhoan WHERE ten_dang_nhap = ? AND mat_khau = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, account.getTenDangNhap());
        stmt.setString(2, account.getMatKhau());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // Gán thêm dữ liệu vào đối tượng account
            account.setMaTaiKhoan(rs.getInt("ma_tai_khoan")); // tên cột đúng
            account.setQuyen(rs.getString("quyen"));
            return true;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

   

    public boolean isUserExists(String username) {
        String sql = "SELECT * FROM qltaikhoan WHERE ten_dang_nhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Nếu có dòng kết quả => user đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getRoleByUsername(String username) {
        String role = null;
        String sql = "SELECT quyen FROM qltaikhoan WHERE ten_dang_nhap = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("quyen");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }
}
