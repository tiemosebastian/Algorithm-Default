import java.util.Comparator;

public class KdTree {
	private static final boolean HOR = true;
	private static final boolean LEFT = true;
	private int size;
	private Node root;
	private int sncount=0,rgcount=0;
	
	public KdTree() {size=0;}                              // construct an empty set of points 
	public boolean isEmpty() {
		if( size == 0) return true;
		return false;
	}                     // is the set empty? 
	public int size(){
	   return size;
	}                         // number of points in the set 
	public void insert(Point2D p) {
		if(p==null) throw new IllegalArgumentException();
		if(isEmpty()) {root=new Node(p, new RectHV(0,0,1,1));size++;}
		else insertput(root, p, HOR);
	}
	// add the point to the set (if it is not already in the set)
	public boolean contains(Point2D p){
		if(p==null) throw new IllegalArgumentException();
		return containssearch(root, p, HOR);
	}
	// does the set contain point p? 
	public void draw(){
		draw(root,HOR);
	}                        
	private void draw(Node node, boolean xory){
		if(node==null) return;
		    
		double a=node.rect.xmin(),b=node.rect.ymin(),c=node.rect.xmax(),d=node.rect.ymax();
		int red=0,blue=0;
		if(xory){
			red=255;
			blue=0;
			a=c=node.point.x();
		}
		else{
			red=0;
			blue=255;
			b=d=node.point.y();
		}
		StdDraw.setPenRadius(.01);
		StdDraw.setPenColor(0,0,0);
		StdDraw.point(node.point.x(),node.point.y());
		StdDraw.setPenRadius(.001);
		StdDraw.setPenColor(red,0,blue);
		StdDraw.line(a,b,c,d);
		draw(node.leftbranch,!xory);
		draw(node.rightbranch,!xory);
	}
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> queue = new Queue<Point2D>();
		range(root, queue, rect);
		return queue;
	}            // all points that are inside the rectangle 
	private void range(Node node, Queue<Point2D> queue, RectHV rect){
		if(node==null) return;	
		if(!rect.intersects(node.rect)) return;
		rgcount++;	
		if(rect.contains(node.point)) queue.enqueue(node.point);		
		range(node.leftbranch,queue,rect);
		range(node.rightbranch,queue,rect);	
	}
	public Point2D nearest(Point2D p){
		if(p==null) throw new IllegalArgumentException();
		if(isEmpty()) return null;
		sncount++;
		return searchnear(root,p);
			
	}             // a nearest neighbor in the set to point p; null if the set is empty 
	public static void main(String[] args){
	}
	private static class Node{
		Point2D point;
		RectHV rect;
		Node leftbranch;
		Node rightbranch;
		
		public Node(Point2D p, RectHV rect){
			point=p;
			this.rect=rect;
			
		}
	}
	private RectHV getRect(Node node, boolean xory, boolean lorr){
		double a=node.rect.xmin(),b=node.rect.ymin(),c=node.rect.xmax(),d=node.rect.ymax();
		if(xory&&lorr) c=node.point.x();
		else if(xory&&!lorr) a=node.point.x();
		else if(!xory&&lorr) d=node.point.y();
		else if(!xory&&!lorr) b=node.point.y();
		return new RectHV(a,b,c,d);
	}
	
	/*
	 * Helper function
	 */
	private Point2D searchnear(Node intree, Point2D newpoint){
		return searchnear2(root,newpoint,root,HOR).point;
	}
	/* Recursively search the 2dTree for closest.
	 * Search branch of tree closer to point, then branch of tree further.
	 * If the closest point is closer than the distance to the HOR/VER line extending from previous Node prune the branch
	 * 
	 */
	private Node searchnear2(Node intree, Point2D newpoint, Node closest, boolean xory){
		if(intree==null) return closest;
		if(newpoint.distanceSquaredTo(closest.point) < intree.rect.distanceSquaredTo(newpoint)) return closest;
		sncount++;
		Node actualclosest=closest;
		if(newpoint.distanceSquaredTo(actualclosest.point) >= newpoint.distanceSquaredTo(intree.point)){
			actualclosest=intree;
		}
		boolean lorr = comphv(intree.point,newpoint,xory)==1;
		if(lorr) actualclosest = searchnear2(intree.leftbranch,newpoint,actualclosest,!xory);
		actualclosest = searchnear2(intree.rightbranch,newpoint,actualclosest,!xory);
		if(!lorr) actualclosest= searchnear2(intree.leftbranch,newpoint,actualclosest,!xory);
		return actualclosest;
	}
	/*
	 * Recursively search for point to put new Node
	 * 
	 */
	private void insertput(Node intree, Point2D newpoint, boolean xory){
		int comp = comphv(intree.point,newpoint,xory);
		if(comp==-1){ 
			if(intree.rightbranch==null) {
				intree.rightbranch = new Node(newpoint,getRect(intree,xory,!LEFT));
				size++;
				}
			else insertput(intree.rightbranch, newpoint,!xory);
		}
		
		else {
			if(comp==0 && comphv(intree.point,newpoint,!xory)==0) return; 
			else if(intree.leftbranch==null) {
				intree.leftbranch = new Node(newpoint,getRect(intree,xory,LEFT));
				size++;
				}
			else insertput(intree.leftbranch,newpoint,!xory);
		}
		
	}
	/*
	 * Compare automatically switches between HOR and VER
	 */
	private static int compare(double f,double s){
		if (f < s) return -1;
        if (f > s) return +1;
        return 0;
	}
	private int comphv(Point2D first, Point2D second, boolean xory){
		if(first==null||second==null) throw new IllegalArgumentException();
		if(xory==HOR) return compare(first.x(),second.x());
		else return compare(first.y(),second.y());
	}
	/*
	 * Recursively searches the Tree for closest point
	 */
	private boolean containssearch(Node intree, Point2D newpoint, boolean xory){
		if(intree == null) return false; 
		else if(newpoint.compareTo(intree.point)==0) return true;
		
		if(comphv(intree.point,newpoint,xory)==-1) return containssearch(intree.rightbranch, newpoint,!xory);
		else return containssearch(intree.leftbranch,newpoint,!xory);
	}
}