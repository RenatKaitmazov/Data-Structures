package datastr.set;

import datastr.tree.RedBlackBST;

/**
 * Created by Renat Kaitmazov on 12/09/15.
 */

public final class SortedSet<T extends Comparable<T>> implements Set<T> {

    // We are going to use a balanced binary search tree
    // as an underlying storage system, so we don't need to calculate
    // a hashcode. The only thing we should check is if we already have a particular
    // item in our storage. Everything else will be done for us by that storage

    private RedBlackBST<T> storage = new RedBlackBST<>();

    @Override
    public String toString() { return storage.toString(); }

    public boolean isEmpty() { return storage.isEmpty(); }
    public int size() { return storage.size(); }
    public boolean contains(T element) { return storage.contains(element); }


    public void insert(T element) { if (!storage.contains(element)) storage.insert(element);}
    public T get(T element) { return storage.get(element); }
    public T delete(T element) { return storage.delete(element); }

    public T[] toArray() { return storage.toArray(); }
}
