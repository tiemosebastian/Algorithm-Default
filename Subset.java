
public class Subset {
	public static void main(String[] args){
		int lengthk = StdIn.readInt();
		RandomizedQueue<String> subset = new RandomizedQueue<String>();
		while(!StdIn.isEmpty()){
			subset.enqueue(StdIn.readString());
		}
		for(int i=0; i<lengthk; i++){
			subset.dequeue();
		}
	}
}
