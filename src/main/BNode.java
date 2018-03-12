package main;

import java.util.ArrayList;

/**
 * A B-Tree node
 */
public class BNode {
	// List of keys
	public ArrayList<Integer> keys;
	// List of pointers to this node's children
	public ArrayList<BNode> children;
	// This node's parent
	public BNode parent;
	// This node's depth
	public int depth;

	/**
	 * Create a new B-tree node
	 * 
	 * @param parent
	 */
	public BNode(BNode parent) {
		this.parent = parent;
		keys = new ArrayList<Integer>(5);
		children = new ArrayList<BNode>(5);
	}
}