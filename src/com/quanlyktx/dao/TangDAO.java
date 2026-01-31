package com.quanlyktx.dao;

import com.quanlyktx.model.Tang;
import com.quanlyktx.util.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TangDAO {
    
    public List<Tang> getAll() {
        List<Tang> list = new ArrayList<>();
        String sql = "SELECT * FROM tang";
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                list.add(new Tang(rs.getInt("MaTang"), rs.getString("TenTang"), rs.getString("GhiChu")));
            }
            con.close();
        } catch (Exception e) {}
        return list;
    }

    public boolean them(Tang t) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO tang(TenTang, GhiChu) VALUES(?,?)");
            ps.setString(1, t.getTenTang());
            ps.setString(2, t.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean sua(Tang t) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE tang SET TenTang=?, GhiChu=? WHERE MaTang=?");
            ps.setString(1, t.getTenTang());
            ps.setString(2, t.getGhiChu());
            ps.setInt(3, t.getMaTang());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public boolean xoa(int ma) {
        try {
            Connection con = DatabaseHelper.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM tang WHERE MaTang=?");
            ps.setInt(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}