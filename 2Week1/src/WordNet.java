import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WordNet {
    private final SAP sap;
	private final Map<String, Set<Integer>> noun2ids;
    private final Map<Integer, String> id2synset;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
		noun2ids = new HashMap<>();
		id2synset = new HashMap<>();
        readSynsets(synsets);
        sap = new SAP(readHypernyms(hypernyms, id2synset.size()));
    }

    private void readSynsets(String filename) {
        In file = new In(filename);
        while (!file.isEmpty()) {
            String[] line = file.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String[] nouns = line[1].split(" ");

            id2synset.put(id, line[1]);
            for (String noun : nouns) {
				Set<Integer> bag = noun2ids.get(noun);
                if (bag == null) {
					bag = new HashSet<>();
                    noun2ids.put(noun, bag);
                }
                bag.add(id);
            }
        }
    }

    private Digraph readHypernyms(String filename, int size) {
        Digraph graph = new Digraph(size);
        In file = new In(filename);
        while (!file.isEmpty()) {
            String[] line = file.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(id, Integer.parseInt(line[i]));
            }
        }
        checkCycles(graph);
        checkOneRoot(graph);
        return graph;
    }

    private void checkCycles(Digraph graph) {
        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOneRoot(Digraph graph) {
        int numRoots = 0;
        // a root has no out-degree = 0
        for (int vertex = 0; vertex < graph.V(); vertex++) {
			if (!graph.adj(vertex).iterator().hasNext()) {
				numRoots++;
			}
		}
        if (numRoots != 1) {
			throw new IllegalArgumentException();
		}
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return noun2ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return noun2ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkBothNouns(nounA, nounB);
        return sap.length(noun2ids.get(nounA), noun2ids.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkBothNouns(nounA, nounB);
        return id2synset.get(sap.ancestor(noun2ids.get(nounA),
                noun2ids.get(nounB)));
    }

    public static void main(String[] args) {
		WordNet wn = new WordNet(args[0], args[1]);
		List<String> nouns = new ArrayList<>();
		for (String noun : wn.nouns()) {
			nouns.add(noun);
		}
		int size = nouns.size();
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			String v = nouns.get(r.nextInt(size));
			String w = nouns.get(r.nextInt(size));

			int distance = wn.distance(v, w);
			String ancestor = wn.sap(v, w).split(" ")[0];
			int d1 = wn.distance(v, ancestor);
			int d2 = wn.distance(w, ancestor);
			if (d1 + d2 != distance) {
				System.err.println("Error for " + v + " and " + w);
			}
		}
    }

    private void checkBothNouns(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
    }
}
