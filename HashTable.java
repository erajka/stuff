
public class HashTable<K, V> implements HashTableADT<K, V> {
    /* Instance variables and constructors
     */
    Linkednode[] hashTable; 
    double loadFactor = 0;
    int items = 0; 
    int[] prime = new int[] {13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 
            73, 79, 83, 89, 97, 101, 103, 107, 109, 113};
    int primeIndex = 0;
     int initialCapacity = 0;

    protected class Linkednode<D> {
        D first;
        Linkednode<D> next;
        public Linkednode(D item) {
            first = item;
            next = null;
        }
       
        public Linkednode<D> getNext() {
            return next;
            }
        
        public void setNext(D item) {
            next = new Linkednode<D>(item);
        }
        
        public D getFirst() {
            return first;
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
        Linkednode<V>[] temp = new Linkednode[prime[primeIndex]];
       for(int i = 0; i < hashTable.length; i++) {
           temp[i] = hashTable[i];
       }
        return temp;
        
    }
    private int hashCodeValue(K key) {
        return key.hashCode() % hashTable.length;
    }
    @Override
    public void clear() {
       hashTable = new Linkednode[initialCapacity];
    }

    @Override
    public V get(K key) {
        //TODO: Implement the get method
        return null;
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
       //TODO: Implement the remove method
        return null;
    }

    @Override
    public int size() {
        return items;
    }
}
