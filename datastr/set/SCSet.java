package datastr.set;

/**
 * Created by Renat Kaitmazov on 19/08/15.
 */


import datastr.linkedlist.LinkedList;
import datastr.linkedlist.ListIterator;

import java.lang.reflect.Array;

/**
 * SC in the name of the class stands for Separate Chaining
 */

public final class SCSet<T> implements Set<T> {
    private LinkedList<T>[] lists;
    private int count;
    private Class clazz; // Need it for toArray() method

    public SCSet(int initialCapacity) {
        count = 0;
        lists = (LinkedList<T>[]) new LinkedList[initialCapacity];
    }

    public SCSet() { this(16); } // If no initial value is provided, 16 is the default size


    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        StringBuilder builder = new StringBuilder("[");

        for (LinkedList<T> list: lists) {
            ListIterator<T> iterator = null;
            if (list != null && !list.isEmpty()) {
                iterator = list.getIterator();
                for (int i = 0; i < list.size(); i++) {
                    builder.append(iterator.get()).append(", ");
                    iterator.next();
                }
            }
        }

        int s = builder.length() - 2;
        int e = builder.length();
        builder.replace(s, e, "]");

        return builder.toString();
    }

    public boolean isEmpty() { return count == 0; }
    public int size() { return count; }

    public boolean contains(T element) { return get(element) != null; }

    private void checkFullness() {
        // Unlike sets using open addressing method this kind of set uses separate chaining
        // its load factor can be bigger than 1 so let it be 1,5
        boolean shouldExpand = (double) count / lists.length >= 1.5;
        if (shouldExpand) {
            // Create a new expanded array twice as big as the current one
            LinkedList<T>[] newArray = (LinkedList<T>[]) new LinkedList[lists.length << 1];

            // Check each linked list
            for (LinkedList<T> list: lists) {
                // If the list is not empty
                if (list != null && !list.isEmpty()) {
                    ListIterator<T> iterator = list.getIterator();
                    for (int j = 0; j < list.size(); j++) {
                        T element = iterator.get();
                        // Recalculate a new hash code with respect to the new array's size
                        int index = Math.abs(element.hashCode()) % newArray.length;

                        if (newArray[index] == null) newArray[index] = new LinkedList<>();

                        // Put it into a linked list which is located at the newly calculated index
                        newArray[index].append(element);
                        iterator.next();
                    }
                }
            }

            lists = newArray;
        }
    }

    public void insert(T element) {
        clazz = element.getClass();
        checkFullness();
        // Calculate index
        int index = Math.abs(element.hashCode()) % lists.length;
        LinkedList<T> list = lists[index];

        if (list == null) {
            list = new LinkedList<>();
            lists[index] = list;
        }

        // Then insert the element only if it is not already in the set
        if (!list.contains(element)) {
            list.append(element);
            ++count;
        }
    }

    public T get(T element) {
        if (isEmpty()) return null;

        // Calculate index
        int index = Math.abs(element.hashCode()) % lists.length;
        LinkedList<T> list = lists[index];

        // If list is not empty
        if (list != null &&!list.isEmpty()) return list.find(element);

        return null;
    }

    public T delete(T element) {
        if (isEmpty()) return null;

        // Calculate index
        int index = Math.abs(element.hashCode()) % lists.length;

        LinkedList<T> list = lists[index];

        // If the list is not empty
        if (list != null && !list.isEmpty()) {
            ListIterator<T> iterator = list.getIterator();
            for (int i = 0; i < list.size(); i++) {
                if (iterator.get().equals(element)) {
                    --count;
                    return iterator.delete();
                }
                iterator.next();
            }
        }

        return null;
    }

    public T[] toArray() {
        if (isEmpty()) return null;

        T[] array = (T[]) Array.newInstance(clazz, count);
        // Get each item in the set
        for (int i = 0, j = 0; i < lists.length; i++) {

            LinkedList<T> list = lists[i];

            // If the list is not empty
            if (list != null && !list.isEmpty()) {
                ListIterator<T> iterator = list.getIterator();

                for (int k = 0; k < list.size(); k++) {
                    array[j++] = iterator.get();
                    iterator.next();
                }
            }
        }

        return array;
    }
}
