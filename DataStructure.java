/*import java.util.*;

public class DataStructure {
     
    // Create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];
     
    public DataStructure() {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++) {
            arrayOfInts[i] = i;
        }
    }
    public void print(DataStructureIterator iterator){
    	while(iterator.hasNext()){
    		System.out.print(iterator.next()+" ");
    	}
    	System.out.println();
    }
    public void printEven() { 
        // Print out values of even indices of the array
        DataStructureIterator iterator = this.new EvenIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
    /*public void print(java.util.function.Function<Integer, Boolean> function) {
        for (int i = 0; i < SIZE; i++) {
            if (function.apply(i)) {
                System.out.print(arrayOfInts[i] + " ");
            }
        }
        System.out.println();
    }*//*
    public DataStructureIterator use(){
    	return new EvenIterator();
    }
    public DataStructureIterator use2(){
    	return new DataStructureIterator(){ 
        	private int nextIndex = 1;
            
            public boolean hasNext() {
                 
                // Check if the current element is the last in the array
                return (nextIndex <= SIZE - 1);
            }       
            public void remove(){}
            public Integer next() {
                 
                // Record a value of an even index of the array
                Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);
                 
                // Get the next even element
                nextIndex += 2;
                return retValue;
            }
        	
        };
    }
    interface DataStructureIterator extends java.util.Iterator<Integer> { }
 
    // Inner class implements the DataStructureIterator interface,
    // which extends the Iterator<Integer> interface
   // public void print(java.util.Function<Integer, Boolean> iteratrot){
    	
   // }

    public static Boolean isEvenIndex(Integer index) {
        if (index % 2 == 0) return Boolean.TRUE;
        return Boolean.FALSE;
    }
    
    public static Boolean isOddIndex(Integer index) {
        if (index % 2 == 0) return Boolean.FALSE;
        return Boolean.TRUE;
    }
    private class EvenIterator implements DataStructureIterator {
         
        // Start stepping through the array from the beginning
        private int nextIndex = 0;
         
        public boolean hasNext() {
             
            // Check if the current element is the last in the array
            return (nextIndex <= SIZE - 1);
        }       
        public void remove(){}
        public Integer next() {
             
            // Record a value of an even index of the array
            Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);
             
            // Get the next even element
            nextIndex += 2;
            return retValue;
        }
    }
    	
    
    public static void main(String s[]) {
         
        // Fill the array with integer values and print out only
        // values of even indices
        DataStructure ds = new DataStructure();
        ds.printEven();
        ds.print(ds.use());
        ds.print(ds.use2());
        ds.print(DataStructure::isEvenIndex);
        ds.print(DataSructure::isOddIndex);
  /*      ds.print(new DataStructureIterator(){ 
        	private int nextIndex = 1;
            
            public boolean hasNext() {
                 
                // Check if the current element is the last in the array
                return (nextIndex <= SIZE - 1);
            }       
            public void remove(){}
            public Integer next() {
                 
                // Record a value of an even index of the array
                Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);
                 
                // Get the next even element
                nextIndex += 2;
                return retValue;
            }
        	
        });*//*
    }
}
*/