import java.lang.*;
import java.util.*;
import java.io.*;

public class Solver {
	private final Board game;
	private final Board twin;
	private MinPQ<BoardNode> gamePQ;
	private MinPQ<BoardNode> twinPQ;
	private final BoardNode solution;
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
    public Solver(Board initial){           // find a solution to the initial board (using the A* algorithm)
    	game=initial;
    	twin=game.twin();
    	gamePQ = new MinPQ<BoardNode>(MANHATTAN);
    	twinPQ = new MinPQ<BoardNode>(MANHATTAN);
    	
    	int counter=0;
    	gamePQ.insert(new BoardNode(game));
    	twinPQ.insert(new BoardNode(twin));
    	while(gamePQ.min().current.isGoal()==false && twinPQ.min().current.isGoal()==false){
    		gamePQ.min().findNeighbors();
    		twinPQ.min().findNeighbors();
    		for(BoardNode p: gamePQ.delMin().neighbors){
    			if(p==null) continue;
    			//StdOut.print(p.current.toString());
    			gamePQ.insert(p);
    			counter++;
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
    		solution = null;
    		solveable=false;
    	}
    	
    }
    public boolean isSolvable(){            // is the initial board solvable?
    	return solveable;
    }
    public int moves(){             
    //	StdOut.print(gamePQ.size());// min number of moves to solve initial board; -1 if unsolvable
    	if(!isSolvable()) return -1;
    	return solution.moves;
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
    	if(!isSolvable()) return null;
    	Stack<Board> temp = new Stack<Board>();
    	for(Board i : new Solve()){
    		temp.push(i);
    	}
    	return temp;
    }
    public static void main(String[] args){ // solve a slider puzzle (given below)
    	String dir = "/Users/tiemo/Desktop/Java Shit/8puzzle";
		File directory = new File(dir);
		File[] matches = directory.listFiles( new FilenameFilter()
				{
			public boolean accept(File directory, String name)
			{ 
				return name.endsWith(".txt");
			}
		});
		for(File i: matches){
		StdOut.println(i.toString());
		}
    	inport(new String[] {matches[4].toString()});
    	
    	/*int testarray[] = new int[]{1,2,3,4,5,6,7,8,0};
    	Solver test=new Solver(new Board(unflatten(testarray,3)));
    	Stopwatch timer = new Stopwatch();
    	for(int i=0;i<1000;i++)
    	{
    		StdRandom.shuffle(testarray);
    		test = new Solver(new Board(unflatten(testarray,3)));
    	}
    	StdOut.print(timer.elapsedTime()+"\n");
    	if(test.isSolvable()){
    		for(Board i : test.solution()){
    			StdOut.print(i.toString());
    		}
    	}
    	StdOut.print("Is the test solvable? "+test.isSolvable()+"\n");
    	StdOut.print("It took " + test.moves() + " moves.");
    	*/
    	// create initial board from file
    }
    private static int[][] unflatten(int[] blocks,int dim){
    	int unflat[][] = new int[dim][dim];
    	for(int i = 0; i<blocks.length;i++){
    		unflat[(i-i%dim)/dim][i%dim]=blocks[i];
    	}
    	return unflat;
    }
    private static void inport(String[] args){
        In in = new In(args[0]);
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
                StdOut.println(board);
        }
        for(Board board: solver.solution()){
        	StdOut.print("\nboard "+board.manhattan());
        	for( Board b:board.neighbors()){
        		StdOut.print("\nb "+b.manhattan());
        		for(Board bb: b.neighbors()){
        			StdOut.print("\nbb "+bb.manhattan());
        		}
        	}
        }
    }
    
    /////////////////////////////////////////////////////////////////////////
    
	private class Solve implements Iterable<Board>{
    	BoardNode curr=solution;
    	
    	public Iterator<Board> iterator(){
    		return new SolveIterator();
    	}
    	
    	class SolveIterator implements Iterator<Board>{
    		public boolean hasNext(){
    			if(curr!=null) return true;
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
    private class BoardNode{
    	public Board current;
    	public BoardNode previous;
    	public BoardNode[] neighbors;
    	public final int moves;
    	public final int manpri;
    	
    	public BoardNode(Board current, BoardNode previous, int manpri){
    		this.current=current;
    		this.previous = previous;
    		if(previous!=null) moves = previous.moves+1;
    		else moves=0;
    		//if(Math.abs(manpri)>1) this.manpri=current.manhattan()+moves;
    		//else{
    		this.manpri = current.manhattan()+moves;
    		//}
    	}
    	public BoardNode(Board current, BoardNode previous){
    		this(current, previous, -1000);
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
    		return manpri;
    	}
    }
}
