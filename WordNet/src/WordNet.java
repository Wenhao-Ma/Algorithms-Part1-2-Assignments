import java.awt.dnd.InvalidDnDOperationException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.TrieST;

public class WordNet {
	private TreeSet<Noun> synset;
	private TreeSet<ID> idList;
	private Digraph G;
	private SAP sap;
	private int root;
	
	private class Noun implements Comparable<Noun>{
		private String noun;
		private ArrayList<Integer> nounId = new ArrayList<Integer>();
		
		public Noun(String noun){
			this.noun = noun;
		}
		public Noun(String noun, int id) {
			this.noun = noun;
			nounId.add(id);
		}
		public int compareTo(Noun that) {
			return this.noun.compareTo(that.noun);
		}
		public boolean equals(Noun that) {
			if (that == null) return false;
			if (that == this) return true;
			if (that.getClass() != this.getClass()) return false;
			if (that.noun.compareTo(this.noun) == 0) return true;
			else return false;
		}
		public void addId(int id) {
			nounId.add(id);
		}
		public Iterable<Integer> getId() {
			return nounId;
		}
		public String getNoun() {
			return this.noun;
		}
	}
	
	private class ID implements Comparable<ID> {
		private int id;
		private String nouns;
		
		public ID(int id) {
			this.id = id; 
		}
		public ID(int id, String nouns) {
			this.id = id;
			this.nouns = nouns;
		}
		public String getNouns() {
			return nouns;
		}
		public int compareTo(ID that) {
			if (this.id > that.id) return 1;
			else if (this.id < that.id) return -1;
			else return 0;
		}
		public boolean equals(ID that) {
			if (that == null) return false;
			if (that == this) return true;
			if (that.getClass() != this.getClass()) return false;
			if (that.id == this.id) return true;
			else return false;
		}
	}
	
	public WordNet(String synsets, String hypernyms) {
		// constructor takes the name of the two input files
		if (synsets == null || hypernyms == null)
			throw new IllegalArgumentException();
		In insysnets = new In(synsets);
		In inhypernyms = new In(hypernyms);
		int vertex = 0;
		synset = new TreeSet<Noun>();
		idList = new TreeSet<ID>();
		
		String line = insysnets.readLine();
		while (line != null)
		{
			vertex++;
			
			String[] strings = line.split(",");
			int id = Integer.parseInt(strings[0]);
			String[] words = strings[1].split(" ");
			ID idd = new ID(id, strings[1]);
			idList.add(idd);
			for (int i = 0; i < words.length; i++) {
				Noun noun = new Noun(words[i], id);
				if (synset.contains(noun)){
					noun = synset.ceiling(noun);
					synset.remove(noun);
					noun.addId(id);
				}
				synset.add(noun);
			}
			line = insysnets.readLine();
		}
		
		G = new Digraph(vertex);
		boolean[] checkRoot = new boolean[vertex];
		line = inhypernyms.readLine();
		while (line != null)
		{
			String[] strings = line.split(",");
			int bottomId = Integer.parseInt(strings[0]);
			checkRoot[bottomId] = true;
			for (int i = 1; i < strings.length; i++) {
				int rootId = Integer.parseInt(strings[i]);
				G.addEdge(bottomId, rootId);
			}
			line = inhypernyms.readLine();
		}
		
		sap = new SAP(G);
		
		DirectedCycle checkCycle = new DirectedCycle(G);
		if (checkCycle.hasCycle()) throw new IllegalArgumentException();
		int rootnum = 0;
		root = 0;
		for (int i = 0; i < checkRoot.length; i++)
			if (!checkRoot[i]) {rootnum++; root = i;}
		if (rootnum > 1) throw new IllegalArgumentException();
		System.out.println("rootnum = "+rootnum);
	}

	
	public Iterable<String> nouns() {
		// returns all WordNet nouns
		ArrayList<String> nounList = new ArrayList<String>();
		for (Noun noun:synset) 
			nounList.add(noun.getNoun());
		return nounList;
	}

	
	public boolean isNoun(String word) {
		// is the word a WordNet noun?
		if (word == null) throw new IllegalArgumentException();
		return synset.contains(new Noun(word));
	}

	
	public int distance(String nounA, String nounB) {
		// distance between nounA and nounB (defined below)
		if (!isNoun(nounA)) throw new IllegalArgumentException();
		if (!isNoun(nounB)) throw new IllegalArgumentException();
		Noun A = synset.ceiling(new Noun(nounA));
		Noun B = synset.ceiling(new Noun(nounB));
		return sap.length(A.getId(), B.getId());
	}

	
	public String sap(String nounA, String nounB) {
		// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
		// in a shortest ancestral path (defined below)
		if (!isNoun(nounA)) throw new IllegalArgumentException();
		if (!isNoun(nounB)) throw new IllegalArgumentException();
		Noun A = synset.ceiling(new Noun(nounA));
		Noun B = synset.ceiling(new Noun(nounB));
		int anc = sap.ancestor(A.getId(), B.getId());
		ID idd = idList.ceiling(new ID(anc));
		return idd.getNouns();
	}
	
	private Iterable<String> getAllContainedNouns(String noun) {
		if (!isNoun(noun)) throw new IllegalArgumentException();	
		ArrayList<String> string = new ArrayList<String>();
		Noun result = synset.ceiling(new Noun(noun));
		for (int i:result.getId()) {
			ID idd = idList.ceiling(new ID(i));
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append(String.format("%d ", i));
			sBuffer.append(idd.getNouns());
			string.add(sBuffer.toString());
		}
		return string;
	}
	
	private String getNouns(int id) {
		ID idd = idList.ceiling(new ID(id));
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(String.format("%d ", id));
		sBuffer.append(idd.getNouns());
		return sBuffer.toString();
	}
	
	private String getRoot() {
		ID r = idList.ceiling(new ID(root));
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(String.format("%d ", root));
		sBuffer.append(r.getNouns());
		return sBuffer.toString();
	}
	
	// do unit testing of this class
	public static void main(String[] args) {
		
		Stopwatch stopwatch1 = new Stopwatch();
		String s1 = "wordnet/synsets.txt";
		String s2 = "wordnet/hypernyms.txt";
		WordNet wordNet = new WordNet(s1, s2);
		String search = "bird";
		System.out.println("Search:"+search);
		for (String s:wordNet.getAllContainedNouns(search))
			System.out.println(s);
		System.out.println(stopwatch1.elapsedTime());
		System.out.println();
		int line = 200;
		System.out.println("Search line "+line);
		System.out.println(wordNet.getNouns(200));
		System.out.println();
		String st1 = "white_marlin";
		String st2 = "mileage";
		System.out.println("(distance = "+wordNet.distance(st1,st2)
						   +") "+st1+","+st2);
		String ancestor = wordNet.sap(st1,st2);
		System.out.println("ancestor = "+ancestor);
		System.out.println();
		System.out.println(wordNet.getRoot());
		
	}
}
