import frame.PeminjamViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        PeminjamViewFrame viewFrame = new PeminjamViewFrame();
        viewFrame.setVisible(true);
    }
}