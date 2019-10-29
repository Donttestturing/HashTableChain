package edu.miracosta.cs113;

import java.util.LinkedList;
import java.util.AbstractSet;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.Hashtable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
/**
 * HashTableChain.java: A hash table data structure with chaining, where when a collusion occurs another node is added to
 * the linked list stored in each table slot, there is only a linked list stored in that table slot if an Entry was first
 * placed in that table slot.
 *
 * Class Invariant: None
 *
 * @author        Brendan Devlin <Donttestturing@gmail.com>
 * @version       1.0
 **/
public class HashTableChain<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    //the table
    private LinkedList<Entry<K, V>>[] table;
    //the number of keys
    private int numKeys;
    //the capacity
    private static final int CAPACITY = 101;
    //the max load factor
    private static final double LOAD_THRESHOLD = 3.0;

//inner class Entry
    /**
     * Inner class Entry, used in Entryset, representing key-value pairs in a hashtable
     * @param <K> key of Entry
     * @param <V> value of Entry
     */
    private static class Entry<K, V> {
        //key
        private K key;
        //value
        private V value;
        @Override
        public int hashCode(){
            return (key.hashCode());
        }
        @Override
        public String toString() {
            return key + "=" + value;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof HashTableChain.Entry)){
                return false;
            }
            HashTableChain.Entry e = (HashTableChain.Entry)obj;
            return (key==null ? e.getKey()==null : key.equals(e.getKey())) &&
                    (value==null ? e.getValue()==null : value.equals(e.getValue()));
        }
        /**
         * Creates a new key value pair
         * @param key
         * @param value
         */
        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
        /**
         * Gets the key
         * @return the key
         */
        public K getKey(){
            return key;
        }
        /**
         * Retrieves the value
         * @return the value
         */
        public V getValue() {
            return value;
        }
        /**
         * Sets the value
         * @param val the new value
         * @return the old value
         */
        public V setValue(V val) {
            V oldValue = value;
            value = val;
           return oldValue;
        }

    }
//inner class EntrySet
    /**
     * Inner class EntrySet, used to represent all entries in a hashtable as a Set
     */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public boolean remove(Object o) {
            return super.remove(o);
        }
        @Override
        public boolean contains(Object obj) {
            for(LinkedList<Entry<K, V>> linkedList : table){
                if (linkedList != null){
                    for (Entry<K, V> entry : linkedList){
                        if (entry.value.equals(obj)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        @Override
        public String toString(){
            StringBuilder builder = new StringBuilder("[");
            boolean second = false;
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && second){
                    builder.append(", ");
                }
                if (table[i] != null) {
                    ListIterator iter = table[i].listIterator();
                    while(iter.hasNext()){
                        builder.append(iter.next());
                    }
                    second = true;
                }
            }
            builder.append("]");
            return builder.toString();
        }
        @Override
        public void clear() {
            super.clear();
            numKeys = 0;
        }
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new SetIterator();
        }
        @Override
        public int size() {
            return numKeys;
        }
    }
//inner class KeySey
    /**
     * Inner class KeySet, used to represent all key values in a hashtable as a set of keys
     */
    private class KeySet extends AbstractSet<K> {
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder("[");
            boolean second = false;
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && second){
                    sb.append(", ");
                }
                if (table[i] != null) {
                    ListIterator iter = table[i].listIterator();
                    while(iter.hasNext()){
                        Entry<K, V> e = (Entry<K, V>) iter.next();
                        sb.append(e.getKey());
                    }
                    second = true;
                }
            }
            sb.append("]");
            return sb.toString();
        }
        @Override
        public Iterator<K> iterator() {
            return new SetIterator();
        }
        @Override
        public int size() {
            return numKeys;
        }
        @Override
        public boolean contains(Object o) {
            if (get(o) != null){
                return true;
            }
            return false;
        }
        @Override
        public boolean remove(Object o) {
            return HashTableChain.this.remove(o) != null;
        }
        @Override
        public void clear() {
            HashTableChain.this.clear();
        }
    }
//inner class SetIterator
    /**
     * Inner class SetIterator, used to iterate over EntrySet, to move around a Set representation of the hash table
     */
    public class SetIterator implements Iterator {
        private Entry<K, V> nextItem;      // the current item
        private Entry<K, V> lastItemReturned;   // the previous item
        private int index = 0;
        ListIterator iter;// = table[index].listIterator();

        public SetIterator(){
            super();
        }
        @Override
        public boolean hasNext() {
            if (iter != null){
                if (iter.hasNext()){
                    return true;
                } else {
                    index++;
                    iter = null;
                }
            }
            while(index < table.length && table[index] == null){
                index++;
            }
            if (index == table.length){
                return false;
            }
            iter = table[index].listIterator();
            return iter.hasNext();
        }
        @Override
        public Object next() {
            lastItemReturned = nextItem;
            if (iter != null){
                if (iter.hasNext()){
                    return iter.next();
                } else {
                    index++;
                    iter = null;
                }
            }
            while(index < table.length && table[index] == null){
                index++;
            }
            if (index == table.length){
                throw new NoSuchElementException();
            }
            iter = table[index].listIterator();
            nextItem = (Entry<K, V>) iter.next();
            return nextItem;
        }
        @Override
        public void remove() {
            entrySet().remove(lastItemReturned);
        }
    }

//constructor

    /**
     * Constructor for HashTableChain, setting size of array to constant CAPACITY
     */
    public HashTableChain(){
        table = new LinkedList[CAPACITY];
    }

//methods
    @Override
    public String toString() {
        return entrySet().toString();
    }
    @Override
    public int hashCode() {
        int hashCode = 1;
        for(LinkedList<Entry<K, V>> linkedList : table){
            if (linkedList != null){
                for (Entry<K, V> entry : linkedList){
                    if (entry != null)
                        hashCode += entry.hashCode();

                }
            }
        }
        return hashCode;
    }
    @Override
    public boolean equals(Object obj) {
       if (obj == null){
           return false;
       }
       Hashtable<K, V> ht = (Hashtable<K, V>) obj;
       HashTableChain<K, V> t = new HashTableChain<>();
       t.putAll(ht);

       return this.hashCode() == t.hashCode();
    }
    @Override
    public V get(Object key){
        int index = key.hashCode() % table.length;
        if (index < 0){
            index += table.length;
        }
        if (table[index] == null){
            return null; //key is not in table
        }
        //search the list at able index to find the key
        for(Entry<K, V> nextItem: table[index]){
            if (nextItem.key.equals(key)){
                return nextItem.value;
            }
        }
        //if reaching here, key is not in table
        return null;
    }
    @Override
    public V put(K key, V value){
        int index = key.hashCode() % table.length;
        if (index < 0){
            index += table.length;
        }
        if (table[index] == null){
            //create a new linked list at table[index]
            table[index] = new LinkedList<Entry<K, V>>();
        }
        //search the list at able index to find the key
        for(Entry<K, V> nextItem: table[index]){
            //if search is successful, replace the old value
            if (nextItem.key.equals(key)){
                //replace value for this key
                V oldVal = nextItem.value;
                nextItem.setValue(value);
                return oldVal;
            }
        }
        //key is not in table, add new item
        table[index].addFirst(new Entry<K, V>(key, value));
        numKeys++;
        if (numKeys > (LOAD_THRESHOLD * table.length)){
            rehash();
        }
        return null;
    }
    /**
     * Rehash method to expand table size when Load factor exceeds LOAD_THRESHOLD
     */
    private void rehash() {
        //save reference to old table
        LinkedList<Entry<K, V>>[] oldTable = table;
        table = new LinkedList[2 * table.length];
        //reset numkeys to 0
        numKeys = 0;
        //fill the new, larger table with the data in the old table
        for (LinkedList<Entry<K, V>> nextItem : oldTable)
            if (nextItem != null)
                for (Entry<K, V> e : nextItem)
                    put(e.key, e.value);
    }
//entrySet method
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }
//keySet method
    @Override
    public Set<K> keySet(){
        return new KeySet();
    }
    @Override
    public boolean remove(Object key, Object value) {
        if (value == null || key == null){
            return false;
        }
        remove(key);
        return true;

    }
    @Override
    public V remove(Object key) {
        V value = null;
        int index = key.hashCode() % table.length;
        if (index < 0){
            index += table.length;
        }
        if (table[index] == null){
            return null;
        }
        for(Entry<K, V> nextItem: table[index]){
            if (nextItem.key.equals(key)){
                value = nextItem.value;
                table[index].remove(nextItem);
                numKeys--;
            }
            if (table[index].size() == 0){
                table[index] = null;
            }
        }

        return value;
    }
    @Override
    public boolean isEmpty() {
        return numKeys == 0;
    }
    @Override
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }
    @Override
    public boolean containsValue(Object value) {
        return entrySet().contains(value);
    }
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        super.forEach(action);
    }
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        super.replaceAll(function);
    }
    @Override
    public V putIfAbsent(K key, V value) {
        return super.putIfAbsent(key, value);
    }
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return super.replace(key, oldValue, newValue);
    }
    @Override
    public V replace(K key, V value) {
        return super.replace(key, value);
    }
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }
    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.computeIfPresent(key, remappingFunction);
    }
    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return super.compute(key, remappingFunction);
    }
    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return super.merge(key, value, remappingFunction);
    }
}
