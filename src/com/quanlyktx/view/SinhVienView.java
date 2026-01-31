package com.quanlyktx.view;

import com.quanlyktx.dao.SinhVienDAO;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.SinhVien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SinhVienView extends JPanel {
    private JTextField txtMaSV, txtHoTen, txtCMND, txtSDT, txtEmail, txtQueQuan;
    private JComboBox<String> cboGioiTinh;
    private JTable tblSinhVien;
    private DefaultTableModel model;
    
    private JTextField txtTimKiem;
    private JButton btnTim, btnHuyTim; 
    
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    private SinhVienDAO dao = new SinhVienDAO();

    public SinhVienView(NhanVien user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. PANEL TOP ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ SINH VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTop.add(lblTitle, BorderLayout.WEST);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        
        txtTimKiem = new JTextField(15);
        btnTim = new JButton("Tìm");
        btnHuyTim = new JButton("Hủy");
        
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnHuyTim);
        
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);

        // --- 2. FORM NHẬP LIỆU ---
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));
        
        // Tăng chiều rộng lên 450px
        pnlInput.setPreferredSize(new Dimension(450, 0)); 
        pnlInput.setBackground(new Color(250, 250, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Tăng khoảng cách
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tăng kích thước ô nhập (20 cột)
        txtMaSV = new JTextField(20);
        txtHoTen = new JTextField(20);
        txtCMND = new JTextField(20);
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtSDT = new JTextField(20);
        txtEmail = new JTextField(20);
        txtQueQuan = new JTextField(20);

        themComponent(pnlInput, gbc, 0, "Mã SV:", txtMaSV);
        themComponent(pnlInput, gbc, 1, "Họ Tên:", txtHoTen);
        themComponent(pnlInput, gbc, 2, "CMND/CCCD:", txtCMND);
        themComponent(pnlInput, gbc, 3, "Giới Tính:", cboGioiTinh);
        themComponent(pnlInput, gbc, 4, "SĐT:", txtSDT);
        themComponent(pnlInput, gbc, 5, "Email:", txtEmail);
        themComponent(pnlInput, gbc, 6, "Quê Quán:", txtQueQuan);

        // --- FIX NÚT BẤM (FlowLayout + Size to) ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlButtons.setOpaque(false);
        
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Mới");
        
        // Set size cố định cho nút
        Dimension btnSize = new Dimension(100, 40);
        btnThem.setPreferredSize(btnSize);
        btnSua.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize);
        btnLamMoi.setPreferredSize(btnSize);
        
        // Màu sắc
        btnThem.setBackground(new Color(46, 204, 113)); btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(241, 196, 15));
        btnXoa.setBackground(new Color(231, 76, 60)); btnXoa.setForeground(Color.WHITE);
        
        // Font chữ
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        btnThem.setFont(btnFont); btnSua.setFont(btnFont); 
        btnXoa.setFont(btnFont); btnLamMoi.setFont(btnFont);

        pnlButtons.add(btnThem); 
        pnlButtons.add(btnSua); 
        pnlButtons.add(btnXoa);
        
        // Dòng nút thao tác
        GridBagConstraints gbcBtns = (GridBagConstraints) gbc.clone();
        gbcBtns.gridx = 0; gbcBtns.gridy = 7; gbcBtns.gridwidth = 2;
        gbcBtns.insets = new Insets(15, 5, 5, 5);
        pnlInput.add(pnlButtons, gbcBtns);
        
        // Nút Mới (riêng 1 dòng hoặc chung tùy ý, ở đây để riêng cho thoáng)
        GridBagConstraints gbcMoi = (GridBagConstraints) gbc.clone();
        gbcMoi.gridy = 8;
        gbcMoi.fill = GridBagConstraints.NONE; // Không giãn
        gbcMoi.anchor = GridBagConstraints.CENTER;
        pnlInput.add(btnLamMoi, gbcMoi);
        
        // Đẩy lên trên
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy = 9; gbcPush.weighty = 1.0;
        pnlInput.add(new JLabel(), gbcPush);

        add(pnlInput, BorderLayout.EAST);

        // --- 3. BẢNG DỮ LIỆU ---
        String[] headers = {"Mã SV", "Họ Tên", "CMND", "Giới Tính", "SĐT", "Email", "Quê Quán"};
        model = new DefaultTableModel(headers, 0);
        tblSinhVien = new JTable(model);
        tblSinhVien.setRowHeight(25);
        tblSinhVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        TableColumnModel colModel = tblSinhVien.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(70); 
        colModel.getColumn(1).setPreferredWidth(130);
        colModel.getColumn(5).setPreferredWidth(130);
        
        add(new JScrollPane(tblSinhVien), BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        loadData(); 

        // --- SỰ KIỆN ---
        tblSinhVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblSinhVien.getSelectedRow();
                if (row >= 0) {
                    txtMaSV.setText(model.getValueAt(row, 0).toString());
                    txtHoTen.setText(model.getValueAt(row, 1).toString());
                    txtCMND.setText(model.getValueAt(row, 2).toString());
                    cboGioiTinh.setSelectedItem(model.getValueAt(row, 3).toString());
                    txtSDT.setText(model.getValueAt(row, 4).toString());
                    txtEmail.setText(model.getValueAt(row, 5).toString());
                    txtQueQuan.setText(model.getValueAt(row, 6).toString());
                }
            }
        });

        btnThem.addActionListener(e -> {
            SinhVien sv = getModel();
            if (dao.themSV(sv)) {
                JOptionPane.showMessageDialog(null, "Thêm thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(null, "Lỗi: Trùng Mã SV hoặc CMND!");
            }
        });

        btnSua.addActionListener(e -> {
             if(dao.suaSV(getModel())) {
                 JOptionPane.showMessageDialog(null, "Sửa thành công!");
                 loadData();
             }
        });

        btnXoa.addActionListener(e -> {
             if(!txtMaSV.getText().isEmpty() && dao.xoaSV(txtMaSV.getText())) {
                 JOptionPane.showMessageDialog(null, "Xóa thành công!");
                 loadData();
                 clearForm();
             }
        });

        btnLamMoi.addActionListener(e -> clearForm());
        
        btnTim.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText();
            if(tuKhoa.isEmpty()) {
                loadData(); 
            } else {
                 List<SinhVien> list = dao.timKiem(tuKhoa); 
                 doDuLieuVaoBang(list);
            }
        });
        
        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            loadData();
        });
    }

    private void phanQuyen(NhanVien user) {
        if (user.getQuyen().equalsIgnoreCase("NhanVien") || user.getQuyen().equalsIgnoreCase("BaoVe")) {
            btnXoa.setEnabled(false);
            btnXoa.setBackground(Color.LIGHT_GRAY);
            btnXoa.setToolTipText("Bạn không có quyền xóa!");
        }
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

    private SinhVien getModel() {
        return new SinhVien(
            txtMaSV.getText(), txtHoTen.getText(), txtCMND.getText(),
            cboGioiTinh.getSelectedItem().toString(), txtSDT.getText(),
            txtEmail.getText(), txtQueQuan.getText()
        );
    }

    private void clearForm() {
        txtMaSV.setText(""); txtHoTen.setText(""); txtCMND.setText("");
        txtSDT.setText(""); txtEmail.setText(""); txtQueQuan.setText("");
        txtMaSV.requestFocus();
    }

    public void loadData() {
        List<SinhVien> list = dao.getAllSinhVien();
        doDuLieuVaoBang(list);
    }
    
    private void doDuLieuVaoBang(List<SinhVien> list) {
        model.setRowCount(0);
        for (SinhVien sv : list) {
            model.addRow(new Object[]{
                sv.getMaSV(), sv.getHoTen(), sv.getCmnd(), 
                sv.getGioiTinh(), sv.getSdt(), sv.getEmail(), sv.getQueQuan()
            });
        }
    }
}