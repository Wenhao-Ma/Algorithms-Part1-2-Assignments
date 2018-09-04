import edu.princeton.cs.algs4.Huffman;
import edu.princeton.cs.algs4.StdIn;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	String s = StdIn.readString();
    	int n = s.length();
    	
    	int[] mtf = new int[256];
    	for (int i = 0; i < 256; i++)
    		mtf[i] = i;
    	
    	for (int i = 0; i < n; i++) {
    		int place = 0;
    		for (int j = 0; j < 256; j++)
    			if (mtf[j] == s.charAt(i)) {
    				place = j;
    				break;
    			}
    		System.out.println(place);
    		
    		int save = mtf[place];
    		for (int k = place-1; k >= 0; k--)
    			mtf[k+1] = mtf[k];
    		mtf[0] = save;
    	}
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	int[] mtf = new int[256];
    	for (int i = 0; i < 256; i++)
    		mtf[i] = i;
    	
    	String s = StdIn.readString();
    	String[] ss = s.split(",");
    	int n = ss.length;
    	int[] ii = new int[n];
    	for (int i = 0; i < n; i++)
    		ii[i] = Integer.parseInt(ss[i]);
    	
    	for (int i = 0; i < n; i++) {		
    		char c = (char) mtf[ii[i]];
    		System.out.print(c);
    		
    		int save = mtf[ii[i]];
    		for (int k = ii[i]-1; k >= 0; k--)
    			mtf[k+1] = mtf[k];
    		mtf[0] = save;
    	}
    }
    /**
     	ABRACADABRA!
        65,66,82,2,68,1,69,1,4,4,2,38
     */
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		encode();
		decode();
	}

}
