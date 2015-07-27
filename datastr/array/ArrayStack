package datastr.array;

/**
 * Created by Renat Kaitmazov on 25/07/15.
 */

import java.util.Arrays;
import datastr.Stack;

/**
 * This class uses an array as an underlying mechanism
 */

public final class ArrayStack<T> implements Stack<T>{
    private static final int INITIAL_SIZE = 16;

    // Instance variables
    private T[] storage;
    private int count;

                        /*** Constructors ***/

    public ArrayStack() {
        // If a client does not provide initial size, we are going to use default initial size which is equal to 16
        storage = (T[]) new Object[INITIAL_SIZE];
        count   = 0;
    }

    public ArrayStack(int size) {
        storage = (T[]) new Object[size];
        count   = 0;
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

            int start = builder.length() - 2;
            int end = builder.length();
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
        if (obj == null || !(obj instanceof ArrayStack)) return false;

        ArrayStack<T> stack = (ArrayStack<T>)obj;

        return Arrays.equals(storage, stack.storage);
    }

    // Analog to clone() method
    public ArrayStack<T> makeCopy() {
        int length          = size();
        ArrayStack<T> copy  = new ArrayStack<>(storage.length);
        copy.count          = count;
        System.arraycopy(storage, 0, copy.storage, 0, length);
        return copy;
    }

                        /*** State information ***/

    public boolean isEmpty() { return count == 0; }

    public int size() { return storage.length; }

    public int elementsCount() { return count; }

                        /*** Main operations ***/

    // If the array is full we create a new one that is twice as big as the previous array
    private void checkFullness() {
        if (count == storage.length) {
            int newSize         = storage.length * 2 + 1;
            T[] resizedArray    = (T[]) new Object[newSize];
            System.arraycopy(storage, 0, resizedArray, 0, storage.length);
            storage = resizedArray;
        }
    }

    private void checkEmptiness() {
        if (isEmpty())
            throw new RuntimeException("datastr.Stack is empty");
    }

    public void push(T element) {
        checkFullness();

        storage[count++] = element;
    }

    public T pop() {
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
