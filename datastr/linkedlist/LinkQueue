package datastr.linkedlist;

import datastr.Queue;

/**
 * Created by Renat Kaitmazov on 04/08/15.
 */

public final class LinkQueue<T> implements Queue<T> {

    private LinkedList<T> storage = new LinkedList<>();

                /*** Methods common to all objects ***/

    @Override
    public String toString() {
        return storage.toString();
    }

    // An analog to clone() method
    public LinkQueue<T> makeCopy() {
        LinkQueue<T> copy   = new LinkQueue<>();
        copy.storage        = storage.makeCopy();
        return copy;
    }

                /*** State information ***/
    public boolean isEmpty()    { return storage.isEmpty(); }
    public int size()           { return storage.size(); }

                /*** Main Operations ***/
    public void enqueue(T data) { storage.append(data); }
    public T dequeue()          { return storage.deleteFirst(); }
    public T peek()             { return storage.getFirst(); }
}
