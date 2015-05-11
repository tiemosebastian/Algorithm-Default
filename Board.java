import java.lang.*;
import java.util.*;

public class Board {
	private final int board[];
	private final int N2;
	private final int dim;
	private int man=-1;
	private int ham=-1;
	
    public Board(int[][] blocks){
    	this(flatten(blocks),-1);
    }
    private static int[] flatten(int[][] blocks){
    	int flat[] = new int[blocks.length*blocks.length];
    	for(int i = 0; i<blocks.length;i++){
    		for(int j = 0; j<blocks.length;j++){
    			flat[i*blocks.length+j]=blocks[i][j];
    		}
    	}
    	return flat;
    }
	private Board(int[] blocks, int man){
    	if(blocks.length<4) throw new java.lang.IllegalArgumentException("Board must be at least 2x2");
    	if(Math.floor(Math.sqrt((double)blocks.length))-Math.sqrt((double)blocks.length)!=0) throw new java.lang.IllegalArgumentException("Board must be square: NxN");
    	board=blocks;
    	N2=blocks.length;
    	dim=(int) Math.sqrt(blocks.length);
    	this.man = man;
    	//StdOut.print("\n"+man+"\n");
    }
	private Board(int[] blocks){// construct a board from an N-by-N array of blocks
    	this(blocks, -1);
    	
    }                                       // (where blocks[i][j] = block in row i, column j)
  
    public int dimension() {                // board dimension N
    	return dim;
    }
    public int hamming(){
    	if(ham!=-1) return ham;
    	int count=0;							// number of blocks out of place
    	for(int i=0;i<N2;i++){
    		if(i==N2-1) continue;
    		if(exp(i)!=board[i]) {
    			count++; 
    		}
    	}
    	ham=count;
    	return count;
    }
    public int manhattan(){                 // sum of Manhattan distances between blocks and goal
    	if(man!=-1) return man;
    	int cityblocks=0;							// number of blocks out of place
    	for(int i=0;i<N2;i++){
    		if(board[i]==0) continue;
    		if(exp(i)==board[i]) continue;
    		else {
   				cityblocks += Math.abs(i(i) - i(board[i]-1)) + //distance of expected 
   							   Math.abs(j(i) - j(board[i]-1));
   		//		StdOut.print("(i,j): ("+i+","+j+")\nFound: "+board[i][j]+"\nExpected at: ("+map(board[i][j]).i + "," +map(board[i][j]).j+")\nMap: "+Math.abs(map(board[i][j]).i - i) +"+" +Math.abs(map(board[i][j]).j - j) +"\nDistance: "+cityblocks+"\n");
   			}    		
    	}
    	man = cityblocks;
    	return cityblocks;
    }
    public boolean isGoal(){				// is this board the goal board?
    	if(hamming()==0) return true;
    	else return false;
    }
    
    public Board twin(){                    // a board that is obtained by exchanging two adjacent blocks in the same row
    	//manhattan();
    	if(dimension()==2 && (board[0]==0||board[1]==0)) return flip(2,3);
    	else if(dimension()==3 && (board[0]==0||board[1]==0)) return flip(3,4);
    	else if(board[0]==0) return flip(1,2);    	
    	else if(board[1]==0) return flip(2,3);
    	else return flip(0,1);
    }
    
    public boolean equals(Object y){        // does this board equal y?
    	if(y==null) return false;
    	if(y.getClass()!=this.getClass()) return false;
    	
    	Board compare = (Board) y;
    	if(dim!=compare.dimension()) return false;
    	for(int i=0; i<N2;i++){
    			if(board[i]!=returnBoard(compare)[i]) return false;
    	}
    	return true;
    }
    public Iterable<Board> neighbors(){     // all neighboring boards
    	return new iter();
    }
    public String toString(){				// string representation of this board (in the output format specified below)
    	String string = String.valueOf(dimension())+"\n";
    	for(int i=0; i<dim; i++){
    		for(int j=0;j<dim;j++){
    			/*if(board[map(i,j)]==0) string=string.concat("   "); 
    			else*/ string=string.concat(String.format("%2d",board[map(i,j)])+" ");
    		}
    		string=string.concat("\n");
    	}
    	return string;
    }
    
    public static void main(String[] args){			 // unit tests (not graded)
    	int[] tes = new int[]{0,1,2,3,4,5,6,7,8};
    	for(int i = 0; i<10; i++){
    	StdRandom.shuffle(tes);
    	Board test = new Board(tes);
    	StdOut.print(test.toString());
    	StdOut.print("hamming: " +test.hamming()+"\n");
    	StdOut.print("manhattan: " +test.manhattan()+"\n");
    	//Board test2 = new Board(Board.convert(tes,3));
    	StdOut.print(test.twin().toString());
    	StdOut.print("hamming: " +test.twin().hamming()+"\n");
    	StdOut.print("manhattan: " +test.twin().manhattan()+"\n");}
    	/*
    	StdOut.print(test.twin().toString());
    	Iterable<Board> itertest=  test.neighbors();
    	for(Board i : itertest){
    		StdOut.print(i.toString());
    	}*/
    	
    	
    }
    
    private static int[] returnBoard(Board the){
    	return the.board;
    }

    private int map(int i, int j){
    	return i*dim+j;
    }
    private int exp(int i){
    	if(i==N2-1) return 0;
    	else return i+1;
    }
    private int i(int pos){
    	return (pos-(pos)%dim)/dim;
    }
    private int j(int pos){
    	return (pos)%dim;
    }

    private Board flip(int in, int out){
    	int twin[]=new int[N2];
    	twin = Arrays.copyOf(board,N2);
    	int placeholder;
    	placeholder=twin[in];
		twin[in]=twin[out];
		twin[out]=placeholder;
	//	StdOut.print("man: " + man+ "\n" + "deltaman: " +(delman(in,out)+" "+delman(out,in)));
		return new Board(twin, man+(delman(in,out)+delman(out,in)));
    }
    private int man(int place){
    	return Math.abs(i(place) - i(board[place]-1)) + //distance of expected 
				   Math.abs(j(place) - j(board[place]-1));
    }
    private int delman(int placein, int placefin){
    	if(board[placein]==0) return 0;
    	return  Math.abs(i(placefin) - i(board[placein]-1)) + //distance of expected from next 
				   Math.abs(j(placefin) - j(board[placein]-1)) 
				   - man(placein);
    }
    private int getEmpty(){
    	
    	for(int i=0;i<N2;i++){
    		if(board[i]==0) return i;
    	}
    	return -2;
    }
    

    private class iter implements Iterable<Board>{
    	
    	public Iterator<Board> iterator(){
    		return new BoardIterator();
    	}
    	
    	private class BoardIterator implements Iterator<Board>{
        	int it = 1;
        	int neighbors[];
        	int empt;
        	
        	public BoardIterator(){
        		neighbors = new int[5];
        		empt = getEmpty();
        		neighbors[0]=empt;
        		if(i(empt)==0) neighbors[1]=-1;
        		else neighbors[1] = map(i(empt)-1,j(empt));
        		if(j(empt)==dim-1) neighbors[2]=-1;
        		else neighbors[2] = map(i(empt),j(empt)+1);
        		if(i(empt)==dim-1) neighbors[3]=-1;
        		else neighbors[3] = map(i(empt)+1,j(empt));
        		if(j(empt)==0) neighbors[4]=-1;
        		else neighbors[4] = map(i(empt),j(empt)-1);
        	}
        	
        	public boolean hasNext(){
        		//if(it>4) return false;
        		for(int i = it; i<5; i++){
        			if(neighbors[i]>-1) return true;
        		}
        		return false;
        	}
        	
        	public void remove(){throw new UnsupportedOperationException();}
        	
        	public Board next(){
        		while(neighbors[it] < 0) it++;
        	//	StdOut.print("next: " +it+"\n");
        		return flip(neighbors[it++],neighbors[0]);
        	}
        }
    }
}
