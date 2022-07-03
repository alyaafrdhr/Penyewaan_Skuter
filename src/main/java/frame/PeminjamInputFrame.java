package frame;

import com.mysql.cj.protocol.ReadAheadInputStream;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeminjamInputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton batalButton;
    private JButton simpanButton;
    private JPanel buttonPanel;

    private int id;

    public  void setId(int id) {
        this.id = id;
    }

    public PeminjamInputFrame() {
        simpanButton.addActionListener(e ->{
            String nama= namaTextField.getText();
            if(nama.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Peminjam",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQl = "SELECT * FROM peminjam WHERE nama = ?";
                    ps = c.prepareStatement(cekSQl);
                    ps.setString(1,nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data nama peminjam sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        String insertSQl = "INSERT INTO peminjam SET nama = ?";
                        ps = c.prepareStatement(insertSQl);
                        ps.setString(1, nama);
                        ps.executeUpdate();
                        dispose();
                    }
                } else {
                    String updateSQL = "UPDATE peminjam SET nama = ? WHERE id= ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex){
                    throw new RuntimeException(ex);
            }
        });

        batalButton.addActionListener(e ->{
            dispose();
        });
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Peminjam");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM peminjam WHERE id = ?";
        PreparedStatement ps;

        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
