package edu.miracosta.cs113;

import java.util.Hashtable;
import java.util.Map;

public class HashTableChainTesterMain {

    public static void main (String [] args){

        HashTableChain<String, Integer> hashTable = new HashTableChain<String, Integer>();
        HashTableChain<String, Integer> hashTable2 = new HashTableChain<String, Integer>();


        Map<String, Integer> other = new Hashtable<String, Integer>();

        for (int i = 1; i < 2; i ++) {
            other.put(Integer.toString(i), i);
            hashTable.put(Integer.toString(i), i);
        }

        System.out.println(hashTable.hashCode());
        //hashTable.remove("4");
        System.out.println(other.hashCode());
        System.out.println(hashTable.equals(other));
        System.out.println("one".hashCode());


        //hashTable.remove("74");
    /*
        private void populateMapWithPut(int numElements) {
        for (int i = 0; i < numElements; i++) {
            hashTable.put(Integer.toString(i), i);
        }
    }
     */






    }



}
