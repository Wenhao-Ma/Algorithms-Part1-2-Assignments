import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	private Node first;
	private Node last;
	private int size;
	
	private class Node {
		Item item;
		Node next;
		Node previous;
	}
	public Deque() {
		// construct an empty deque   
		first = null;
		last = null;
		size = 0;
	}
	public boolean isEmpty() {
		// is the deque empty?
		return (size == 0);
	}
	public int size() {
		// return the number of items on the deque
		return size;
	}
	public void addFirst(Item item) {
		// add the item to the front
		if (item == null) {
			throw new IllegalArgumentException("input can't be null.");
		}
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		first.previous = null;
		size++;
		if (size == 1) {
			last = first;
		}
		else {
			oldfirst.previous = first;
		}
	}	
	public void addLast(Item item) {    
		// add the item to the end
		if (item == null) {
			throw new IllegalArgumentException("input can't be null.");
		}
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.previous = oldlast;
		last.next = null;
		size++;
		if (size == 1) {
			first = last;
		}
		else {
			oldlast.next = last;
		}
	}
	public Item removeFirst() {
		// remove and return the item from the front
		if (first == null) {
			throw new NoSuchElementException("Deque is empty!");
		}
		Item item = first.item;
		first = first.next;
		size--;
		if (isEmpty()) {
			first = last = null;
		}
		else {
			first.previous = null;
		}
		return item;
	}
	public Item removeLast() {
		// remove and return the item from the end
		if (last == null) {
			throw new NoSuchElementException("Deque is empty!");
		}
		Item item = last.item;
		last = last.previous;
		size--;
		if (isEmpty()) {
			first = last =null;
		}
		else {
			last.next = null;
		}
		return item;
	}
	
	public Iterator<Item> iterator() {
		// return an iterator over items in order from front to end
		return new DequeIterator();
	}
	private class DequeIterator implements Iterator<Item> {
		private Node current;
		
		public DequeIterator(){
			current = first;
		}
		public boolean hasNext() {
			return (current != null);
		}
		public Item next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There are no more items to return!");
			}
			Item item = current.item;
			current = current.next;
			return item;
		}
		public void remove() {
			throw new UnsupportedOperationException("remove is not supported.");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deque<Integer> deque = new Deque<Integer>();
		deque.addFirst(2);
		deque.addFirst(3);
		
	}
}
