package com.quanlyktx.view;

import com.quanlyktx.dao.PhongDAO;
import com.quanlyktx.dao.SuCoDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Phong;
import com.quanlyktx.model.SuCo;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SuCoView extends JPanel {
    
    // Input Form
    private JComboBox<Tang> cboTangForm; 
    private JComboBox<String> cboPhong;
    private JTextField txtNguoiBao, txtNgayBao, txtChiPhi;
    private JTextArea txtMoTa;
    private JComboBox<String> cboTrangThai;
    
    // Search Bar
    private JComboBox<Tang> cboTangSearch; 
    private JTextField txtTimKiem;
    
    private JTable tblData;
    private DefaultTableModel model;
    
    // Nút bấm
    private JButton btnBaoCao, btnCapNhat, btnXoa, btnMoi;
    
    private SuCoDAO dao = new SuCoDAO();
    private PhongDAO phongDAO = new PhongDAO();
    private TangDAO tangDAO = new TangDAO();
    
    private List<Phong> listAllPhong; 
    private int maSuCoDangChon = -1; 

    public SuCoView(NhanVien user) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ SỰ CỐ & SỬA CHỮA KTX", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(231, 76, 60)); 
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        // PANEL TÌM KIẾM
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        
        pnlSearch.add(new JLabel("Khu Vực:"));
        cboTangSearch = new JComboBox<>();
        pnlSearch.add(cboTangSearch);
        
        pnlSearch.add(new JLabel("| Tìm:"));
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Lọc / Tìm");
        JButton btnHuy = new JButton("Hủy");
        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTim); pnlSearch.add(btnHuy);
        
        pnlHeader.add(pnlSearch, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // 1. INPUT FORM
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin báo hỏng"));
        pnlInput.setBackground(new Color(250, 250, 250));
        pnlInput.setPreferredSize(new Dimension(480, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        cboTangForm = new JComboBox<>(); 
        cboPhong = new JComboBox<>();
        
        txtNguoiBao = new JTextField(20);
        
        // --- [FIX QUAN TRỌNG] Đổi định dạng ngày thành yyyy-MM-dd để DB hiểu ---
        txtNgayBao = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20);
        txtNgayBao.setToolTipText("Định dạng: Năm-Tháng-Ngày (VD: 2026-01-27)");
        // -----------------------------------------------------------------------
        
        txtMoTa = new JTextArea(8, 20); 
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true); 
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        
        cboTrangThai = new JComboBox<>(new String[]{"Chưa xử lý", "Đang sửa", "Đã xong"});
        txtChiPhi = new JTextField("0", 20);

        themComponent(pnlInput, gbc, 0, "Chọn Tầng:", cboTangForm);
        themComponent(pnlInput, gbc, 1, "Chọn Phòng:", cboPhong);
        themComponent(pnlInput, gbc, 2, "Người báo:", txtNguoiBao);
        themComponent(pnlInput, gbc, 3, "Ngày báo:", txtNgayBao);
        
        // Label Mô tả
        GridBagConstraints gbcLblMoTa = (GridBagConstraints) gbc.clone();
        gbcLblMoTa.gridx=0; gbcLblMoTa.gridy=4; 
        gbcLblMoTa.anchor = GridBagConstraints.NORTHWEST; 
        gbcLblMoTa.insets = new Insets(8, 5, 8, 5);
        pnlInput.add(new JLabel("Mô tả hư hỏng:"), gbcLblMoTa);
        
        // Scroll Mô tả
        GridBagConstraints gbcScroll = (GridBagConstraints) gbc.clone();
        gbcScroll.gridx=1; gbcScroll.gridy=4; 
        pnlInput.add(scrollMoTa, gbcScroll);
        
        // Separator
        JSeparator sep = new JSeparator();
        GridBagConstraints gbcSep = (GridBagConstraints) gbc.clone();
        gbcSep.gridx=0; gbcSep.gridy=5; gbcSep.gridwidth=2; 
        gbcSep.insets = new Insets(10, 5, 10, 5);
        pnlInput.add(sep, gbcSep);
        
        themComponent(pnlInput, gbc, 6, "Trạng thái:", cboTrangThai);
        themComponent(pnlInput, gbc, 7, "Chi phí (VNĐ):", txtChiPhi);
        
        // NÚT BẤM
        btnBaoCao = createBtn("GỬI BÁO CÁO", new Color(231, 76, 60));
        btnBaoCao.setPreferredSize(new Dimension(150, 40));
        
        btnCapNhat = createBtn("CẬP NHẬT", new Color(46, 204, 113));
        btnCapNhat.setPreferredSize(new Dimension(120, 40));
        
        btnXoa = createBtn("Xóa", new Color(149, 165, 166));
        btnMoi = createBtn("Mới", new Color(52, 152, 219));

        // Layout nút
        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy=8; gbcSpace.gridwidth=2; 
        pnlInput.add(new JLabel(" "), gbcSpace);
        
        JPanel pnlRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlRow1.setOpaque(false);
        pnlRow1.add(btnBaoCao); pnlRow1.add(btnCapNhat);
        
        JPanel pnlRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlRow2.setOpaque(false);
        pnlRow2.add(btnMoi); pnlRow2.add(btnXoa);
        
        GridBagConstraints gbcR1 = (GridBagConstraints) gbc.clone();
        gbcR1.gridy=9; gbcR1.gridwidth=2;
        pnlInput.add(pnlRow1, gbcR1);
        
        GridBagConstraints gbcR2 = (GridBagConstraints) gbc.clone();
        gbcR2.gridy=10; gbcR2.gridwidth=2;
        pnlInput.add(pnlRow2, gbcR2);
        
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy=11; gbcPush.weighty=1.0;
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // 2. TABLE
        String[] headers = {"Mã", "Phòng", "Mô Tả Sự Cố", "Người Báo", "Ngày", "Trạng Thái", "Chi Phí"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblData.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        phanQuyen(user);
        loadDataInit();
        loadTable();
        
        cboTangSearch.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) thucHienLoc();
        });

        cboTangForm.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                Tang t = (Tang) cboTangForm.getSelectedItem();
                if(t != null) loadPhongTheoTang(t.getMaTang());
            }
        });

        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    maSuCoDangChon = Integer.parseInt(model.getValueAt(r, 0).toString());
                    String tenPhong = model.getValueAt(r, 1).toString();
                    
                    chonPhongTrenForm(tenPhong);
                    
                    txtMoTa.setText(model.getValueAt(r, 2).toString());
                    txtNguoiBao.setText(model.getValueAt(r, 3).toString());
                    txtNgayBao.setText(model.getValueAt(r, 4).toString());
                    cboTrangThai.setSelectedItem(model.getValueAt(r, 5).toString());
                    txtChiPhi.setText(model.getValueAt(r, 6).toString().replace(",", "").replace(" đ", ""));
                }
            }
        });

        btnTim.addActionListener(e -> thucHienLoc());
        btnHuy.addActionListener(e -> { txtTimKiem.setText(""); cboTangSearch.setSelectedIndex(0); loadTable(); });

        // --- NÚT GỬI BÁO CÁO ---
        btnBaoCao.addActionListener(e -> {
            try {
                if(cboPhong.getSelectedItem() == null) { 
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng!"); return; 
                }
                if(txtMoTa.getText().trim().isEmpty()) { 
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập mô tả hư hỏng!"); return; 
                }
                
                SuCo sc = new SuCo();
                sc.setMaPhong(cboPhong.getSelectedItem().toString());
                sc.setNguoiBao(txtNguoiBao.getText());
                sc.setMoTa(txtMoTa.getText());
                sc.setNgayBao(txtNgayBao.getText());
                
                // Mặc định khi gửi mới
                sc.setTrangThai("Chưa xử lý");
                sc.setChiPhi(0); 
                
                if(dao.them(sc)) { 
                    JOptionPane.showMessageDialog(null, "Gửi báo cáo thành công!\n(Trạng thái: Chưa xử lý - 0đ)"); 
                    thucHienLoc(); 
                    clearForm(); 
                } else {
                    JOptionPane.showMessageDialog(null, "Gửi thất bại! Kiểm tra lại ngày tháng (yyyy-MM-dd) hoặc kết nối mạng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi hệ thống: " + ex.getMessage());
            }
        });
        
        // --- NÚT CẬP NHẬT ---
        btnCapNhat.addActionListener(e -> {
            if(maSuCoDangChon == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn sự cố để cập nhật!"); return;
            }
            try {
                SuCo sc = new SuCo();
                sc.setMaSuCo(maSuCoDangChon);
                sc.setTrangThai(cboTrangThai.getSelectedItem().toString());
                sc.setChiPhi(Double.parseDouble(txtChiPhi.getText().replace(",", "").replace(" đ", "")));
                sc.setMoTa(txtMoTa.getText()); 
                
                if(dao.capNhat(sc)) { 
                    JOptionPane.showMessageDialog(null, "Cập nhật thành công!"); 
                    thucHienLoc(); 
                    clearForm(); 
                }
            } catch(Exception ex) { JOptionPane.showMessageDialog(null, "Lỗi nhập liệu chi phí!"); }
        });
        
        btnXoa.addActionListener(e -> {
            if(maSuCoDangChon != -1 && JOptionPane.showConfirmDialog(null, "Xóa sự cố này?") == JOptionPane.YES_OPTION) {
                dao.xoa(maSuCoDangChon); thucHienLoc(); clearForm();
            }
        });
        
        btnMoi.addActionListener(e -> clearForm());
    }

    private JButton createBtn(String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnXoa.setEnabled(false); btnXoa.setBackground(Color.LIGHT_GRAY);
        }
    }

    public void loadDataInit() {
        listAllPhong = phongDAO.getAllPhong();
        cboTangSearch.removeAllItems(); cboTangSearch.addItem(new Tang(-1, "Tất cả khu vực", ""));
        cboTangForm.removeAllItems();
        List<Tang> listTang = tangDAO.getAll();
        for(Tang t : listTang) { cboTangSearch.addItem(t); cboTangForm.addItem(t); }
        if(cboTangForm.getItemCount() > 0) {
            cboTangForm.setSelectedIndex(0);
            loadPhongTheoTang(((Tang)cboTangForm.getItemAt(0)).getMaTang());
        }
    }
    
    public void loadTable() { doDuLieuVaoBang(dao.getAll()); }

    private void loadPhongTheoTang(int maTang) {
        cboPhong.removeAllItems();
        for(Phong p : listAllPhong) if(p.getMaTang() == maTang) cboPhong.addItem(p.getMaPhong());
    }
    
    private void chonPhongTrenForm(String tenPhong) {
        for(Phong p : listAllPhong) {
            if(p.getMaPhong().equals(tenPhong)) {
                for(int i=0; i<cboTangForm.getItemCount(); i++) {
                    if(cboTangForm.getItemAt(i).getMaTang() == p.getMaTang()) {
                        cboTangForm.setSelectedIndex(i); break;
                    }
                }
                cboPhong.setSelectedItem(p.getMaPhong()); break;
            }
        }
    }
    
    private void thucHienLoc() {
        String tk = txtTimKiem.getText();
        Tang t = (Tang) cboTangSearch.getSelectedItem();
        int maTang = (t != null) ? t.getMaTang() : -1;
        doDuLieuVaoBang(dao.timKiemNangCao(tk, maTang));
    }
    
    private void doDuLieuVaoBang(List<SuCo> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for(SuCo sc : list) {
            model.addRow(new Object[]{
                sc.getMaSuCo(), sc.getMaPhong(), sc.getMoTa(), sc.getNguoiBao(), sc.getNgayBao(),
                sc.getTrangThai(), df.format(sc.getChiPhi())
            });
        }
    }
    
    private void clearForm() { 
        maSuCoDangChon = -1; 
        txtMoTa.setText(""); txtNguoiBao.setText(""); 
        txtChiPhi.setText("0"); cboTrangThai.setSelectedIndex(0); 
        tblData.clearSelection(); 
    }

    private void themComponent(JPanel p, GridBagConstraints gbc, int row, String label, JComponent comp) {
        GridBagConstraints c1 = (GridBagConstraints) gbc.clone();
        c1.gridx = 0; c1.gridy = row; c1.gridwidth = 1; c1.weightx = 0.0;
        p.add(new JLabel(label), c1);
        GridBagConstraints c2 = (GridBagConstraints) gbc.clone();
        c2.gridx = 1; c2.gridy = row; c2.weightx = 1.0; 
        p.add(comp, c2);
    }
}