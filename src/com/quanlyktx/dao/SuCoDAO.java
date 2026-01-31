package com.quanlyktx.dao;

import com.quanlyktx.model.SuCo;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuCoDAO {

    // 1. LẤY TẤT CẢ
    public List<SuCo> getAll() {
        List<SuCo> list = new ArrayList<>();
        // Sắp xếp sự cố mới nhất lên đầu
        String sql = "SELECT * FROM suco ORDER BY MaSuCo DESC";
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                list.add(mapResultSetToSuCo(rs));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. THÊM
    public boolean them(SuCo sc) {
        String sql = "INSERT INTO suco(MaPhong, MoTa, NguoiBao, NgayBao, TrangThai, ChiPhi) VALUES(?,?,?,?,?,?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sc.getMaPhong());
            ps.setString(2, sc.getMoTa());
            ps.setString(3, sc.getNguoiBao());
            ps.setString(4, sc.getNgayBao());
            ps.setString(5, "Chưa xử lý"); // Mặc định
            ps.setDouble(6, 0); // Mặc định chưa có phí
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // 3. CẬP NHẬT
    public boolean capNhat(SuCo sc) {
        String sql = "UPDATE suco SET TrangThai=?, ChiPhi=?, MoTa=? WHERE MaSuCo=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sc.getTrangThai());
            ps.setDouble(2, sc.getChiPhi());
            ps.setString(3, sc.getMoTa());
            ps.setInt(4, sc.getMaSuCo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // 4. XÓA
    public boolean xoa(int ma) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM suco WHERE MaSuCo=?");
            ps.setInt(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // --- 5. TÌM KIẾM NÂNG CAO (Có lọc theo Tầng) ---
    public List<SuCo> timKiemNangCao(String keyword, int maTang) {
        List<SuCo> list = new ArrayList<>();
        
        // JOIN bảng suco với bảng phong để lấy MaTang
        StringBuilder sql = new StringBuilder(
            "SELECT s.* FROM suco s " +
            "JOIN phong p ON s.MaPhong = p.MaPhong " +
            "WHERE (s.MaPhong LIKE ? OR s.NguoiBao LIKE ?)"
        );
        
        // Nếu chọn tầng cụ thể (khác -1) thì thêm điều kiện
        if (maTang != -1) {
            sql.append(" AND p.MaTang = ?");
        }
        
        sql.append(" ORDER BY s.MaSuCo DESC");

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
                list.add(mapResultSetToSuCo(rs));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Hàm phụ map dữ liệu để code gọn hơn
    private SuCo mapResultSetToSuCo(ResultSet rs) throws SQLException {
        SuCo sc = new SuCo();
        sc.setMaSuCo(rs.getInt("MaSuCo"));
        sc.setMaPhong(rs.getString("MaPhong"));
        sc.setMoTa(rs.getString("MoTa"));
        sc.setNguoiBao(rs.getString("NguoiBao"));
        sc.setNgayBao(rs.getString("NgayBao"));
        sc.setTrangThai(rs.getString("TrangThai"));
        sc.setChiPhi(rs.getDouble("ChiPhi"));
        return sc;
    }
    
    // Hàm tìm kiếm cũ (để tương thích)
    public List<SuCo> timKiem(String k) {
        return timKiemNangCao(k, -1);
    }
}