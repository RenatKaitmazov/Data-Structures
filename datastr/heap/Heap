package datastr.heap;

/**
 * Created by Renat Kaitmazov on 13/08/15.
 */


public final class Heap<T extends Comparable<T>> {
    private static final int INITIAL_SIZE = 16;

    private int count = 0;
    private T[] storage;

                        /*** Constructors ***/

    public Heap() {
        storage = (T[]) new Comparable[INITIAL_SIZE];
    }

    public Heap(int size) {
        storage = (T[]) new Comparable[size];
    }

                        /*** Methods common to all objects ***/
    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < count; i++)
            builder.append(storage[i].toString()).append(", ");

        int start   = builder.length() - 2;
        int end     = builder.length();
        builder.replace(start, end, "]");

        return builder.toString();
    }

                        /*** State Information ***/

    public int size()           { return count; }
    public boolean isEmpty()    { return count == 0; }

                        /*** Insertion ***/

    // If the array is full, we create a new array which is twice as big
    private void checkFullness() {
        if (count == storage.length) {
            T[] newArray    = (T[]) new Comparable[storage.length * 2 + 1];
            System.arraycopy(storage, 0, newArray, 0, storage.length);
            storage         = newArray;
        }
    }

    /**
     * Brings the largest element to the beginning of the array so that it will occupy 0th index
     */
    private void heapifyFromBottom(int index) {
        T lastElement   = storage[index];
        int parentIndex = (index - 1) / 2;

        while (index > 0 && lastElement.compareTo(storage[parentIndex]) > 0) {
            storage[index] = storage[parentIndex];
            index = parentIndex;
            parentIndex = (parentIndex - 1) / 2;
        }

        storage[index] = lastElement;
    }

    public void insert(T data) {
        checkFullness();

        storage[count] = data;
        heapifyFromBottom(count++);
    }

                                /*** Deletion ***/

    /**
     * To delete the largest element from the array we first need to replace it with the last element and it becomes a new root.
     * Obviously the new root may not be the second largest element and we should know its proper place in the array
     * This routine finds out what position the new root should occupy
     */
    private void heapifyFromTop(int index) {
        T key = storage[index];

        // While the new root has at least one child
        while (index * 2 + 1 <= count - 1) {
            int maxChildIndex = index * 2 + 1;
            // If it has the second child and the second one is greater than the first one
            if (maxChildIndex < (count - 1) && storage[maxChildIndex].compareTo(storage[maxChildIndex + 1]) < 0) maxChildIndex++;
            // If parent is greater than either child we stop
            if (key.compareTo(storage[maxChildIndex]) > 0) break;
            // Otherwise we replace parent with the larger child
            storage[index] = storage[maxChildIndex];
            index = maxChildIndex;
        }

        storage[index] = key;
    }

    public T delete() {
        if (isEmpty()) return null;
        T temp = storage[0];
        storage[0] = storage[--count];
        storage[count] = null;
        heapifyFromTop(0);
        return temp;
    }

    public void modify(int index, T newValue) {
        if (index < 0 || index >= count || isEmpty()) return;

        T oldValue = storage[index];

        storage[index] = newValue;

        if (newValue.compareTo(oldValue) > 0)
            heapifyFromBottom(index);
        else
            heapifyFromTop(index);
    }

    public T max() { return storage[0]; }
}
