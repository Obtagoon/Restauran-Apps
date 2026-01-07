package id.ac.unpas.restoranapp.view;

import id.ac.unpas.restoranapp.controller.KategoriController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Kategori extends JPanel {
    private KategoriController controller;

    // Komponen UI
    private JTextField txtNamaKategori;
    private JTextArea txtDeskripsi;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;
    private JTable tableKategori;
    private int selectedId = -1;

    public Kategori() {
        controller = new KategoriController();
        initComponents();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kategori"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Kategori:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNamaKategori = new JTextField(20);
        formPanel.add(txtNamaKategori, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDeskripsi = new JTextArea(3, 20);
        txtDeskripsi.setLineWrap(true);
        formPanel.add(new JScrollPane(txtDeskripsi), gbc);

        // --- Tombol ---
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

        // --- Tabel ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Kategori"));
        tableKategori = new JTable();
        // Listener saat baris diklik
        tableKategori.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selectTableRow();
        });
        tablePanel.add(new JScrollPane(tableKategori), BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Event Listener
        btnTambah.addActionListener(e -> tambahKategori());
        btnUpdate.addActionListener(e -> updateKategori());
        btnHapus.addActionListener(e -> hapusKategori());
        btnBersihkan.addActionListener(e -> bersihkanForm());
    }

    private void tambahKategori() {
        if (txtNamaKategori.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!");
            return;
        }
        id.ac.unpas.restoranapp.model.Kategori k = new id.ac.unpas.restoranapp.model.Kategori();
        k.setNamaKategori(txtNamaKategori.getText());
        k.setDeskripsi(txtDeskripsi.getText());
        
        // Panggil insert (Controller lama return String)
        String result = controller.insert(k); 
        JOptionPane.showMessageDialog(this, result);
        loadTableData();
        bersihkanForm();
    }

    private void updateKategori() {
        if (selectedId == -1) return;
        id.ac.unpas.restoranapp.model.Kategori k = new id.ac.unpas.restoranapp.model.Kategori();
        k.setId(selectedId);
        k.setNamaKategori(txtNamaKategori.getText());
        k.setDeskripsi(txtDeskripsi.getText());
        
        String result = controller.update(k);
        JOptionPane.showMessageDialog(this, result);
        loadTableData();
        bersihkanForm();
    }

    private void hapusKategori() {
        if (selectedId != -1 && JOptionPane.showConfirmDialog(this, "Hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String result = controller.delete(selectedId);
            JOptionPane.showMessageDialog(this, result);
            loadTableData();
            bersihkanForm();
        }
    }

    private void loadTableData() {
        // Ambil DefaultTableModel langsung dari controller lama
        DefaultTableModel model = controller.getAllKategori();
        tableKategori.setModel(model);
    }

    private void selectTableRow() {
        int row = tableKategori.getSelectedRow();
        if (row != -1) {
            // Kolom Controller Lama: 0=ID, 1=Nama, 2=Deskripsi
            selectedId = Integer.parseInt(tableKategori.getValueAt(row, 0).toString());
            txtNamaKategori.setText(tableKategori.getValueAt(row, 1).toString());
            txtDeskripsi.setText(tableKategori.getValueAt(row, 2).toString());
        }
    }

    private void bersihkanForm() {
        selectedId = -1;
        txtNamaKategori.setText("");
        txtDeskripsi.setText("");
        tableKategori.clearSelection();
    }
}