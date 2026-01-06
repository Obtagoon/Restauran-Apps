package id.ac.unpas.restoranapp.model;

import java.sql.Timestamp;

public class Pesanan {
    private int id;
    private int menuId;
    private String nomorMeja;
    private int jumlah;
    private double totalHarga;
    private String status;
    private Timestamp tanggalPesan;

    public Pesanan() {
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public String getNomorMeja() { return nomorMeja; }
    public void setNomorMeja(String nomorMeja) { this.nomorMeja = nomorMeja; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getTanggalPesan() { return tanggalPesan; }
    public void setTanggalPesan(Timestamp tanggalPesan) { this.tanggalPesan = tanggalPesan; }
}