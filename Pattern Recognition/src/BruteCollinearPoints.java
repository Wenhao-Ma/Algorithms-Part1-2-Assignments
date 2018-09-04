import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private int N;
	private Point low, high;
	private Point[] points;
 	private LineSegment[] store;
 	
	public BruteCollinearPoints(Point[] lpoints){
		// finds all line segments containing 4 points
		if (lpoints == null) {
			throw new IllegalArgumentException();
		}
		points = new Point[lpoints.length]; 
		for (int i = 0; i < lpoints.length; i++) {
			if (lpoints[i] == null) {
				throw new IllegalArgumentException();
			}
			points[i] = lpoints[i];
		}
		store = new LineSegment[lpoints.length*lpoints.length];
		N = 0;
		for (int i = 0; i < points.length; i++) {
			for (int j = i+1; j < points.length; j++) {
				if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
					throw new IllegalArgumentException();
				}
				for (int k = j+1; k < points.length; k++) {
					for (int l = k+1; l < points.length; l++) {
						double slope1 = points[i].slopeTo(points[j]);
						double slope2 = points[i].slopeTo(points[k]);
						double slope3 = points[i].slopeTo(points[l]);			
						if (slope1 == slope2 && slope2 == slope3) {
							findvertex(points,i,j,k,l);
							LineSegment line = new LineSegment(low, high);
							store[N++] = line;
						}		
					}
				}
			}
		}
	}
	
	public int numberOfSegments() {
		// the number of line segments
		return N;
	}
	public LineSegment[] segments() {
		// the line segments
		LineSegment[] colline = new LineSegment[N];
		for (int i = 0; i < N; i++) {
			colline[i] = store[i];
		}		
		return colline;
	}

	private void findvertex(Point[] points, int i, int j, int k ,int l) {
		Point[] cpy = new Point[4];
		cpy[0] = points[i];
		cpy[1] = points[j];
		cpy[2] = points[k];
		cpy[3] = points[l];
		low = cpy[0];
		high = cpy[0];
		for (int m = 0; m < cpy.length; m++) {
			if (cpy[m].compareTo(low) < 0){
				low = cpy[m];
			}
			if (cpy[m].compareTo(high) > 0) {
				high = cpy[m];
			}
		}
	}
	
	public static void main(String[] args) {
	    // read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}

