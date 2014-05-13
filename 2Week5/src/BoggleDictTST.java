public class BoggleDictTST {
    private Node root;
    // cache the last valid prefix query
    private String cachePrefix;
    private Node cacheNode;

    private class Node {
        private char c; // character
        private Node left, mid, right; // left, middle, and right subtries
        private int val; // value associated with string
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public Integer get(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0)
            throw new IllegalArgumentException("key must have length >= 1");

        Object[] getCache = getCache(key);
        Node startNode = (Node) getCache[0];
        int startPos = (Integer) getCache[1];
        Node x = get(startNode, key, startPos);
        if (x == null) return null;
        updateCache(key, x);
        return x.val;
    }

    // return if the prefix is a prefix of some strings
    public boolean prefix(String prefix) {
        Object[] getCache = getCache(prefix);
        Node startNode = (Node) getCache[0];
        int startPos = (Integer) getCache[1];

        if (prefix == null) throw new NullPointerException();
        if (prefix.length() == 0) {
            // empty prefix
            return root != null;
        }

        Node x = get(startNode, prefix, startPos);
        updateCache(prefix, x);
        return x != null;
    }

    private Object[] getCache(String key) {
        Node startNode = root;
        int startPos = 0;
        if (cachePrefix != null && key.startsWith(cachePrefix)) {
            startNode = cacheNode;
            startPos = cachePrefix.length() - 1;
        }

        return new Object[] { startNode, startPos };
    }

    private void updateCache(String key, Node x) {
        if (x != null) {
            cachePrefix = key;
            cacheNode = x;
        }
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0)
            throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if (c < x.c)
            return get(x.left, key, d);
        else if (c > x.c)
            return get(x.right, key, d);
        else if (d < key.length() - 1)
            return get(x.mid, key, d + 1);
        else
            return x;
    }

    public void put(String s, int val) {
        root = put(root, s, val, 0);
    }

    private Node put(Node x, String s, int val, int d) {
        char c = s.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c)
            x.left = put(x.left, s, val, d);
        else if (c > x.c)
            x.right = put(x.right, s, val, d);
        else if (d < s.length() - 1)
            x.mid = put(x.mid, s, val, d + 1);
        else
            x.val = val;
        return x;
    }
}
