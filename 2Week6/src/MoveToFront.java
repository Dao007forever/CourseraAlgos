public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        char[] current = initCurrent();
        char[] indexOf = initCurrent();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(moveToFront(current, indexOf, indexOf[c]));
        }
        BinaryStdOut.close();
    }

    private static char[] initCurrent() {
        char[] current = new char[R];
        for (char i = 0; i < R; i++) {
            current[i] = i;
        }
        return current;
    }

    private static char moveToFront(char[] current, char[] indexOf, char index) {
        char c = current[index];
        for (char i = index; i > 0; i--) {
            current[i] = current[i - 1];
            if (indexOf != null) {
                indexOf[current[i]] = i;
            }
        }
        current[0] = c;
        if (indexOf != null) {
            indexOf[c] = 0;
        }
        return index;
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        char[] current = initCurrent();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(current[c]);
            moveToFront(current, null, c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException("Illegal command line argument");
    }
}