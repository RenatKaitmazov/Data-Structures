package datastr.array;

/**
 * Created by Renat Kaitmazov on 24/07/15.
 */

public interface ArrayInterface<T> {

    /*** State information ***/

    public int size();
    public boolean isFull();
    public boolean isEmpty();
    public int elementsCount();

    /*** Main operations ***/

        /* Insertion */
    public void insert(T element);

        /* Searching */
    public int find(T element);
    public boolean contains(T element);

        /* Deletion */
    public T delete();
    public T deleteLast();
    public T delete(int index);

        /* Getting elements */
    public T get(int index);
    public T getFirst();
    public T getLast();
}
