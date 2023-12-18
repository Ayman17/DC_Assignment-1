import java.awt.image.BufferedImage;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        boolean useGUI = false;
        
        if (useGUI) {
            
            // new GUI();

        } else {
            String fileName = "car";
            String extension = ".jpg";
            PredictiveCoding v = new PredictiveCoding();
            BufferedImage image = v.readGrayImage(fileName + extension);


            List<Byte> result = v.compress(image, 508);
            // v.saveCompressedImage(result, "compressed.bin");

            // List<Byte> compressedImage = v.readBinaryFile("compressed.bin");
            // BufferedImage decompressedImage = v.decompress(compressedImage);
            // v.saveGrayImage(decompressedImage, "decomressed.jpg");
            
        }
    }
}