
public class Testing {
	public static Point2D randomPoint(){
		return new Point2D(StdRandom.uniform(),StdRandom.uniform()); 
	}
	public static RectHV randomRect(){
		double a=StdRandom.uniform()/2,b=StdRandom.uniform()/2,c=StdRandom.uniform(0+a,1-a),d=StdRandom.uniform(0+b,1-a);
		return new RectHV(c-a,d-b,c+a,d+b);
		
	}
	public static void main(String[] args){
		int N=1000;
		int M=100000;
		int j=0;
		double rect;
		double point;
		StdOut.print("\nIterations: "+N);
		StdOut.print("\nSize: "+M);
		Stopwatch timer;
		timer=new Stopwatch();
		for(int i=0;i<M;i++){
			Testing.randomRect();
		}
		rect=timer.elapsedTime();
		StdOut.print("\nRect: "+timer.elapsedTime());
		timer=new Stopwatch();
		for(int i=0;i<M;i++){
			Testing.randomPoint();
		}
		point=timer.elapsedTime();
		StdOut.print("\nPoint: "+timer.elapsedTime());
		double data[]=new double[12];
		while(timer.elapsedTime()<30.&& M<400000){
		if(j>8) j=0;
		KdTree fast = new KdTree();
		PointSET slow = new PointSET();
		StdOut.print("\nIterations: "+N);
		StdOut.print("\nSize: "+M);
		for(int i=0;i<M;i++){
			fast.insert(Testing.randomPoint());
		}
		data[j++]=timer.elapsedTime();
		StdOut.print("\nfast insert: "+timer.elapsedTime());
		timer = new Stopwatch();
		for(int i=0;i<M;i++){
			slow.insert(Testing.randomPoint());
		}
		data[j++]=timer.elapsedTime();
		StdOut.print("\nslow insert: "+timer.elapsedTime());
		timer=new Stopwatch();
		for(int i=0;i<N;i++){
			fast.nearest(Testing.randomPoint());
		}
		data[j++]=timer.elapsedTime();
		StdOut.print("\nfast findnear: "+timer.elapsedTime());
		timer=new Stopwatch();
		for(int i=0;i<N;i++){
			slow.nearest(Testing.randomPoint());
		}
		data[j++]=timer.elapsedTime();
		StdOut.print("\nslow findnear: "+timer.elapsedTime());
		timer=new Stopwatch();
		for(int i=0;i<N;i++){
			fast.range(Testing.randomRect());
		}
		data[j++]=timer.elapsedTime();
		StdOut.print("\nfast rangefind: "+timer.elapsedTime());
		timer=new Stopwatch();
		for(int i=0;i<N;i++){
			slow.range(Testing.randomRect());
		}
		data[j++]=timer.elapsedTime();
		StdOut.print("\nslow rangefind: "+timer.elapsedTime());
		M*=2;
		fast=null;
		slow=null;
		}
		int i=0;
			StdOut.println("\n fi: " + ratio(data,6*i)+ " si: " + ratio(data,6*i+1)+" fn: " + ratio(data,6*i+2)+" sn: " + ratio(data,6*i+3)+" fr: " + ratio(data,6*i+4)+" sr: "+ratio(data,6*i+5));
	}
	
	public static double ratio(double[] data, int i){
		return data[i]/data[i+6];
	}
}
