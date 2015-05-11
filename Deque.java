import java.util.*;

public class Deque<Item> implements Iterable<Item> {
	private Node first;
	private Node last;
	private int size=0;
	
	private class Node{
		Item item;
		Node next;
		Node previous;
	}
	public boolean isEmpty(){
		return size==0;
	}
	public int size(){
		return size;
	}
	public void addFirst(Item item){
		if(item==null) throw new NullPointerException("Cannot add null item");
		if(first==null) {
			first=last=new Node();
			first.next=null;
			first.previous=null;
			first.item = item;
			size++;
			return;
		}
		Node oldfirst = first;
		first = new Node();
		first.item=item;
		first.previous=null;
		first.next = oldfirst;
		first.next.previous = first;
		size++;
	}
	public void addLast(Item item){
		if(item==null) throw new NullPointerException("Cannot add null item.");
		if(last==null){
			last=first=new Node();
			last.next=null;
			last.previous=null;
			last.item=item;
			size++;
			return;
		}
		Node oldlast = last;
		last = new Node();
		last.item=item;
		last.previous=oldlast;
		last.previous.next=last;
		last.next=null;
		size++;
	}
	public Item removeFirst(){
		if(this.isEmpty()) throw new NoSuchElementException("Deque is Empty.");
		Item item = first.item;
		if(first.next==null){
			first=null;
			last=null;
			size--;
			return item;
		}
		first=first.next;
		first.previous=null;
		size--;
		return item;
	}
	public Item removeLast(){
		if(this.isEmpty()) throw new NoSuchElementException("Deque is Empty.");
		Item item = last.item;
		if(last.previous==null){
			last=null;
			first=null;
			size--;
			return item;
		}
		last=last.previous;
		last.next=null;
		size--;
		return item;
	}
	private class DequeIterator implements Iterator<Item>{
		private Node current = first;
		
		public boolean hasNext(){
			return current!=null;
		}
		public void remove(){throw new UnsupportedOperationException("Cannot remove items from iterator.");}
		public Item next(){
			if(current==null) throw new NoSuchElementException("Reached last item.");
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
	public Iterator<Item> iterator(){
		return new DequeIterator();
	}
	public static void main(String[] args){
		Deque<String> test = new Deque<String>();
		String currentinput;
		test.addFirst("2");
		test.addFirst("1");
		test.addLast("3");
		test.addLast("4");
		while(true){
			currentinput=StdIn.readString();
			//StdOut.println(currentinput);
			if(currentinput.startsWith("push")){
				if(currentinput.endsWith("first")){
					test.addFirst(currentinput);
				}
				else{
					test.addLast(currentinput);
				}
				
			}
			else if(currentinput.startsWith("pop")){
				if(currentinput.endsWith("first")){
					StdOut.println(test.removeFirst());
				}
				else{
					StdOut.println(test.removeLast());
				}
			}
			else if(currentinput.startsWith("iterate")){
				Iterator<String> iter = test.iterator();
				while(iter.hasNext()){
					StdOut.println(iter.next());
				}
				StdOut.println(iter.next());
				StdOut.println("\n");
				for(String S : test) StdOut.println(S);
			}
			else if(currentinput.startsWith("size")){
				StdOut.println(test.size());
			}
			else if(currentinput.startsWith("empty")){
				StdOut.println(test.isEmpty());
			}
			else StdOut.println("Invalid input.");
			
		}
	}
}