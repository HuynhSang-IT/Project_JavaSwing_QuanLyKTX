package com.quanlyktx.view;

import com.quanlyktx.dao.GuiXeDAO;
import com.quanlyktx.dao.SinhVienDAO;
import com.quanlyktx.model.GuiXe;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.SinhVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

public class GuiXeView extends JPanel {
    
    // Input
    private JComboBox<String> cboSinhVien;
    private JTextField txtBienSo, txtMauXe, txtPhi;
    private JComboBox<String> cboLoaiXe;
    private JButton btnDangKy, btnGiaHan, btnHuyGui, btnMoi;
    
    // Stats Panel
    private JLabel lblTongXe, lblDoanhThu;
    
    // Table
    private JTable tblData;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    
    private GuiXeDAO dao = new GuiXeDAO();
    private SinhVienDAO svDao = new SinhVienDAO();
    
    private int maVeDangChon = -1;

    public GuiXeView(NhanVien user) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("QUẢN LÝ GỬI XE (THÁNG)", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(39, 174, 96)); 
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        // Search
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.add(new JLabel("Tìm Biển số/Tên:"));
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tìm");
        JButton btnReload = new JButton("Tải lại");
        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTim); pnlSearch.add(btnReload);
        pnlHeader.add(pnlSearch, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // 1. LEFT PANEL (Form + Stats)
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setBackground(Color.WHITE);
        
        // --- SỬA 1: Tăng chiều rộng Panel trái lên 450px ---
        pnlLeft.setPreferredSize(new Dimension(450, 0));
        
        // A. Form Nhập
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Đăng ký vé tháng"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        pnlInput.setBackground(new Color(250, 250, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        cboSinhVien = new JComboBox<>();
        
        // --- SỬA 2: Tăng chiều dài ô nhập liệu (20 cột) ---
        txtBienSo = new JTextField(20);
        cboLoaiXe = new JComboBox<>(new String[]{"Xe máy", "Xe đạp", "Xe điện"});
        txtMauXe = new JTextField(20);
        txtPhi = new JTextField("50000", 20); 
        txtPhi.setEditable(false); 
        txtPhi.setFont(new Font("Arial", Font.BOLD, 14));
        
        themComponent(pnlInput, gbc, 0, "Sinh Viên:", cboSinhVien);
        themComponent(pnlInput, gbc, 1, "Biển Số:", txtBienSo);
        themComponent(pnlInput, gbc, 2, "Loại Xe:", cboLoaiXe);
        themComponent(pnlInput, gbc, 3, "Màu Xe:", txtMauXe);
        themComponent(pnlInput, gbc, 4, "Phí Tháng:", txtPhi);
        
        // --- SỬA 3: Nút bấm to đẹp (Cao 40-45px) ---
        btnDangKy = new JButton("ĐĂNG KÝ & THU TIỀN");
        btnDangKy.setBackground(new Color(46, 204, 113)); btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangKy.setPreferredSize(new Dimension(0, 45)); // Nút to
        
        btnGiaHan = new JButton("Gia Hạn 1 Tháng");
        btnGiaHan.setBackground(new Color(52, 152, 219)); btnGiaHan.setForeground(Color.WHITE);
        btnGiaHan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGiaHan.setPreferredSize(new Dimension(0, 40));
        
        btnHuyGui = new JButton("Trả Xe / Hủy");
        btnHuyGui.setBackground(new Color(231, 76, 60)); btnHuyGui.setForeground(Color.WHITE);
        btnHuyGui.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuyGui.setPreferredSize(new Dimension(0, 40));
        
        btnMoi = new JButton("Làm mới");
        btnMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMoi.setPreferredSize(new Dimension(0, 40));

        // Space
        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy=5; gbcSpace.gridwidth=2; 
        pnlInput.add(new JLabel(" "), gbcSpace);
        
        // Nút Đăng ký (Full width)
        GridBagConstraints gbcBtnDK = (GridBagConstraints) gbc.clone();
        gbcBtnDK.gridy=6; gbcBtnDK.gridwidth=2;
        pnlInput.add(btnDangKy, gbcBtnDK);
        
        // Panel 3 nút nhỏ
        JPanel pnlBtn = new JPanel(new GridLayout(1, 3, 5, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnGiaHan); pnlBtn.add(btnHuyGui); pnlBtn.add(btnMoi);
        
        GridBagConstraints gbcPnlBtn = (GridBagConstraints) gbc.clone();
        gbcPnlBtn.gridy=7; gbcPnlBtn.gridwidth=2;
        pnlInput.add(pnlBtn, gbcPnlBtn);
        
        // Đẩy form lên
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy=8; gbcPush.weighty=1.0; 
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlLeft.add(pnlInput, BorderLayout.CENTER);
        
        // B. Mini Stats (Thống kê nhỏ bên dưới)
        JPanel pnlStats = new JPanel(new GridLayout(2, 1));
        pnlStats.setBorder(BorderFactory.createTitledBorder("Thống kê nhanh"));
        pnlStats.setBackground(new Color(230, 255, 234)); // Xanh nhạt
        pnlStats.setPreferredSize(new Dimension(0, 100));
        
        lblTongXe = new JLabel("Tổng xe: 0", JLabel.CENTER);
        lblTongXe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoanhThu = new JLabel("Doanh thu tháng: 0 đ", JLabel.CENTER);
        lblDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 16)); lblDoanhThu.setForeground(new Color(192, 57, 43));
        
        pnlStats.add(lblTongXe);
        pnlStats.add(lblDoanhThu);
        pnlLeft.add(pnlStats, BorderLayout.SOUTH);
        
        pnlCenter.add(pnlLeft, BorderLayout.WEST);
        
        // 2. TABLE
        String[] headers = {"Mã Vé", "SV", "Biển Số", "Loại", "Màu", "Ngày ĐK", "Hết Hạn", "Phí", "Trạng Thái"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30); // Row cao hơn
        tblData.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        // --- LOGIC ---
        loadDataInit();
        loadTable();
        
        // Tự động chỉnh giá tiền theo loại xe
        cboLoaiXe.addActionListener(e -> {
            String loai = cboLoaiXe.getSelectedItem().toString();
            if(loai.equals("Xe đạp")) txtPhi.setText("30000");
            else txtPhi.setText("50000");
        });

        // Click bảng
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    maVeDangChon = Integer.parseInt(model.getValueAt(r, 0).toString());
                    txtBienSo.setText(model.getValueAt(r, 2).toString());
                    cboLoaiXe.setSelectedItem(model.getValueAt(r, 3).toString());
                    txtMauXe.setText(model.getValueAt(r, 4).toString());
                    txtPhi.setText(model.getValueAt(r, 7).toString().replace(",", "").replace(" đ", ""));
                }
            }
        });

        // ĐĂNG KÝ
        btnDangKy.addActionListener(e -> {
            try {
                if(cboSinhVien.getSelectedItem() == null) return;
                String maSV = cboSinhVien.getSelectedItem().toString().split(" - ")[0];
                
                GuiXe gx = new GuiXe();
                gx.setMaSV(maSV);
                gx.setBienSo(txtBienSo.getText());
                gx.setLoaiXe(cboLoaiXe.getSelectedItem().toString());
                gx.setMauXe(txtMauXe.getText());
                gx.setPhiThang(Double.parseDouble(txtPhi.getText()));
                gx.setTrangThai("Đã thanh toán"); 
                
                if(dao.dangKy(gx)) {
                    JOptionPane.showMessageDialog(null, "Đăng ký thành công! Đã thu " + txtPhi.getText() + "đ");
                    loadTable(); updateStats(); clearForm();
                }
            } catch(Exception ex) { JOptionPane.showMessageDialog(null, "Lỗi nhập liệu!"); }
        });
        
        // GIA HẠN
        btnGiaHan.addActionListener(e -> {
            if(maVeDangChon == -1) { JOptionPane.showMessageDialog(null, "Chọn vé cần gia hạn!"); return; }
            int confirm = JOptionPane.showConfirmDialog(null, "Thu tiền tháng mới (" + txtPhi.getText() + "đ) và Gia hạn?");
            if(confirm == JOptionPane.YES_OPTION) {
                if(dao.giaHan(maVeDangChon)) {
                    JOptionPane.showMessageDialog(null, "Gia hạn thành công!");
                    loadTable(); updateStats();
                }
            }
        });
        
        // HỦY / TRẢ XE
        btnHuyGui.addActionListener(e -> {
            if(maVeDangChon != -1 && JOptionPane.showConfirmDialog(null, "Xác nhận trả vé xe này?") == JOptionPane.YES_OPTION) {
                dao.huyGui(maVeDangChon); loadTable(); updateStats(); clearForm();
            }
        });
        
        btnMoi.addActionListener(e -> clearForm());
        
        btnTim.addActionListener(e -> {
            String tk = txtTimKiem.getText();
            if(!tk.isEmpty()) doDuLieuVaoBang(dao.timKiem(tk));
            else loadTable();
        });
        
        btnReload.addActionListener(e -> { txtTimKiem.setText(""); loadTable(); });
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnHuyGui.setEnabled(false);
            btnHuyGui.setBackground(Color.LIGHT_GRAY);
            btnHuyGui.setToolTipText("Chỉ Admin mới có quyền hủy vé!");
            lblDoanhThu.setText("Doanh thu: *****");
        }
    }

    public void loadDataInit() {
        cboSinhVien.removeAllItems();
        List<SinhVien> listSV = svDao.getAllSinhVien();
        for(SinhVien sv : listSV) cboSinhVien.addItem(sv.getMaSV() + " - " + sv.getHoTen());
    }
    
    public void loadTable() {
        doDuLieuVaoBang(dao.getAll());
        updateStats(); 
    }
    
    private void updateStats() {
        lblTongXe.setText("Tổng xe đang gửi: " + dao.getSoLuongXe());
        if (!lblDoanhThu.getText().contains("*")) {
            DecimalFormat df = new DecimalFormat("#,### đ");
            lblDoanhThu.setText("Doanh thu tháng: " + df.format(dao.getDoanhThuThangNay()));
        }
    }
    
    private void doDuLieuVaoBang(List<GuiXe> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for(GuiXe gx : list) {
            model.addRow(new Object[]{
                gx.getMaVe(), gx.getTenSV(), gx.getBienSo(), gx.getLoaiXe(), gx.getMauXe(),
                gx.getNgayDangKy(), gx.getNgayHetHan(), df.format(gx.getPhiThang()), gx.getTrangThai()
            });
        }
    }
    
    private void clearForm() {
        maVeDangChon = -1; txtBienSo.setText(""); txtMauXe.setText(""); tblData.clearSelection();
    }

    private void themComponent(JPanel p, GridBagConstraints gbc, int row, String label, JComponent comp) {
        GridBagConstraints c1 = (GridBagConstraints) gbc.clone();
        c1.gridx = 0; 
        c1.gridy = row; 
        c1.gridwidth = 1; 
        c1.weightx = 0.0;
        p.add(new JLabel(label), c1);
        
        GridBagConstraints c2 = (GridBagConstraints) gbc.clone();
        c2.gridx = 1; 
        c2.gridy = row; 
        c2.weightx = 1.0; 
        p.add(comp, c2);
    }
}