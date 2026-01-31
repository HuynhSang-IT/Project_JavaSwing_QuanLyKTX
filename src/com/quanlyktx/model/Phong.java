package com.quanlyktx.model;

public class Phong {
    private String maPhong;
    private String tenPhong;
    private int soNguoiO;
    private int soLuongMax;
    private double giaPhong;
    private String loaiPhong;
    // --- THÊM MỚI ---
    private int maTang;    // Lưu xuống DB
    private String tenTang; // Để hiển thị lên bảng (Vd: "Tầng 1")

    public Phong() {}

    public Phong(String maPhong, String tenPhong, int soNguoiO, int soLuongMax, double giaPhong, String loaiPhong, int maTang, String tenTang) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.soNguoiO = soNguoiO;
        this.soLuongMax = soLuongMax;
        this.giaPhong = giaPhong;
        this.loaiPhong = loaiPhong;
        this.maTang = maTang;
        this.tenTang = tenTang;
    }

    // Getter & Setter cũ giữ nguyên...
    
    // Getter & Setter MỚI
    public int getMaTang() { return maTang; }
    public void setMaTang(int maTang) { this.maTang = maTang; }
    public String getTenTang() { return tenTang; }
    public void setTenTang(String tenTang) { this.tenTang = tenTang; }
    
    // Các getter/setter cũ:
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public int getSoNguoiO() { return soNguoiO; }
    public void setSoNguoiO(int soNguoiO) { this.soNguoiO = soNguoiO; }
    public int getSoLuongMax() { return soLuongMax; }
    public void setSoLuongMax(int soLuongMax) { this.soLuongMax = soLuongMax; }
    public double getGiaPhong() { return giaPhong; }
    public void setGiaPhong(double giaPhong) { this.giaPhong = giaPhong; }
    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }
}