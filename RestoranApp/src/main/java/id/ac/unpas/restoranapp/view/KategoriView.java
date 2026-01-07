package id.ac.unpas.restoranapp.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import id.ac.unpas.restoranapp.controller.KategoriController;
import id.ac.unpas.restoranapp.model.KategoriModel;

public class KategoriView extends JPanel {
    private KategoriController controller;

    // Komponen form input
    private JTextField txtNamaKategori;
    private JTextArea txtDeskripsi;
    private JButton btnInsert, btnUpdate, btnDelete, btnClear;

    // Komponen tabel
    private JTable tableKategori;
    private DefaultTableModel tableModel;

    // Variable untuk menyimpan ID saat update
    private int selectedId = -1;

    public KategoriView() {
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

        // Panel Button (Menggunakan nama variabel Remote: btnInsert, btnUpdate, btnDelete, btnClear)
        // Tapi label tetap sesuai selera/remote
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnInsert = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        btnClear = new JButton("Bersihkan");

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

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
        btnInsert.addActionListener(e -> insertKategori());
        btnUpdate.addActionListener(e -> updateKategori());
        btnDelete.addActionListener(e -> deleteKategori());
        btnClear.addActionListener(e -> clearForm());
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
    private void insertKategori() {
        if (!validateInput()) return;

        KategoriModel kategori = new KategoriModel();
        kategori.setNamaKategori(txtNamaKategori.getText().trim());
        kategori.setDeskripsi(txtDeskripsi.getText().trim());

        // Menggunakan logika HEAD (controller.insert mengembalikan String)
        if (controller.insert(kategori).contains("Berhasil")) {
            JOptionPane.showMessageDialog(this, "Kategori berhasil ditambahkan!");
            loadTableData();
            clearForm();
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

        KategoriModel kategori = new KategoriModel();
        kategori.setId(selectedId);
        kategori.setNamaKategori(txtNamaKategori.getText().trim());
        kategori.setDeskripsi(txtDeskripsi.getText().trim());

        if (controller.update(kategori).contains("Berhasil")) {
            JOptionPane.showMessageDialog(this, "Kategori berhasil diupdate!");
            loadTableData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate kategori!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method DELETE
    private void deleteKategori() {
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
            if (controller.delete(selectedId).contains("Berhasil")) {
                JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus!");
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kategori!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method untuk load data ke tabel
    private void loadTableData() {
        // Logika HEAD: Menggunakan getAllKategoriList() untuk List<KategoriModel>
        tableModel.setRowCount(0); // Clear tabel
        List<KategoriModel> kategoriList = controller.getAllKategoriList(); 

        for (KategoriModel k : kategoriList) {
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
    private void clearForm() {
        selectedId = -1;
        txtNamaKategori.setText("");
        txtDeskripsi.setText("");
        tableKategori.clearSelection();
    }
}
