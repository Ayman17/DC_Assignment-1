public class Node implements Comparable<Node>  {
    public float freq;
    public String code;
    public char ch = '\0';
    public Node right = null;
    public Node left = null;
    public boolean isLeaf = true;

    @Override
    public int compareTo(Node other) {
        if (other.freq == this.freq) {
            return 1;
        }
        return Float.compare(this.freq, other.freq);
    }

}


