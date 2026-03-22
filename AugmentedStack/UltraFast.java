package comp2402a4;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class UltraFast implements UltraStack {
    private static final int EMPTY_MIN = Integer.MAX_VALUE;
    private static final int EMPTY_MAX = Integer.MIN_VALUE;

    private int[] values;
    private int n;

    // base = number of leaves in iterative segment tree
    private int base;

    private int[] oddTree;
    private int[] minTree;
    private int[] maxTree;

    public UltraFast() {
        base = 1;
        values = new int[1];

        oddTree = new int[2 * base];
        minTree = new int[2 * base];
        maxTree = new int[2 * base];

        for (int i = 1; i < 2 * base; i++) {
            minTree[i] = EMPTY_MIN;
            maxTree[i] = EMPTY_MAX;
        }

        n = 0;
    }

    public void push(Integer x) {
        if (n == values.length) {
            resize();
        }
        values[n] = x;
        setLeaf(n, x);
        n++;
    }

    public Integer pop() {
        if (n == 0) {
            return null;
        }

        int old = values[n - 1];
        setLeafEmpty(n - 1);
        n--;
        return old;
    }

    public Integer get(int i) {
        if (i < 0 || i >= n) {
            return null;
        }
        return values[i];
    }

    public Integer set(int i, int x) {
        if (i < 0 || i >= n) {
            return null;
        }

        int old = values[i];
        values[i] = x;
        setLeaf(i, x);
        return old;
    }

    public int topKodd(int k) {
        if (k <= 0 || n == 0) {
            return 0;
        }

        if (k > n) {
            k = n;
        }

        int left = n - k;
        int right = n - 1;
        return rangeOdd(left, right);
    }

    public Integer maxDiff() {
        if (n < 2) {
            return null;
        }
        return maxTree[1] - minTree[1];
    }

    public int size() {
        return n;
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int index = 0;

            public boolean hasNext() {
                return index < n;
            }

            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return values[index++];
            }
        };
    }

    private void resize() {
        int newCap = values.length * 2;
        int[] newValues = new int[newCap];
        System.arraycopy(values, 0, newValues, 0, n);
        values = newValues;

        // base matches values capacity and stays a power of 2
        base = newCap;

        oddTree = new int[2 * base];
        minTree = new int[2 * base];
        maxTree = new int[2 * base];

        for (int i = 1; i < 2 * base; i++) {
            minTree[i] = EMPTY_MIN;
            maxTree[i] = EMPTY_MAX;
        }

        for (int i = 0; i < n; i++) {
            int pos = base + i;
            int v = values[i];
            oddTree[pos] = (v % 2 != 0) ? 1 : 0;
            minTree[pos] = v;
            maxTree[pos] = v;
        }

        for (int i = base - 1; i >= 1; i--) {
            pull(i);
        }
    }

    private void setLeaf(int index, int value) {
        int pos = base + index;
        oddTree[pos] = (value % 2 != 0) ? 1 : 0;
        minTree[pos] = value;
        maxTree[pos] = value;

        pos /= 2;
        while (pos >= 1) {
            pull(pos);
            pos /= 2;
        }
    }

    private void setLeafEmpty(int index) {
        int pos = base + index;
        oddTree[pos] = 0;
        minTree[pos] = EMPTY_MIN;
        maxTree[pos] = EMPTY_MAX;

        pos /= 2;
        while (pos >= 1) {
            pull(pos);
            pos /= 2;
        }
    }

    private void pull(int i) {
        int left = 2 * i;
        int right = left + 1;

        oddTree[i] = oddTree[left] + oddTree[right];
        minTree[i] = Math.min(minTree[left], minTree[right]);
        maxTree[i] = Math.max(maxTree[left], maxTree[right]);
    }

    private int rangeOdd(int left, int right) {
        int sum = 0;
        left += base;
        right += base;

        while (left <= right) {
            if ((left & 1) == 1) {
                sum += oddTree[left];
                left++;
            }
            if ((right & 1) == 0) {
                sum += oddTree[right];
                right--;
            }
            left /= 2;
            right /= 2;
        }

        return sum;
    }
}