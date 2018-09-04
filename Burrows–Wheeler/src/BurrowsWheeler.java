import java.util.PriorityQueue;

import edu.princeton.cs.algs4.Huffman;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
    	//String s = BinaryStdIn.readString();
    	String s = StdIn.readString();
    	CircularSuffixArray csa = new CircularSuffixArray(s);
    	int n = csa.length();
    	StringBuffer stringBuffer = new StringBuffer();
    	for (int i = 0; i < n; i++) {
    		if (csa.index(i) == 0) {
    			StdOut.println(i);
    			//BinaryStdOut.write(i);
    		}
    		stringBuffer.append(s.charAt((csa.index(i)+n-1)%n));
    	}
    	StdOut.println(stringBuffer.toString());
    	//BinaryStdOut.write(stringBuffer.toString());
    	//BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
    	//String s = BinaryStdIn.readString();
    	String s = StdIn.readString();
    	//int fst = BinaryStdIn.readInt();
    	int fst = StdIn.readInt(); 
    	int n = s.length();
    	int[] next = new int[n];
    	PriorityQueue<IndexChar> pq = new PriorityQueue<>();
    	
    	for (int i = 0; i < n; i++) 
    		pq.add(new IndexChar(i, s.charAt(i)));
    	for (int i = 0; i < n; i++)
    		next[i] = pq.remove().i;
    	int i = next[fst];
    	StringBuffer stringBuffer = new StringBuffer();
    	for (int k = 0; k < n; k++) {
    		stringBuffer.append(s.charAt(i));
    		i = next[i];
    	}
    	System.out.println(stringBuffer.toString());
    	//BinaryStdOut.write(stringBuffer.toString());
    	//BinaryStdOut.close();
    }
    
	private static class IndexChar implements Comparable<IndexChar>{
		public int i;
		public char c;
		public IndexChar(int i, char c) {
			this.i = i;
			this.c = c;
		}
		public int compareTo(IndexChar that) {
			// TODO Auto-generated method stub
			if (this.c < that.c) return -1;
			if (this.c > that.c) return 1;
			if (this.c == that.c)
			{
				if (this. i < that.i) return -1;
				if (this. i > that.i) return 1;
			}
			return 0;
		}
	}
	
    /**
 	ABRACADABRA!
 	
 	->
 	3
	ARD!RCAAAABB
	
	
    65,66,82,2,68,1,69,1,4,4,2,38
     */
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//transform();	
		//inverseTransform();	
		Huffman.compress();
	}

}
