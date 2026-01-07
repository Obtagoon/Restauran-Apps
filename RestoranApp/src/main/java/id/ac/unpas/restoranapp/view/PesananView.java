package id.ac.unpas.restoranapp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import id.ac.unpas.restoranapp.controller.MenuController;
import id.ac.unpas.restoranapp.controller.PesananController;
import id.ac.unpas.restoranapp.model.MenuModel;
import id.ac.unpas.restoranapp.model.PesananModel;

public class PesananView extends JPanel{
    private PesananController pesananController;
    private MenuController menuController;

    // Komponen form input
    private JTextField txtNomorMeja, txtJumlah;
    private JComboBox<MenuModel> cmbMenu; // Use MenuModel
    private JComboBox<String> cmbStatus;
    private JLabel lblHargaSatuan, lblTotalHarga;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;

    // Komponen tabel
    private JTable tablePesanan;
    private DefaultTableModel tableModel;

    private int selectedId = -1;

    public PesananView() {
        pesananController = new PesananController();
        menuController = new MenuController();
        initComponents();
        loadMenuComboBox();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Form Input
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Pesanan"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nomor Meja
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nomor Meja:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomorMeja = new JTextField(20);
        formPanel.add(txtNomorMeja, gbc);

        // Menu
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Menu:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbMenu = new JComboBox<>();
        // Custom Renderer
        cmbMenu.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MenuModel) {
                    setText(((MenuModel) value).getNamaMenu());
                }
                return this;
            }
        });
        // Event listener untuk update harga saat menu dipilih
        cmbMenu.addActionListener(e -> updateHargaInfo());
        formPanel.add(cmbMenu, gbc);

        // Harga Satuan (Read-only, otomatis dari menu)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Harga Satuan:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        lblHargaSatuan = new JLabel("Rp 0");
        lblHargaSatuan.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblHargaSatuan, gbc);

        // Jumlah
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Jumlah:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtJumlah = new JTextField(20);
        // Event listener untuk hitung total otomatis
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hitungTotalHarga();
            }
        });
        formPanel.add(txtJumlah, gbc);

        // Total Harga (Otomatis dihitung)
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Total Harga:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        lblTotalHarga = new JLabel("Rp 0");
        lblTotalHarga.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalHarga.setForeground(Color.BLUE);
        formPanel.add(lblTotalHarga, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] statusOptions = {"Pending", "Selesai", "Dibatalkan"};
        cmbStatus = new JComboBox<>(statusOptions);
        formPanel.add(cmbStatus, gbc);

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

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Panel Tabel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Pesanan"));

        String[] columnNames = {"ID", "No. Meja", "Menu", "Jumlah", "Total Harga", "Status", "Tanggal"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePesanan = new JTable(tableModel);
        tablePesanan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectTableRow();
            }
        });

        JScrollPane scrollTable = new JScrollPane(tablePesanan);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        // Panel info total pendapatan
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblInfo = new JLabel("Total Pendapatan (Selesai): ");
        JLabel lblPendapatan = new JLabel("Rp 0");
        lblPendapatan.setFont(new Font("Arial", Font.BOLD, 14));
        lblPendapatan.setForeground(new Color(0, 128, 0));
        infoPanel.add(lblInfo);
        infoPanel.add(lblPendapatan);
        tablePanel.add(infoPanel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Event listener
        btnTambah.addActionListener(e -> {
            tambahPesanan();
            updatePendapatan(lblPendapatan);
        });
        btnUpdate.addActionListener(e -> {
            updatePesanan();
            updatePendapatan(lblPendapatan);
        });
        btnHapus.addActionListener(e -> {
            hapusPesanan();
            updatePendapatan(lblPendapatan);
        });
        btnBersihkan.addActionListener(e -> bersihkanForm());

        // Update pendapatan awal
        updatePendapatan(lblPendapatan);
    }

    // Load data menu ke ComboBox (hanya menu yang tersedia)
    private void loadMenuComboBox() {
        cmbMenu.removeAllItems();
        List<MenuModel> menuList = menuController.getAllMenuList();
        for (MenuModel m : menuList) {
            if (m.isTersedia()) { // Hanya menu yang tersedia
                cmbMenu.addItem(m);
            }
        }
    }

    // Update info harga saat menu dipilih
    private void updateHargaInfo() {
        MenuModel selectedMenu = (MenuModel) cmbMenu.getSelectedItem();
        if (selectedMenu != null) {
            lblHargaSatuan.setText(String.format("Rp %.2f", selectedMenu.getHarga()));
            hitungTotalHarga();
        }
    }

    // Hitung total harga otomatis
    private void hitungTotalHarga() {
        try {
            MenuModel selectedMenu = (MenuModel) cmbMenu.getSelectedItem();
            if (selectedMenu != null && !txtJumlah.getText().trim().isEmpty()) {
                int jumlah = Integer.parseInt(txtJumlah.getText().trim());
                double total = selectedMenu.getHarga() * jumlah;
                lblTotalHarga.setText(String.format("Rp %.2f", total));
            } else {
                lblTotalHarga.setText("Rp 0");
            }
        } catch (NumberFormatException e) {
            lblTotalHarga.setText("Rp 0");
        }
    }

    // Validasi input
    private boolean validateInput() {
        // Validasi nomor meja
        if (txtNomorMeja.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nomor meja tidak boleh kosong!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi menu
        if (cmbMenu.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Pilih menu!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi jumlah
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Jumlah harus lebih dari 0!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Jumlah harus berupa angka!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Method CREATE
    private void tambahPesanan() {
        if (!validateInput()) return;

        MenuModel selectedMenu = (MenuModel) cmbMenu.getSelectedItem();
        int jumlah = Integer.parseInt(txtJumlah.getText().trim());
        double totalHarga = selectedMenu.getHarga() * jumlah;

        PesananModel pesanan = new PesananModel();
        pesanan.setNomorMeja(txtNomorMeja.getText().trim());
        pesanan.setMenuId(selectedMenu.getId());
        pesanan.setJumlah(jumlah);
        pesanan.setTotalHarga(totalHarga);
        pesanan.setStatus((String) cmbStatus.getSelectedItem());

        if (pesananController.insert(pesanan).contains("Pesanan dibuat")) {
            JOptionPane.showMessageDialog(this, "Pesanan berhasil ditambahkan!");
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan pesanan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method UPDATE
    private void updatePesanan() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan diupdate!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!validateInput()) return;

        MenuModel selectedMenu = (MenuModel) cmbMenu.getSelectedItem();
        int jumlah = Integer.parseInt(txtJumlah.getText().trim());
        double totalHarga = selectedMenu.getHarga() * jumlah;

        PesananModel pesanan = new PesananModel();
        pesanan.setId(selectedId);
        pesanan.setNomorMeja(txtNomorMeja.getText().trim());
        pesanan.setMenuId(selectedMenu.getId());
        pesanan.setJumlah(jumlah);
        pesanan.setTotalHarga(totalHarga);
        pesanan.setStatus((String) cmbStatus.getSelectedItem());

        // Note: Controller update method might be different allow only Status update based on previous findings.
        // We assume updateStatus is available or we implement handling similar to previous.
        // If controller only has updateStatus, we check here.
        if (pesananController.updateStatus(selectedId, (String) cmbStatus.getSelectedItem()).contains("berhasil")) {
             JOptionPane.showMessageDialog(this, "Status Pesanan berhasil diupdate! (Hanya Status)");
             loadTableData();
             bersihkanForm();
        } else {
             JOptionPane.showMessageDialog(this, "Gagal mengupdate pesanan!",
                     "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method DELETE
    private void hapusPesanan() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan dihapus!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus pesanan ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (pesananController.delete(selectedId).contains("dihapus")) {
                JOptionPane.showMessageDialog(this, "Pesanan berhasil dihapus!");
                loadTableData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus pesanan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Load data ke tabel
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<PesananModel> pesananList = pesananController.getAllPesananList();

        for (PesananModel p : pesananList) {
            Object[] row = {
                    p.getId(),
                    p.getNomorMeja(),
                    p.getNamaMenu(), // Sekarang sudah ada field ini di PesananModel
                    p.getJumlah(),
                    String.format("Rp %.2f", p.getTotalHarga()),
                    p.getStatus(),
                    p.getTanggalPesan() != null ? p.getTanggalPesan().toString() : "-"
            };
            tableModel.addRow(row);
        }
    }

    // Select baris tabel
    private void selectTableRow() {
        int selectedRow = tablePesanan.getSelectedRow();
        if (selectedRow != -1) {
            selectedId = (int) tableModel.getValueAt(selectedRow, 0);
            txtNomorMeja.setText((String) tableModel.getValueAt(selectedRow, 1));

            String namaMenu = (String) tableModel.getValueAt(selectedRow, 2);
            for (int i = 0; i < cmbMenu.getItemCount(); i++) {
                MenuModel m = cmbMenu.getItemAt(i);
                if (m.getNamaMenu().equalsIgnoreCase(namaMenu)) {
                    cmbMenu.setSelectedIndex(i);
                    break;
                }
            }

            txtJumlah.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));

            String status = (String) tableModel.getValueAt(selectedRow, 5);
            cmbStatus.setSelectedItem(status);

            hitungTotalHarga();
        }
    }

    // Update total pendapatan
    private void updatePendapatan(JLabel lblPendapatan) {
        double total = pesananController.getTotalPendapatan();
        lblPendapatan.setText(String.format("Rp %.2f", total));
    }

    // Bersihkan form
    private void bersihkanForm() {
        selectedId = -1;
        txtNomorMeja.setText("");
        txtJumlah.setText("");
        lblHargaSatuan.setText("Rp 0");
        lblTotalHarga.setText("Rp 0");
        if (cmbMenu.getItemCount() > 0) {
            cmbMenu.setSelectedIndex(0);
        }
        cmbStatus.setSelectedIndex(0);
        tablePesanan.clearSelection();
    }
}
