public class SAP {
	private Digraph G;
	private SAPsearch G1;
	/************************************************************************
     *Public Methods 
     ***********************************************************************/
       
	/**
        *  constructor takes a digraph (not necessarily a DAG)
        * @param G
        */
       public SAP(Digraph G){
    	   nullcheck(G);
    	   //DAGCheck(G);
    	   this.G=new Digraph(G);
    	   G1 = new SAPsearch(G);
       }

       /** 
        * length of shortest ancestral path between v and w; -1 if no such path
        * @param v
        * @param w
        * @return
        */
       public int length(int v, int w){
    	   vertexCheck(v);
    	   vertexCheck(w);
    	   G1.search(v,w);
    	   return G1.length();
    	   }
       
       /**
        *  a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
        * @param v
        * @param w
        * @return
        */
       public int ancestor(int v, int w){
    	   vertexCheck(v);
    	   vertexCheck(w);
    	   G1.search(v,w);
    	   return G1.ancestor();
    	   }

       /**
        *  length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
        * @param v
        * @param w
        * @return
        */
       public int length(Iterable<Integer> v, Iterable<Integer> w){
    	   vertexCheck(v);
    	   vertexCheck(w);
           G1.search(v,w);
           return G1.length();
    	   }

       /**
        *  a common ancestor that participates in shortest ancestral path; -1 if no such path
        * @param v
        * @param w
        * @return
        */
       public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
    	   vertexCheck(v);
    	   vertexCheck(w);
    	   G1.search(v,w);    	   
    	   return G1.ancestor();
    	   }

       /************************************************************************
        *Private Methods 
        ***********************************************************************/
       
       /**
        * Check to ensure the vertex is a legal point.
        * @param v
        */
       private void vertexCheck(int v){
    	   nullcheck(v);
    	   if(v<0||v>G.V()-1) throw new IndexOutOfBoundsException();
       }
       /**
        * Check whether a set of vertices are legal.
        * @param v
        */   
       private void vertexCheck(Iterable<Integer> v){
    	   nullcheck(v);
    	   for(int w:v){
    		   vertexCheck(w);
    	   }
       }
       /**
        * Check if the object is null.
        * @param notnull
        */
       private void nullcheck(Object notnull){
    	   if(notnull==null) throw new NullPointerException();
       }
       /**
        * Not actually used, but useful to ensure the Digraph is Acyclic
        * @param G
        *//*
       private void DAGCheck(Digraph G){
    	   nullcheck(G);
    	   KosarajuSharirSCC test=new KosarajuSharirSCC(G);
    	   if(test.count()!=G.V()) throw new IllegalArgumentException("Digraph must be Acyclic");
       }
       */
       
      /**
       * Test the Equivalence relationship properties for every set of mutual points in G
       * @param G
       * @return
       */
       private static boolean TotalEqTest(Digraph G){
    	   boolean flag=true;
    	   for(int i=0;i<G.V();i++){
   	    	for(int j=0;j<G.V();j++){
   	    		for(int k=0;k<G.V();k++){
   	    			if(!EqTest(G,i,j,k)) flag=false;
   	    		}
   	    	}
   	    }
    	   return flag;
       }
       /**
        * Test Equivalence relationship properties for G, and three vertices.
        * return false if not upheld. meant to be used with assert. 
        * @param G
        * @param v
        * @param w
        * @param x
        * @return
        */
       private static boolean EqTest(Digraph G,int v,int w, int x){
    	   boolean flag=false;
    	   SAP sap = new SAP(G);
    	   int test[] = new int[]{v,w,x};
    	   for(int i:test){
    		   if(sap.length(i,i)==-1) {StdOut.print("\n for some x, length reflexivity doesn't hold"); flag=true;}
    		   if(sap.length(i,i)!=0) {StdOut.print("\n for some x, length(x,x)!=0"); flag=true;}
    		   if(sap.ancestor(i,i)==-1) {
    			   StdOut.print("\n x = "+i+""+sap.ancestor(i,i));
    			   StdOut.print("\n for some x, ancestor reflexivity doesn't hold"); 
    			   flag=true;
    			   }
    		   if(sap.ancestor(i,i)!=i) {StdOut.print("\n for some x, ancestor(x,x)!=x"); flag=true;}
    		   if(sap.length(i,(i+1)%3)!=sap.length((i+1)%3,i)) {StdOut.print("\n for some v,w length Symmetry doesn't hold"); flag=true;}
    		   if(sap.ancestor(i,(i+1)%3)!=sap.ancestor((i+1)%3,i)) {StdOut.print("\n for some v,w ancestor Symmetry doesn't hold"); flag=true;}
    	   }
    	   if(!Trans(G,v,w,x)) flag=true;
    	   return !flag;
       }
       /**
        * Test the Transitivity property only called from EqTest
        * @param G
        * @param v
        * @param w
        * @param x
        * @return
        */
       private static boolean Trans(Digraph G,int v,int w, int x){
    	   boolean flag=false;
    	   SAP sap = new SAP(G);
    	   int test[] = new int[]{v,w,x};
    	   for(int i:test){
    		   if(sap.length(i,(i+1)%3)!=-1&&sap.length((i+1)%3,(i+2)%3)!=-1){
    			   if(sap.length(i,(i+2)%3)==-1) {StdOut.print("\nfor some, v,w,x lengthTransitivity doesn't hold"); flag=true;}
    		   }
    		   if(sap.ancestor(i,(i+1)%3)!=-1&&sap.ancestor((i+1)%3,(i+2)%3)!=-1){
    			   if(sap.ancestor(i,(i+2)%3)==-1) {StdOut.print("\nfor some, v,w,x lengthTransitivity doesn't hold"); flag=true;}
    		   }
    	   }
    	   return !flag;
       }
       ////////////////////////////////////////////////////////////////////////////////////
       ////////////////////////////////////////////////////////////////////////////////////
       public static void main(String[] args){
    	    In in = new In("/Users/tiemo/Downloads/wordnet/digraph3.txt");
    	    
    	    Digraph G = new Digraph(in);
    	    SAP sap = new SAP(G);
    	    assert TotalEqTest(G): "TotalEqTest failed.";

    	    while (!StdIn.isEmpty()) {
    	    	Bag<Integer> v1= new Bag<Integer>();
    	        Bag<Integer> w1= new Bag<Integer>();
    	        int v2 = StdIn.readInt();
    	        int w2 = StdIn.readInt();
    	        int length2   = sap.length(v2, w2);
    	        int ancestor2 = sap.ancestor(v2, w2);
    	        StdOut.printf("length = %d, ancestor = %d\n", length2, ancestor2);
    	    	StdOut.print("\n\nHow many vs?\n");
    	    	int nv = StdIn.readInt();
    	    	StdOut.print("\n\nHow many ws?\n");
    	    	int nw = StdIn.readInt();
    	    	StdOut.print("\n\nEnter vs:\n");
    	    	for(int i=0;i<nv;i++){
    	    		int v = StdIn.readInt();
    	    		v1.add(v);
    	    	}
    	        StdOut.print("\n\nEnter ws:\n");
    	        for(int i=0;i<nw;i++){
    	    		int w = StdIn.readInt();
    	    		w1.add(w);
    	    	}
    	       // assert(sap.G1.testiter(v1,w1));
    	        int length   = sap.length(v1, w1);
    	        int ancestor = sap.ancestor(v1, w1);
    	        StdOut.printf("\n length = %d, ancestor = %d\n", length, ancestor);
    	    }
    	    
       }
    }


