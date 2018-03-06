package main;

import java.util.ArrayList;


public class BNode{
	public ArrayList<Integer> keys;
	public ArrayList<BNode> children;
	public int numKeys;
	public BNode parent;
	
	public BNode(BNode parent) {
		this.parent = parent;
		numKeys = 0;
		keys = new ArrayList<Integer>(5);
		children = new ArrayList<BNode>(5);
	}
}