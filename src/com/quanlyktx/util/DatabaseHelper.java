package com.quanlyktx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    // Cấu hình kết nối
    // Lưu ý: Tên DB là 'quanlyktx' như trong ảnh bạn gửi
    private static final String URL = "jdbc:mysql://localhost:3306/quanlyktx"; 
    private static final String USER = "root"; // Mặc định XAMPP là root
    private static final String PASS = "";     // Mặc định XAMPP không có mật khẩu

    // Hàm tạo kết nối
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Nạp Driver (Bắt buộc cho các bản Java cũ & mới để ổn định)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Mở kết nối
            conn = DriverManager.getConnection(URL, USER, PASS);
            // System.out.println("Kết nối thành công!"); 
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Chưa thêm thư viện MySQL JDBC!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối đến XAMPP. Kiểm tra xem MySQL đã Start chưa?");
            e.printStackTrace();
        }
        return conn;
    }

    // Hàm đóng kết nối (Dùng sau khi xong việc để giải phóng RAM)
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // --- HÀM MAIN ĐỂ CHẠY THỬ NGAY ---
    public static void main(String[] args) {
        System.out.println("--- Đang kiểm tra kết nối ---");
        Connection c = getConnection();
        if (c != null) {
            System.out.println("✅ CHÚC MỪNG! Đã kết nối thành công tới Database 'quanlyktx'");
            closeConnection(c);
        } else {
            System.out.println("❌ KẾT NỐI THẤT BẠI. Hãy kiểm tra lại XAMPP.");
        }
    }
}