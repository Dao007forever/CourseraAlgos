public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        int length = nouns.length;
        int maxVal = Integer.MAX_VALUE / length;
        int[][] distances = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                int temp = wordNet.distance(nouns[i], nouns[j]);
                int dist = temp == -1 ? maxVal : temp;
                distances[i][j] = dist;
                distances[j][i] = dist;
            }
        }
        int max = Integer.MIN_VALUE;
        String maxNoun = null;
        for (int i = 0; i < length; i++) {
            int sum = 0;
            for (int j = 0; j < length; j++) {
                sum += distances[i][j];
            }
            if (sum > max) {
                max = sum;
                maxNoun = nouns[i];
            }
        }
        return maxNoun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}