package comp2402a2;

import java.util.ArrayList;
import java.util.Iterator;

public class SuperFast implements SuperStack {
    private final ArrayList<Integer> a;      // values
    private final ArrayList<Integer> oddPref; // odd count from bottom: oddPref[i] = #odd in a[0..i]
    private final ArrayList<Integer> minPref; // min from bottom: minPref[i] = min(a[0..i])
    private final ArrayList<Integer> maxPref; // max from bottom: maxPref[i] = max(a[0..i])

    public SuperFast() {
        a = new ArrayList<>();
        oddPref = new ArrayList<>();
        minPref = new ArrayList<>();
        maxPref = new ArrayList<>();
    }

    public void push(Integer x) {
        a.add(x);

        int n = a.size();
        int prevOdd = (n >= 2) ? oddPref.get(n - 2) : 0;
        int prevMin = (n >= 2) ? minPref.get(n - 2) : Integer.MAX_VALUE;
        int prevMax = (n >= 2) ? maxPref.get(n - 2) : Integer.MIN_VALUE;

        oddPref.add(prevOdd + ((x % 2 != 0) ? 1 : 0));
        minPref.add(Math.min(prevMin, x));
        maxPref.add(Math.max(prevMax, x));
    }

    public Integer pop() {
        if (size() <= 0) return null;

        int last = a.size() - 1;
        oddPref.remove(last);
        minPref.remove(last);
        maxPref.remove(last);
        return a.remove(last);
    }

    public void doubleTop() {
        if (size() > 0) {
            Integer x = a.get(a.size() - 1);
            push(x * 2);
        }
    }

    public void swapTop() {
        int n = size();
        if (n <= 1) return;

        // swap values
        int i1 = n - 1, i2 = n - 2;
        Integer t = a.get(i1);
        a.set(i1, a.get(i2));
        a.set(i2, t);

        // recompute prefix info only for last two indices (O(1))
        recomputeFrom(i2);
        recomputeFrom(i1);
    }

    private void recomputeFrom(int idx) {
        int n = a.size();
        if (idx < 0 || idx >= n) return;

        int prevOdd = (idx >= 1) ? oddPref.get(idx - 1) : 0;
        int prevMin = (idx >= 1) ? minPref.get(idx - 1) : Integer.MAX_VALUE;
        int prevMax = (idx >= 1) ? maxPref.get(idx - 1) : Integer.MIN_VALUE;

        int x = a.get(idx);

        oddPref.set(idx, prevOdd + ((x % 2 != 0) ? 1 : 0));
        minPref.set(idx, Math.min(prevMin, x));
        maxPref.set(idx, Math.max(prevMax, x));
    }

    public int topKodd(int k) {
        if (k <= 0 || size() == 0) return 0;

        int n = size();
        int m = Math.min(k, n);
        int totalOdd = oddPref.get(n - 1);
        int before = (n - m - 1 >= 0) ? oddPref.get(n - m - 1) : 0;
        return totalOdd - before;
    }

    public Integer maxDiff() {
        if (size() < 2) return null;
        int n = size();
        int mn = minPref.get(n - 1);
        int mx = maxPref.get(n - 1);
        return mx - mn; // max absolute difference = max - min
    }

    public int size() {
        return a.size();
    }

    public Iterator<Integer> iterator() {
        return a.iterator();
    }
}
