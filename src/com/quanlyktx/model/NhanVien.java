package com.quanlyktx.model;

public class NhanVien {
    private String tenDangNhap; // Map với cột TenDangNhap
    private String matKhau;     // Map với cột MatKhau
    private String hoTen;       // Map với cột HoTen
    private String quyen;       // Map với cột Quyen
    private String sdt;         // Map với cột SDT (vừa thêm)

    public NhanVien() {
    }

    public NhanVien(String tenDangNhap, String matKhau, String hoTen, String quyen, String sdt) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.quyen = quyen;
        this.sdt = sdt;
    }

    // Getter & Setter
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}