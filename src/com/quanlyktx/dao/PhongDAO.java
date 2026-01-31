package com.quanlyktx.dao;

import com.quanlyktx.model.Phong;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongDAO {
    
    // 1. Lấy danh sách (Sửa tên cột SoNguoiO -> SoNguoiDangO)
    public List<Phong> getAllPhong() {
        List<Phong> list = new ArrayList<>();
        // Sửa query: Lấy SoNguoiDangO
        String sql = "SELECT p.*, t.TenTang FROM phong p LEFT JOIN tang t ON p.MaTang = t.MaTang ORDER BY p.MaPhong ASC";
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                Phong p = new Phong();
                p.setMaPhong(rs.getString("MaPhong"));
                p.setTenPhong(rs.getString("TenPhong"));
                
                // --- SỬA Ở ĐÂY: Map cột DB 'SoNguoiDangO' vào thuộc tính 'SoNguoiO' của Java ---
                p.setSoNguoiO(rs.getInt("SoNguoiDangO")); 
                
                p.setSoLuongMax(rs.getInt("SoLuongMax"));
                p.setGiaPhong(rs.getDouble("GiaPhong"));
                p.setLoaiPhong(rs.getString("LoaiPhong"));
                
                // Lấy trạng thái (nếu model Phong có thuộc tính này thì set, không thì thôi)
                // p.setTrangThai(rs.getString("TrangThai")); 
                
                p.setMaTang(rs.getInt("MaTang"));
                p.setTenTang(rs.getString("TenTang")); 
                
                list.add(p);
            }
            con.close();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // 2. Thêm phòng (Sửa tên cột & Thêm cột TrangThai)
    public boolean themPhong(Phong p) {
        String sql = "INSERT INTO phong(MaPhong, TenPhong, SoNguoiDangO, SoLuongMax, GiaPhong, LoaiPhong, MaTang, TrangThai) VALUES(?,?,0,?,?,?,?,?)";
        
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getTenPhong());
            // Tham số thứ 3 là 0 (SoNguoiDangO) đã viết cứng trong SQL
            ps.setInt(3, p.getSoLuongMax());
            ps.setDouble(4, p.getGiaPhong());
            ps.setString(5, p.getLoaiPhong());
            ps.setInt(6, p.getMaTang());
            
            // --- THÊM GIÁ TRỊ TRẠNG THÁI ---
            ps.setString(7, "Còn trống"); // Mặc định là Còn trống
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            // In lỗi đỏ ra màn hình console NetBeans để bạn thấy rõ nếu còn sai
            System.err.println("Lỗi SQL Thêm Phòng: " + e.getMessage());
            e.printStackTrace(); 
            return false; 
        }
    }

    // 3. Sửa phòng (Cũng phải sửa tên cột SoNguoiDangO nếu có update số người)
    // Nhưng thường sửa phòng chỉ sửa tên, giá, loại...
    public boolean suaPhong(Phong p) {
        String sql = "UPDATE phong SET TenPhong=?, SoLuongMax=?, GiaPhong=?, LoaiPhong=?, MaTang=? WHERE MaPhong=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getTenPhong());
            ps.setInt(2, p.getSoLuongMax());
            ps.setDouble(3, p.getGiaPhong());
            ps.setString(4, p.getLoaiPhong());
            ps.setInt(5, p.getMaTang());
            ps.setString(6, p.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { 
            e.printStackTrace();
            return false; 
        }
    }

    // 4. Xóa (Giữ nguyên)
    public boolean xoaPhong(String maPhong) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM phong WHERE MaPhong=?");
            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // 5. Tìm kiếm (Sửa tên cột SoNguoiDangO)
    public List<Phong> timKiemPhong(String keyword) {
        List<Phong> list = new ArrayList<>();
        // Sửa query
        String sql = "SELECT p.*, t.TenTang FROM phong p " +
                     "LEFT JOIN tang t ON p.MaTang = t.MaTang " +
                     "WHERE p.MaPhong LIKE ? OR p.TenPhong LIKE ? OR t.TenTang LIKE ? " +
                     "ORDER BY p.MaPhong ASC";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            String tk = "%" + keyword + "%";
            ps.setString(1, tk);
            ps.setString(2, tk);
            ps.setString(3, tk);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Phong p = new Phong();
                p.setMaPhong(rs.getString("MaPhong"));
                p.setTenPhong(rs.getString("TenPhong"));
                
                // --- SỬA MAP ---
                p.setSoNguoiO(rs.getInt("SoNguoiDangO"));
                
                p.setSoLuongMax(rs.getInt("SoLuongMax"));
                p.setGiaPhong(rs.getDouble("GiaPhong"));
                p.setLoaiPhong(rs.getString("LoaiPhong"));
                p.setMaTang(rs.getInt("MaTang"));
                p.setTenTang(rs.getString("TenTang")); 
                list.add(p);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}