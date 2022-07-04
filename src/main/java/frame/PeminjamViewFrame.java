package frame;

import helpers.Koneksi;
import net.sf.jasperreports.view.JasperViewer;
import org.bouncycastle.jcajce.provider.drbg.DRBG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class PeminjamViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public PeminjamViewFrame(){

        ubahButton.addActionListener(e ->{
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null,
                        "Pilih data dulu",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            PeminjamInputFrame inputFrame = new PeminjamInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });

        tambahButton.addActionListener(e ->{
            PeminjamInputFrame inputFrame = new PeminjamInputFrame();
            inputFrame.setVisible(true);
        });

        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null,
                        "Pilih data dulu",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,
                    "Yakin mau hapus?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if(pilihan == 0) { //pilihan YES
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());

                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM peminjam WHERE id = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cariButton.addActionListener(e -> {
            if(cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            String keyword= "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM peminjam WHERE nama like ?";
            Connection c = Koneksi.getConnection();

            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();

                String[] header = {"Id","Nama"};
                DefaultTableModel dtm = new DefaultTableModel(header,0);
                viewTable.setModel(dtm);
                viewTable.getColumnModel().getColumn(0).setWidth(100);
                viewTable.getColumnModel().getColumn(0).setMaxWidth(100);
                viewTable.getColumnModel().getColumn(0).setMinWidth(100);
                viewTable.getColumnModel().getColumn(0).setPreferredWidth(100);

                Object[] row = new Object[2];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }

        });
        isiTable();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Data Peminjam");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    public void isiTable(){
        String selectSQL = "SELECT * FROM peminjam";
        Connection c = Koneksi.getConnection();

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id","Nama"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setWidth(100);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(100);
            viewTable.getColumnModel().getColumn(0).setMinWidth(100);
            viewTable.getColumnModel().getColumn(0).setPreferredWidth(100);

            Object[] row = new Object[2];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

