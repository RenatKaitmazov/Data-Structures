package datastr;

/**
 * Created by Renat Kaitmazov on 25/07/15.
 */
public interface Queue<T> {
        /*** State information ***/
    public boolean isEmpty();
    public int size();

        /*** Main operations ***/
    public void enqueue(T element);
    public T dequeue();
    public T peek();
}
