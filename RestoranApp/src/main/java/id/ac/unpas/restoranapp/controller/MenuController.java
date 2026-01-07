package id.ac.unpas.restoranapp.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import id.ac.unpas.restoranapp.database.KoneksiDB;
import id.ac.unpas.restoranapp.model.MenuModel;

public class MenuController {

    public DefaultTableModel getAllMenu() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Kategori ID");
        model.addColumn("Nama Menu");
        model.addColumn("Harga");
        model.addColumn("Deskripsi");
        model.addColumn("Status");

        try {
            String sql = "SELECT * FROM menu";
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                String status = res.getBoolean("tersedia") ? "Tersedia" : "Habis";
                model.addRow(new Object[]{
                        res.getInt("id"),
                        res.getInt("kategori_id"),
                        res.getString("nama_menu"),
                        res.getDouble("harga"),
                        res.getString("deskripsi"),
                        status
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    // Ambil semua data untuk ComboBox (List)
    public java.util.List<MenuModel> getAllMenuList() {
        java.util.List<MenuModel> list = new java.util.ArrayList<>();
        try {
            String sql = "SELECT * FROM menu";
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                MenuModel menu = new MenuModel();
                menu.setId(res.getInt("id"));
                menu.setKategoriId(res.getInt("kategori_id"));
                menu.setNamaMenu(res.getString("nama_menu"));
                menu.setHarga(res.getDouble("harga"));
                menu.setDeskripsi(res.getString("deskripsi"));
                menu.setTersedia(res.getBoolean("tersedia"));
                list.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String insert(MenuModel menu) {
        try {
            String sql = "INSERT INTO menu (kategori_id, nama_menu, harga, deskripsi, tersedia) VALUES (?, ?, ?, ?, ?)";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, menu.getKategoriId());
            pst.setString(2, menu.getNamaMenu());
            pst.setDouble(3, menu.getHarga());
            pst.setString(4, menu.getDeskripsi());
            pst.setBoolean(5, menu.isTersedia());
            pst.executeUpdate();
            return "Berhasil menambahkan menu";
        } catch (SQLException e) {
            return "Gagal insert: " + e.getMessage();
        }
    }

    public String update(MenuModel menu) {
        try {
            String sql = "UPDATE menu SET kategori_id=?, nama_menu=?, harga=?, deskripsi=?, tersedia=? WHERE id=?";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, menu.getKategoriId());
            pst.setString(2, menu.getNamaMenu());
            pst.setDouble(3, menu.getHarga());
            pst.setString(4, menu.getDeskripsi());
            pst.setBoolean(5, menu.isTersedia());
            pst.setInt(6, menu.getId());
            pst.executeUpdate();
            return "Berhasil update menu";
        } catch (SQLException e) {
            return "Gagal update: " + e.getMessage();
        }
    }

    public String delete(int id) {
        try {
            String sql = "DELETE FROM menu WHERE id=?";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return "Berhasil menghapus menu";
        } catch (SQLException e) {
            return "Gagal hapus: " + e.getMessage();
        }
    }
}