import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class VectorQuantization {
    List<Integer> imageSize = Arrays.asList(640, 427);

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

    public BufferedImage compress(BufferedImage input, int vectorWidth, int vectorHeight, int numberOfVectors) {
        List<Vector> inputVectors = new ArrayList<Vector>();
        // Vector average = new Vector(vectorWidth, vectorHeight);
        // Set<Integer> unique = new HashSet<>();
        int maxVal = 0;
        for (int i = 0; i < input.getWidth(); i++ ) {
            for (int j = 0; j < input.getHeight(); j ++) {
                int value = (input.getRGB(i, j)>> 16) & 0xFF;
                if (value > maxVal) {
                    maxVal = value;
                }
                // unique.add(value);
            }
        }
        // System.out.println(unique.size());

        for (int i = 0; i < input.getHeight(); i += vectorWidth){
            for (int j = 0; j < input.getWidth(); j += vectorHeight) {
                Vector temp = new Vector(vectorWidth, vectorHeight);
                temp.addStartingFrom(j, i, input);
                inputVectors.add(temp);
            }
        }
        
        Node root = new Node(inputVectors, vectorWidth, vectorHeight);
        List<Node> output = new ArrayList<>(); 
        output.add(root);
        
        for (int i = 0; i < numberOfVectors; i++) {
            List<Node> newNodes = new ArrayList<>();
            for (int j = 0; j < output.size(); j++) {
                Vector floored = output.get(j).average.floor();
                Vector ceiled = output.get(j).average.ceil();
                newNodes.add(new Node(floored, vectorWidth, vectorHeight));
                newNodes.add(new Node(ceiled, vectorWidth, vectorHeight));
            }

            divideInputsVecotrs(newNodes, inputVectors);

            output = newNodes;
            for (int j = 0; j < output.size(); j++) {
                output.get(j).calcAverage();
            }
        }

        // for (Node n : output ) {
        //     n.average.print();
        //     System.out.println();
        // }
        
        
        Map<Vector, Vector> mapping = new HashMap<>();
        List<List<Vector>> result = new ArrayList<>();
        
        for (int i = 0; i < output.size(); i++) {
            for (int j = 0; j < output.get(i).childVectors.size(); j++) {
                mapping.put(output.get(i).childVectors.get(j), output.get(i).average);
            } 
        }
        
        for (int i = 0; i < inputVectors.size(); i++) {
            if (i % (imageSize.get(0) / vectorWidth) == 0) {
                result.add(new ArrayList<>());
            }
            result.get(result.size() - 1).add(mapping.get(inputVectors.get(i)));
            // result.get(result.size() - 1).add(inputVectors.get(i));
        }
        
        return vectorToImage(result, vectorWidth, vectorHeight);
    }

    BufferedImage vectorToImage(List<List<Vector>> v, int vectorWidth, int vectorHeight) { 
        BufferedImage result = new BufferedImage(imageSize.get(0), imageSize.get(1), BufferedImage.TYPE_BYTE_GRAY);
        List<List<Integer>> grayScale = new ArrayList<>(imageSize.get(0));

        for (int i = 0; i < imageSize.get(1); i++) {
            grayScale.add(new ArrayList<>());
            for (int j = 0; j < imageSize.get(0); j++) {
                int value = (int) (double) v.get((int) i / vectorWidth).get((int) j / vectorHeight).vector.get(i  % vectorWidth).get(j % vectorHeight);
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

    void addVectorToList(List<List<Integer>> grayScale, Vector vector) {
    }

    public String decompress(List<Byte> input) {
        return new String();
    }
}
