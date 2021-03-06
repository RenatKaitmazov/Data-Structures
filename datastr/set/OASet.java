package datastr.set;

/**
 * Created by Renat Kaitmazov on 18/08/15.
 */

import java.lang.reflect.Array;

/**
 * OA in the name of the class stands for Open Addressing.
 * It uses double hashing method to find an appropriate place for an element to be stored in the array
 */

public final class OASet<T> implements Set<T> {
    /**
     * This class is going to be used as a wrapper for storing data.
     * It has an important boolean field we are going to use when deleting items.
     * We don't actually delete an item but just mark it as deleted by switching its field.
     */
    private static class Item<T> {
        T data;
        boolean isDeleted = false;

        Item(T data) { this.data = data; }

        @Override
        public String toString()    { return data.toString(); }

        @Override
        public int hashCode()       { return data.hashCode(); }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !(o instanceof Item)) return false;
            Item<T> item = ((Item<T>) o);

            return data.equals(item.data);
        }
    }

    private Item<T>[] storage;
    private int count = 0;
    private Class clazz; // Need it for toArray() method

    public OASet() {
        this(31);
    }

    public OASet(int initialCapacity) {
        /**
         * Let's imagine that a client wants a set of size 4. In this case a set of size 5 will be created instead
         * since it is the nearest prime number to 4. The calculateStep() method may return 5. If this is the case, now suppose
         * that 0th index is occupied. Now let's have a look at the snippet of code from insert() routine. It looks like this:
         * index   += step;
         * index   %= size;
         * 0 is occupied, so the next index is 0 + 5 = 5 and 5 % 5 = 0.
         * As you can see this situation can create an infinite loop and to avoid this we should check the size a client want a set to be.
         * It must not be equal to 4;
         */

        if (initialCapacity == 4) initialCapacity = 5;

        int size    = getNearestPrimeNumber(initialCapacity << 1);
        storage     = (Item<T>[]) new Item[size];
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "{}";
        else {
            StringBuilder builder = new StringBuilder("{");

            for (int i = 0; i < storage.length; i++) {
                Item<T> item = storage[i];

                if (item != null && !item.isDeleted)
                    builder.append(item.toString()).append(", ");
            }

            int start   = builder.length() - 2;
            int end     = builder.length();
            builder.replace(start, end, "}");

            return builder.toString();
        }
    }

    public boolean isEmpty() { return count == 0; }
    public int size() { return count; }
    public boolean contains(T element) { return get(element) != null; }


    private int abs(int value) {
        return value < 0 ? -value : value;
    }

    private int calculateStep(T forElement) {
        return 5 - (abs(forElement.hashCode()) % 5);
    }

    private boolean isPrimeNumber(int number) {
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) return false;
        }

        return true;
    }

    /**
     * Since we are going to use double hashing method here, it is vital that our array's size be equal to a prime number
     * Otherwise we can get an infinite loop while inserting an item if several items hash to the same index.
     * This is not the case when array's size equals to a prime number.
     */
    private int getNearestPrimeNumber(int number) {
        int prime = number + 1;

        while (!isPrimeNumber(prime))
            ++prime;
        return prime;
    }

    /**
     * When the array is full, we have to create a new one which is twice as big and move all the elements from the old array
     * to the newly created. Just copying won't do since in the insert() method we calculate an index based on the array's size.
     * After creating a new array its size is larger so we need to recalculate indices for each element.
     */

    private void checkFullness() {
        // Load factor is 0.75

        if (count >= storage.length * 0.75) {
            int newSize = getNearestPrimeNumber(storage.length * 2);
            Item<T>[] newArray = (Item<T>[]) new Object[newSize];

            for (int i = 0; i < storage.length; i++) {
                Item<T> item = storage[i];
                if (item != null && !item.isDeleted) {
                    int index = abs(item.hashCode()) % newSize;
                    int step = calculateStep(item.data);

                    while (newArray[index] != null) {
                        index += step;
                        index %= newSize; // Go to the beginning of the array
                    }

                    newArray[index] = item;
                }
            }

            storage = newArray;
        }
    }

    public void insert(T element) {
        clazz = element.getClass();
        checkFullness();

        Item<T> newItem = new Item<>(element);

        int size        = storage.length;
        int index       = abs(element.hashCode()) % size;
        int step        = calculateStep(newItem.data);
        Item<T> oldItem = storage[index];


        while (oldItem != null && !oldItem.isDeleted) {
            if (oldItem.data.equals(element))
                return;

            index += step;
            index %= size;

            oldItem = storage[index];
        }

        storage[index] = newItem;
        ++count;
    }

    public T delete(T element) {
        if (isEmpty()) return null;

        int size        = storage.length;
        int index       = abs(element.hashCode()) % size;
        int step        = calculateStep(element);

        while (storage[index] != null) {
            if (element.equals(storage[index].data)) {
                Item<T> item = storage[index];
                item.isDeleted = true;
                --count;
                return item.data;
            }

            index += step;
            index %= size;
        }

        return null;
    }

    public T get(T element) {
        if (isEmpty()) return null;

        int size    = storage.length;
        int index   = abs(element.hashCode()) % size;
        int step    = calculateStep(element);

        while (storage[index] != null) {
            if (storage[index].isDeleted || !storage[index].data.equals(element)) {
                index += step;
                index %= size;
                continue;
            }

            if (storage[index].data.equals(element))
                return storage[index].data;
        }

        return null;
    }

    public T[] toArray() {
        if (isEmpty()) return null;

        T[] array = (T[]) Array.newInstance(clazz, count);

        for (int i = 0, j = 0; i < storage.length; i++) {
            if (storage[i] != null && !storage[i].isDeleted)
                array[j++] = storage[i].data;
        }

        return array;
    }
}
