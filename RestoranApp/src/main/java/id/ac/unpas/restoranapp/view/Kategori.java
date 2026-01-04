package id.ac.unpas.restoranapp.view;

import controller.KategoriController;
import model.Kategori;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Kategori extends JPanel {
    private KategoriController controller;

    // Komponen form input
    private JTextField txtNamaKategori;
    private JTextArea txtDeskripsi;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;

    // Komponen tabel
    private JTable tableKategori;
    private DefaultTableModel tableModel;

    // Variable untuk menyimpan ID saat update
    private int selectedId = -1;

    public Kategori() {
        controller = new KategoriController();
        initComponents();
        loadTableData();
    }
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Form Input (Bagian Atas)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kategori"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nama Kategori
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Kategori:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNamaKategori = new JTextField(20);
        formPanel.add(txtNamaKategori, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Deskripsi:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDeskripsi = new JTextArea(3, 20);
        txtDeskripsi.setLineWrap(true);
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        formPanel.add(scrollDesc, gbc);

        // Panel Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnBersihkan = new JButton("Bersihkan");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnBersihkan);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Panel Tabel (Bagian Tengah)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Kategori"));

        // Buat tabel dengan kolom
        String[] columnNames = {"ID", "Nama Kategori", "Deskripsi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };
        tableKategori = new JTable(tableModel);
        tableKategori.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectTableRow();
            }
        });

        JScrollPane scrollTable = new JScrollPane(tableKategori);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        // Tambahkan komponen ke panel utama
        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Event listener untuk button
        btnTambah.addActionListener(e -> tambahKategori());
        btnUpdate.addActionListener(e -> updateKategori());
        btnHapus.addActionListener(e -> hapusKategori());
        btnBersihkan.addActionListener(e -> bersihkanForm());
    }

    // Method untuk validasi input
    private boolean validateInput() {
        if (txtNamaKategori.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama kategori tidak boleh kosong!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtNamaKategori.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this,
                    "Nama kategori minimal 3 karakter!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Method CREATE
    private void tambahKategori() {
        if (!validateInput()) return;

        // Cek duplikasi nama
        if (controller.isKategoriExists(txtNamaKategori.getText().trim(), 0)) {
            JOptionPane.showMessageDialog(this,
                    "Nama kategori sudah ada!",
                    "Duplikasi Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Kategori kategori = new Kategori(
                txtNamaKategori.getText().trim(),
                txtDeskripsi.getText().trim()
        );

        if (controller.tambahKategori(kategori)) {
            JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!");
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan kategori!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method UPDATE
    private void updateKategori() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan diupdate!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!validateInput()) return;

        // Cek duplikasi nama (exclude ID yang sedang diedit)
        if (controller.isKategoriExists(txtNamaKategori.getText().trim(), selectedId)) {
            JOptionPane.showMessageDialog(this,
                    "Nama kategori sudah ada!",
                    "Duplikasi Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Kategori kategori = new Kategori(
                selectedId,
                txtNamaKategori.getText().trim(),
                txtDeskripsi.getText().trim()
        );

        if (controller.updateKategori(kategori)) {
            JOptionPane.showMessageDialog(this, "Kategori berhasil diupdate!");
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate kategori!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method DELETE
    private void hapusKategori() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan dihapus!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus kategori ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.hapusKategori(selectedId)) {
                JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus!");
                loadTableData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus kategori! Mungkin masih ada menu yang menggunakan kategori ini.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method untuk load data ke tabel
    private void loadTableData() {
        tableModel.setRowCount(0); // Clear tabel
        List<Kategori> kategoriList = controller.getAllKategori();

        for (Kategori k : kategoriList) {
            Object[] row = {k.getId(), k.getNamaKategori(), k.getDeskripsi()};
            tableModel.addRow(row);
        }
    }

    // Method untuk select baris tabel dan isi form
    private void selectTableRow() {
        int selectedRow = tableKategori.getSelectedRow();
        if (selectedRow != -1) {
            selectedId = (int) tableModel.getValueAt(selectedRow, 0);
            txtNamaKategori.setText((String) tableModel.getValueAt(selectedRow, 1));
            txtDeskripsi.setText((String) tableModel.getValueAt(selectedRow, 2));
        }
    }

    // Method untuk bersihkan form
    private void bersihkanForm() {
        selectedId = -1;
        txtNamaKategori.setText("");
        txtDeskripsi.setText("");
        tableKategori.clearSelection();
    }
}
