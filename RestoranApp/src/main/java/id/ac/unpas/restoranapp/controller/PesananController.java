package id.ac.unpas.restoranapp.controller;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import id.ac.unpas.restoranapp.database.KoneksiDB;
import id.ac.unpas.restoranapp.model.PesananModel;

public class PesananController {

    public DefaultTableModel getAllPesanan() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Menu ID");
        model.addColumn("No Meja");
        model.addColumn("Jumlah");
        model.addColumn("Total Harga");
        model.addColumn("Status");
        model.addColumn("Waktu");

        try {
            String sql = "SELECT * FROM pesanan ORDER BY id DESC";
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{
                    res.getInt("id"),
                    res.getInt("menu_id"),
                    res.getString("nomor_meja"),
                    res.getInt("jumlah"),
                    res.getDouble("total_harga"),
                    res.getString("status"),
                    res.getTimestamp("tanggal_pesan")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    // Ambil data untuk ListObject (jika diperlukan untuk manipulasi manual di View)
    public java.util.List<PesananModel> getAllPesananList() {
        java.util.List<PesananModel> list = new java.util.ArrayList<>();
        try {
            String sql = "SELECT p.*, m.nama_menu FROM pesanan p JOIN menu m ON p.menu_id = m.id ORDER BY p.id DESC";
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                PesananModel p = new PesananModel();
                p.setId(res.getInt("id"));
                p.setMenuId(res.getInt("menu_id"));
                p.setNomorMeja(res.getString("nomor_meja"));
                p.setJumlah(res.getInt("jumlah"));
                p.setTotalHarga(res.getDouble("total_harga"));
                p.setStatus(res.getString("status"));
                p.setTanggalPesan(res.getTimestamp("tanggal_pesan"));
                p.setNamaMenu(res.getString("nama_menu"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Insert Pesanan (Hitung Total Otomatis)
    public String insert(PesananModel pesanan) {
        try {
            double hargaSatuan = getHargaMenu(pesanan.getMenuId());
            double total = hargaSatuan * pesanan.getJumlah();

            String sql = "INSERT INTO pesanan (menu_id, nomor_meja, jumlah, total_harga, status) VALUES (?, ?, ?, ?, ?)";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, pesanan.getMenuId());
            pst.setString(2, pesanan.getNomorMeja());
            pst.setInt(3, pesanan.getJumlah());
            pst.setDouble(4, total);
            pst.setString(5, "Pending");
            pst.executeUpdate();
            return "Pesanan dibuat. Total: " + total;
        } catch (SQLException e) {
            return "Gagal pesan: " + e.getMessage();
        }
    }

    private double getHargaMenu(int menuId) throws SQLException {
        String sql = "SELECT harga FROM menu WHERE id = ?";
        Connection conn = KoneksiDB.configDB();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, menuId);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) return rs.getDouble("harga");
        return 0;
    }

    public String updateStatus(int id, String status) {
        try {
            String sql = "UPDATE pesanan SET status=? WHERE id=?";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, status);
            pst.setInt(2, id);
            pst.executeUpdate();
            return "Status berhasil diupdate";
        } catch (SQLException e) {
            return "Gagal update: " + e.getMessage();
        }
    }

    public String delete(int id) {
        try {
            String sql = "DELETE FROM pesanan WHERE id=?";
            Connection conn = KoneksiDB.configDB();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return "Pesanan dihapus";
        } catch (SQLException e) {
            return "Gagal hapus: " + e.getMessage();
        }
    }

    // Hitung Total Pendapatan (Status Selesai)
    public double getTotalPendapatan() {
        double total = 0;
        try {
            String sql = "SELECT SUM(total_harga) AS total FROM pesanan WHERE status = 'Selesai'";
            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            if (res.next()) {
                total = res.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Fitur Export PDF
    public String exportToPDF(String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath + ".pdf"));
            document.open();

            document.add(new Paragraph("LAPORAN DATA PESANAN RESTORAN"));
            document.add(new Paragraph("--------------------------------------------------"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.addCell("ID");
            table.addCell("Meja");
            table.addCell("Menu ID");
            table.addCell("Jumlah");
            table.addCell("Total");
            table.addCell("Status");

            Connection conn = KoneksiDB.configDB();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM pesanan");

            while (res.next()) {
                table.addCell(String.valueOf(res.getInt("id")));
                table.addCell(res.getString("nomor_meja"));
                table.addCell(String.valueOf(res.getInt("menu_id")));
                table.addCell(String.valueOf(res.getInt("jumlah")));
                table.addCell(String.valueOf(res.getDouble("total_harga")));
                table.addCell(res.getString("status"));
            }

            document.add(table);
            document.close();
            return "PDF Berhasil dibuat!";
        } catch (Exception e) {
            return "Gagal Export PDF: " + e.getMessage();
        }
    }
}