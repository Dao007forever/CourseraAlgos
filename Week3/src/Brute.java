import java.util.Arrays;

public class Brute {
    public static void main(String[] args) {
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
            for (int j = i + 1; j < N - 2; j++) {
                for (int k = j + 1; k < N - 1; k++) {
                    for (int l = k + 1; l < N; l++) {
                        double s1 = points[i].slopeTo(points[j]);
                        double s2 = points[i].slopeTo(points[k]);
                        double s3 = points[i].slopeTo(points[l]);

                        if (s1 == s2 && s1 == s3) {
                            StdOut.print(points[i] + " -> ");
                            StdOut.print(points[j] + " -> ");
                            StdOut.print(points[k] + " -> ");
                            StdOut.println(points[l]);

                            points[i].drawTo(points[l]);
                        }
                    }
                }
            }
        }
    }
}
