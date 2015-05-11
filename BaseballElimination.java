
public class BaseballElimination {
    private final int N;
    private final int maxid;
    private final int schedule[][];
    private final int C;
    private final int s;
    private final int t;
    private final BST<String,Integer> ids;
    private final Team teams[];
    /**
     * / create a baseball division from given filename in format specified below
     */
    public BaseballElimination(String filename){
        In file = new In(filename);
		N = file.readInt();
		ids = new BST<String,Integer>();
		schedule = new int[N][N];
		C=(N-1)*(N-2)/2;					//number of games remaining between other teams.(N-1)((N-1)+1)/2
		s=N+C;
		t=N+C+1;
		teams=new Team[N];
		int tw=0;
		int mi=0;
		for(int i=0; i<N; i++){
			String team = file.readString();
			int wins = file.readInt();
			if(wins>tw) {tw=wins; mi=i;}
			int losses = file.readInt();
			int remaining = file.readInt();			
			for(int j=0;j<N; j++){
				schedule[i][j]=file.readInt();
			}
			ids.put(team, i); 
			teams[i]=new Team(team, wins, losses, remaining,i);
		}
		maxid=mi;
	}
	
	/**
	 * // number of teams
	 */
	public int numberOfTeams(){
		return N;
	}
	/**
	 * Returns teams in Dataset
	 */
	public Iterable<String> teams(){
		return ids.keys();
	}
	/**
	 * number of wins for given team
	 * @param team
	 * @return
	 */
	public int wins(String team) {
		checkteam(team);
		int id = ids.get(team);
		return teams[id].W;
	}
	/**
	 * number of losses for given team
	 * @param team
	 * @return
	 */
	public int losses(String team){
		checkteam(team);
		int id= ids.get(team);
		return teams[id].L;
	}
	/**
	 * number of remaining games for given team
	 * @param team
	 * @return
	 */
	public int remaining(String team) {
		checkteam(team);
		int id = ids.get(team);
		return teams[id].R;
	}
	/**
	 *  number of remaining games between team1 and team2
	 * @param team1
	 * @param team2
	 * @return
	 */
	public int against(String team1, String team2) {
		checkteam(team1);
		checkteam(team2);
		
		int id1=ids.get(team1);
		int id2=ids.get(team2);
		
		assert schedule[id1][id2]==schedule[id2][id1];
		
		return schedule[id1][id2];
	}
	/**
	 *  is given team eliminated?
	 * @param team
	 * @return
	 */
	public boolean isEliminated(String team) {
		checkteam(team);
		Team T = teams[ids.get(team)];
		if(T.cache) return T.elim;
		elim(team); 
		return T.elim;
	}
	/**
	 * subset R of teams that eliminates given team; null if not eliminated
	 * @param team
	 * @return
	 */
	public Iterable<String> certificateOfElimination(String team){
		checkteam(team);
		if(!isEliminated(team)) return null;
		return teams[ids.get(team)].eliminators;
	}
	/**********************************************************************************************************
	 *  Private methods
	 *********************************************************************************************************/
	private boolean trivialElim(String team){
		checkteam(team);
		if(wins(team)+remaining(team)<teams[maxid].W) {
			int id=ids.get(team);
			teams[id].setElim(true);
			StdOut.print("TRIVELIM: ");
			teams[id].addEliminator(maxid);
			return true;
		}
		return false;
	}
	private boolean checkteam(String team){
		if(team==null) throw new NullPointerException();
		if(ids.contains(team)==false) throw new IllegalArgumentException();
		return true;
	}
	private boolean elim(String team){
		checkteam(team);
		int id=ids.get(team);
		Team T=teams[id];
		if(T.cache) return T.elim;
		if(trivialElim(team)) return T.elim;
		FlowNetwork G = new FlowNetwork(N+C+2);
		int e=N;
		for(int i=0; i<N; i++){
			if(i==id) continue;
			G.addEdge(new FlowEdge(i,t,T.W+T.R-teams[i].W));
			for(int j=i;j<N;j++){
				if(j==id) continue;
				if(j==i) continue;
				G.addEdge(new FlowEdge(s,e,schedule[i][j]));
				G.addEdge(new FlowEdge(e,i,Double.POSITIVE_INFINITY));
				G.addEdge(new FlowEdge(e,j,Double.POSITIVE_INFINITY));
				e++;
			}
		}
		FordFulkerson Elim = new FordFulkerson(G,s,t);
		double flow=Elim.value();
		int sum=0;
		for(int i = 0;i<N; i++){
			if(i==id) continue;
			for(int j=0;j<N;j++){
				if(j==id) continue;
				sum=sum + schedule[j][i];
			}
		}
		T.setElim(flow != sum/2);
		for(int i=0;i<N;i++){
			if(i==id) continue;
			if(Elim.inCut(i)) teams[id].addEliminator(i);
		}
		return flow!=sum/2;
	}
	/**********************************************************************************************************
	 *  Private Class Team
	 *********************************************************************************************************/
	private class Team{
		private boolean cache=false;
		private final String name;
		private final int W;
		private final int L;
		private final int R;
	//	private final int id;
		private boolean elim=false;
		private Bag<String> eliminators = new Bag<String>();
		
		public Team(String N, int W, int L, int R, int id){
			name=N;
			this.W=W;
			this.L=L;
			this.R=R;
		//	this.id=id;
		}
		public void setElim(boolean elim){
			this.elim=elim;
			cache=true;
		}
		public void addEliminator(int elim){
			eliminators.add(teams[elim].name);
		}
	}
	public static void main(String args[]){
		BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }	
	}
}