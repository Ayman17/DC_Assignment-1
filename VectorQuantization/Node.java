import java.util.ArrayList;
import java.util.List;

public class Node {
    Vector average;
    Vector rounded;
    int vectorDimension;
    List<Vector> childVectors = new ArrayList<>();
    List<Vector> inputVectors = new ArrayList<>();

    Node(List<Vector> vs, int vectorDimension) {
        this.vectorDimension = vectorDimension;
        this.childVectors = vs;
        calcAverage();
    }

    Node(Vector rounded, int vectorDimension) {
        this.vectorDimension = vectorDimension;
        this.vectorDimension = vectorDimension;
        average = null;
        this.rounded = rounded;
    }

    public void calcAverage() {
        average = new Vector(vectorDimension);
        for (Vector vector : childVectors) {
            average.add(vector);
        }
        average.divide(childVectors.size());
    }

    public void divideChilds(List<Node> newNodes) {
        for (int i = 0; i < childVectors.size(); i++) {
            int result = -1;
            int minVal = Integer.MAX_VALUE;
            for (int j = 0; j < newNodes.size(); j++) {
                int current = childVectors.get(i).getDistance(newNodes.get(j).rounded);
                if (current < minVal) {
                    minVal = current;
                    result = j;
                }
            }
            newNodes.get(result).childVectors.add(childVectors.get(i));
        }
    }
}
    