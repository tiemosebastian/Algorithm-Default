

public class WordNet {
	private SAP wnet;
	private Wnet Index[];
	private int length;
	private RedBlackBST<String, Bag<Integer>> ref=new RedBlackBST<String, Bag<Integer>>();
		/**
		 *  constructor takes the name of the two input files
		 * @param synsets
		 * @param hypernyms
		 */
	   public WordNet(String synsets, String hypernyms){
		   if(synsets==null||hypernyms==null) throw new NullPointerException();
		   In syn = new In(synsets);
		   Queue<Wnet> Q = new Queue<Wnet>();//Use Queue to 'count' file length.
		   
		   while(!syn.isEmpty()){
			   String cur[]= syn.readLine().split(",");
			   int v = Integer.parseInt(cur[0]);
			   Q.enqueue(new Wnet(v,cur[1],cur[2]));// Don't need to store gloss
			   for(String m:cur[1].split(" ")){//put nouns into BST
				   if(!ref.contains(m)) ref.put(m,new Bag<Integer>());
				   ref.get(m).add(v);
			   }
		   }
		   int n=0;
		   Index=new Wnet[Q.size()];
		   length=Q.size();
		   for(Wnet q:Q){
			   if(n!=q.id) StdOut.print("\n\n\n\nn!=v:Good thing I put this if loop in to test this...");
			   Index[n++]=q;
		   }
		   In hyp = new In(hypernyms);
		   Digraph tempgraph =new Digraph(Q.size());
		   Q=null;
		   while(!hyp.isEmpty()){
			   String cur[]= hyp.readLine().split(",");
			   int v = Integer.parseInt(cur[0]);
			   for(int i=1;i<cur.length;i++){
				   int w=Integer.parseInt(cur[i]);
				   if(v==w) continue;
				   tempgraph.addEdge(v, w);
		   		}
		   }
		   if(!isRootedDAG(tempgraph)) throw new IllegalArgumentException("Input must correspond to rooted DAG tree");
		   wnet=new SAP(tempgraph);
	   }
	   /***
	    * returns all WordNet nouns
	    * @return
	    */
	   
	   public Iterable<String> nouns(){
		   return ref.keys();
	   }
	   /**
	    * * is the word a WordNet noun?
	    */
	   public boolean isNoun(String word){
		   if(word==null) throw new NullPointerException();
		   if(ref.contains(word)) return true;
		   return false;
	   }

	   /**
	    * distance between nounA and nounB (defined below)
	    */
	   public int distance(String nounA, String nounB){
		   if(nounA==null||nounB==null) throw new NullPointerException();
		   if(!isNoun(nounA)||!isNoun(nounB)) throw new IllegalArgumentException();
		   return wnet.length(ref.get(nounA),ref.get(nounB));
	   }
	   
	   /**
	    *  a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	    *   in a shortest ancestral path (defined below)
	    * @param nounA
	    * @param nounB
	    * @return
	    */
	   public String sap(String nounA, String nounB){
		   if(nounA==null||nounB==null) throw new NullPointerException();
		   if(!isNoun(nounA)||!isNoun(nounB)) throw new IllegalArgumentException();		  
		   int in=wnet.ancestor(ref.get(nounA), ref.get(nounB));
		   if(in==-1) return "No common ancestor";
		   return Index[in].nouns;
	   }
	   /***************************************************************************************
	    * Private Methods
	    **************************************************************************************/
	   /**
	    * Checks to ensure Digraph establishing hypernyms has exactly one root and it Acyclic
	    * Using the Kosaraju-Sharir method for finding Strong Connected Components, and ensuring
	    * that there are as many, as there are components.
	    * Checks each vertex to ensure there is exactly one with outdegree==1
	    * @param w
	    * @return
	    */
	   private boolean isRootedDAG(Digraph G){
		   KosarajuSharirSCC test=new KosarajuSharirSCC(G);
    	   if(test.count()!=G.V()) throw new IllegalArgumentException("Digraph must be Acyclic");
    	   int count=0;
    	   for(int i = 0; i<G.V();i++){
    		   if(G.outdegree(i)==0) count++;
    	   }
    	   if(count!=1) throw new IllegalArgumentException("Digraph must have exactly one root");
		   return true;
	   }
	   /**
	    * local class holds information in the Wordnet:
	    * id, nouns, and a method for getting the nouns split as an iterable 
	    * @author tiemo
	    *
	    */
	   private class Wnet{
		   public int id;
		   public String nouns;
		//   public String gloss;
		   
		   public Wnet(int id,String nouns,String gloss){
			   this.id=id;
			//   this.gloss=gloss;
			   this.nouns=nouns;
		   }
		   
		   public Bag<String> nouns(){
			   Bag<String> N = new Bag<String>(); 
			   for(String n:nouns.split(" ")){
				   N.add(n);
			   }
			   return N;
		   }
	   }
	   /**
	    * Checks to ensure the id of each word matches its identifier
	    * @return
	    */
	   private boolean idCheck(){
		   boolean flag=false;
		   for(Wnet i:Index){
			   for(String m: i.nouns()){
				   if(!contains(ref.get(m),i.id)){
					   StdOut.print("\n get noun doesn't match id.\nref.get(m): "+ref.get(m)+" \n id: "+i.id+"\nnoun: "+
							   m+"\n expected: " +i.nouns+"\n word at misplaced reference: " +Index[84].nouns);
					   flag=true;
					   }
				   boolean flag2 =false;
				   for(int j:ref.get(m)){
					   if(contains(Index[j].nouns(),m)) {
						   flag2=true;
					   }
					   if(flag2==false){
						   StdOut.print("\n m: " +m);
						   StdOut.print("\nm is not in one of the ids that it should be in?\n");
						   StdOut.print("\n" +m+ " is in:"+ printit(ref.get(m))+ " which has: " + printits(Index[j].nouns())); 
						   flag=true;
					   }
				   }
			   }
		   }
		   return !flag;
	   }
	   /**
	    * check for containment in an iterable
	    * @param b
	    * @param x
	    * @return
	    */
	   
	   private static boolean contains(Iterable<String> b,String x){
			   for(String n:b){
				   if(n.compareTo(x)==0) return true;
			   }
			   return false;
		   }
	   /**
	    * check for containment in an iterable
	    * @param b
	    * @param x
	    * @return
	    */
	   private static boolean contains(Iterable<Integer> b,int x){
		   for(int n:b){
			   if(n==x) return true;
		   }
		   return false;
	   }
	   /**
	    * easy printing of iterable
	    * @param p
	    * @return
	    */
	   private static String printit(Iterable<Integer> p){
		   String temp = "";
		   for(int i: p){
			   StdOut.print(i);
			   temp=temp.concat(","+Integer.toString(i));
		   }
		   return temp;
	   }
	   /**
	    * easy printing of iterable
	    * @param p
	    * @return
	    */
	   private static String printits(Iterable<String> p){
		   String temp = "";
		   for(String i: p){
			   StdOut.print(i);
			   temp=temp.concat("\n "+i);
		   }
		   return temp;
	   }
	   /**
	    * Times relevant operation n*2^k times k in {0:p}
	    * @param n
	    * @param p
	    * @return
	    */
	   private double[] timing(int n,int p){
		   double dtime[]=new double[p];
		   Stopwatch timer;
		   for(int j=0;j<p;j++){
		   		timer=new Stopwatch();
		   		for(int i=0;i<n*Math.pow(2, j);i++) distance(rW(),rW());
		   		dtime[j]=timer.elapsedTime();
		   }
		   double atime[]=new double[p];
		   for(int j=0;j<p;j++){
		   		timer=new Stopwatch();
		   		for(int i=0;i<n*Math.pow(2, j);i++) sap(rW(),rW());
		   		atime[j]=timer.elapsedTime();
		   }
		   StdOut.print("\ndistance iterations: " +n+" X "+Math.pow(2,0)+" time: "+dtime[0]+" Ratio: ");
		   for(int i=1;i<p;i++){
			   StdOut.print("\ndistance iterations: " +n+" X "+Math.pow(2,i)+" time: "+dtime[i]+" Ratio: "+Math.log(dtime[i]/dtime[i-1])/Math.log(2));
		   }
		   StdOut.print("\n\n\n\nancestor iterations: " +n+" X "+Math.pow(2,0)+" time: "+atime[0]+" Ratio: ");
		   for(int i=1;i<p;i++){
			   StdOut.print("\nancestor iterations: " +n+" X "+Math.pow(2,i)+" time: "+atime[i]+" Ratio: "+Math.log(atime[i]/atime[i-1])/Math.log(2));
		   }
		   return new double[] {dtime[p-1],atime[p-1]};
	   }
	   /**
	    * Random Word from synset 
	    * @return
	    */
	   private String rW(){   
		   return rW(1)[0];
	   }
	   /**
	    * Set of Random Words from synset
	    * @param v1
	    * @return
	    */
	   private String[] rW(int v1){
		   int l=length;
		   String[] toret=new String[v1];
		   for(int i=0;i<v1;i++){
			   toret[i]=Index[StdRandom.uniform(0,l)].nouns().iterator().next();
		   }
		   return toret;
	   }
	   ///////////////////////////////////////////////////////////////////////////////////////
	   ///////////////////////////////////////////////////////////////////////////////////////
	   public static void main(String[] args){
		   Stopwatch timer = new Stopwatch();
		   WordNet test=new WordNet("/Users/tiemo/Downloads/wordnet/synsets.txt",
				   "/Users/tiemo/Downloads/wordnet/hypernyms.txt");
		   StdOut.print("\n time 80: " + timer.elapsedTime());
		   timer=new Stopwatch();
		   WordNet test1=new WordNet("/Users/tiemo/Downloads/wordnet/synsets1000-subgraph.txt",
				   "/Users/tiemo/Downloads/wordnet/hypernyms1000-subgraph.txt");
		   StdOut.print("\n time 1: " + timer.elapsedTime());
		   timer=new Stopwatch();
		   WordNet test2=new WordNet("/Users/tiemo/Downloads/wordnet/synsets5000-subgraph.txt",
				   "/Users/tiemo/Downloads/wordnet/hypernyms5000-subgraph.txt");
		   StdOut.print("\n time 5: " + timer.elapsedTime());
		   timer=new Stopwatch();
		   WordNet test3=new WordNet("/Users/tiemo/Downloads/wordnet/synsets10000-subgraph.txt",
				   "/Users/tiemo/Downloads/wordnet/hypernyms10000-subgraph.txt");
		   StdOut.print("\n time 10: " + timer.elapsedTime());
		   timer=new Stopwatch();
		   WordNet test4=new WordNet("/Users/tiemo/Downloads/wordnet/synsets50000-subgraph.txt",
				   "/Users/tiemo/Downloads/wordnet/hypernyms50000-subgraph.txt");
		   StdOut.print("\n time 50: " + timer.elapsedTime());
		   timer=new Stopwatch();
		  
		   assert test.idCheck():"idCheck failed";
		   In in=new In();
		   int i=0;
		   for(String s:test.nouns()){
			   i++;
		   }
		   StdOut.print("\n\nnouns: " +i);
		   StdOut.print("\n\nRBBST: " +test.ref.size());
		   StdOut.print("\n\nentries: " +test.Index.length);
		   while(!in.isEmpty()){
			   int n=in.readInt();
			   int p=in.readInt();
			   double t[][]=new double[5][2];
			   t[0]=test.timing(n,p);
			   t[1]=test1.timing(n,p);
			   t[2]=test2.timing(n,p);
			   t[3]=test3.timing(n,p);
			   t[4]=test4.timing(n,p);
			   /*StdOut.print("\n\n\nE+V: "+ test1.wnet.G.E()+" + "+test1.wnet.G.V()+" time: " +t[1][0]+","+t[1][1]+" rel: "+t[1][0]+","+t[1][1]);
			   StdOut.print("\nE+V: "+ test2.wnet.G.E()+" + "+test2.wnet.G.V()+" time: " +t[2][0]+","+t[2][1]+" rel: "+t[2][0]/5+","+t[2][1]/5);
			   StdOut.print("\nE+V: "+ test3.wnet.G.E()+" + "+test3.wnet.G.V()+" time: " +t[3][0]+","+t[3][1]+" rel: "+t[3][0]/10+","+t[3][1]/10);
			   StdOut.print("\nE+V: "+ test4.wnet.G.E()+" + "+test4.wnet.G.V()+" time: " +t[4][0]+","+t[4][1]+" rel: "+t[4][0]/50+","+t[4][1]/50);
			   StdOut.print("\nE+V: "+ test.wnet.G.E()+" + "+test.wnet.G.V()+" time: " +t[0][0]+","+t[0][1]+" rel: "+t[0][0]/81+","+t[0][1]/81);*/
			   /*String a=in.readString();
			   String b=in.readString();
			   StdOut.print("\ngeta: " +printit(test.ref.get(a)));
			   StdOut.print("\ngetb: " +printit(test.ref.get(b)));
			   StdOut.print("\ndist("+a+","+b+"): "+test.distance(a,b));
			   StdOut.print("\nancestor("+a+","+b+"): "+test.sap(a,b));*/
		   }
	   }
	   
}
