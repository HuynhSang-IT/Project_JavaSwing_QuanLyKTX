package com.quanlyktx.model;

import java.util.Date; // Hoặc dùng java.sql.Date đều được

public class ChuyenPhong {
    private int maCP;
    private String maSV;
    private String tenSV;      // Tên sinh viên (để hiển thị)
    private String phongCu;
    private String phongMoi;
    private String lyDo;
    private Date ngayChuyen;   // <-- Bạn đang thiếu cái này
    private String nguoiThucHien;

    public ChuyenPhong() {
    }

    public ChuyenPhong(int maCP, String maSV, String tenSV, String phongCu, String phongMoi, String lyDo, Date ngayChuyen, String nguoiThucHien) {
        this.maCP = maCP;
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.phongCu = phongCu;
        this.phongMoi = phongMoi;
        this.lyDo = lyDo;
        this.ngayChuyen = ngayChuyen;
        this.nguoiThucHien = nguoiThucHien;
    }

    // --- GETTER & SETTER ---
    public int getMaCP() {
        return maCP;
    }

    public void setMaCP(int maCP) {
        this.maCP = maCP;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getPhongCu() {
        return phongCu;
    }

    public void setPhongCu(String phongCu) {
        this.phongCu = phongCu;
    }

    public String getPhongMoi() {
        return phongMoi;
    }

    public void setPhongMoi(String phongMoi) {
        this.phongMoi = phongMoi;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public Date getNgayChuyen() {
        return ngayChuyen;
    }

    public void setNgayChuyen(Date ngayChuyen) {
        this.ngayChuyen = ngayChuyen;
    }

    public String getNguoiThucHien() {
        return nguoiThucHien;
    }

    public void setNguoiThucHien(String nguoiThucHien) {
        this.nguoiThucHien = nguoiThucHien;
    }
}