package com.quanlyktx.view;

import com.quanlyktx.dao.NhanVienDAO;
import com.quanlyktx.model.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangNhapView extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnExit;
    private NhanVienDAO dao = new NhanVienDAO();

    // Màu sắc chủ đạo (Xanh dương hiện đại)
    private final Color COLOR_PRIMARY = new Color(0, 102, 204);
    private final Color COLOR_SECONDARY = new Color(51, 153, 255);
    private final Color COLOR_BG_RIGHT = Color.WHITE;

    public DangNhapView() {
        setTitle("Đăng Nhập Hệ Thống KTX");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); // Chia đôi màn hình
        setResizable(false);

        // --- PHẦN 1: PANEL TRÁI (BRANDING) ---
        JPanel pnlLeft = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Tạo hiệu ứng màu Gradient chéo
                GradientPaint gp = new GradientPaint(0, 0, COLOR_PRIMARY, getWidth(), getHeight(), COLOR_SECONDARY);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlLeft.setLayout(new GridBagLayout());
        
        // Nội dung bên trái
        JPanel pnlBrand = new JPanel(new GridLayout(2, 1));
        pnlBrand.setOpaque(false);
        
        // --- LOGO ---
        JLabel lblIcon = new JLabel("", JLabel.CENTER);
        try {
            ImageIcon iconGoc = new ImageIcon(getClass().getResource("/imgaes/dnc.png"));
            Image img = iconGoc.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblIcon.setText("🏫"); 
            lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 100));
            lblIcon.setForeground(Color.WHITE);
        }
        
        JLabel lblBrandName = new JLabel("<html><center>HỆ THỐNG QUẢN LÝ<br>KÝ TÚC XÁ</center></html>", JLabel.CENTER);
        lblBrandName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBrandName.setForeground(Color.WHITE);
        
        pnlBrand.add(lblIcon);
        pnlBrand.add(lblBrandName);
        pnlLeft.add(pnlBrand);

        // --- PHẦN 2: PANEL PHẢI (FORM NHẬP) ---
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(COLOR_BG_RIGHT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tiêu đề
        JLabel lblLogin = new JLabel("ĐĂNG NHẬP");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLogin.setForeground(COLOR_PRIMARY);
        lblLogin.setHorizontalAlignment(JLabel.CENTER);
        
        // Ô nhập liệu
        txtUser = createStyledTextField();
        txtPass = createStyledPasswordField();
        
        JLabel lblUser = new JLabel("Tài khoản:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Nút bấm
        btnLogin = createStyledButton("ĐĂNG NHẬP", COLOR_PRIMARY);
        btnExit = createStyledButton("Thoát", new Color(231, 76, 60)); // Màu đỏ

        // --- SỬA LỖI DESIGN: DÙNG .clone() CHO MỌI DÒNG ADD ---
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnlRight.add(lblLogin, (GridBagConstraints) gbc.clone());
        
        gbc.gridy = 1; 
        pnlRight.add(new JLabel(" "), (GridBagConstraints) gbc.clone()); // Khoảng trống
        
        gbc.gridy = 2; gbc.gridwidth = 2;
        pnlRight.add(lblUser, (GridBagConstraints) gbc.clone());
        
        gbc.gridy = 3;
        pnlRight.add(txtUser, (GridBagConstraints) gbc.clone());
        
        gbc.gridy = 4;
        pnlRight.add(lblPass, (GridBagConstraints) gbc.clone());
        
        gbc.gridy = 5;
        pnlRight.add(txtPass, (GridBagConstraints) gbc.clone());
        
        gbc.gridy = 6; 
        pnlRight.add(new JLabel(" "), (GridBagConstraints) gbc.clone()); // Khoảng trống
        
        gbc.gridy = 7; 
        pnlRight.add(btnLogin, (GridBagConstraints) gbc.clone());
        
        gbc.gridy = 8;
        pnlRight.add(btnExit, (GridBagConstraints) gbc.clone());

        // --- THÊM VÀO FRAME ---
        add(pnlLeft);
        add(pnlRight);

        // --- XỬ LÝ SỰ KIỆN ---
        btnLogin.addActionListener(e -> xuLyDangNhap());
        btnExit.addActionListener(e -> System.exit(0));
        
        // Enter ở ô pass cũng đăng nhập luôn cho tiện
        txtPass.addActionListener(e -> xuLyDangNhap());
    }

    private void xuLyDangNhap() {
        String u = txtUser.getText();
        String p = new String(txtPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        NhanVien nv = dao.checkLogin(u, p);
        if (nv != null) {
            // Đăng nhập thành công -> Mở trang chủ
            this.dispose(); // Đóng form đăng nhập
            new TrangChuView(nv).setVisible(true); // Mở trang chủ (Truyền thông tin NV vào)
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- CÁC HÀM TRANG TRÍ UI ---
    
    private JTextField createStyledTextField() {
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10) // Padding bên trong
        ));
        return txt;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField txt = new JPasswordField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng Hover chuột
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    // Main để test riêng form này
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        new DangNhapView().setVisible(true);
    }
}