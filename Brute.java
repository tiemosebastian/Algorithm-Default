import java.util.Arrays;


public class Brute {
	
	public static void main(String[] args) {
		run(args);
		//run(new String[] {"/Users/tiemo/Desktop/Java Shit/collinear/input8.txt"});
		/*final Point points[] = shotgun(3500,3500); 
		Stopwatch timer = new Stopwatch();
        printPointArray(findLines(points));
        StdOut.print("\nslowbrute: "+ timer.elapsedTime());
        timer=new Stopwatch();
        printPointArray(findLines2(points));
        StdOut.print("\nfastbrute: "+timer.elapsedTime());*/
	}
	private static void setSettings(){		
		StdDraw.setXscale(0,32768);
		StdDraw.setYscale(0,32768);
	}
	private static void run(String[] args){
		setSettings();
		In in = new In(args[0]);      // input file
        int N = in.readInt();         // N-by-N percolation system
        int k=0;
        Point points[] = new Point[N];
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            points[k++]= new Point(i,j);
            points[k-1].draw();
        }
        printPointArray(findLines2(points));
	}
	/*private static Point[][] findLines(Point[] test){
		Point degen[][] = new Point[0][];
		for(Point s : test){
    		for(Point t : test){
    			if(s.compareTo(t)==0) continue;
    			for(Point u: test){
    				if(s.compareTo(u)==0||t.equals(u)) continue;
    				if(s.slopeTo(t)!=s.slopeTo(u)) continue;
    				for(Point v:test){
    					if(s.equals(v)||t.equals(v)||u.equals(v)) continue;
    					if(s.slopeTo(t)!=s.slopeTo(v)) continue;
    					degen = addIfUnique(degen,s,t,u,v);
    				}
    			}
    		}
    	}
		return degen;
	}*/
	private static Stack<Point[]> findLines2(Point[] test){
		Stack<Point[]> degen = new Stack<Point[]>();
		for(Point s : test){
    		for(Point t : test){
    			if(s.compareTo(t)!=-1) continue;
    			for(Point u: test){
    				if(t.compareTo(u)!=-1) continue;
    				if(s.slopeTo(t)!=s.slopeTo(u)) continue;
    				for(Point v:test){
    					if(u.compareTo(v)!=-1) continue;
    					if(s.slopeTo(t)!=s.slopeTo(v)) continue;
    					degen.push(new Point[]{s,t,u,v});
    				}
    			}
    		}
    	}
		return degen;
	}
	private static void printPointArray(Stack<Point[]> degen){
		StdDraw.setPenRadius(.00625);
		for(Point[] p : degen){
			for(int i = 0; i< p.length-1;i++){
				StdOut.print(p[i].toString()+" -> "); 
			}
			p[0].drawTo(p[p.length-1]);
			StdOut.println(p[p.length-1]);
		}
    //	StdOut.print("end");
	}
	/*private static void printPointArray(Point[][] degen){
		StdDraw.setPenRadius(.00625);
		for(Point[] p : degen){
			for(int i = 0; i< p.length-1;i++){
				StdOut.print(p[i].toString()+" -> "); 
			}
			p[0].drawTo(p[p.length-1]);
			StdOut.println(p[p.length-1]);
		}
    	StdOut.print("end");
	}*/
	
	/*private static Point[][] addIfUnique(Point[][] degen, Point s, Point t, Point u, Point v){
		Point temp[][] = new Point[degen.length+1][];
		Point[] tempar = new Point[] {s, t, u, v};
		Arrays.sort(tempar);
		if(checkDegeneracy(tempar,degen)) return degen;
		temp[degen.length] = tempar;
		for(int i = 0; i<degen.length; i++){
			temp[i]=degen[i]; 
		}
		return temp;
	}*/
	
	/*private static boolean checkDegeneracy(Point[] tempar, Point[][] degen){
		boolean p = false;
		for(Point[] q:degen){
			if(tempar[0].compareTo(q[0])==0 && tempar[1].compareTo(q[1])==0 && tempar[2].compareTo(q[2])==0 &&tempar[3].compareTo(q[3])==0) p = true;
		}
		return p;
	}
	*/
	private static Point[] shotgun(int size, int sample){
    	StdDraw.setXscale(0,sample);
    	StdDraw.setYscale(0,sample);
    	StdDraw.setPenRadius(.025);
		Point test[] = new Point[size];
		for(int i=0;i<size;i++){
    		test[i] = new Point(StdRandom.uniform(0,sample),StdRandom.uniform(0,sample));
    		test[i].draw();
    	}
		Arrays.sort(test);
		for(int i=0; i<test.length-1;i++){
			if(test[i].compareTo(test[i+1])==0) {
				StdDraw.clear();
				test=shotgun(size, sample);
			}
		}
			
		return test;
	}
}

