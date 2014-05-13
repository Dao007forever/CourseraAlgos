import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final int n;
    private final Team[] teams;
    private final Map<String, Integer> name2Idx;
    private FordFulkerson ff;
    private int cert;
    private int cachedIdx;
    private boolean cachedEliminated;

    private class Team {
        public final String name;
        public final int wins;
        public final int losses;
        public final int remain;
        public final int matches[];

        public Team(int n, In in) {
            this.name = in.readString();
            this.wins = in.readInt();
            this.losses = in.readInt();
            this.remain = in.readInt();

            this.matches = new int[n];
            for (int i = 0; i < n; i++) {
                matches[i] = in.readInt();
            }
        }
    }

    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified
        // below
        In in = new In(filename);
        this.n = in.readInt();
        this.name2Idx = new HashMap<>();

        this.teams = new Team[n];
        for (int i = 0; i < n; i++) {
            teams[i] = new Team(n, in);
            name2Idx.put(teams[i].name, i);
        }

        this.cachedIdx = -1;
    }

    public int numberOfTeams() {
        // number of teams
        return n;
    }

    public Iterable<String> teams() {
        // all teams
        return name2Idx.keySet();
    }

    private int check(String team) {
        Integer idx = name2Idx.get(team);
        if (idx == null) {
            throw new IllegalArgumentException();
        }

        return idx.intValue();
    }

    public int wins(String team) {
        // number of wins for given team
        int idx = check(team);
        return teams[idx].wins;
    }

    public int losses(String team) {
        // number of losses for given team
        int idx = check(team);
        return teams[idx].losses;
    }

    public int remaining(String team) {
        // number of remaining games for given team
        int idx = check(team);
        return teams[idx].remain;
    }

    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        int idx1 = check(team1);
        int idx2 = check(team2);
        return teams[idx1].matches[idx2];
    }

    public boolean isEliminated(String team) {
        // is given team eliminated?
        int idx = check(team);
        if (cachedIdx == idx) {
            return cachedEliminated;
        }
        cachedIdx = idx;
        cert = -1;
        int maxWin = teams[idx].wins + teams[idx].remain;
        List<FlowEdge> edges = new ArrayList<>();
        int remainingMatches = 0;
        // s = n, t = n+1
        int source = n, sink = n + 1;
        int node = n + 2;
        for (int i = 0; i < n; i++) {
            if (i == idx) {
                continue;
            }
            if (maxWin - teams[i].wins < 0) {
                cert = i;
                cachedEliminated = true;
                return true;
            }
            edges.add(new FlowEdge(i, sink, maxWin - teams[i].wins));
            for (int j = i + 1; j < n; j++) {
                if (j == idx) {
                    continue;
                }
                if (teams[i].matches[j] != 0) {
                    edges.add(new FlowEdge(source, node, teams[i].matches[j]));
                    edges.add(new FlowEdge(node, i, Double.POSITIVE_INFINITY));
                    edges.add(new FlowEdge(node++, j, Double.POSITIVE_INFINITY));

                    remainingMatches += teams[i].matches[j];
                }
            }
        }

        FlowNetwork fn = new FlowNetwork(node);
        for (FlowEdge edge : edges) {
            fn.addEdge(edge);
        }

        ff = new FordFulkerson(fn, source, sink);
        cachedEliminated = remainingMatches != ff.value();
        return cachedEliminated;
    }

    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        int idx = check(team);
        if (idx != cachedIdx) {
            isEliminated(team);
        }
        if (!cachedEliminated) {
            return null;
        }
        if (cert != -1) {
            return Arrays.asList(teams[cert].name);
        }
        List<String> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (i == idx) {
                continue;
            }
            if (ff.inCut(i)) {
                res.add(teams[i].name);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
