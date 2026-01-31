package com.quanlyktx.model;

public class KyLuat {
    private int maKL;
    private String maSV;
    private String tenSV; // Biến này để hiển thị tên cho dễ nhìn (không lưu CSDL)
    private String noiDung;
    private String hinhThuc;
    private String ngayViPham;
    private double soTienPhat;

    public KyLuat() {
    }

    public KyLuat(int maKL, String maSV, String noiDung, String hinhThuc, String ngayViPham, double soTienPhat) {
        this.maKL = maKL;
        this.maSV = maSV;
        this.noiDung = noiDung;
        this.hinhThuc = hinhThuc;
        this.ngayViPham = ngayViPham;
        this.soTienPhat = soTienPhat;
    }

    // Getter & Setter
    public int getMaKL() { return maKL; }
    public void setMaKL(int maKL) { this.maKL = maKL; }
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }
    public String getTenSV() { return tenSV; }
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public String getHinhThuc() { return hinhThuc; }
    public void setHinhThuc(String hinhThuc) { this.hinhThuc = hinhThuc; }
    public String getNgayViPham() { return ngayViPham; }
    public void setNgayViPham(String ngayViPham) { this.ngayViPham = ngayViPham; }
    public double getSoTienPhat() { return soTienPhat; }
    public void setSoTienPhat(double soTienPhat) { this.soTienPhat = soTienPhat; }
}