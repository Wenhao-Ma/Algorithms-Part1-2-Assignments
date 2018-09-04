import java.awt.Color;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private Picture picture;
	private static final boolean VERTICAL = true;
	private int[][] color;
	private int w,h;
	
	private class DijSP {
		private int[][] edgeTo;
		private double[][] distTo;
		private IndexMinPQ<Double> pq;
		
		public DijSP(Boolean method) {
			pq = new IndexMinPQ<Double>(width()*height());
			edgeTo = new int[width()][height()];
			distTo = new double[width()][height()];
			
			for (int x = 0; x < width(); x++)
				for (int y = 0; y < height(); y++)
					distTo[x][y] = Double.POSITIVE_INFINITY;
			if (method == VERTICAL)
				for (int x = 0; x < width(); x++) {
					distTo[x][0] = 1000.0;
					int i = to1D(x, 0);
					pq.insert(i, 1000.0);
				}
			else
				for (int y = 0; y < height(); y++) {
					distTo[0][y] = 1000.0;
					int i = to1D(0, y);
					pq.insert(i, 1000.0);
				}
			
			while (!pq.isEmpty())
			{
				int v = pq.delMin();
				int x = to2D(v)[0], y = to2D(v)[1];
				if (method == VERTICAL)
					for (int e:adjVertical(x, y)) relax(e,x,y);
				else for (int e:adjHorizontal(x, y)) relax(e,x,y);
			}
		}
		
		private void relax(int e, int i, int j) {
			int x = to2D(e)[0]; int y = to2D(e)[1];
			if (distTo[x][y] > distTo[i][j] + energy(x, y))
			{
				distTo[x][y] = distTo[i][j] + energy(x, y);
				edgeTo[x][y] = to1D(i, j);
				if (pq.contains(e)) pq.decreaseKey(e, distTo[x][y]);
				else pq.insert(e, distTo[x][y]);
			}
		}	
		
		public int[] seam(Boolean method) {
			int[] path = new int[method == VERTICAL?height():width()];
			int x, y, n, lastpixel = 0;
			double mindist = Double.POSITIVE_INFINITY;
			if (method == VERTICAL)
			{
				for (int xx = 0; xx < width(); xx++)
					if (distTo[xx][height()-1] < mindist){
						mindist = distTo[xx][height()-1];
						lastpixel = xx;
					}
				n = height()-1;
				path[n] = lastpixel;;
				x = lastpixel; y = height()-1;
				while (n > 0){
					int e = edgeTo[x][y];
					x = to2D(e)[0]; y = to2D(e)[1];
					path[--n] = x;
				} 
			}
			else
			{
				for (int yy = 0; yy < height(); yy++)
					if (distTo[width()-1][yy] < mindist){
						mindist = distTo[width()-1][yy];
						lastpixel = yy;
					}
				n = width()-1;
				path[n] = lastpixel;;
				x = width()-1; y = lastpixel;	
				while (n > 0) {
					int e = edgeTo[x][y];
					x = to2D(e)[0]; y = to2D(e)[1];
					path[--n] = y;
				}
			}
			return path;
		}
	}
	
	private Iterable<Integer> adjVertical(int x, int y) {
		Bag<Integer> adj = new Bag<Integer>();
		if (y < height()-1)
		{	
			if (width() == 1)
			{adj.add(to1D(0, y+1));}
			else if (x == 0)
			{adj.add(to1D(x, y+1));adj.add(to1D(x+1, y+1));}
			else if (x == width()-1)
			{adj.add(to1D(x-1, y+1));adj.add(to1D(x, y+1));}
			else
			{adj.add(to1D(x-1, y+1));adj.add(to1D(x, y+1));
			 adj.add(to1D(x+1, y+1));}
		}
		return adj;
	}
		
	private Iterable<Integer> adjHorizontal(int x, int y) {
		Bag<Integer> adj = new Bag<Integer>();
		if (x < width()-1)
		{
			if (height() == 1)
			{adj.add(to1D(x+1, y));}
			else if (y == 0)
			{adj.add(to1D(x+1, y));adj.add(to1D(x+1, y+1));}
			else if (y == height()-1)
			{adj.add(to1D(x+1, y));adj.add(to1D(x+1, y-1));}
			else
			{adj.add(to1D(x+1, y-1));adj.add(to1D(x+1, y));
			 adj.add(to1D(x+1, y+1));}
		}
		return adj;
	}
	
	private int to1D(int x, int y) {
		return x+y*width();
	}
	
	private int[] to2D(int i) {
		int[] d = new int[2];
		d[0] = i%width();
		d[1] = i/width();
		return d;
	}
	
	public SeamCarver(Picture picture) {
		// create a seam carver object based on the given picture
		if (picture == null) throw new IllegalArgumentException();
		h = picture.height();
		w = picture.width();
		color = new int[w][h];
		for (int x = 0; x < width(); x++)
			for (int y = 0; y < height(); y++){
				Color rbg = picture.get(x, y);
				color[x][y] = rbg.getRGB();
			}
	}
	
	public Picture picture() {
		// current picture
		picture = new Picture(width(),height());
		for (int x = 0; x < width(); x++)
			for (int y = 0; y < height(); y++)
			{
				Color c = new Color(color[x][y]);
				picture.set(x, y, c);
			}	
		return picture;
	}
	
	public int width() {
		// width of current picture
		return w;
	}
	
	public int height() {
		// height of current picture
		return h;
	}
	
	public double energy(int x, int y) {
		// energy of pixel at column x and row y
		if (!isValid(x, y)) throw new IllegalArgumentException();
		if (x == 0 || x == width()-1 ||
			y == 0 || y == height()-1) 
			return 1000.0;
		return Math.sqrt(xenergy(x, y)+yenergy(x, y));
	}
	
	private boolean isValid(int x, int y) {
		if (x >= 0 && x < width() && y >= 0 && y <height()) return true;
		else return false;
	}
	
	private int xenergy(int x, int y) {
		int rx = ((color[x-1][y]>>16)&0xff)-((color[x+1][y]>>16)&0xff);
		int gx = ((color[x-1][y]>>8)&0xff)-((color[x+1][y]>>8)&0xff);
		int bx = (color[x-1][y]&0xff)-(color[x+1][y]&0xff);
		int squarex = rx*rx+gx*gx+bx*bx;
		return squarex;
	}
	
	private int yenergy(int x, int y) {
		int ry = ((color[x][y-1]>>16)&0xff)-((color[x][y+1]>>16)&0xff);
		int gy = ((color[x][y-1]>>8)&0xff)-((color[x][y+1]>>8)&0xff);
		int by = (color[x][y-1]&0xff)-(color[x][y+1]&0xff);
		int squarey = ry*ry+gy*gy+by*by;
		return squarey;		
	}

	public int[] findHorizontalSeam() {
		// sequence of indices for horizontal seam
		DijSP sp =new DijSP(!VERTICAL);
		int[] path = sp.seam(!VERTICAL);
		return path;
	}
	
	public int[] findVerticalSeam() {
		// sequence of indices for vertical seam
		DijSP sp = new DijSP(VERTICAL);
		int[] path = sp.seam(VERTICAL);
		return path;
	}
	
	public void removeHorizontalSeam(int[] seam) {
		// remove horizontal seam from current picture
		if (seam == null) throw new IllegalArgumentException();
		if (height() <= 1) throw new IllegalArgumentException();
		if (seam.length != width()) throw new IllegalArgumentException();
        int[][] cl = new int[width()][height()-1];
        for (int x = 0; x < width(); x++) {
            if (seam[x] < 0 || seam[x] >= height())
            	throw new IllegalArgumentException(); 
            if (x > 0 && Math.abs(seam[x]-seam[x-1]) > 1) 
            	throw new IllegalArgumentException();           
            for (int y = 0; y < seam[x]; y++) {
                cl[x][y] = color[x][y];
            }
            for (int y = seam[x]+1; y < height(); y++) {
                cl[x][y-1] = color[x][y];
            }
        }
        color = cl;
        h--;
	}
	
	public void removeVerticalSeam(int[] seam) {
		// remove vertical seam from current picture
		if (seam == null) throw new IllegalArgumentException();	
		if (width() <= 1) throw new IllegalArgumentException();
		if (seam.length != height()) throw new IllegalArgumentException();
        int[][] cl = new int[width()-1][height()];
        for (int y = 0; y < height(); y++) {
        	if (seam[y] < 0 || seam[y] >= width())
        		throw new IllegalArgumentException();    
            if (y > 0 && Math.abs(seam[y]-seam[y-1]) > 1)
            	throw new IllegalArgumentException();       	
            for (int x = 0; x < seam[y]; x++) {
                cl[x][y] = color[x][y];
            }
            for (int x = seam[y]+1; x < width(); x++) {
                cl[x-1][y] = color[x][y];
            }
        }
        color = cl;
        w--;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Picture picture = new Picture("seam/5x6.png");
		SeamCarver seamCarver = new SeamCarver(picture);
		int X = seamCarver.width();
		int Y = seamCarver.height();
		System.out.println("width = "+X+", height = "+Y+";");
		
		
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++)
				System.out.printf("%6.0f",seamCarver.energy(x, y));
			System.out.println();
		}
		
		int[] vpath = seamCarver.findVerticalSeam();
		
		System.out.println("VerticalSeam: ");
		int n = 0;
		for (int i:vpath)
		{
			n++;
			System.out.print(i+" ");
			if (n == 10)
				{System.out.println();n=0;}
		}		
		System.out.println();
		
		seamCarver.removeVerticalSeam(vpath);
		
		X = seamCarver.width();
		Y = seamCarver.height();
		for (int y = 0; y < Y; y++) {
			for (int x = 0; x < X; x++)
				System.out.printf("%6.0f",seamCarver.energy(x, y));
			System.out.println();
		}
		System.out.println(picture.equals(seamCarver.picture()));
	}

}
