import java.util.Arrays;

public class CircularSuffixArray {
	private String s;
	private int[] index;
	
	private class Suffix implements Comparable<Suffix>{
		public int i;
		public Suffix(int i) {
			this.i = i;
		}
		public int compareTo(Suffix that) {
			// TODO Auto-generated method stub
			int n = length();
			for (int k = 0; k < n; k++) {
				if (s.charAt((this.i+k)%n) > s.charAt((that.i+k)%n)) return 1;
				if (s.charAt((this.i+k)%n) < s.charAt((that.i+k)%n)) return -1;
			}
			return 0;
		}
	}
	
	public CircularSuffixArray(String s) {
		// circular suffix array of s
		this.s = s;
		Suffix[] suffix = new Suffix[s.length()];
		for (int i = 0; i < s.length(); i++) 
			suffix[i] = new Suffix(i);
		index = new int[s.length()];
		
		Arrays.sort(suffix);
		for (int i = 0; i < s.length(); i++)
			index[i] = suffix[i].i;
	}
	public int length() {
		// length of s
		return s.length();
	}
	public int index(int i) {
		// returns index of ith sorted suffix
		return index[i];
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "ABRACADABRA!";
		System.out.println(s);
		CircularSuffixArray c = new CircularSuffixArray(s);
		System.out.println("length = "+c.length());
		for (int i = 0; i < c.length(); i++)
			System.out.println("index["+i+"] = "+c.index(i));
	}
}
