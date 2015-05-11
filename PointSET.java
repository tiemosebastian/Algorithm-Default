public class PointSET {
	
	private SET<Point2D> set;
	private int size;
	/**
	 * Initializes empty PointSET
	 */
	public PointSET(){
	   set = new SET<Point2D>();
   }                             
	/**
	 * Returns true if there are no items in PointSET 
	 */
	public boolean isEmpty(){
		return set.isEmpty();
	}
	/**
	 * returns the number of points in PointSET
	 * @return size
	 */
	public int size(){
		return size;
	} 
	/**
	 * Adds point p to the set, so long as p is not already in the set  
	 * @param p
	 */
	public void insert(Point2D p){
		if(this.contains(p)) return;
		set.add(p);
		size++;
	}/**
	* Checks to see if the set contains p
	*
	*/
	public boolean contains(Point2D p){
		return set.contains(p);
	}
	/**
	 * Draws all points to standard draw
	 */
	public void draw(){
		StdDraw.setPenRadius(.01);
		for(Point2D p : range(new RectHV(0,0,1,1))){
			StdDraw.point(p.x(), p.y());
			StdOut.print("("+p.x()+"," + p.y()+ ")");
		}
	}     
	/**
	 * *
	 * Return the range of rectangle
	 * @param rect
	 * @return
	 */
	public Iterable<Point2D> range(RectHV rect){
		Queue<Point2D> queue = new Queue<Point2D>();
		for(Point2D p : set){
			if(rect.contains(p)){
				queue.enqueue(p);;
			}
		}
		return queue;
	}
	/**
	 * Brute force implementation of find. Check every other point for Distance squared, save least value.
	 * @param p
	 * @return
	 */
	public Point2D nearest(Point2D p){
		if(isEmpty()) return null;
		Point2D reference = null;
		for(Point2D q : set){
			if(reference==null) reference = q;
			else if(p.distanceSquaredTo(q) < p.distanceSquaredTo(reference)){
				reference = q;
			}
		}
		return reference;
	}      // a nearest neighbor in the set to point p; null if the set is empty 

   public static void main(String[] args){
	
   }    // unit testing of the methods (optional) 
}