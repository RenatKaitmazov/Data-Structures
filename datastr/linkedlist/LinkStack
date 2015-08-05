package datastr.linkedlist;

import datastr.Stack;

/**
 * Created by Renat Kaitmazov on 02/08/15.
 */

public final class LinkStack<T> implements Stack<T> {

    private LinkedList<T> storage = new LinkedList<>();

                /*** Methods common to all objects ***/
    @Override
    public String toString() {
        return storage.toString();
    }

    // An analog to clone() method
    public LinkStack<T> makeCopy() {
        LinkStack<T> copy   = new LinkStack<>();
        copy.storage        = storage.makeCopy();
        return copy;
    }

                /*** State Information ***/
    public boolean isEmpty()    { return storage.isEmpty(); }
    public int size()           { return storage.size(); }

                /*** Main Operations ***/
    public void push(T data)    { storage.append(data); }
    public T pop()              { return storage.deleteLast(); }
    public T peek()             { return storage.getLast(); }
}
