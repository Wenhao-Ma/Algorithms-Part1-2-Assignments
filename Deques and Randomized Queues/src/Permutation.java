import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int k = Integer.valueOf(args[0]);
		RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			randomizedQueue.enqueue(item);
		}
		while (k > 0) {
			StdOut.println(randomizedQueue.dequeue());
			k--;
		}
	}
}
