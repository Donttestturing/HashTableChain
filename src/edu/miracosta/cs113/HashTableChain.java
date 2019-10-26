package edu.miracosta.cs113;


import java.util.LinkedList;
import java.util.AbstractSet;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
/**
 * HashTableChain.java:
 *
 * Class Invariant:
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
    private static final double LOAD_THRESHOLD = 3.0;                               //need to implement/override equals and

//inner class Entry
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
                    builder.append(table[i].listIterator(0).next());
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
                sb.append(table[i].listIterator(0).next().getKey());
                second = true;
            }
        }
        sb.append("]");
        return sb.toString();

        }
        public Iterator<K> iterator() {
            return new SetIterator();
        }
        public int size() {
            return numKeys;
        }
        public boolean contains(Object o) {

            if (get(o) != null){
                return true;
            }
            return false;


        }
        public boolean remove(Object o) {
            return HashTableChain.this.remove(o) != null;
        }
        public void clear() {
            HashTableChain.this.clear();
        }
    }
//inner class SetIterator
    public class SetIterator implements Iterator {

        private Entry<K, V> nextItem;      // the current node
        private Entry<K, V> lastItemReturned;   // the previous node
        private int index;

        public SetIterator(){
            super();
        }
        @Override
        public boolean hasNext() {
            return nextItem != null;
        }
        @Override
        public Object next() {
            lastItemReturned = nextItem;
            nextItem = (Entry<K, V>) entrySet().iterator().next();
            index++;
            return nextItem;
        }
        @Override
        public void remove() {
            entrySet().remove(lastItemReturned);
        }
    }

//constructor
    public HashTableChain(){
        table = new LinkedList[CAPACITY];
    }


//methods
    @Override
    public String toString() {
        return entrySet().toString();
    }

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

    private void rehash() {
        LinkedList<Entry<K, V>>[] oldTable = table;
        table = new LinkedList[2 * table.length];
        numKeys = 0;
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
