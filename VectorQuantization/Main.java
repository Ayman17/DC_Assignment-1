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


            v.compress(image, 4, 4, 4);

            v.saveGrayImage(image, "result" + extension);
            
        }
    }
}