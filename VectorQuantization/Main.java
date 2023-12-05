import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        boolean useGUI = false;
        
        if (useGUI) {
            
            // GUI gui = new GUI();

        } else {
            String fileName = "image";
            String extension = ".jpg";
            VectorQuantization v = new VectorQuantization();
            BufferedImage image = v.readGrayImage(fileName + extension);


            BufferedImage result = v.compress(image,4, 4, 8);
            v.saveGrayImage(result, "compressed" + extension);
            
        }
    }
}