import java.awt.Color;

public class SeamCarver {
    private static final int MAX_ENERGY = 195075;

    private Picture picture;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        // current picture
        return picture;
    }

    public int width() {
        // width of current picture
        return picture.width();
    }

    public int height() {
        // height of current picture
        return picture.height();
    }

    private static void check(int x, int dim) {
        if (x < 0 || x >= dim) {
            throw new IndexOutOfBoundsException();
        }
    }

    private static boolean border(int x, int dim) {
        return x == 0 || x == dim - 1;
    }

    private static int sq(int a, int b) {
        return (a - b) * (a - b);
    }

    private static int deltaSq(Color c1, Color c2) {
        return sq(c1.getRed(), c2.getRed())
                + sq(c1.getGreen(), c2.getGreen())
                + sq(c1.getBlue(), c2.getBlue());
    }

    private static double energy(int x, int y, Picture picture) {
        // energy of pixel at column x and row y in current picture
        int w = picture.width(), h = picture.height();
        check(x, w);
        check(y, h);
        if (border(x, w) || border(y, h)) {
            return MAX_ENERGY;
        }
        return deltaSq(picture.get(x - 1, y), picture.get(x + 1, y))
                + deltaSq(picture.get(x, y - 1), picture.get(x, y + 1));
    }

    public double energy(int x, int y) {
        return energy(x, y, picture);
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam in current picture
        return findSeam(picture, true);
    }

    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam in current picture
        return findSeam(picture, false);
    }

    private static double value(double[][] minArr, int r, int c) {
        if (c < 0 || c >= minArr[0].length) {
            return Double.MAX_VALUE;
        }
        return minArr[r][c];
    }

    private static double[] getMin(double[][] minArr, int r, int c) {
        double min = Double.MAX_VALUE;
        int save = r - 1;
        for (int ct = c - 1; ct <= c + 1; ct++) {
            double val = value(minArr, r - 1, ct);
            if (val < min) {
                min = val;
                save = ct;
            }
        }
        return new double[] { min, save };
    }

    private static int[] findSeam(Picture pic, boolean transpose) {
        int w = pic.width(), h = pic.height();
        if (transpose) {
            int t = w;
            w = h;
            h = t;
        }
        double[][] a = new double[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                a[i][j] = transpose ? energy(i, j, pic) : energy(j, i, pic);
            }
        }

        int[][] trace = new int[h][w];
        for (int i = 1; i < h; i++) {
            for (int j = 0; j < w; j++) {
                double[] minPair = getMin(a, i, j);
                a[i][j] = minPair[0] + a[i][j];
                trace[i][j] = (int) minPair[1];
            }
        }
        int curr = 0;
        double min = Double.MAX_VALUE;
        for (int j = 0; j < w; j++) {
            if (a[h - 1][j] < min) {
                min = a[h - 1][j];
                curr = j;
            }
        }

        int[] res = new int[h];
        int i = h - 1;
        while (i > 0) {
            res[i] = curr;
            curr = trace[i--][curr];
        }
        res[i] = curr;
        return res;
    }

    public void removeHorizontalSeam(int[] a) {
        // remove horizontal seam from current picture
        if (height() <= 1) {
            throw new IllegalArgumentException();
        }
        if (a.length != width()) {
            throw new IllegalArgumentException();
        }
        Picture p = new Picture(width(), height() - 1);
        int removedCol = a[0];
        for (int col = 0; col < width(); col++) {
            if (a[col] < removedCol - 1 || a[col] > removedCol + 1) {
                throw new IllegalArgumentException();
            }
            if (a[col] < 0 || a[col] >= height()) {
                throw new IndexOutOfBoundsException();
            }
            removedCol = a[col];
            for (int row = 0; row < height() - 1; row++) {
                if (row < removedCol) {
                    p.set(col, row, picture.get(col, row));
                } else {
                    p.set(col, row, picture.get(col, row + 1));
                }
            }
        }
        picture = p;
    }

    public void removeVerticalSeam(int[] a) {
        // remove vertical seam from current picture
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        if (a.length != height()) {
            throw new IllegalArgumentException();
        }
        Picture p = new Picture(width() - 1, height());
        int removedCol = a[0];
        for (int row = 0; row < height(); row++) {
            if (a[row] < removedCol - 1 || a[row] > removedCol + 1) {
                throw new IllegalArgumentException();
            }
            if (a[row] < 0 || a[row] >= width()) {
                throw new IndexOutOfBoundsException(Integer.toString(a[row]));
            }
            removedCol = a[row];
            for (int col = 0; col < width() - 1; col++) {
                if (col < removedCol) {
                    p.set(col, row, picture.get(col, row));
                } else {
                    p.set(col, row, picture.get(col + 1, row));
                }
            }
        }
        picture = p;
    }
}