import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Vector {
    int MIN_VAL = 0; 
    int MAX_VAL = 255;
    int vectorDimension = 0;
    List<List<Double>> vector = new ArrayList<>();

    Vector(int vectorDimension) {
        this.vectorDimension = vectorDimension;
        for (int i = 0; i < vectorDimension; i++) {
            List<Double> temp = new ArrayList<>();
            for (int j = 0; j < vectorDimension; j++) {
                temp.add(0d);
            }
            vector.add(temp);
        }
    }
    
    void addStartingFrom(int x, int y, BufferedImage image) {
        for (int i = 0; i < vectorDimension && i + y < image.getHeight(); i++) {
            for (int j = 0; j < vectorDimension && j + x < image.getWidth(); j++) {
                int value = image.getRGB(j + x, i + y);
                value = (value >> 16) & 0xFF;
                updatePixel(j, i, (double) value);
            }
        }
    }


    void updatePixel(int x, int y, Double value) {
        vector.get(y).set(x, value);
    }

    void add(Vector other) {
        Double value = 0d;
        for (int i = 0; i < vectorDimension; i++) {
            for (int j = 0; j < vectorDimension; j++) {
                value = vector.get(i).get(j) + other.vector.get(i).get(j);
                updatePixel(j, i, value);
            }
        }
    }

    void divide(int value) {
        if (value == 0) {
            return;
        }
        for (int i = 0; i < vectorDimension; i++) {
            for (int j = 0; j < vectorDimension; j++) {
                Double newValue = vector.get(i).get(j) / value;
                updatePixel(j, i, newValue);
            }
        }
    }

    void print() {
        for (int i = 0; i < vectorDimension; i++) {
            for (int j = 0; j < vectorDimension; j++) {
                System.out.print(vector.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    Vector floor() {
        Vector result = new Vector(vectorDimension);

        for (int i = 0; i < vectorDimension; i++) {
            for (int j = 0; j < vectorDimension; j++) {
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
        Vector result = new Vector(vectorDimension);
        for (int i = 0; i < vectorDimension; i++) {
            for (int j = 0; j < vectorDimension; j++) {
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
        for (int i = 0; i < vectorDimension; i++) {
            for (int j = 0; j < vectorDimension; j++) {
                result += Math.abs(vector.get(i).get(j) - other.vector.get(i).get(j));
            }
        } 
        return result;
    }
}