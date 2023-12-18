import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PredictiveCoding {
    public BufferedImage readGrayImage(String filePath)  {
        BufferedImage result;
 
        try {
            BufferedImage input = ImageIO.read(new File(filePath));
            result = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            result.getGraphics().drawImage(input, 0, 0, null);


        } catch(Exception e) {
            System.out.println(e);
            return null;
        }

        return result;
    }

     public ArrayList<Byte> readBinaryFile(String fileName) {
        ArrayList <Byte> content = new ArrayList();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int byteRead;
            while ((byteRead = fileInputStream.read()) != -1) {
                content.add((byte) byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public void saveCompressedImage(List<Byte> content, String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            // Convert ArrayList<Byte> to byte array
            byte[] byteArray = new byte[content.size()];
            for (int i = 0; i < content.size(); i++) {
                byteArray[i] = content.get(i);
            }

            fileOutputStream.write(byteArray);

        } catch (Exception e) {
            e.printStackTrace();
        }  
    }

    public void saveGrayImage(BufferedImage image, String filePath) {
        try {
            ImageIO.write(image, "jpg", new File(filePath));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private int grayValue(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        return (r + b + g) / 3;
    }

    private int predictPixel(int up, int left, int diagonale) {
        if (diagonale <= Math.min(up, left)) {
            return Math.max(up, left);
        } else if (diagonale >= Math.max(up, left)) {
            return Math.min(up, left);
        } else {
            return up + left - diagonale;
        }
    }

    // private List<List<Integer>> getPredictedImage(BufferedImage image, int) {
    //     List<List<Integer>> predicted = new ArrayList<List<Integer>>(); 
    //     List<Integer> qunantizer = new ArrayList<Integer>();
    //     qunantizer = getQuantizer()
    //     // List<List<Integer>> differences = new ArrayList<List<Integer>>(); 
    //     // int maxDiff = Integer.MIN_VALUE;
    //     // int minDiff = Integer.MAX_VALUE;
    
    //     for (int y = 0; y < image.getHeight(); y++) {
    //         predicted.add(new ArrayList<Integer>());
    //         // differences.add(new ArrayList<Integer>());
            
    //         for (int x = 0; x < image.getWidth(); x++) {
    //             if (y == 0 || x == 0) {
    //                 predicted.get(y).add(-1);
    //                 // differences.get(y).add(-1);
    //             } else {
    //                 int up = grayValue(image.getRGB(x, y - 1));
    //                 int left = grayValue(image.getRGB(x - 1, y));
    //                 int diagonale = grayValue(image.getRGB(x - 1, y - 1));

    //                 predicted.get(y).add(predictPixel(up, left, diagonale));
                    
    //                 // int currentDiff = predicted.get(y - 1).get(x - 1) - grayValue(image.getRGB(x, y));

    //                 // maxDiff = Math.max(maxDiff, currentDiff);
    //                 // minDiff = Math.min(minDiff, currentDiff);

    //                 // differences.get(y).add(currentDiff);
    //             }
    //         }
    //     }
        
    //     return predicted;
    // }

    private List<Integer> getQuantizer(int qunatizerSize) {
        List<Integer> qunantizer = new ArrayList<Integer>();
        double stepAdder = (1 / (double) qunatizerSize) * 510;
        System.out.println(stepAdder);
        double step = -255 + (stepAdder / 2);
        for (int i = 0; i < qunatizerSize; i++) {
            qunantizer.add((int) step);
            step += stepAdder;
        }

        // for (int i = -32; i < 32; i++) {
        //     qunantizer.add(i);
        // }

        return qunantizer;
    }

    private int getBestQunatizerIndex(List<Integer> qunantizer, int original, int predicted) {
        int result = 0;
        int subtracted = predicted - original;
        for (int i = 0; i < qunantizer.size(); i++) {
            if (Math.abs(subtracted - qunantizer.get(result)) < Math.abs(subtracted - qunantizer.get(i))) {
                break;
            }
            result = i;
        }
        return result;

    }

    public List<Byte> compress(BufferedImage input, int qunatizerSize) {
        // List<List<Integer>> predicted = new ArrayList<List<Integer>>(); 
        List<List<Integer>> quantizerIndexes = new ArrayList<List<Integer>>();
        List<Integer> qunantizer = new ArrayList<Integer>();
        qunantizer = getQuantizer(qunatizerSize);

        for (int y = 0; y < input.getHeight(); y++) {
            // predicted.add(new ArrayList<Integer>());
            quantizerIndexes.add(new ArrayList<Integer>());
            
            for (int x = 0; x < input.getWidth(); x++) {
                if (y == 0 || x == 0) {
                    // predicted.get(y).add(-1);
                    quantizerIndexes.get(y).add(-1);
                } else {
                    int up = grayValue(input.getRGB(x, y - 1));
                    int left = grayValue(input.getRGB(x - 1, y));
                    int diagonale = grayValue(input.getRGB(x - 1, y - 1));

                    int currentPixel = grayValue(input.getRGB(x, y));
                    int currentPredicted = predictPixel(up, left, diagonale);

                    // predicted.get(y).add(predictPixel(up, left, diagonale));
                    quantizerIndexes.get(y).add(getBestQunatizerIndex(qunantizer, currentPixel, currentPredicted));
                }
            }
        }

        //TODO: remove this
        toImage(input, qunantizer, quantizerIndexes);

        return new ArrayList<Byte>();
    }

    private void toImage(BufferedImage image, List<Integer> qunantizer, List<List<Integer>> quantizerIndexes) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Set<Integer> set = new java.util.HashSet<Integer>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (y == 0 || x == 0) {
                    int rgbValue = (image.getRGB(x, y) << 16) | (image.getRGB(x, y) << 8) | image.getRGB(x, y);
                    result.setRGB(x, y, rgbValue);
                } else {
                    int up = grayValue(result.getRGB(x, y - 1));
                    int left = grayValue(result.getRGB(x - 1, y));
                    int diagonale = grayValue(result.getRGB(x - 1, y - 1));

                    int predicted = predictPixel(up, left, diagonale);
                    int quantizedDiff = qunantizer.get(quantizerIndexes.get(y).get(x));

                    int resultPixel = predicted - quantizedDiff;
                    int rgbValue = (resultPixel << 16) | (resultPixel << 8) | resultPixel;

                    result.setRGB(x, y, rgbValue);
                    set.add(resultPixel);
                }
            }
        }
        System.out.println(set.size());
        saveGrayImage(result, "hamada.jpg");
    }

    public BufferedImage decompress(List<Byte> input) {

        return new BufferedImage(0, 0, 0);
    }
}
