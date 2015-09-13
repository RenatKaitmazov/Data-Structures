package datastr.linkedlist;

import java.util.Iterator;

/**
 * Created by Renat Kaitmazov on 30/07/15.
 */

public final class LinkedList<T> implements List<T> {

    private Link<T> head    = null;
    private Link<T> tail    = null;
    private int count       = 0;



            /*** Methods common to all objects ***/

    @Override
    public String toString() {

        if (isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder("[");

        for (Link<T> current = head; current != null; current = current.getNext()) {
            builder.append(current.toString());
            builder.append(", ");
        }

        int start = builder.length() - 2, end = builder.length();
        builder.replace(start, end, "]");

        return builder.toString();
    }


    // An analog to clone() method
    public LinkedList<T> makeCopy() {
        LinkedList<T> copy = new LinkedList<>();

        Link<T> current = head;

        while (current != null) {
            copy.append(current.getData());
            current = current.getNext();
        }

        copy.count = count;

        return copy;
    }

            /*** State Information ***/

    public int size()               { return count; }
    public boolean isEmpty()        { return head == null; }
    public boolean contains(T key)  { return find(key) != null; }

            /*** Main Operations ***/

    private Link<T> getLink(int index) {
        if (isEmpty() || index < 0 || index >= size()) return null;

        Link<T> current = head;

        boolean isLessThanMiddle = index < size() / 2;

        if (isLessThanMiddle) {

            for (int i = 0; i < index; i++)
                current = current.getNext();

        } else {

            current = tail;

            for (int i = size() - 1; i > index; i--)
                current = current.getPrevious();

        }

        return current;
    }

            /* Insertion */

    void connect(Link<T> newLink, Link<T> current) {
        newLink.setNext(current);
        newLink.setPrevious(current.getPrevious());
        current.getPrevious().setNext(newLink);
        current.setPrevious(newLink);
    }

    // inserts an item at the beginning of a list
    public void insert(T data) {
        Link<T> newLink = new Link<>(data);

        if (isEmpty())
            tail = newLink;
        else {
            newLink.setNext(head);
            head.setPrevious(newLink);
        }

        head = newLink;
        ++count;
    }

    public void insert(T data, int index) {

        checkIndex(index);

        if (index == 0 || isEmpty()) {
            insert(data);
            return;
        }

        if (index == size()) {
            append(data);
            return;
        }

        Link<T> newLink = new Link<>(data);
        Link<T> current = getLink(index);

        connect(newLink, current);

        ++count;
    }

    // Appends an item to the end of a list
    public void append(T data) {
        Link<T> newLink = new Link<>(data);

        if (isEmpty())
            head = newLink;
        else {
            tail.setNext(newLink);
            newLink.setPrevious(tail);
        }
        tail = newLink;

        ++count;
    }

            /* Deletion */

    private void checkEmptiness() {
        if (isEmpty())
            throw new RuntimeException("The list is empty");
    }

    void disconnect(Link<T> link) {
        link.getPrevious().setNext(link.getNext());
        link.getNext().setPrevious(link.getPrevious());
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= count)
            throw new IllegalArgumentException("Index out of bounds");
    }

    public T delete(T key) {
        checkEmptiness();
        if (key == head.getData())  return deleteFirst();
        if (key == tail.getData())  return deleteLast();

        Link<T> current = head;

        while (current.getData() != key) {
            current = current.getNext();
            if (current == null) return null;
        }

        disconnect(current);

        --count;

        return current.getData();
    }

    public T delete(int index) {
        checkEmptiness();
        checkIndex(index);
        if (index == 0)                     return deleteFirst();
        if (index == size() - 1)            return deleteLast();


        Link<T> current = getLink(index);

        disconnect(current);

        --count;

        return current.getData();
    }

    public T deleteFirst()  {
        checkEmptiness();

        Link<T> temp = head;

        if (size() == 1) tail = null;
        else temp.getNext().setPrevious(null);

        head = head.getNext();

        --count;

        return temp.getData();
    }

    public T deleteLast()   {
        checkEmptiness();

        Link<T> temp = tail;

        if (size() == 1) head = null;
        else temp.getPrevious().setNext(null);

        tail = tail.getPrevious();

        --count;

        return temp.getData();
    }

    public void deleteAll() {
        head    = null;
        tail    = null;
        count   = 0;
    }

                /* Modification */
    public void modify(int index, T data) {
        checkEmptiness();
        Link<T> current = getLink(index);
        if (current != null) current.setData(data);
    }

                /* Searching */

    public T find(T key) {
        if (isEmpty()) return null;

        Link<T> current = head;

        while (!current.getData().equals(key)) {
            current = current.getNext();
            if (current == null) return null;
        }

        return current.getData();
    }

                /* Getting elements */

    public T get(int index) {
        checkEmptiness();
        checkIndex(index);
        Link<T> current = getLink(index);
        return current != null ? current.getData() : null;
    }
    public T getFirst() {
        checkEmptiness();
        return get(0);
    }

    public T getLast()  {
        checkEmptiness();
        return get(size() - 1);
    }

    public int indexOf(T value) {
        checkEmptiness();

        int i = 0;

        Link<T> current = head;

        while (!current.getData().equals(value)) {
            current = current.getNext();
            if (current == null) return -1;
            ++i;
        }

        return i;
    }

    // Getters
    Link<T> getHead() { return head; }
    Link<T> getTail() { return tail; }

    // Operations on count for iterators
    void incrementCount() { ++count; }
    void decrementCount() { --count; }

    // Getting iterator
    public ListIterator<T> getIterator() { return new ListIterator<T>(this); }
}
