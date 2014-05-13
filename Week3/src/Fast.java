import java.util.Arrays;

public class Fast {
    public static void main(String[] args)
    {
        String file = args[0];

        In in;
        in = new In(file);
        String n = in.readLine();
        int N = Integer.parseInt(n);
        int count = 0;
        Point[] points = new Point[N];
        while (!in.isEmpty()) {
            int x = Integer.parseInt(in.readString());
            int y = Integer.parseInt(in.readString());
            points[count] = new Point(x, y);
            points[count++].draw();
        }

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        Arrays.sort(points);
        for (int i = 0; i < N - 3; i++) {
            int k = N - i - 1;
            Point[] orderedPoints = new Point[N - 1];
            System.arraycopy(points, 0, orderedPoints, 0, i);
            System.arraycopy(points, i + 1, orderedPoints, i, k);
            Arrays.sort(orderedPoints, points[i].SLOPE_ORDER);

            int start = 0;
            do {
                count = 1;
                double value = points[i].slopeTo(orderedPoints[start]);
                int j = start + 1;
                while (j < N - 1
                        && points[i].slopeTo(orderedPoints[j]) == value) {
                    count++;
                    j++;
                }
                if (count >= 3) {
                    boolean smallest = true;
                    for (int l = start; l < j; l++) {
                        if (points[i].compareTo(orderedPoints[l]) > 0) {
                            smallest = false;
                            break;
                        }
                    }

                    if (smallest) {
                        StdOut.print(points[i]);
                        for (int l = start; l < j; l++) {
                            StdOut.print(" -> " + orderedPoints[l]);
                        }
                        StdOut.println();
                        points[i].drawTo(orderedPoints[j - 1]);
                    }
                }
                start = j;
            } while (start < N - 1);
        }
    }
}
