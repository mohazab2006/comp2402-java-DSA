package comp2402a3;

public class BSTNode<Node extends BSTNode<Node,T>,T>
		extends BTNode<Node> {
	/**
	 * The key stored at this node
	 */
	public T x;
}
