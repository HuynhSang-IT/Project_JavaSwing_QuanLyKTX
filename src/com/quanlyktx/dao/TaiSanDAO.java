package com.quanlyktx.dao;

import com.quanlyktx.model.TaiSan;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiSanDAO {

    // 1. LẤY THEO PHÒNG (Giữ nguyên)
    public List<TaiSan> getByPhong(String maPhong) {
        List<TaiSan> list = new ArrayList<>();
        String sql = "SELECT * FROM taisan WHERE MaPhong = ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(mapResultSetToTaiSan(rs));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. THÊM
    public boolean them(TaiSan ts) {
        String sql = "INSERT INTO taisan(MaPhong, TenTaiSan, SoLuong, TinhTrang, GhiChu) VALUES(?,?,?,?,?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ts.getMaPhong());
            ps.setString(2, ts.getTenTaiSan());
            ps.setInt(3, ts.getSoLuong());
            ps.setString(4, ts.getTinhTrang());
            ps.setString(5, ts.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // 3. SỬA
    public boolean sua(TaiSan ts) {
        String sql = "UPDATE taisan SET TenTaiSan=?, SoLuong=?, TinhTrang=?, GhiChu=? WHERE MaTS=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ts.getTenTaiSan());
            ps.setInt(2, ts.getSoLuong());
            ps.setString(3, ts.getTinhTrang());
            ps.setString(4, ts.getGhiChu());
            ps.setInt(5, ts.getMaTS());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // 4. XÓA
    public boolean xoa(int ma) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM taisan WHERE MaTS=?");
            ps.setInt(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // --- 5. TÌM KIẾM NÂNG CAO (LỌC THEO TẦNG) ---
    // Tìm theo Tên tài sản hoặc Phòng, có thể lọc theo Tầng
    public List<TaiSan> timKiemNangCao(String keyword, int maTang) {
        List<TaiSan> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT ts.* FROM taisan ts " +
            "JOIN phong p ON ts.MaPhong = p.MaPhong " +
            "WHERE (ts.TenTaiSan LIKE ? OR ts.MaPhong LIKE ?)"
        );
        
        // Nếu chọn tầng cụ thể
        if (maTang != -1) {
            sql.append(" AND p.MaTang = ?");
        }
        
        sql.append(" ORDER BY ts.MaTS DESC");

        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            String k = "%" + keyword + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            
            if (maTang != -1) {
                ps.setInt(3, maTang);
            }
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(mapResultSetToTaiSan(rs));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Map Helper
    private TaiSan mapResultSetToTaiSan(ResultSet rs) throws SQLException {
        TaiSan ts = new TaiSan();
        ts.setMaTS(rs.getInt("MaTS"));
        ts.setMaPhong(rs.getString("MaPhong"));
        ts.setTenTaiSan(rs.getString("TenTaiSan"));
        ts.setSoLuong(rs.getInt("SoLuong"));
        ts.setTinhTrang(rs.getString("TinhTrang"));
        ts.setGhiChu(rs.getString("GhiChu"));
        return ts;
    }
    
    // Giữ hàm cũ cho tương thích
    public List<TaiSan> timKiem(String k) {
        return timKiemNangCao(k, -1);
    }
}