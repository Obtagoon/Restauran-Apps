package id.ac.unpas.restoranapp.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import id.ac.unpas.restoranapp.controller.KategoriController;
import id.ac.unpas.restoranapp.controller.MenuController;
import id.ac.unpas.restoranapp.model.KategoriModel;
import id.ac.unpas.restoranapp.model.MenuModel;

public class MenuView extends JPanel{
    private MenuController menuController;
    private KategoriController kategoriController;

    // Komponen form input
    private JTextField txtNamaMenu, txtHarga;
    private JComboBox<KategoriModel> cmbKategori; // Use KategoriModel, NOT View
    private JTextArea txtDeskripsi;
    private JCheckBox chkTersedia;
    private JButton btnTambah, btnUpdate, btnHapus, btnBersihkan;

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
        // Custom Renderer untuk menampilkan nama kategori di ComboBox
        cmbKategori.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof KategoriModel) {
                    setText(((KategoriModel) value).getNamaKategori());
                }
                return this;
            }
        });
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

        String[] columnNames = {"ID", "Nama Menu", "Kategori ID", "Harga", "Deskripsi", "Tersedia"};
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
        List<KategoriModel> kategoriList = kategoriController.getAllKategoriList(); // Use Safe List
        for (KategoriModel k : kategoriList) {
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

        KategoriModel selectedKategori = (KategoriModel) cmbKategori.getSelectedItem();
        MenuModel menu = new MenuModel();
        menu.setNamaMenu(txtNamaMenu.getText().trim());
        menu.setKategoriId(selectedKategori.getId());
        menu.setHarga(Double.parseDouble(txtHarga.getText().trim()));
        menu.setDeskripsi(txtDeskripsi.getText().trim());
        menu.setTersedia(chkTersedia.isSelected());

        if (menuController.insert(menu).contains("Berhasil")) {
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

        KategoriModel selectedKategori = (KategoriModel) cmbKategori.getSelectedItem();
        MenuModel menu = new MenuModel();
        menu.setId(selectedId);
        menu.setNamaMenu(txtNamaMenu.getText().trim());
        menu.setKategoriId(selectedKategori.getId());
        menu.setHarga(Double.parseDouble(txtHarga.getText().trim()));
        menu.setDeskripsi(txtDeskripsi.getText().trim());
        menu.setTersedia(chkTersedia.isSelected());

        if (menuController.update(menu).contains("Berhasil")) {
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
            if (menuController.delete(selectedId).contains("Berhasil")) {
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
        tableModel = (DefaultTableModel) menuController.getAllMenu();
        tableMenu.setModel(tableModel);
    }

    // Select baris tabel
    private void selectTableRow() {
        int selectedRow = tableMenu.getSelectedRow();
        if (selectedRow != -1) {
            selectedId = (int) tableMenu.getModel().getValueAt(selectedRow, 0);
            
            // Note: Data di tabel mungkin tidak lengkap, idealnya getById dari controller
            // Tapi untuk simplifikasi kita ambil dari tabel atau field yang ada
             txtNamaMenu.setText((String) tableMenu.getModel().getValueAt(selectedRow, 2));
             txtHarga.setText(String.valueOf(tableMenu.getModel().getValueAt(selectedRow, 3)));
             txtDeskripsi.setText((String) tableMenu.getModel().getValueAt(selectedRow, 4));
             // Tersedia (String) -> Boolean
             String status = (String) tableMenu.getModel().getValueAt(selectedRow, 5);
             chkTersedia.setSelected(status.equalsIgnoreCase("Tersedia"));
             
             // Set Kategori ComboBox
             // Di tabel ada kategori_id (index 1)
             int katId = (int) tableMenu.getModel().getValueAt(selectedRow, 1);
             for (int i = 0; i < cmbKategori.getItemCount(); i++) {
                 KategoriModel k = cmbKategori.getItemAt(i);
                 if (k.getId() == katId) {
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
