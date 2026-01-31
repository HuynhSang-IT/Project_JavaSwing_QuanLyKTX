package com.quanlyktx.view;

import com.quanlyktx.dao.NhanVienDAO;
import com.quanlyktx.model.NhanVien;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NhanVienView extends JPanel {
    
    private JTextField txtUser, txtPass, txtHoTen, txtSDT;
    private JComboBox<String> cboQuyen;
    private JTable tblData;
    private DefaultTableModel model;
    
    private NhanVienDAO dao = new NhanVienDAO();

    public NhanVienView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN NHÂN VIÊN", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80)); 
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // 1. FORM INPUT (BÊN TRÁI)
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin tài khoản"),
            new EmptyBorder(5, 5, 5, 5)
        ));
        pnlInput.setBackground(new Color(250, 250, 250));
        
        // Tăng chiều rộng lên 450px
        pnlInput.setPreferredSize(new Dimension(450, 0)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tăng chiều dài ô nhập
        txtUser = new JTextField(20);
        txtPass = new JPasswordField(20);
        txtHoTen = new JTextField(20);
        txtSDT = new JTextField(20);
        
        cboQuyen = new JComboBox<>(new String[]{"Admin", "NhanVien", "BaoVe"});

        themComponent(pnlInput, gbc, 0, "Tên Đăng Nhập:", txtUser);
        themComponent(pnlInput, gbc, 1, "Mật khẩu:", txtPass);
        themComponent(pnlInput, gbc, 2, "Họ tên:", txtHoTen);
        themComponent(pnlInput, gbc, 3, "Số ĐT:", txtSDT);
        themComponent(pnlInput, gbc, 4, "Quyền:", cboQuyen);
        
        // --- SỬA LẠI NÚT BẤM ---
        JButton btnThem = new JButton("THÊM MỚI");
        btnThem.setBackground(new Color(46, 204, 113)); btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setPreferredSize(new Dimension(0, 45)); // Nút chính to
        
        JButton btnSua = new JButton("Cập nhật");
        btnSua.setBackground(new Color(52, 152, 219)); btnSua.setForeground(Color.WHITE);
        btnSua.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // Đặt chiều rộng tối thiểu để không bị mất chữ
        btnSua.setPreferredSize(new Dimension(100, 40)); 
        
        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(231, 76, 60)); btnXoa.setForeground(Color.WHITE);
        btnXoa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXoa.setPreferredSize(new Dimension(100, 40));
        
        JButton btnMoi = new JButton("Làm mới");
        btnMoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMoi.setPreferredSize(new Dimension(100, 40));

        // Nút Thêm to nằm riêng 1 dòng
        GridBagConstraints gbcBtnThem = (GridBagConstraints) gbc.clone();
        gbcBtnThem.gridx = 0; gbcBtnThem.gridy = 5; 
        gbcBtnThem.gridwidth = 2; 
        gbcBtnThem.insets = new Insets(15, 5, 5, 5); 
        pnlInput.add(btnThem, gbcBtnThem);
        
        // --- ĐỔI SANG FLOWLAYOUT ĐỂ KHÔNG BỊ CẮT CHỮ ---
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Cách nhau 10px
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnSua); 
        pnlBtn.add(btnXoa); 
        pnlBtn.add(btnMoi);
        
        GridBagConstraints gbcPnlBtn = (GridBagConstraints) gbc.clone();
        gbcPnlBtn.gridy = 6;
        gbcPnlBtn.gridwidth = 2; // Chiếm hết bề ngang
        gbcPnlBtn.insets = new Insets(5, 5, 5, 5);
        pnlInput.add(pnlBtn, gbcPnlBtn);
        
        // Đẩy toàn bộ nội dung lên trên
        GridBagConstraints gbcPush = (GridBagConstraints) gbc.clone();
        gbcPush.gridy = 7; gbcPush.weighty = 1.0; 
        pnlInput.add(new JLabel(), gbcPush);
        
        pnlCenter.add(pnlInput, BorderLayout.WEST);
        
        // 2. TABLE (BÊN PHẢI)
        String[] headers = {"Tài khoản", "Mật khẩu", "Họ Tên", "Quyền", "SĐT"};
        model = new DefaultTableModel(headers, 0);
        tblData = new JTable(model);
        tblData.setRowHeight(30); // Row cao hơn
        
        tblData.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        pnlCenter.add(new JScrollPane(tblData), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- LOGIC ---
        loadTable();
        
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tblData.getSelectedRow();
                if(r >= 0) {
                    txtUser.setText(model.getValueAt(r, 0).toString());
                    txtUser.setEditable(false); 
                    txtPass.setText(model.getValueAt(r, 1).toString());
                    txtHoTen.setText(model.getValueAt(r, 2).toString());
                    cboQuyen.setSelectedItem(model.getValueAt(r, 3).toString());
                    txtSDT.setText(model.getValueAt(r, 4).toString());
                }
            }
        });

        // CRUD
        btnThem.addActionListener(e -> {
            try {
                if(txtUser.getText().isEmpty()) { JOptionPane.showMessageDialog(null, "Nhập tên đăng nhập!"); return; }
                NhanVien nv = new NhanVien(txtUser.getText(), txtPass.getText(), txtHoTen.getText(), txtSDT.getText(), cboQuyen.getSelectedItem().toString());
                if(dao.them(nv)) { JOptionPane.showMessageDialog(null, "Thêm thành công!"); loadTable(); clearForm(); }
                else JOptionPane.showMessageDialog(null, "Trùng tên đăng nhập!");
            } catch(Exception ex) {}
        });
        
        btnSua.addActionListener(e -> {
            try {
                NhanVien nv = new NhanVien(txtUser.getText(), txtPass.getText(), txtHoTen.getText(), txtSDT.getText(), cboQuyen.getSelectedItem().toString());
                if(dao.sua(nv)) { JOptionPane.showMessageDialog(null, "Cập nhật thành công!"); loadTable(); clearForm(); }
            } catch(Exception ex) {}
        });
        
        btnXoa.addActionListener(e -> {
            if(!txtUser.getText().isEmpty() && JOptionPane.showConfirmDialog(null, "Xóa tài khoản này?") == JOptionPane.YES_OPTION) {
                dao.xoa(txtUser.getText()); loadTable(); clearForm();
            }
        });
        
        btnMoi.addActionListener(e -> clearForm());
    }

    public void loadTable() {
        model.setRowCount(0);
        List<NhanVien> list = dao.getAll();
        for(NhanVien nv : list) {
            model.addRow(new Object[]{nv.getTenDangNhap(), nv.getMatKhau(), nv.getHoTen(), nv.getQuyen(), nv.getSdt()});
        }
    }
    
    private void clearForm() {
        txtUser.setText(""); txtUser.setEditable(true); txtPass.setText(""); txtHoTen.setText(""); txtSDT.setText(""); tblData.clearSelection();
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