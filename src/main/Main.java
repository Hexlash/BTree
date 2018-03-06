package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		String url = args[0];

		BufferedReader in;
		InputStream is;

		BNode root = new BNode(null);

		try {
			is = Main.class.getResourceAsStream(url);
			in = new BufferedReader(new InputStreamReader(is));

			String line = ""; // Contains the current line of text

			// Retrieves the next integer value and adds it to the true
			while ((line = in.readLine()) != null)
				addToBTree(Integer.parseInt(line), root);

			in.close();

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

	}

	// Add to B Tree a node isntead of k so can recurse
	
	private static void addToBTree(int key, BNode node) {
		// If a node is empty, add a key
		if (node.numKeys == 0)
			node.keys.add(key);
		else {
			// If a node has no children, add the key to the node
			if (node.children.isEmpty())
				for (int i = 0; node.keys.get(i) > key; i++) {
					if (key > node.keys.get(i)) {
						node.keys.add(key, i);
						node.numKeys++;
					}
				}
			// Otherwise, recursively travel to through the nodes
			else

			// Once a node has become too large, split it
			if (node.numKeys == 5)
				split(node);
		}

	}
	
	private void insertNode(BNode node, BNode parent) {
		
	}

	// Split a node that has reached the max
	private static void split(BNode node) {
		// If the node to split has no parent
		if (node.parent != null) {
			// Replace current parent node with a new parent
			BNode newParent = new BNode(node.parent);
			for (BNode child : node.parent.children)
				if (child == node)
					child = newParent;
			newParent.keys.add(node.keys.get(2));

			// Give the lower half of the keys to the new left child
			BNode leftChild = new BNode(newParent);
			leftChild.keys.add(node.keys.get(0));
			leftChild.keys.add(node.keys.get(1));
			newParent.children.add(leftChild);

			// Give the upper half of the keys to the new right child
			BNode rightChild = new BNode(newParent);
			rightChild.keys.add(node.keys.get(3));
			rightChild.keys.add(node.keys.get(4));
			newParent.children.add(rightChild);
		}
		else {
			// TODO add previous key to parent while handling new children if it gets too big
			insertNode
			
		}
	}

}
