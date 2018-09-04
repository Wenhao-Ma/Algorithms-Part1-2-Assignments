import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {    
    private int n, num; // n-by-n grid
	private boolean[] open; // the site is open-->true, block-->false
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF uftest;
	
	public Percolation(int n) { // create n-by-n grid, with all sites blocked
		if (n <= 0) throw new IllegalArgumentException("N must be bigger than 0");
		this.n = n;
		uf = new WeightedQuickUnionUF(n*n+2);
		uftest = new WeightedQuickUnionUF(n*n+1);
		open = new boolean[n*n];
		num = 0;
		for (int i = 0; i < n*n; i++) open[i] = false;
	}
	
	public void open(int row, int col) {  // open site (row, col) if it is not open already
	    valid_rc(row, col);    
	    int index = index(row, col);
	    if (open[index]) return;
	    open[index] = true;
	    num++;
	    
	    if (row > 1 && open[index-n]) {
	    	uf.union(index, index-n);
	    	uftest.union(index, index-n);
	    }
	    if (row < n && open[index+n]) {
	    	uf.union(index, index+n);
	    	uftest.union(index, index+n);
	    }
	    if (col > 1 && open[index-1]) {
	    	uf.union(index, index-1);
	    	uftest.union(index, index-1);
	    }
	    if (col < n && open[index+1]) {
	    	uf.union(index, index+1);
	    	uftest.union(index, index+1);
	    }
	    if (row == 1) {
	    	uf.union(index, n*n);
	    	uftest.union(index, n*n);
	    }
	    if (row == n) {
	    	uf.union(index, n*n+1);
	    }
	}
	
	public boolean isOpen(int row, int col) { // is site (row, col) open?
	    valid_rc(row, col);
	    return open[index(row, col)];
	}
	
	public boolean isFull(int row, int col) { // is site (row, col) full?
	    valid_rc(row, col);
	    return uftest.connected(index(row, col), n*n);
	}
	
    public boolean percolates() { // does the system percolate?
        return uf.connected(n*n, n*n+1);
    }
    
    public int numberOfOpenSites() { // number of open sites
    	return num;
    }
	
	private void valid_rc(int row, int col) { // validate that (row, col) is a valid index
		if (row < 1 || row > n || col < 1 || col > n) 
			throw new IllegalArgumentException("Index is outside its prescribed range");
	}
	
	private int index(int row, int col) {
		valid_rc(row, col);
		return col+(row-1)*n-1;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
