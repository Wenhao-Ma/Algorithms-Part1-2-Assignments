
import java.util.TreeSet;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver {
	private TreeSet<String> words;
	private BoggleTrie<Integer> dict;
	
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	dict = new BoggleTrie<Integer>();
    	int n = dictionary.length;
    	for (int i = 0; i < n; i++)
    		dict.put(dictionary[i], 1);
    }

    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	words = new TreeSet<String>();
    	boolean[][] marked = new boolean[board.rows()][board.cols()];
    	for (int x = 0; x < board.rows(); x++)
    		for (int y = 0; y < board.cols(); y++)
    		{
    			BoggleTrie.Node node = dict.root();
    			if (node != null)
    				dfs(board, x, y, "", marked, node);
    		}
    	return words;
    }

    private void dfs(BoggleBoard B, int x, int y, String prefix, boolean[][] marked, BoggleTrie.Node node) {
    	char c = B.getLetter(x, y);
        StringBuilder sB = new StringBuilder();
        sB.append(prefix+c);
        BoggleTrie.Node node2 = dict.getnext(node, c);     
        if (c == 'Q') {
        	sB.append('U'); 
        	node2 = dict.getnext(node2, 'U');
        }    
        prefix = sB.toString();        
        if (node2 == null) return;
        marked[x][y] = true;  
        if (node2.val != null) 
        	if (!words.contains(prefix) && prefix.length() > 2)
        		words.add(prefix);
        for (Integer[] w : adj(B, x, y)) 
            if (!marked[w[0]][w[1]]) {
            	dfs(B, w[0], w[1], prefix, marked, node2);
            }     
        marked[x][y] = false;
    }
    
    private Iterable<Integer[]> adj(BoggleBoard B, int x, int y) {
    	Bag<Integer[]> bag = new Bag<Integer[]>();
    	int X = B.rows(), Y = B.cols();
    	for (int i = x-1<0?0:x-1; i <= x+1 && i < X; i++)
    		for (int j = y-1<0?0:y-1; j <= y+1 && j < Y; j++) {
    			if (i == x && j == y) continue;
    			Integer[] a = new Integer[2];
    			a[0] = i; a[1] = j;
    			bag.add(a);
    		}
    	return bag;
    }
    
    private BoggleTrie<Integer> getdict() {
    	return dict;
    }
    /**
     * word length   points
     *    0-2          0
     *    3-4          1
     *     5           2
     *     6           3
     *     7           5
     *     8+          11
     */   
   
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)

    public int scoreOf(String word) {
    	if (!dict.contains(word)) return 0;
    	int l = word.length();
    	if 		(l <= 2) return 0;
    	else if (l <= 4) return 1;
    	else if (l == 5) return 2;
    	else if (l == 6) return 3;
    	else if (l == 7) return 5;
    	else 			 return 11;
    }
    
    
    /**
     * O  O  L  U  
         A  E  W  G  
         W  N  A  S  
         E  D  T  Qu
     * @param args
     */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		In in = new In("boggle/dictionary-algs4.txt");
		String[] dict = in.readAllStrings();
		char[][] c = new char[][]{{'Q', 'O', 'L', 'U'},  
							  	{'A', 'Q', 'W', 'G'},  
							  {'W', 'N', 'A', 'S'},  
							  {'E', 'D', 'T', 'Q'}};
		char[][] d = new char[][]{{'Q','E','S','T','I','O','N'}};
		char[][] e = new char[][]{
			{'S', 'R','I'},  
	         {'T','A','Y'},  
	         {'O','A','E'}
		};
		BoggleBoard board = new BoggleBoard("boggle/board-q.txt");
		System.out.println(board);
		System.out.println();
		
		
		BoggleSolver solver = new BoggleSolver(dict);	
		int n = 0;
	    int score = 0;
	    Stopwatch stopwatch = new Stopwatch();
	    for (String word : solver.getAllValidWords(board)) {
	        StdOut.println(word);
	        n++;
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	    System.out.println("n = "+n);
	    System.out.println("time ="+stopwatch.elapsedTime());
	    double t = n/stopwatch.elapsedTime();
	    System.out.println("n/time = "+t);
	}
}