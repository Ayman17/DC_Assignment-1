import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class VectorQuantization {

    public BufferedImage readGrayImage(String filePath)  {
        BufferedImage result;
 
        try {
            BufferedImage input = ImageIO.read(new File(filePath));
            result = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            result.getGraphics().drawImage(input, 0, 0, null);
            System.out.println(result.getWidth() + ", " + result.getHeight());


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

    public void divideInputsVecotrs(List<Node> newNodes, List<Vector>inputVectors) {
        for (int i = 0; i < inputVectors.size(); i++) {
            int result = -1;
            int minVal = Integer.MAX_VALUE;
            for (int j = 0; j < newNodes.size(); j++) {
                int current = inputVectors.get(i).getDistance(newNodes.get(j).rounded);
                if (current < minVal) {
                    minVal = current;
                    result = j;
                }
            }
            newNodes.get(result).childVectors.add(inputVectors.get(i));
        }
    }

    public List<Byte> compress(BufferedImage input, int vectorDimension, int numberOfVectors) {
        List<Vector> inputVectors = new ArrayList<Vector>();
        List<Integer> imageSize = Arrays.asList(input.getWidth(),input.getHeight());

        for (int i = 0; i < input.getHeight(); i += vectorDimension){
            for (int j = 0; j < input.getWidth(); j += vectorDimension) {
                Vector temp = new Vector(vectorDimension);
                temp.addStartingFrom(j, i, input);
                inputVectors.add(temp);
            }
        }
        
        Node root = new Node(inputVectors, vectorDimension);
        List<Node> output = new ArrayList<>(); 
        output.add(root);
        
        while (output.size() < numberOfVectors) {
            List<Node> newNodes = new ArrayList<>();
            for (int j = 0; j < output.size(); j++) {
                Vector floored = output.get(j).average.floor();
                Vector ceiled = output.get(j).average.ceil();
                newNodes.add(new Node(floored, vectorDimension));
                newNodes.add(new Node(ceiled, vectorDimension));
            }

            divideInputsVecotrs(newNodes, inputVectors);

            output = newNodes;
            for (int j = 0; j < output.size(); j++) {
                output.get(j).calcAverage();
            }
        }
        
        Map<Vector, Vector> mapping = new HashMap<>();
        List<List<Vector>> result = new ArrayList<>();
        
        for (int i = 0; i < output.size(); i++) {
            for (int j = 0; j < output.get(i).childVectors.size(); j++) {
                mapping.put(output.get(i).childVectors.get(j), output.get(i).average);
            } 
        }
        
        for (int i = 0; i < inputVectors.size(); i++) {
            if (i % (imageSize.get(0) / vectorDimension) == 0) {
                result.add(new ArrayList<>());
            }
            result.get(result.size() - 1).add(mapping.get(inputVectors.get(i)));
        }

        List<Byte> compressedBytes = compressedToBytes(imageSize, numberOfVectors, vectorDimension, output, result);

        return compressedBytes;
    }
    /*
     * outputByts : 
     *          2 byte for width
     *          2 byte for height
     *          1 byte for numberOfVectors
     *          1 byte for vectorDimension
     * 
     *          for each vector :
     *              vectorDimension * vectorDimension * 1 bytes for each value
     *          
     *          1 byte for each vector code
     */
    List<Byte> compressedToBytes(List<Integer> imageSize, int numberOfVectors, int vectorDimension, List<Node> compressedVectors, List<List<Vector>> image2DVectors){
        List<Byte> outputBytes = new ArrayList<>();
        byte byte1 = (byte) ((imageSize.get(0)>> 8) & 0xFF); // Higher byte
        byte byte2 = (byte) (imageSize.get(0)& 0xFF); // Lower byte

        outputBytes.add(byte1);
        outputBytes.add(byte2);


        byte1 = (byte) ((imageSize.get(1)>> 8) & 0xFF); // Higher byte
        byte2 = (byte) (imageSize.get(1)& 0xFF); // Lower byte

        outputBytes.add(byte1);
        outputBytes.add(byte2);
       
        // int intValue = ((byte1 & 0xFF) << 8) | (byte2 & 0xFF);
        // System.out.println(intValue);

        byte1 = (byte) numberOfVectors;

        outputBytes.add(byte1);

        byte1 = (byte) vectorDimension;

        outputBytes.add(byte1);

        Map<Vector, Integer> mapping = new HashMap<>();
        for (int i = 0; i < compressedVectors.size(); i++) {
            mapping.put(compressedVectors.get(i).average, i);
            for (int y = 0; y < vectorDimension; y++) {
                for (int x = 0; x < vectorDimension; x++) {
                    byte1 = (byte) (int) (double) compressedVectors.get(i).average.vector.get(y).get(x);
                    outputBytes.add(byte1);
                }
            }
        }

        for (int i = 0; i < image2DVectors.size() ; i++) {
            for (int j = 0; j < image2DVectors.get(i).size(); j++) {
                int val = mapping.get(image2DVectors.get(i).get(j));
                outputBytes.add((byte) (val & 0xFF));
            }
        }

        return outputBytes;
    }

    BufferedImage vectorToImage(List<List<Vector>> v, int vectorDimension, List<Integer> imageSize) { 
        BufferedImage result = new BufferedImage(imageSize.get(0), imageSize.get(1), BufferedImage.TYPE_BYTE_GRAY);
        List<List<Integer>> grayScale = new ArrayList<>(imageSize.get(0));

        for (int i = 0; i < imageSize.get(1); i++) {
            grayScale.add(new ArrayList<>());
            for (int j = 0; j < imageSize.get(0); j++) {
                int value = (int) (double) v.get((int) i / vectorDimension).get((int) j / vectorDimension).vector.get(i  % vectorDimension).get(j % vectorDimension);
                grayScale.get(i).add(value);
            }
        }

        Set<Integer> unique = new HashSet<>();

        for (int y = 0; y < grayScale.size(); y++) {
            for (int x = 0; x < grayScale.get(y).size(); x++) {
                int pixelValue = grayScale.get(y).get(x);
                unique.add(pixelValue);
                int rgbValue = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
                result.setRGB(x, y, rgbValue);
            }
        }

        System.out.println(unique.size());

        return result;

    }



    public BufferedImage decompress(List<Byte> input) {
        System.out.println();
        System.out.println("Decompressing");

        int i = 0;

        int imageWidth = ((input.get(i) & 0xFF) << 8) | (input.get(i + 1) & 0xFF);
        i += 2;
        int imageHeight = ((input.get(i) & 0xFF) << 8) | (input.get(i + 1) & 0xFF);
        i += 2;
        int numberOfVectors = input.get(i) & 0xFF;
        i += 1;
        int vectorDimension = input.get(i) & 0xFF;
        i++;

        System.out.println(imageWidth + ", " + imageHeight);
        System.out.println("numer of vectors: " + numberOfVectors);
        System.out.println("vector dimension: " + vectorDimension);

        List<Vector> codes = new ArrayList<>();
        int counter = 0;
        while (codes.size() < numberOfVectors) {
            counter ++;
            Vector temp = new Vector(vectorDimension);
            for (int y = 0; y < vectorDimension; y++) {
                for (int x = 0; x < vectorDimension; x++) {
                    Double value = (double) (input.get(i) & 0xFF);
                    temp.updatePixel(x, y, value);
                    i++;
                }
            }
            codes.add(temp);
        }


        List<Vector> imageVector1D = new ArrayList<>();

        System.out.println("codes size:" + codes.size() + " counter: " + counter);

        while (i < input.size()) {
            int code = input.get(i) & 0xFF;
            Vector current;

            if (code > codes.size() - 1) {
                code = 0;
            }

            current = codes.get(code);
            
            imageVector1D.add(current);
            i++;
        }

        List<List<Vector>> imageVector2D = new ArrayList<>();


        for (int j = 0; j < imageVector1D.size(); j++) {
            if (j % (imageWidth / vectorDimension) == 0) {
                imageVector2D.add(new ArrayList<>());
            }
            imageVector2D.get(imageVector2D.size() - 1).add(imageVector1D.get(j));
        }


        return vectorToImage(imageVector2D, vectorDimension, Arrays.asList(imageWidth, imageHeight));
    }
}
