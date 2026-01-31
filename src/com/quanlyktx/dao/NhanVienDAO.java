package com.quanlyktx.dao;

import com.quanlyktx.model.NhanVien;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    
    // 1. Kiểm tra đăng nhập (Chắc chắn bạn đã có, cập nhật lại cho chuẩn model mới)
    public NhanVien checkLogin(String u, String p) {
        String sql = "SELECT * FROM nhanvien WHERE TenDangNhap=? AND MatKhau=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u); ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new NhanVien(
                    rs.getString("TenDangNhap"), 
                    rs.getString("MatKhau"), 
                    rs.getString("HoTen"), 
                    rs.getString("Quyen"),
                    rs.getString("SDT")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 2. Lấy danh sách nhân viên
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                list.add(new NhanVien(
                    rs.getString("TenDangNhap"), 
                    rs.getString("MatKhau"), 
                    rs.getString("HoTen"), 
                    rs.getString("Quyen"),
                    rs.getString("SDT")
                ));
            }
            con.close();
        } catch (Exception e) {}
        return list;
    }

    // 3. Thêm mới
    public boolean them(NhanVien nv) {
        String sql = "INSERT INTO nhanvien(TenDangNhap, MatKhau, HoTen, Quyen, SDT) VALUES(?,?,?,?,?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nv.getTenDangNhap());
            ps.setString(2, nv.getMatKhau());
            ps.setString(3, nv.getHoTen());
            ps.setString(4, nv.getQuyen());
            ps.setString(5, nv.getSdt());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; } 
    }

    // 4. Cập nhật (Không cho sửa TenDangNhap)
    public boolean sua(NhanVien nv) {
        String sql = "UPDATE nhanvien SET MatKhau=?, HoTen=?, Quyen=?, SDT=? WHERE TenDangNhap=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nv.getMatKhau());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getQuyen());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getTenDangNhap());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // 5. Xóa
    public boolean xoa(String user) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM nhanvien WHERE TenDangNhap=?");
            ps.setString(1, user);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}