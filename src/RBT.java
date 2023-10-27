import edu.princeton.cs.algs4.StdIn;

public class RBT<Key extends Comparable<Key>, Value> implements IST<Key, Value> {
    private Node<Key, Value> root;

    private int size(Node<Key, Value> x) {
        if (x == null) return 0;
        return x.N;
    }

    @Override
    public Node<Key, Value> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
    public Value get(Key key) {
        return null;
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    @Override
    public Key min() {
        return null;
    }

    @Override
    public Key max() {
        return null;
    }

    @Override
    public Key floor(Key key) {
        return null;
    }

    @Override
    public Key select(int k) {
        return null;
    }

    @Override
    public int rank(Key key) {
        return 0;
    }

    @Override
    public void deleteMin() {

    }

    @Override
    public void delete(Key key) {

    }

    @Override
    public Iterable<Key> keys() {
        return null;
    }

    @Override
    public Iterable<Key> keys(Key lo, Key hi) {
        return null;
    }

    private Node<Key, Value> put(Node<Key, Value> x, Key key, Value val) {
        if (x == null) return new Node<>(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        if (!isRed(x.left) && isRed(x.right)) x = rotateLeft(x);
        if (isRed(x.left) && isRed(x.left.left)) x = rotateRight(x);
        if (isRed(x.left) && isRed(x.right)) flipColor(x);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    private boolean isRed(Node<Key, Value> x) {
        if (x == null) return false;
        return x.isRed();
    }

    private Node<Key, Value> rotateLeft(Node<Key, Value> x) {
        Node<Key, Value> t = x;
        x = t.right;
        t.right = x.left;
        x.left = t;
        x.N = t.N;
        t.N = size(t.left) + size(t.right) + 1;
        x.color = t.color;
//        t.color = RED;
        t.setColor(true);
        return x;
    }

    private Node<Key, Value> rotateRight(Node<Key, Value> x) {
        Node<Key, Value> t = x;
        x = t.left;
        t.left = x.right;
        x.right = t;
        x.N = t.N;
        t.N = size(t.left) + size(t.right) + 1;
        x.color = t.color;
//        t.color = RED;
        t.setColor(true);
        return x;
    }

    private void flipColor(Node<Key, Value> x) {
        x.color = !x.color;
        if (x.left != null) x.left.color = !x.left.color;
        if (x.right != null) x.right.color = !x.right.color;
    }

    public static void main(String[] args) {
        RBT<String, String> rbt = new RBT<>();
        String s = StdIn.readString();
        while (s != null) {
            rbt.put(s, s);
            TreeUtil.printTree(rbt);
            System.out.println();
            s = StdIn.readString();
        }
    }

}
