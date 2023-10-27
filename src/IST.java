public interface IST<Key extends Comparable<Key>, Value> {
    Node<Key, Value> getRoot();

    int size();

    Value get(Key key);

    void put(Key key, Value value);

    Key min();

    Key max();

    Key floor(Key key);

    Key select(int k);

    int rank(Key key);

    void deleteMin();

    void delete(Key key);

    Iterable<Key> keys();

    Iterable<Key> keys(Key lo, Key hi);
}
