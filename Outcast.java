public class Outcast {
	private WordNet wordnet;
	/**
	 * Creates an instance of Outcast with given wordnet
	 * @param wordnet
	 */
	public Outcast(WordNet wordnet){
		this.wordnet=wordnet;
	}
	/**
	 * Finds the least sum of distances of the listed nouns to determine the outcast 
	 * @param nouns
	 * @return
	 */
	public String outcast(String[] nouns){
		int index=0;
		int summax=0;
		int sum=0;
		int n=0;
		for(String i:nouns){
			for(String j:nouns){
				sum+=wordnet.distance(i, j);
			}
			if(summax<sum) {
				//StdOut.print("currentmax: "+nouns[n]+"\n");
				summax=sum;
				index=n;
			}
			sum=0;
			n++;
		}		
		return nouns[index];
	}
	//////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		WordNet wordnet=new WordNet("/Users/tiemo/Downloads/wordnet/synsets.txt",
				   "/Users/tiemo/Downloads/wordnet/hypernyms.txt");
		String arg[]= new String[]{"","","/Users/tiemo/Downloads/wordnet/outcast5.txt",
				"/Users/tiemo/Downloads/wordnet/outcast8.txt",
				"/Users/tiemo/Downloads/wordnet/outcast11.txt"};
		StdOut.print(arg.length);
	    //WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < arg.length; t++) {
	        In in = new In(arg[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(arg[t] + ": " + outcast.outcast(nouns));
	    }
	}
}
