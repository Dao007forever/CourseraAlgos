public class CircularSuffixArray {
    private static final int CUTOFF = 10;

    private char[] s;
    private int N;
    private int[] index;

    public CircularSuffixArray(String s) {
        // circular suffix array of s
        this.s = s.toCharArray();
        this.N = s.length();
        index = new int[N];
        for (int i = 0; i < N; i++) {
            index[i] = i;
        }

        sort(0, N - 1, 0);
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) {
        if (d == N) return;
        if (lo >= hi) return;
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int i = lo, j = hi + 1;
        int p = lo, q = hi + 1;
        char v = s[(index[lo] + d) % N];
        while (true) {
            while (s[(index[++i] + d) % N] < v)
                if (i == hi) break;
            while (v < s[(index[--j] + d) % N])
                if (j == lo) break;

            // pointers cross
            if (i == j && s[(index[i] + d) % N] == v) exch(++p, i);

            if (i >= j) break;
            exch(i, j);

            if (s[(index[i] + d) % N] == v) exch(++p, i);
            if (v == s[(index[j] + d) % N]) exch(--q, j);
        }

        i = j + 1;
        for (int k = lo; k <= p; k++)
            exch(k, j--);
        for (int k = hi; k >= q; k--)
            exch(k, i++);

        sort(lo, j, d);
        sort(j + 1, i - 1, d + 1);
        sort(i, hi, d);
    }

    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j - 1], d); j--)
                exch(j, j - 1);
    }

    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        i = (i + d) % N;
        j = (j + d) % N;
        int l = 0;
        while (l < N) {
            if (s[i] < s[j]) return true;
            if (s[i] > s[j]) return false;
            i = (i + 1) % N;
            j = (j + 1) % N;
            l++;
        }
        return i < j;
    }

    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    public int length() {
        // length of s
        return s.length;
    }

    public int index(int i) {
        // returns index of ith sorted suffix
        return index[i];
    }

    public static void main(String[] args) {
        String s = "CADABRA!ABRA";
        String ss = s + s;
        CircularSuffixArray c = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(c.index(i));
            System.out
                    .println(ss.substring(c.index(i), s.length() + c.index(i)));
        }
    }
}