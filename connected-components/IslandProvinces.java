package comp2402a5;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class IslandProvinces {

    // Union-Find structures for active component labels only
    private static int[] parent;
    private static int[] compSize;
    private static int[] freeStack;
    private static int freeTop;
    private static int nextLabel;

    // Marks used to track which roots appear in prev/current row
    private static int[] seenStamp;
    private static int stampCounter;

    private static void initUF(int initialCapacity) {
        parent = new int[initialCapacity];
        compSize = new int[initialCapacity];
        freeStack = new int[initialCapacity];
        seenStamp = new int[initialCapacity];
        freeTop = 0;
        nextLabel = 1; // label 0 means "no label"
        stampCounter = 1;
    }

    private static void ensureCapacity(int need) {
        if (need < parent.length) {
            return;
        }
        int newCap = parent.length;
        while (newCap <= need) {
            newCap *= 2;
        }

        int[] newParent = new int[newCap];
        int[] newCompSize = new int[newCap];
        int[] newFreeStack = new int[newCap];
        int[] newSeenStamp = new int[newCap];

        System.arraycopy(parent, 0, newParent, 0, parent.length);
        System.arraycopy(compSize, 0, newCompSize, 0, compSize.length);
        System.arraycopy(freeStack, 0, newFreeStack, 0, freeStack.length);
        System.arraycopy(seenStamp, 0, newSeenStamp, 0, seenStamp.length);

        parent = newParent;
        compSize = newCompSize;
        freeStack = newFreeStack;
        seenStamp = newSeenStamp;
    }

    private static int newLabel() {
        int x;
        if (freeTop > 0) {
            x = freeStack[--freeTop];
        } else {
            x = nextLabel++;
            ensureCapacity(x + 1);
        }
        parent[x] = x;
        compSize[x] = 0;
        return x;
    }

    private static void freeLabel(int x) {
        if (x <= 0) {
            return;
        }
        compSize[x] = 0;
        parent[x] = 0;
        freeStack[freeTop++] = x;
    }

    private static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (x != root) {
            int p = parent[x];
            parent[x] = root;
            x = p;
        }
        return root;
    }

    private static int union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return ra;
        }

        // Union by component size
        if (compSize[ra] < compSize[rb]) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }

        parent[rb] = ra;
        compSize[ra] += compSize[rb];
        compSize[rb] = 0;
        return ra;
    }

    private static int sumDigits(int x) {
        int sum = 0;
        while (x > 0) {
            sum += x % 10;
            x /= 10;
        }
        return sum;
    }

    /**
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException
     */
    public static void doIt(BufferedReader r, PrintWriter w) throws IOException {
        String firstLine = null;
        while ((firstLine = r.readLine()) != null) {
            firstLine = firstLine.trim();
            if (firstLine.length() > 0) {
                break;
            }
        }

        if (firstLine == null) {
            return;
        }

        int cols = firstLine.length();

        // Province totals indexed by digit sum.
        // Even for very large inputs, digit sums stay small.
        long[] provinceTotals = new long[128];

        // Only keep two rows of labels
        int[] prevLabels = new int[cols];
        int[] currLabels = new int[cols];

        // Unique active roots in previous/current row
        int[] prevRoots = new int[cols];
        int[] currRoots = new int[cols];
        int prevRootCount = 0;
        int currRootCount = 0;

        // UF capacity only needs to be proportional to row width
        initUF(Math.max(16, cols * 4 + 10));

        String line = firstLine;

        while (line != null) {
            line = line.trim();
            if (line.length() == 0) {
                line = r.readLine();
                continue;
            }

            // Clear current row labels
            for (int i = 0; i < cols; i++) {
                currLabels[i] = 0;
            }
            currRootCount = 0;

            // Process row
            for (int c = 0; c < cols; c++) {
                if (line.charAt(c) != '1') {
                    continue;
                }

                int label = 0;

                // Neighbors relevant for 8-connectivity in a left-to-right scan:
                // current row left:        (r, c-1)
                // previous row left/up/right: (r-1, c-1), (r-1, c), (r-1, c+1)

                if (c > 0 && currLabels[c - 1] != 0) {
                    label = find(currLabels[c - 1]);
                }
                if (c > 0 && prevLabels[c - 1] != 0) {
                    if (label == 0) label = find(prevLabels[c - 1]);
                    else label = union(label, prevLabels[c - 1]);
                }
                if (prevLabels[c] != 0) {
                    if (label == 0) label = find(prevLabels[c]);
                    else label = union(label, prevLabels[c]);
                }
                if (c + 1 < cols && prevLabels[c + 1] != 0) {
                    if (label == 0) label = find(prevLabels[c + 1]);
                    else label = union(label, prevLabels[c + 1]);
                }

                if (label == 0) {
                    label = newLabel();
                } else {
                    label = find(label);
                }

                currLabels[c] = label;
                compSize[label]++;
            }

            // Canonicalize current labels and collect unique current roots
            stampCounter++;
            for (int c = 0; c < cols; c++) {
                if (currLabels[c] != 0) {
                    currLabels[c] = find(currLabels[c]);
                    int root = currLabels[c];
                    if (seenStamp[root] != stampCounter) {
                        seenStamp[root] = stampCounter;
                        currRoots[currRootCount++] = root;
                    }
                }
            }

            // Finalize components that were active in previous row
            // but do not appear in current row anymore
            for (int i = 0; i < prevRootCount; i++) {
                int root = find(prevRoots[i]);
                if (seenStamp[root] != stampCounter) {
                    int size = compSize[root];
                    if (size > 0) {
                        provinceTotals[sumDigits(size)] += size;
                    }
                    freeLabel(root);
                }
            }

            // Swap previous/current rows
            int[] tmpLabels = prevLabels;
            prevLabels = currLabels;
            currLabels = tmpLabels;

            int[] tmpRoots = prevRoots;
            prevRoots = currRoots;
            currRoots = tmpRoots;
            prevRootCount = currRootCount;

            line = r.readLine();
        }

        // Finalize anything still active after the last row
        for (int i = 0; i < prevRootCount; i++) {
            int root = find(prevRoots[i]);
            int size = compSize[root];
            if (size > 0) {
                provinceTotals[sumDigits(size)] += size;
            }
            freeLabel(root);
        }

        ArrayList<Long> answers = new ArrayList<Long>();
        for (int i = 0; i < provinceTotals.length; i++) {
            if (provinceTotals[i] > 0) {
                answers.add(provinceTotals[i]);
            }
        }

        Collections.sort(answers);
        for (long x : answers) {
            w.println(x);
        }
    }

    /**
     * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
     * and System.out or from filenames specified on the command line, then call doIt.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader r;
            PrintWriter w;
            if (args.length == 0) {
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 1) {
                r = new BufferedReader(new FileReader(args[0]));
                w = new PrintWriter(System.out);
            } else {
                r = new BufferedReader(new FileReader(args[0]));
                w = new PrintWriter(new FileWriter(args[1]));
            }
            long start = System.nanoTime();
            doIt(r, w);
            w.flush();
            long stop = System.nanoTime();
            System.out.println("Execution time: " + 1e-9 * (stop - start));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
    }
}