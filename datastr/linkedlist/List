package datastr.linkedlist;

/**
 * Created by Renat Kaitmazov on 30/07/15.
 */

public interface List<T> {
         /*** State Information ***/

    public int size();
    public boolean isEmpty();
    public boolean contains(T key);

        /*** Main Operations ***/

            /* Insertion */
    public void append(T data);

            /* Deletion */
    public T delete(T key);
    public T delete(int index);
    public T deleteFirst();
    public T deleteLast();
    public void deleteAll();

            /* Searching */
    public T find(T key);

            /* Getting elements */
    public T get(int index);
    public T getFirst();
    public T getLast();

    // We assume there are no duplicates
    public int indexOf(T value);
}
