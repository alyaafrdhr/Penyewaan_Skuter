//import frame.PeminjamViewFrame;
import frame.PemesananViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
//        PeminjamViewFrame viewFrame = new PeminjamViewFrame();
//       viewFrame.setVisible(true);
        PemesananViewFrame viewFrame = new PemesananViewFrame();
        viewFrame.setVisible(true);
    }
}