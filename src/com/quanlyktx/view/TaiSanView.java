package com.quanlyktx.view;

import com.quanlyktx.dao.PhongDAO;
import com.quanlyktx.dao.TaiSanDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Phong;
import com.quanlyktx.model.TaiSan;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TaiSanView extends JPanel {
    
    // Form Inputs
    private JComboBox<Tang> cboTangForm; 
    private JComboBox<String> cboPhong;
    private JTextField txtTenTS, txtSoLuong, txtGhiChu;
    private JComboBox<String> cboTinhTrang;
    
    // Search Inputs
    private JComboBox<Tang> cboTangSearch; 
    private JTextField txtTimKiem;
    
    private JTable tblData;
    private DefaultTableModel model;
    
    // Nút bấm (Đưa ra ngoài để phân quyền)
    private JButton btnThem, btnSua, btnXoa, btnMoi;
    
    private TaiSanDAO dao = new TaiSanDAO();
    private PhongDAO phongDao = new PhongDAO();
    private TangDAO tangDao = new TangDAO();
    
    private List<Phong> listAllPhong; 
    private int maTSDangChon = -1;

    public TaiSanView(NhanVien user) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI SẢN & THIẾT BỊ PHÒNG", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(142, 68, 173)); 
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
        
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Chi tiết tài sản"),
            new EmptyBorder(10, 10, 10, 10) 
        ));
        
        pnlInput.setBackground(new Color(250, 250, 250));
        
        // --- SỬA 1: Tăng chiều rộng Panel lên 450px ---
        pnlInput.setPreferredSize(new Dimension(450, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        cboTangForm = new JComboBox<>();
        cboPhong = new JComboBox<>();
        
        // --- SỬA 2: Tăng chiều dài ô nhập liệu (20 cột) ---
        txtTenTS = new JTextField(20);
        txtSoLuong = new JTextField("1", 20);
        cboTinhTrang = new JComboBox<>(new String[]{"Tốt", "Hỏng nhẹ", "Cần thay thế", "Mất"});
        txtGhiChu = new JTextField(20);

        themComponent(pnlInput, gbc, 0, "Chọn Tầng:", cboTangForm);
        themComponent(pnlInput, gbc, 1, "Chọn Phòng:", cboPhong);
        
        // --- SEPARATOR ---
        JSeparator sep = new JSeparator();
        GridBagConstraints gbcSep = (GridBagConstraints) gbc.clone();
        gbcSep.gridx=0; gbcSep.gridy=2; gbcSep.gridwidth=2; 
        gbcSep.insets = new Insets(15, 5, 15, 5); // Cách xa chút
        pnlInput.add(sep, gbcSep);
        
        themComponent(pnlInput, gbc, 3, "Tên Tài Sản:", txtTenTS);
        themComponent(pnlInput, gbc, 4, "Số Lượng:", txtSoLuong);
        themComponent(pnlInput, gbc, 5, "Tình Trạng:", cboTinhTrang);
        themComponent(pnlInput, gbc, 6, "Ghi Chú:", txtGhiChu);
        
        // --- SỬA 3: Nút bấm to đẹp (Cao 40-45px) ---
        btnThem = new JButton("THÊM MỚI");
        btnThem.setBackground(new Color(46, 204, 113)); btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setPreferredSize(new Dimension(0, 45)); // Nút chính cao hơn
        
        btnSua = new JButton("CẬP NHẬT");
        btnSua.setBackground(new Color(52, 152, 219)); btnSua.setForeground(Color.WHITE); 
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSua.setPreferredSize(new Dimension(0, 45));
        
        btnXoa = new JButton("XÓA");
        btnXoa.setBackground(new Color(231, 76, 60)); btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoa.setPreferredSize(new Dimension(0, 35));
        
        btnMoi = new JButton("LÀM MỚI");
        btnMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMoi.setPreferredSize(new Dimension(0, 35));

        // --- SPACE ---
        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy=7; gbcSpace.gridwidth=2; 
        pnlInput.add(new JLabel(" "), gbcSpace); 
        
        // --- Nút Thêm (Full width) ---
        GridBagConstraints gbcThem = (GridBagConstraints) gbc.clone();
        gbcThem.gridy=8; gbcThem.gridwidth=2;
        pnlInput.add(btnThem, gbcThem);
        
        // --- Nút Sửa (Full width) ---
        GridBagConstraints gbcSua = (GridBagConstraints) gbc.clone();
        gbcSua.gridy=9; gbcSua.gridwidth=2;
        pnlInput.add(btnSua, gbcSua);
        
        // --- Panel 2 nút nhỏ ---
        JPanel pnlBtn = new JPanel(new GridLayout(1, 2, 10, 0)); 
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnMoi); pnlBtn.add(btnXoa); // Đảo thứ tự cho thuận tay
        
        GridBagConstraints gbcBtn = (GridBagConstraints) gbc.clone();
        gbcBtn.gridy=10; gbcBtn.gridwidth=2;
        pnlInput.add(pnlBtn, gbcBtn);
        
        // Đẩy lên trên
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy=11; gbcPush.weighty=1.0; 
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // 2. TABLE
        String[] headers = {"Mã", "Phòng", "Tên Tài Sản", "Số Lượng", "Tình Trạng", "Ghi Chú"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(40);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        // --- LOGIC ---
        loadDataInit();
        loadTableAll(); 
        
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
                    maTSDangChon = Integer.parseInt(model.getValueAt(r, 0).toString());
                    String tenPhong = model.getValueAt(r, 1).toString();
                    
                    chonPhongTrenForm(tenPhong); 
                    
                    txtTenTS.setText(model.getValueAt(r, 2).toString());
                    txtSoLuong.setText(model.getValueAt(r, 3).toString());
                    cboTinhTrang.setSelectedItem(model.getValueAt(r, 4).toString());
                    txtGhiChu.setText(model.getValueAt(r, 5).toString());
                }
            }
        });

        btnTim.addActionListener(e -> thucHienLoc());
        
        btnHuy.addActionListener(e -> { 
            txtTimKiem.setText(""); 
            cboTangSearch.setSelectedIndex(0);
            loadTableAll(); 
        });

        btnThem.addActionListener(e -> {
            try {
                if(txtTenTS.getText().isEmpty()) { JOptionPane.showMessageDialog(null, "Nhập tên tài sản!"); return; }
                TaiSan ts = new TaiSan();
                ts.setMaPhong(cboPhong.getSelectedItem().toString());
                ts.setTenTaiSan(txtTenTS.getText());
                ts.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                ts.setTinhTrang(cboTinhTrang.getSelectedItem().toString());
                ts.setGhiChu(txtGhiChu.getText());
                if(dao.them(ts)) { 
                    JOptionPane.showMessageDialog(null, "Đã thêm tài sản!"); 
                    thucHienLoc(); clearForm(); 
                }
            } catch(Exception ex) { JOptionPane.showMessageDialog(null, "Lỗi nhập liệu!"); }
        });
        
        btnSua.addActionListener(e -> {
            if(maTSDangChon == -1) { JOptionPane.showMessageDialog(null, "Chọn dòng cần sửa!"); return; }
            try {
                TaiSan ts = new TaiSan();
                ts.setMaTS(maTSDangChon);
                ts.setTenTaiSan(txtTenTS.getText());
                ts.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                ts.setTinhTrang(cboTinhTrang.getSelectedItem().toString());
                ts.setGhiChu(txtGhiChu.getText());
                if(dao.sua(ts)) { 
                    JOptionPane.showMessageDialog(null, "Cập nhật thành công!"); 
                    thucHienLoc(); clearForm(); 
                }
            } catch(Exception ex) {}
        });
        
        btnXoa.addActionListener(e -> {
            if(maTSDangChon != -1 && JOptionPane.showConfirmDialog(null, "Xóa tài sản này?") == JOptionPane.YES_OPTION) {
                dao.xoa(maTSDangChon); thucHienLoc(); clearForm();
            }
        });
        
        btnMoi.addActionListener(e -> clearForm());
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnXoa.setEnabled(false);
            btnXoa.setBackground(Color.LIGHT_GRAY);
            btnXoa.setToolTipText("Chỉ Admin mới được xóa tài sản!");
        }
    }

    public void loadDataInit() {
        listAllPhong = phongDao.getAllPhong();
        
        cboTangSearch.removeAllItems();
        cboTangSearch.addItem(new Tang(-1, "Tất cả khu vực", ""));
        
        cboTangForm.removeAllItems();
        List<Tang> listTang = tangDao.getAll();
        for(Tang t : listTang) {
            cboTangSearch.addItem(t);
            cboTangForm.addItem(t);
        }
        
        if(cboTangForm.getItemCount() > 0) {
            cboTangForm.setSelectedIndex(0);
            loadPhongTheoTang(((Tang)cboTangForm.getItemAt(0)).getMaTang());
        }
    }
    
    private void loadPhongTheoTang(int maTang) {
        cboPhong.removeAllItems();
        for(Phong p : listAllPhong) {
            if(p.getMaTang() == maTang) {
                cboPhong.addItem(p.getMaPhong());
            }
        }
    }
    
    private void chonPhongTrenForm(String tenPhong) {
        for(Phong p : listAllPhong) {
            if(p.getMaPhong().equals(tenPhong)) {
                for(int i=0; i<cboTangForm.getItemCount(); i++) {
                    Tang t = cboTangForm.getItemAt(i);
                    if(t.getMaTang() == p.getMaTang()) {
                        cboTangForm.setSelectedIndex(i);
                        break;
                    }
                }
                cboPhong.setSelectedItem(p.getMaPhong());
                break;
            }
        }
    }
    
    public void loadTableAll() {
        doDuLieuVaoBang(dao.timKiemNangCao("", -1));
    }
    
    private void thucHienLoc() {
        String tk = txtTimKiem.getText();
        Tang t = (Tang) cboTangSearch.getSelectedItem();
        int maTang = (t != null) ? t.getMaTang() : -1;
        doDuLieuVaoBang(dao.timKiemNangCao(tk, maTang));
    }
    
    private void doDuLieuVaoBang(List<TaiSan> list) {
        model.setRowCount(0);
        for(TaiSan ts : list) {
            model.addRow(new Object[]{
                ts.getMaTS(), ts.getMaPhong(), ts.getTenTaiSan(), 
                ts.getSoLuong(), ts.getTinhTrang(), ts.getGhiChu()
            });
        }
    }
    
    private void clearForm() { 
        maTSDangChon = -1; 
        txtTenTS.setText(""); txtSoLuong.setText("1"); txtGhiChu.setText(""); 
        tblData.clearSelection(); 
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