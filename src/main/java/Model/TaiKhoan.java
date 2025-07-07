package Model;

public class TaiKhoan {
    private int maTaiKhoan;
    private Integer idLienKet;  // Có thể null nếu không phải nhân viên
    private String hoTen;       // Tên nhân viên nếu có
    private String tenDangNhap;
    private String matKhau;
    private String quyen;       // 'admin', 'nhanvien', 'khachhang'

    // Constructor
    public TaiKhoan(Integer idLienKet, String tenDangNhap, String matKhau, String quyen) {
        this.idLienKet = idLienKet;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
    }
    public TaiKhoan(String tenDangNhap, String matKhau, String quyen) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
    }

    public TaiKhoan() {
    }

    // Getter & Setter
    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public Integer getIdLienKet() {
        return idLienKet;
    }

    public void setIdLienKet(Integer idLienKet) {
        this.idLienKet = idLienKet;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }
}
