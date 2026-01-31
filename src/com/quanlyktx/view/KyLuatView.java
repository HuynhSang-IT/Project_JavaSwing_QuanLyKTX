package com.quanlyktx.view;

import com.quanlyktx.dao.KyLuatDAO;
import com.quanlyktx.dao.SinhVienDAO;
import com.quanlyktx.model.KyLuat;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.SinhVien;
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

public class KyLuatView extends JPanel {
    
    private JComboBox<String> cboSinhVien; 
    private JTextArea txtNoiDung;
    private JComboBox<String> cboHinhThuc;
    private JTextField txtNgay, txtTienPhat, txtTimKiem;
    
    private JTable tblData;
    private DefaultTableModel model;
    
    private JButton btnThem, btnSua, btnXoa, btnMoi;
    
    private KyLuatDAO dao = new KyLuatDAO();
    private SinhVienDAO svDao = new SinhVienDAO();
    
    private int maKLDangChon = -1;

    public KyLuatView(NhanVien user) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ KỶ LUẬT & VI PHẠM", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(192, 57, 43)); 
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        // Tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tìm SV");
        pnlSearch.add(new JLabel("Tìm kiếm:")); pnlSearch.add(txtTimKiem); pnlSearch.add(btnTim);
        pnlHeader.add(pnlSearch, BorderLayout.EAST);
        
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER: CHIA 2 CỘT ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // 1. INPUT FORM
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin vi phạm"));
        pnlInput.setBackground(new Color(250, 250, 250));
        pnlInput.setPreferredSize(new Dimension(450, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        cboSinhVien = new JComboBox<>();
        
        // --- CHỈNH LẠI NGÀY THÁNG SANG YYYY-MM-DD ---
        txtNgay = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 20);
        // ----------------------------------------------
        
        txtNoiDung = new JTextArea(8, 20); 
        txtNoiDung.setLineWrap(true);
        txtNoiDung.setWrapStyleWord(true);
        JScrollPane scrollNoiDung = new JScrollPane(txtNoiDung);
        
        cboHinhThuc = new JComboBox<>(new String[]{"Nhắc nhở", "Cảnh cáo", "Khiển trách", "Phạt tiền", "Đuổi khỏi KTX"});
        txtTienPhat = new JTextField("0", 20);
        txtTienPhat.setEditable(false); // Mặc định khóa
        txtTienPhat.setBackground(new Color(230, 230, 230));

        themComponent(pnlInput, gbc, 0, "Sinh viên:", cboSinhVien);
        themComponent(pnlInput, gbc, 1, "Ngày vi phạm:", txtNgay);
        
        // Label Nội dung
        GridBagConstraints gbcLblND = (GridBagConstraints) gbc.clone();
        gbcLblND.gridx=0; gbcLblND.gridy=2; 
        gbcLblND.anchor = GridBagConstraints.NORTHWEST; 
        gbcLblND.insets = new Insets(8, 5, 8, 5);
        pnlInput.add(new JLabel("Nội dung:"), gbcLblND);
        
        // Scroll Nội dung
        GridBagConstraints gbcScroll = (GridBagConstraints) gbc.clone();
        gbcScroll.gridx=1; gbcScroll.gridy=2; 
        pnlInput.add(scrollNoiDung, gbcScroll);
        
        // Separator
        JSeparator sep = new JSeparator();
        GridBagConstraints gbcSep = (GridBagConstraints) gbc.clone();
        gbcSep.gridx=0; gbcSep.gridy=3; gbcSep.gridwidth=2; 
        gbcSep.insets = new Insets(10, 5, 10, 5);
        pnlInput.add(sep, gbcSep);
        
        themComponent(pnlInput, gbc, 4, "Hình thức xử lý:", cboHinhThuc);
        themComponent(pnlInput, gbc, 5, "Số tiền phạt:", txtTienPhat);
        
        // NÚT BẤM
        btnThem = createBtn("GHI VI PHẠM", new Color(231, 76, 60));
        btnThem.setPreferredSize(new Dimension(0, 45)); 
        
        btnSua = createBtn("Cập nhật", new Color(243, 156, 18));
        btnSua.setPreferredSize(new Dimension(0, 40));
        
        btnXoa = createBtn("Xóa", new Color(149, 165, 166));
        btnXoa.setPreferredSize(new Dimension(0, 35));
        
        btnMoi = createBtn("Làm mới", new Color(52, 152, 219));
        btnMoi.setPreferredSize(new Dimension(0, 35));

        // Space
        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy=6; gbcSpace.gridwidth=2; 
        pnlInput.add(new JLabel(" "), gbcSpace);
        
        GridBagConstraints gbcBtnThem = (GridBagConstraints) gbc.clone();
        gbcBtnThem.gridy=7; gbcBtnThem.gridwidth=2;
        pnlInput.add(btnThem, gbcBtnThem);
        
        GridBagConstraints gbcBtnSua = (GridBagConstraints) gbc.clone();
        gbcBtnSua.gridy=8; gbcBtnSua.gridwidth=2;
        pnlInput.add(btnSua, gbcBtnSua);
        
        JPanel pnlBtn = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnMoi); pnlBtn.add(btnXoa);
        
        GridBagConstraints gbcPnlBtn = (GridBagConstraints) gbc.clone();
        gbcPnlBtn.gridy=9; gbcPnlBtn.gridwidth=2;
        pnlInput.add(pnlBtn, gbcPnlBtn);
        
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy=10; gbcPush.weighty=1.0; 
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // 2. TABLE
        String[] headers = {"Mã", "Mã SV", "Họ Tên", "Nội Dung", "Hình Thức", "Ngày", "Tiền Phạt"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblData.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblData.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        // --- LOGIC ---
        loadComboBoxSV();
        loadTable();
        
        // [LOGIC KHÓA Ô TIỀN]
        cboHinhThuc.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) checkHinhThuc();
        });

        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    maKLDangChon = Integer.parseInt(model.getValueAt(r, 0).toString());
                    cboSinhVien.setSelectedItem(model.getValueAt(r, 1).toString()); 
                    txtNoiDung.setText(model.getValueAt(r, 3).toString());
                    cboHinhThuc.setSelectedItem(model.getValueAt(r, 4).toString());
                    txtNgay.setText(model.getValueAt(r, 5).toString());
                    txtTienPhat.setText(model.getValueAt(r, 6).toString().replace(",", "").replace(" đ", ""));
                    
                    checkHinhThuc(); 
                }
            }
        });

        btnThem.addActionListener(e -> {
            try {
                KyLuat kl = new KyLuat();
                kl.setMaSV(cboSinhVien.getSelectedItem().toString());
                kl.setNoiDung(txtNoiDung.getText());
                kl.setHinhThuc(cboHinhThuc.getSelectedItem().toString());
                kl.setNgayViPham(txtNgay.getText());
                
                if (!txtTienPhat.isEditable()) kl.setSoTienPhat(0);
                else kl.setSoTienPhat(Double.parseDouble(txtTienPhat.getText()));
                
                if(dao.them(kl)) {
                    JOptionPane.showMessageDialog(null, "Đã ghi nhận vi phạm!");
                    loadTable(); clearForm();
                }
            } catch(Exception ex) { JOptionPane.showMessageDialog(null, "Lỗi nhập liệu!"); }
        });
        
        btnSua.addActionListener(e -> {
            if(maKLDangChon == -1) return;
            try {
                KyLuat kl = new KyLuat();
                kl.setMaKL(maKLDangChon);
                kl.setNoiDung(txtNoiDung.getText());
                kl.setHinhThuc(cboHinhThuc.getSelectedItem().toString());
                kl.setNgayViPham(txtNgay.getText());
                
                if (!txtTienPhat.isEditable()) kl.setSoTienPhat(0);
                else kl.setSoTienPhat(Double.parseDouble(txtTienPhat.getText()));
                
                if(dao.capNhat(kl)) { JOptionPane.showMessageDialog(null, "Cập nhật thành công!"); loadTable(); clearForm(); }
            } catch(Exception ex) {}
        });
        
        btnXoa.addActionListener(e -> {
            if(maKLDangChon != -1 && JOptionPane.showConfirmDialog(null, "Xóa kỷ luật này?") == JOptionPane.YES_OPTION) {
                dao.xoa(maKLDangChon); loadTable(); clearForm();
            }
        });
        
        btnTim.addActionListener(e -> {
            String k = txtTimKiem.getText();
            if(k.isEmpty()) loadTable();
            else doDuLieuVaoBang(dao.timKiem(k));
        });
        
        btnMoi.addActionListener(e -> clearForm());
    }
    
    private void checkHinhThuc() {
        String hinhThuc = cboHinhThuc.getSelectedItem().toString();
        if (hinhThuc.equals("Phạt tiền")) {
            txtTienPhat.setEditable(true);
            txtTienPhat.setBackground(Color.WHITE);
            txtTienPhat.requestFocus();
        } else {
            txtTienPhat.setEditable(false);
            txtTienPhat.setText("0"); 
            txtTienPhat.setBackground(new Color(230, 230, 230)); 
        }
    }

    private JButton createBtn(String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnXoa.setEnabled(false);
            btnXoa.setBackground(Color.LIGHT_GRAY);
            btnXoa.setToolTipText("Chỉ Admin mới có quyền xóa biên bản!");
        }
    }

    private void loadComboBoxSV() {
        cboSinhVien.removeAllItems();
        List<SinhVien> list = svDao.getAllSinhVien(); 
        for(SinhVien sv : list) cboSinhVien.addItem(sv.getMaSV());
    }
    
    public void loadTable() { doDuLieuVaoBang(dao.getAll()); }
    
    private void doDuLieuVaoBang(List<KyLuat> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for(KyLuat kl : list) {
            model.addRow(new Object[]{
                kl.getMaKL(), kl.getMaSV(), kl.getTenSV(), 
                kl.getNoiDung(), kl.getHinhThuc(), kl.getNgayViPham(), 
                df.format(kl.getSoTienPhat())
            });
        }
    }
    
    private void clearForm() {
        maKLDangChon = -1;
        txtNoiDung.setText(""); txtTienPhat.setText("0");
        cboHinhThuc.setSelectedIndex(0); 
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