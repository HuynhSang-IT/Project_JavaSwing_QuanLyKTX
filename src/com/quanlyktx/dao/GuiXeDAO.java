package com.quanlyktx.dao;

import com.quanlyktx.model.GuiXe;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuiXeDAO {

    // 1. Lấy danh sách (JOIN SinhVien)
    public List<GuiXe> getAll() {
        List<GuiXe> list = new ArrayList<>();
        String sql = "SELECT g.*, s.HoTen FROM guixe g JOIN sinhvien s ON g.MaSV = s.MaSV WHERE g.TrangThai != 'Đã lấy xe' ORDER BY g.MaVe DESC";
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                list.add(mapResult(rs));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Đăng ký xe mới (Mặc định 1 tháng)
    public boolean dangKy(GuiXe gx) {
        // Dùng hàm CURDATE() và DATE_ADD của MySQL để tự tính ngày
        String sql = "INSERT INTO guixe(MaSV, BienSo, LoaiXe, MauXe, NgayDangKy, NgayHetHan, PhiThang, TrangThai) VALUES(?,?,?,?,CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), ?, ?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, gx.getMaSV());
            ps.setString(2, gx.getBienSo());
            ps.setString(3, gx.getLoaiXe());
            ps.setString(4, gx.getMauXe());
            ps.setDouble(5, gx.getPhiThang());
            ps.setString(6, gx.getTrangThai()); // Thường là "Đã thanh toán" luôn
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 3. Gia hạn thêm 1 tháng
    public boolean giaHan(int maVe) {
        // Cộng thêm 30 ngày vào ngày hết hạn hiện tại
        String sql = "UPDATE guixe SET NgayHetHan = DATE_ADD(NgayHetHan, INTERVAL 30 DAY), TrangThai = 'Đã thanh toán' WHERE MaVe=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, maVe);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // 4. Hủy gửi xe (Xóa mềm - chỉ đổi trạng thái)
    public boolean huyGui(int maVe) {
        try {
            Connection con = DatabaseHelper.getConnection();
            return con.createStatement().executeUpdate("UPDATE guixe SET TrangThai = 'Đã lấy xe' WHERE MaVe=" + maVe) > 0;
        } catch(Exception e) { return false; }
    }

    // --- THỐNG KÊ NHANH (Cho Dashboard nhỏ) ---
    public double getDoanhThuThangNay() {
        double total = 0;
        // Tính tổng phí của những xe đăng ký trong 30 ngày qua
        String sql = "SELECT SUM(PhiThang) FROM guixe WHERE NgayDangKy >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            if(rs.next()) total = rs.getDouble(1);
            con.close();
        } catch(Exception e) {}
        return total;
    }
    
    public int getSoLuongXe() {
        int count = 0;
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM guixe WHERE TrangThai != 'Đã lấy xe'");
            if(rs.next()) count = rs.getInt(1);
            con.close();
        } catch(Exception e) {}
        return count;
    }

    // Hàm phụ map
    private GuiXe mapResult(ResultSet rs) throws SQLException {
        GuiXe gx = new GuiXe();
        gx.setMaVe(rs.getInt("MaVe"));
        gx.setMaSV(rs.getString("MaSV"));
        gx.setTenSV(rs.getString("HoTen"));
        gx.setBienSo(rs.getString("BienSo"));
        gx.setLoaiXe(rs.getString("LoaiXe"));
        gx.setMauXe(rs.getString("MauXe"));
        gx.setNgayDangKy(rs.getDate("NgayDangKy"));
        gx.setNgayHetHan(rs.getDate("NgayHetHan"));
        gx.setPhiThang(rs.getDouble("PhiThang"));
        gx.setTrangThai(rs.getString("TrangThai"));
        return gx;
    }
    
    // Tìm kiếm
    public List<GuiXe> timKiem(String keyword) {
        List<GuiXe> list = new ArrayList<>();
        String sql = "SELECT g.*, s.HoTen FROM guixe g JOIN sinhvien s ON g.MaSV = s.MaSV WHERE (s.HoTen LIKE ? OR g.BienSo LIKE ?) AND g.TrangThai != 'Đã lấy xe'";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            String k = "%" + keyword + "%";
            ps.setString(1, k); ps.setString(2, k);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) list.add(mapResult(rs));
            con.close();
        } catch(Exception e) {}
        return list;
    }
}