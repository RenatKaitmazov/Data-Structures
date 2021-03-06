package datastr.hashmap;

import java.lang.reflect.Array;

/**
 * Created by Renat Kaitmazov on 10/09/15.
 */

public final class HashMap<K extends Comparable<K>, V> implements Map<K, V> {

    /**
     *
     * @param <K> key
     * @param <V> value
     *
     * A class acting as a wrapper for storing data (key and value)
     */
    private class Item<K, V> {
        private K key;
        private V value;
        private boolean isDeleted; // An important field to simulate deletion

        Item(K key, V value) {
            this.key = key;
            this.value = value;
            isDeleted = false;
        }

        public String toString() { return String.format("%s=%s", key.toString(), value.toString()); }
        public int hashCode() { return key.hashCode(); }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || !(o instanceof Item)) return false;
            Item<K, V> item = ((Item<K, V>) o);

            return key.equals(item.key) && value.equals(item.value);
        }
    }

    private Item<K, V>[] items;
    private int count;
    private Class keyClass; // Need it for keySet() method to create a generic array containing keys
    private Class valueClass; // Need it for values() method to create a generic array containing values

    // If no initial capacity provided we create an array of size 31 (29 will eventually become 31)
    // which is a prime number
    public HashMap() { this(29); }

    public HashMap(int initialCapacity) {
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
        int size = getNearestPrimeNumber(initialCapacity << 1);
        items = (Item<K, V>[]) new Item[size];
    }

    public String toString() {
        if (isEmpty()) return "[]";

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < items.length; i++) {
            Item<K, V> item = items[i];

            if (item != null && !item.isDeleted) builder.append(item).append(", ");
        }

        int s = builder.length() - 2;
        int e = builder.length();
        builder.replace(s, e, "]");

        return builder.toString();
    }

    public boolean isEmpty() { return count == 0; }
    public int size() { return count; }
    public boolean contains(K key) { return get(key) != null; }

    /**
     *
     * @param i is a number with respect to which we calculate a prime number
     * @return the nearest prime number relative to i
     */
    private int getNearestPrimeNumber(int i) {
        int prime = i + 1;
        while (!isPrime(prime)) prime++;
        return prime;
    }

    private boolean isPrime(int number) {
        for (int i = 2; i * i <= number; i++) if (number % i == 0) return false;
        return true;
    }

    /**
     * We use the double hashing system to avoid collisions so we need to calculate a step to make a probe if a particular
     * cell is already occupied
     * @param key for which we calculate the second hash code
     * @return second hash code relative to some constant (5 in this case) instead of an array's size
     */
    private int calculateStep(K key) { return  5 - Math.abs(key.hashCode()) % 5; }

    /**
     * The more items in the array, the worse the main operations' efficiency becomes
     * and to avoid that we expand our array when it is 75% full.
     */
    private void checkFullness() {
        if ((double)count / items.length >= 0.75) {
            Item<K, V>[] newArray = (Item<K, V>[]) new Item[getNearestPrimeNumber(items.length << 1)];

            // Just copying the old items into the new array is not enough
            // We need to recalculate hash code for each element with respect to the new array's size
            // since it is now twice as big
            for (int i = 0; i < items.length; i++) {

                Item<K, V> item = items[i];

                if (item != null && !item.isDeleted) {
                    int index = Math.abs(item.hashCode()) % newArray.length;
                    int step = calculateStep(item.key);

                    while (newArray[index] != null) {
                        index += step;
                        index %= newArray.length;
                    }

                    newArray[index] = item;
                }

            }
            items = newArray;
        }
    }

    public void insert(K key, V value) {
        keyClass = key.getClass();
        valueClass = value.getClass();
        checkFullness();
        Item<K, V> newItem = new Item<>(key, value); // Create a new item

        // Calculate a hash code for the item which is actually an index at which the item will be inserted into the array
        int index = Math.abs(newItem.hashCode()) % items.length;

        // In case there is an item at that index above we calculate the second hash code which is actually a step
        int step = calculateStep(newItem.key);

        // Until we find a free cell
        while (items[index] != null) {
            // If we insert a key that's already is the map, we just change its value
            if (items[index].key.equals(key)) {
                items[index].value = value;
                return;
            }

            // If there is an item marked as deleted, we break the loop and insert the new item at this index
            if (items[index].isDeleted) break;

            // If the cell at the index is already occupied, check another cell
            index += step;

            // If we reach the end of the array, go to the beginning of the array
            index %= items.length;
        }

        // We have found a free cell
        items[index] = newItem;
        ++count;
    }

    public V get(K key) {
        if (isEmpty()) return null;

        // Calculate a hash code which is actually an index at which the item we are looking for might be stored
        int index = Math.abs(key.hashCode()) % items.length;

        // In case the cell at the index above is already occupied by another item
        int step = calculateStep(key);

        // We assume this is the item we are looking for
        Item<K, V> item = items[index];

        // If the item is not null and is not marked as deleted, check whether it is the item we need
        while (item != null && !item.isDeleted) {
            // If so, just return its value
            if (key.equals(item.key)) return item.value;


            // If not, check others in the array
            index += step;
            // Go to the beginning if the index has become greater than the array's length
            index %= items.length;

            // The next item we are going to test against the specified key
            item = items[index];
        }

        // If the item we are looking for is not in the map, we'll eventually bump into free cell
        // and after that we will quit the loop
        // It means we couldn't find the specified key
        return null;
    }

    public V delete(K key) {
        if (isEmpty()) return null;

        // Calculate a hash code which is actually an index at which the item we are looking for might be stored
        int index = Math.abs(key.hashCode()) % items.length;

        // In case the cell at the index above is already occupied by another item
        int step = calculateStep(key);

        // We assume this is the item we are looking for
        Item<K, V> item = items[index];

        // If the item is not null and is not marked as deleted, check whether it is the item we need
        while (item != null && !item.isDeleted) {
            // If so, mark it as deleted and return its value
            // We can't explicitly delete the item by making it null because it will break our code for the main operations, making it less efficient
            if (item.key.equals(key)) {
                --count;
                item.isDeleted = true;
                return item.value;
            }

            // If not, check others in the array
            index += step;

            // Go to the beginning if the index has become greater than the array's length
            index %= items.length;

            // Test another item
            item = items[index];
        }

        // If the item we are looking for is not in the map, we'll eventually bump into free cell
        // and after that we will quit the loop
        // It means we couldn't find the specified key, so there is nothing to delete and just return null
        return null;
    }

    public K[] keySet() {
        if (isEmpty()) return null;

        K[] keySet = (K[]) Array.newInstance(keyClass, count);

        for (int i = 0, j = 0; i < items.length; i++) {
            Item<K, V> item = items[i];

            if (item != null && !item.isDeleted) keySet[j++] = item.key;
        }

        return keySet;
    }

    public V[] values() {
        if (isEmpty()) return null;

        V[] values = (V[]) Array.newInstance(valueClass, count);

        for (int i = 0, j = 0; i < items.length; i++) {
            Item<K, V> item = items[i];

            if (item != null && !item.isDeleted) values[j++] = item.value;
        }

        return values;
    }
}
