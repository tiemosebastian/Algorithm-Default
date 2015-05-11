import java.util.*;
public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] queue;
	private int last;
	
	public RandomizedQueue(){ 
		queue = (Item[]) new Object[2];
		last = 0;
	}
	public boolean isEmpty(){
		return last==0;
	}
	public int size(){
		return last;
	}
	public void enqueue(Item item){ 
		if(item==null) throw new java.lang.NullPointerException("Can't add a null item.");
		if(last+1==queue.length) resize(2*queue.length);
		queue[last]=item;
		//StdOut.print(item);
		last++;
	}
	private void printqueue(){
		for(int i=0; i<queue.length;i++){
			StdOut.print(queue[i]);
		}
	}
	public Item dequeue(){
		if(isEmpty()) throw new java.util.NoSuchElementException("RandomizedQueue is empty.");
		if(queue.length/4>last) resize(queue.length/2);
		int random = StdRandom.uniform(last);
		Item item = queue[random];
		queue[random]=queue[last-1];
		queue[last-1]=null;
		last--;
		return item;
	}
	public Item sample(){
		if(isEmpty()) throw new java.util.NoSuchElementException("RandomizedQueue is empty.");
		int random = StdRandom.uniform(last);
		return queue[random];
	}
	public Iterator<Item> iterator(){
		return new RandomizedQueueIterator();
	}
	private class RandomizedQueueIterator implements Iterator<Item>{
		private Item[] copy;
		private int clast;
		private int current;
	
		public RandomizedQueueIterator(){
			copy = (Item[]) new Object[queue.length];
			copy=queue.clone();
			clast=last;
			shuffle();
		}
		private void shuffle(){
			int random;
			Item placeholder;
			for(int i=0; i < clast; i++){
				random=StdRandom.uniform(i,last);
				placeholder=copy[i];
				copy[i]=copy[random];
				copy[random]=placeholder;
			}
		}
		public boolean hasNext(){
			return current < clast;
		}
		public Item next(){
			Item temp;
			if(!hasNext()) throw new java.lang.IndexOutOfBoundsException();
			temp = copy[current];
			current++;
			return temp;
		}
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
	private void resize(int capacity){
		//if(capacity<last) return;
		Item[] copy;
		copy= (Item[]) new Object[capacity];
		int length = queue.length;
		if(queue.length > capacity) length = capacity;	
		for(int i = 0; i < length; i++){
			copy[i] = queue[i];
			}
		queue = copy;
	}
	public static void main(String[] args){
		RandomizedQueue<String> test = new RandomizedQueue<String>();
		String currentinput;
		test.enqueue("2");
		test.enqueue("1");
		test.enqueue("3");
		test.enqueue("4");
		test.enqueue("5");
		test.enqueue("6");
		test.enqueue("7");
		test.enqueue("8");
		test.enqueue("9");
		test.enqueue("10");
		test.enqueue("11");
		test.enqueue("12");
		test.enqueue("13");
		test.enqueue("14");
		test.enqueue("15");
		test.enqueue("16");
		while(true){
			currentinput=StdIn.readString();
			//StdOut.println(currentinput);
			if(currentinput.startsWith("push")){
				test.enqueue(currentinput);
			}
			else if(currentinput.startsWith("pop")){
				StdOut.println(test.dequeue());
				StdOut.println("\n");
				test.printqueue();
			}
			else if(currentinput.startsWith("iterate")){
				Iterator<String> iter = test.iterator();
				Iterator<String> iter2 = test.iterator();
				while(iter.hasNext()){
					StdOut.print(iter.next()+",");
					StdOut.print(iter2.next()+"\n");
				}
				StdOut.println("\n");
				//for(String S : test) StdOut.println(S);
			}
			else if(currentinput.startsWith("size")){
				StdOut.println(test.size());
			}
			else if(currentinput.startsWith("sample")){
				StdOut.println(test.sample());
			}
			else StdOut.println("Invalid input.");
			
		}
	}	
}
