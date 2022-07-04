package frame;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class PemesananInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JPanel buttonPanel;
    private JComboBox PeminjamComboBox;
    private JRadioButton skuterAnakRadioButton;
    private JRadioButton skuterDewasaRadioButton;
    private JTextField noTelpTextField;
    private JTextField alamatTextField;
    private DatePicker tanggalSewaDatePicker;
    private ButtonGroup tipeButtonGroup;

    private int id;

    public  void setId(int id) {
        this.id = id;
    }

    public PemesananInputFrame() {
        simpanButton.addActionListener(e ->{
            String nama= namaTextField.getText();
            if(nama.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Skuter",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }

            String tipe = "";
            if(skuterAnakRadioButton.isSelected()){
                tipe = "Skuter Anak";
            } else if(skuterDewasaRadioButton.isSelected()) {
                tipe = "Skuter Dewasa";
            } else{
                JOptionPane.showMessageDialog(null,
                        "Pilih tipe",
                        "Validasi Data Kosong",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ComboBoxItem item = (ComboBoxItem) PeminjamComboBox.getSelectedItem();
            int peminjamId = item.getValue();
            if(peminjamId == 0){
                JOptionPane.showMessageDialog(null,
                        "Pilih Nama Peminjam",
                        "Validasi Combobox",
                        JOptionPane.WARNING_MESSAGE);
                PeminjamComboBox.requestFocus();
                return;
            }

            String noTelp= noTelpTextField.getText();
            if(noTelp.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi No Telp",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                noTelpTextField.requestFocus();
                return;
            }

            String alamat= alamatTextField.getText();
            if(alamat.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Alamat",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                alamatTextField.requestFocus();
                return;
            }

            String tanggalSewa = tanggalSewaDatePicker.getText();
            if(tanggalSewa.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Tanggal Sewa",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                tanggalSewaDatePicker.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                String insertSQl = "INSERT INTO pemesanan (id, nama, peminjam_id, tipe, no_telp, alamat, tanggal" +
                        "VALUES (NULL, ?, ?, ?, ?, ?, ?)";
                ps = c.prepareStatement(insertSQl);
                ps.setString(1, nama);
                ps.setInt(2, peminjamId);
                ps.setString(3, tipe);
                ps.setString(4, noTelp);
                ps.setString(5, alamat);
                ps.setString(6, tanggalSewa);
                ps.executeUpdate();
                dispose();

                String updateSQL = "UPDATE pemesanan SET nama = ?, peminjam_id = ?, tipe = ?, no_telp = ?, alamat = ?, tanggal = ? WHERE id= ?";
                ps = c.prepareStatement(updateSQL);
                ps.setString(1, nama);
                ps.setInt(2, peminjamId);
                ps.setString(3, tipe);
                ps.setString(4, noTelp);
                ps.setString(5, alamat);
                ps.setString(6, tanggalSewa);
                ps.setInt(7, id);
                ps.executeUpdate();
                dispose();


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        batalButton.addActionListener(e ->
            dispose());
        kustomisasiKomponen();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Pemesanan");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        idTextField.setText(String.valueOf(id));
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM pemesanan WHERE id = ?";
        PreparedStatement ps;

        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                int peminjamId = rs.getInt("peminjam_id");
                for (int i = 0; i < PeminjamComboBox.getItemCount(); i++) {
                    PeminjamComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) PeminjamComboBox.getSelectedItem();
                    if(peminjamId == item.getValue()){
                        break;
                    }
                }
                String tipe = rs.getString("tipe");
                if(tipe != null){
                    if(tipe.equals("SKUTER ANAK")){
                        skuterAnakRadioButton.setSelected(true);
                    } else if(tipe.equals("SKUTER DEWASA")){
                        skuterDewasaRadioButton.setSelected(true);
                    }
                }
                noTelpTextField.setText(rs.getString("no_telp"));
                alamatTextField.setText(rs.getString("alamat"));
                tanggalSewaDatePicker.setText(rs.getString("tanggal"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void kustomisasiKomponen() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM peminjam ORDER BY nama";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            PeminjamComboBox.addItem(new ComboBoxItem(0, "Pilih Nama Peminjam"));
            while (rs.next()) {
                PeminjamComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        tipeButtonGroup = new ButtonGroup();
        tipeButtonGroup.add(skuterAnakRadioButton);
        tipeButtonGroup.add(skuterDewasaRadioButton);

        DatePickerSettings dps = new DatePickerSettings();
        dps.setFormatForDatesCommonEra("yyyy-MM-dd");
        tanggalSewaDatePicker.setSettings(dps);
    }

}
