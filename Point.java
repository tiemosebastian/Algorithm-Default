import java.util.Comparator;

public class Point implements Comparable<Point> {

    /**
     *  Comparator to compare points by slope
     */
	private static final Point MaxPoint=new Point(Integer.MAX_VALUE,Integer.MAX_VALUE); 
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>(){
    	public int compare(final Point point1, final Point point2){
    		if(point1.compareTo(MaxPoint)==0&&point2.compareTo(MaxPoint)==0) return 0;
    		else if(point1.compareTo(MaxPoint)==0) return 1;
    		else if(point2.compareTo(MaxPoint)==0) return -1;
    		else if(slopeTo(point1) > slopeTo(point2))
    			return 1;
    		else if(slopeTo(point1) < slopeTo(point2))
    			return -1;
    		else return 0;
    	}
    };

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    /**
     * 
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
        
    }
    /**
     * 
     */
    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }
    /**
     * 
     * @param that
     */
    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    /**
     * 
     * @param that
     * @return
     */
    // slope between this point and that point
    public double slopeTo(Point that) {
    	if(this.x==that.x && this.y==that.y) return Double.NEGATIVE_INFINITY;
    	else if(this.x==that.x) return Double.POSITIVE_INFINITY;
    	else if((double)(that.y-this.y)/(double)(that.x-this.x)==0.0) return 0.0;
    	else return (double)(that.y-this.y)/(that.x-this.x);
    }
    
    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    /**
     * compares
     */
    public int compareTo(Point that) {
        if(this.y < that.y) return -1;
        else if(this.y > that.y) return 1;
        else if(this.x < that.x) return -1;
        else if(this.x >that.x) return 1;
        return 0;
    }

    // return string representation of this point
    /**
     *  Prints out the string representation
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
    	/*
    	StdDraw.setXscale(0,25);
    	StdDraw.setYscale(0,25);
    	StdDraw.setPenRadius(.025);
    	Point test[] = new Point[25]; 
    	for(int i=0;i<test.length;i++){
    		test[i] = new Point(StdRandom.uniform(0,25),StdRandom.uniform(0,25));
    		test[i].draw();
    	}
    	StdDraw.setPenRadius(.00625);

    	for(Point s : test){
    		for(Point t : test){
    			for(Point u: test){
    				for(Point v:test){
    					if(s.equals(t)||s.equals(u)||s.equals(v)||t.equals(u)||t.equals(v)||u.equals(v)) continue;
    						if(s.slopeTo(t)==s.slopeTo(u)&&s.slopeTo(t)==s.slopeTo(v)){
    							StdOut.print(s.toString()+" ");
    							StdOut.print(t.toString()+" "+ s.slopeTo(t)+" ");
    							StdOut.print(u.toString()+" "+ s.slopeTo(u)+" ");
    							StdOut.print(v.toString()+" "+ s.slopeTo(v)+" \n");
    							s.drawTo(t);
    							s.drawTo(u);
    							s.drawTo(v);
    					}
    				}
    			}
    		}
    	}
    	*/
    }
}