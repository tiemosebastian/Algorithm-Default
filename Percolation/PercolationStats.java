
public class PercolationStats{
	private int data[];
	private int iterations;
	private int gridsize;
	private int size;
	
	public PercolationStats(int N, int T){
		if(N <= 0) throw new IllegalArgumentException("Size of percolation grid, N, must be greater than 0");
		if(T <= 0) throw new IllegalArgumentException("Number of Trials, T, must be greater than 0");
		iterations = T;
		gridsize = N*N;
		size = N;
		
		data = Monte(T,N);
	}
	private int[] Monte(int T, int N){
		int length[]=new int[T];
		for(int i = 0; i < T; i++){
			length[i]=Carlo(N);
		}
		return length;
	}
	private int Carlo(int N){
		int i,j,counter=0;
		Percolation MonteCarlo=new Percolation(size);
		while(!MonteCarlo.percolates()){
			i=StdRandom.uniform(1,size+1);
			j=StdRandom.uniform(1,size+1);
			if(!MonteCarlo.isOpen(i,j)){
				MonteCarlo.open(i,j);
				counter++;
			}
		}
		return counter;
	}
	public static void main(String[] args) {
		
		int N = StdIn.readInt();
		int T = StdIn.readInt();
		if(N <= 0) throw new IllegalArgumentException("Size of percolation grid, N, must be greater than 0");
		if(T <= 0) throw new IllegalArgumentException("Number of Trials, T, must be greater than 0");
		
        PercolationStats ps = new PercolationStats(N, T);
        
		StdOut.println("mean                    = "+ps.mean());
		StdOut.println("stddev                  = "+ps.stddev());
		StdOut.println("95% confidence interval = "+ps.confidenceLo()+", "+ps.confidenceHi());
	}
	public double mean(){
		return StdStats.mean(data)/gridsize;
	}
    public double stddev(){
        return StdStats.stddev(data)/gridsize;
    }
    public double confidenceLo(){
        double mu = mean();
        double sigma = stddev();
        return mu-(1.96*sigma)/Math.sqrt(iterations);
    }
    public double confidenceHi(){
        double mu=mean();
        double sigma=stddev();
        return mu+(1.96*sigma)/Math.sqrt(iterations);
    }
    public void printstats(){
        System.out.print("Iterations: "+data.length+"\n");
    //    System.out.print("Sample Size: "+ size+"\n");
        System.out.print("Average: "+mean() +"\n");
        System.out.print("Standard Deviation: " + stddev() + "\n");
        System.out.print("Confidence High: " + confidenceHi()+"\n");
        System.out.print("Confidence Low: " + confidenceLo()+"\n");
    }

}
