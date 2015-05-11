/*import java.lang.*;
import java.util.*;

public class Boardsingle {
	private final int board[];
	private final int N2;
	private final int dim;
	private int man=-1;
	private int ham=-1;
	
    private Boardsingle(int[] blocks, int man){
    	if(blocks.length<4) throw new java.lang.IllegalArgumentException("Boardsingle must be at least 2x2");
    	if(Math.floor(Math.sqrt((double)blocks.length))-Math.sqrt((double)blocks.length)!=0) throw new java.lang.IllegalArgumentException("Boardsingle must be square: NxN");
    	board=blocks;
    	N2=blocks.length;
    	dim=(int) Math.sqrt(blocks.length);
    	this.man = man;
    	//StdOut.print("\n"+man+"\n");
    }
	public Boardsingle(int[] blocks){// construct a board from an N-by-N array of blocks
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
    
    public Boardsingle twin(){                    // a board that is obtained by exchanging two adjacent blocks in the same row
    	manhattan();
    	if(dimension()==2 && (board[0]==0||board[1]==0)) return flip(2,3);
    	if(board[0]==0) return flip(1,2);    	
    	else if(board[1]==0) return flip(0,2);
    	else return flip(0,1);
    }
    
    public boolean equals(Object y){        // does this board equal y?
    	if(y.getClass()!=this.getClass()) return false;
    	
    	Boardsingle compare = (Boardsingle) y;
    	if(dim!=compare.dimension()) return false;
    	for(int i=0; i<N2;i++){
    			if(board[i]!=returnBoard(compare)[i]) return false;
    	}
    	return true;
    }
    public Iterable<Boardsingle> neighbors(){     // all neighboring boards
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
    	Boardsingle test = new Boardsingle(tes);
    	StdOut.print(test.toString());
    	StdOut.print("hamming: " +test.hamming()+"\n");
    	StdOut.print("manhattan: " +test.manhattan()+"\n");
    	//Board test2 = new Board(Board.convert(tes,3));
    	StdOut.print(test.twin().toString());
    	StdOut.print("hamming: " +test.twin().hamming()+"\n");
    	StdOut.print("manhattan: " +test.twin().manhattan()+"\n");}
    	/*
    	StdOut.print(test.twin().toString());
    	Iterable<Boardsingle> itertest=  test.neighbors();
    	for(Boardsingle i : itertest){
    		StdOut.print(i.toString());
    	}*/
    	
  /*  	
    }
    
    private static int[] returnBoard(Boardsingle the){
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

    private Boardsingle flip(int in, int out){
    	int twin[]=new int[N2];
    	twin = Arrays.copyOf(board,N2);
    	int placeholder;
    	placeholder=twin[in];
		twin[in]=twin[out];
		twin[out]=placeholder;
		//StdOut.print("man: " + man+ "\n" + "deltaman: " +(delman(in,out)+delman(out,in)));
		return new Boardsingle(twin, man+(delman(in,out)+delman(out,in)));
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
    

    private class iter implements Iterable<Boardsingle>{
    	
    	public Iterator<Boardsingle> iterator(){
    		return new BoardIterator();
    	}
    	
    	private class BoardIterator implements Iterator<Boardsingle>{
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
        	
        	public Boardsingle next(){
        		while(neighbors[it] < 0) it++;
        	//	StdOut.print("next: " +it+"\n");
        		return flip(neighbors[it++],neighbors[0]);
        	}
        }
    }
}
import java.lang.*;
import java.util.*;

public class Solver {
	final Board game;
	final Board twin;
	MinPQ<BoardNode> gamePQ;
	MinPQ<BoardNode> twinPQ;
	final BoardNode solution;
	private final boolean solveable;
	private final Comparator<BoardNode> MANHATTAN = new Comparator<BoardNode>(){
		public int compare(BoardNode first, BoardNode second){
			if(first==null) return -1;
			if(second==null) return 1;
			int man1=first.manhattanPriority();
			int man2=second.manhattanPriority();
			if(man1 > man2) return 1;
			else if(man1 < man2) return -1;
			return 0;
		}
	};

	//////////////////////////////////////////////////////////////////////////////////////////
 /*   public Solver(Board initial){           // find a solution to the initial board (using the A* algorithm)
    	game=initial;
    	twin=game.twin();
    	gamePQ = new MinPQ<BoardNode>(MANHATTAN);
    	twinPQ = new MinPQ<BoardNode>(MANHATTAN);
    	
    	gamePQ.insert(new BoardNode(game));
    	twinPQ.insert(new BoardNode(twin));
    	while(gamePQ.min().current.isGoal()==false && twinPQ.min().current.isGoal()==false){
    		gamePQ.min().findNeighbors();
    		twinPQ.min().findNeighbors();
    		for(BoardNode p: gamePQ.delMin().neighbors){
    			if(p==null) continue;
    			//StdOut.print(p.current.toString());
    			gamePQ.insert(p);
    		}
    		for(BoardNode p: twinPQ.delMin().neighbors){
    			if(p==null) continue;
    			twinPQ.insert(p);
    		}
    	}
    	if(gamePQ.min().current.isGoal()==true) {
    		solution = gamePQ.min();
    		solveable = true;
    	}
    	else {
 /*   		solution = null;
    		solveable=false;
    	}
    }
    public boolean isSolvable(){            // is the initial board solvable?
    	return solveable;
    }
    public int moves(){                     // min number of moves to solve initial board; -1 if unsolvable
    	if(!isSolvable()) return -1;
    	return solution.moves;
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
    	if(!isSolvable()) return null;
    	return new Solve();
    }
    public static void main(String[] args){ // solve a slider puzzle (given below)
    	int testarray[] = new int[]{1,2,3,4,5,6,7,8,0};
    	StdRandom.shuffle(testarray);
    	Solver test = new Solver(new Board(convert(testarray, 3)));
    	Stopwatch timer = new Stopwatch();
    	for(int i=0;i<1000;i++)
    	{
    		StdRandom.shuffle(testarray);
    		test = new Solver(new Board(Board.convert(testarray,3)));
    	}
    	StdOut.print(timer.elapsedTime()+"\n");
    	
    	StdOut.print("Is the test solvable? "+test.isSolvable()+"\n");
    	StdOut.print("It took " + test.moves() + " moves.");
    	
    	// create initial board from file
       /* In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);*/
  /*      }
    
    
    /////////////////////////////////////////////////////////////////////////
  /*  private static int[][] convert(int[] input,int dimension){
    	int[][] toReturn = new int[dimension][dimension];
    	for(int i=0; i<dimension*dimension;i++){
    		//	StdOut.print((i%dimension) + " " + "i-i%dim: "+(i-i%dimension)/dimension+ " "+ i+"\n");
    		toReturn[(i-i%dimension)/dimension][i%dimension]=input[i];
    	}
    	return toReturn;
    }
	private class Solve implements Iterable<Board>{
    	BoardNode curr=solution;
    	
    	public Iterator<Board> iterator(){
    		return new SolveIterator();
    	}
    	
    	class SolveIterator implements Iterator<Board>{
    		public boolean hasNext(){
    			if(curr.previous!=null) return true;
    			return false;
    		}
    		public void remove(){throw new UnsupportedOperationException();}
    		public Board next(){
    			Board temp = curr.current;
    			curr=curr.previous;
    			return temp;
    		}
    	}
    }
    class BoardNode{
    	public Board current;
    	public BoardNode previous;
    	public BoardNode[] neighbors;
    	public int moves = 0;
    	
    	public BoardNode(Board current, BoardNode previous){
    		this.current=current;
    		this.previous = previous;
    		if(previous!=null) moves = previous.moves+1; 
    	}
    	public BoardNode(Board current){
    		this(current, null);
    	}
    	public void findNeighbors(){
    		int i=0;
    		neighbors = new BoardNode[4];
    		for(Board p : current.neighbors()){
    			if(this.previous!=null){
    				if(p.equals(this.previous.current)) continue;
    			}
    			neighbors[i++] = new BoardNode(p,this);
    		}
    	}
    	public int manhattanPriority(){
    		return current.manhattan()+moves;
    	}
    }
}

*/