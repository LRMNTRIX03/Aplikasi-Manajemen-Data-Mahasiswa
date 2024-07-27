import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FormMahasiswa extends JFrame {
    private JRadioButton lkRB;
    private JPanel Main;
    private JButton btnDelete;
    private JTable tableHasil;
    private JLabel judul;
    private JLabel txtNim;
    private JLabel txtNama;
    private JLabel txtFakultas;
    private JLabel txtJurusan;
    private JLabel txtAlamat;
    private JTextField etNama;
    private JRadioButton prRB;
    private JTextField etNim;
    private JTextArea etAlamat;
    private JButton tambahButton;
    private JButton btnUpdate;
    private JComboBox<String> cbFak;
    private JComboBox<String> cbJR;
    private DefaultTableModel tableModel;

    public FormMahasiswa() {
        tableModel = new DefaultTableModel();
        tableHasil.setModel(tableModel);
        setTitle("Form Mahasiswa");
        setContentPane(Main);
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        ButtonGroup btn = new ButtonGroup();
        btn.add(lkRB);
        btn.add(prRB);
        createTable();
        tampilData();
        loadComboBoxFakultas(cbFak);
        loadComboBoxJurusan(cbJR,cbFak);

        cbFak.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadComboBoxJurusan(cbJR, cbFak);
                }
            }
        });


        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim = etNim.getText().trim();
                String nama = etNama.getText().trim();
                String fakultas = cbFak.getSelectedItem().toString();
                String jurusan = cbJR.getSelectedItem().toString();
                String alamat = etAlamat.getText().trim();
                String jk = lkRB.isSelected() ? "Laki-laki" : "Perempuan";
                if (nim.isEmpty() || nama.isEmpty() || fakultas.isEmpty() || jurusan.isEmpty() || alamat.isEmpty() || jk.isEmpty()) {
                    JOptionPane.showMessageDialog(FormMahasiswa.this, "Tolong Isi Seluruh Form");
                } else {
                    Mahsiswa mahsiswa = new Mahsiswa(nim, nama, fakultas, jurusan, alamat, jk);
                    insertData(mahsiswa);
                    tampilData();
                    loadComboBoxFakultas(cbFak);
                    loadComboBoxJurusan(cbJR, cbFak);
                }
            }
        });

        tableHasil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableHasil.getSelectedRow();
                if (selectedRow != -1) {
                    String nim = (String) tableModel.getValueAt(selectedRow, 0);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableHasil.getSelectedRow();
                if (selectedRow != -1) {
                    String nim = (String) tableModel.getValueAt(selectedRow, 0);
                    int hasil = JOptionPane.showConfirmDialog(FormMahasiswa.this,
                            "Apakah anda yakin ingin Menghapus Data ini?", "Peringatan", JOptionPane.YES_NO_OPTION);
                    if (hasil == JOptionPane.YES_OPTION) {
                        deleteData(nim);
                        tampilData();
                        loadComboBoxFakultas(cbFak);
                        loadComboBoxJurusan(cbJR, cbFak);
                    }
                } else {
                    JOptionPane.showMessageDialog(FormMahasiswa.this, "Pilih baris yang ingin dihapus.");
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableHasil.getSelectedRow();
                if (selectedRow != -1) {
                    String nim = (String) tableModel.getValueAt(selectedRow, 0);
                    dialogUpdate(nim);
                    tampilData();
                    loadComboBoxFakultas(cbFak);
                    loadComboBoxJurusan(cbJR, cbFak);
                } else {
                    JOptionPane.showMessageDialog(FormMahasiswa.this, "Data Belum Dipilih atau Kosong Silakan Tambahkan/Pilih Data");
                }
            }
        });

    }

    private void deleteData(String nim) {
        try {
            Koneksi koneksi = new Koneksi();
            Connection connection = koneksi.getConnection();
            String sql = "DELETE FROM Mahasiswa WHERE nim = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nim);
            int hasil = preparedStatement.executeUpdate();
            if (hasil > 0) {
                JOptionPane.showMessageDialog(FormMahasiswa.this, "Data Berhasil Dihapus");
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(FormMahasiswa.this, "Error: " + err.getMessage());
        }
    }

    private void createTable() {
        tableModel.setDataVector(new Object[][]{}, new String[]{"NIM", "Nama", "Fakultas", "Jurusan", "Alamat", "Jenis Kelamin"});
    }

    public void insertData(Mahsiswa mahsiswa) {
        try {
            Koneksi koneksi = new Koneksi();
            Connection connection = koneksi.getConnection();
            String sql = "INSERT INTO Mahasiswa(nim, nama, fakultas, jurusan, alamat, jk) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mahsiswa.getNim());
            preparedStatement.setString(2, mahsiswa.getNama());
            preparedStatement.setString(3, mahsiswa.getFk());
            preparedStatement.setString(4, mahsiswa.getJr());
            preparedStatement.setString(5, mahsiswa.getAlamat());
            preparedStatement.setString(6, mahsiswa.getJk());
            int hasil = preparedStatement.executeUpdate();
            if (hasil > 0) {
                JOptionPane.showMessageDialog(FormMahasiswa.this, "Data Berhasil Ditambahkan");
                etNim.setText("");
                etNama.setText("");
                etAlamat.setText("");
                lkRB.setSelected(true);
            } else {
                JOptionPane.showMessageDialog(FormMahasiswa.this, "Data Gagal Ditambahkan");
            }
        } catch (Exception er) {
            JOptionPane.showMessageDialog(FormMahasiswa.this, "Error: " + er.getMessage());
        }
    }

    public void tampilData() {
        try {
            Koneksi konek = new Koneksi();
            Connection connection = konek.getConnection();
            String sql = "SELECT * FROM Mahasiswa";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                Object[] data = new Object[6];
                data[0] = resultSet.getString("nim");
                data[1] = resultSet.getString("nama");
                data[2] = resultSet.getString("fakultas");
                data[3] = resultSet.getString("jurusan");
                data[4] = resultSet.getString("alamat");
                data[5] = resultSet.getString("jk");
                tableModel.addRow(data);
            }
        } catch (Exception er) {
            JOptionPane.showMessageDialog(FormMahasiswa.this, "Error: " + er.getMessage());
        }
    }

    public void dialogUpdate(String nim) {
        JDialog dialog = new JDialog(this, "Update Data Mahasiswa", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        dialog.setContentPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel txtNama = new JLabel("Nama: ");
        JLabel txtJudul = new JLabel("Update Data Mahasiswa");
        txtJudul.setHorizontalAlignment(SwingConstants.CENTER);
        txtJudul.setFont(new Font("Arial", Font.BOLD, 25));
        JTextField etNamaUpdate = new JTextField(20);
        JComboBox<String> cbFakUpdate = new JComboBox<>();
        JComboBox<String> cbJurUpdate = new JComboBox<>();
        JTextArea etAlamatUpdate = new JTextArea(3, 20);
        JRadioButton lkRBUpdate = new JRadioButton("Laki-laki");
        JRadioButton prRBUpdate = new JRadioButton("Perempuan");
        ButtonGroup bgUpdate = new ButtonGroup();
        bgUpdate.add(lkRBUpdate);
        bgUpdate.add(prRBUpdate);
        loadComboBoxFakultas(cbFakUpdate);

        cbFakUpdate.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadComboBoxJurusan(cbJurUpdate, cbFakUpdate);
                }
            }
        });

        try {
            Koneksi konek = new Koneksi();
            Connection connection = konek.getConnection();
            String sql = "SELECT * FROM Mahasiswa WHERE nim = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nim);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                etNamaUpdate.setText(resultSet.getString("nama"));
                cbFakUpdate.setSelectedItem(resultSet.getString("fakultas"));
                cbJurUpdate.setSelectedItem(resultSet.getString("jurusan"));
                etAlamatUpdate.setText(resultSet.getString("alamat"));
                String jk = resultSet.getString("jk");
                if ("Laki-laki".equals(jk)) {
                    lkRBUpdate.setSelected(true);
                } else {
                    prRBUpdate.setSelected(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        panel.add(txtJudul);
        panel.add(txtNama);
        panel.add(etNamaUpdate);
        panel.add(new JLabel("Fakultas"));
        panel.add(cbFakUpdate);
        panel.add(new JLabel("Jurusan"));
        panel.add(cbJurUpdate);
        panel.add(new JLabel("Alamat"));
        panel.add(new JScrollPane(etAlamatUpdate));
        panel.add(lkRBUpdate);
        panel.add(prRBUpdate);

        JButton btnUpdateDialog = new JButton("Update");
        btnUpdateDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Koneksi konek = new Koneksi();
                    Connection connection = konek.getConnection();
                    String sql = "UPDATE Mahasiswa SET nama = ?, fakultas = ?, jurusan = ?, alamat = ?, jk = ? WHERE nim = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, etNamaUpdate.getText().trim());
                    preparedStatement.setString(2, (String) cbFakUpdate.getSelectedItem());
                    preparedStatement.setString(3, (String) cbJurUpdate.getSelectedItem());
                    preparedStatement.setString(4, etAlamatUpdate.getText().trim());
                    preparedStatement.setString(5, lkRBUpdate.isSelected() ? "Laki-laki" : "Perempuan");
                    preparedStatement.setString(6, nim);
                    int hasil = preparedStatement.executeUpdate();
                    if (hasil > 0) {
                        JOptionPane.showMessageDialog(dialog, "Data Berhasil Diupdate");
                        dialog.dispose();
                        tampilData();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Data Tidak Ditemukan");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                }
            }
        });
        panel.add(btnUpdateDialog);

        dialog.setVisible(true);
    }

    public void loadComboBoxJurusan(JComboBox<String> cbJurusan, JComboBox<String> cbFakultas) {
        try {
            Koneksi koneksi = new Koneksi();
            Connection connection = koneksi.getConnection();


            String fakultasDipilih = (String) cbFakultas.getSelectedItem();


            String sql = "SELECT nama FROM jurusan WHERE fakultas = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fakultasDipilih);
            ResultSet rsJur = preparedStatement.executeQuery();

            cbJurusan.removeAllItems();
            while (rsJur.next()) {
                cbJurusan.addItem(rsJur.getString("nama"));
            }
        } catch (Exception er) {
            JOptionPane.showMessageDialog(FormMahasiswa.this, "Data Bermasalah: " + er.getMessage());
        }
    }

    public void loadComboBoxFakultas(JComboBox<String> cbFakultas) {
        try {
            Koneksi koneksi = new Koneksi();
            Connection connection = koneksi.getConnection();

            String sql = "SELECT nama FROM fakultas";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rsFak = preparedStatement.executeQuery();

            cbFakultas.removeAllItems();
            while (rsFak.next()) {
                cbFakultas.addItem(rsFak.getString("nama"));
            }
        } catch (Exception er) {
            JOptionPane.showMessageDialog(FormMahasiswa.this, "Data Bermasalah: " + er.getMessage());
        }
    }


}
