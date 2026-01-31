package com.quanlyktx.dao;

import com.quanlyktx.model.SinhVien;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> list = new ArrayList<>();
        // SELECT * sẽ lấy luôn cả cột MaPhong mới thêm
        String sql = "SELECT * FROM sinhvien";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SinhVien sv = mapSinhVien(rs);
                list.add(sv);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themSV(SinhVien sv) {
        // Thêm sinh viên mới (lúc đầu chưa có phòng -> MaPhong để NULL hoặc không insert)
        String sql = "INSERT INTO sinhvien(MaSV, HoTen, CMND, GioiTinh, SDT, Email, QueQuan) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sv.getMaSV());
            ps.setString(2, sv.getHoTen());
            ps.setString(3, sv.getCmnd());      
            ps.setString(4, sv.getGioiTinh());
            ps.setString(5, sv.getSdt());
            ps.setString(6, sv.getEmail());     
            ps.setString(7, sv.getQueQuan());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean suaSV(SinhVien sv) {
        String sql = "UPDATE sinhvien SET HoTen=?, CMND=?, GioiTinh=?, SDT=?, Email=?, QueQuan=? WHERE MaSV=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sv.getHoTen());
            ps.setString(2, sv.getCmnd());      
            ps.setString(3, sv.getGioiTinh());
            ps.setString(4, sv.getSdt());
            ps.setString(5, sv.getEmail());     
            ps.setString(6, sv.getQueQuan());
            ps.setString(7, sv.getMaSV());       
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaSV(String maSV) {
        String sql = "DELETE FROM sinhvien WHERE MaSV=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maSV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Hàm tìm kiếm (Có lấy MaPhong)
    public List<SinhVien> timKiem(String keyword) {
        List<SinhVien> list = new ArrayList<>();
        String sql = "SELECT * FROM sinhvien WHERE MaSV LIKE ? OR HoTen LIKE ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            String tuKhoa = "%" + keyword + "%";
            ps.setString(1, tuKhoa);
            ps.setString(2, tuKhoa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SinhVien sv = mapSinhVien(rs);
                list.add(sv);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // Hàm phụ trợ để map dữ liệu cho gọn (tránh lặp code)
    private SinhVien mapSinhVien(ResultSet rs) throws SQLException {
        SinhVien sv = new SinhVien();
        sv.setMaSV(rs.getString("MaSV"));
        sv.setHoTen(rs.getString("HoTen"));
        sv.setCmnd(rs.getString("CMND"));
        sv.setGioiTinh(rs.getString("GioiTinh"));
        sv.setSdt(rs.getString("SDT"));
        sv.setEmail(rs.getString("Email"));
        sv.setQueQuan(rs.getString("QueQuan"));
        
        // QUAN TRỌNG: Lấy mã phòng
        try {
            sv.setMaPhong(rs.getString("MaPhong"));
        } catch (SQLException e) {
            // Nếu cột MaPhong chưa có trong DB (lỡ chưa chạy lệnh ALTER TABLE) thì bỏ qua
            // Nhưng bạn đã chạy lệnh rồi nên sẽ không vào đây đâu.
        }
        return sv;
    }
}