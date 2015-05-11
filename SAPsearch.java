public class SAPsearch {
	private final int inf=Integer.MAX_VALUE;
	private int max;
	private pnt ref[];
	private int ancestor=-1;
	private final Digraph G;
	/*********************************************************************************
	 * public methods 																				*
	 ********************************************************************************/
	
	/**
	 * Initiate instance of class
	 * @param G
	 */
	public SAPsearch(Digraph G){
		if(G==null) throw new NullPointerException();
		ref = new pnt[G.V()];
		this.G = new Digraph(G);
	}
	/**
	 * returns the ancestor from the last search.
	 * @return
	 */
	public int ancestor(){
		return ancestor;
	}
	/**
	 * returns the length of ths SAP from the last search
	 * @return
	 */
	public int length(){
		if(max==inf) return -1;
		return max;
	}
	/**Search Digraph for Shortest Ancestral Path SAP between the sets of point v in vs{} and w inws{}. 
	 * Method: enqueue points in vs and ws and do an alternating Breadth First Search
	 * if visited check, by which(if v finds w or vis-versa an SAP is found)
	 * Note: R keeps track of the values that were changed, and empties them after running.
	 * in order to efficiently implement many searches. 
	 * @param vs
	 * @param ws
	 */
	public void search(Iterable<Integer> vs, Iterable<Integer> ws){
		for(int v:vs){
			for(int w:ws){
				if(v==w){
					max=0;
					ancestor=v;
					return;
				}
			}
		}
		Queue<Integer> Q = new Queue<Integer>();
		Bag<Integer> R = new Bag<Integer>();
		for(int v:vs){
			ref[v]=new pnt(true,false,0);
			Q.enqueue(v);
			R.add(v);
		}
		for( int w:ws){
			ref[w]=new pnt(false,true,0);
			Q.enqueue(w);
			R.add(w);
		}
		max=inf;
		ancestor=-1;
		testQ(Q,R);
		refresh(R);
	}
	/**
	 * Search Digraph for Shortest Ancestral Path SAP between v,w. 
	 * Method: enqueue point v and w and do an alternating Breadth First Search
	 * if visited check, by which(if v finds w or vis-versa an SAP is found)
	 * Note: R keeps track of the values that were changed, and empties them after running.
	 * in order to efficiently implement many searches. 
	 * @param v
	 * @param w
	 */
	public void search(int v, int w){
		if(v==w) {
			max=0;
			ancestor=v;
			return;
		}
		Queue<Integer> Q = new Queue<Integer>();
		Bag<Integer> R = new Bag<Integer>();
		ref[v]=new pnt(true,false,0);
		ref[w]=new pnt(false,true,0);
		Q.enqueue(v);
		Q.enqueue(w);
		R.add(v);
		R.add(w);
		max=inf;
		ancestor=-1;
		testQ(Q,R);
		refresh(R);
	}
	/*********************************************************************************
	 * 	private methods																*
	 ********************************************************************************/
	/**

	 * Method to test whether the iterable version of the sapsearch return the correct result
	 * (As determined by the non-iterable search.
	 * @param vs
	 * @param ws
	 * @return
	 */
	/*private boolean testiter( Iterable<Integer> vs, Iterable<Integer> ws){
		int min=inf;
		for(int v:vs){
			for(int w:ws){
				search(v,w);
				if(min>max) min=this.max;
			}
		}
		search(vs,ws);
		if(min==max) return true; 
		StdOut.print("\nmin distances don't match in iterated single serach vs single iterated search!");
		StdOut.print("\n min: "+min+"\nmax: " + max);
		return false;
	}*/
	/** Runs the search called in search. Despite name not actually a test method.
	 * 
	 * @param Q
	 * @param R
	 */
	private void testQ(Queue<Integer> Q,Bag<Integer> R){
		int x;
		while(!Q.isEmpty()){
			x = Q.dequeue();
			if(ref[x].dist>max) break;
			for(int x1:G.adj(x)){
				if(visited(x1)){
					if(!ref[x1].v) ref[x1].v=ref[x].v;
					if(!ref[x1].w) ref[x1].w=ref[x].w;
					if(ref[x1].v&&ref[x1].w) {	//if searchpaths(v and w) cross, check if shortest.
						if(max>ref[x1].dist+ref[x].dist+1){//if new shortest path save as ancestor
							max=ref[x1].dist+ref[x].dist+1;	
							ancestor=x1;
							//ancedg=x;
						}
					}
				}
				else{//mark new points, add to queue and cleanup queue
					ref[x1]=new pnt(ref[x].v, ref[x].w, ref[x].dist+1);//ref[x1].edge=x;
					Q.enqueue(x1);
					R.add(x1);
				}
			}
		}
	}	
	/**
	 * undoes the array edits made by search
	 * @param R
	 */
	private void refresh(Iterable<Integer> R){
		for(int r:R){
			ref[r].v=false;
			ref[r].w=false;
		}
	}
	/**
	 * checks whether a point has been visited by the bfs
	 * @param v
	 * @return
	 */
	private boolean visited(int v){
		if(ref[v]==null) return false;
		if(ref[v].v||ref[v].w) {
			return true;
		}
		return false;
	}
	/**
	 * small point class to more concisely store data. 
	 * @author tiemo
	 *
	 */
	private class pnt{
		public boolean v;
		public boolean w;
		public int dist;
		public pnt(boolean v, boolean w, int dist){
			this.v=v;
			this.w=w;
			this.dist=dist;
		}
		public void print(){
			StdOut.print("\nv: " + v + " w: "+ w+ " dist: "+ dist+"  ");
		}
	}
}
