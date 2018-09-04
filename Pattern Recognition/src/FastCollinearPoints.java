import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private int N, Ncheck;
	private LineSegment[] colline;
	private Point low, high;
	private LineSegment[] store;
	private Pair[] pointpair;
	private Point[] lpoints;
	
	public FastCollinearPoints(Point[] points) {
		// finds all line segments containing 4 or more points
		if (points == null) {
			throw new IllegalArgumentException();
		}
		int len = points.length;
		lpoints = new Point[len];
		for (int i = 0; i < len; i++) {
			if (points[i] == null) {
				throw new IllegalArgumentException();
			}			
			lpoints[i] = points[i];
		}
		pointpair = new Pair[len*len];
		N = 0;	
		Ncheck = 0;
		Arrays.sort(lpoints);
		for (int i = 0; i < len; i++) {
			Comparator<Point> BY_SLOPE = lpoints[i].slopeOrder();
			int len2 = len-i-1;
			Point[] sortpoints = new Point[len2];
			for (int j = 0; j < len2; j++) {
				sortpoints[j] = lpoints[i+1+j];
			}
			Arrays.sort(sortpoints);
			Arrays.sort(sortpoints, BY_SLOPE);	
			double[] slope = new double[len2];
			for (int j = 0; j < len2; j++) {
				slope[j] = lpoints[i].slopeTo(sortpoints[j]);
				if (slope[j] == Double.NEGATIVE_INFINITY) {
					throw new IllegalArgumentException();
				}
			}
			int num = 0;
			double slopecheck = Double.NEGATIVE_INFINITY;
			for (int j = 0; j < len2; j++) {
				if (slope[j] == slopecheck) {
					num++;
					if (num >= 3 && j == len2-1) {
						Find(sortpoints, num, i, j, slopecheck);
					}
				}
				else {
					if (num >= 3) {
						Find(sortpoints, num, i, j-1, slopecheck);
					}
					num = 1;
					slopecheck = slope[j];
				}
			}
		}
		RemoveDuplicate();
	}
	  
	private void Find(Point[] sortpoints, int num, int i, int j, double slopecheck) {
		low = high = lpoints[i];
		if (low.compareTo(sortpoints[j-num+1]) > 0) {
			low = sortpoints[j-num+1];
		}
		if (high.compareTo(sortpoints[j]) < 0){
			high = sortpoints[j];
		}		
		Pair pair = new Pair(low,high,slopecheck);
		pointpair[Ncheck] = pair;
		Ncheck++;
	}
	
	private void RemoveDuplicate() {
		store = new LineSegment[Ncheck];
		Arrays.sort(pointpair,0,Ncheck);
		for (int i = 0; i < Ncheck; i++) {
			if (i == 0) {
				LineSegment line = new LineSegment(pointpair[i].getlow(), pointpair[i].gethigh()); 
				store[N] = line;
				N++;
			}
			else if (pointpair[i].compareTo(pointpair[i-1]) != 0) {
				LineSegment line = new LineSegment(pointpair[i].getlow(), pointpair[i].gethigh());
				store[N] = line;
				N++;
			}
		}
	}
	
	private class Pair implements Comparable<Pair>{
		private final Point high;
		private final Point low;
		private final double slp;
		public Pair(Point lowpoint,Point highpoint, double slope){
			this.high = highpoint;
			this.low = lowpoint;
			this.slp = slope;
		}
		public Point gethigh() {
			return this.high;
		}
		public Point getlow() {
			return this.low;
		}
		public int compareTo(Pair that) {
			if (this.high.compareTo(that.high) < 0) {
				return -1;
			}
			if (this.high.compareTo(that.high) == 0) {
				if (this.slp < that.slp) {
					return -1;
				}
				if (this.slp == that.slp) {
					return 0;
				}
				return 1;
			}
			return 1;
		}
	}
	
	public int numberOfSegments() {
		// the number of line segments
		return N;
	}
	
	public LineSegment[] segments() {
		// the line segments
		colline = new LineSegment[N];
		for (int i = 0; i < N; i++) {
			colline[i] = store[i];
		}
		return colline;
	}
	
	public static void main(String[] args) {
	    // read the n points from a file
//		/**
	    In in = new In(args[0]);
	    int n = in.readInt();
//	    */
//		int n = StdIn.readInt();
		Point[] points = new Point[n];
//	   /**
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }
//     */
	    /**
	    for (int i = 0; i < n; i++) {
	    	int x = StdIn.readInt();
	    	int y = StdIn.readInt();
	    	points[i] = new Point(x, y);
	    }
       */
	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}

