package id.ac.unpas.restoranapp.controller;

import id.ac.unpas.restoranapp.database.KoneksiDB;
import id.ac.unpas.restoranapp.model.Pesanan;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
// Import Library iText PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

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

    // Insert Pesanan (Hitung Total Otomatis)
    public String insert(Pesanan pesanan) {
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