import java.util.Arrays;


public class Percolation {
    private static final int[] x = new int[] { -1, 1, 0, 0 };
    private static final int[] y = new int[] { 0, 0, -1, 1 };

    private final int n;
    private final int[][] id;
    private final WeightedQuickUnionUF uf;

    public Percolation(int n) {
        this.n = n;
        id = new int[n+1][n+1];
        for (int[] row : id) {
            Arrays.fill(row, 0);
        }

        uf = new WeightedQuickUnionUF(n * (n + 1) + 1);
    }

    public void open(int i, int j) {
        if (i > n || i < 1)
            throw new IndexOutOfBoundsException();
        if (j > n || j < 1)
            throw new IndexOutOfBoundsException();

        id[i][j] = 1;

        int t = i * n + j;

        if (i == 1) {
            uf.union(0, t);
        }
        if (i == n) {
            uf.union(1, t);
        }

        for (int k = 0; k < 4; k++) {
            int u = i + x[k], v = j + y[k];
            if (u <= n && u >= 1 &&
                    v <= n && v >= 1 &&
                    id[u][v] == 1) {
                uf.union(t, u * n + v);
            }
        }
    }

    public boolean isOpen(int i, int j) {
        if (i > n || i < 1)
            throw new IndexOutOfBoundsException();
        if (j > n || j < 1)
            throw new IndexOutOfBoundsException();

        return (id[i][j] == 1);
    }

    public boolean isFull(int i, int j) {
        if (i > n || i < 1)
            throw new IndexOutOfBoundsException();
        if (j > n || j < 1)
            throw new IndexOutOfBoundsException();

        return uf.connected(i * n + j, 0);
    }

    public boolean percolates() {
        return uf.connected(0, 1);
    }
}
