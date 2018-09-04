
import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private Node root;
	private int N;
	
	private static class Node {
		private Point2D p;      // the point
		private RectHV rect;    // the axis-aligned rectangle corresponding to this node
		private Node lb;        // the left/bottom subtree
		private Node rt;        // the right/top subtree
		public Node(Point2D p, RectHV r) {
			this.p = p;
			rect = r;
			lb = null;
			rt = null;
		}
	}	
	
	public KdTree() {    
		// construct an empty set of points 
		root = null;
		N = 0;
	}
	
	public boolean isEmpty() {
		// is the set empty? 
		return N == 0;
	}
	
	public int size() {
		// number of points in the set 
		return N;
	}
	
	private int compareto(Point2D p, Point2D q, int orientation) {
		if (p.equals(q)) return 0;
		if (orientation == 1){
			if (p.y() < q.y()) return -1;
			else return 1;
		}
		else {
			if (p.x() < q.x()) return -1;
			else return 1;
		}
	}
	
	public void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		if (p == null) throw new IllegalArgumentException(); 
		root = insert(root, p, null, 0, 0);
	}
	
	private Node insert(Node root, Point2D p, Node parent, int orientation, int cmp) {
		if (root == null) {
			if (N++ == 0){ 
				RectHV unit = new RectHV(0, 0, 1, 1);
				return new Node(p, unit);
			}
			else {
				RectHV rect;
				if (cmp > 0){
					if (orientation != 0)
						 rect = new RectHV(parent.p.x(), parent.rect.ymin(),
				    		               parent.rect.xmax(), parent.rect.ymax());
					else rect = new RectHV(parent.rect.xmin(), parent.p.y(),
							               parent.rect.xmax(), parent.rect.ymax());
				}
				else{
					if (orientation != 0)
						 rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
								           parent.p.x(), parent.rect.ymax());
					else rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),
							               parent.rect.xmax(), parent.p.y());
				}
				return new Node(p, rect);	
			}		
		}
		int cp = compareto(p, root.p, orientation);		
		if      (cp < 0) {
			root.lb = insert(root.lb, p, root, 1-orientation, cp);
		}
		else if (cp > 0) {
			root.rt = insert(root.rt, p, root, 1-orientation, cp);
		}
		return root;
	}
	
	public boolean contains(Point2D p) {
		// does the set contain point p? 
		if (p == null) throw new IllegalArgumentException();
		return get(root,p,0);
	}
	
	private Boolean get(Node root, Point2D p, int orientation) {
		if (root == null) return false;
		int cmp = compareto(p, root.p, orientation);
		if      (cmp < 0) return get(root.lb, p, 1-orientation);
		else if (cmp > 0) return get(root.rt, p, 1-orientation);
		else 			  return true;
	}

	public void draw() {
		// draw all points to standard draw
		draw(root,0);
	}
	
	private void draw(Node root, int orientation) {
		if (root != null){
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		root.p.draw();
		StdDraw.setPenRadius();
		if (orientation == 0){
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(root.p.x(), root.rect.ymin(), root.p.x(), root.rect.ymax());
		}
		else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(root.rect.xmin(), root.p.y(), root.rect.xmax(), root.p.y());
		}
		draw(root.lb, 1-orientation);
		draw(root.rt, 1-orientation);
		}
	}
	
	public Iterable<Point2D> range(RectHV rect) {
		// all points that are inside the rectangle (or on the boundary) 
		if (rect == null) throw new IllegalArgumentException();
		ArrayList<Point2D> inRect = new ArrayList<Point2D>();
		if (root == null) return inRect;
		inRect = findIn(inRect, root, rect);
		return inRect;
	}
	
	private ArrayList<Point2D> findIn(ArrayList<Point2D> inRect, Node root, RectHV rect) {
		if (root.rect.intersects(rect)) 
		{
			if (rect.contains(root.p)) inRect.add(root.p);
			if (root.lb != null && root.lb.rect.intersects(rect)) 
				inRect = findIn(inRect, root.lb, rect);
			if (root.rt != null && root.rt.rect.intersects(rect))
				inRect = findIn(inRect, root.rt, rect);
		}
		return inRect;
	}

	public Point2D nearest(Point2D p) {
		// a nearest neighbor in the set to point p; null if the set is empty
		if (p == null) throw new IllegalArgumentException();
		if (root == null) return null;
		Point2D nearest = findNearest(p, root, root.p, 0);
		return nearest;
	}
	
	private Point2D findNearest(Point2D p, Node root, Point2D temp, int orientation) {
		double shortest = temp.distanceSquaredTo(p);
		double tempdis = root.p.distanceSquaredTo(p);	
		if (tempdis < shortest) {
			shortest = tempdis;
			temp = root.p;
		}
		int cmp = compareto(p, root.p, orientation);
		if (cmp < 0){
			if (root.lb != null && root.lb.rect.distanceSquaredTo(p) < shortest)
				temp = findNearest(p, root.lb, temp, 1-orientation);
			shortest = temp.distanceSquaredTo(p);
			if (root.rt != null && root.rt.rect.distanceSquaredTo(p) < shortest)
				temp = findNearest(p, root.rt, temp, 1-orientation);
		}
		else {
			if (root.rt != null && root.rt.rect.distanceSquaredTo(p) < shortest)
				temp = findNearest(p, root.rt, temp, 1-orientation);
			shortest = temp.distanceSquaredTo(p);
			if (root.lb != null && root.lb.rect.distanceSquaredTo(p) < shortest)
				temp = findNearest(p, root.lb, temp, 1-orientation);
		}
		return temp;
	}

	public static void main(String[] args) {
		// unit testing of the methods (optional) 	
        In in = new In("kdtree/input10.txt");
        int n = 10;
        KdTree kdTree = new KdTree();
        for (int i = 0; i < n; i++)
        {
        	double x = in.readDouble();
        	double y = in.readDouble();
        	Point2D point2d = new Point2D(x, y);
        	kdTree.insert(point2d);
        }
        kdTree.draw();
        Point2D pp = new Point2D(0.5, 0.25);
        StdDraw.setPenRadius(0.05);
        pp.draw();
        System.out.println(kdTree.nearest(pp));
	}
	
	
}
