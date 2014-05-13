import java.util.Arrays;


public class Board {
    private static final byte[] dx = new byte[] { -1, 1, 0, 0 };
    private static final byte[] dy = new byte[] { 0, 0, -1, 1 };

    private final byte N;
    private byte xb, yb;
    private final int[][] blocks;
    private int manhattan = -1;

    public Board(int[][] blocks) {
        // construct a board from an N-by-N array of blocks
        N = (byte) blocks.length;
        this.blocks = new int[N][N];

        int i = 0;
        for (int[] block : blocks) {
            this.blocks[i] = new int[N];
            System.arraycopy(block, 0, this.blocks[i++], 0, N);
        }

        findBlank();
    }

    private void findBlank() {
        for (byte i = 0; i < N; i++) {
            for (byte j = 0; j < N; j++)
                if (blocks[i][j] == 0) {
                    this.xb = i;
                    this.yb = j;
                    return;
                }
        }
    }

    public int dimension() {
        // board dimension N
        return N;
    }
    public int hamming() {
        // number of blocks out of place
        int hamming = 0;

        int k = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != k++) {
                    hamming++;
                }
            }
        }

        return --hamming;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        if (manhattan != -1) {
            return manhattan;
        }

        this.manhattan = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0)
                    continue;

                int rx = (blocks[i][j] - 1) / N;
                int ry = (blocks[i][j] - 1) % N;
                manhattan += Math.abs(rx - i) + Math.abs(ry - j);
            }
        }

        return manhattan;
    }

    public boolean isGoal() {
        if (hamming() == 0) {
            return true;
        }

        return false;
    }

    private void swap(int i, int j, int k, int l) {
        int temp = blocks[i][j];
        blocks[i][j] = blocks[k][l];
        blocks[k][l] = temp;
    }

    public Board twin() {
        // a board obtained by exchanging two adjacent blocks in the same row
        int i;
        for (i = 0; i < N; i++)
            if (i != xb) break;

        swap(i, 0, i, 1);
        Board twin = new Board(blocks);
        swap(i, 0, i, 1);

        return twin;
    }

    @Override
    public boolean equals(Object y) {
        // does this board equal y?
        if (y == this) return true;
        if (y instanceof Board) {
            Board b = (Board) y;
            if (b.blocks.length != N) return false;

            for (int i = 0; i < N; i++) {
                if (!Arrays.equals(this.blocks[i], b.blocks[i]))
                    return false;
            }

            return true;
        }
        return false;
    }

    private boolean inside(int i) {
        return i >= 0 && i < N;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        Queue<Board> q = new Queue<Board>();

        for (int i = 0; i < 4; i++) {
            int x = xb + dx[i];
            int y = yb + dy[i];
            if (inside(x) && inside (y)) {
                swap(xb, yb, x, y);
                q.enqueue(new Board(blocks));
                swap(xb, yb, x, y);
            }
        }

        return q;
    }

    @Override
    public String toString() {
        // string representation of the board (in the output format specified
        // below)
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}