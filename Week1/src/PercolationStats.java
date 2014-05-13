

public class PercolationStats {
    private final double mean;
    private final double std;
    private final double lo, hi;

    public PercolationStats(int N, int T) {
        // perform T independent computational experiments on an N-by-N grid
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();

        double[] res = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            int count = 0;
            while (!p.percolates()) {
                int u = StdRandom.uniform(1, N + 1), v = StdRandom.uniform(1,
                        N + 1);
                if (p.isFull(u, v)) {
                    count++;
                    p.open(u, v);
                }
            }
            res[i] = count / (N * 1.0 * N);
        }
        mean = StdStats.mean(res);
        std = StdStats.stddev(res);
        lo = mean - 1.96 * std / Math.sqrt(T);
        hi = mean + 1.96 * std / Math.sqrt(T);
    }

    public double mean() {
        // sample mean of percolation threshold
        return mean;
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return std;
    }

    public double confidenceLo() {
        // returns lower bound of the 95% confidence interval
        return lo;
    }

    public double confidenceHi() {
        // returns upper bound of the 95% confidence interval
        return hi;
    }

    public static void main(String[] args) {
        // test client, described below
        int N = Integer.valueOf(args[0]);
        int T = Integer.valueOf(args[1]);

        PercolationStats ps = new PercolationStats(N, T);

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo() + ", "
                + ps.confidenceHi());
    }
}
