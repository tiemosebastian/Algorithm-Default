/**********************************************************************************
 * Percolation Class extends WeightedQuickUnionUF to model porous materials
 * @author tiemo
 *
 */

public class Percolation{
	private boolean[][] perc;
	private int size, top, bottom;
	private WeightedQuickUnionUF full;
	private WeightedQuickUnionUF percolate;

	public Percolation(int N){
		if(N <= 0 ) throw new IllegalArgumentException("Size of percolation grid, N, must be greater than 0");
		perc = new boolean[N][N];
		full = new WeightedQuickUnionUF(N*N+2);
		percolate = new WeightedQuickUnionUF(N*N+2);
		size = N;	
		top = N*N;
		bottom = N*N+1;
		
		for(int j = 1; j<=size ;j++){
			percolate.union(map(1,j),top);
			full.union(map(1,j),top);
			percolate.union(map(size,j),bottom);
		}
	}
	//Calculates position in WQUUF from coordinates in percolation grid.
	private int map(int i, int j){  
		return (i-1)*size+(j-1);
	}
	//Opens spot on boolean array and both WQUUFs for percolation and fullness 
	public void open(int i, int j){
		int x=i-1;
		int y=j-1;
		int point = map(i,j);
		
		if( i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if( j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		perc[x][y]=true;
		if (i < size && perc[x + 1][y]){ 
			percolate.union(point,map(i+1,j));
			full.union(point,map(i+1,j));
		}
		if (i > 1 && perc[x - 1][y]){
			percolate.union(point, map(i - 1,j));
			full.union(point, map(i - 1,j));
		}
	    if (j > 1 && perc[x][y - 1]){
	    	percolate.union(point, map(i,j-1));
	    	full.union(point, map(i,j-1));
	    }
	    if(j<size && perc[x][y+1]){
	    	percolate.union(point, map(i,j+1));
	    	full.union(point, map(i,j+1));
	    }
	}
	//References the boolean array to check openness.
	public boolean isOpen(int i, int j){
		if( i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if( j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		return perc[i-1][j-1]; 
	}
	//If Closed on boolean array, returns false, else checks the full.WQUUF
	public boolean isFull(int i, int j){
		if(i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if(j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		if(perc[i-1][j-1]==false) return false;
		else return full.connected(map(i,j), top);
	}
	//Checks percolation(this).WQUUF for connection between top and bottom.
	public boolean percolates(){
		if(size==1) return perc[0][0];
		return percolate.connected(top,bottom);
	}
	/*//for debuggin purposes
	private int find(int i, int j){
		if(i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if(j <= 0 || j > size) throw new IndexOutOfBoundsException("column index j out of bounds");
		
		return percolate.find(map(i,j));
	}*/
	public static void main(String[] args) {
		
	}
}
