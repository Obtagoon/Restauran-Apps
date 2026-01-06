/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.restoranapp.controller;

import com.mysql.cj.xdevapi.Statement;
import id.ac.unpas.restoranapp.database.KoneksiDB;
import id.ac.unpas.restoranapp.model.Kategori;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author milda
 */
public class KategoriController {

    //Ambil semua data untuk JTable
    public DefaultTableModel getAllKategori() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Kategori");
        model.addColumn("Deskripsi");

        try {
            String sql = "SELECT * FROM kategori";
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{
                    res.getInt("id"),
                    res.getString("nama_kategori"),
                    res.getString("deskripsi")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    //Tambah data baru
    public String insert(Kategori kategori) {
        if (kategori.getNamaKategori().trim().isEmpty()) {
            return "Nama Kategori tidak boleh kosong!";
        }
        try {
            String sql = "INSERT INTO kategori (nama_kategori, deskripsi) VALUES (?, ?)";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kategori.getNamaKategori());
            pst.setString(2, kategori.getDeskripsi());
            pst.executeUpdate();
            return "Berhasil menyimpan kategori";
        } catch (SQLException e) {
            return "Gagal simpan: " + e.getMessage();
        }
    }

    //Ubah data
    public String update(Kategori kategori) {
        try {
            String sql = "UPDATE kategori SET nama_kategori=?, deskripsi=? WHERE id=?";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kategori.getNamaKategori());
            pst.setString(2, kategori.getDeskripsi());
            pst.setInt(3, kategori.getId());
            pst.executeUpdate();
            return "Berhasil mengubah kategori";
        } catch (SQLException e) {
            return "Gagal update: " + e.getMessage();
        }
    }

    //Hapus data
    public String delete(int id) {
        try {
            String sql = "DELETE FROM kategori WHERE id=?";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return "Berhasil menghapus kategori";
        } catch (SQLException e) {
            return "Gagal hapus: " + e.getMessage();
        }
    }
}
