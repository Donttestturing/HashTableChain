package edu.miracosta.cs113;

import java.util.Hashtable;
import java.util.Map;
/**
 * HashTableChainTesterMain.java: a driver class to test the implementation of HashTableChain class
 *
 * Class Invariant: none
 *
 * @author        Brendan Devlin <Donttestturing@gmail.com>
 * @version       1.0
 **/
public class HashTableChainTesterMain {

    public static void main (String [] args){

        HashTableChain<String, Integer> hashTable = new HashTableChain<String, Integer>();

        Map<String, Integer> other = new Hashtable<String, Integer>();
        /*
        for (int i = 1; i < 2; i ++) {
            other.put(Integer.toString(i), i);
            hashTable.put(Integer.toString(i), i);
        }
        */
        other.put("one", 1);
        hashTable.put("one", 1);
        System.out.println(hashTable.hashCode());
        hashTable.remove("one");

        System.out.println(other.equals(hashTable));
        System.out.println(hashTable.equals(other));



        //hashTable.remove("74");







    }



}
