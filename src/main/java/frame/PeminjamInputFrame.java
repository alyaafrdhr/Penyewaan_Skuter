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

    public PeminjamInputFrame() {
        simpanButton.addActionListener(e ->{
            String nama= namaTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                String insertSQl = "INSERT INTO peminjam SET nama = ?";
                ps = c.prepareStatement(insertSQl);
                ps.setString(1,nama);
                ps.executeUpdate();
                dispose();
            } catch (SQLException ex) {
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


}
