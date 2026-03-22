package comp2402a3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class A3Tree extends
BinarySearchTree<A3Tree.Node, Integer> {
	public static class Node extends BSTNode<Node,Integer> {
        public Point2D position;
	
        public Node() {
            position = new Point2D();
        }
        
        public String toString() {
            return position.toString();
        }
	}

	public A3Tree() {
		sampleNode = new Node();
		c = new DefaultComparator<Integer>();
	}

    protected void assignLevels() {
		assignLevels(r, 0);
	}
	
	protected void assignLevels(Node u, int i) {
		if (u == null) return;
		u.position.y = i;
		assignLevels(u.left, i+1);
		assignLevels(u.right, i+1);
	}

    public void inorderDraw() {
		assignLevels();
		Node node = firstInNode();
		int index = 0;
		while(node!= nil){
			node.position.x = index++;
			node = nextInNode(node);
		}
	}

    public void addAll(List<Integer> list) {
        for (Integer x : list) {
            add(x);
        }
    }

    public void printPreOrderRecursive(Node u) {
        if (u == nil) {
            return;
        }
        System.out.print(u.x+", ");
        printPreOrderRecursive(u.left);
        printPreOrderRecursive(u.right);
    }

    public void printPreOrderRecursive(){
        printPreOrderRecursive(r);
        System.out.println();
    }

    public void printPostOrderRecursive(){
        printPostOrderRecursive(r);
        System.out.println();
    }

    public void printPostOrderRecursive(Node u) {
        if (u == nil) {
            return;
        }
        printPostOrderRecursive(u.left);
        printPostOrderRecursive(u.right);
        System.out.print(u.x+", ");
    }

    public int maxSum(){
    	//TODO: Your code goes here, must avoid recursion
        return maxSumHelper(r);
    }

	protected int maxSumHelper(Node u) {
		if(u == nil) return 0;
		return u.x + Math.max(maxSumHelper(u.left), maxSumHelper(u.right));
	}
}
