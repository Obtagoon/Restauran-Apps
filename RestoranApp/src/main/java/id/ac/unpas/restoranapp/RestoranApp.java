package id.ac.unpas.restoranapp;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import id.ac.unpas.restoranapp.view.Kategori;
import id.ac.unpas.restoranapp.view.Menu;
import id.ac.unpas.restoranapp.view.Pesanan;

public class RestoranApp extends JFrame {

    public RestoranApp() {
        // Set properti JFrame
        setTitle("Aplikasi Restoran App - Main Menu");
        setSize(1024, 768); // Ukuran lebih besar agar enak dilihat
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen
        setLayout(new java.awt.BorderLayout());

        // Judul Aplikasi
        JLabel titleLabel = new JLabel("Sistem Manajemen Restoran", SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, java.awt.BorderLayout.NORTH);

        // Membuat JTabbedPane untuk menampung view
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tambahkan Tab View
        // Pastikan kelas Kategori, Menu, dan Pesanan adalah JPanel atau extends Component
        tabbedPane.addTab("Manajemen Menu", new Menu());
        tabbedPane.addTab("Manajemen Kategori", new Kategori());
        tabbedPane.addTab("Pesanan", new Pesanan());

        // Tambahkan ke Frame
        add(tabbedPane, java.awt.BorderLayout.CENTER);
        
        // Footer info (Opsional)
        JLabel footerLabel = new JLabel("Dibuat dengan Java JDBC & Swing | 2026", SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(footerLabel, java.awt.BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Menggunakan Event Dispatch Thread untuk Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Set LookAndFeel ke Nimbus agar tampilan lebih modern
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Fallback ke default jika gagal
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // Jalankan aplikasi
            new RestoranApp().setVisible(true);
        });
    }
}