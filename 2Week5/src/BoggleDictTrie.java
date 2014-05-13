public class BoggleDictTrie {
    private static final int R = 26;

    private Node root;
    // cache the last valid prefix query
    private String cachePrefix;
    private Node cacheNode;

    private static class Node {
        private int val;
        private Node[] next = new Node[R];
    }

    /****************************************************
     * Is the key in the symbol table?
     ****************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public Integer get(String key) {
        Object[] getCache = getCache(key);
        Node startNode = (Node) getCache[0];
        int startPos = (Integer) getCache[1];

        Node x = get(startNode, key, startPos);
        updateCache(key, x);
        if (x == null) return null;
        return x.val;
    }

    private Object[] getCache(String key) {
        Node startNode = root;
        Integer startPos = 0;
        if (cachePrefix != null && key.startsWith(cachePrefix)) {
            startNode = cacheNode;
            startPos = cachePrefix.length();
        }
        return new Object[] { startNode, startPos };
    }

    private void updateCache(String key, Node x) {
        if (x != null) {
            cachePrefix = key;
            cacheNode = x;
        }
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = indexOf(key, d);
        return get(x.next[c], key, d + 1);
    }

    /****************************************************
     * Insert key-value pair into the symbol table.
     ****************************************************/
    public void put(String key, int val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        int c = indexOf(key, d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    private int indexOf(String key, int pos) {
        return key.charAt(pos) - 'A';
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
}
