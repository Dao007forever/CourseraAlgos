public class BoggleDict {
    private static final int R = 26;

    private Node root;
    private int markedFound = 1;

    public static class Node {
        private Object val;
        private int found;
        private Node[] next = new Node[R];

        public Node getChild(String key) {
            int index = BoggleDict.indexOf(key.charAt(0));
            if (key.length() == 1) {
                return next[index];
            }
            if (next[index] != null) {
                return next[index].getChild(key.substring(1));
            }
            return null;
        }

        public Object getVal() {
            return val;
        }
    }

    public void clearFound() {
        // TODO: deal with overflow, make `found` field of all Nodes becomes 0
        markedFound++;
    }

    public Node getRoot() {
        return root;
    }

    /****************************************************
     * Is the key in the symbol table?
     ****************************************************/
    public boolean contains(CharSequence key) {
        return get(key) != null;
    }

    public Integer get(CharSequence key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Integer) x.val;
    }

    public Node get(CharSequence key, Node startNode, int startPos) {
        Node x = get(startNode, key, startPos);
        return x;
    }

    public void markFound(Node x) {
        x.found = markedFound;
    }

    public boolean isFound(Node x) {
        return x.found == markedFound;
    }

    private Node get(Node x, CharSequence key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = indexOf(key, d);
        return get(x.next[c], key, d + 1);
    }

    /****************************************************
     * Insert key-value pair into the symbol table.
     ****************************************************/
    public void put(CharSequence key, int val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, CharSequence key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        int c = indexOf(key, d);
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    public static int indexOf(CharSequence key, int pos) {
        return key.charAt(pos) - 'A';
    }

    public static int indexOf(char c) {
        return c - 'A';
    }

    // return if the prefix is a prefix of some strings
    public boolean prefix(CharSequence prefix, Node startNode, int startPos) {
        if (prefix == null) throw new NullPointerException();
        if (prefix.length() == 0) {
            // empty prefix
            return root != null;
        }

        Node x = get(startNode, prefix, startPos);
        return x != null;
    }
}
