package id.ac.unpas.restoranapp.view;

import id.ac.unpas.restoranapp.controller.MenuController;
import id.ac.unpas.restoranapp.controller.KategoriController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Menu extends JPanel{
    private MenuController menuController;
    private KategoriController kategoriController;

    // Komponen form input
    private JTextField txtNamaMenu, txtHarga;
    private JComboBox<Kategori> cmbKategori;
    private JTextArea txtDeskripsi;
    private JCheckBox chkTersedia;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;

    // Komponen tabel
    private JTable tableMenu;
    private DefaultTableModel tableModel;

    private int selectedId = -1;

    public Menu() {
        menuController = new MenuController();
        kategoriController = new KategoriController();
        initComponents();
        loadKategoriComboBox();
        loadTableData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Form Input
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Menu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nama Menu
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Menu:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNamaMenu = new JTextField(20);
        formPanel.add(txtNamaMenu, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Kategori:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbKategori = new JComboBox<>();
        formPanel.add(cmbKategori, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Harga:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtHarga = new JTextField(20);
        formPanel.add(txtHarga, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Deskripsi:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDeskripsi = new JTextArea(3, 20);
        txtDeskripsi.setLineWrap(true);
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        formPanel.add(scrollDesc, gbc);

        // Tersedia
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Tersedia:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        chkTersedia = new JCheckBox("Menu tersedia untuk dipesan");
        chkTersedia.setSelected(true);
        formPanel.add(chkTersedia, gbc);

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

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Panel Tabel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Menu"));

        String[] columnNames = {"ID", "Nama Menu", "Kategori", "Harga", "Deskripsi", "Tersedia"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMenu = new JTable(tableModel);
        tableMenu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectTableRow();
            }
        });

        JScrollPane scrollTable = new JScrollPane(tableMenu);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        // Event listener
        btnTambah.addActionListener(e -> tambahMenu());
        btnUpdate.addActionListener(e -> updateMenu());
        btnHapus.addActionListener(e -> hapusMenu());
        btnBersihkan.addActionListener(e -> bersihkanForm());
    }

    // Load data kategori ke ComboBox
    private void loadKategoriComboBox() {
        cmbKategori.removeAllItems();
        List<Kategori> kategoriList = kategoriController.getAllKategori();
        for (Kategori k : kategoriList) {
            cmbKategori.addItem(k);
        }
    }

    // Validasi input
    private boolean validateInput() {
        // Validasi nama menu
        if (txtNamaMenu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama menu tidak boleh kosong!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi kategori
        if (cmbKategori.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Pilih kategori menu!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validasi harga
        try {
            double harga = Double.parseDouble(txtHarga.getText().trim());
            if (harga <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Harga harus lebih dari 0!",
                        "Validasi Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Harga harus berupa angka!",
                    "Validasi Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Method CREATE
    private void tambahMenu() {
        if (!validateInput()) return;

        Kategori selectedKategori = (Kategori) cmbKategori.getSelectedItem();
        Menu menu = new Menu(
                txtNamaMenu.getText().trim(),
                selectedKategori.getId(),
                Double.parseDouble(txtHarga.getText().trim()),
                txtDeskripsi.getText().trim(),
                chkTersedia.isSelected()
        );

        if (menuController.tambahMenu(menu)) {
            JOptionPane.showMessageDialog(this, "Menu berhasil ditambahkan!");
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan menu!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method UPDATE
    private void updateMenu() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan diupdate!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!validateInput()) return;

        Kategori selectedKategori = (Kategori) cmbKategori.getSelectedItem();
        Menu menu = new Menu(
                selectedId,
                txtNamaMenu.getText().trim(),
                selectedKategori.getId(),
                selectedKategori.getNamaKategori(),
                Double.parseDouble(txtHarga.getText().trim()),
                txtDeskripsi.getText().trim(),
                chkTersedia.isSelected()
        );

        if (menuController.updateMenu(menu)) {
            JOptionPane.showMessageDialog(this, "Menu berhasil diupdate!");
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate menu!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method DELETE
    private void hapusMenu() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data yang akan dihapus!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus menu ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (menuController.hapusMenu(selectedId)) {
                JOptionPane.showMessageDialog(this, "Menu berhasil dihapus!");
                loadTableData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus menu!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Load data ke tabel
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Menu> menuList = menuController.getAllMenu();

        for (Menu m : menuList) {
            Object[] row = {
                    m.getId(),
                    m.getNamaMenu(),
                    m.getNamaKategori(),
                    String.format("Rp %.2f", m.getHarga()),
                    m.getDeskripsi(),
                    m.isTersedia() ? "Ya" : "Tidak"
            };
            tableModel.addRow(row);
        }
    }

    // Select baris tabel
    private void selectTableRow() {
        int selectedRow = tableMenu.getSelectedRow();
        if (selectedRow != -1) {
            selectedId = (int) tableModel.getValueAt(selectedRow, 0);

            // Ambil data lengkap dari database
            Menu menu = menuController.getMenuById(selectedId);
            if (menu != null) {
                txtNamaMenu.setText(menu.getNamaMenu());

                // Set selected kategori di combobox
                for (int i = 0; i < cmbKategori.getItemCount(); i++) {
                    Kategori k = cmbKategori.getItemAt(i);
                    if (k.getId() == menu.getKategoriId()) {
                        cmbKategori.setSelectedIndex(i);
                        break;
                    }
                }

                txtHarga.setText(String.valueOf(menu.getHarga()));
                txtDeskripsi.setText(menu.getDeskripsi());
                chkTersedia.setSelected(menu.isTersedia());
            }
        }
    }

    // Bersihkan form
    private void bersihkanForm() {
        selectedId = -1;
        txtNamaMenu.setText("");
        txtHarga.setText("");
        txtDeskripsi.setText("");
        chkTersedia.setSelected(true);
        if (cmbKategori.getItemCount() > 0) {
            cmbKategori.setSelectedIndex(0);
        }
        tableMenu.clearSelection();
    }
}
