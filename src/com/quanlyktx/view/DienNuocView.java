package com.quanlyktx.view;

import com.quanlyktx.dao.DienNuocDAO;
import com.quanlyktx.dao.PhongDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.DienNuoc;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Phong;
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

public class DienNuocView extends JPanel {
    
    private JComboBox<Tang> cboTang;
    private JComboBox<Phong> cboPhong;
    
    private JTextField txtThang, txtDienCu, txtDienMoi, txtNuocCu, txtNuocMoi;
    private JTextField txtGiaDien, txtGiaNuoc;
    
    private JTextField txtTimKiem;
    
    private int maHDDangChon = -1; 
    
    private JTable tblData;
    private DefaultTableModel model;
    
    // Nút bấm
    private JButton btnLuu, btnSua, btnXoa, btnLamMoi;
    
    private DienNuocDAO dao = new DienNuocDAO();
    private PhongDAO phDao = new PhongDAO();
    private TangDAO tangDao = new TangDAO(); 
    
    private List<Phong> listAllPhong; 

    public DienNuocView(NhanVien user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- TOP: HEADER & TÌM KIẾM ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("GHI CHỈ SỐ & TÍNH ĐIỆN NƯỚC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 153, 102));
        pnlTop.add(lblTitle, BorderLayout.WEST);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.add(new JLabel("Tìm (Phòng/Tháng):"));
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tìm");
        JButton btnHuy = new JButton("Hủy");
        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTim); pnlSearch.add(btnHuy);
        
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);

        // --- RIGHT: FORM NHẬP LIỆU ---
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Nhập chỉ số"));
        
        // --- SỬA 1: Tăng chiều rộng Panel lên 450 ---
        pnlInput.setPreferredSize(new Dimension(450, 0)); 
        pnlInput.setBackground(new Color(250, 250, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Tăng khoảng cách dòng chút cho thoáng
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        cboTang = new JComboBox<>();
        cboPhong = new JComboBox<>();
        
        // Renderer để hiện tên phòng đẹp
        cboPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Phong) {
                    setText(((Phong)value).getTenPhong());
                }
                return this;
            }
        });

        // --- SỬA 2: Tăng kích thước các ô nhập liệu (15 cột) ---
        txtThang = new JTextField(new SimpleDateFormat("MM/yyyy").format(new Date()), 15);
        txtThang.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        txtDienCu = new JTextField("0", 15); txtDienCu.setEditable(false); txtDienCu.setBackground(Color.LIGHT_GRAY);
        txtDienMoi = new JTextField("", 15);
        txtGiaDien = new JTextField("3500", 15);
        
        txtNuocCu = new JTextField("0", 15); txtNuocCu.setEditable(false); txtNuocCu.setBackground(Color.LIGHT_GRAY);
        txtNuocMoi = new JTextField("", 15);
        txtGiaNuoc = new JTextField("6000", 15);
        
        themComponent(pnlInput, gbc, 0, "Chọn Tầng:", cboTang);
        themComponent(pnlInput, gbc, 1, "Chọn Phòng:", cboPhong);
        themComponent(pnlInput, gbc, 2, "Tháng:", txtThang);
        
        // --- SEPARATOR 1 ---
        JSeparator s1 = new JSeparator(); 
        GridBagConstraints gbcS1 = (GridBagConstraints) gbc.clone();
        gbcS1.gridx=0; gbcS1.gridy=3; gbcS1.gridwidth=2; 
        gbcS1.insets = new Insets(15, 5, 15, 5); // Cách xa chút
        pnlInput.add(s1, gbcS1);
        
        themComponent(pnlInput, gbc, 4, "Điện Cũ:", txtDienCu);
        themComponent(pnlInput, gbc, 5, "Điện Mới:", txtDienMoi);
        themComponent(pnlInput, gbc, 6, "Giá Điện:", txtGiaDien);
        
        // --- SEPARATOR 2 ---
        JSeparator s2 = new JSeparator(); 
        GridBagConstraints gbcS2 = (GridBagConstraints) gbc.clone();
        gbcS2.gridx=0; gbcS2.gridy=7; gbcS2.gridwidth=2; 
        gbcS2.insets = new Insets(15, 5, 15, 5);
        pnlInput.add(s2, gbcS2);
        
        themComponent(pnlInput, gbc, 8, "Nước Cũ:", txtNuocCu);
        themComponent(pnlInput, gbc, 9, "Nước Mới:", txtNuocMoi);
        themComponent(pnlInput, gbc, 10, "Giá Nước:", txtGiaNuoc);
        
        // --- SỬA 3: Cấu hình Nút bấm to đẹp ---
        btnLuu = new JButton("THÊM MỚI");
        btnLuu.setBackground(new Color(46, 204, 113)); btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setPreferredSize(new Dimension(0, 40)); // Cao 40px
        
        btnSua = new JButton("CẬP NHẬT (SỬA)");
        btnSua.setBackground(new Color(241, 196, 15)); 
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSua.setPreferredSize(new Dimension(0, 40));
        
        btnXoa = new JButton("Xóa dòng");
        btnXoa.setBackground(new Color(231, 76, 60)); btnXoa.setForeground(Color.WHITE);
        btnXoa.setPreferredSize(new Dimension(0, 35));
        
        btnLamMoi = new JButton("Làm mới form"); 
        btnLamMoi.setPreferredSize(new Dimension(0, 35));

        // --- ADD BUTTONS ---
        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy = 11; gbcSpace.gridwidth = 2; 
        pnlInput.add(new JLabel(" "), gbcSpace);
        
        GridBagConstraints gbcBtnLuu = (GridBagConstraints) gbc.clone();
        gbcBtnLuu.gridy = 12; gbcBtnLuu.gridwidth = 2; // Chiếm hết bề ngang
        pnlInput.add(btnLuu, gbcBtnLuu);
        
        GridBagConstraints gbcBtnSua = (GridBagConstraints) gbc.clone();
        gbcBtnSua.gridy = 13; gbcBtnSua.gridwidth = 2;
        pnlInput.add(btnSua, gbcBtnSua);
        
        JPanel pnlBot = new JPanel(new GridLayout(1, 2, 10, 0)); // Cách nhau 10px
        pnlBot.setOpaque(false);
        pnlBot.add(btnLamMoi); pnlBot.add(btnXoa);
        
        GridBagConstraints gbcBot = (GridBagConstraints) gbc.clone();
        gbcBot.gridy = 14; gbcBot.gridwidth = 2;
        pnlInput.add(pnlBot, gbcBot);
        
        // Đẩy form lên trên cùng
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy = 15; gbcPush.weighty = 1.0;
        pnlInput.add(new JLabel(), gbcPush);
        
        add(pnlInput, BorderLayout.EAST);

        // --- CENTER: BẢNG DỮ LIỆU ---
        String[] headers = {"Mã", "Phòng", "Tháng", "Số Điện", "Tiền Điện", "Số Nước", "Tiền Nước", "Tổng Đ+N", "TT"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30); // Dòng bảng cao hơn chút
        tblData.getColumnModel().getColumn(0).setPreferredWidth(40);
        add(new JScrollPane(tblData), BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);
        // ------------------

        // --- LOGIC XỬ LÝ ---
        listAllPhong = phDao.getAllPhong(); 
        loadComboBoxTang();
        loadTable();
        
        cboTang.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                Tang t = (Tang) cboTang.getSelectedItem();
                loadPhongTheoTang(t.getMaTang());
            }
        });

        cboPhong.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) capNhatChiSoCu();
        });

        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    maHDDangChon = Integer.parseInt(model.getValueAt(r, 0).toString());
                    String tenPhong = model.getValueAt(r, 1).toString();
                    chonPhongTrenComboBox(tenPhong);
                    
                    txtThang.setText(model.getValueAt(r, 2).toString());
                    int soDienTieuThu = Integer.parseInt(model.getValueAt(r, 3).toString());
                    capNhatChiSoCu();
                    
                    int soDienCu = Integer.parseInt(txtDienCu.getText());
                    txtDienMoi.setText(String.valueOf(soDienCu + soDienTieuThu));
                    
                    int soNuocTieuThu = Integer.parseInt(model.getValueAt(r, 5).toString());
                    int soNuocCu = Integer.parseInt(txtNuocCu.getText());
                    txtNuocMoi.setText(String.valueOf(soNuocCu + soNuocTieuThu));
                }
            }
        });

        btnLuu.addActionListener(e -> {
            try {
                if(cboPhong.getSelectedItem() == null) return;
                Phong p = (Phong) cboPhong.getSelectedItem();
                String thang = txtThang.getText();
                
                if(dao.checkTonTai(p.getMaPhong(), thang)) {
                    JOptionPane.showMessageDialog(null, "LỖI: Phòng này tháng " + thang + " đã nhập rồi!");
                    return;
                }
                DienNuoc dn = layDuLieuTuForm(); 
                if(dn != null && dao.them(dn)) {
                    JOptionPane.showMessageDialog(null, "Thêm thành công!");
                    loadTable(); clearForm();
                }
            } catch(Exception ex) { ex.printStackTrace(); }
        });
        
        btnSua.addActionListener(e -> {
            if(maHDDangChon == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng để sửa!"); return;
            }
            DienNuoc dn = layDuLieuTuForm();
            if(dn != null) {
                dn.setMaHD(maHDDangChon);
                if(dao.sua(dn)) {
                    JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                    loadTable(); clearForm();
                }
            }
        });
        
        btnXoa.addActionListener(e -> {
            int r = tblData.getSelectedRow();
            if(r>=0 && JOptionPane.showConfirmDialog(null, "Xóa dòng này?") == JOptionPane.YES_OPTION) {
                dao.xoa(Integer.parseInt(model.getValueAt(r, 0).toString())); loadTable(); clearForm();
            }
        });
        
        btnTim.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText();
            if(!tuKhoa.isEmpty()) {
                List<DienNuoc> listKetQua = dao.timKiem(tuKhoa);
                doDuLieuVaoBang(listKetQua);
            } else {
                loadTable();
            }
        });

        btnHuy.addActionListener(e -> {
            txtTimKiem.setText("");
            loadTable();
        });
        
        btnLamMoi.addActionListener(e -> clearForm());
    }
    
    // --- HÀM PHÂN QUYỀN ---
    private void phanQuyen(NhanVien user) {
        // Nếu không phải Admin thì khóa nút Xóa và sửa Giá
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnXoa.setEnabled(false);
            btnXoa.setBackground(Color.LIGHT_GRAY);
            
            txtGiaDien.setEditable(false);
            txtGiaNuoc.setEditable(false);
            txtGiaDien.setToolTipText("Liên hệ Admin để chỉnh giá");
        }
    }
    
    public void loadComboBoxTang() {
        cboTang.removeAllItems();
        List<Tang> listTang = tangDao.getAll();
        for(Tang t : listTang) cboTang.addItem(t);
        
        if(cboTang.getItemCount() > 0) {
            cboTang.setSelectedIndex(0);
            loadPhongTheoTang(((Tang)cboTang.getItemAt(0)).getMaTang());
        }
    }
    
    private void loadPhongTheoTang(int maTang) {
        cboPhong.removeAllItems();
        for(Phong p : listAllPhong) {
            if(p.getMaTang() == maTang) {
                cboPhong.addItem(p);
            }
        }
        if(cboPhong.getItemCount() > 0) {
            cboPhong.setSelectedIndex(0);
            capNhatChiSoCu();
        } else {
            txtDienCu.setText("0"); txtNuocCu.setText("0");
        }
    }

    private DienNuoc layDuLieuTuForm() {
        try {
            DienNuoc dn = new DienNuoc();
            Phong p = (Phong) cboPhong.getSelectedItem();
            dn.setMaPhong(p.getMaPhong());
            dn.setThangNam(txtThang.getText());
            int dCu = Integer.parseInt(txtDienCu.getText()); int dMoi = Integer.parseInt(txtDienMoi.getText());
            int nCu = Integer.parseInt(txtNuocCu.getText()); int nMoi = Integer.parseInt(txtNuocMoi.getText());
            long giaDien = Long.parseLong(txtGiaDien.getText().replace(",","")); 
            long giaNuoc = Long.parseLong(txtGiaNuoc.getText().replace(",",""));
            if (dMoi < dCu || nMoi < nCu) { JOptionPane.showMessageDialog(null, "Chỉ số MỚI phải >= chỉ số CŨ!"); return null; }
            dn.setSoDienCu(dCu); dn.setSoDienMoi(dMoi); dn.setSoNuocCu(nCu); dn.setSoNuocMoi(nMoi);
            double tienDien = (dMoi - dCu) * giaDien; double tienNuoc = (nMoi - nCu) * giaNuoc;
            dn.setTienDien(tienDien); dn.setTienNuoc(tienNuoc); dn.setThanhTien(tienDien + tienNuoc);
            return dn;
        } catch(Exception e) { JOptionPane.showMessageDialog(null, "Vui lòng nhập số hợp lệ!"); return null; }
    }

    private void capNhatChiSoCu() {
        if (cboPhong.getSelectedItem() != null) {
            Phong p = (Phong) cboPhong.getSelectedItem();
            DienNuoc last = dao.getChiSoCuoiCung(p.getMaPhong());
            if(last != null) { txtDienCu.setText(String.valueOf(last.getSoDienMoi())); txtNuocCu.setText(String.valueOf(last.getSoNuocMoi())); } 
            else { txtDienCu.setText("0"); txtNuocCu.setText("0"); }
            if(maHDDangChon == -1) { txtDienMoi.setText(""); txtNuocMoi.setText(""); }
            txtDienMoi.requestFocus();
        }
    }
    
    private void chonPhongTrenComboBox(String tenPhong) {
        for(Phong p : listAllPhong) {
            if(p.getTenPhong().equals(tenPhong)) {
                for(int i=0; i<cboTang.getItemCount(); i++) {
                    Tang t = (Tang) cboTang.getItemAt(i);
                    if(t.getMaTang() == p.getMaTang()) {
                        cboTang.setSelectedIndex(i);
                        break;
                    }
                }
                for(int j=0; j<cboPhong.getItemCount(); j++) {
                    Phong item = (Phong) cboPhong.getItemAt(j);
                    if(item.getMaPhong().equals(p.getMaPhong())) {
                        cboPhong.setSelectedIndex(j);
                        break;
                    }
                }
                break;
            }
        }
    }
    
    private void clearForm() {
        maHDDangChon = -1; txtDienMoi.setText(""); txtNuocMoi.setText(""); tblData.clearSelection(); capNhatChiSoCu();
    }
    
    public void loadComboBox() {
        listAllPhong = phDao.getAllPhong();
        loadComboBoxTang();
    }
    
    public void loadTable() { doDuLieuVaoBang(dao.getAll()); }
    
    private void doDuLieuVaoBang(List<DienNuoc> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        for(DienNuoc dn : list) {
            model.addRow(new Object[]{ 
                dn.getMaHD(), dn.getTenPhong(), dn.getThangNam(), 
                (dn.getSoDienMoi()-dn.getSoDienCu()), df.format(dn.getTienDien()), 
                (dn.getSoNuocMoi()-dn.getSoNuocCu()), df.format(dn.getTienNuoc()), 
                df.format(dn.getThanhTien()), dn.getTrangThai() 
            });
        }
    }

    // Hàm hỗ trợ thêm component vào Panel (WindowBuilder Friendly)
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