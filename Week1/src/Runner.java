import java.lang.reflect.Field;
import java.util.Scanner;

public class Runner {
    public static void main(String args[]) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        QU u = new QU(10);

        String[] pairs = s.split(" ");
        for (String pair : pairs) {
            String[] pq = pair.split("-");
            int p = Integer.valueOf(pq[0]);
            int q = Integer.valueOf(pq[1]);
            u.union(p, q);
        }

        Field f = u.getClass().getDeclaredField("id");
        f.setAccessible(true);
        int[] ids = (int[]) f.get(u);
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i : ids) {
            System.out.print(i + " ");
        }
        sc.close();
    }
}

class QU {
    int[] id;

    public QU(int n) {
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    public void union(int p, int q) {
        int t = id[p];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == t)
                id[i] = id[q];
        }
    }
}
