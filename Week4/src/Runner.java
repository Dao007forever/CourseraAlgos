
public class Runner {
    public static void main(String args[]) {
        int[] a = new int[] { 28, 14, 60, 26, 41, 73, 35, 57, 68, 97, 81, 92 };

        BST<Integer, Integer> bst = new BST<Integer, Integer>();

        for (int i : a)
            bst.put(i, i);

        bst.delete(26);
        bst.delete(97);
        bst.delete(60);
        for (int i : bst.levelOrder())
            System.out.print(i + " ");
    }
}
