package datastr.array;

import datastr.Queue;
import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * Created by RenatKaitmazov on 26/07/15.
 */
public final class ArrayPriorityQueue<T extends Comparable<T>> implements Queue<T> {
    private static final int INITIAL_SIZE = 16;

    private T[] storage;
    private int count;
    private Class<T> clazz;

                    /*** Constructors ***/

    public ArrayPriorityQueue(Class<T> clazz) {
        storage     = (T[]) Array.newInstance(clazz, INITIAL_SIZE);
        count       = 0;
        this.clazz  = clazz;
    }

    public ArrayPriorityQueue(Class<T> clazz, int size) {
        storage     = (T[]) Array.newInstance(clazz, size);
        count       = 0;
        this.clazz  = clazz;
    }

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

            int start   = builder.length() - 2;
            int end     = builder.length();
            builder.replace(start, end, "]");

            return builder.toString();
        } else
            return "[]";
    }

    @Override
    public int hashCode() {
        int result = 17;
            result = 31 * result + Arrays.hashCode(storage);
            result = 31 * result + count;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof ArrayPriorityQueue)) return false;

        ArrayPriorityQueue<T> priorityQueue = (ArrayPriorityQueue<T>)obj;

        return Arrays.equals(storage, priorityQueue.storage);
    }

    // Analog to clone() method
    public ArrayPriorityQueue<T> makeCopy() {
        int length                  = storage.length;
        ArrayPriorityQueue<T> copy  = new ArrayPriorityQueue<T>(clazz, length);
        copy.count                  = count;
        System.arraycopy(storage, 0, copy.storage, 0, length);
        return copy;
    }

                                /*** State information ***/

    public int size() { return storage.length; }

    public int elementsCount() { return count; }

    public boolean isEmpty() { return count == 0; }

                                /*** Main operations ***/
    private void checkFullness() {
        if (count == storage.length) {
            int newSize         = storage.length * 2 + 1;
            T[] resizedArray    = (T[]) Array.newInstance(clazz, newSize);
            System.arraycopy(storage, 0, resizedArray, 0, storage.length);
            storage = resizedArray;
        }
    }

    private void checkEmptiness() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");
    }

    public void enqueue(T element) {
        checkFullness();

        if (isEmpty())
            storage[count] = element;
        else {

            int i = count;

            while (i > 0 && storage[i - 1].compareTo(element) > 0) {
                storage[i] = storage[i - 1];
                i--;
            }

            storage[i] = element;
        }

        count++;
    }

    public T dequeue() {
        checkEmptiness();

        T temp          = storage[--count];
        storage[count]  = null;
        return temp;
    }

    public T peek() {
        checkEmptiness();

        return storage[count - 1];
    }
}
