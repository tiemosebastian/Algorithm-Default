import java.lang.*;
import java.util.*;

public class Boardrel {
	private final int board[];
	private final int N2;
	private final int dim;
	private final Move fromprev;
	private int man=-1;
	private int ham=-1;
	
	private Boardrel(Boardrel previous, Move move){
		board=null;
		dim=previous.dimension();
		N2=dim*dim;
		fromprev=move;
		man=previous.manhattan()+fromprev.delman;
		ham=previous.hamming() + fromprev.delham;
	}
    private Boardrel(int[] blocks, int man){
    	if(blocks.length<4) throw new java.lang.IllegalArgumentException("Boardrel must be at least 2x2");
    	if(Math.floor(Math.sqrt((double)blocks.length))-Math.sqrt((double)blocks.length)!=0) throw new java.lang.IllegalArgumentException("Boardrel must be square: NxN");
    	board=blocks;
    	N2=blocks.length;
    	dim=(int) Math.sqrt(blocks.length);
    	this.man = man;
    	fromprev=null;
    	//StdOut.print("\n"+man+"\n");
    }
	public Boardrel(int[] blocks){// construct a board from an N-by-N array of blocks
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
    
    public Boardrel twin(){                    // a board that is obtained by exchanging two adjacent blocks in the same row
    	manhattan();
    	if(dimension()==2 && (board[0]==0||board[1]==0)) return flip(2,3);
    	if(board[0]==0) return flip(1,2);    	
    	else if(board[1]==0) return flip(0,2);
    	else return flip(0,1);
    }
    
    public boolean equals(Object y){        // does this board equal y?
    	if(y.getClass()!=this.getClass()) return false;
    	
    	Boardrel compare = (Boardrel) y;
    	if(dim!=compare.dimension()) return false;
    	for(int i=0; i<N2;i++){
    			if(board[i]!=returnBoard(compare)[i]) return false;
    	}
    	return true;
    }
    public Iterable<Boardrel> neighbors(){     // all neighboring boards
    	return new iter();
    }
    public String toString(){				// string representation of this board (in the output format specified below)
    	String string = String.valueOf(dimension())+"\n";
    	for(int i=0; i<dim; i++){
    		for(int j=0;j<dim;j++){
    			if(board[map(i,j)]==0) string=string.concat("   "); 
    			else string=string.concat(String.format("%2d",board[map(i,j)])+" ");
    		}
    		string=string.concat("\n");
    	}
    	return string;
    }
    
    public static void main(String[] args){			 // unit tests (not graded)
    	int[] tes = new int[]{0,1,2,3,4,5,6,7,8};
    	for(int i = 0; i<10; i++){
    	StdRandom.shuffle(tes);
    	Boardrel test = new Boardrel(tes);
    	StdOut.print(test.toString());
    	StdOut.print("hamming: " +test.hamming()+"\n");
    	StdOut.print("manhattan: " +test.manhattan()+"\n");
    	//Board test2 = new Board(Board.convert(tes,3));
    	StdOut.print(test.twin().toString());
    	StdOut.print("hamming: " +test.twin().hamming()+"\n");
    	StdOut.print("manhattan: " +test.twin().manhattan()+"\n");}
    	/*
    	StdOut.print(test.twin().toString());
    	Iterable<Boardrel> itertest=  test.neighbors();
    	for(Boardrel i : itertest){
    		StdOut.print(i.toString());
    	}*/
    	
    	
    }
    
    private static int[] returnBoard(Boardrel the){
    	return the.board;
    }

    private int map(int i, int j){
    	return i*dim+j;
    }
    private int exp(int i, int j){
    	if(i==dim-1&&j==dim-1) return 0;
    	else return i+1;
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
    private Boardrel flipzero(int in){
    	int h=0;
    	if(exp(getEmpty())==in) h=1;
    	return new Boardrel(this,new Move(getEmpty(),in,delman(in,getEmpty()),h));
    }
    private Boardrel flip(int in, int out){
    	int twin[]=new int[N2];
    	twin = Arrays.copyOf(board,N2);
    	int placeholder;
    	placeholder=twin[in];
		twin[in]=twin[out];
		twin[out]=placeholder;
		//StdOut.print("man: " + man+ "\n" + "deltaman: " +(delman(in,out)+delman(out,in)));
		return new Boardrel(twin, man+(delman(in,out)+delman(out,in)));
    }
    private int man(int place){
    	return Math.abs(i(place) - i(board[place]-1)) + //distance of expected 
				   Math.abs(j(place) - j(board[place]-1));
    }
    private int delman(int placein, int placefin){
    	//if(board[placein]==0) return 0;
    	return  Math.abs(i(placefin) - i(board[placein]-1)) + //distance of expected from next 
				   Math.abs(j(placefin) - j(board[placein]-1)) 
				   - man(placein);
    }
    private int getEmpty(){
    	if(fromprev!=null) return fromprev.newzero;
    	for(int i=0;i<N2;i++){
    		if(board[i]==0) return i;
    	}
    	return -2;
    }
    

    private class iter implements Iterable<Boardrel>{
    	
    	public Iterator<Boardrel> iterator(){
    		return new BoardIterator();
    	}
    	
    	private class BoardIterator implements Iterator<Boardrel>{
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
        	
        	public Boardrel next(){
        		while(neighbors[it] < 0) it++;
        	//	StdOut.print("next: " +it+"\n");
        		return flipzero(neighbors[it++]);
        	}
        }
    }
    private class Move{
    	final int newzero;
    	final int oldzero;
    	final int delman;
    	final int delham;
    	public Move(int oldzero, int newzero, int delman, int delham){
    		this.newzero = newzero;
    		this.oldzero=oldzero;
    		this.delman=delman;
    		this.delham=delham;
    	}
    }
}

