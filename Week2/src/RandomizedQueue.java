import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q; // queue elements
    private int N = 0; // number of elements on queue

    public RandomizedQueue() {
        q = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    // resize the underlying array
    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            temp[i] = q[i];
        }
        q = temp;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException();
        // double size of array if necessary and recopy to front of array
        if (N == q.length)
            resize(2 * q.length); // double size of array if necessary
        q[N++] = item; // add item
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");
        int pos = StdRandom.uniform(N);
        Item item = q[pos];
        q[pos] = q[--N];
        q[N] = null;
        // shrink size of array if necessary
        if (N > 0 && N == q.length / 4)
            resize(q.length / 2);
        return item;
    }

    public Item sample() {
        if (N == 0)
            throw new java.util.NoSuchElementException();
        return q[StdRandom.uniform(N)];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizeQueueIterator();
    }

    private class RandomizeQueueIterator implements Iterator<Item> {
        private int i = N;
        private final Item[] shuffleItems;

        public RandomizeQueueIterator() {
            shuffleItems = (Item[]) new Object[N];
            System.arraycopy(q, 0, shuffleItems, 0, N);
            StdRandom.shuffle(shuffleItems, 0, N - 1);
        }

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (i == 0)
                throw new java.util.NoSuchElementException();
            return shuffleItems[--i];
        }
    }
}