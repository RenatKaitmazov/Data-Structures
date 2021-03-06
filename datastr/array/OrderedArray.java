package datastr.array;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Renat Kaitmazov on 23/07/15.
 */

final public class OrderedArray<T extends Comparable<T>> implements ArrayInterface<T> {

    // Instance variables
    private T[] storage;
    private int count;
    private final Class<T> clazz;

                                    /*** CONSTRUCTORS ***/

    public OrderedArray(Class<T> clazz, int size) {
        storage     = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        count       = 0;
        this.clazz  = clazz;
    }

                                    /*** INTERFACE ***/

                                    /* State information */

    /**
     * @return the actual size of the array
     */
    public int size() {
        return storage.length;
    }

    /**
     * @return how many elements there are in the array.
     */
    public int elementsCount() {
        return count;
    }

    public boolean isEmpty() { return count == 0; }

    public boolean isFull() { return count >= storage.length; }

                                    /* Methods common to all objects */
    @Override
    public String toString() {
        if (!isEmpty()) {
            StringBuilder builder = new StringBuilder("[");

            for (int i = 0; i < count; i++) {
                String value = storage[i].toString();
                builder.append(value);
                builder.append(", ");
            }

            int start   = builder.length() - 2;
            int end     = builder.length();
            builder.replace(start, end, "]");

            return builder.toString();
        } else
            return "[]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof OrderedArray)) return false;

        OrderedArray<T> ordArray = (OrderedArray<T>)obj;

        return Arrays.equals(storage, ordArray.getArray());
    }

    @Override
    public int hashCode() {
        int result = 17;
            result = 31 * result + Arrays.hashCode(storage);
            result = 31 * result + count;
        return result;
    }

    // Analog to clone() method

    public OrderedArray<T> makeCopy() {
        OrderedArray<T> copy    = new OrderedArray<>(clazz, size());
        copy.storage            = storage;
        copy.count              = count;

        return copy;
    }
                                    /*** Getters ***/

    /**
     * @return A copy of the array instead of the reference so that the original array can't be modified
     */
    public T[] getArray() {
        T[] copy    = (T[]) Array.newInstance(clazz, size());
        System.arraycopy(storage, 0, copy, 0, count);
        return copy;
    }

                                    /*** Main operations ***/

                                        /* Insertion */

    public void insert(T element) {
        if (isFull())
            throw new RuntimeException("Array is full");

        if (isEmpty())
            storage[count] = element;
        else {
            int i = count - 1;

            while (i >= 0 && storage[i].compareTo(element) > 0) {
                storage[i + 1] = storage[i];
                i--;
            }

            storage[i + 1] = element;
        }

        count++;
    }

                                        /* Searching */

    public int find(T element) {
        // Since the array is sorted we are going to make use of the binary search algorithm
        int lowerBound = 0;
        int upperBound = count - 1;

        while (upperBound >= lowerBound) {
            // For large numbers it prevents from overflow
            int middle = lowerBound + (upperBound - lowerBound) / 2;

            if (storage[middle] == element)
                return middle;
            else if (storage[middle].compareTo(element) > 0)
                upperBound = middle - 1;
            else
                lowerBound = middle + 1;
        }

        return -1;
    }

    public boolean contains(T element) {
        return find(element) != -1;
    }

                                        /* Deletion */

    private void removeFromMemory() {
        // Just decrementing count is not enough. We should assign null to the item being deleted to let the Garbage Collector know
        // that we don't need the item anymore so that GC could reclaim memory
//        T temp          = storage[--count];
//        storage[count]  = null;
//        return temp;
        storage[--count] = null;
    }

    private void checkStorageEmptiness() {
        if (isEmpty())
            throw new RuntimeException("Array is empty");
    }

    private void indexCheck(int index) {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Index is out of bounds");
    }

    public T delete(int index) {
        checkStorageEmptiness();

        indexCheck(index);

        T temp = storage[index];

        for (int i = index; i < count - 1; i++)
            storage[i] = storage[i + 1];

        removeFromMemory();

        return temp;
    }

    public T delete() {
        return delete(0);
    }

    public T deleteLast() {
        return delete(count - 1);
    }

                                /* Getting elements */

    public T get(int index) {
        checkStorageEmptiness();

        return storage[index];
    }

    public T getFirst() {
        return get(0);
    }

    public T getLast() {
        return get(count - 1);
    }
}
