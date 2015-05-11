public class Percolation extends WeightedQuickUnionUF{
	private boolean[][] perc;
	private int size, top, bottom;
	private WeightedQuickUnionUF full;
	
	public Percolation(int N){
		super(N*N+2);
		
		if(N <= 0 ) throw new IllegalArgumentException("Size of percolation grid, N, must be greater than 0");
		perc = new boolean[N][N];
		full = new WeightedQuickUnionUF(N*N+2);
		size = N;	
		top = N*N;
		bottom = N*N+1;
		
		
		for(int j = 1; j<=size ;j++){
			this.union(map(1,j),top);
			full.union(map(1,j),top);
			this.union(map(size,j),bottom);
		}
	}
	
	private int map(int i, int j){
		return (i-1)*size+(j-1);
	}
	public void open(int i, int j){
		int x=i-1;
		int y=j-1;
		int point = map(i,j);
		
		if( i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if( j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		perc[x][y]=true;
		if (i < size && perc[x + 1][y]){ 
			this.union(point,map(i+1,j));
			full.union(point,map(i+1,j));
		}
		if (i > 1 && perc[x - 1][y]){
			this.union(point, map(i - 1,j));
			full.union(point, map(i - 1,j));
		}
	    if (j > 1 && perc[x][y - 1]){
	    	this.union(point, map(i,j-1));
	    	full.union(point, map(i,j-1));
	    }
	    if(j<size && perc[x][y+1]){
	    	this.union(point, map(i,j+1));
	    	full.union(point, map(i,j+1));
	    }
	}
	public boolean isOpen(int i, int j){
		if( i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if( j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		return perc[i-1][j-1]; 
	}
	public boolean isFull(int i, int j){
		if(i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if(j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		if(perc[i-1][j-1]==false) return false;
		else return full.connected(map(i,j), top);
	}
	public boolean percolates(){
		return this.connected(top,bottom);
	}
	public int find(int i, int j){
		if(i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if(j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		return this.find(map(i,j));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
