package comp2402a2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DuperFast implements DuperDeque {

    /**
     * Two-halves deque:
     * - left: stores the FRONT half in reverse order (top = front)
     * - right: stores the BACK half in normal order (top = back)
     * <p>
     * Deque order: left(top..bottom), then right(bottom..top)
     */
    private final Half left;
    private final Half right;

    public DuperFast() {
        left = new Half();
        right = new Half();
    }

    public void addFirst(Integer x) {
        left.push(x);
        balanceIfNeeded();
    }

    public void addLast(Integer x) {
        right.push(x);
        balanceIfNeeded();
    }

    public Integer removeFirst() {
        if (size() == 0) return null;
        ensureLeftHasFront();
        Integer v = left.pop();
        balanceIfNeeded();
        return v;
    }

    public Integer removeLast() {
        if (size() == 0) return null;

        // If right is empty, the only element(s) must be in left (this mainly happens when size()==1)
        Integer v;
        if (right.size() > 0) {
            v = right.pop();
        } else {
            // size==1 case: left has the only element as its top
            v = left.pop();
        }

        balanceIfNeeded();
        return v;
    }

    public void swapEnds() {
        if (size() <= 1) return;

        Integer first = removeFirst();
        Integer last = removeLast();

        addFirst(last);
        addLast(first);
    }

    public int firstKeven(int k) {
        if (k <= 0 || size() == 0) return 0;

        int m = Math.min(k, size());
        int fromLeft = Math.min(m, left.size());

        int cnt = left.topKeven(fromLeft);

        int rem = m - fromLeft;
        if (rem > 0) {
            // remaining are taken from the FRONT of right, which is the BOTTOM of right
            cnt += right.bottomKeven(rem);
        }
        return cnt;
    }

    public int lastKeven(int k) {
        if (k <= 0 || size() == 0) return 0;

        int m = Math.min(k, size());
        int fromRight = Math.min(m, right.size());

        int cnt = right.topKeven(fromRight);

        int rem = m - fromRight;
        if (rem > 0) {
            // remaining are taken from the BACK part that spills into left, i.e., BOTTOM of left
            cnt += left.bottomKeven(rem);
        }
        return cnt;
    }

    public Integer maxDiff() {
        if (size() < 2) return null;

        Integer lmin = left.min();
        Integer rmin = right.min();
        Integer lmax = left.max();
        Integer rmax = right.max();

        int mn;
        if (lmin == null) mn = rmin;
        else if (rmin == null) mn = lmin;
        else mn = Math.min(lmin, rmin);

        int mx;
        if (lmax == null) mx = rmax;
        else if (rmax == null) mx = lmax;
        else mx = Math.max(lmax, rmax);

        return mx - mn;
    }

    public int size() {
        return left.size() + right.size();
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            // left is stored reversed: top=front, so iterate left from end->start
            int i = left.size() - 1;
            // right is stored normal: bottom is index 0
            int j = 0;
            boolean inLeft = true;

            public boolean hasNext() {
                if (inLeft) {
                    if (i >= 0) return true;
                    inLeft = false;
                }
                return j < right.size();
            }

            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                if (inLeft) {
                    return left.get(i--);
                } else {
                    return right.get(j++);
                }
            }
        };
    }

    // ------------------ balancing helpers ------------------

    private void ensureLeftHasFront() {
        if (left.size() == 0 && size() > 0) rebalance();
    }

    private void balanceIfNeeded() {
        int nl = left.size();
        int nr = right.size();
        int n = nl + nr;

        if (n <= 1) {
            // keep single element in left if it exists
            if (n == 1 && nl == 0 && nr == 1) rebalance();
            return;
        }

        // Rebuild when significantly imbalanced (amortized O(1))
        if (nl * 3 < nr || nr * 3 < nl) {
            rebalance();
        }
    }

    private void rebalance() {
        // Collect elements in deque order
        ArrayList<Integer> all = new ArrayList<>(size());
        for (Integer x : this) all.add(x);

        int n = all.size();
        int newLeftSize = (n + 1) / 2;

        left.clear();
        right.clear();

        // left stores first newLeftSize elements in reverse order so that top=front
        for (int i = newLeftSize - 1; i >= 0; i--) {
            left.push(all.get(i));
        }

        // right stores remaining in normal order so that top=back
        for (int i = newLeftSize; i < n; i++) {
            right.push(all.get(i));
        }
    }

    // ------------------ internal half structure ------------------

    private static class Half {
        private final ArrayList<Integer> a;
        private final ArrayList<Integer> evenPref; // #even in a[0..i]
        private final ArrayList<Integer> minPref;  // min in a[0..i]
        private final ArrayList<Integer> maxPref;  // max in a[0..i]

        Half() {
            a = new ArrayList<>();
            evenPref = new ArrayList<>();
            minPref = new ArrayList<>();
            maxPref = new ArrayList<>();
        }

        int size() {
            return a.size();
        }

        Integer get(int i) {
            return a.get(i);
        }

        void clear() {
            a.clear();
            evenPref.clear();
            minPref.clear();
            maxPref.clear();
        }

        void push(Integer x) {
            a.add(x);
            int n = a.size();

            int prevEven = (n >= 2) ? evenPref.get(n - 2) : 0;
            int prevMin = (n >= 2) ? minPref.get(n - 2) : Integer.MAX_VALUE;
            int prevMax = (n >= 2) ? maxPref.get(n - 2) : Integer.MIN_VALUE;

            evenPref.add(prevEven + ((x % 2 == 0) ? 1 : 0));
            minPref.add(Math.min(prevMin, x));
            maxPref.add(Math.max(prevMax, x));
        }

        Integer pop() {
            if (a.size() == 0) return null;
            int last = a.size() - 1;
            evenPref.remove(last);
            minPref.remove(last);
            maxPref.remove(last);
            return a.remove(last);
        }

        Integer min() {
            if (a.size() == 0) return null;
            return minPref.get(a.size() - 1);
        }

        Integer max() {
            if (a.size() == 0) return null;
            return maxPref.get(a.size() - 1);
        }

        int topKeven(int k) {
            if (k <= 0 || a.size() == 0) return 0;
            int n = a.size();
            int m = Math.min(k, n);
            int total = evenPref.get(n - 1);
            int before = (n - m - 1 >= 0) ? evenPref.get(n - m - 1) : 0;
            return total - before;
        }

        int bottomKeven(int k) {
            if (k <= 0 || a.size() == 0) return 0;
            int m = Math.min(k, a.size());
            return evenPref.get(m - 1);
        }
    }
}
