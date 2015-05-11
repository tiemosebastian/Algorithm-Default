import java.lang.*;
import java.util.*;

public class Solverrel {
	final Boardrel game;
	final Boardrel twin;
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
    public Solverrel(Boardrel initial){           // find a solution to the initial board (using the A* algorithm)
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
    	StdOut.print(gamePQ.size());// min number of moves to solve initial board; -1 if unsolvable
    	if(!isSolvable()) return -1;
    	return solution.moves;
    }
    public Iterable<Boardrel> solution() {     // sequence of boards in a shortest solution; null if unsolvable
    	if(!isSolvable()) return null;
    	return new Solve();
    }
    public static void main(String[] args){ // solve a slider puzzle (given below)
    	int testarray[] = new int[]{1,2,3,4,5,6,7,8,0};
    	Solverrel test=new Solverrel(new Boardrel(testarray));
    	Stopwatch timer = new Stopwatch();
    	for(int i=0;i<1;i++)
    	{
    		StdRandom.shuffle(testarray);
    		test = new Solverrel(new Boardrel(testarray));
    	}
    	StdOut.print(timer.elapsedTime()+"\n");
    	/*if(test.isSolvable()){
    		for(Boardrel i : test.solution()){
    			StdOut.print(i.toString());
    		}
    	}*/
    	StdOut.print("Is the test solvable? "+test.isSolvable()+"\n");
    	StdOut.print("It took " + test.moves() + " moves.");
    	
    	// create initial board from file
       /* In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Boardrel initial = new Boardrel(blocks);

        // solve the puzzle
        Solverrel solver = new Solverrel(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Boardrel board : solver.solution())
                StdOut.println(board);*/
        }
    
    
    /////////////////////////////////////////////////////////////////////////
    
	private class Solve implements Iterable<Boardrel>{
    	BoardNode curr=solution;
    	
    	public Iterator<Boardrel> iterator(){
    		return new SolveIterator();
    	}
    	
    	class SolveIterator implements Iterator<Boardrel>{
    		public boolean hasNext(){
    			if(curr.previous!=null) return true;
    			return false;
    		}
    		public void remove(){throw new UnsupportedOperationException();}
    		public Boardrel next(){
    			Boardrel temp = curr.current;
    			curr=curr.previous;
    			return temp;
    		}
    	}
    }
    class BoardNode{
    	public Boardrel current;
    	public BoardNode previous;
    	public BoardNode[] neighbors;
    	public final int moves;
    	public final int manpri;
    	
    	public BoardNode(Boardrel current, BoardNode previous, int manpri){
    		this.current=current;
    		this.previous = previous;
    		if(previous!=null) moves = previous.moves+1;
    		else moves=0;
    		//if(Math.abs(manpri)>1) this.manpri=current.manhattan()+moves;
    		//else{
    		this.manpri = current.manhattan()+moves;
    		//}
    	}
    	public BoardNode(Boardrel current, BoardNode previous){
    		this(current, previous, -100);
    	}
    	public BoardNode(Boardrel current){
    		this(current, null);
    	}
    	public void findNeighbors(){
    		int i=0;
    		neighbors = new BoardNode[4];
    		for(Boardrel p : current.neighbors()){
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
