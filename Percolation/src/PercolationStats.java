import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] x;
	private int t;
	private double mean, stddev;
	
    public PercolationStats(int n, int trials) {
    	// perform trials independent experiments on an n-by-n grid
    	if (n <= 0 || trials <= 0)
    		throw new IllegalArgumentException("n and trial must be bigger than 0");
    	
    	t = trials;
    	x = new double[trials];
    	for (int i = 0; i < trials; i++) {
    		Percolation pe = new Percolation(n);
    		int r, c;
    		while (true) {
    			do {
					r = StdRandom.uniform(n)+1;
					c = StdRandom.uniform(n)+1;
				} while (pe.isOpen(r, c));
    			pe.open(r, c);
    			if (pe.percolates()) {
    				break;
    			}
    		}
    		x[i] = pe.numberOfOpenSites()/(double) (n*n);
    	}
    	 mean = StdStats.mean(x);
    	 stddev = StdStats.stddev(x);   	
    }
    
    public double mean() {
    	// sample mean of percolation threshold
    	return mean;
    }
    
    public double stddev() {
    	// sample standard deviation of percolation threshold
    	return stddev;
    }
    
    public double confidenceLo() {
    	// low  endpoint of 95% confidence interval
    	return mean-1.96*stddev/Math.sqrt(t);
    }
    
    public double confidenceHi() {
    	// high endpoint of 95% confidence interval    
    	return mean+1.96*stddev/Math.sqrt(t);
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n, trials;
		n = StdIn.readInt();
		trials = StdIn.readInt();
		PercolationStats pStats1 = new PercolationStats(n, trials);
		StdOut.println("%java PercolationStats "+n+" "+trials);
		StdOut.println("mean                    ="+pStats1.mean());
		StdOut.println("stddev                  ="+pStats1.stddev());
		StdOut.println("95% confidence interval =["+pStats1.confidenceLo()+","+pStats1.confidenceHi()+"]");
	}

}
