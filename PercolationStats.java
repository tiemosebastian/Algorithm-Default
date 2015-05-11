/*************************************
 * PercolationsStats finds the Average % of open sites normalized by size required to have a system percolate
 * @author tiemo
 *
 */
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
	//Calls Carlo to finds # iterations before percolation T times  
	private int[] Monte(int T, int N){
		int length[]=new int[T];
		for(int i = 0; i < T; i++){
			length[i]=Carlo(N);
		}
		return length;
	}
	//Runs one instance of Percolation to find # of iterations before it percolates 
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
	//Returns average # of iterations normalized to size.
	public double mean(){
		return StdStats.mean(data)/gridsize;
	}
	//Returns standard deviation of # of iterations normalized to size.
    public double stddev(){
        return StdStats.stddev(data)/gridsize;
    }
    //Returns low confidence interval normalized to size 
    public double confidenceLo(){
        double mu = mean();
        double sigma = stddev();
        return mu-(1.96*sigma)/Math.sqrt(iterations);
    }
  //Returns high confidence interval normalized to size
    public double confidenceHi(){
        double mu=mean();
        double sigma=stddev();
        return mu+(1.96*sigma)/Math.sqrt(iterations);
    }
    /*//Prints stats to help debugging
    private void printstats(){
        System.out.print("Iterations: "+data.length+"\n");
    //    System.out.print("Sample Size: "+ size+"\n");
        System.out.print("Average: "+mean() +"\n");
        System.out.print("Standard Deviation: " + stddev() + "\n");
        System.out.print("Confidence High: " + confidenceHi()+"\n");
        System.out.print("Confidence Low: " + confidenceLo()+"\n");
    }*/

}
