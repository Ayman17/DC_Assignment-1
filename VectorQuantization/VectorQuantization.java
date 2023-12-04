import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.tree.TreeNode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class VectorQuantization {
    List<Integer> imageSize = Arrays.asList(640, 426);

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
        Vector average = new Vector(vectorWidth, vectorHeight);

        for (int i = 0; i < input.getWidth(); i += vectorWidth){
            for (int j = 0; j < input.getHeight(); j += vectorHeight) {
                Vector temp = new Vector(vectorWidth, vectorHeight);
                temp.addStartingFrom(i, j, input);
                inputVectors.add(temp);
            }
        }
        
        Node root = new Node(inputVectors);
        List<Node> output = new ArrayList<>(); 
        output.add(root);

        for (int i = 0; i < numberOfVectors; i++) {
            List<Node> newNodes = new ArrayList<>();
            for (int j = 0; j < output.size(); j++) {
                Vector floored = output.get(j).average.floor();
                Vector ceiled = output.get(j).average.ceil();
                newNodes.add(new Node(floored));
                newNodes.add(new Node(ceiled));
            }

            divideInputsVecotrs(newNodes, inputVectors);

            output = newNodes;
            for (int j = 0; j < output.size(); j++) {
                output.get(j).calcAverage();
            }
        }

        Map<Vector, Vector> mapping = new HashMap<>();

        for (int i = 0; i < output.size(); i++) {
            for (int j = 0; j < output.get(i).childVectors.size(); j++) {
                mapping.put(output.get(i).childVectors.get(j), output.get(i).average);
            } 
        }

        
        List<Vector> result = new ArrayList<>();
        for (int i = 0; i < inputVectors.size(); i++) {
            result.add(mapping.get(inputVectors.get(i)));
        }

        result.get(0).print();


        return input;
    }

    public String decompress(List<Byte> input) {
        return new String();
    }
}
