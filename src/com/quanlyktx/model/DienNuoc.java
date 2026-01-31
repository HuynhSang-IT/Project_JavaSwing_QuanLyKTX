package com.quanlyktx.model;

public class DienNuoc {
    private int maHD;
    private String maPhong;
    private String thangNam;
    private int soDienCu, soDienMoi;
    private int soNuocCu, soNuocMoi;
    private double thanhTien;
    private String trangThai;
    private double tienPhong;
    private String hinhThucTT;
    private double tienDien;
    private double tienNuoc;
    
    // Biến phụ để hiển thị tên phòng
    private String tenPhong;

    public DienNuoc() {}

    // Constructor đầy đủ
    public DienNuoc(int maHD, String maPhong, String thangNam, int soDienCu, int soDienMoi, int soNuocCu, int soNuocMoi, double thanhTien, String trangThai) {
        this.maHD = maHD;
        this.maPhong = maPhong;
        this.thangNam = thangNam;
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    // --- GETTER & SETTER ---
    public int getMaHD() { return maHD; }
    public void setMaHD(int maHD) { this.maHD = maHD; }
    
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    
    public String getThangNam() { return thangNam; }
    public void setThangNam(String thangNam) { this.thangNam = thangNam; }
    
    public int getSoDienCu() { return soDienCu; }
    public void setSoDienCu(int soDienCu) { this.soDienCu = soDienCu; }
    
    public int getSoDienMoi() { return soDienMoi; }
    public void setSoDienMoi(int soDienMoi) { this.soDienMoi = soDienMoi; }
    
    public int getSoNuocCu() { return soNuocCu; }
    public void setSoNuocCu(int soNuocCu) { this.soNuocCu = soNuocCu; }
    
    public int getSoNuocMoi() { return soNuocMoi; }
    public void setSoNuocMoi(int soNuocMoi) { this.soNuocMoi = soNuocMoi; }
    
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    
    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public String getHinhThucTT() { return hinhThucTT; }
    public void setHinhThucTT(String hinhThucTT) { this.hinhThucTT = hinhThucTT; }
    
    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }
}