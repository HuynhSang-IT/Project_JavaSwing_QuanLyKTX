package com.quanlyktx.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import com.quanlyktx.dao.ThongKeDAO;

public class HomeView extends JPanel {
    
    private ThongKeDAO dao = new ThongKeDAO();
    
    // Các Label hiển thị số liệu
    private JLabel lblSoSV, lblSoPhong, lblPhongTrong, lblHopDong; 
    private JLabel lblSuCo, lblViPham, lblDoanhThu, lblHoaDonNo;

    // Màu sắc chuẩn Enterprise (Đồng bộ với TrangChuView mới)
    private final Color COL_SV      = new Color(0, 123, 255);   // Blue
    private final Color COL_PHONG   = new Color(23, 162, 184);  // Cyan
    private final Color COL_TRONG   = new Color(40, 167, 69);   // Green
    private final Color COL_HOPDONG = new Color(108, 117, 125); // Grey
    private final Color COL_SUCO    = new Color(255, 193, 7);   // Yellow
    private final Color COL_VIPHAM  = new Color(220, 53, 69);   // Red
    private final Color COL_TIEN    = new Color(102, 16, 242);  // Purple
    private final Color COL_NO      = new Color(52, 58, 64);    // Dark

    public HomeView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Nền xám nhạt
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("TỔNG QUAN TÌNH HÌNH KÝ TÚC XÁ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle, BorderLayout.NORTH);

        // --- GRID CARDS (2 hàng, 4 cột) ---
        JPanel pnlCards = new JPanel(new GridLayout(2, 4, 20, 20));
        pnlCards.setBackground(new Color(245, 247, 250)); // Trùng màu nền

        // Khởi tạo các Label
        lblSoSV = createValueLabel();
        lblSoPhong = createValueLabel();
        lblPhongTrong = createValueLabel();
        lblHopDong = createValueLabel();
        lblSuCo = createValueLabel();
        lblViPham = createValueLabel();
        lblDoanhThu = createValueLabel();
        lblHoaDonNo = createValueLabel();

        // Add Cards vào Panel
        pnlCards.add(createCard("TỔNG SINH VIÊN", lblSoSV, COL_SV));
        pnlCards.add(createCard("TỔNG SỐ PHÒNG", lblSoPhong, COL_PHONG));
        pnlCards.add(createCard("PHÒNG TRỐNG", lblPhongTrong, COL_TRONG));
        pnlCards.add(createCard("HỢP ĐỒNG", lblHopDong, COL_HOPDONG));

        pnlCards.add(createCard("SỰ CỐ CHỜ XỬ LÝ", lblSuCo, COL_SUCO));
        pnlCards.add(createCard("VI PHẠM KỶ LUẬT", lblViPham, COL_VIPHAM));
        pnlCards.add(createCard("DOANH THU (VNĐ)", lblDoanhThu, COL_TIEN));
        pnlCards.add(createCard("HÓA ĐƠN NỢ", lblHoaDonNo, COL_NO));

        add(pnlCards, BorderLayout.CENTER);
        
        // Decor footer
        JLabel lblDecor = new JLabel("Hệ thống quản lý Ký túc xá v2.0 - Enterprise Edition", JLabel.CENTER);
        lblDecor.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblDecor.setForeground(Color.GRAY);
        add(lblDecor, BorderLayout.SOUTH);

        // Load dữ liệu
        loadThongKe();
    }
    
    // --- HÀM PUBLIC ĐỂ RELOAD DỮ LIỆU ---
    public void loadThongKe() {
        try {
            lblSoSV.setText(String.valueOf(dao.getTongSinhVien()));
            lblSoPhong.setText(String.valueOf(dao.getTongPhong()));
            lblPhongTrong.setText(String.valueOf(dao.getPhongTrong()));
            lblHopDong.setText(String.valueOf(dao.getSoHopDong()));
            lblSuCo.setText(String.valueOf(dao.getSuCoChuaXuLy()));
            lblViPham.setText(String.valueOf(dao.getSoViPham()));
            
            DecimalFormat df = new DecimalFormat("#,###");
            lblDoanhThu.setText(df.format(dao.getTongDoanhThu()));
            lblHoaDonNo.setText(String.valueOf(dao.getSoHoaDonChuaThu()));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- HELPER METHODS ---
    
    private JLabel createValueLabel() {
        JLabel lbl = new JLabel("...");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    private JPanel createCard(String title, JLabel lblValue, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        
        // Viền màu bên trái (Style Enterprise)
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, color),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Viền bóng mờ nhẹ
        card.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
             card.getBorder()
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.GRAY);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }
}