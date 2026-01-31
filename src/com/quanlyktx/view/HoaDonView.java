package com.quanlyktx.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.FileOutputStream;
import com.quanlyktx.dao.DienNuocDAO;
import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.DienNuoc;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

public class HoaDonView extends JPanel {
    private JTextField txtMaHD, txtTenPhong, txtTienDien, txtTienNuoc, txtTienPhong, txtTongCong;
    private JTextField txtTimKiem;
    
    private JComboBox<Tang> cboTang;
    
    private JTable tblHoaDon;
    private DefaultTableModel model;
    
    private DienNuocDAO dao = new DienNuocDAO();
    private TangDAO tangDao = new TangDAO(); 
    
    private JButton btnThuTien, btnInHoaDon;

    public HoaDonView(NhanVien user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. HEADER & TÌM KIẾM ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("THANH TOÁN & IN HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTop.add(lblTitle, BorderLayout.WEST);
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setBackground(Color.WHITE);
        
        pnlSearch.add(new JLabel("Khu Vực:"));
        cboTang = new JComboBox<>();
        loadCboTang(); 
        pnlSearch.add(cboTang);
        
        pnlSearch.add(new JLabel(" | Tìm:"));
        txtTimKiem = new JTextField(15);
        pnlSearch.add(txtTimKiem);
        
        JButton btnTim = new JButton("Lọc / Tìm");
        JButton btnHuy = new JButton("Hủy");
        pnlSearch.add(btnTim);
        pnlSearch.add(btnHuy);
        
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);

        // --- 2. LEFT: FORM NHẬP LIỆU ---
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin thanh toán"));
        
        // Tăng chiều rộng Panel lên 450px
        pnlInput.setPreferredSize(new Dimension(450, 0));
        pnlInput.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHD = new JTextField(20); txtMaHD.setEditable(false);
        txtTenPhong = new JTextField(20); txtTenPhong.setEditable(false);
        txtTienDien = new JTextField(20); txtTienDien.setEditable(false);
        txtTienNuoc = new JTextField(20); txtTienNuoc.setEditable(false);
        
        txtTienPhong = new JTextField(20); 
        txtTienPhong.setFont(new Font("Arial", Font.BOLD, 14)); 
        txtTienPhong.setForeground(Color.RED);
        
        txtTongCong = new JTextField(20); txtTongCong.setEditable(false);
        txtTongCong.setFont(new Font("Arial", Font.BOLD, 20)); 
        txtTongCong.setForeground(new Color(0, 153, 51)); 
        txtTongCong.setHorizontalAlignment(JTextField.RIGHT);

        themComponent(pnlInput, gbc, 0, "Mã HĐ:", txtMaHD);
        themComponent(pnlInput, gbc, 1, "Phòng:", txtTenPhong);
        themComponent(pnlInput, gbc, 2, "Tiền Điện:", txtTienDien);
        themComponent(pnlInput, gbc, 3, "Tiền Nước:", txtTienNuoc);
        
        JSeparator s = new JSeparator(); 
        GridBagConstraints gbcSep = (GridBagConstraints) gbc.clone();
        gbcSep.gridx=0; gbcSep.gridy=4; gbcSep.gridwidth=2; 
        gbcSep.insets = new Insets(15, 5, 15, 5); 
        pnlInput.add(s, gbcSep);
        
        themComponent(pnlInput, gbc, 5, "Tiền Phòng:", txtTienPhong);
        themComponent(pnlInput, gbc, 6, "TỔNG BILL:", txtTongCong);

        // --- SỬA LẠI KÍCH THƯỚC NÚT BẤM ---
        btnThuTien = new JButton("XÁC NHẬN THU TIỀN");
        btnThuTien.setBackground(new Color(46, 204, 113));
        btnThuTien.setForeground(Color.WHITE);
        btnThuTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThuTien.setFocusPainted(false);
        // Thay đổi từ 0 thành 250 để đảm bảo chiều rộng tối thiểu
        btnThuTien.setPreferredSize(new Dimension(250, 45)); 
        
        btnInHoaDon = new JButton("Xuất Hóa Đơn (PDF)");
        btnInHoaDon.setBackground(new Color(52, 152, 219));
        btnInHoaDon.setForeground(Color.WHITE);
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Thay đổi từ 0 thành 250
        btnInHoaDon.setPreferredSize(new Dimension(250, 40)); 

        // --- ADD BUTTONS ---
        GridBagConstraints gbcSpace = (GridBagConstraints) gbc.clone();
        gbcSpace.gridy = 7; gbcSpace.gridwidth = 2; 
        pnlInput.add(new JLabel(" "), gbcSpace);
        
        GridBagConstraints gbcBtn1 = (GridBagConstraints) gbc.clone();
        gbcBtn1.gridy = 8; 
        pnlInput.add(btnThuTien, gbcBtn1);
        
        GridBagConstraints gbcBtn2 = (GridBagConstraints) gbc.clone();
        gbcBtn2.gridy = 9; 
        gbcBtn2.insets = new Insets(10, 5, 5, 5); 
        pnlInput.add(btnInHoaDon, gbcBtn2);
        
        // Đẩy form lên trên
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy = 10; gbcPush.weighty = 1.0;
        pnlInput.add(new JLabel(), gbcPush);
        
        add(pnlInput, BorderLayout.EAST);

        // --- 3. CENTER: BẢNG ---
        String[] headers = {"Mã", "Phòng", "Tháng", "Tiền Điện", "Tiền Nước", "Tiền Phòng", "TỔNG CỘNG", "Trạng Thái", "Hình Thức"};
        model = new DefaultTableModel(headers, 0);
        tblHoaDon = new JTable(model);
        tblHoaDon.setRowHeight(30);
        tblHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblHoaDon.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        // --- PHÂN QUYỀN ---
        phanQuyen(user);

        // --- 4. LOGIC XỬ LÝ ---
        loadData(); 

        cboTang.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                thucHienLoc();
            }
        });

        btnTim.addActionListener(e -> thucHienLoc());

        btnHuy.addActionListener(e -> {
            txtTimKiem.setText("");
            cboTang.setSelectedIndex(0);
            loadData();
        });

        tblHoaDon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblHoaDon.getSelectedRow();
                if(r >= 0) {
                    try {
                        txtMaHD.setText(model.getValueAt(r, 0).toString());
                        txtTenPhong.setText(model.getValueAt(r, 1).toString());
                        
                        String dienStr = model.getValueAt(r, 3).toString().replaceAll("[^0-9]", "");
                        String nuocStr = model.getValueAt(r, 4).toString().replaceAll("[^0-9]", "");
                        String phongStr = model.getValueAt(r, 5).toString().replaceAll("[^0-9]", "");
                        
                        if(dienStr.isEmpty()) dienStr = "0";
                        if(nuocStr.isEmpty()) nuocStr = "0";
                        if(phongStr.isEmpty()) phongStr = "0";
                        
                        DecimalFormat df = new DecimalFormat("#,###");
                        txtTienDien.setText(df.format(Double.parseDouble(dienStr)));
                        txtTienNuoc.setText(df.format(Double.parseDouble(nuocStr)));
                        
                        double tienPhong = Double.parseDouble(phongStr);
                        if(tienPhong == 0) {
                            try {
                                double giaGoc = dao.getGiaPhongTheoTen(txtTenPhong.getText());
                                if(giaGoc == 0) giaGoc = 500000;
                                txtTienPhong.setText(String.valueOf((long)giaGoc));
                            } catch (Throwable err) { txtTienPhong.setText("500000"); }
                        } else {
                            txtTienPhong.setText(String.valueOf((long)tienPhong));
                        }
                        
                        tinhTongTuDong();

                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            }
        });

        txtTienPhong.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { tinhTongTuDong(); }
        });

        btnThuTien.addActionListener(e -> {
            try {
                if(txtMaHD.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn hóa đơn cần thu!"); return;
                }
                
                int r = tblHoaDon.getSelectedRow();
                if(r >= 0 && model.getValueAt(r, 7).toString().equals("Đã thu")) {
                    JOptionPane.showMessageDialog(null, "Hóa đơn này ĐÃ THU rồi!"); return;
                }

                String[] options = {"Tiền Mặt", "Chuyển Khoản", "Hủy Bỏ"};
                int choice = JOptionPane.showOptionDialog(null, 
                        "Xác nhận thu tiền phòng " + txtTenPhong.getText() + "?\n" +
                        "Tổng cộng: " + txtTongCong.getText() + " VNĐ",
                        "Thanh Toán", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, 
                        null, options, options[0]);

                if(choice == 0 || choice == 1) {
                    int maHD = Integer.parseInt(txtMaHD.getText());
                    double tienPhong = Double.parseDouble(txtTienPhong.getText().replaceAll("[^0-9]", ""));
                    double tongTien = Double.parseDouble(txtTongCong.getText().replace(",", "").replace(".", ""));
                    String hinhThuc = (choice == 0) ? "Tiền mặt" : "Chuyển khoản";
                    
                    if(dao.capNhatHoaDon(maHD, tienPhong, tongTien, "Đã thu", hinhThuc)) {
                        JOptionPane.showMessageDialog(null, "Thanh toán thành công!");
                        thucHienLoc(); 
                        clearForm();
                    }
                }
            } catch(Exception ex) { 
                JOptionPane.showMessageDialog(null, "Lỗi số liệu! Kiểm tra lại."); 
            }
        });
        
        btnInHoaDon.addActionListener(e -> {
            if(txtMaHD.getText().isEmpty()) { 
                JOptionPane.showMessageDialog(null, "Vui lòng chọn hóa đơn để in!"); 
                return; 
            }
            xuatHoaDonPDF();
        });
    }
    
    private void phanQuyen(NhanVien user) {
        if (user.getQuyen().equalsIgnoreCase("BaoVe")) {
            btnThuTien.setEnabled(false);
            btnThuTien.setBackground(Color.GRAY);
            btnThuTien.setToolTipText("Bảo vệ không có quyền thu tiền!");
        }
    }
    
    private void loadCboTang() {
        cboTang.removeAllItems();
        cboTang.addItem(new Tang(-1, "Tất cả khu vực", "")); 
        List<Tang> list = tangDao.getAll();
        for(Tang t : list) cboTang.addItem(t);
    }
    
    private void thucHienLoc() {
        String tuKhoa = txtTimKiem.getText();
        Tang t = (Tang) cboTang.getSelectedItem();
        int maTang = (t != null) ? t.getMaTang() : -1;
        List<DienNuoc> list = dao.timKiemNangCao(tuKhoa, maTang);
        doDuLieuVaoBang(list);
    }

    private void tinhTongTuDong() {
        try {
            double dien = Double.parseDouble(txtTienDien.getText().replaceAll("[^0-9]", ""));
            double nuoc = Double.parseDouble(txtTienNuoc.getText().replaceAll("[^0-9]", ""));
            String phongText = txtTienPhong.getText().replaceAll("[^0-9]", "");
            double phong = phongText.isEmpty() ? 0 : Double.parseDouble(phongText);
            
            double tong = dien + nuoc + phong;
            txtTongCong.setText(new DecimalFormat("#,###").format(tong));
        } catch(Exception ex) {}
    }

    public void loadData() {
        doDuLieuVaoBang(dao.getAll());
    }
    
    private void doDuLieuVaoBang(List<DienNuoc> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        for(DienNuoc dn : list) {
            double tongBill = dn.getTienDien() + dn.getTienNuoc() + dn.getTienPhong();
            model.addRow(new Object[]{
                dn.getMaHD(), dn.getTenPhong(), dn.getThangNam(),
                df.format(dn.getTienDien()), df.format(dn.getTienNuoc()), 
                df.format(dn.getTienPhong()), df.format(tongBill), 
                dn.getTrangThai(), dn.getHinhThucTT()
            });
        }
    }
    
    private void clearForm() {
        txtMaHD.setText(""); txtTenPhong.setText(""); txtTienPhong.setText(""); 
        txtTienDien.setText(""); txtTienNuoc.setText(""); txtTongCong.setText("");
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
    
    private void xuatHoaDonPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn");
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".pdf")) path += ".pdf";

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(path));
                document.open();

                BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

                Paragraph title = new Paragraph("HÓA ĐƠN TIỀN ĐIỆN NƯỚC & PHÒNG", fontTitle);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" ", fontNormal));

                document.add(new Paragraph("Mã Hóa Đơn: " + txtMaHD.getText(), fontBold));
                document.add(new Paragraph("Phòng: " + txtTenPhong.getText(), fontBold));
                
                int r = tblHoaDon.getSelectedRow();
                String thang = model.getValueAt(r, 2).toString();
                document.add(new Paragraph("Tháng: " + thang, fontNormal));
                document.add(new Paragraph("----------------------------------------------------------", fontNormal));

                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                addCell(table, "Khoản thu", fontBold);
                addCell(table, "Thành tiền (VNĐ)", fontBold);

                addCell(table, "Tiền Điện", fontNormal);
                addCell(table, txtTienDien.getText(), fontNormal);

                addCell(table, "Tiền Nước", fontNormal);
                addCell(table, txtTienNuoc.getText(), fontNormal);

                addCell(table, "Tiền Phòng", fontNormal);
                addCell(table, txtTienPhong.getText(), fontNormal);

                document.add(table);

                Paragraph tongCong = new Paragraph("TỔNG CỘNG: " + txtTongCong.getText() + " VNĐ", fontTitle);
                tongCong.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(tongCong);
                
                document.add(new Paragraph(" ", fontNormal));
                document.add(new Paragraph(" ", fontNormal));
                PdfPTable footer = new PdfPTable(2);
                footer.setWidthPercentage(100);
                
                PdfPCell cell1 = new PdfPCell(new Paragraph("Người nộp tiền\n(Ký, họ tên)", fontNormal));
                cell1.setBorder(0); cell1.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                
                PdfPCell cell2 = new PdfPCell(new Paragraph("Người thu tiền\n(Ký, họ tên)", fontNormal));
                cell2.setBorder(0); cell2.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                
                footer.addCell(cell1);
                footer.addCell(cell2);
                document.add(footer);

                document.close();
                JOptionPane.showMessageDialog(this, "Xuất hóa đơn PDF thành công!\nLưu tại: " + path);
                
                try {
                    Desktop.getDesktop().open(new java.io.File(path));
                } catch (Exception ex) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + e.getMessage() + "\n(Hãy đảm bảo bạn đã add thư viện iText)");
        }
    }

    private void addCell(PdfPTable table, String text, com.itextpdf.text.Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setPadding(5);
        table.addCell(cell);
    }
}