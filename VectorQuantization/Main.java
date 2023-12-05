import java.awt.image.BufferedImage;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        boolean useGUI = true;
        
        if (useGUI) {
            
            new GUI();

        } else {
            String fileName = "image";
            String extension = ".jpg";
            VectorQuantization v = new VectorQuantization();
            BufferedImage image = v.readGrayImage(fileName + extension);


            List<Byte> result = v.compress(image, 4, 32);
            v.saveCompressedImage(result, "compressed.bin");

            List<Byte> compressedImage = v.readBinaryFile("compressed.bin");
            BufferedImage decompressedImage = v.decompress(compressedImage);
            v.saveGrayImage(decompressedImage, "decomressed.jpg");
            
        }
    }
}