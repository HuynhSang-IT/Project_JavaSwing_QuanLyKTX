package com.quanlyktx.dao;

import com.quanlyktx.model.DienNuoc;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    // 1. Đếm tổng sinh viên
    public int getTongSinhVien() { 
        return getCount("SELECT COUNT(*) FROM sinhvien"); 
    }

    // 2. Đếm tổng số phòng
    public int getTongPhong() { 
        return getCount("SELECT COUNT(*) FROM phong"); 
    }

    // 3. [FIXED] Đếm số phòng trống (Dựa vào số người đang ở = 0)
    public int getPhongTrong() { 
        // Code cũ: WHERE TrangThai = 'Trống' (Dễ sai sót do nhập liệu)
        // Code mới: WHERE SoNguoiDangO = 0 (Chính xác tuyệt đối)
        return getCount("SELECT COUNT(*) FROM phong WHERE SoNguoiDangO = 0"); 
    }

    // 4. Số lượng Nam
    public int getSoLuongNam() { 
        return getCount("SELECT COUNT(*) FROM sinhvien WHERE GioiTinh = 'Nam'"); 
    }
    
    // 5. Số lượng Nữ
    public int getSoLuongNu() { 
        return getCount("SELECT COUNT(*) FROM sinhvien WHERE GioiTinh = 'Nữ'"); 
    }

    // 6. Tổng doanh thu (Chỉ tính hóa đơn ĐÃ THU)
    public double getTongDoanhThu() {
        double total = 0;
        String sql = "SELECT SUM(ThanhTien) FROM diennuoc WHERE TrangThai = 'Đã thu'";
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) total = rs.getDouble(1);
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return total;
    }
    
    // 7. Số hóa đơn chưa thu
    public int getSoHoaDonChuaThu() {
        return getCount("SELECT COUNT(*) FROM diennuoc WHERE TrangThai = 'Chưa thu' OR TrangThai = 'Chờ lập HD'");
    }
    
    // 8. Đếm số sự cố chưa xử lý
    public int getSuCoChuaXuLy() {
        return getCount("SELECT COUNT(*) FROM suco WHERE TrangThai = 'Chưa xử lý'");
    }

    // 9. Đếm số kỷ luật / vi phạm
    public int getSoViPham() {
        return getCount("SELECT COUNT(*) FROM kyluat");
    }

    // 10. Đếm số hợp đồng (có thể thêm điều kiện ngày nếu cần)
    public int getSoHopDong() {
        return getCount("SELECT COUNT(*) FROM hopdong"); 
    }
    
    // --- HÀM TÌM KIẾM THỐNG KÊ (GIỮ NGUYÊN CODE CỦA BẠN VÌ ĐÃ TỐT) ---
    public List<DienNuoc> timKiemThongKe(String thoiGian, String trangThai, int maTang) {
        List<DienNuoc> list = new ArrayList<>();
        
        // SQL JOIN 3 BẢNG: DIENNUOC - PHONG - TANG
        StringBuilder sql = new StringBuilder(
            "SELECT dn.*, p.TenPhong, t.TenTang " +
            "FROM diennuoc dn " +
            "JOIN phong p ON dn.MaPhong = p.MaPhong " +
            "LEFT JOIN tang t ON p.MaTang = t.MaTang " + 
            "WHERE 1=1"
        );
        
        // 1. Lọc theo thời gian (Tháng/Năm)
        if (thoiGian != null && !thoiGian.isEmpty() && !thoiGian.equals("Tất cả")) {
            sql.append(" AND dn.ThangNam LIKE ?");
        }
        
        // 2. Lọc theo trạng thái (Đã thu/Chưa thu)
        if (trangThai != null && !trangThai.isEmpty() && !trangThai.equals("Hiện Tất Cả")) {
            sql.append(" AND dn.TrangThai = ?");
        }
        
        // 3. Lọc theo Tầng
        if (maTang != -1) {
            sql.append(" AND p.MaTang = ?");
        }
        
        sql.append(" ORDER BY MaHD DESC");

        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            
            int index = 1;
            
            if (thoiGian != null && !thoiGian.isEmpty() && !thoiGian.equals("Tất cả")) {
                ps.setString(index++, "%" + thoiGian); 
            }
            if (trangThai != null && !trangThai.isEmpty() && !trangThai.equals("Hiện Tất Cả")) {
                ps.setString(index++, trangThai);
            }
            if (maTang != -1) {
                ps.setInt(index++, maTang);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DienNuoc dn = new DienNuoc();
                dn.setMaHD(rs.getInt("MaHD"));
                dn.setMaPhong(rs.getString("MaPhong"));
                
                // Ghép tên Tầng vào tên Phòng: "P.101 (Tầng 1)"
                String tenP = rs.getString("TenPhong");
                String tenT = rs.getString("TenTang");
                dn.setTenPhong(tenP + (tenT != null ? " (" + tenT + ")" : ""));
                
                dn.setThangNam(rs.getString("ThangNam"));
                
                // Lấy tiền (xử lý null an toàn)
                dn.setTienDien(rs.getDouble("TienDien"));
                dn.setTienNuoc(rs.getDouble("TienNuoc"));
                dn.setTienPhong(rs.getDouble("TienPhong"));
                dn.setThanhTien(rs.getDouble("ThanhTien"));
                
                dn.setTrangThai(rs.getString("TrangThai"));
                dn.setHinhThucTT(rs.getString("HinhThucTT"));
                
                list.add(dn);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // Hàm phụ trợ đếm (Code của bạn OK rồi)
    private int getCount(String sql) {
        int count = 0;
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
            con.close();
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }
}