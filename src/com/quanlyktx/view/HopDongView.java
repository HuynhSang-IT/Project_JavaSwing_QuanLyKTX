package com.quanlyktx.view;

import com.quanlyktx.dao.HopDongDAO;
import com.quanlyktx.dao.PhongDAO;
import com.quanlyktx.dao.SinhVienDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.HopDong;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Phong;
import com.quanlyktx.model.SinhVien;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HopDongView extends JPanel {
    private JComboBox<String> cboSinhVien;
    
    private JComboBox<Tang> cboTang;
    private JComboBox<Phong> cboPhong; 
    
    private JTextField txtNgayBD, txtNgayKT, txtTimKiem; 
    
    private JTable tblHopDong;
    private DefaultTableModel model;
    
    private JButton btnLapHD;
    
    private HopDongDAO hdDao = new HopDongDAO();
    private SinhVienDAO svDao = new SinhVienDAO();
    private PhongDAO phDao = new PhongDAO();
    private TangDAO tangDao = new TangDAO();

    private List<SinhVien> listSV;
    private List<Tang> listAllTang; // Lưu toàn bộ tầng để lọc

    public HopDongView(NhanVien user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 1. TIÊU ĐỀ & TÌM KIẾM
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlTop.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("QUẢN LÝ HỢP ĐỒNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTop.add(lblTitle, BorderLayout.WEST);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.add(new JLabel("Tìm (Tên SV/Phòng):"));
        
        txtTimKiem = new JTextField(15);
        JButton btnTim = new JButton("Tìm");
        JButton btnHuy = new JButton("Hủy");
        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTim); pnlSearch.add(btnHuy);

        pnlTop.add(pnlSearch, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);
        
        // Sự kiện Tìm kiếm
        btnTim.addActionListener(e -> {
            String tk = txtTimKiem.getText();
            if (!tk.isEmpty()) {
                List<HopDong> list = hdDao.timKiemHopDong(tk); 
                model.setRowCount(0);
                for (HopDong hd : list) {
                    model.addRow(new Object[]{ hd.getMaHopDong(), hd.getTenSV(), hd.getTenPhong(), hd.getNgayBatDau(), hd.getNgayKetThuc() });
                }
            } else loadTable();
        });

        btnHuy.addActionListener(e -> { txtTimKiem.setText(""); loadTable(); });
        
        // 2. FORM ĐĂNG KÝ
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Đăng ký phòng"));
        pnlInput.setPreferredSize(new Dimension(450, 0)); // Rộng 450px
        pnlInput.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboSinhVien = new JComboBox<>();
        cboTang = new JComboBox<>();  
        cboPhong = new JComboBox<>(); 
        
        // Renderer cho Phòng (Tô đỏ nếu đầy)
        cboPhong.setRenderer(new DefaultListCellRenderer() {
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
        
        txtNgayBD = new JTextField("2026-01-01", 15);
        txtNgayKT = new JTextField("2026-06-01", 15);

        themComponent(pnlInput, gbc, 0, "Sinh Viên:", cboSinhVien);
        
        JSeparator sep = new JSeparator(); 
        GridBagConstraints gbcSep = (GridBagConstraints) gbc.clone();
        gbcSep.gridx=0; gbcSep.gridy=1; gbcSep.gridwidth=2; 
        pnlInput.add(sep, gbcSep);
        
        themComponent(pnlInput, gbc, 2, "Chọn Tầng:", cboTang); 
        themComponent(pnlInput, gbc, 3, "Chọn Phòng:", cboPhong); 
        
        themComponent(pnlInput, gbc, 4, "Ngày Bắt Đầu:", txtNgayBD);
        themComponent(pnlInput, gbc, 5, "Ngày Kết Thúc:", txtNgayKT);

        btnLapHD = new JButton("Lập Hợp Đồng");
        btnLapHD.setBackground(new Color(46, 204, 113));
        btnLapHD.setForeground(Color.WHITE);
        btnLapHD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLapHD.setPreferredSize(new Dimension(0, 45)); // Nút to
        
        GridBagConstraints gbcBtn = (GridBagConstraints) gbc.clone();
        gbcBtn.gridx = 0; gbcBtn.gridy = 6; gbcBtn.gridwidth = 2;
        pnlInput.add(btnLapHD, gbcBtn);
        
        JLabel lblHD = new JLabel("<html><small>Hệ thống tự động lọc Tầng theo giới tính:<br>- Nam: Tầng Lẻ (1, 3, 5...)<br>- Nữ: Tầng Chẵn (2, 4, 6...)</small></html>");
        lblHD.setForeground(new Color(39, 174, 96)); // Màu xanh thông báo
        GridBagConstraints gbcLbl = (GridBagConstraints) gbc.clone();
        gbcLbl.gridy = 7;
        pnlInput.add(lblHD, gbcLbl);

        add(pnlInput, BorderLayout.EAST);

        // 3. BẢNG DANH SÁCH
        String[] headers = {"Mã HĐ", "Tên Sinh Viên", "Tên Phòng", "Ngày Vào", "Ngày Ra"};
        model = new DefaultTableModel(headers, 0);
        tblHopDong = new JTable(model);
        tblHopDong.setRowHeight(25);
        add(new JScrollPane(tblHopDong), BorderLayout.CENTER);

        phanQuyen(user);
        
        // --- LOGIC ---
        // 1. Load dữ liệu ban đầu
        listSV = svDao.getAllSinhVien();
        listAllTang = tangDao.getAll();
        
        // 2. Đổ dữ liệu vào ComboBox Sinh Viên
        cboSinhVien.removeAllItems();
        for (SinhVien sv : listSV) {
            cboSinhVien.addItem(sv.getMaSV() + " - " + sv.getHoTen());
        }
        
        // 3. Sự kiện: Khi chọn Sinh viên -> Lọc lại Tầng theo giới tính
        cboSinhVien.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                locTangTheoGioiTinh();
            }
        });
        
        // 4. Sự kiện: Khi chọn Tầng -> Load Phòng
        cboTang.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                Tang t = (Tang) cboTang.getSelectedItem();
                loadPhongTheoTang(t.getMaTang());
            }
        });

        // Kích hoạt lọc lần đầu
        if(cboSinhVien.getItemCount() > 0) {
            cboSinhVien.setSelectedIndex(0);
            locTangTheoGioiTinh();
        }

        loadTable();

     // --- SỰ KIỆN NÚT LẬP HỢP ĐỒNG (CÓ RÀNG BUỘC NAM/NỮ + CHỐNG TRÙNG LẶP) ---
        btnLapHD.addActionListener(e -> {
            try {
                // 1. Kiểm tra nhập liệu
                if (cboSinhVien.getSelectedItem() == null || cboPhong.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin!"); return;
                }

                String maSV = cboSinhVien.getSelectedItem().toString().split(" - ")[0];
                SinhVien svChon = null;
                for(SinhVien sv : listSV) {
                    if(sv.getMaSV().equals(maSV)) { svChon = sv; break; }
                }
                
                // 2. Kiểm tra Logic Tầng (Nam Lẻ - Nữ Chẵn)
                Tang t = (Tang) cboTang.getSelectedItem();
                String tenTang = t.getTenTang(); 
                try {
                    int soTang = Integer.parseInt(tenTang.replaceAll("[^0-9]", ""));
                    boolean isTangLe = (soTang % 2 != 0);
                    boolean isNam = svChon.getGioiTinh().equalsIgnoreCase("Nam");

                    if (isNam && !isTangLe) {
                        JOptionPane.showMessageDialog(null, "LỖI: Sinh viên NAM chỉ được ở Tầng LẺ!", "Sai quy định", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (!isNam && isTangLe) {
                        JOptionPane.showMessageDialog(null, "LỖI: Sinh viên NỮ chỉ được ở Tầng CHẴN!", "Sai quy định", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch(Exception ex) {}

                // 3. Kiểm tra Phòng đầy
                Phong p = (Phong) cboPhong.getSelectedItem();
                String maPhong = p.getMaPhong();
                if(p.getSoNguoiO() >= p.getSoLuongMax()) {
                    JOptionPane.showMessageDialog(null, "Phòng này đã ĐẦY!"); return;
                }

                Date d1 = Date.valueOf(txtNgayBD.getText());
                Date d2 = Date.valueOf(txtNgayKT.getText());

                // --- [MỚI] 4. KIỂM TRA TRÙNG HỢP ĐỒNG ---
                if (hdDao.checkTonTai(maSV, maPhong, d1)) {
                    JOptionPane.showMessageDialog(null, 
                        "LỖI: Sinh viên này đã có hợp đồng tại phòng " + maPhong + " vào ngày " + d1 + " rồi!", 
                        "Trùng dữ liệu", JOptionPane.ERROR_MESSAGE);
                    return; // Dừng lại, không lưu nữa
                }
                // ----------------------------------------

                HopDong hd = new HopDong();
                hd.setMaSV(maSV);
                hd.setMaPhong(maPhong);
                hd.setNgayBatDau(d1);
                hd.setNgayKetThuc(d2);

                if (hdDao.themHopDong(hd)) {
                    JOptionPane.showMessageDialog(null, "Đăng ký thành công!");
                    loadTable();
                    
                    // Load lại phòng để cập nhật sĩ số
                    Tang tangHienTai = (Tang) cboTang.getSelectedItem();
                    if(tangHienTai != null) loadPhongTheoTang(tangHienTai.getMaTang());
                } else {
                    JOptionPane.showMessageDialog(null, "Thất bại! Có lỗi xảy ra hoặc SV đã có phòng.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi dữ liệu ngày tháng (yyyy-MM-dd)!");
                ex.printStackTrace();
            }
        });
    }

    // --- HÀM LỌC TẦNG THEO GIỚI TÍNH ---
    private void locTangTheoGioiTinh() {
        if(cboSinhVien.getSelectedItem() == null) return;
        
        // 1. Tìm Sinh viên đang chọn
        String maSV = cboSinhVien.getSelectedItem().toString().split(" - ")[0];
        SinhVien svChon = null;
        for(SinhVien sv : listSV) {
            if(sv.getMaSV().equals(maSV)) { svChon = sv; break; }
        }
        
        if(svChon == null) return;
        
        // 2. Xác định giới tính
        boolean isNam = svChon.getGioiTinh().equalsIgnoreCase("Nam");
        
        // 3. Lọc danh sách Tầng
        cboTang.removeAllItems();
        for(Tang t : listAllTang) {
            try {
                // Lấy số tầng từ tên (VD: "Tầng 1" -> 1)
                int soTang = Integer.parseInt(t.getTenTang().replaceAll("[^0-9]", ""));
                
                if (isNam) {
                    // Nam chỉ add Tầng Lẻ (1, 3, 5...)
                    if (soTang % 2 != 0) cboTang.addItem(t);
                } else {
                    // Nữ chỉ add Tầng Chẵn (2, 4, 6...)
                    if (soTang % 2 == 0) cboTang.addItem(t);
                }
            } catch (Exception ex) {
                // Nếu tên tầng không có số (VD: "Khu A"), cứ add vào cho chắc
                cboTang.addItem(t);
            }
        }
        
        // 4. Chọn tầng đầu tiên sau khi lọc
        if(cboTang.getItemCount() > 0) {
            cboTang.setSelectedIndex(0);
        } else {
            cboPhong.removeAllItems(); // Không có tầng phù hợp -> Xóa ds phòng
        }
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnLapHD.setEnabled(false);
            btnLapHD.setBackground(Color.LIGHT_GRAY);
            btnLapHD.setToolTipText("Chỉ Admin mới có quyền lập hợp đồng!");
        }
    }
    
    public void loadDataComboBox() {
        // Hàm này gọi khi cần refresh dữ liệu từ bên ngoài
        listSV = svDao.getAllSinhVien();
        listAllTang = tangDao.getAll();
        
        cboSinhVien.removeAllItems();
        for (SinhVien sv : listSV) {
            cboSinhVien.addItem(sv.getMaSV() + " - " + sv.getHoTen());
        }
        // Gọi lại lọc để update list tầng
        if(cboSinhVien.getItemCount() > 0) {
            cboSinhVien.setSelectedIndex(0);
            locTangTheoGioiTinh();
        }
    }
    
    private void loadPhongTheoTang(int maTang) {
        cboPhong.removeAllItems();
        List<Phong> allPhong = phDao.getAllPhong();
        for(Phong p : allPhong) {
            if(p.getMaTang() == maTang) { 
                cboPhong.addItem(p);
            }
        }
    }

    public void loadTable() {
        model.setRowCount(0);
        List<HopDong> list = hdDao.getAllHopDong();
        for (HopDong hd : list) {
            model.addRow(new Object[]{
                hd.getMaHopDong(), hd.getTenSV(), hd.getTenPhong(), hd.getNgayBatDau(), hd.getNgayKetThuc()
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