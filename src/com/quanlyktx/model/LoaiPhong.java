package com.quanlyktx.model;

public class LoaiPhong {
    private String maLoai;
    private String tenLoai;
    private double donGia;
    private int soNguoiToiDa;

    public LoaiPhong() {}

    public LoaiPhong(String maLoai, String tenLoai, double donGia, int soNguoiToiDa) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.donGia = donGia;
        this.soNguoiToiDa = soNguoiToiDa;
    }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public int getSoNguoiToiDa() { return soNguoiToiDa; }
    public void setSoNguoiToiDa(int soNguoiToiDa) { this.soNguoiToiDa = soNguoiToiDa; }

    @Override
    public String toString() {
        return tenLoai; // Để sau này hiện lên ComboBox cho đẹp
    }
}