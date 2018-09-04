import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private boolean solvable, twinsolvable;
	private int movestep;
	private SearchNode goal;
	
    public Solver(Board initial) {
    	// find a solution to the initial board (using the A* algorithm)
    	if (initial == null) {
			throw new IllegalArgumentException();
    	}	
    	solvable = false;
    	twinsolvable = false;
    	
    	MinPQ<SearchNode> gametree = new MinPQ<SearchNode>();
    	MinPQ<SearchNode> twingametree = new MinPQ<SearchNode>();
    	
    	SearchNode search = new SearchNode(null, initial, 0, initial.manhattan());
    	SearchNode twinsearch = new SearchNode(null, initial.twin(), 0, initial.twin().manhattan());
    	
    	gametree.insert(search);
    	twingametree.insert(twinsearch);
    	
    	while (!solvable && !twinsolvable)
    	{
        	SearchNode sn = gametree.delMin();
        	movestep = sn.moves;
        	SearchNode tsn = twingametree.delMin();
        	Board step = sn.now;
         	Board tstep = tsn.now;

        	if (tstep.isGoal()) {twinsolvable = true;return;}    	
          	if (step.isGoal())
          	{
          		solvable = true;
          		goal = sn;
          		return;
          	}   	
        	for (Board neighbors:step.neighbors()) 
        	{
        		if (sn.pre != null && neighbors.equals(sn.pre.now)) continue;
        		SearchNode newNode = new SearchNode(sn, neighbors, sn.moves+1, neighbors.manhattan());
        		gametree.insert(newNode);
        	}
        	for (Board tneighbors:tstep.neighbors())
        	{
        		if (sn.pre != null && tneighbors.equals(tsn.pre.now)) continue;
        		SearchNode newTwinNode = new SearchNode(tsn, tneighbors, tsn.moves+1, tneighbors.manhattan());
                twingametree.insert(newTwinNode);
        	}
    	}
    }
    
	private class SearchNode implements Comparable<SearchNode> {
		private SearchNode pre;
		private Board now;
		private int moves;
		private int man;
		public SearchNode(SearchNode pre, Board now, int moves, int man) {
			this.pre = pre;
			this.now = now;
			this.moves = moves;
			this.man = man;
		}
		public int priority() {
			return moves+man;
		}
		@Override
		public int compareTo(SearchNode that) {
			if (this.priority() < that.priority()) return -1;
			else if (this.priority() > that.priority()) return 1;
			else return 0;
		}
	}
    
    public boolean isSolvable() {
    	// is the initial board solvable?
    	return solvable;
    }
    public int moves() {
    	// min number of moves to solve initial board; -1 if unsolvable
    	if (isSolvable()) return movestep;
    	else return -1;
    }
    public Iterable<Board> solution() {
    	// sequence of boards in a shortest solution; null if unsolvable
    	if (isSolvable())
    	{
    		ArrayList<Board> solution = new ArrayList<Board>();
    		SearchNode temp = goal;
    		while (temp != null)
    		{
    			solution.add(temp.now);
    			temp = temp.pre;
    		}
    		for (int i = 0, j = solution.size()-1; i < j; i++, j--){
    			Board save = solution.get(j);
    			solution.set(j, solution.get(i));
    			solution.set(i, save);
    		}
    		return solution;
    	}
    	else return null;
    }
    public static void main(String[] args) {
    	// solve a slider puzzle (given below)
//    	/**
        In in = new In("8puzzle/puzzle3x3-05.txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
//       */
    	/**
        int[][] blocks =  {
				 {1,2,3},
				 {4,6,8},
				 {7,5,0}
				};
    	*/
//        /**
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
 //       */
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
}
