import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;

public class Board {
	private int[][] blocks;
	private int X, Y;
	// the position of blank place
	
    public Board(int[][] blocks) {
    	// construct a board from an n-by-n array of blocks
    	int len = blocks.length;
    	this.blocks = new int[len][len];
    	for (int i = 0; i < len; i++)
    		for (int j = 0; j < len; j++) 
    		{
    			this.blocks[i][j] = blocks[i][j];
    			if (blocks[i][j] == 0) {
					X = i;
					Y = j;
				}
    		}
    	
    }
    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
    	// board dimension n
    	return blocks.length;
    }
    public int hamming() {
    	// number of blocks out of place
    	int n = 0;
    	for (int i = 0; i < dimension(); i++)
    		for (int j = 0; j < dimension(); j++)
    			if (blocks[i][j] != 0 && blocks[i][j] != i*dimension()+j+1) n++;
    	return n;
    }
    public int manhattan() {
    	// sum of Manhattan distances between blocks and goal
    	int n = 0;
    	for (int i = 0; i < dimension(); i++)
    		for (int j = 0; j < dimension(); j++)
    			if (blocks[i][j] != 0){
    				int i1 = (blocks[i][j]-1)/dimension();
    				int j1 = (blocks[i][j]-1)%dimension();
    				n += Math.abs(i1-i)+Math.abs(j1-j);
    			}
    	return n;
    }
    public boolean isGoal() {
    	// is this board the goal board?
    	for (int i = 0; i < dimension(); i++)
    		for (int j = 0; j < dimension(); j++)
    			if (blocks[i][j] != 0 && blocks[i][j] != i*dimension()+j+1)
    				return false;
    	return true;
    }
    
    private int[][] exch(int i1, int j1, int i2, int j2) {
    	int save = blocks[i1][j1];
    	blocks[i1][j1] = blocks[i2][j2];
    	blocks[i2][j2] = save;
    	return blocks;
    }
    
    public Board twin() {
    	// a board that is obtained by exchanging any pair of blocks
    	Board twin;
    	if (X == 0) {
			twin = new Board(exch(1, 0, 1, 1));
			exch(1, 0, 1, 1);
			return twin;
		}		
    	else {
			twin = new Board(exch(0, 0, 0, 1));
			exch(0, 0, 0, 1);  
			return twin;
    	}
    }
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	if (y == this) return true;
    	if (y == null) return false;
    	if (y.getClass() != this.getClass()) return false;
    	Board cmp = (Board) y;
    	if (cmp.dimension() != this.dimension()) return false;
    	for (int i = 0; i < dimension(); i++)
    		for (int j = 0; j < dimension(); j++)
    			if (this.blocks[i][j] != cmp.blocks[i][j]) return false;
    	return true;
    }
    
    public Iterable<Board> neighbors() {
    	// all neighboring boards
        ArrayList<Board> neighbors = new ArrayList<Board>();;
    	
    	int[] PosX = {1,-1,0,0};
    	int[] PosY = {0,0,1,-1};
    	for (int i = 0; i < 4; i++)
    	{
    		int nX = X+PosX[i];
    		int nY = Y+PosY[i];
    		if (nX >= 0 && nX < dimension() && nY >= 0 && nY < dimension()) {
				neighbors.add(new Board(exch(X, Y, nX, nY)));
				exch(X, Y, nX, nY);
			}
    	}
    	return neighbors;
    }

    
    public String toString() {
    	// string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] a = {
					 {1,2,3},
					 {4,8,5},
					 {6,7,0}
					};
		int[][] b = {
					 {0,2,3},
				 	 {4,1,5},
				 	 {6,7,8}				
					};
		Board ba = new Board(a);
		Board bb = new Board(b);
		Board tBoard = bb.twin();
		System.out.println(bb);
		System.out.println(tBoard);
		System.out.println(bb);
	}
}
