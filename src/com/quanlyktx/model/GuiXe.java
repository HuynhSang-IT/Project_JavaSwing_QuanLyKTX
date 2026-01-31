package com.quanlyktx.model;

import java.sql.Date;

public class GuiXe {
    private int maVe;
    private String maSV;
    private String tenSV; // Biến phụ hiển thị
    private String bienSo;
    private String loaiXe;
    private String mauXe;
    private Date ngayDangKy;
    private Date ngayHetHan;
    private double phiThang;
    private String trangThai;

    public GuiXe() {}

    public GuiXe(int maVe, String maSV, String tenSV, String bienSo, String loaiXe, String mauXe, Date ngayDangKy, Date ngayHetHan, double phiThang, String trangThai) {
        this.maVe = maVe;
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.bienSo = bienSo;
        this.loaiXe = loaiXe;
        this.mauXe = mauXe;
        this.ngayDangKy = ngayDangKy;
        this.ngayHetHan = ngayHetHan;
        this.phiThang = phiThang;
        this.trangThai = trangThai;
    }

    // Getter & Setter
    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public String getTenSV() { return tenSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }
    public String getBienSo() { return bienSo; }
    public void setBienSo(String bienSo) { this.bienSo = bienSo; }
    public String getLoaiXe() { return loaiXe; }
    public void setLoaiXe(String loaiXe) { this.loaiXe = loaiXe; }
    public String getMauXe() { return mauXe; }
    public void setMauXe(String mauXe) { this.mauXe = mauXe; }
    public Date getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(Date ngayDangKy) { this.ngayDangKy = ngayDangKy; }
    public Date getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(Date ngayHetHan) { this.ngayHetHan = ngayHetHan; }
    public double getPhiThang() { return phiThang; }
    public void setPhiThang(double phiThang) { this.phiThang = phiThang; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}