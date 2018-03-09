import java.util.NoSuchElementException;

public class HashTable<K, V> implements HashTableADT<K, V> {
    /* Instance variables and constructors
     */
    Linkednode[] hashTable; 
    double loadFactor = 0;
    int items = 0; 
    int initialCapacity = 0;

    protected class Linkednode<D> {
        D value;
        Linkednode<D> next;
        public Linkednode(D item) {
            value = item;
            next = null;
        }
       
        public Linkednode<D> getNext() {
            return next;
            }
        
        public void setNext(D item) {
            next = new Linkednode<D>(item);
        }
        
        public D getFirst() {
            return value;
        }
  
    }
    
    public HashTable(int initialCapacity, double loadFactor) {
       hashTable = new Linkednode[initialCapacity];
       this.initialCapacity  = initialCapacity;
       this.loadFactor = loadFactor;
    }
    
    private boolean maintainLoadFactor() {
        double lF = (items) / (hashTable.length);
        return lF > loadFactor;
    }
    
    @Override
    public V put(K key, V value) {
        int hashValue = hashCodeValue(key); // gets the hashcode value that fits into our array's table size    
       
        if(hashTable[hashValue] == null) { //if the array is empty at the hashvalue index
            hashTable[hashValue] = new Linkednode<V>(value); //add a new linkednode containing the value here
        }
        else { //if the array is not null at the hashvalue index
            Linkednode<V> next = hashTable[hashValue].getNext(); //find the next bucket at the position of hash value
            while (next != null) //then search until reach the end of all the buckets
                next = next.getNext();
            next = new Linkednode<V>(value);// now create a new bucket at this location holding the value entered
        }
        items++; //increase number of items in table 
        if(maintainLoadFactor()) { //if the load factor is larger than initial loadFactor set
            hashTable = move(); //call to method to increase table size
        }
        return value; 
    }

    /**
     * move will put the old hash table into a new Linkednode[] that has increased to the next prime number length
     * it will copy the old values of the hash table into this new, larger array
     * @return larger table sized array 
     */
    private Linkednode<V>[] move() {
    	   int nextLength = getNextPrimeNumber(hashTable.length);
    	   
       Linkednode<V>[] temp = new Linkednode[nextLength];
       for(int i = 0; i < hashTable.length; i++) {
           temp[i] = hashTable[i];
       }
        return temp;
        
    }
    
    private int getNextPrimeNumber(int first) {
    		int num = first + 1;
    		double rootOfNum = Math.sqrt(num);
    		int divisor = 2;
    		boolean isPrime = true;
    		
    		while (isPrime) {
    			while (divisor < rootOfNum) {
    				if (num % divisor == 0) {
    					isPrime = false;
    					break;
    				}
    				divisor++;
    			}
    			if (!isPrime)
    				num++;
    			else
    				break;
    		}
    	    return num;
    }
    private int hashCodeValue(K key) {
        return key.hashCode() % hashTable.length;
    }
    @Override
    public void clear() {
        for(int i = 0; i < hashTable.length; i++) {
            hashTable[i] = null;
        }
        //both do the same thing 
     //  hashTable = new Linkednode[initialCapacity];
    }

    @Override
    public V get(K key) {
        int hashValue = hashCodeValue(key);
        if(hashTable[hashValue] == null) {
            throw new NoSuchElementException();
        }
        else {
            return (V) hashTable[hashValue].value;       
        }
    }

    @Override
    public boolean isEmpty() {
        boolean empty = true;
        for(int i = 0; i < hashTable.length; i++) {
            if(hashTable[i] != null) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    @Override
    public V remove(K key) {
        int hashValue = hashCodeValue(key);
         V value = null;
         if(hashTable[hashValue] != null) {
            Linkednode<V> current = hashTable[hashValue];
            while(current.getNext() != null) {
                current = current.getNext();
            }
            value = current.value;
            current = null;
        }
        items--;
        return value;
    }

    @Override
    public int size() {
        return items;
    }
}
