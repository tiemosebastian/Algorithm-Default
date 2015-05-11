import java.util.*;

//import java.io.*;
public class Fast{
	private static Point MaxPoint= new Point(Integer.MAX_VALUE,Integer.MAX_VALUE);
	public static void main(String[] args){
		run(args);
	}
	private static void setSettings(){		
		StdDraw.setXscale(0,32768);
		StdDraw.setYscale(0,32768);
	}
	private static void run(String args[]){
		setSettings();
		In in = new In(args[0]);      // input file
        int N = in.readInt();         // N-by-N percolation system
        int k=0;
        Point points[] = new Point[N];
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            points[k++]= new Point(i,j);
     //       StdDraw.setPenRadius(.01);
            points[k-1].draw();
        }
      //  StdDraw.setPenColor(255,255,0);
        printPointArray(findLines2(points));
	}
	/*
	 * 
	 */
	private static Stack<Point[]> findLines2(Point[] refs){
		Stack<Point[]> degen = new Stack<Point[]>();
		int num;
		Point[] reference= refs.clone();
		Point[] scatter= reference.clone();
		Arrays.sort(reference);
		for(Point p : reference){
			Arrays.sort(scatter,p.SLOPE_ORDER);
			for(int i=0; i<scatter.length-2; i++){
				num=checknext(scatter,i);
				if(num < 3){
					i+=num-1;
					continue;
				}
				Point tempar[] = new Point[num+1];
				tempar[0]=p;
				for(int j=0; j<num; j++) tempar[j+1] = scatter[i+j];
				Arrays.sort(tempar);
				if(tempar[0]==p) degen.push(tempar);
				i += num-1;
			}
		}
		return degen;
	}
	/*
	 * 
	 */

	private static void printPointArray(Stack<Point[]> degen){
		StdDraw.setPenRadius(.00625);
		int j=0;
		for(Point[] p : degen){
				for(int i = 0; i< p.length-1;i++){
					StdOut.print(p[i].toString()+" -> "); 
				}
			p[0].drawTo(p[p.length-1]);
			StdOut.println(p[p.length-1]);
			j++;
		}
		StdOut.print("\nsize"+j);
	}
	/*
	 * 
	 */

	private static int checknext(Point[] array, int index){
		int counter=1;
		if(index==array.length-1|| array[index].compareTo(MaxPoint)==0||array[index+1].compareTo(MaxPoint)==0) return counter;
		if(array[0].slopeTo(array[index])==array[0].slopeTo(array[index+1])) {
			counter+=checknext(array,index+1);
		}
		return counter;
	}
	/*
	 * *
	 */
	private static Point[] shotgun(int size, int sample){
    	StdDraw.setXscale(0,sample);
    	StdDraw.setYscale(0,sample);
    	StdDraw.setPenRadius(.0125);
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
