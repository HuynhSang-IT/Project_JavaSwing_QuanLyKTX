package com.quanlyktx.dao;

import com.quanlyktx.model.DienNuoc;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DienNuocDAO {

    // ... (Các hàm getAll, them, capNhatHoaDon... GIỮ NGUYÊN NHƯ CŨ) ...
    // ... Bạn chỉ cần thay thế hoặc thêm mới hàm tìm kiếm bên dưới ...

    // 1. LẤY DANH SÁCH (Hiển thị đầy đủ thông tin)
    public List<DienNuoc> getAll() {
        List<DienNuoc> list = new ArrayList<>();
        String sql = "SELECT dn.*, p.TenPhong FROM diennuoc dn JOIN phong p ON dn.MaPhong = p.MaPhong ORDER BY MaHD ASC";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToDienNuoc(rs));
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ... (Hàm them, sua, xoa, getChiSoCuoiCung... giữ nguyên code của bạn) ...
    // Mình viết gọn lại hàm map để dùng chung cho đỡ dài dòng
    
    private DienNuoc mapResultSetToDienNuoc(ResultSet rs) throws SQLException {
        DienNuoc dn = new DienNuoc();
        dn.setMaHD(rs.getInt("MaHD"));
        dn.setMaPhong(rs.getString("MaPhong"));
        dn.setTenPhong(rs.getString("TenPhong"));
        dn.setThangNam(rs.getString("ThangNam"));
        
        try { dn.setTienPhong(rs.getDouble("TienPhong")); } catch(Exception e) { dn.setTienPhong(0); }
        try { dn.setTienDien(rs.getDouble("TienDien")); } catch(Exception e) { dn.setTienDien(0); }
        try { dn.setTienNuoc(rs.getDouble("TienNuoc")); } catch(Exception e) { dn.setTienNuoc(0); }
        
        dn.setSoDienCu(rs.getInt("SoDienCu"));
        dn.setSoDienMoi(rs.getInt("SoDienMoi"));
        dn.setSoNuocCu(rs.getInt("SoNuocCu"));
        dn.setSoNuocMoi(rs.getInt("SoNuocMoi"));
        
        dn.setThanhTien(rs.getDouble("ThanhTien"));
        dn.setTrangThai(rs.getString("TrangThai"));
        dn.setHinhThucTT(rs.getString("HinhThucTT"));
        return dn;
    }

    // --- HÀM TÌM KIẾM NÂNG CAO (LỌC THEO TỪ KHÓA VÀ TẦNG) ---
    // keyword: Tên phòng hoặc tháng
    // maTang: Mã tầng (-1 là tất cả)
    public List<DienNuoc> timKiemNangCao(String keyword, int maTang) {
         List<DienNuoc> list = new ArrayList<>();
         
         StringBuilder sql = new StringBuilder(
             "SELECT dn.*, p.TenPhong " +
             "FROM diennuoc dn " +
             "JOIN phong p ON dn.MaPhong = p.MaPhong " +
             "WHERE (p.TenPhong LIKE ? OR dn.ThangNam LIKE ?)"
         );
         
         if (maTang != -1) {
             sql.append(" AND p.MaTang = ?");
         }
         
         sql.append(" ORDER BY dn.MaHD ASC");

         try {
             Connection con = DatabaseHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             String tk = "%" + keyword + "%";
             ps.setString(1, tk);
             ps.setString(2, tk);
             
             if (maTang != -1) {
                 ps.setInt(3, maTang);
             }
             
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
                 list.add(mapResultSetToDienNuoc(rs));
             }
             con.close();
         } catch(Exception e) { e.printStackTrace(); }
         return list;
    }
    
    // ... (Các hàm update, getGiaPhong... giữ nguyên) ...
    // Lưu ý: Nhớ copy nốt các hàm them, sua, xoa, capNhatHoaDon, checkTonTai... từ code cũ sang nhé!
    // Ở đây mình chỉ show phần thay đổi quan trọng là timKiemNangCao.
    
    // Copy lại hàm them() từ code bạn gửi
    public boolean them(DienNuoc dn) {
        String sql = "INSERT INTO diennuoc(MaPhong, ThangNam, TienPhong, TienDien, TienNuoc, SoDienCu, SoDienMoi, SoNuocCu, SoNuocMoi, ThanhTien, TrangThai, HinhThucTT) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dn.getMaPhong());
            ps.setString(2, dn.getThangNam());
            ps.setDouble(3, 0); 
            ps.setDouble(4, dn.getTienDien());
            ps.setDouble(5, dn.getTienNuoc());
            ps.setInt(6, dn.getSoDienCu());
            ps.setInt(7, dn.getSoDienMoi());
            ps.setInt(8, dn.getSoNuocCu());
            ps.setInt(9, dn.getSoNuocMoi());
            ps.setDouble(10, dn.getThanhTien());
            ps.setString(11, "Chờ lập HD");
            ps.setString(12, "Chưa thanh toán");
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // Copy lại hàm cập nhật
    public boolean capNhatHoaDon(int maHD, double tienPhong, double tongTien, String trangThai, String hinhThuc) {
        String sql = "UPDATE diennuoc SET TienPhong=?, ThanhTien=?, TrangThai=?, HinhThucTT=? WHERE MaHD=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, tienPhong);
            ps.setDouble(2, tongTien);
            ps.setString(3, trangThai);
            ps.setString(4, hinhThuc);
            ps.setInt(5, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // Copy lại hàm checkTonTai
    public boolean checkTonTai(String maPhong, String thangNam) {
        String sql = "SELECT COUNT(*) FROM diennuoc WHERE MaPhong = ? AND ThangNam = ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPhong);
            ps.setString(2, thangNam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            con.close();
        } catch (Exception e) { }
        return false;
    }
    
    // Copy lại hàm sửa
    public boolean sua(DienNuoc dn) {
        String sql = "UPDATE diennuoc SET SoDienMoi=?, SoNuocMoi=?, TienDien=?, TienNuoc=?, ThanhTien=? WHERE MaHD=?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, dn.getSoDienMoi());
            ps.setInt(2, dn.getSoNuocMoi());
            ps.setDouble(3, dn.getTienDien());
            ps.setDouble(4, dn.getTienNuoc());
            ps.setDouble(5, dn.getThanhTien());
            ps.setInt(6, dn.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // Copy lại hàm getChiSoCuoiCung
    public DienNuoc getChiSoCuoiCung(String maPhong) {
        String sql = "SELECT * FROM diennuoc WHERE MaPhong = ? ORDER BY MaHD DESC LIMIT 1";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DienNuoc dn = new DienNuoc();
                dn.setSoDienMoi(rs.getInt("SoDienMoi"));
                dn.setSoNuocMoi(rs.getInt("SoNuocMoi"));
                return dn;
            }
            con.close();
        } catch (Exception e) {}
        return null;
    }
    
    // Copy lại hàm getGiaPhongTheoTen
    public double getGiaPhongTheoTen(String tenPhong) {
        double gia = 0;
        String sql = "SELECT lp.DonGia FROM phong p JOIN loaiphong lp ON p.MaLoaiPhong = lp.MaLoaiPhong WHERE p.TenPhong = ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tenPhong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) gia = rs.getDouble("DonGia");
            con.close();
        } catch (Exception e) {}
        return gia;
    }
    
    // Copy lại hàm xoa
    public boolean xoa(int maHD) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM diennuoc WHERE MaHD=?");
            ps.setInt(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
    
    // Hàm timKiem cũ (giữ lại để tương thích nếu cần, nhưng HoaDonView sẽ dùng timKiemNangCao)
    public List<DienNuoc> timKiem(String keyword) {
        return timKiemNangCao(keyword, -1);
    }
}