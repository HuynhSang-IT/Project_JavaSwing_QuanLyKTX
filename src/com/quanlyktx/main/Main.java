package com.quanlyktx.main;

import com.quanlyktx.view.DangNhapView;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Thiết lập giao diện giống hệ điều hành (Windows/Mac) cho đẹp và mượt
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chạy màn hình Đăng Nhập đầu tiên
        java.awt.EventQueue.invokeLater(() -> {
            new DangNhapView().setVisible(true);
        });
    }
}