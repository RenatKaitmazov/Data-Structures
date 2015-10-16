package datastr.heap;

import datastr.Queue;

/**
 * Created by Renat Kaitmazov on 15/08/15.
 */

public final class PriorityQueue<T extends Comparable<T>> implements Queue<T> {
    private Heap<T> storage = new Heap<>();


                        /*** State Information ***/

    public boolean isEmpty()    { return storage.isEmpty(); }
    public int size()           { return storage.size(); }

                        /*** Main Operations ***/

    public void enqueue(T element)  { storage.insert(element); }
    public T dequeue()              { return storage.delete(); }
    public T peek()                 { return storage.max(); }
}
