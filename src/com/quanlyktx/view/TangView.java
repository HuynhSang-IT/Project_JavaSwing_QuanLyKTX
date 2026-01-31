package com.quanlyktx.view;

import com.quanlyktx.dao.TangDAO;
import com.quanlyktx.model.NhanVien;
import com.quanlyktx.model.Tang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TangView extends JPanel {
    
    private JTextField txtTenTang, txtGhiChu;
    private JTable tblData;
    private DefaultTableModel model;
    private TangDAO dao = new TangDAO();
    private int maDangChon = -1;
    
    private JButton btnThem, btnSua, btnXoa, btnMoi;

    public TangView(NhanVien user) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // HEADER
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("QUẢN LÝ TẦNG ( KHU VỰC KTX )", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(127, 140, 141)); 
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // CENTER
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // INPUT FORM
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin khu vực"));
        pnlInput.setBackground(new Color(250, 250, 250));
        
        // Tăng chiều rộng lên 480px cho thoải mái
        pnlInput.setPreferredSize(new Dimension(480, 0)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtTenTang = new JTextField(20);
        txtGhiChu = new JTextField(20);

        themComponent(pnlInput, gbc, 0, "Tên Tầng (Khu KTX):", txtTenTang);
        themComponent(pnlInput, gbc, 1, "Ghi Chú:", txtGhiChu);
        
        // --- NÚT BẤM (CHIA 2 DÒNG ĐỂ KHÔNG BỊ CẮT) ---
        btnThem = createBtn("THÊM", new Color(46, 204, 113));
        btnSua = createBtn("SỬA", new Color(52, 152, 219));
        btnXoa = createBtn("XÓA", new Color(231, 76, 60));
        btnMoi = createBtn("Làm mới", new Color(149, 165, 166));
        
        // Dòng 1: Thêm + Sửa
        JPanel pnlRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlRow1.setOpaque(false);
        pnlRow1.add(btnThem); 
        pnlRow1.add(btnSua);

        // Dòng 2: Mới + Xóa
        JPanel pnlRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlRow2.setOpaque(false);
        pnlRow2.add(btnMoi); 
        pnlRow2.add(btnXoa);

        // Add Dòng 1
        GridBagConstraints gbcR1 = (GridBagConstraints) gbc.clone();
        gbcR1.gridx = 0; gbcR1.gridy = 2; gbcR1.gridwidth = 2; 
        gbcR1.insets = new Insets(20, 5, 5, 5); 
        pnlInput.add(pnlRow1, gbcR1);
        
        // Add Dòng 2
        GridBagConstraints gbcR2 = (GridBagConstraints) gbc.clone();
        gbcR2.gridx = 0; gbcR2.gridy = 3; gbcR2.gridwidth = 2; 
        gbcR2.insets = new Insets(5, 5, 10, 5); 
        pnlInput.add(pnlRow2, gbcR2);
        
        // Đẩy form lên trên
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy = 4; gbcPush.weighty = 1.0; 
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // TABLE
        String[] headers = {"Mã", "Tên Tầng (Khu KTX)", "Ghi Chú"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30);
        tblData.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        phanQuyen(user);
        loadTable();
        
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    maDangChon = Integer.parseInt(model.getValueAt(r, 0).toString());
                    txtTenTang.setText(model.getValueAt(r, 1).toString());
                    Object ghiChu = model.getValueAt(r, 2);
                    txtGhiChu.setText(ghiChu != null ? ghiChu.toString() : "");
                }
            }
        });

        btnThem.addActionListener(e -> {
            if(txtTenTang.getText().isEmpty()) { JOptionPane.showMessageDialog(null, "Nhập tên tầng!"); return; }
            if(dao.them(new Tang(0, txtTenTang.getText(), txtGhiChu.getText()))) {
                JOptionPane.showMessageDialog(null, "Thêm thành công!"); loadTable(); clearForm();
            } else {
                JOptionPane.showMessageDialog(null, "Lỗi hoặc tên đã tồn tại!");
            }
        });
        
        btnSua.addActionListener(e -> {
            if(maDangChon != -1) {
                if(dao.sua(new Tang(maDangChon, txtTenTang.getText(), txtGhiChu.getText()))) {
                    JOptionPane.showMessageDialog(null, "Sửa thành công!"); loadTable(); clearForm();
                }
            }
        });
        
        btnXoa.addActionListener(e -> {
            if(maDangChon != -1 && JOptionPane.showConfirmDialog(null, "Xóa?") == JOptionPane.YES_OPTION) {
                dao.xoa(maDangChon); loadTable(); clearForm();
            }
        });
        
        btnMoi.addActionListener(e -> clearForm());
    }

    private JButton createBtn(String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(110, 40)); // Size to, cố định
        return btn;
    }

    private void phanQuyen(NhanVien user) {
        if (!user.getQuyen().equalsIgnoreCase("Admin")) {
            btnXoa.setEnabled(false); btnXoa.setBackground(Color.LIGHT_GRAY);
        }
    }

    public void loadTable() {
        model.setRowCount(0);
        List<Tang> list = dao.getAll();
        for(Tang t : list) model.addRow(new Object[]{t.getMaTang(), t.getTenTang(), t.getGhiChu()});
    }
    
    private void clearForm() {
        maDangChon = -1; txtTenTang.setText(""); txtGhiChu.setText(""); tblData.clearSelection();
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