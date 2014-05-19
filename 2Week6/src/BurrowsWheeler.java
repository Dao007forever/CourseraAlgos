public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing
    // to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int first = 0;
        for (int i = 0; i < s.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) - 1 + s.length())
                    % s.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing
    // to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }

        char[] sorted = new char[sb.length()];
        int[] count = keyIndexCount(sb, sorted);

        int[] next = new int[sb.length()];
        for (int i = 0; i < sb.length(); i++) {
            next[count[sb.charAt(i)]++] = i;
        }

        int current = first;
        for (int i = 0; i < sb.length(); i++) {
            BinaryStdOut.write(sorted[current]);
            current = next[current];
        }

        BinaryStdOut.close();
    }

    private static int[] keyIndexCount(CharSequence cs, char[] sorted) {
        int[] count = new int[R + 1];
        int[] res = new int[R + 1];
        for (int i = 0; i < cs.length(); i++) {
            count[cs.charAt(i) + 1]++;
        }

        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }

        System.arraycopy(count, 0, res, 0, R + 1);

        char[] aux = new char[sorted.length];
        for (int i = 0; i < cs.length(); i++)
            aux[count[cs.charAt(i)]++] = cs.charAt(i);

        System.arraycopy(aux, 0, sorted, 0, cs.length());

        return res;
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException("Illegal command line argument");
    }
}