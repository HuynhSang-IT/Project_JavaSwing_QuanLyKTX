package com.quanlyktx.model;

import java.sql.Date;

public class HopDong {
    private int maHopDong; // Tự tăng
    private String maSV;
    private String maPhong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    
    // Biến phụ để hiển thị thông tin chi tiết
    private String tenSV;
    private String tenPhong;

    public HopDong() {}

    public HopDong(int maHopDong, String maSV, String maPhong, Date ngayBatDau, Date ngayKetThuc) {
        this.maHopDong = maHopDong;
        this.maSV = maSV;
        this.maPhong = maPhong;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // --- GETTER & SETTER ---
    public int getMaHopDong() { return maHopDong; }
    public void setMaHopDong(int maHopDong) { this.maHopDong = maHopDong; }

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getTenSV() { return tenSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
}