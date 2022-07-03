package frame;

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

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String insertSQl = "INSERT INTO pemesanan SET nama = ?, peminjam_id = ?";
                    ps = c.prepareStatement(insertSQl);
                    ps.setString(1, nama);
                    ps.setInt(2,peminjamId);
                    ps.executeUpdate();
                    dispose();

                } else {
                    String cekSQl = "SELECT * FROM pemesanan WHERE nama = ?AND id != ?";
                    ps = c.prepareStatement(cekSQl);
                    ps.setString(1,nama);
                    ps.setInt(2,id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data nama skuter sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE);

                    } else {
                        String updateSQL = "UPDATE pemesanan SET nama = ?, peminjam_id = ? WHERE id= ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setInt(2,peminjamId);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                        dispose();
                    }
                }
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
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM pemesanan WHERE id = ?";
        PreparedStatement ps;

        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
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
    }

}
