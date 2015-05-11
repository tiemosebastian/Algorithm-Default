import java.util.*;
import java.awt.*;
/**
 * Experimental Seam Finder based around Node Lattice rather than typical array;
 * Expected Benefits: Faster seam erasure; Rather than reindexing ~N^2/2 points 
 * only need to relink ~ Sqrt(N) spots. 
 * @author tiemo
 *
 */
public class DAGPic implements Iterable<Pixel>{
	private final int inf= Integer.MAX_VALUE;
	private Node zero;
	private Node fin = new Node(true);
	//private Node sta = new Node(false);
	private int h;
	private int w;
	
	/**
	 * Creates Node lattice. Node constructor makes lattice corresponding to height
	 * and Width of Data matrix passed.
	 * @param w
	 * @param h
	 * @param datamatrix
	 */
	public DAGPic(int w, int h, Pixel[] datamatrix){
		this.h=h;
		this.w=w;
		zero=new Node(null,0,datamatrix);
		
		Node N = zero.d;
		while(N.r!=null) {
			N.sp=N.u;
			N=N.r;
		}
	}
	public DAGPic(Pixel[][] datamatrix){
		this.h=datamatrix.length;
		this.w=datamatrix[0].length;
		zero=new Node(null,0,flatten(datamatrix));
		StdOut.print(h+" "+w);
		
		Node N = zero.d;
		while(N.r!=null) {
			N.sp=N.u;
			N=N.r;
		}
	}
	/**
	 * Primary benefit of lattice construction seen here.
	 * 
	 * @param del
	 */
	public void DeleteVertSeam(int[] del){
		Node N=zero;
		for(int i=1; i<del[0]+1;i++) N=N.r;
		N=DelTop(N);
		//StdOut.print("\n"+N.p);
		//StdOut.print("\n"+N.d.p);
		for(int i=1;i<del.length-1;i++){
			//zero.print();
			//StdOut.print("\n"+(del[i]-del[i-1]));
			switch (del[i]-del[i-1]){
				case 0: 
					N=DelD(N);
					break;
				case 1: 
					N=DelDR(N);
					break;
				case -1: 
					N=DelDL(N);
					break;
				default: throw new IllegalArgumentException("abs(del[i]-de[i-1])>1");
			}
		}
		w--;
	//	zero.print();
	}
	public void DeleteHorzSeam(int[] del){
		Node N=zero;
		for(int i=1; i<del[0];i++) N=N.d;
		N=DelLeft(N);
		//StdOut.print("\n"+N.p);
		//StdOut.print("\n"+N.d.p);
		for(int i=1;i<del.length;i++){
			//zero.print();
			//StdOut.print("\n"+(del[i]-del[i-1]));
			switch (del[i]-del[i-1]){
				case 0: 
					N=DelR(N);
					break;
				case 1: 
					N=DelRD(N);
					break;
				case -1: 
					N=DelRU(N);
					break;
				default: throw new IllegalArgumentException("abs(del[i]-de[i-1])>1");
			}
		}
		w--;
		zero.print();
	}
	/** Finds seam: see class VertSeamFinder
	 * 
	 * @return
	 */
	public int[] FVSeam(){
		VertSeamFinder t = new VertSeamFinder();
		return t.getSeam();
	}
	public int[] FindVSeam(){
		findSPV();
		return getSeam();
	}
	public int[] FindHSeam(){
		HorzSeamFinder t = new HorzSeamFinder();
		return t.getSeam();
	}
	/**
	 * Returns current height of the lattice.
	 * @return
	 */
	public int height(){
		return h;
	}
	/**
	 * Returns current width of the lattice
	 * @return
	 */
	public int width(){
		return w;
	}
	/**
	 * Returns lattice values Row after Row for picture reconstruction.
	 */
	public HNodeIterator iterator() {
		return new HNodeIterator();
	}
	/**
	 * Finds the Pixel(Color + energy) corresponding to position x,y in the lattice.
	 * @param x
	 * @param y
	 * @return
	 */
	public Pixel get(int x, int y){
		Node N=zero;
		for(int i=0;i<x;i++){
			N=N.r;
		}
		for(int j=0;j<y;j++){
			N=N.d;
		}
		return N.data;
	}
	/**********************************************************************************************************
	 *         Private helper functions
	 **********************************************************************************************************/
	private int[] getSeam(){
		int seam[] = new int[h];
		Node n=fin.sp;
		int i=0;
		while(n!=null){
			n=n.l;
			i++;
		}
		seam[h-1]=i-1;
		//StdOut.print("\nindex:"+(i-1));
		n=fin.sp.u;
		for(i=h-2;i>=1;i--){
		//	StdOut.print("\n n: "+(n.p-n.p%w)/w);
		//	StdOut.print("\n n.d: "+n.sp.p);
		//	StdOut.print("\n seam["+i+"+1]: "+seam[i+1]);
			if(n.sp.d.p==n.p) seam[i]=seam[i+1];
			else if(n.sp.d.r.p==n.p) seam[i]=seam[i+1]-1;
			else if(n.sp.d.l.p==n.p) seam[i]=seam[i+1]+1;
			else assert false;
			n=n.sp;
		}
		seam[0]=seam[1];
		return seam;
	}
	private void findSPV(){
		Node N = zero.d;
		Node M = zero.d;
		while(M.d!=null){
			M=M.d;
			N=M;
			while(N!=null){
				//System.out.print(" "+N.d.data.e());
				N.disto=N.min().disto+N.data.e();
	//			System.out.print(" (p: "+N.p);
//				System.out.printf("%06d ",N.disto);
				N.sp=N.min();
				N=N.r;
			}
//			System.out.print("\n ");
		}
		N=M;
//		System.out.print(N.p/w);
		while(M.r!=null){
			if(M.disto<N.disto) N=M;
			M=M.r;
		}
		fin.sp=N;
	//	System.out.print("fin.sp: " + N.p);
	}
	/**
	 * Flattens N X M array for DAGPic constructor
	 * @param in
	 * @return
	 */
	private Pixel[] flatten(Pixel[][] in){
		int h = in.length;
		int w = in[0].length;
		Pixel[] out=new Pixel[h*w];
		for(int i=0; i<h;i++){
			for(int j=0; j<w;j++){
				out[i*w+j]=in[i][j];
			}
		}
		return out;
	}
	/**
	 * Series of helper functions for deleting Values.
	 * Lattice remains complete, since an entire column/row of values is removed,
	 * therefore all of the Nodes point to same neighbors except those adjacent to 
	 * the seam. These are updated, as is the energy value.
	 * @param N
	 * @return
	 */
	private Node DelTop(Node N){
		N.l.r=N.r;
		N.r.l=N.l;
		DelD(N);
		return N.d;
	}
	/**
	 * When the seam goes down vertically, only horizontal links need to be updated.
	 * @param N
	 * @return
	 */
	private Node DelD(Node N){
	//	System.out.print("\n"+N.p%w);
		Node M=N.d;
		if(M.r==null){
			M.l.r=null; System.out.print("\n Right Side D" + M.p);
		}
		else if(M.l==null){
			M.r.l=null; System.out.print("\n Left Side D"+M.p%366);
		}
		else{
			M.l.r=M.r;
			M.r.l=M.l;
			
			M.l.u.refreshenergy();
			M.r.u.refreshenergy();
		}
		return M;
	}
	/**
	 * When seam goes down and right, horizontal links need to be updated, plus 
	 * links going diagonally orthogonal to the direction of the seam.
	 * @param N
	 * @return
	 */
	private Node DelDR(Node N){
		Node M=N.d.r;
		if(M.r==null){
			M.l.r=null;
			if(M.l.u.l==null) System.out.print(M.l.u.p);
			System.out.print("\n Right Side R"+M.p);
		}
		else{
			M.l.r=M.r;
			M.r.l=M.l;
		}
		M.l.u=M.u;
		M.u.d=M.l;
		
		M.l.u.refreshenergy();
		if(M.l.u.l!=null) M.l.u.l.refreshenergy();
		return M;
	}
	/**
	 * When seam goes down and right, horizontal links need to be updated, plus 
	 * links going diagonally orthogonal to the direction of the seam.
	 */
	private Node DelDL(Node N){
		Node M=N.d.l;
		if(M==null) System.out.println("\nuh-oh"+N.d.p/366 +","+N.d.p%366);
		if(M.l==null){
			M.r.l=null;
			System.out.print("\n Left Side L"+M.p%366);
		}
		else{
			M.l.r=M.r;
			M.r.l=M.l;
		}
		
		M.r.u=M.u;
		M.u.d=M.r;
		
		M.r.u.refreshenergy();
		M.r.u.r.refreshenergy();
		return M;
	}
	/**
	 * Horizontal Versions
	 * @param N
	 * @return
	 */
	private Node DelLeft(Node N){
		N.u.d=N.d;
		N.d.u=N.u;
		return N;
	}
	private Node DelR(Node N){
		Node M=N.r;
		M.u.d=M.d;
		M.d.u=M.u;
		
		M.u.l.refreshenergy();
		M.d.l.refreshenergy();
		return M;
	}
	/**
	 * When seam goes right and down, vertical links need to be updated, plus 
	 * links going diagonally orthogonal to the direction of the seam.
	 */
	private Node DelRD(Node N){
		Node M=N.r.d;
		M.u.d=M.d;
		M.d.u=M.u;
		
		M.u.l=M.l;
		M.l.r=M.u;
		
		M.u.l.refreshenergy();
		M.u.l.u.refreshenergy();
		return M;
	}
	/**
	 * When seam goes right and down, vertical links need to be updated, plus 
	 * links going diagonally orthogonal to the direction of the seam.
	 */
	private Node DelRU(Node N){
		Node M=N.r.u;
		M.u.d=M.d;
		M.d.u=M.u;
		
		M.l.r=M.d;
		M.d.l=M.l;
		
		M.d.l.refreshenergy();
		M.d.l.d.refreshenergy();
		return M;
	}
	/**
	 * Reset disto to inf. Note: could be optimized. Setting only seam-adjacent values
	 *  to inf, and refreshing should be faster. 
	 */
	private void rinf(){
		Node n = zero.r.d;
		Node m;
		while(n.d!=null){
			m=n.d;
			while(n.r!=null){
				n.disto=inf;
				n=n.r;
			}
			n=m;			
		}
	}
	public static void main(String[] args){
		Pixel a[][] = new Pixel[25][25];
		int h1 = a.length;
		int w1 = a[0].length; 
		for(int i=0; i<h1;i++){
			for(int j=0;j<w1;j++){
				if(i<2||j<2){
					a[i][j] = new Pixel(new Color(StdRandom.uniform(0,255-i%254+1),StdRandom.uniform(0,i%254+j%254+1)/2,StdRandom.uniform(0,j%254+1)));
					//a[i][j] = new Pixel(new Color(StdRandom.uniform(0,255),StdRandom.uniform(0,255)/2,StdRandom.uniform(0,255)));
				}
				/*else if(j==125||j==126||j==127){
					a[i][j] = new Pixel(new Color(0,0,0));
					a[i-1][j-1].energy(a[i-2][j-1],a[i][j-1],a[i-1][j],a[i-1][j-2]);
				}*/
				else{
					//a[i][j] = new Pixel(new Color(StdRandom.uniform(0,255),StdRandom.uniform(0,255)/2,StdRandom.uniform(0,255)));
					a[i][j] = new Pixel(new Color(StdRandom.uniform(0,(300-i)%254+1),StdRandom.uniform(0,(300-i)%254+(400-j)%254+1)/2,StdRandom.uniform(0,j%254+1)));
					a[i-1][j-1].energy(a[i-2][j-1],a[i][j-1],a[i-1][j],a[i-1][j-2]);
				}
			}
		}
		DAGPic test= new DAGPic(a);
		int h = test.height();
		int w = test.width();
		Picture temp = new Picture(w,h);
		int i=0;
	//	StdOut.println("\n");
		for(Pixel p: test){
			//StdOut.print(p.e()+""+p.c().toString());
			temp.set( i%w, (i-i%w)/w,p.c());
			i++;
			//if(i%w==0) StdOut.print(i);
		}
		temp.show();
		i=0;
		for(int f: test.FVSeam()){
		//	StdOut.print("Hi, I'm still alive! "+f);
			temp.set( f, i ,new Color(255,255,255));
			i++;
		}
		temp.show();
		//test.zero.print();
	}
	/**********************************************************************************************************
	 *         Helper Classes
	 *********************************************************************************************************/
	private class VertSeamFinder{
		IndexMinPQ<Node> pq=new IndexMinPQ<Node>(h*w);
		
		/**
		 * Work is done in the constructor. Uses Dijkstra's algorithm in order to find shortest path.
		 * Algorithm modified to weigh Vertices rather than Edges.
		 */
		public VertSeamFinder(){
			Node n = zero.r.d;
			rinf();
			while(n.r!=null){
				pq.insert(n.p,n);
//				StdOut.print(n.p);
				n=n.r;
			}
			while(!pq.isEmpty()){
				n = pq.minKey();
//				StdOut.print("\nVSM: "+n.p);
				pq.delMin();
				for(Node p: n.VAdj()){
					relax(p,n);
				}
			}
		}
		/**
		 * Rebuild Seam to fit API.
		 * Faster would be to simply modify delete seam operation to accept lattice links.
		 * Currently we essentially build the array just to deconstruct it later.
		 * @return
		 */
		public int[] getSeam(){
			int seam[] = new int[h];
			Node n=fin;
			Node c=fin.sp;
			int i=0;
			while(c!=null){
				c=c.l;
				i++;
			}
			seam[h-1]=i;
		//	StdOut.print(i);
			for(i=h-2;i>1;i--){
				n=n.sp;
	//			StdOut.print("\n n: "+n.p);
		//		StdOut.print("\n n.sp: "+n.sp.p);
			//	StdOut.print("\n seam["+i+"+1]: "+seam[i+1]);
				if(n.sp.d.p==n.p) seam[i]=seam[i+1];
				else if(n.sp.d.r.p==n.p) seam[i]=seam[i+1]-1;
				else if(n.sp.d.l.p==n.p) seam[i]=seam[i+1]+1;
				else assert false;
			}
			seam[0]=seam[1];
			return seam;
		}
		/**
		 * If there is a shorter path update.
		 * @param n
		 * @param from
		 */
		private void relax(Node n, Node from){//DirectedEdge e){
			//StdOut.print("\nStaying alive! ");
			if(n.disto > from.disto + n.data.e())
			{
			//	StdOut.print("Relax dV: "+n.p+" " + from.p);
				n.disto = from.disto+n.data.e();//=distTo[w] = distTo[v] + e.weight();
				n.sp = from;
				//if (pq.contains(n.p)) pq.decreaseKey(n.p, n);
				//else pq.insert(n.p, n);
				if(n.p!=-1){ 
					if (!pq.contains(n.p)) pq.insert(n.p, n);
				}
			}
		}
	}
	/**
	 * 
	 * @author tiemo
	 *
	 */
	private class HorzSeamFinder{
		public int[] getSeam(){
			return null;
		}
	}
	/**
	 * 
	 */
	private class VNodeIterator implements Iterator<Pixel>{
		Node current=zero;
		Node first;
		public boolean hasNext(){
			return current.r!=null&&current.d!=null;
		}
		public void remove() {throw new UnsupportedOperationException();}
		public Pixel next(){
			if(current.d!=null) {
				current=current.d;
				return current.get();
			}
			else first = first.r;
			return first.get();
		}
	}
	/**
	 * Returns lattice values in row by row form to reconstruct picture.
	 * @author tiemo
	 *
	 */
	private class HNodeIterator implements Iterator<Pixel>{
		Node current=zero;
		Node first=zero;
		int i=0;
		int width=w;
		int j=0;
		public boolean hasNext(){
			return !(current.r==null&&current.d==null);
		}
		public void remove() {throw new UnsupportedOperationException();}
		public Pixel next(){
			if(current.r!=null) {
				i++;
				current=current.r;
				return current.get();
			}
			else{
				j++;
				if(width!=i+1) System.out.print("\nColumns: "+(i+1)+"\nWidth: "+w+"\n Row:"+ j);
				first = first.d;
				current = first;
				i=0;
			}
			return first.get();
		}
	}
	/**Nodes build a lattice vertically and horizontally connected to adjacent Nodes. 
	 * Vertical constructor recursively calls horizontal constructor then itself. 
	 * Horizontal constructor recursively calls itself. Result is a lattice that gets built
	 * row by row.
	 * 
	 * Implements comparable to find Seams using Dijsktra's algorithm.
	 * @author tiemo
	 *
	 */
	private class Node implements Comparable<Node>{
		Node u;
		Node d;
		Node l;
		Node r;
		Node sp;
		int p;
		int disto;
		private Pixel data;
		/**
		 *Special Node for beginning and end of Seam finding. Gives reference to start reconstruction from. 
		 */
		public Node(boolean t){
			if(t) disto=inf;
			else disto = 0;
			data=new Pixel(new Color(0,0,0),0);
			p=-1;
		}
		/**
		 * Horizontal constructor. Recursively calls another Horizontal Node Constructor until bounds are reached
		 * @param p
		 * @param prev
		 * @param dm
		 */
		public Node(int p, Node prev,Pixel[] dm){
			this.p=p;
			this.data= dm[p];
			//StdOut.printf(" %03d ",p);
			assert p%w!=0;
			if(p<w) u=null;//if on top border no up branch;
			else {
				u=prev.u.r;
				prev.u.r.d=this;
			}
			l=prev;
			d=null; //if on bottom border no down branch
			
			
				
			if(p%w==w-1) r=null;//if on right border no right branch
			else r=new Node(p+1,this,dm);//recursive call to new right branch
		}
		/**
		 * Vertical constructor. Calls Horizontal Node Constructor, then makes a recursive call to the Vertical Constructor
		 * until bounds are reached.
		 * @param prev
		 * @param p
		 * @param dm
		 */
		public Node(Node prev, int p,Pixel[] dm){
			this.p=p;
			this.data = dm[p];
			//StdOut.printf("\n %03d ",p);
			assert p%w==0;
			assert p>w;
			l=null;//if on left border no left branch
			u=prev;
			r=new Node(p+1,this,dm);
			if((p-p%w)/w==h-1) d=null; //if on bottom border no down branch
			else d=new Node(this,p+w,dm);
		}
		public int compareTo(Node n){
			if(n==null) throw new NullPointerException();
			if(disto>n.disto) return 1;
			else if(disto<n.disto) return -1;
			else return 0;
		}
		public Pixel get(){
			return data;
		}
		public void print(){
			StdOut.print("\n\n");
			Node cur=this;
			Node cur2;
			while(cur!=null){
				StdOut.print("\n "+ cur.data.toString());
				cur2=cur.r;
				cur=cur.d;
				while(cur2!=null) {
					StdOut.print(" "+ cur2.data.toString());
					cur2=cur2.r;
				}
			}
		}
		/**
		 * Calculate energy.
		 */
		public void refreshenergy(){
			if(l==null||r==null||u==null||d==null) {
				data.border();
				return;
			}
			//System.out.print("\n"+p);
			data.energy(l.data,r.data,u.data,d.data);
		}
		private Node min(){
			if(l==null){
				if(u.disto<u.r.disto) return u;
				return u.r;
			}
			else if(r==null){
				if(u.disto<u.l.disto) return u;
				return u.l;
			}
			else if(u.disto<u.r.disto){
				if(u.disto<u.l.disto) return u;
				else return u.l;
			}
			else if(u.r.disto<u.l.disto) return u.r;
			else return u.l;
		}
		public Bag<Node> HAdj(){
			Bag<Node> hadj=new Bag<Node>();
			hadj.add(r);
			hadj.add(r.u);
			hadj.add(r.d);
			return hadj;
		}
		public Bag<Node> VAdj(){			
			Bag<Node> vadj=new Bag<Node>();
			if(d==null) return vadj;
			if(d.d==null) {
				vadj.add(fin);
				return vadj;
			}
			vadj.add(d);
			if(d.r!=null) vadj.add(d.r);
			if(d.l!=null) vadj.add(d.l);
			return vadj;
		}
	}
}
