package id.ac.unpas.restoranapp.model;

public class MenuModel {
    private int id;
    private int kategoriId;
    private String namaMenu;
    private double harga;
    private String deskripsi;
    private boolean tersedia;

    public MenuModel() {
    }

    // Getter & Setter
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public int getKategoriId() { 
        return kategoriId; 
    }
    public void setKategoriId(int kategoriId) { 
        this.kategoriId = kategoriId; 
    }

    public String getNamaMenu() { 
        return namaMenu;
    }
    public void setNamaMenu(String namaMenu) { 
        this.namaMenu = namaMenu; 
    }

    public double getHarga() { 
        return harga; 
    }
    public void setHarga(double harga) { 
        this.harga = harga; 
    }

    public String getDeskripsi() { 
        return deskripsi; 
    }
    public void setDeskripsi(String deskripsi) { 
        this.deskripsi = deskripsi; 
    }

    public boolean isTersedia() { 
        return tersedia; 
    }
    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }
}