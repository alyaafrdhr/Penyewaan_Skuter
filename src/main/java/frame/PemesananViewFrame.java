package frame;

import helpers.Koneksi;
import org.bouncycastle.jcajce.provider.drbg.DRBG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
public class PemesananViewFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JScrollPane viewScrollPane;
    private JTable viewTable;
    private JPanel buttonPanel;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
    private JPanel judulPanel;

    public PemesananViewFrame(){

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
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            PemesananInputFrame inputFrame = new PemesananInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });

        tambahButton.addActionListener(e ->{
            PemesananInputFrame inputFrame = new PemesananInputFrame();
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
                String deleteSQL = "DELETE FROM pemesanan WHERE id = ?";
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
            String keyword = cariTextField.getText();
            if(keyword.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            Connection c = Koneksi.getConnection();
            keyword= "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT P.*,B.nama AS nama_peminjam FROM pemesanan AS P " +
                    "LEFT JOIN peminjam AS B ON P.peminjam_id = B.id" +
                    "WHERE B.nama like ? OR P.nama like ?";

            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ResultSet rs = ps.executeQuery();

                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[7];

                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("nama_peminjam");
                    row[3] = rs.getString("tipe");
                    row[4] = rs.getString("no_telp");
                    row[5] = rs.getString("alamat");
                    row[6] = rs.getString("tanggal");
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
        setTitle("Data Pemesanan");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    public void isiTable(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT P.*,B.nama AS nama_peminjam FROM pemesanan AS P " +
                "LEFT JOIN peminjam AS B ON P.peminjam_id = B.id";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String[] header = {"Id","Nama Skuter", "Nama Peminjam","Tipe","No Telp","Alamat","Tanggal Sewa"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setWidth(50);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(50);
            viewTable.getColumnModel().getColumn(0).setMinWidth(50);
            viewTable.getColumnModel().getColumn(0).setPreferredWidth(50);

            Object[] row = new Object[7];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("nama_peminjam");
                row[3] = rs.getString("tipe");
                row[4] = rs.getString("no_telp");
                row[5] = rs.getString("alamat");
                row[6] = rs.getString("tanggal");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}


