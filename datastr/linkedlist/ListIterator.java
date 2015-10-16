package datastr.linkedlist;

/**
 * Created by Renat Kaitmazov on 01/08/15.
 */

public final class ListIterator<T> implements datastr.Iterable<T> {
    private List<T> list;
    private Link<T> current;

                            /*** Constructors ***/
    ListIterator(List<T> list) {
        this.list   = list;
        current     = ((LinkedList<T>)list).getHead();
    }

    @Override
    public String toString() {
        return current.getData().toString();
    }

                            /*** State Information ***/

    public boolean atEnd()          { return current == ((LinkedList) list).getTail(); }
    public boolean atBeginning()    { return current == ((LinkedList) list).getHead(); }
    public boolean hasNext() { return current.getNext() != null; }

                            /*** Main Operations ***/

    public void reset()     { current = ((LinkedList<T>)list).getHead(); }
    public T get()          { return current.getData(); }

    public void next() {
        if (atEnd()) return;
        current = current.getNext();
    }

    public void previous() {
        if (atBeginning()) return;
        current = current.getPrevious();
    }

    public void insertAfter(T data) {
        if (list.isEmpty() || atEnd()) {
            list.append(data);
            current = ((LinkedList<T>)list).getTail();
        }
        else {
            Link<T> newLink = new Link<>(data);

            newLink.setPrevious(current);
            newLink.setNext(current.getNext());
            current.getNext().setPrevious(newLink);
            current.setNext(newLink);
            // After insertion we shift the iterator to the right
            next();
            ((LinkedList<T>)list).incrementCount();
        }
    }

    public void insertBefore(T data) {

        if (list.isEmpty() || atBeginning()) {
            list.append(data);
            current = ((LinkedList<T>)list).getHead();
        }
        else {
            Link<T> newLink = new Link<>(data);

            ((LinkedList<T>)list).connect(newLink, current);
            // In this method we go backward after insertion
            previous();
            ((LinkedList<T>)list).incrementCount();
        }
    }

    public T delete() {
        if (list.isEmpty()) return null;


        if (atBeginning()) return list.deleteFirst();
        else if (atEnd())  return list.deleteLast();
        else {
            Link<T> temp = current;
            ((LinkedList<T>)list).disconnect(current);

            // After deletion we go backward
            current = current.getPrevious();
            ((LinkedList<T>)list).decrementCount();

            return temp.getData();
        }
    }
}
