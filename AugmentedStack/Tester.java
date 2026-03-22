package comp2402a4;

import java.util.Iterator;
import java.util.Random;

public class Tester {
    static boolean useSeed = true;

    static void showContents(Iterable<Integer> ds) {
        System.out.print("[");
        Iterator<Integer> it = ds.iterator();
        while (it.hasNext()) {
            System.out.print(it.next());
            if (it.hasNext()) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    static void compare(UltraStack a, UltraStack b) {
        if (a.size() != b.size()) {
            throw new RuntimeException("Size mismatch: " + a.size() + " vs " + b.size());
        }

        for (int i = 0; i < a.size(); i++) {
            Integer x = a.get(i);
            Integer y = b.get(i);
            if ((x == null && y != null) || (x != null && !x.equals(y))) {
                throw new RuntimeException("get(" + i + ") mismatch: " + x + " vs " + y);
            }
        }

        Integer md1 = a.maxDiff();
        Integer md2 = b.maxDiff();
        if ((md1 == null && md2 != null) || (md1 != null && !md1.equals(md2))) {
            throw new RuntimeException("maxDiff mismatch: " + md1 + " vs " + md2);
        }

        for (int k = 0; k <= a.size() + 2; k++) {
            int t1 = a.topKodd(k);
            int t2 = b.topKodd(k);
            if (t1 != t2) {
                throw new RuntimeException("topKodd(" + k + ") mismatch: " + t1 + " vs " + t2);
            }
        }
    }

    static void randomTest(int ops) {
        UltraStack slow = new UltraSlow();
        UltraStack fast = new UltraFast();

        Random rand = useSeed ? new Random(2402) : new Random();

        for (int step = 0; step < ops; step++) {
            int choice = rand.nextInt(7);

            if (choice == 0) {
                int x = rand.nextInt(6000001) - 3000000;
                slow.push(x);
                fast.push(x);
            } else if (choice == 1) {
                Integer a = slow.pop();
                Integer b = fast.pop();
                if ((a == null && b != null) || (a != null && !a.equals(b))) {
                    throw new RuntimeException("pop mismatch: " + a + " vs " + b);
                }
            } else if (choice == 2) {
                int i = rand.nextInt(slow.size() + 4) - 2;
                Integer a = slow.get(i);
                Integer b = fast.get(i);
                if ((a == null && b != null) || (a != null && !a.equals(b))) {
                    throw new RuntimeException("get(" + i + ") mismatch: " + a + " vs " + b);
                }
            } else if (choice == 3) {
                int i = rand.nextInt(slow.size() + 4) - 2;
                int x = rand.nextInt(6000001) - 3000000;
                Integer a = slow.set(i, x);
                Integer b = fast.set(i, x);
                if ((a == null && b != null) || (a != null && !a.equals(b))) {
                    throw new RuntimeException("set(" + i + "," + x + ") mismatch: " + a + " vs " + b);
                }
            } else if (choice == 4) {
                int k = rand.nextInt(slow.size() + 5);
                int a = slow.topKodd(k);
                int b = fast.topKodd(k);
                if (a != b) {
                    throw new RuntimeException("topKodd(" + k + ") mismatch: " + a + " vs " + b);
                }
            } else if (choice == 5) {
                Integer a = slow.maxDiff();
                Integer b = fast.maxDiff();
                if ((a == null && b != null) || (a != null && !a.equals(b))) {
                    throw new RuntimeException("maxDiff mismatch: " + a + " vs " + b);
                }
            } else {
                if (slow.size() != fast.size()) {
                    throw new RuntimeException("size mismatch: " + slow.size() + " vs " + fast.size());
                }
            }

            compare(slow, fast);
        }

        System.out.println("Random test passed: " + ops + " operations");
    }

    public static void main(String[] args) {
        randomTest(200000);
    }
}