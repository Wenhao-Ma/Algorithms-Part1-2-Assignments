import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {
    private  int x;
    private  int y;
    
    public Point(int x, int y){
    	//constructs the point (x, y)
    	this.x = x;
    	this.y = y;
    }
    
    public void draw() {
    	// draws this point
        StdDraw.point(x, y);
    }
    
    public void drawTo(Point that) {
    	//draws the line segment from this point to that point
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public String toString() {
    	// string representation
        return "(" + x + ", " + y + ")";
    }
    
	@Override
	public int compareTo(Point o) {
		//compare two points by y-coordinates, breaking ties by x-coordinates
		if ( this.y < o.y) { return -1; }
		else if (this.y == o.y && this.x < o.x) { return -1;}
		else if (this.y == o.y && this.x == o.x) { return 0; }
		return 1;
	}
	
	public double slopeTo(Point that){
		// the slope between this point and that point
		if (this.y == that.y && this.x == that.x){return Double.NEGATIVE_INFINITY;}
		else if (this.y == that.y) {return +0;}
		else if (this.x == that.x) {return Double.POSITIVE_INFINITY;}
		return (that.y-this.y)/(double)(that.x-this.x);
	}
	
	public Comparator<Point> slopeOrder(){
		// compare two points by slopes they make with this point
		return new BySlope();
	}
	private class BySlope implements Comparator<Point>{
		@Override
		public int compare(Point o1, Point o2) {
		    if (slopeTo(o1) < slopeTo(o2)) {return -1;}
		    else if (slopeTo(o1) == slopeTo(o2)) {
		    	return 0;
		    	}
		    return 1;
		}	
	}
	
    public static void main(String[] args) {
    	ArrayList<Point> point = new ArrayList<Point>(5);
    //	/**
		int n = StdIn.readInt();
	    Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = StdIn.readInt();
			int y = StdIn.readInt();
			points[i] = new Point(x, y);
			point.add(points[i]);
		}
		/**
		Comparator<Point> BY_SLOPE = points[0].slopeOrder();
	    Arrays.sort(points, BY_SLOPE);
	    Point low = points[0];
	    Point high = points[0];
		Arrays.sort(points,1,4);
		if (low.compareTo(points[1]) > 0) {
			low = points[1];
		}
		if (high.compareTo(points[3]) < 0) {
			high = points[3];
		}
	    for (int i = 0; i < n; i++) {
			System.out.println(points[i].toString());
		}
	    System.out.println(low+" "+high);
	    */
		
	//	*/
	}
}

class LineSegment {
    private final Point p;   // one endpoint of this line segment
    private final Point q;   // the other endpoint of this line segment

    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new NullPointerException("argument is null");
        }
        this.p = p;
        this.q = q;
    }

    public void draw() {
    	p.drawTo(q);
    }

    public String toString() {
    	return p + " -> " + q;
    }

    public int hashCode() {
    	throw new UnsupportedOperationException();
    }
}

