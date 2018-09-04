import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private WordNet wordNet;
	
	public Outcast(WordNet wordnet) {
		// constructor takes a WordNet object
		this.wordNet = wordnet;
	}
	public String outcast(String[] nouns) {
		// given an array of WordNet nouns, return an outcast
		int n = nouns.length;
		int[] distance = new int[n];
		for (int i = 0; i < n; i++)
			for (int j = i+1; j < n; j++){
				int dis = wordNet.distance(nouns[i], nouns[j]);
				distance[i]+=dis;
				distance[j]+=dis;
			}
		int max = 0;
		String outest = null;
		for (int i = 0; i < n; i++)
			if (distance[i] > max){
				max = distance[i];
				outest = nouns[i];
			}
		return outest;
	}
	public static void main(String[] args) {
		// see test client below
		String s1 = "wordnet/synsets.txt";
		String s2 = "wordnet/hypernyms.txt";
	    WordNet wordnet = new WordNet(s1, s2);
	    Outcast outcast = new Outcast(wordnet);
	    In in = new In("wordnet/outcast11.txt");
	    String[] nouns = in.readAllStrings();
	    for (String n:nouns) System.out.println(n);
	    System.out.println("Outcast: "+outcast.outcast(nouns));
	}
}
