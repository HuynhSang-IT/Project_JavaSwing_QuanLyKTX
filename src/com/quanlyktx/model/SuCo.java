package com.quanlyktx.model;

public class SuCo {
    private int maSuCo;
    private String maPhong;
    private String nguoiBao;
    private String moTa;
    private String ngayBao;
    private String trangThai;
    private double chiPhi;

    public SuCo() {
    }

    public SuCo(int maSuCo, String maPhong, String nguoiBao, String moTa, String ngayBao, String trangThai, double chiPhi) {
        this.maSuCo = maSuCo;
        this.maPhong = maPhong;
        this.nguoiBao = nguoiBao;
        this.moTa = moTa;
        this.ngayBao = ngayBao;
        this.trangThai = trangThai;
        this.chiPhi = chiPhi;
    }

    // Getter & Setter
    public int getMaSuCo() { return maSuCo; }
    public void setMaSuCo(int maSuCo) { this.maSuCo = maSuCo; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getNguoiBao() { return nguoiBao; }
    public void setNguoiBao(String nguoiBao) { this.nguoiBao = nguoiBao; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getNgayBao() { return ngayBao; }
    public void setNgayBao(String ngayBao) { this.ngayBao = ngayBao; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public double getChiPhi() { return chiPhi; }
    public void setChiPhi(double chiPhi) { this.chiPhi = chiPhi; }
}