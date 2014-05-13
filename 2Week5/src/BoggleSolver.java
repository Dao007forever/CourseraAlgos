import java.util.ArrayList;
import java.util.Collection;

public class BoggleSolver {
    private static final int[] SCORE = new int[] { 0, 0, 0, 1, 1, 2, 3, 5, 11 };
    private static final int[] XD = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] YD = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };

    private BoggleDict tst;
    private BoggleBoard board;
    private boolean[][] visited;
    private Collection<String> result;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.tst = new BoggleDict();
        for (String s : dictionary) {
            tst.put(s, scoreOf(s.length()));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an
    // Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == this.board) {
            return result;
        }

        this.board = board;
        int r = board.rows(), c = board.cols();
        visited = new boolean[r][c];
        result = new ArrayList<>();
        tst.clearFound();

        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++) {
                String start = getLetter(i, j);
                dfs(i, j, start, tst.getRoot().getChild(start), start.length());
            }

        return result;
    }

    private void dfs(int x, int y, String current, BoggleDict.Node currentNode,
            int pos) {
        if (currentNode == null) {
            return;
        }

        visited[x][y] = true;
        if (tst.prefix(current, currentNode, pos)) {
            if (current.length() > 2) {
                BoggleDict.Node node = tst.get(current, currentNode, pos);
                if (node != null && node.getVal() != null && !tst.isFound(node)) {
                    result.add(current);
                    tst.markFound(node);
                }
            }

            for (int i = 0; i < XD.length; i++) {
                int xt = x + XD[i], yt = y + YD[i];
                if (check(xt, yt) && !visited[xt][yt]) {
                    String next = getLetter(xt, yt);
                    dfs(xt, yt, current + next, currentNode.getChild(next), pos
                            + next.length());
                }
            }
        }
        visited[x][y] = false;
    }

    private String getLetter(int x, int y) {
        char letter = board.getLetter(x, y);
        if (letter == 'Q') {
            return "QU";
        } else {
            return String.valueOf(letter);
        }
    }

    private boolean check(int x, int y) {
        return x >= 0 && x < board.rows() && y >= 0 && y < board.cols();
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through
    // Z.)
    public int scoreOf(String word) {
        Integer res = tst.get(word);
        return res != null ? res : 0;
    }

    private int scoreOf(int length) {
        return SCORE[Math.min(length, SCORE.length - 1)];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}