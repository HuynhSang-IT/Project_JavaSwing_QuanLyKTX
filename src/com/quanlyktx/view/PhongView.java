package com.quanlyktx.view;

import com.quanlyktx.dao.PhongDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Phong;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

public class PhongView extends JPanel {
    
    private JTextField txtMaPhong, txtTenPhong, txtGia, txtSoLuongMax;
    private JComboBox<String> cboLoaiPhong;
    private JComboBox<Tang> cboTang; 
    private JTextField txtTimKiem;
    private JTable tblData;
    private DefaultTableModel model;
    
    private JButton btnThem, btnSua, btnXoa, btnMoi;
    
    private PhongDAO dao = new PhongDAO();
    private TangDAO tangDao = new TangDAO();

    public PhongView(NhanVien user) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("QUẢN LÝ PHÒNG KTX", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tìm");
        JButton btnHuy = new JButton("Hủy");
        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTim); pnlSearch.add(btnHuy);
        pnlHeader.add(pnlSearch, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
        pnlInput.setBackground(new Color(250, 250, 250));
        
        // Tăng chiều rộng Panel lên 450px
        pnlInput.setPreferredSize(new Dimension(450, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tăng chiều dài ô nhập
        txtMaPhong = new JTextField(20);
        txtMaPhong.setBackground(new Color(255, 255, 224));
        txtTenPhong = new JTextField(20);
        
        txtTenPhong.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { genMa(); }
            public void removeUpdate(DocumentEvent e) { genMa(); }
            public void changedUpdate(DocumentEvent e) { genMa(); }
            private void genMa() {
                String val = txtTenPhong.getText().replaceAll("[^0-9]", ""); 
                if(!val.isEmpty()) txtMaPhong.setText(val);
            }
        });

        cboLoaiPhong = new JComboBox<>(new String[]{"Thường", "Dịch vụ", "VIP"});
        cboTang = new JComboBox<>();
        txtSoLuongMax = new JTextField("8", 20);
        txtGia = new JTextField("500000", 20); 

        themComponent(pnlInput, gbc, 0, "Tên Phòng (VD: 101):", txtTenPhong);
        themComponent(pnlInput, gbc, 1, "Mã Phòng (Tự động):", txtMaPhong);
        themComponent(pnlInput, gbc, 2, "Loại Phòng:", cboLoaiPhong);
        themComponent(pnlInput, gbc, 3, "Tầng / Khu:", cboTang);
        themComponent(pnlInput, gbc, 4, "Số Người Max:", txtSoLuongMax);
        themComponent(pnlInput, gbc, 5, "Giá Phòng:", txtGia);
        
        // --- NÚT BẤM TO RÕ ---
        btnThem = new JButton("THÊM"); btnThem.setBackground(new Color(46, 204, 113)); btnThem.setForeground(Color.WHITE);
        btnSua = new JButton("SỬA"); btnSua.setBackground(new Color(52, 152, 219)); btnSua.setForeground(Color.WHITE);
        btnXoa = new JButton("XÓA"); btnXoa.setBackground(new Color(231, 76, 60)); btnXoa.setForeground(Color.WHITE);
        btnMoi = new JButton("LÀM MỚI");
        
        Dimension btnSize = new Dimension(180, 45); // Kích thước nút lớn
        btnThem.setPreferredSize(btnSize); btnSua.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize); btnMoi.setPreferredSize(btnSize);
        
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 14));

        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy=6; gbcSpace.gridwidth=2; 
        pnlInput.add(new JLabel(" "), gbcSpace); 
        
        // Dùng GridLayout 2x2 cho 4 nút là đẹp nhất với không gian này
        JPanel pnlBtn = new JPanel(new GridLayout(2, 2, 10, 10)); 
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnThem); pnlBtn.add(btnSua); 
        pnlBtn.add(btnXoa);  pnlBtn.add(btnMoi);
        
        GridBagConstraints gbcBtn = (GridBagConstraints) gbc.clone();
        gbcBtn.gridy=7; 
        pnlInput.add(pnlBtn, gbcBtn);
        
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy=8; gbcPush.weighty=1.0; 
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // TABLE
        String[] headers = {"Mã", "Tên Phòng", "Loại", "Tầng", "Số Người", "Max", "Giá"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30); 
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // PHÂN QUYỀN
        phanQuyen(user);

        // LOGIC KHỞI TẠO
        loadComboBoxTang(); 
        loadTable();
        
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    txtMaPhong.setText(model.getValueAt(r, 0).toString());
                    txtTenPhong.setText(model.getValueAt(r, 1).toString());
                    cboLoaiPhong.setSelectedItem(model.getValueAt(r, 2).toString());
                    chonItemCboTang(model.getValueAt(r, 3).toString());
                    txtSoLuongMax.setText(model.getValueAt(r, 5).toString());
                    txtGia.setText(model.getValueAt(r, 6).toString().replace(",", "").replace(" đ", ""));
                }
            }
        });

        btnTim.addActionListener(e -> {
            String kw = txtTimKiem.getText();
            if(!kw.isEmpty()) doDuLieuVaoBang(dao.timKiemPhong(kw)); else loadTable();
        });
        
        btnHuy.addActionListener(e -> { txtTimKiem.setText(""); loadTable(); });

        btnThem.addActionListener(e -> {
            try {
                if(txtTenPhong.getText().isEmpty()) { JOptionPane.showMessageDialog(null, "Chưa nhập tên phòng!"); return; }
                if(cboTang.getSelectedItem() == null) { JOptionPane.showMessageDialog(null, "Chưa chọn Tầng!"); return; }

                Phong p = layDuLieuForm();
                
                if(dao.themPhong(p)) {
                    JOptionPane.showMessageDialog(null, "Thêm thành công!"); 
                    loadTable(); 
                    clearForm();
                } else {
                    String debugInfo = "Mã: " + p.getMaPhong() + "\nTên: " + p.getTenPhong() + 
                                       "\nID Tầng: " + p.getMaTang() + "\nGiá: " + p.getGiaPhong();
                    JOptionPane.showMessageDialog(null, "Thêm thất bại! Vui lòng kiểm tra:\n1. Mã phòng có bị trùng?\n2. CSDL có cho phép mã chữ không?\n\nThông tin gửi đi:\n" + debugInfo);
                }
            } catch(Exception ex) { 
                ex.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "Lỗi Code: " + ex.getMessage()); 
            }
        });
        
        btnSua.addActionListener(e -> {
            try {
                if(dao.suaPhong(layDuLieuForm())) { JOptionPane.showMessageDialog(null, "Sửa thành công!"); loadTable(); clearForm(); }
            } catch(Exception ex) {}
        });
        
        btnXoa.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null, "Xóa phòng này?") == JOptionPane.YES_OPTION) {
                dao.xoaPhong(txtMaPhong.getText()); loadTable(); clearForm();
            }
        });
        
        btnMoi.addActionListener(e -> { loadComboBoxTang(); clearForm(); });
    }

    public void loadComboBoxTang() {
        cboTang.removeAllItems();
        List<Tang> list = tangDao.getAll();
        for(Tang t : list) cboTang.addItem(t);
        if(cboTang.getItemCount() > 0) cboTang.setSelectedIndex(0);
    }
    
    private Phong layDuLieuForm() {
        Phong p = new Phong();
        p.setMaPhong(txtMaPhong.getText().trim());
        p.setTenPhong(txtTenPhong.getText().trim());
        p.setLoaiPhong(cboLoaiPhong.getSelectedItem().toString());
        
        String max = txtSoLuongMax.getText().trim();
        p.setSoLuongMax(max.isEmpty() ? 8 : Integer.parseInt(max));
        
        String gia = txtGia.getText().trim();
        p.setGiaPhong(gia.isEmpty() ? 0 : Double.parseDouble(gia));
        
        p.setSoNguoiO(0); 
        
        Tang t = (Tang) cboTang.getSelectedItem();
        if(t != null) p.setMaTang(t.getMaTang()); 
        
        return p;
    }
    
    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnXoa.setEnabled(false); btnXoa.setBackground(Color.LIGHT_GRAY);
        }
    }
    
    private void chonItemCboTang(String tenTang) {
        for(int i=0; i<cboTang.getItemCount(); i++) {
            Tang t = cboTang.getItemAt(i);
            if(t.getTenTang().equals(tenTang)) { cboTang.setSelectedIndex(i); return; }
        }
    }
    
    public void loadTable() { doDuLieuVaoBang(dao.getAllPhong()); }
    
    private void doDuLieuVaoBang(List<Phong> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for(Phong p : list) {
            model.addRow(new Object[]{
                p.getMaPhong(), p.getTenPhong(), p.getLoaiPhong(), 
                p.getTenTang(), p.getSoNguoiO(), p.getSoLuongMax(), 
                df.format(p.getGiaPhong())
            });
        }
    }
    
    private void clearForm() {
        txtTenPhong.setText(""); txtMaPhong.setText(""); txtSoLuongMax.setText("8"); txtGia.setText("500000"); tblData.clearSelection();
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