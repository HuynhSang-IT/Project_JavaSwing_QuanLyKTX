package com.quanlyktx.dao;

import com.quanlyktx.model.ChuyenPhong;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChuyenPhongDAO {

    // 1. Lấy danh sách lịch sử chuyển phòng
    public List<ChuyenPhong> getAll() {
        List<ChuyenPhong> list = new ArrayList<>();
        String sql = "SELECT cp.*, sv.HoTen FROM chuyenphong cp JOIN sinhvien sv ON cp.MaSV = sv.MaSV ORDER BY NgayChuyen DESC";
        try {
            Connection con = DatabaseHelper.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                ChuyenPhong cp = new ChuyenPhong();
                cp.setMaCP(rs.getInt("MaCP"));
                cp.setMaSV(rs.getString("MaSV"));
                cp.setTenSV(rs.getString("HoTen"));
                cp.setPhongCu(rs.getString("PhongCu"));
                cp.setPhongMoi(rs.getString("PhongMoi"));
                cp.setLyDo(rs.getString("LyDo"));
                cp.setNgayChuyen(rs.getDate("NgayChuyen"));
                cp.setNguoiThucHien(rs.getString("NguoiThucHien"));
                list.add(cp);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. THỰC HIỆN CHUYỂN PHÒNG (QUAN TRỌNG)
    public String thucHienChuyenPhong(String maSV, String phongCu, String maPhongMoi, String lyDo, String nguoiThucHien) {
        Connection con = DatabaseHelper.getConnection();
        try {
            con.setAutoCommit(false); // Bắt đầu giao dịch an toàn

            // BƯỚC 1: Tìm tên phòng mới để lưu vào lịch sử cho đẹp (Thay vì lưu mã 102)
            String tenPhongMoi = maPhongMoi;
            PreparedStatement psTen = con.prepareStatement("SELECT TenPhong FROM phong WHERE MaPhong=?");
            psTen.setString(1, maPhongMoi);
            ResultSet rsTen = psTen.executeQuery();
            if(rsTen.next()) tenPhongMoi = rsTen.getString("TenPhong");

            // BƯỚC 2: Ghi lịch sử
            String sqlHis = "INSERT INTO chuyenphong(MaSV, PhongCu, PhongMoi, LyDo, NgayChuyen, NguoiThucHien) VALUES(?,?,?,?,NOW(),?)";
            PreparedStatement psHis = con.prepareStatement(sqlHis);
            psHis.setString(1, maSV);
            psHis.setString(2, phongCu); // Lưu tên phòng cũ
            psHis.setString(3, tenPhongMoi); // Lưu tên phòng mới
            psHis.setString(4, lyDo);
            psHis.setString(5, nguoiThucHien);
            psHis.executeUpdate();

            // BƯỚC 3: Cập nhật phòng mới cho Sinh Viên
            String sqlUpdateSV = "UPDATE sinhvien SET MaPhong=? WHERE MaSV=?";
            PreparedStatement psSV = con.prepareStatement(sqlUpdateSV);
            psSV.setString(1, maPhongMoi);
            psSV.setString(2, maSV);
            psSV.executeUpdate();
            
            // BƯỚC 4: Cập nhật Hợp Đồng (Để lần sau load lên đúng phòng mới)
            String sqlUpdateHD = "UPDATE hopdong SET MaPhong=? WHERE MaSV=? AND NgayKetThuc >= CURDATE()";
            PreparedStatement psHD = con.prepareStatement(sqlUpdateHD);
            psHD.setString(1, maPhongMoi);
            psHD.setString(2, maSV);
            psHD.executeUpdate();

            // BƯỚC 5: Giảm số người ở Phòng Cũ (Nếu có phòng cũ)
            // LƯU Ý: Ở đây ta tìm phòng cũ theo TÊN (do giao diện gửi tên), nên phải cẩn thận
            if (phongCu != null && !phongCu.isEmpty() && !phongCu.equals("Chưa có phòng")) {
                // Thử tìm mã phòng cũ dựa trên tên (nếu phongCu là tên) HOẶC mã (nếu phongCu là mã)
                // Nhưng đơn giản nhất là cập nhật dựa trên TênPhong hoặc MaPhong
                String sqlGiam = "UPDATE phong SET SoNguoiDangO = GREATEST(SoNguoiDangO - 1, 0) WHERE TenPhong=? OR MaPhong=?";
                PreparedStatement psGiam = con.prepareStatement(sqlGiam);
                psGiam.setString(1, phongCu);
                psGiam.setString(2, phongCu);
                psGiam.executeUpdate();
                
                // Set trạng thái phòng cũ thành "Đang ở" (vì vừa bớt người)
                String sqlSttOld = "UPDATE phong SET TrangThai='Đang ở' WHERE TenPhong=? OR MaPhong=?";
                PreparedStatement psSttOld = con.prepareStatement(sqlSttOld);
                psSttOld.setString(1, phongCu);
                psSttOld.setString(2, phongCu);
                psSttOld.executeUpdate();
            }

            // BƯỚC 6: Tăng số người ở Phòng Mới
            // --- CẬP NHẬT ĐÚNG CỘT: SoNguoiDangO ---
            String sqlTang = "UPDATE phong SET SoNguoiDangO = SoNguoiDangO + 1 WHERE MaPhong=?";
            PreparedStatement psTang = con.prepareStatement(sqlTang);
            psTang.setString(1, maPhongMoi);
            int kqua = psTang.executeUpdate();
            
            // Kiểm tra xem có cập nhật được không
            if (kqua == 0) {
                con.rollback();
                return "Phòng mới (" + maPhongMoi + ") không tìm thấy trong CSDL!";
            }

            // BƯỚC 7: Cập nhật trạng thái phòng mới
            String sqlStatusNew = "UPDATE phong SET TrangThai = CASE WHEN SoNguoiDangO >= SoLuongMax THEN 'Đã đầy' ELSE 'Đang ở' END WHERE MaPhong=?";
            PreparedStatement psSttNew = con.prepareStatement(sqlStatusNew);
            psSttNew.setString(1, maPhongMoi);
            psSttNew.executeUpdate();

            con.commit(); // Chốt đơn!
            return "OK";

        } catch (Exception e) {
            try { con.rollback(); } catch(Exception ex){}
            e.printStackTrace();
            return "Lỗi hệ thống: " + e.getMessage();
        } finally {
            try { con.close(); } catch(Exception ex){}
        }
    }
}