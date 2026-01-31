package com.quanlyktx.view;

import com.quanlyktx.dao.ChuyenPhongDAO;
import com.quanlyktx.dao.PhongDAO;
import com.quanlyktx.dao.SinhVienDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.ChuyenPhong;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Phong;
import com.quanlyktx.model.SinhVien;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class ChuyenPhongView extends JPanel {
    
    private static final long serialVersionUID = 1L;

    private JComboBox<SinhVien> cboSinhVien; 
    
    private JTextField txtPhongCu; 
    private JComboBox<Tang> cboTang;
    private JComboBox<Phong> cboPhongMoi;
    
    private JTextArea txtLyDo;
    private JTable tblData;
    private DefaultTableModel model;
    
    private JButton btnChuyen, btnLamMoi;
    
    private ChuyenPhongDAO dao = new ChuyenPhongDAO();
    private SinhVienDAO svDao = new SinhVienDAO();
    private PhongDAO phongDao = new PhongDAO();
    private TangDAO tangDao = new TangDAO();
    
    private List<Phong> listAllPhong; 
    private NhanVien currentUser;

    public ChuyenPhongView(NhanVien user) {
        this.currentUser = user;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("ĐIỀU CHUYỂN PHÒNG Ở", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(230, 126, 34)); 
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // 1. INPUT FORM
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thực hiện chuyển phòng"));
        pnlInput.setBackground(new Color(250, 250, 250));
        
        // Tăng chiều rộng lên 450px
        pnlInput.setPreferredSize(new Dimension(450, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        cboSinhVien = new JComboBox<>();
        cboSinhVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof SinhVien) {
                    SinhVien sv = (SinhVien) value;
                    setText(sv.getMaSV() + " - " + sv.getHoTen());
                }
                return this;
            }
        });

        txtPhongCu = new JTextField(15); 
        txtPhongCu.setEditable(false); 
        txtPhongCu.setFont(new Font("Arial", Font.BOLD, 14));
        txtPhongCu.setForeground(Color.RED); 
        txtPhongCu.setBackground(new Color(240, 240, 240));
        
        cboTang = new JComboBox<>();
        cboPhongMoi = new JComboBox<>();
        
        cboPhongMoi.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Phong) {
                    Phong p = (Phong) value;
                    setText(p.getTenPhong() + " (" + p.getSoNguoiO() + "/" + p.getSoLuongMax() + ")");
                    if(p.getSoNguoiO() >= p.getSoLuongMax()) setForeground(Color.RED);
                    else setForeground(Color.BLACK);
                }
                return this;
            }
        });
        
        txtLyDo = new JTextArea(3, 15); txtLyDo.setLineWrap(true);
        
        themComponent(pnlInput, gbc, 0, "Sinh Viên:", cboSinhVien);
        themComponent(pnlInput, gbc, 1, "Phòng Hiện Tại:", txtPhongCu);
        
        JSeparator s = new JSeparator(); 
        GridBagConstraints gbcSep = (GridBagConstraints) gbc.clone();
        gbcSep.gridx=0; gbcSep.gridy=2; gbcSep.gridwidth=2; 
        pnlInput.add(s, gbcSep);
        
        themComponent(pnlInput, gbc, 3, "Chuyển Đến Tầng:", cboTang);
        themComponent(pnlInput, gbc, 4, "Chọn Phòng Mới:", cboPhongMoi);
        
        GridBagConstraints gbcLblLyDo = (GridBagConstraints) gbc.clone();
        gbcLblLyDo.gridx=0; gbcLblLyDo.gridy=5; 
        pnlInput.add(new JLabel("Lý Do:"), gbcLblLyDo);
        
        GridBagConstraints gbcTxtLyDo = (GridBagConstraints) gbc.clone();
        gbcTxtLyDo.gridx=1; gbcTxtLyDo.gridy=5; 
        pnlInput.add(new JScrollPane(txtLyDo), gbcTxtLyDo);
        
        // --- NÚT BẤM TO RÕ ---
        btnChuyen = new JButton("XÁC NHẬN CHUYỂN");
        btnChuyen.setBackground(new Color(46, 204, 113)); btnChuyen.setForeground(Color.WHITE);
        btnChuyen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChuyen.setPreferredSize(new Dimension(0, 45)); // Nút to
        
        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLamMoi.setPreferredSize(new Dimension(0, 35));

        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy=6; gbcSpace.gridwidth=2; 
        pnlInput.add(new JLabel(" "), gbcSpace);
        
        GridBagConstraints gbcBtn1 = (GridBagConstraints) gbc.clone();
        gbcBtn1.gridy=7; gbcBtn1.gridwidth=2; 
        pnlInput.add(btnChuyen, gbcBtn1);
        
        GridBagConstraints gbcBtn2 = (GridBagConstraints) gbc.clone();
        gbcBtn2.gridy=8; gbcBtn2.gridwidth=2; 
        pnlInput.add(btnLamMoi, gbcBtn2);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // 2. TABLE LỊCH SỬ
        String[] headers = {"Mã CP", "SV", "Họ Tên", "Từ Phòng", "Đến Phòng", "Lý Do", "Ngày", "Người TH"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(28);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        // --- LOGIC ---
        loadDataInit();
        loadTable();
        
        cboSinhVien.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                hienThiPhongCuCuaSV();
            }
        });
        
        cboTang.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                Tang t = (Tang) cboTang.getSelectedItem();
                if(t != null) loadPhongMoiTheoTang(t.getMaTang());
            }
        });

        btnChuyen.addActionListener(e -> {
            if(cboSinhVien.getSelectedItem() == null || cboPhongMoi.getSelectedItem() == null) return;
            
            SinhVien sv = (SinhVien) cboSinhVien.getSelectedItem();
            String maSV = sv.getMaSV();
            
            String phongCu = txtPhongCu.getText();
            if(phongCu.startsWith("Chưa có")) phongCu = ""; 
            
            if(phongCu.contains("(")) phongCu = phongCu.split("\\(")[0].trim();
            
            Phong pMoi = (Phong) cboPhongMoi.getSelectedItem();
            String phongMoi = pMoi.getTenPhong();
            String maPhongMoi = pMoi.getMaPhong();

            String lyDo = txtLyDo.getText();
            
            if(pMoi.getSoNguoiO() >= pMoi.getSoLuongMax()) {
                JOptionPane.showMessageDialog(null, "Phòng " + phongMoi + " đã ĐẦY!"); return;
            }
            
            if(phongCu.equals(phongMoi)) {
                JOptionPane.showMessageDialog(null, "Phòng mới trùng với phòng cũ!"); return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(null, 
                    "Xác nhận chuyển SV [" + maSV + "]\nTừ: " + (phongCu.isEmpty() ? "Chưa có" : phongCu) + " -> Đến: " + phongMoi + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            
            if(confirm == JOptionPane.YES_OPTION) {
                String nguoiThucHien = (currentUser != null) ? currentUser.getHoTen() : "Admin";
                
                String ketQua = dao.thucHienChuyenPhong(maSV, phongCu, maPhongMoi, lyDo, nguoiThucHien);
                
                if(ketQua.equals("OK")) {
                    JOptionPane.showMessageDialog(null, "Chuyển phòng thành công!");
                    loadDataInit(); 
                    loadTable(); 
                    txtLyDo.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Lỗi: " + ketQua, "Thất bại", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnLamMoi.addActionListener(e -> { loadDataInit(); loadTable(); });
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnChuyen.setEnabled(false);
            btnChuyen.setBackground(Color.GRAY);
        }
    }

    public void loadDataInit() {
        listAllPhong = phongDao.getAllPhong();
        List<SinhVien> listSV = svDao.getAllSinhVien(); 
        
        cboSinhVien.removeAllItems();
        for(SinhVien sv : listSV) cboSinhVien.addItem(sv);
        
        cboTang.removeAllItems();
        List<Tang> listTang = tangDao.getAll();
        for(Tang t : listTang) cboTang.addItem(t);
        
        if(cboTang.getItemCount() > 0) {
            cboTang.setSelectedIndex(0);
            loadPhongMoiTheoTang(((Tang)cboTang.getItemAt(0)).getMaTang());
        }
        
        hienThiPhongCuCuaSV();
    }
    
    private void hienThiPhongCuCuaSV() {
        if(cboSinhVien.getSelectedItem() == null) return;
        
        SinhVien sv = (SinhVien) cboSinhVien.getSelectedItem();
        String maPhongDB = sv.getMaPhong();
        
        if (maPhongDB == null || maPhongDB.isEmpty()) {
            txtPhongCu.setText("Chưa có phòng");
            txtPhongCu.setForeground(Color.RED);
        } else {
            String tenHienThi = maPhongDB;
            for(Phong p : listAllPhong) {
                if(p.getMaPhong().equals(maPhongDB)) {
                    tenHienThi = p.getTenPhong();
                    break;
                }
            }
            txtPhongCu.setText(tenHienThi);
            txtPhongCu.setForeground(new Color(0, 100, 0));
        }
    }
    
    private void loadPhongMoiTheoTang(int maTang) {
        cboPhongMoi.removeAllItems();
        for(Phong p : listAllPhong) { 
            if(p.getMaTang() == maTang) {
                cboPhongMoi.addItem(p);
            }
        }
    }
    
    public void loadTable() {
        model.setRowCount(0);
        List<ChuyenPhong> listCP = dao.getAll();
        for(ChuyenPhong cp : listCP) {
            model.addRow(new Object[]{
                cp.getMaCP(), cp.getMaSV(), cp.getTenSV(), 
                cp.getPhongCu(), cp.getPhongMoi(), cp.getLyDo(), 
                cp.getNgayChuyen(), cp.getNguoiThucHien()
            });
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
}