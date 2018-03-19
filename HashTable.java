import java.util.NoSuchElementException;

public class HashTable<K, V> implements HashTableADT<K, V> {
    Linkednode[] hashTable; //our hash table will be an array filled with linked lists at each index
    double loadFactor = 0; //the number that will tell us when we need to resize our array
    int items = 0; //number of items actually in the hashTable
    int initialCapacity = 0; //initial number of positions in the hashTable

    /**
     * Inner protected class that has our implementation
     * of linkednodes. this class creates  linkednodes
     * that point to each other 
     *
     * @param <D> the generic key 
     * @param <S> the generic value
     */
    protected class Linkednode<D, S> {
        D key;
        S value;
        Linkednode<D, S> next;
        public Linkednode(D key, S value) {
            this.value = value;
            this.key = key;
            next = null;
        }
    }
    
    /**
     * Constructor to that will set our hashtable's array to size
     * initialCapacity. It will also set our local variables
     * for loadFactor and initialCapacity to the values entered
     * by the user
     * @param initialCapacity
     * @param loadFactor
     */
    public HashTable(int initialCapacity, double loadFactor) {
       hashTable = new Linkednode[initialCapacity];
       this.initialCapacity  = initialCapacity;
       this.loadFactor = loadFactor;
    }
    
    /**
     * this method will divide the number of items in the 
     * array by the length of the array to get loadfactor
     * this method will tell us whether our loadfactor set
     * initially is still maintained. Then we know when to 
     * resize our array
     * @return true if current load factor is greater than
     * initialized loadfactor, else return false
     */
    private boolean maintainLoadFactor() {
        double lF = (items) / (hashTable.length);
        return lF > loadFactor;
    }
    
    /**
    *
    * @param key : The key that goes into the hashtable
    * @param value: The Value associated with the key
    * @return value of the key added to the hashtable,
    *      throws NullPointerException if key is null
    */
    @Override
    public V put(K key, V value) {
        if(key == null) throw new NullPointerException();
        int hashValue = hashCodeValue(key); // gets the hashcode value that fits into our array's table size    
       
        if(hashTable[hashValue] == null) { //if the array is empty at the hashvalue index
            hashTable[hashValue] = new Linkednode<K, V>(key, value); //add a new linkednode containing the value here
        }
        else { //if the array is not null at the hashvalue index
            Linkednode<K, V> current = hashTable[hashValue].next; //find the next bucket at the position of hash value
            while (current != null) { //then search until reach the end of all the buckets
                if (key.equals(current.key)) { //if the key is already in the linked list
                    current.value = value; //change the value at this key to the new value 
                    break;
                }
                current = current.next; //otherwise keep incrementing until reach the end of the linkednodes
            }
            current = new Linkednode<K, V>(key, value);// now create a new bucket at this location holding the value entered
        }
        items++; //increase number of items in table 
        if(maintainLoadFactor()) { //if the load factor is larger than initial loadFactor set
            hashTable = move(); //call to method to increase table size
        }
        return value; //return the value added to the hash table
    }

    /**
     * move will put the old hash table into a new Linkednode[] that has increased to the next prime number length
     * it will copy the old values of the hash table into this new, larger array
     * @return larger table sized array 
     */
    private Linkednode<K, V>[] move() {
       int resized = getNextPrimeNumber(hashTable.length); //obtains the value of the size of new array
           
       Linkednode<K, V>[] resizedHashTable = new Linkednode[resized]; //make a temp array that is the size we just obtained
       for(int i = 0; i < hashTable.length; i++) {
           resizedHashTable[i] = hashTable[i]; //put the values of hashTable into temp array
       }
        return resizedHashTable; //return the larger new array
        
    }
    
    /**
     * a helper method that will resize the hash table to double the size
     * then rounded to the nearest prime number 
     * @param first
     * @return the new size of the array 
     */
    private int getNextPrimeNumber(int first) {
        int num = 2*first + 1;
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
    
    /**
     * Our hashcode method that will use Java's built in
     * hashCode method, then mod by table size and take 
     * the absolute value so we always have positive
     * indexes
     * @param key
     * @return an index within the table that the key can
     * be put into
     */
    private int hashCodeValue(K key) {
        return Math.abs(key.hashCode()) % hashTable.length;
    }
    
    /**
     * method to set all the indexes in the array to null
     */
    @Override
    public void clear() {
        for(int i = 0; i < hashTable.length; i++) {
            hashTable[i] = null;
        }
    }

    /**
     * @param key: The key for which the value is returned
     * @return The value associated with the key,
     *          else throws NoSuch Element Exception
     */
    @Override
    public V get(K key) {
        int hashValue = hashCodeValue(key);
        if(hashTable[hashValue] == null) {
            throw new NoSuchElementException();
        }
        else {
            Linkednode<K, V> current = hashTable[hashValue]; //will keep track of the node we are searching at
            K hashKey = (K) current.key; //the key at the current node
            V val = (V) current.value;  //the value at the current node
    
            while(!hashKey.equals(key)) { //loop that will search to find when the key of the current 
                                          //node is equal to the key we are searching for
                 if (current.next == null) { //if the current node's key != the key were looking for and
                     throw new NoSuchElementException(); //the current node's next pointer is null throw
                     //a nosuch element exception
                 }
                 //while we haven't found the correct key and it has a next node
                 current = current.next; //increment to the next node
                 hashKey = (K) current.key; //and reset the key to the key at the new current node
                 val = (V) current.value; //reset the value to be the value of the current nodes
            }
            return val; //return the value that appears at the node with the key we were searching for
        }
    }

    /**
     * method will search the array to see if it is empty
     * at every index. if it finds an index that is not
     * empty it will return false and break the for loop
     * Checks if the hashtable is empty
     * @return true : if Empty, else False
     */
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

    /**
    *
    * @param key: Key of the entry to be removed
    * @return value: Value of the key-value pair removed,
    *          null if key did not have a mapping
    * @throws NullPointerException if key is null
    */
    @Override
    public V remove(K key) {
        if(key == null) throw new NullPointerException();
        int hashValue = hashCodeValue(key); //the calculated hash index based on the key
        if(hashTable[hashValue] == null) { //if there are no items at this index
            throw new NoSuchElementException(); // throw a no such element exception
        }
        else {
            Linkednode<K, V> past = null; //value to keep track of the previous node
            Linkednode<K, V> current = hashTable[hashValue];//this initializes current to be whatever is the first linkednode at the hash index
            K hashKey = (K) current.key; //the key at the node we are currently searching at
            V val = (V) current.value; //the value at the node we are currently searching at
            
            while(!hashKey.equals(key)) { //will search through the array looking for 
                                          //a node that has the same key as the inputted key
                 if (current.next == null) { //this is a check to make sure that we have
                          throw new NoSuchElementException(); //not reached the end of our linked nodes, if we have and still have not found a matching key
                          //throw new no such elemenet exception
                 }
             
                 past = current; //we will set past the node we just searched at to see if there was a match in the key values
                 current = current.next; //now we will increment the current value to search at the next node
                 hashKey = (K) current.key; //now we will reset the key at our new current node
                 val = (V) current.value; //we will also reset the value at our new current node
            }
            
            if (past == null) { //if a value is not set for node named past
                    current = current.next; //then 
            }
            else { //otherwise set past's next value to be current's next value
                past.next = current.next; //this will remove the node that had the matching key and set the
                //previous node to point to the correct next node
            }
            items--; //decrease the number of items in the array
            return val; // return the value of the node that is removed
        }
    }

    /**
    *
    * @return: The total number of entries in the hashtable
    */
    @Override
    public int size() {
        return items; 
    }

}
