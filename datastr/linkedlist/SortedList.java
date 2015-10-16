package datastr.linkedlist;

/**
 * Created by Renat Kaitmazov on 01/08/15.
 */

public final class SortedList<T extends Comparable<T>> implements List<T> {

    private Link<T> head    = null;
    private Link<T> tail    = null;
    private int count       = 0;

                            /*** Methods common to all objects ***/

    @Override
    public String toString() {
        if (isEmpty()) return "[]";

        StringBuilder builder = new StringBuilder("[");

        Link<T> current = head;

        while (current != null) {
            builder.append(current.toString());
            builder.append(", ");
            current = current.getNext();
        }

        int start   = builder.length() - 2;
        int end     = builder.length();
        builder.replace(start, end, "]");

        return builder.toString();
    }

    // An analog to clone() method
    public SortedList<T> makeCopy() {
        SortedList<T> copy = new SortedList<>();

        Link<T> current = head;

        while (current != null) {
            copy.append(current.getData());
            current = current.getNext();
        }

        copy.count = count;

        return copy;
    }

                            /*** State Information ***/

    public boolean isEmpty()        { return head == null; }
    public int size()               { return count; }
    public boolean contains(T key)  { return find(key) != null; }

                            /*** Main Operations ***/

                            /* Insertion */

    public void append(T data) {
        Link<T> newLink = new Link<>(data);

        if (isEmpty()) {
            head = newLink;
            tail = newLink;
        } else {

            Link<T> current = head;

            while (current != null && current.getData().compareTo(data) < 0)
                current = current.getNext();

            if (current == head) {
                newLink.setNext(current);
                head.setPrevious(newLink);
                head = newLink;
            } else if (current == null) {
                newLink.setPrevious(tail);
                tail.setNext(newLink);
                tail = newLink;
            } else {
                newLink.setNext(current);
                newLink.setPrevious(current.getPrevious());
                current.getPrevious().setNext(newLink);
                current.setPrevious(newLink);
            }

        }

        ++count;
    }

                            /* Deletion */

    private Link<T> getLink(int index) {

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

    private void disconnect(Link<T> link) {
        link.getPrevious().setNext(link.getNext());
        link.getNext().setPrevious(link.getPrevious());
    }

    private void checkEmptiness() {
        if (isEmpty())
            throw new RuntimeException("The list is empty");
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size())
            throw new IllegalArgumentException("Index out of bounds");
    }

    public T delete(T key) {
        checkEmptiness();

        Link<T> current = head;

        while (!current.getData().equals(key)) {
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

        if (index == 0)             return deleteFirst();
        if (index == size() - 1)    return deleteLast();

        Link<T> temp = getLink(index);

        disconnect(temp);

        --count;

        return temp.getData();
    }

    public T deleteFirst() {
        checkEmptiness();

        Link<T> temp = head;

        if (count == 1) tail = null;
        else temp.getNext().setPrevious(null);

        head = head.getNext();
        --count;

        return temp.getData();
    }

    public T deleteLast() {
        checkEmptiness();

        Link<T> temp = tail;

        if (isEmpty()) head = null;
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

                            /* Searching */

    public T find(T key) {
        checkEmptiness();

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

    public T getLast() {
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

}
