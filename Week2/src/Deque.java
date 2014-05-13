import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N; // number of elements on queue
    private Node first; // beginning of queue
    private Node last; // end of queue

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    public Deque() {
        first = last = null;
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        if (isEmpty()) last = first;
        else           oldFirst.previous = first;
        N++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (isEmpty()) first = last;
        else           oldlast.next = last;
        N++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item i = first.item;
        first = first.next;
        if (first != null)
            first.previous = null;
        else
            last = null;
        N--;
        return i;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item i = last.item;
        last = last.previous;
        if (last != null)
            last.next = null;
        else
            first = null;
        N--;
        return i;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
