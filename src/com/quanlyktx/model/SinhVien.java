package com.quanlyktx.model;

public class SinhVien {
    private String maSV;
    private String hoTen;
    private String cmnd;  
    private String gioiTinh;
    private String sdt;
    private String email; 
    private String queQuan;
    
    // Thuộc tính mới thêm để phục vụ Chuyển phòng
    private String maPhong; 
    
    public SinhVien() {
    }

    // Constructor cơ bản
    public SinhVien(String maSV, String hoTen, String cmnd, String gioiTinh, String sdt, String email, String queQuan) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.cmnd = cmnd;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.email = email;
        this.queQuan = queQuan;
    }

    // --- GETTER & SETTER ---
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getCmnd() { return cmnd; }       
    public void setCmnd(String cmnd) { this.cmnd = cmnd; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getEmail() { return email; }      
    public void setEmail(String email) { this.email = email; } 

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }
    
    // Getter & Setter cho MaPhong
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    
    // toString để hiển thị trên Combobox nếu cần
    @Override
    public String toString() {
        return maSV + " - " + hoTen;
    }
}