package id.ac.unpas.restoranapp.model;

public class KategoriModel {
    private int id;
    private String namaKategori;
    private String deskripsi;

    // Konstruktor Kosong
    public KategoriModel() {
    }

    // Konstruktor Lengkap
    public KategoriModel(int id, String namaKategori, String deskripsi) {
        this.id = id;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
    }

    // Getter & Setter
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getNamaKategori() { 
        return namaKategori; 
    }
    public void setNamaKategori(String namaKategori) { 
        this.namaKategori = namaKategori; 
    }

    public String getDeskripsi() { 
        return deskripsi; 
    }
    public void setDeskripsi(String deskripsi) { 
        this.deskripsi = deskripsi; 
    }
}
