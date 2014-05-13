
public class Subset {
    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }

        for (int i = 1; i <= num; i++)
            StdOut.println(rq.dequeue());
    }
}