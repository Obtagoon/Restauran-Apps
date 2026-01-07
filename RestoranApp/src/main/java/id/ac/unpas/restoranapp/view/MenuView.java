package id.ac.unpas.restoranapp.view;
import id.ac.unpas.restoranapp.controller.MenuController;
import id.ac.unpas.restoranapp.controller.KategoriController;
import id.ac.unpas.restoranapp.model.MenuModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuView extends JPanel{
    private MenuController menuController;
    private KategoriController kategoriController;

    // Komponen form input
    private JTextField txtNamaMenu, txtHarga;
    private JComboBox<String> cmbKategori;
    private JTextArea txtDeskripsi;
    private JCheckBox chkTersedia;
    private JButton btnInsert, btnUpdate, btnDelete, btnClear;

    // Komponen tabel
    private JTable tableMenu;
    private DefaultTableModel tableModel;

    private int selectedId = -1;

    public MenuView() {
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
        btnInsert = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Hapus");
        btnClear = new JButton("Bersihkan");

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Panel Tabel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Menu"));

        String[] columnNames = {"ID", "Kategori ID", "Nama Menu", "Harga", "Deskripsi", "Status"};
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
        btnInsert.addActionListener(e -> tambahMenu());
        btnUpdate.addActionListener(e -> updateMenu());
        btnDelete.addActionListener(e -> hapusMenu());
        btnClear.addActionListener(e -> bersihkanForm());
    }

    // Load data kategori ke ComboBox
    private void loadKategoriComboBox() {
        cmbKategori.removeAllItems();
        DefaultTableModel kategoriModel = kategoriController.getAllKategori();

        for (int i = 0; i < kategoriModel.getRowCount(); i++) {
            int id = (int) kategoriModel.getValueAt(i, 0);
            String nama = (String) kategoriModel.getValueAt(i, 1);
            cmbKategori.addItem(id + " - " + nama);
        }
    }

    // Ambil kategori ID dari ComboBox
    private int getSelectedKategoriId() {
        String selected = (String) cmbKategori.getSelectedItem();
        if (selected != null) {
            return Integer.parseInt(selected.split(" - ")[0]);
        }
        return -1;
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

        MenuModel menu = new MenuModel();
        menu.setKategoriId(getSelectedKategoriId());
        menu.setNamaMenu(txtNamaMenu.getText().trim());
        menu.setHarga(Double.parseDouble(txtHarga.getText().trim()));
        menu.setDeskripsi(txtDeskripsi.getText().trim());
        menu.setTersedia(chkTersedia.isSelected());

        String result = menuController.insert(menu);

        if (result.contains("Berhasil")) {
            JOptionPane.showMessageDialog(this, result);
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, result,
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

        MenuModel menu = new MenuModel();
        menu.setId(selectedId);
        menu.setKategoriId(getSelectedKategoriId());
        menu.setNamaMenu(txtNamaMenu.getText().trim());
        menu.setHarga(Double.parseDouble(txtHarga.getText().trim()));
        menu.setDeskripsi(txtDeskripsi.getText().trim());
        menu.setTersedia(chkTersedia.isSelected());

        String result = menuController.update(menu);

        if (result.contains("Berhasil")) {
            JOptionPane.showMessageDialog(this, result);
            loadTableData();
            bersihkanForm();
        } else {
            JOptionPane.showMessageDialog(this, result,
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
            String result = menuController.delete(selectedId);

            if (result.contains("Berhasil")) {
                JOptionPane.showMessageDialog(this, result);
                loadTableData();
                bersihkanForm();
            } else {
                JOptionPane.showMessageDialog(this, result,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Load data ke tabel
    private void loadTableData() {
        tableModel = menuController.getAllMenu();
        tableMenu.setModel(tableModel);
    }

    // Select baris tabel
    private void selectTableRow() {
        int selectedRow = tableMenu.getSelectedRow();
        if (selectedRow != -1) {
            selectedId = (int) tableModel.getValueAt(selectedRow, 0);
            int kategoriId = (int) tableModel.getValueAt(selectedRow, 1);
            String namaMenu = (String) tableModel.getValueAt(selectedRow, 2);
            double harga = (double) tableModel.getValueAt(selectedRow, 3);
            String deskripsi = (String) tableModel.getValueAt(selectedRow, 4);
            String status = (String) tableModel.getValueAt(selectedRow, 5);

            txtNamaMenu.setText(namaMenu);
            txtHarga.setText(String.valueOf(harga));
            txtDeskripsi.setText(deskripsi);
            chkTersedia.setSelected(status.equals("Tersedia"));

            // Set selected kategori di combobox
            for (int i = 0; i < cmbKategori.getItemCount(); i++) {
                String item = cmbKategori.getItemAt(i);
                int id = Integer.parseInt(item.split(" - ")[0]);
                if (id == kategoriId) {
                    cmbKategori.setSelectedIndex(i);
                    break;
                }
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