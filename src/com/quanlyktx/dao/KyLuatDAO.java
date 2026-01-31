package com.quanlyktx.dao;

import com.quanlyktx.model.KyLuat;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KyLuatDAO {
    
    // 1. Lấy danh sách (Kèm tên sinh viên cho dễ nhìn)
    public List<KyLuat> getAll() {
        List<KyLuat> list = new ArrayList<>();
        // Join bảng sinhvien để lấy HoTen
        String sql = "SELECT kl.*, sv.HoTen FROM kyluat kl JOIN sinhvien sv ON kl.MaSV = sv.MaSV ORDER BY kl.MaKL DESC";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                KyLuat kl = new KyLuat();
                kl.setMaKL(rs.getInt("MaKL"));
                kl.setMaSV(rs.getString("MaSV"));
                kl.setTenSV(rs.getString("HoTen")); // Lấy tên từ bảng sinhvien
                kl.setNoiDung(rs.getString("NoiDungVP"));
                kl.setHinhThuc(rs.getString("HinhThucXuLy"));
                kl.setNgayViPham(rs.getString("NgayViPham"));
                kl.setSoTienPhat(rs.getDouble("SoTienPhat"));
                list.add(kl);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 2. Thêm kỷ luật
    public boolean them(KyLuat kl) {
        String sql = "INSERT INTO kyluat(MaSV, NoiDungVP, HinhThucXuLy, NgayViPham, SoTienPhat) VALUES(?,?,?,?,?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kl.getMaSV());
            ps.setString(2, kl.getNoiDung());
            ps.setString(3, kl.getHinhThuc());
            ps.setString(4, kl.getNgayViPham());
            ps.setDouble(5, kl.getSoTienPhat());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    
    // 3. Cập nhật
    public boolean capNhat(KyLuat kl) {
        String sql = "UPDATE kyluat SET NoiDungVP=?, HinhThucXuLy=?, NgayViPham=?, SoTienPhat=? WHERE MaKL=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kl.getNoiDung());
            ps.setString(2, kl.getHinhThuc());
            ps.setString(3, kl.getNgayViPham());
            ps.setDouble(4, kl.getSoTienPhat());
            ps.setInt(5, kl.getMaKL());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // 4. Xóa
    public boolean xoa(int maKL) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM kyluat WHERE MaKL=?");
            ps.setInt(1, maKL);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // 5. Tìm kiếm (Theo Mã SV hoặc Tên SV)
    public List<KyLuat> timKiem(String keyword) {
        List<KyLuat> list = new ArrayList<>();
        String sql = "SELECT kl.*, sv.HoTen FROM kyluat kl JOIN sinhvien sv ON kl.MaSV = sv.MaSV " +
                     "WHERE kl.MaSV LIKE ? OR sv.HoTen LIKE ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            String k = "%" + keyword + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                KyLuat kl = new KyLuat();
                kl.setMaKL(rs.getInt("MaKL"));
                kl.setMaSV(rs.getString("MaSV"));
                kl.setTenSV(rs.getString("HoTen"));
                kl.setNoiDung(rs.getString("NoiDungVP"));
                kl.setHinhThuc(rs.getString("HinhThucXuLy"));
                kl.setNgayViPham(rs.getString("NgayViPham"));
                kl.setSoTienPhat(rs.getDouble("SoTienPhat"));
                list.add(kl);
            }
        } catch (Exception e) {}
        return list;
    }
}