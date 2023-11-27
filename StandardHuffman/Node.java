import java.util.ArrayList;

public class Node {
    private float sumFreq;
    private ArrayList<Leaf> leaves = new ArrayList<>();

    public float getSumFreq() {
        return sumFreq;
    }
    public void setSumFreq(float sumFreq) {
        this.sumFreq = sumFreq;
    }
    public ArrayList<Leaf> getLeaves() {
        return leaves;
    }
    public void addLeaf(Leaf leaf) {
        leaves.add(leaf);
    }
    public void removeLeaf(Leaf leaf) {
        leaves.remove(leaf);
    }
}


