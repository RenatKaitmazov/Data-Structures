package datastr.array;

import java.util.Arrays;

/**
 * Created by Renat Kaitmazov on 23/07/15.
 */

public final class Array<T> implements ArrayInterface<T> {

    // Instance variables
    private T[] storage;
    private int count;

                                    /*** CONSTRUCTORS ***/

    public Array(int size) {
        storage     = (T[]) new Object[size];
        count       = 0;
    }

                                    /*** INTERFACE ***/


                                /*** State information ***/

    /**
     * @return the actual size of the array
     */
    public int size() { return storage.length; }

    /**
     * @return how many elements there are in the array.
     */
    public int elementsCount() { return count; }

    public boolean isEmpty() { return count == 0; }

    public boolean isFull() { return count >= storage.length; }

                            /*** Methods common to all objects ***/

    @Override
    public String toString() {
        if (!isEmpty()) {
            StringBuilder builder = new StringBuilder("[");

            for (int i = 0; i < count; i++) {
                String value = storage[i].toString();
                builder.append(value);
                builder.append(", ");
            }

            int start = builder.length() - 2;
            int end = builder.length();
            builder.replace(start, end, "]");

            return builder.toString();
        } else
            return "[]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof Array)) return false;

        Array<T> array = (Array<T>)obj;
        return Arrays.equals(storage, array.getArray());
    }

    @Override
    public int hashCode() {
        int result = 17;
            result = 31 * result + Arrays.hashCode(storage);
            result = 31 * result + count;
        return result;
    }

    // Analog to clone() method

    public Array<T> makeCopy() {
        Array<T> copy   = new Array<>(size());
        copy.storage    = getArray();
        copy.count      = count;
        return copy;
    }

                                    /*** Getters ***/

    /**
     * @return A copy of the array instead of the reference so that the original array can't be modified
     */
    public T[] getArray() {
        T[] copy = (T[]) new Object[size()];
        System.arraycopy(storage, 0, copy, 0, count);
        return copy;
    }


                                    /*** Main operations ***/

                                        /* Insertion */


    private void indexCheck(int index) {
        // The index must not be less than 0 or greater than count
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Index is out of bounds");
    }

    /**
     * Appends a value to the end of the array
     */
    public void append(T element) {
        if (!isFull())
            storage[count++] = element;
        else
            throw new RuntimeException("Array is full");
    }

    /**
     * Inserts an element at specified index
     */
    public void insert(T element, int index) {
        if (index < 0 || index > count)
            throw new IllegalArgumentException("Index is out of bounds");

        // To insert the element at specified index
        // we need to move each object in the array by one cell to the right
        if (!isFull()) {


            // Finding a place to insert the element
            int i = count;
            for (; i > index; i--) {
                storage[i] = storage[i - 1];
            }

            // After we have found the right place to insert, we insert the element and increment count
            storage[i] = element;
            count++;

        } else
            throw new RuntimeException("Array is full");
    }

    /**
     * Inserts an element at the beginning of the array
     */
    public void insert(T element) {
        insert(element, 0);
    }

                                                /* Modification */

    public void modify(T newData, int index) {
        indexCheck(index);
        storage[index] = newData;
    }

                                                /* Searching */
    public int find(T element) {
        // This kind of array is not sorted and unfortunately we can't apply the binary search algorithm

        if (!isEmpty()) {

            int i = 0;
            for (; i < count; i++) {
                if (storage[i].equals(element))
                    return i;
            }

        }

        return -1;
    }

    public boolean contains(T element) { return find(element) != -1; }

                                                /* Deletion */


    private T removeFromMemory() {
        // Just decrementing count is not enough. We should assign null to the item being deleted to let the Garbage Collector know
        // that we don't need the item anymore so that GC could reclaim memory
        T temp          = storage[--count];
        storage[count]  = null;
        return temp;
    }
    
    private void checkStorageEmptiness() {
        if (isEmpty())
            throw new RuntimeException("Array is empty");
    }

    /**
     * Removes element from the beginning
     */
    public T delete() { return delete(0); }

    public T deleteLast() { return delete(count - 1); }


    /**
     * Removes element at specified index
     */
    public T delete(int index) {
        checkStorageEmptiness();

        indexCheck(index);

        T temp = storage[index];

        for (int i = index; i < count - 1; i++)
            storage[i] = storage[i + 1];

        removeFromMemory();

        return temp;
    }

                                            /* Getting elements */

    public T get(int index) {
        if (!isEmpty()) {
            indexCheck(index);
            return storage[index];
        } else
            return null;
    }

    public T getLast() {
        return get(count - 1);
    }

    public T getFirst() {
        return get(0);
    }
}
