import java.util.ArrayList;
import java.util.List;

public class Node {
    Node left;
    Node right;
    Vector average;
    Vector rounded;
    List<Vector> childVectors = new ArrayList<>();
    List<Vector> inputVectors = new ArrayList<>();

    Node(List<Vector> vs) {
        left = null;
        right = null;
        this.childVectors = vs;
        calcAverage();
    }

    Node(Vector rounded) {
        left = null;
        right = null;
        average = null;
        this.rounded = rounded;
    }

    public void calcAverage() {
        average = new Vector(childVectors.get(0).xSize, childVectors.get(0).ySize);
        for (Vector vector : childVectors) {
            average.add(vector);
        }
        average.divide(childVectors.size());
    }

    public void divideChilds(List<Node> newNodes) {
        // Vector floored = average.floor();
        // Vector ceiled  = average.ceil();

        // System.out.println("Flooored:::: ");
        // floored.print();    

        // System.out.println("Ceiled:::: ");
        // ceiled.print();    

        // Node newLeft = new Node();
        // Node newRight = new Node();

        // left = newLeft;
        // right = newRight;

        
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
    