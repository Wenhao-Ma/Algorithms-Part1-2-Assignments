import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	private Digraph G;
	
	public SAP(Digraph G) {
		// constructor takes a digraph (not necessarily a DAG)
		if (G == null) throw new IllegalArgumentException();
		this.G = new Digraph(G);
	}

	public int length(int v, int w) {
		// length of shortest ancestral path between v and w; -1 if no such path
		if (!judge(v, w)) throw new IllegalArgumentException();
		return getResult(v, w)[0];
	}

	public int ancestor(int v, int w) {
		// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
		if (!judge(v, w)) throw new IllegalArgumentException();
		return getResult(v, w)[1];
	}

	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
		if (!judge(v, w)) throw new IllegalArgumentException();
		return getResult(v, w)[0];
	}

	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		// a common ancestor that participates in shortest ancestral path; -1 if no such path
		if (!judge(v, w)) throw new IllegalArgumentException();
		return getResult(v, w)[1];
	}
	
	private int[] getResult(int v, int w) {
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
		int len = Integer.MAX_VALUE;
		int anc = -1;
		for (int i = 0; i < G.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int save = bfsV.distTo(i)+bfsW.distTo(i);
				if (save < len) {
					len = save;
					anc = i;
				}				
			}
		}
		int[] result = new int[2];
		if (anc == -1) result[0] = -1;
		else result[0] = len;
		result[1] = anc;
		return result;
	}
	
	private int[] getResult(Iterable<Integer> v, Iterable<Integer> w) {
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
		int len = Integer.MAX_VALUE;
		int anc = -1;
		for (int i = 0; i < G.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int save = bfsV.distTo(i)+bfsW.distTo(i);
				if (save < len) {
					len = save;
					anc = i;
				}				
			}
		}
		int[] result = new int[2];
		if (anc == -1) result[0] = -1;
		else result[0] = len;
		result[1] = anc;
		return result;
	}
	
	private boolean judge(int v, int w) {
		if (v >= 0 && v < G.V() && w >=0 && w < G.V()) return true;
		else return false;
	}
	
	private boolean judge(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) return false;
		for (int i:v)
			if (i < 0 || i > G.V()-1) return false;
		for (int i:w)
			if (i < 0 || i > G.V()-1) return false;
		return true;
	}
	
	public static void main(String[] args) {
		// do unit testing of this class
	    In in = new In("wordnet/test.txt");
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    System.out.println("ancestor = "+sap.ancestor(1, 2));
	    System.out.println("length = "+sap.length(1, 2));
	}
}
