import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size;
	private Item[] queue;
	
	public RandomizedQueue() {
		// construct an empty randomized queue
		queue = (Item[]) new Object[1];
	}
	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < size; i++) {
			copy[i] = queue[i];
		}
		queue = copy;
	}
	public boolean isEmpty() {
		// is the randomized queue empty?
		return (size == 0);
	}
	public int size() {
		// return the number of items on the randomized queue
		return size;
	}
	public void enqueue(Item item) {
		// add the item
		if (item == null) {
			throw new IllegalArgumentException("input can't be null!");
		}
		if (queue.length == size) {
			resize(2*queue.length);
		}
		queue[size] = item;
		size++;
	}
	public Item dequeue() {
		// remove and return a random item
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty.");
		}
		int num = StdRandom.uniform(size);
		Item item = queue[num];
		queue[num] = queue[size-1];
		queue[size-1] = null;
		size--;
		if (size > 0 && size == queue.length/4) {
			resize(queue.length/2);
		}
		return item;
	}
	public Item sample() {              
		// return a random item (but do not remove it)
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty.");
		}
		int num = StdRandom.uniform(size);
		return queue[num];
	}		
	
	public Iterator<Item> iterator() {   
		// return an independent iterator over items in random order
		return new RdmQueueIterator();
	}
	private class RdmQueueIterator implements Iterator<Item> {
		private Item[] copy;
		private int cpysize;
		public RdmQueueIterator() {
			cpysize = size;
			copy = (Item[]) new Object[size];
			for (int i = 0; i < size; i++) {
				copy[i] = queue[i];
			}
		}
		public boolean hasNext() {
			return (cpysize != 0);
		}
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There are no more items to return!");
			}
			int num = StdRandom.uniform(cpysize);
			Item item = copy[num];
			copy[num] = copy[cpysize-1];
			copy[cpysize-1] = null;
			cpysize--;
			return item;
		}
		public void remove() {
			throw new UnsupportedOperationException("remove is not supported.");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
		r.enqueue(1);
		r.dequeue();
		StdOut.println("The size is "+r.size+". Is empty?"+r.isEmpty());
		Iterator<Integer> iterator1 = r.iterator();
		while (iterator1.hasNext()) {
			StdOut.print(iterator1.next()+" ");
		}
		StdOut.println();
		Iterator<Integer> iterator2 = r.iterator();
		while(iterator2.hasNext()) {
			StdOut.print(iterator2.next()+" ");
		}
		/**
		StdOut.println(r.sample());
		StdOut.println(r.sample());
		StdOut.println(r.sample());
		StdOut.println(r.sample());
		StdOut.println(r.sample());
		*/
	}
}
