package datastr;

/**
 * Created by Renat Kaitmazov on 25/07/15.
 */

public interface Stack<T> {
        /*** State information ***/
    public boolean isEmpty();
    public int size();

        /*** Main operations ***/
    public void push(T element);
    public T pop();
    public T peek();
}
