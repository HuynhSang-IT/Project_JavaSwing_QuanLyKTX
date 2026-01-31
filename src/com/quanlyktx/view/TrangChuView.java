package com.quanlyktx.view;

import com.quanlyktx.model.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TrangChuView extends JFrame {

    private JPanel pnlMenuContent, pnlHienThi;
    private CardLayout cardLayout;
    private NhanVien taiKhoan;

    // --- MÀU SẮC GIAO DIỆN ---
    private final Color COLOR_PRIMARY = new Color(13, 71, 161);
    private final Color COLOR_SIDEBAR = new Color(33, 47, 61);
    private final Color COLOR_BTN_MENU = new Color(52, 73, 94);
    private final Color COLOR_BG_GRAY = new Color(245, 247, 250);

    // --- CÁC VIEW CON ---
    private HomeView viewHome; // Dashboard chính (File HomeView.java)
    
    private SinhVienView viewSV;
    private PhongView viewPhong;
    private HopDongView viewHopDong;
    private DienNuocView viewDienNuoc;
    private ThongKeView viewThongKe;
    private HoaDonView viewHoaDon;
    private SuCoView viewSuCo;
    private TaiSanView viewTaiSan;
    private KyLuatView viewKyLuat;
    private GuiXeView viewGuiXe;
    private NhanVienView viewNhanVien;
    private ChuyenPhongView viewChuyenPhong;
    private TangView viewTang;

    public TrangChuView(NhanVien nv) {
        this.taiKhoan = nv;
        setTitle("HỆ THỐNG QUẢN LÝ KÝ TÚC XÁ - " + nv.getHoTen());
        setSize(1350, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_PRIMARY);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lblLogo = new JLabel("QUẢN LÝ KÝ TÚC XÁ");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        
        JLabel lblUser = new JLabel("Xin chào, " + nv.getHoTen());
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblUser.setForeground(Color.WHITE);

        pnlHeader.add(lblLogo, BorderLayout.WEST);
        pnlHeader.add(lblUser, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. SIDEBAR ---
        JPanel pnlSidebar = new JPanel(new BorderLayout());
        pnlSidebar.setPreferredSize(new Dimension(240, 0));
        pnlSidebar.setBackground(COLOR_SIDEBAR);

        // Menu list
        pnlMenuContent = new JPanel();
        pnlMenuContent.setLayout(new BoxLayout(pnlMenuContent, BoxLayout.Y_AXIS));
        pnlMenuContent.setBackground(COLOR_SIDEBAR);
        pnlMenuContent.setBorder(new EmptyBorder(15, 10, 15, 10)); 
        
        phanQuyenMenu(nv.getQuyen());

        JScrollPane scrollMenu = new JScrollPane(pnlMenuContent);
        scrollMenu.setBorder(null);
        scrollMenu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);
        
        // Nút Đăng Xuất
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(COLOR_SIDEBAR);
        pnlBottom.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        RaisedButton btnLogout = new RaisedButton("Đăng Xuất");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.setPreferredSize(new Dimension(200, 45));
        
        btnLogout.addActionListener(e -> {
            this.dispose(); 
            new DangNhapView().setVisible(true); 
        });
        
        pnlBottom.add(btnLogout);
        pnlSidebar.add(scrollMenu, BorderLayout.CENTER);
        pnlSidebar.add(pnlBottom, BorderLayout.SOUTH);
        add(pnlSidebar, BorderLayout.WEST);

        // --- 3. MAIN CONTENT ---
        cardLayout = new CardLayout();
        pnlHienThi = new JPanel(cardLayout);
        pnlHienThi.setBackground(COLOR_BG_GRAY);

        // --- KHỞI TẠO CÁC VIEW ---
        
        // 1. Dashboard (Quan trọng: Khởi tạo HomeView)
        viewHome = new HomeView();
        pnlHienThi.add(viewHome, "dashboard");

        // 2. Các view chức năng
        viewSV = new SinhVienView(taiKhoan);        pnlHienThi.add(viewSV, "sinhvien");
        viewPhong = new PhongView(taiKhoan);        pnlHienThi.add(viewPhong, "phong");
        viewHopDong = new HopDongView(taiKhoan);    pnlHienThi.add(viewHopDong, "hopdong");
        viewDienNuoc = new DienNuocView(taiKhoan);  pnlHienThi.add(viewDienNuoc, "diennuoc");
        viewThongKe = new ThongKeView(taiKhoan);    pnlHienThi.add(viewThongKe, "thongke");
        viewHoaDon = new HoaDonView(taiKhoan);      pnlHienThi.add(viewHoaDon, "hoadon");
        viewSuCo = new SuCoView(taiKhoan);          pnlHienThi.add(viewSuCo, "suco");
        viewTaiSan = new TaiSanView(taiKhoan);      pnlHienThi.add(viewTaiSan, "taisan");
        viewKyLuat = new KyLuatView(taiKhoan);      pnlHienThi.add(viewKyLuat, "kyluat");
        viewGuiXe = new GuiXeView(taiKhoan);        pnlHienThi.add(viewGuiXe, "guixe");
        viewChuyenPhong = new ChuyenPhongView(taiKhoan); pnlHienThi.add(viewChuyenPhong, "chuyenphong");
        viewTang = new TangView(taiKhoan);          pnlHienThi.add(viewTang, "tang");
        viewNhanVien = new NhanVienView();          pnlHienThi.add(viewNhanVien, "nhanvien"); 

        // Mặc định hiển thị Dashboard khi mở lên
        cardLayout.show(pnlHienThi, "dashboard");
        
        add(pnlHienThi, BorderLayout.CENTER);
    }

    private void phanQuyenMenu(String quyen) {
        taoNutMenu("Trang Chủ", "dashboard");
        pnlMenuContent.add(Box.createRigidArea(new Dimension(0, 15)));

        if (quyen.equalsIgnoreCase("BaoVe")) {
            taoNutMenu("Điện Nước & Chỉ Số", "diennuoc");
            taoNutMenu("Sự Cố & Sửa Chữa", "suco");
            taoNutMenu("Gửi Xe", "guixe");
            taoNutMenu("Vi Phạm & Kỷ Luật", "kyluat");
        } else {
            taoNutMenu("Quản Lý Sinh Viên", "sinhvien");
            taoNutMenu("Quản Lý Phòng", "phong");
            taoNutMenu("Quản Lý Tầng/Khu", "tang");
            taoNutMenu("Hợp Đồng Thuê", "hopdong");
            taoNutMenu("Chuyển Phòng", "chuyenphong");
            
            pnlMenuContent.add(Box.createRigidArea(new Dimension(0, 15)));
            taoNutMenu("Điện Nước & Chỉ Số", "diennuoc");
            taoNutMenu("Thanh Toán Hóa Đơn", "hoadon");
            
            if(quyen.equalsIgnoreCase("Admin")) {
                taoNutMenu("Thống Kê Doanh Thu", "thongke");
            }
            
            pnlMenuContent.add(Box.createRigidArea(new Dimension(0, 15)));
            taoNutMenu("Sự Cố & Sửa Chữa", "suco");
            taoNutMenu("Tài Sản & Thiết Bị", "taisan");
            taoNutMenu("Vi Phạm & Kỷ Luật", "kyluat");
            taoNutMenu("Gửi Xe", "guixe");
            
            if(quyen.equalsIgnoreCase("Admin")) {
                pnlMenuContent.add(Box.createRigidArea(new Dimension(0, 15)));
                taoNutMenu("Tài Khoản Nhân Viên", "nhanvien");
            }
        }
    }

    // --- HÀM TẠO NÚT MENU ---
    private void taoNutMenu(String title, String key) {
        RaisedButton btn = new RaisedButton(title);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(COLOR_BTN_MENU);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 45));
        
        // Sự kiện: Chỉ cần gọi chuyenTrang(key) là đủ
        btn.addActionListener(e -> chuyenTrang(key));
        
        pnlMenuContent.add(btn);
        pnlMenuContent.add(Box.createRigidArea(new Dimension(0, 8))); 
    }

    // --- HÀM CHUYỂN TRANG & LÀM MỚI DỮ LIỆU ---
    public void chuyenTrang(String key) {
        cardLayout.show(pnlHienThi, key);
        
        switch (key) {
            case "dashboard":   
                if(viewHome != null) viewHome.loadThongKe(); 
                break;
            case "sinhvien":    if(viewSV != null) viewSV.loadData(); break;
            case "phong":       if(viewPhong != null) { viewPhong.loadTable(); viewPhong.loadComboBoxTang(); } break;
            case "tang":        if(viewTang != null) viewTang.loadTable(); break;
            case "hopdong":     if(viewHopDong != null) { viewHopDong.loadTable(); viewHopDong.loadDataComboBox(); } break;
            case "diennuoc":    if(viewDienNuoc != null) { viewDienNuoc.loadTable(); viewDienNuoc.loadComboBox(); } break;
            case "hoadon":      if(viewHoaDon != null) viewHoaDon.loadData(); break;
            case "thongke":     if(viewThongKe != null) viewThongKe.loadData(); break;
            case "suco":        if(viewSuCo != null) viewSuCo.loadDataInit(); break;
            case "taisan":      if(viewTaiSan != null) viewTaiSan.loadDataInit(); break;
            case "kyluat":      if(viewKyLuat != null) viewKyLuat.loadTable(); break;
            case "guixe":       if(viewGuiXe != null) { viewGuiXe.loadTable(); viewGuiXe.loadDataInit(); } break;
            case "chuyenphong": 
                if(viewChuyenPhong != null) {
                    viewChuyenPhong.loadTable();     // Load lịch sử
                    viewChuyenPhong.loadDataInit();  // Load lại danh sách SV và Phòng mới nhất
                } 
                break;

            case "nhanvien":    if(viewNhanVien != null) viewNhanVien.loadTable(); break;
        }
    }
}