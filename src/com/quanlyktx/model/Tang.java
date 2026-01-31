package com.quanlyktx.model;

public class Tang {
    private int maTang;
    private String tenTang;
    private String ghiChu;

    public Tang() {}

    public Tang(int maTang, String tenTang, String ghiChu) {
        this.maTang = maTang;
        this.tenTang = tenTang;
        this.ghiChu = ghiChu;
    }

    public int getMaTang() { return maTang; }
    public void setMaTang(int maTang) { this.maTang = maTang; }
    public String getTenTang() { return tenTang; }
    public void setTenTang(String tenTang) { this.tenTang = tenTang; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    // Hàm này để hiển thị trong ComboBox (nếu sau này cần dùng)
    @Override
    public String toString() {
        return tenTang;
    }
}