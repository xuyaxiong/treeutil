public class Node<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    Key key;
    Value val;
    Node<Key, Value> left;
    Node<Key, Value> right;
    int N;
    boolean color;

    public Node(Key key, Value val, int N) {
        this.key = key;
        this.val = val;
        this.N = N;
        this.color = RED;
    }

    public boolean isRed() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }
}
