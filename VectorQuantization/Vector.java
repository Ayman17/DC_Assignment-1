import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Vector {
    int MIN_VAL = 0; 
    int MAX_VAL = 255;
    int xSize = 0;
    int ySize = 0;
    List<List<Double>> vector = new ArrayList<>();

    Vector(int xSize,int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        for (int i = 0; i < xSize; i++) {
            List<Double> temp = new ArrayList<>();
            for (int j = 0; j < ySize; j++) {
                temp.add(0d);
            }
            vector.add(temp);
        }
    }
    
    void addStartingFrom(int x, int y, BufferedImage image) {
        for (int i = 0; i < xSize && xSize + x < image.getWidth(); i++) {
            for (int j = 0; j < ySize && ySize + y < image.getHeight(); j++) {
                int value = image.getRGB(i + x, j + y);
                value = (value >> 16) & 0xFF;
                updatePixel(i, j, (double) value);
            }
        }
    }


    void updatePixel(int x, int y, Double value) {
        vector.get(x).set(y, value);
    }

    void add(Vector other) {
        Double value = 0d;
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                value = vector.get(i).get(j) + other.vector.get(i).get(j);
                updatePixel(i, j, value);
            }
        }
    }

    void divide(int value) {
        if (value == 0) {
            System.out.println("yeah you can't do that");
            return;
        }
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                Double newValue = vector.get(i).get(j) / value;
                updatePixel(i, j, newValue);
            }
        }
    }

    void print() {
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                System.out.print(vector.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    Vector floor() {
        Vector result = new Vector(xSize, ySize);

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                Double newValue = Math.floor(vector.get(i).get(j));
                if (newValue == vector.get(i).get(j)) {
                    result.updatePixel(i, j, newValue - 1);
                } else {
                    result.updatePixel(i, j, newValue);
                }
            }
        }
        return result;
    }

    Vector ceil() {
        Vector result = new Vector(xSize, ySize);
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                Double newValue = Math.ceil(vector.get(i).get(j));
                if (newValue == vector.get(i).get(j)) {
                    result.updatePixel(i, j, newValue + 1);
                } else {
                    result.updatePixel(i, j, newValue);
                }
            }
        }
        return result;
    }

    int getDistance (Vector other) {
        int result = 0;
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                result += Math.abs(vector.get(i).get(j) - other.vector.get(i).get(j));
            }
        } 
        return result;
    }
}