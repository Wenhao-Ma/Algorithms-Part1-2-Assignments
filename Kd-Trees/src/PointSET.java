import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
	private SET<Point2D> points;
	
	public PointSET() {                      
		// construct an empty set of points 
		points = new SET<Point2D>(); 
	}
	
	public boolean isEmpty() {
		// is the set empty? 
		return points.size() == 0;
	}
	
	public int size() {
		// number of points in the set 
		return points.size();
	}
	
	public void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		if (p == null) throw new IllegalArgumentException();
		points.add(p);
	}
	
	public  boolean contains(Point2D p) {
		// does the set contain point p? 
		if (p == null) throw new IllegalArgumentException();
		return points.contains(p);
	}
	
	public void draw() {                      
		// draw all points to standard draw 
		StdDraw.setXscale(0,1);
		StdDraw.setYscale(0,1);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		for (Point2D p:points)
			StdDraw.point(p.x(), p.y());
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		// all points that are inside the rectangle (or on the boundary) 
		if (rect == null) throw new IllegalArgumentException();
		ArrayList<Point2D> inRect = new ArrayList<Point2D>();
		for (Point2D p:points) 
			if (rect.contains(p)) inRect.add(p);
		return inRect;
	}
	
	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to point p; null if the set is empty
		if (p == null) throw new IllegalArgumentException();
		double distance = Double.MAX_VALUE;
		Point2D nearP = null;
		for (Point2D pp:points)
		{
			double ndis = pp.distanceSquaredTo(p);
			if (ndis < distance)
			{
				distance = ndis;
				nearP = pp;
			}
		}
		return nearP;
	}
	
	public static void main(String[] args) {
		// unit testing of the methods (optional) 
        In in = new In("kdtree/input10.txt");
        int n = 10;
        PointSET pointSET = new PointSET();
        for (int i = 0; i < n; i++)
        {
        	double x = in.readDouble();
        	double y = in.readDouble();
        	Point2D point2d = new Point2D(x, y);
        	pointSET.insert(point2d);
        }
        pointSET.draw();
        Point2D p = new Point2D(0.5, 0.5);
        System.out.println(pointSET.nearest(p));
	}
}
