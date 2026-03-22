package comp2402a3;
import java.util.*;

public class SkipListTester {
    static <T> void showContents(Iterable<T> ds) {
        System.out.print("[");
        Iterator<T> it = ds.iterator();
        while (it.hasNext()) {
            System.out.print(it.next());
            if (it.hasNext()) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    static void skipItTest(int n){
		System.out.println("SkipList:");
        Random rand = new Random();
        IndexedSSet<Integer> iss = new SkipItSlow<>();
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(3*n);
            System.out.println("add(" + x + ") = " + iss.add(x));
        }
        System.out.print("Contents: ");
        showContents(iss);

        System.out.println("size()=" + iss.size());

        for (int i = 0; i < iss.size(); i++) {
            System.out.println("get(" + i + ")=" + iss.get(i));
        }

		int i = rand.nextInt(n+3);
		int j = rand.nextInt(n+3);
		System.out.println("deleteSpan(" + i + ", " + j + ")");
		iss.deleteSpan(i, j);
		System.out.print("Contents: ");
		showContents(iss);
    }

    public static void main(String[] args) {
        skipItTest(20);
    }
}
