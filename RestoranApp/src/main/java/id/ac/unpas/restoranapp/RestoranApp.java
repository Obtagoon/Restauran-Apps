package id.ac.unpas.restoranapp;

import id.ac.unpas.restoranapp.view.Kategori;
import id.ac.unpas.restoranapp.view.Menu;
import id.ac.unpas.restoranapp.view.Pesanan;
import javax.swing.*;

public class RestoranApp {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { // Nimbus theme
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    private static void createAndShowGUI() {
        // Membuat Frame Utama
        JFrame frame = new JFrame("Aplikasi Kasir Restoran");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        // Membuat TabbedPane untuk navigasi antar halaman
        JTabbedPane tabbedPane = new JTabbedPane();
        // Menambahkan Panel View ke dalam Tab
        // Tab 1: Manajemen Pesanan (Transaksi)
        tabbedPane.addTab("Pesanan", new ImageIcon(), new Pesanan(), "Transaksi Pesanan");
        // Tab 2: Manajemen Menu
        tabbedPane.addTab("Menu Makanan", new ImageIcon(), new Menu(), "Kelola Data Menu");
        // Tab 3: Manajemen Kategori
        tabbedPane.addTab("Kategori", new ImageIcon(), new Kategori(), "Kelola Kategori Menu");
        // Menambahkan TabbedPane ke Frame
        frame.add(tabbedPane);
        // Menampilkan Frame
        frame.setVisible(true);
    }
}