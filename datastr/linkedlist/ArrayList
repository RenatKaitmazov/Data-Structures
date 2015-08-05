package datastr.linkedlist;

import java.util.Arrays;

/**
 * Created by Renat Kaitmazov on 01/08/15.
 */

public final class ArrayList<T> implements List<T> {
    private final static int INITIAL_SIZE = 16;

    private T[] storage;
    private int count = 0;

                            /*** Constructors ***/

    public ArrayList()          { storage = (T[]) new Object[INITIAL_SIZE]; }
    public ArrayList(int size)  { storage = (T[]) new Object[size]; }

                            /*** Methods common to all objects ***/

    @Override
    public String toString() {
        if (isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < size(); i++) {
            builder.append(storage[i].toString());
            builder.append(", ");
        }

        int start   = builder.length() - 2;
        int end     = builder.length();
        builder.replace(start, end, "]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        int result = 17;
            result = 31 * result + count;
            result = 31 * result + Arrays.hashCode(storage);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)                                    return true;
        if (obj == null || !(obj instanceof ArrayList))     return false;

        ArrayList<T> list = (ArrayList<T>)obj;

        if (count != list.count) return false;

        return Arrays.equals(storage, list.storage);
    }

    // An analog to clone() method
    public ArrayList<T> makeCopy() {
        ArrayList<T> copy   = new ArrayList<>(storage.length);
        System.arraycopy(storage, 0, copy.storage, 0, size());
        copy.count          = count;
        return copy;
    }

                                /*** State Information ***/

    public int size()               { return count; }
    public boolean isEmpty()        { return count == 0; }
    public boolean contains(T key)  { return find(key) != null; }

                                /*** Main Operations ***/

                                /* Insertion */

    // This method checks if the array is full or not. If so, then it creates a new one twice as bigger as the previous one

    private void checkFullness() {
        if (count == storage.length) {
            T[] newStorage = (T[]) new Object[storage.length * 2 + 1];
            System.arraycopy(storage, 0, newStorage, 0, storage.length);
            storage = newStorage;
        }
    }

    private void shiftRightTill(int index) {
        for (int i = count; i > index; i--)
            storage[i] = storage[i - 1];
    }

    // Inserts data at the beginning of the array
    public void insert(T data) {
        checkFullness();

        shiftRightTill(0);

        storage[0] = data;
        ++count;
    }

    // Inserts data at specified index
    public void insert(T data, int index) {
        if (index < 0 || index > count) return;

        checkFullness();

        shiftRightTill(index);
        storage[index] = data;
        ++count;
    }

    // Appends data to the end of the array
    public void append(T data) {
        checkFullness();
        storage[count++] = data;
    }

                            /* Deletion */

    private void checkEmptiness() {
        if (isEmpty())
            throw new RuntimeException("The list is empty");
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Index out of bounds");
    }

    // This method shifts each item in the array to left and frees up memory by explicitly assigning
    // null to the last and redundant item
    private T shiftLeftTill(int index) {
        T temp = storage[index];

        for (int i = index; i < count; i++)
            storage[i] = storage[i + 1];

        --count;
        storage[count] = null;

        return temp;
    }

    public T delete(T key) {
        checkEmptiness();

        int index = 0;

        for (; index < count; index++) {
            if (storage[index].equals(key))
                break;
        }

        if (index == count) return null;

        return shiftLeftTill(index);
    }

    public T delete(int index) {
        checkEmptiness();
        checkIndex(index);

        return shiftLeftTill(index);
    }

    public T deleteFirst() {
        checkEmptiness();

        return shiftLeftTill(0);
    }

    public T deleteLast() {
        checkEmptiness();

        return shiftLeftTill(count - 1);
    }

    public void deleteAll() {
        T[] newStorage  = (T[]) new Object[storage.length];
        storage         = newStorage;
        count           = 0;
    }

                            /* Searching */
    public T find( T key) {
        checkEmptiness();

        for (int i = 0; i < count; i++) {
            if (storage[i].equals(key))
                return storage[i];
        }

        return null;
    }

                            /* Modification */
    public void modify(T data, int index) {
        checkEmptiness();
        checkIndex(index);

        storage[index] = data;
    }

                            /* Getting elements */
    public T get(int index) {
        checkEmptiness();
        checkIndex(index);

        return storage[index];
    }

    public T getFirst() {
        checkEmptiness();

        return storage[0];
    }

    public T getLast() {
        checkEmptiness();

        return storage[count - 1];
    }

    public int indexOf(T value) {
        checkEmptiness();

        int i = 0;

        for (; i < count; i++) {
            if (storage[i].equals(value))
                break;
        }

        if (i == count) return -1;

        return i;
    }
}
