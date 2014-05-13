import java.util.Comparator;

public class Solver {

    private static final Comparator<SearchNode> cmp = new Comparator<SearchNode>() {
        @Override
        public int compare(SearchNode x, SearchNode y) {
            return x.priority - y.priority;
        }
    };

    private final Stack<Board> solution = new Stack<Board>();

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        SearchNode sol = solve(initial);
        while (sol != null) {
            solution.push(sol.board);
            sol = sol.pre;
        }
    }

    private SearchNode solve(Board initial) {
        MinPQ<SearchNode> minQ = new MinPQ<SearchNode>(cmp);
        MinPQ<SearchNode> twinQ = new MinPQ<SearchNode>(cmp);
        minQ.insert(new SearchNode(initial, null));
        twinQ.insert(new SearchNode(initial.twin(), null));

        while (true) {
            SearchNode least = next(minQ);
            if (least.board.isGoal()) return least;
            if (next(twinQ).board.isGoal()) return null;
        }
    }

    private SearchNode next(MinPQ<SearchNode> pq) {
        SearchNode least = pq.delMin();

        if (least.board.isGoal()) {
            return least;
        }

        for (Board neighbor: least.board.neighbors()) {
            if (least.pre == null || !neighbor.equals(least.pre.board))
                pq.insert(new SearchNode(neighbor, least));
        }
        return least;
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return !solution.isEmpty();
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if no solution
        return solution.size() - 1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if no solution
        if (solution.isEmpty())
            return null;
        return solution;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private static class SearchNode {
        SearchNode pre;
        Board board;
        int moves;
        int priority;

        public SearchNode(Board board, SearchNode pre) {
            this.board = board;
            this.pre = pre;
            if (pre != null)
                this.moves = pre.moves + 1;
            else this.moves = 0;

            this.priority = board.manhattan() + moves;
        }
    }
}
