package comp2402a3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class BinaryTree<Node extends BTNode<Node>> {
	/**
	 * An extension of BTNode that you can actually instantiate.
	 */
	protected static class EndNode extends BTNode<EndNode> {
		public EndNode() {
			this.parent = this.left = this.right = null;
		}
	}
	/**
	 * Used to make a mini-factory
	 */
	public Node sampleNode;

	/**
	 * The root of this tree
	 */
	public Node r;

	/**
	 * This tree's null node
	 */
	public Node nil;

	/**
	 * Create a new instance of this class
	 * @param isampleNode
	 */
	public BinaryTree(Node nil) {
		sampleNode = nil;
		this.nil = null;
	}

	/**
	 * Create a new instance of this class
	 * @warning child must set sampleNode before anything that
	 * might make calls to newNode()
	 */
	public BinaryTree() { }

	/**
	 * Allocate a new node for use in this tree
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	public Node newNode() {
		try {
			Node u = (Node)sampleNode.getClass().getDeclaredConstructor().newInstance();
			u.parent = u.left = u.right = nil;
			return u;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Construct a random binary tree
	 * @return an n-node BinaryTree that has the shape of a random
	 * binary search tree.
	 */
	public static BinaryTree<EndNode> randomBT(int n) {
		Random rand = new Random();
		return randomBT(n, rand);
	}

	public static BinaryTree<EndNode> randomBT(int n, Random rand) {
		EndNode sample = new EndNode();
		BinaryTree<EndNode> t = new BinaryTree<EndNode>(sample);
		t.r = randomBTHelper(n, rand);
		return t;
	}

	protected static EndNode randomBTHelper(int n, Random rand) {
		if (n == 0) {
			return null;
		}
		EndNode r = new EndNode();
		int ml = rand.nextInt(n);
		int mr = n - ml - 1;
		if (ml > 0) {
			r.left = randomBTHelper(ml, rand);
			r.left.parent = r;
		}
		if (mr > 0) {
			r.right = randomBTHelper(mr, rand);
			r.right.parent = r;
		}
		return r;
	}

	public int depth(Node u) {
		int d = 0;
		while (u != r) {
			u = u.parent;
			d++;
		}
		return d;
	}

	/**
	 * Compute the size (number of nodes) of this tree
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return size(r);
	}

	/**
	 * @return the size of the subtree rooted at u
	 */
	public int size(Node u) {
		if (u == nil)
			return 0;
		return 1 + size(u.left) + size(u.right);
	}

	public int height() {
		return height(r);
	}

	/**
	 * @return the size of the subtree rooted at u
	 */
	public int height(Node u) {
		if (u == nil)
			return -1;
		return 1 + Math.max(height(u.left), height(u.right));
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return r == nil;
	}

	/**
	 * Make this tree into the empty tree
	 */
	public void clear() {
		r = nil;
	}

	public void traverse(Node u) {
		if (u == nil) return;
		traverse(u.left);
		traverse(u.right);
	}

	public void traverse2() {
		Node u = r, prev = nil, next;
		while (u != nil) {
			if (prev == u.parent) {
				if (u.left != nil) next = u.left;
				else if (u.right != nil) next = u.right;
				else next = u.parent;
			} else if (prev == u.left) {
				if (u.right != nil) next = u.right;
				else next = u.parent;
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
	}

	public int size2() {
		Node u = r, prev = nil, next;
		int n = 0;
		while (u != nil) {
			if (prev == u.parent) {
				n++;
				if (u.left != nil) next = u.left;
				else if (u.right != nil) next = u.right;
				else next = u.parent;
			} else if (prev == u.left) {
				if (u.right != nil) next = u.right;
				else next = u.parent;
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
		return n;
	}

	/**
	 * Find the first node in an in-order traversal
	 * @return
	 */
	public Node firstInNode() {
		Node w = r;
		if (w == nil) return nil;
		while (w.left != nil)
			w = w.left;
		return w;
	}

	/**
	 * Find the node that follows u in an in-order traversal
	 * @param w
	 * @return
	 */
	public Node nextInNode(Node w) {
		if (w.right != nil) {
			w = w.right;
			while (w.left != nil)
				w = w.left;
		} else {
			while (w.parent != nil && w.parent.left != w)
				w = w.parent;
			w = w.parent;
		}
		return w;
	}

	public void bfTraverse() {
		Queue<Node> q = new LinkedList<Node>();
		q.add(r);
		while (!q.isEmpty()) {
			Node u = q.remove();
			if (u.left != nil) q.add(u.left);
			if (u.right != nil) q.add(u.right);
		}
	}

	public int smallestLeaf() {
		// TODO: Your code goes here, must avoid recursion
		if(r == nil)return -1;
		return smallestLeafHelper(r, 0);
	}

	protected int smallestLeafHelper(Node u, int d) {
		if(u.left == nil && u.right == nil)
			return d;
		else if(u.left == nil)
			return smallestLeafHelper(u.right, d+1);
		else if(u.right == nil)
			return smallestLeafHelper(u.left, d+1);
		else
			return Math.min(smallestLeafHelper(u.left, d+1), smallestLeafHelper(u.right, d+1));
	}

	public int maxWidth() {
		// TODO: Your code goes here, must avoid recursion
		int h = height(), max_width = 0;
		for(int i=0; i<=h; i++){
			int width = maxWidthHelper(r, 0, i);
			if(width > max_width)
				max_width = width;
		}
		return max_width;
	}

	protected int maxWidthHelper(Node u, int d, int D) {
		if(u == nil) return 0;
		if(d == D)return 1;
		return maxWidthHelper(u.left, d+1, D) + maxWidthHelper(u.right, d+1, D);
	}
}
