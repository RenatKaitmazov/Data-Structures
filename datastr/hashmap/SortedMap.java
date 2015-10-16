package datastr.hashmap;


import datastr.tree.RedBlackBST;

/**
 * Created by Renat Kaitmazov on 21/09/15.
 */


public final class SortedMap<K extends Comparable<K>, V> implements Map<K, V> {
    private RedBlackBST.RBTreeDecorator storage = new RedBlackBST.RBTreeDecorator();

    @Override
    public String toString() { return storage.toString(); }

    public boolean isEmpty() { return storage.isEmpty(); }
    public int size() { return storage.size(); }
    public boolean contains(K key) { return storage.contains(key); }

    public void insert(K key, V value) { storage.insert(key, value);}
    public V get(K key) { return (V)storage.get(key); }
    public V delete(K key) { return (V)storage.delete(key); }

    public K[] keySet() { return (K[])storage.keySet(); }
    public V[] values() { return (V[])storage.values();}
}
