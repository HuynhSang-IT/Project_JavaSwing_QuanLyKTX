package com.quanlyktx.dao;

import com.quanlyktx.model.HopDong;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HopDongDAO {

    // 1. Lấy danh sách hợp đồng
    public List<HopDong> getAllHopDong() {
        List<HopDong> list = new ArrayList<>();
        // Sửa query đơn giản, tránh lỗi
        String sql = "SELECT h.*, s.HoTen, p.TenPhong " +
                     "FROM hopdong h " +
                     "JOIN sinhvien s ON h.MaSV = s.MaSV " +
                     "JOIN phong p ON h.MaPhong = p.MaPhong";
        try {
            Connection con = DatabaseHelper.getConnection();
            // Dùng Statement bình thường cho query select đơn giản
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setMaHopDong(rs.getInt("MaHopDong"));
                hd.setMaSV(rs.getString("MaSV"));
                hd.setMaPhong(rs.getString("MaPhong"));
                hd.setNgayBatDau(rs.getDate("NgayBatDau"));
                hd.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                
                // Lấy thông tin phụ hiển thị
                hd.setTenSV(rs.getString("HoTen"));
                hd.setTenPhong(rs.getString("TenPhong"));
                list.add(hd);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. THÊM HỢP ĐỒNG (QUAN TRỌNG: CẬP NHẬT CẢ BẢNG SINH VIÊN)
    public boolean themHopDong(HopDong hd) {
        Connection con = DatabaseHelper.getConnection();
        try {
            con.setAutoCommit(false); // Bắt đầu Transaction (Giao dịch)

            // A. Kiểm tra phòng đầy chưa (Lấy trực tiếp từ bảng PHONG)
            String checkSql = "SELECT SoNguoiDangO, SoLuongMax FROM phong WHERE MaPhong = ?";
            PreparedStatement psCheck = con.prepareStatement(checkSql);
            psCheck.setString(1, hd.getMaPhong());
            ResultSet rsCheck = psCheck.executeQuery();
            
            if (rsCheck.next()) {
                int dangO = rsCheck.getInt("SoNguoiDangO");
                int toiDa = rsCheck.getInt("SoLuongMax");
                if (dangO >= toiDa) {
                    System.out.println("Phòng đã đầy!");
                    return false; 
                }
            } else {
                return false; // Không tìm thấy phòng
            }

            // B. Thêm Hợp Đồng vào bảng HOPDONG
            String sqlInsert = "INSERT INTO hopdong(MaSV, MaPhong, NgayBatDau, NgayKetThuc) VALUES(?, ?, ?, ?)";
            PreparedStatement psInsert = con.prepareStatement(sqlInsert);
            psInsert.setString(1, hd.getMaSV());
            psInsert.setString(2, hd.getMaPhong());
            psInsert.setDate(3, hd.getNgayBatDau());
            psInsert.setDate(4, hd.getNgayKetThuc());
            psInsert.executeUpdate();

            // C. QUAN TRỌNG: Cập nhật MaPhong cho SINHVIEN (Để tab Chuyển Phòng thấy được)
            String sqlUpdateSV = "UPDATE SinhVien SET MaPhong = ? WHERE MaSV = ?";
            PreparedStatement psSV = con.prepareStatement(sqlUpdateSV);
            psSV.setString(1, hd.getMaPhong());
            psSV.setString(2, hd.getMaSV());
            psSV.executeUpdate();

            // D. Tăng số người đang ở trong bảng PHONG
            String sqlUpdatePhong = "UPDATE phong SET SoNguoiDangO = SoNguoiDangO + 1 WHERE MaPhong = ?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdatePhong);
            psUpdate.setString(1, hd.getMaPhong());
            psUpdate.executeUpdate();

            // E. Cập nhật trạng thái phòng (Nếu đầy thì ghi 'Đã đầy')
            String sqlStatus = "UPDATE phong SET TrangThai = CASE WHEN SoNguoiDangO >= SoLuongMax THEN 'Đã đầy' ELSE 'Đang ở' END WHERE MaPhong = ?";
            PreparedStatement psStatus = con.prepareStatement(sqlStatus);
            psStatus.setString(1, hd.getMaPhong());
            psStatus.executeUpdate();

            con.commit(); // Thành công thì lưu tất cả
            return true;

        } catch (Exception e) {
            try { con.rollback(); } catch(Exception ex){} // Hoàn tác nếu lỗi
            System.err.println("Lỗi Thêm HĐ: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { con.close(); } catch(Exception ex){}
        }
    }
    
    // 3. Tìm kiếm
    public List<HopDong> timKiemHopDong(String keyword) {
        List<HopDong> list = new ArrayList<>();
        String sql = "SELECT h.*, s.HoTen, p.TenPhong " +
                     "FROM hopdong h " +
                     "JOIN sinhvien s ON h.MaSV = s.MaSV " +
                     "JOIN phong p ON h.MaPhong = p.MaPhong " +
                     "WHERE s.HoTen LIKE ? OR p.TenPhong LIKE ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            String k = "%" + keyword + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HopDong hd = new HopDong();
                hd.setMaHopDong(rs.getInt("MaHopDong"));
                hd.setMaSV(rs.getString("MaSV"));
                hd.setMaPhong(rs.getString("MaPhong"));
                hd.setNgayBatDau(rs.getDate("NgayBatDau"));
                hd.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                hd.setTenSV(rs.getString("HoTen"));
                hd.setTenPhong(rs.getString("TenPhong"));
                list.add(hd);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
 // 4. Kiểm tra xem Hợp đồng đã tồn tại chưa (Trùng SV, Phòng và Ngày bắt đầu)
    public boolean checkTonTai(String maSV, String maPhong, Date ngayBatDau) {
        boolean exist = false;
        String sql = "SELECT COUNT(*) FROM hopdong WHERE MaSV = ? AND MaPhong = ? AND NgayBatDau = ?";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maSV);
            ps.setString(2, maPhong);
            ps.setDate(3, ngayBatDau);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exist = true; // Đã có ít nhất 1 dòng trùng
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }
}