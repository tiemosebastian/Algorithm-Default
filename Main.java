import java.io.*;
public class Main {
	
	public static void main(String args[]){
		//PercolationStats test1 = new PercolationStats(1000,100);
		//test1.printstats();
		/*Percolation test = new Percolation(1);
		System.out.println(test.isOpen(1,1));
		System.out.println(test.isFull(1, 1));
		System.out.println(test.percolates());
		test.isFull(1, 1);
		test.open(1,1);
		System.out.println(test.isOpen(1,1));
		System.out.println(test.isFull(1, 1));
		System.out.println(test.percolates());*/
		//PercolationStats test = new PercolationStats(100,100);
		//test.printstats();
		String t[]=new String[1];
		t[0]="1";
		InteractivePercolationVisualizer.main(t);
		//String t[] = new String[1];
		//t[0]="/Users/tiemo/Desktop/percolation/input2.txt";
		//PercolationVisualizer.main(t);
		/*Percolation test = new Percolation(2);
		test.open(1,1);
		test.open(2, 2);
		System.out.println(test.percolates());
		test.open(1,2);
		System.out.println(test.percolates());
		System.out.println(test.isOpen(2,2));
		/*String t[] = new String[1];
		String dir = "/Users/tiemo/Desktop/percolation";
		
		File directory = new File(dir);
		File[] matches = directory.listFiles( new FilenameFilter()
				{
			public boolean accept(File directory, String name)
			{ 
				return name.endsWith(".txt");
			}
		});
		System.out.println(matches[2]);
		for(int i=0;i < matches.length;i++){
			t[0]=  matches[i].toString();
			PercolationVisualizer.main(t);
		}*/
	}
}
