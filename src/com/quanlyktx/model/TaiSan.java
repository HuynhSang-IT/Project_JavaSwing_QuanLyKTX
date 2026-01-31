package com.quanlyktx.model;

public class TaiSan {
    private int maTS;
    private String maPhong;
    private String tenTaiSan;
    private int soLuong;
    private String tinhTrang;
    private String ghiChu;

    public TaiSan() {
    }

    public TaiSan(int maTS, String maPhong, String tenTaiSan, int soLuong, String tinhTrang, String ghiChu) {
        this.maTS = maTS;
        this.maPhong = maPhong;
        this.tenTaiSan = tenTaiSan;
        this.soLuong = soLuong;
        this.tinhTrang = tinhTrang;
        this.ghiChu = ghiChu;
    }

    public int getMaTS() { return maTS; }
    public void setMaTS(int maTS) { this.maTS = maTS; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getTenTaiSan() { return tenTaiSan; }
    public void setTenTaiSan(String tenTaiSan) { this.tenTaiSan = tenTaiSan; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public String getTinhTrang() { return tinhTrang; }
    public void setTinhTrang(String tinhTrang) { this.tinhTrang = tinhTrang; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}