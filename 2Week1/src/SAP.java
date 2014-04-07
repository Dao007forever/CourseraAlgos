import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SAP {
	private final Digraph graph;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph graph) {
		this.graph = new Digraph(graph);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		check(v, w);
		return minDistance(Arrays.asList(v), Arrays.asList(w))[1];
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		check(v, w);
		return minDistance(Arrays.asList(v), Arrays.asList(w))[0];
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
		check(vs, ws);
		return minDistance(vs, ws)[1];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
		check(vs, ws);
		return minDistance(vs, ws)[0];
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}

	private Map<Integer, Integer> bfs(Iterable<Integer> us) {
		Queue<Integer> queue = new Queue<Integer>();
		Map<Integer, Integer> dist = new HashMap<Integer, Integer>();
		for (int u : us) {
			queue.enqueue(u);
			dist.put(u, 0);
		}
		while (!queue.isEmpty()) {
			int u = queue.dequeue();
			int currentDist = dist.get(u);
			for (int v : graph.adj(u)) {
				if (!dist.containsKey(v)) {
					queue.enqueue(v);
					dist.put(v, currentDist + 1);
				}
			}
		}
		return dist;
	}

	private int[] minDistance(Iterable<Integer> vs, Iterable<Integer> ws) {
		Map<Integer, Integer> ancestorV = bfs(vs);
		Map<Integer, Integer> ancestorW = bfs(ws);
		int minDist = Integer.MAX_VALUE;
		int minIndex = -1;
		for (Entry<Integer, Integer> items : ancestorV.entrySet()) {
			int ancestor = items.getKey();
			if (ancestorW.containsKey(ancestor)) {
				int currentDist = ancestorW.get(ancestor) + items.getValue();
				if (currentDist < minDist) {
					minDist = currentDist;
					minIndex = ancestor;
				}
			}
		}
		if (minDist == Integer.MAX_VALUE) {
			return new int[] { -1, -1 };
		}
		return new int[] { minIndex, minDist };
	}

	private void check(int i) {
		if (i < 0 || i > graph.V() - 1) {
			throw new IndexOutOfBoundsException();
		}
	}

	private void check(Iterable<Integer> is) {
		for (int i : is) {
			check(i);
		}
	}

	private void check(int i, int j) {
		check(i);
		check(j);
	}

	private void check(Iterable<Integer> is, Iterable<Integer> js) {
		check(is);
		check(js);
	}
}
