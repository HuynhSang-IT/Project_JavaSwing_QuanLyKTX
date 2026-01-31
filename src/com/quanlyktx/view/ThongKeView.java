package com.quanlyktx.view;

import com.quanlyktx.dao.ThongKeDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.DienNuoc;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class ThongKeView extends JPanel {
    
    private ThongKeDAO dao = new ThongKeDAO();
    private TangDAO tangDao = new TangDAO(); 
    
    private JLabel lblDoanhThu, lblCongNo;
    private JProgressBar prgNam, prgNu;
    private JTable tblData;
    private DefaultTableModel model;
    
    // Các component lọc
    private JComboBox<String> cboThang;
    private JTextField txtNam;
    private JComboBox<Tang> cboTang; 
    
    private JButton btnXem, btnXuatExcel;
    
    private List<DienNuoc> listHoaDon; 
    
    // Lưu thông tin người dùng để in vào báo cáo
    private NhanVien currentUser;

    public ThongKeView(NhanVien user) {
        this.currentUser = user; // Lưu user lại
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. HEADER & BỘ LỌC ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("THỐNG KÊ & BÁO CÁO TỔNG HỢP", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlHeader.add(lblTitle, BorderLayout.NORTH);
        
        // Thanh tìm kiếm
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); 
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        pnlFilter.add(new JLabel("Tháng:"));
        cboThang = new JComboBox<>();
        cboThang.addItem("Tất cả");
        for(int i=1; i<=12; i++) cboThang.addItem(String.format("%02d", i));
        pnlFilter.add(cboThang);
        
        pnlFilter.add(new JLabel("Năm:"));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        txtNam = new JTextField(String.valueOf(currentYear), 5);
        pnlFilter.add(txtNam);
        
        // --- THÊM BỘ LỌC TẦNG ---
        pnlFilter.add(new JLabel("|  Khu Vực:"));
        cboTang = new JComboBox<>();
        loadCboTang(); 
        pnlFilter.add(cboTang);
        
        btnXem = new JButton("Xem Báo Cáo");
        btnXem.setBackground(new Color(0, 102, 204)); btnXem.setForeground(Color.WHITE);
        pnlFilter.add(btnXem);
        
        JButton btnReset = new JButton("Hiện Tất Cả");
        pnlFilter.add(btnReset);
        
        // Nút Xuất Excel
        btnXuatExcel = new JButton("Xuất Báo Cáo (Excel)");
        btnXuatExcel.setBackground(new Color(39, 174, 96)); // Màu xanh lá
        btnXuatExcel.setForeground(Color.WHITE);
        pnlFilter.add(btnXuatExcel);
        
        pnlHeader.add(pnlFilter, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. CENTER: THÔNG TIN CHUNG ---
        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 10, 10)); 
        pnlCenter.setBackground(Color.WHITE);

        // A. Panel Tài Chính
        JPanel pnlTaiChinh = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlTaiChinh.setBackground(Color.WHITE);
        
        lblDoanhThu = createStatLabel("0 đ", new Color(46, 204, 113));
        lblCongNo = createStatLabel("0 Hóa đơn", new Color(231, 76, 60));
        
        JPanel cardCongNo = createCard("HÓA ĐƠN CHƯA THU (Click để lọc)", lblCongNo);
        cardCongNo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardCongNo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                locDuLieu(null, "Chưa thu", -1); 
                JOptionPane.showMessageDialog(null, "Đang hiển thị danh sách các hóa đơn CHƯA THANH TOÁN.");
            }
        });
        
        pnlTaiChinh.add(createCard("TỔNG DOANH THU (Đang hiển thị)", lblDoanhThu));
        pnlTaiChinh.add(cardCongNo);
        
        // B. Panel Biểu Đồ Giới Tính (Fix Design tại đây)
        JPanel pnlGioiTinh = new JPanel(new GridBagLayout());
        pnlGioiTinh.setBackground(Color.WHITE);
        pnlGioiTinh.setBorder(BorderFactory.createTitledBorder("Tỷ lệ giới tính Sinh viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 10, 5, 10); gbc.weightx = 1.0;
        
        prgNam = new JProgressBar(0, 100); prgNam.setStringPainted(true);
        prgNam.setForeground(new Color(52, 152, 219)); prgNam.setPreferredSize(new Dimension(0, 30));
        
        prgNu = new JProgressBar(0, 100); prgNu.setStringPainted(true);
        prgNu.setForeground(new Color(236, 64, 122)); prgNu.setPreferredSize(new Dimension(0, 30));

        // --- FIX DESIGN: DÙNG .clone() ---
        GridBagConstraints gbc1 = (GridBagConstraints) gbc.clone();
        gbc1.gridy=0; pnlGioiTinh.add(new JLabel("Nam:"), gbc1);
        
        GridBagConstraints gbc2 = (GridBagConstraints) gbc.clone();
        gbc2.gridy=1; pnlGioiTinh.add(prgNam, gbc2);
        
        GridBagConstraints gbc3 = (GridBagConstraints) gbc.clone();
        gbc3.gridy=2; pnlGioiTinh.add(new JLabel("Nữ:"), gbc3);
        
        GridBagConstraints gbc4 = (GridBagConstraints) gbc.clone();
        gbc4.gridy=3; pnlGioiTinh.add(prgNu, gbc4);

        pnlCenter.add(pnlTaiChinh);
        pnlCenter.add(pnlGioiTinh);
        add(pnlCenter, BorderLayout.CENTER);

        // --- 3. SOUTH: BẢNG CHI TIẾT ---
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setPreferredSize(new Dimension(0, 300));
        pnlSouth.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));
        
        String[] headers = {"Mã HD", "Phòng (Khu vực)", "Tháng", "Tiền Điện", "Tiền Nước", "Tiền Phòng", "TỔNG CỘNG", "Trạng Thái"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(28);
        tblData.getColumnModel().getColumn(1).setPreferredWidth(150); 
        
        pnlSouth.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlSouth, BorderLayout.SOUTH);
        
        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        // --- EVENTS ---
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if (r >= 0 && listHoaDon != null && r < listHoaDon.size()) {
                    hienThiChiTiet(listHoaDon.get(r));
                }
            }
        });

        btnXem.addActionListener(e -> {
            String thang = cboThang.getSelectedItem().toString();
            String nam = txtNam.getText().trim();
            String thoiGian = thang.equals("Tất cả") ? nam : thang + "/" + nam;
            Tang t = (Tang) cboTang.getSelectedItem();
            int maTang = (t != null) ? t.getMaTang() : -1;
            locDuLieu(thoiGian, null, maTang); 
        });
        
        btnReset.addActionListener(e -> loadData()); 
        
        btnXuatExcel.addActionListener(e -> xuatFileExcelChuyenNghiep());

        loadData(); 
    }
    
    private void xuatFileExcelChuyenNghiep() {
        if (tblData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Báo Cáo Doanh Thu");
        String defaultName = "BaoCao_KTX_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".csv";
        fileChooser.setSelectedFile(new File(defaultName));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToSave))) {
                bw.write("\ufeff"); 
                
                bw.write("BAN QUẢN LÝ KÝ TÚC XÁ ĐẠI HỌC ABC\n");
                bw.write("Địa chỉ: Số 123 - Đường 3/2 - TP. Cần Thơ\n");
                bw.write("Hotline: 1900 1234 - Email: ktx@university.edu.vn\n");
                bw.write("\n"); 
                
                bw.write(",,BÁO CÁO THỐNG KÊ DOANH THU ĐIỆN NƯỚC & PHÒNG\n"); 
                bw.write("Thời gian xuất:," + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
                String nguoiLap = (currentUser != null) ? currentUser.getHoTen() : "Admin";
                bw.write("Người lập báo cáo:," + nguoiLap + "\n");
                
                String dieuKienLoc = "Tháng: " + cboThang.getSelectedItem() + " - Năm: " + txtNam.getText();
                bw.write("Điều kiện lọc:," + dieuKienLoc + "\n");
                bw.write("\n");

                for (int i = 0; i < tblData.getColumnCount(); i++) {
                    bw.write(tblData.getColumnName(i).toUpperCase()); 
                    if (i < tblData.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();

                double tongTienDien = 0;
                double tongTienNuoc = 0;
                double tongTienPhong = 0;
                double tongDoanhThu = 0;
                int daThu = 0;
                int chuaThu = 0;

                for (int i = 0; i < tblData.getRowCount(); i++) {
                    for (int j = 0; j < tblData.getColumnCount(); j++) {
                        Object value = tblData.getValueAt(i, j);
                        String valStr = (value != null) ? value.toString() : "";
                        
                        if (valStr.contains(",")) {
                            valStr = "\"" + valStr + "\"";
                        }
                        bw.write(valStr);
                        if (j < tblData.getColumnCount() - 1) bw.write(",");
                    }
                    bw.newLine();
                    
                    DienNuoc item = listHoaDon.get(i);
                    tongTienDien += item.getTienDien();
                    tongTienNuoc += item.getTienNuoc();
                    tongTienPhong += item.getTienPhong();
                    tongDoanhThu += item.getThanhTien();
                    
                    if(item.getTrangThai().equals("Đã thu")) daThu++;
                    else chuaThu++;
                }
                bw.write("\n");

                DecimalFormat df = new DecimalFormat("#,###");
                
                bw.write("TỔNG KẾT SỐ LIỆU:\n");
                bw.write(",Tổng Tiền Điện:," + "\"" + df.format(tongTienDien) + " đ\"\n");
                bw.write(",Tổng Tiền Nước:," + "\"" + df.format(tongTienNuoc) + " đ\"\n");
                bw.write(",Tổng Tiền Phòng:," + "\"" + df.format(tongTienPhong) + " đ\"\n");
                bw.write(",--------------------\n");
                bw.write(",TỔNG DOANH THU:," + "\"" + df.format(tongDoanhThu) + " đ\"\n");
                bw.write("\n");
                
                bw.write("TÌNH TRẠNG THU:\n");
                bw.write(",Số hóa đơn đã thu:," + daThu + "\n");
                bw.write(",Số hóa đơn nợ:," + chuaThu + "\n");
                
                bw.write("\n");
                bw.write("\n");
                bw.write(",,Người Lập Báo Cáo\n");
                bw.write(",,(Ký và ghi rõ họ tên)\n");
                bw.write("\n");
                bw.write("\n");
                bw.write(",," + nguoiLap + "\n");

                JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công!\n" + fileToSave.getAbsolutePath());
                Desktop.getDesktop().open(fileToSave); 
                
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi ghi file: " + ex.getMessage());
            }
        }
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            lblDoanhThu.setText("*****"); 
            lblDoanhThu.setForeground(Color.GRAY);
            btnXem.setEnabled(false); 
            btnXuatExcel.setEnabled(false); 
            btnXuatExcel.setBackground(Color.GRAY);
            tblData.setVisible(false);
        }
    }
    
    private void loadCboTang() {
        cboTang.removeAllItems();
        cboTang.addItem(new Tang(-1, "Tất cả khu vực", "")); 
        List<Tang> list = tangDao.getAll();
        for(Tang t : list) cboTang.addItem(t);
    }

    public void loadData() {
        cboThang.setSelectedIndex(0);
        cboTang.setSelectedIndex(0); 
        locDuLieu(null, null, -1);
        
        int nam = dao.getSoLuongNam(); int nu = dao.getSoLuongNu(); int tongSV = nam + nu;
        if (tongSV > 0) {
            prgNam.setValue((nam * 100) / tongSV); prgNam.setString(nam + " SV (" + prgNam.getValue() + "%)");
            prgNu.setValue(100 - prgNam.getValue()); prgNu.setString(nu + " SV (" + prgNu.getValue() + "%)");
        }
    }
    
    private void locDuLieu(String thoiGian, String trangThai, int maTang) {
        listHoaDon = dao.timKiemThongKe(thoiGian, trangThai, maTang);
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,### đ");
        double tongDoanhThuHienTai = 0;
        int countChuaThu = 0;
        
        for (DienNuoc dn : listHoaDon) {
            if(dn.getTrangThai().equals("Đã thu")) {
                tongDoanhThuHienTai += dn.getThanhTien(); 
            } else {
                countChuaThu++;
            }
            model.addRow(new Object[]{
                dn.getMaHD(), dn.getTenPhong(), dn.getThangNam(),
                df.format(dn.getTienDien()), df.format(dn.getTienNuoc()), 
                df.format(dn.getTienPhong()), df.format(dn.getThanhTien()),
                dn.getTrangThai()
            });
        }
        
        if (!lblDoanhThu.getText().equals("*****")) {
             lblDoanhThu.setText(df.format(tongDoanhThuHienTai));
        }
        lblCongNo.setText(countChuaThu + " Hóa đơn");
    }

    private void hienThiChiTiet(DienNuoc dn) {
        DecimalFormat df = new DecimalFormat("#,### đ");
        double tong = dn.getTienDien() + dn.getTienNuoc() + dn.getTienPhong();
        String msg = "<html><body style='width: 300px; font-family: Segoe UI; font-size: 13px;'>" +
                "<h2 style='color: #0066cc; text-align: center;'>HÓA ĐƠN #" + dn.getMaHD() + "</h2><hr>" +
                "<b>Phòng:</b> " + dn.getTenPhong() + "<br>" +
                "<b>Tháng:</b> " + dn.getThangNam() + "<br><br>" +
                "<b>1. ĐIỆN:</b> " + df.format(dn.getTienDien()) + "<br>" +
                "<b>2. NƯỚC:</b> " + df.format(dn.getTienNuoc()) + "<br>" +
                "<b>3. PHÒNG:</b> " + df.format(dn.getTienPhong()) + "<br><hr>" +
                "<div style='text-align: right; font-size: 16px;'><b>TỔNG: <span style='color: red;'>" + df.format(tong) + "</span></b></div><br>" +
                "<b>Trạng thái:</b> " + dn.getTrangThai() + "</body></html>";
        JOptionPane.showMessageDialog(this, msg, "Chi tiết", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28)); lbl.setForeground(color);
        return lbl;
    }
    private JPanel createCard(String title, JLabel lblValue) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(250, 250, 250));
        p.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14)); lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        p.add(lblTitle, BorderLayout.NORTH); p.add(lblValue, BorderLayout.CENTER);
        return p;
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