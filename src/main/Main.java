/**
 * CPSC 335 - Assignment 2 + 3
 * 
 * Wesley Tang - ID#30030487
 */
package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

	static BNode root;

	public static void main(String[] args) {
		// Get location of file with keys
		String url = "/" + args[0];

		BufferedReader in;
		InputStream is;

		// Initial values
		root = new BNode(null);
		int keysAdded = 0;

		// Read in file to generate the tree
		try {
			is = Main.class.getResourceAsStream(url);
			in = new BufferedReader(new InputStreamReader(is));

			String line = "";

			// Retrieves the next integer value and adds it to the true
			while ((line = in.readLine()) != null) {
				addToBTree(Integer.parseInt(line), root);
				keysAdded++;
			}

			in.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (NullPointerException npe) {
			System.out.println("The file '" + args[0] + "' was not found in the directory this program is in.");
			return;
		}

		System.out.println("ROOT:  " + root.keys.toString());

		// Get tree info
		System.out.println("Number of nodes: " + getNodeNum(root, 1));

		System.out.println("Number of keys: " + keysAdded);

		System.out.println("Depth of tree: " + getDepth(root, 1));

		// Searching for a given key entered by user
		System.out.println("Enter a key to search for: ");
		Scanner keyboard = new Scanner(System.in);
		int key = keyboard.nextInt();
		int answer = searchFor(key, root, 1, 0);
		System.out.println(answer == -1 ? "Key not found in B-tree"
				: "Key found at position " + answer + " in order of the B-Tree.");
		keyboard.close();
	}

	// Retrieves number of nodes
	private static int getNodeNum(BNode node, int numNode) {
		System.out.println(node.keys.toString() + " numNode so far: " + numNode);

		System.out.println("Children: ");
		for (BNode child : node.children)
			System.out.print(child.keys.toString() + "\t");

		// If at leaf, return num nodes + 1
		if (node.children.isEmpty())
			return numNode + 1;

		// Otherwise, recurse through each of the child nodes
		for (int i = 0; i < node.children.size(); i++) {
			if (node.children.get(i) != null)
				numNode = getNodeNum(node.children.get(i), numNode);
		}

		// Once each child node is recursed through, add this node to the count
		return numNode + 1;
	}

	// Retrieves depth by using the B-Tree property and descending down its left
	// child until it can no longer do so.
	private static int getDepth(BNode node, int depth) {
		if (!node.children.isEmpty()) {
			depth++;
			return getDepth(node.children.get(0), depth);
		}
		return depth;
	}

	/**
	 * Returns the order of a given key within a B-tree specified by a root node.
	 * 
	 * @param key
	 *            The key that is being searched for
	 * @param node
	 *            BNode from which searching is done
	 * @param order
	 *            What order the key that is being searched for is at
	 * @param pos
	 *            The current position of the keys or children to search.
	 * @return The order of the found key or -1 if it was not found
	 */
	private static int searchFor(int key, BNode node, int order, int pos) {
		System.out.println(node.keys.toString() + "\norder:" + order + "\npos: " + pos);
		// Keep moving to the leftmost node if possible before searching, continuing as
		// normal if the leftmost node fails to find the key
		int result;
		if (!node.children.isEmpty()) {
			result = searchFor(key, node.children.get(0), order, 0);
			if (result != -1)
				return result;
		}

		// If exhausted node's keys and no value has been found, return -1.
		if (pos >= node.keys.size())
			return -1;

		// If this key is the one that we are looking for, return order
		else if (node.keys.get(pos).equals(key))
			return order;

		else {
			// Increment order now that a new key is being evaluated
			order++;

			// If it is not the key, check the right child, if possible.
			if (node.children.get(pos + 1) != null) {
				int answer = searchFor(key, node.children.get(pos + 1), order, 0);
				// If the key is found, return it, otherwise, continue to the next key.
				if (answer != -1)
					return answer;
			}

			// Check the next key in the node
			return searchFor(key, node, order, pos + 1);
		}
	}

	// Add a key to a given BNode
	private static void addToBTree(int key, BNode node) {
		// System.out.println("\nAdding " + key + " to " + node.keys.toString() + "
		// numkeys: " + node.keys.size());
		// System.out.println("Children: ");
		// for (BNode child : node.children)
		// System.out.print(child.keys.toString() + "\t");
		// System.out.println();

		// If a node is empty, add a key
		if (node.keys.isEmpty()) {
			node.keys.add(key);
		} else {
			// If a node is a leaf, add the key to the node
			if (node.children.isEmpty()) {
				int i = -1;
				// Find the point where the key is no longer smaller than the keys in the node
				do {
					// System.out.println(i + ": " + key + " < " + node.keys.get(i));
					i++;

				} while ((i < node.keys.size() && key > node.keys.get(i)));
				node.keys.add(i, key);
			}
			// Otherwise, recursively travel through the nodes to find the right leaf
			else {
				// Find the index of the keys where the key to be added is first larger than
				int keyIndex = -1;
				do {
					keyIndex++;
				} while (keyIndex < node.keys.size() - 1 && key > node.keys.get(keyIndex));
				// Then add it in recursively depending on whether it should go into the left or right child
				if (key < node.keys.get(keyIndex))
					addToBTree(key, node.children.get(keyIndex));
				else
					addToBTree(key, node.children.get(keyIndex + 1));
			}

			// Once a node has become too large, split it
			if (node.keys.size() >= 5)
				node = split(node);
		}

	}

	/**
	 * Adds a node into a parent
	 * 
	 * @param node
	 *            to add
	 * @param parent
	 *            that was here originally
	 */
	private static void insertNode(BNode node, BNode parent) {
		// System.out.println("Inserting " + node.keys.toString() + " into " +
		// parent.keys.toString());
		// System.out.println("Children: ");
		// for (BNode child : node.children)
		// System.out.print(child.keys.toString() + "\t");

		// Find the index of the keys where the key to be added is first larger than
		int keyIndex = -1;
		do {
			keyIndex++;
		} while (keyIndex < node.keys.size() - 1 && parent.keys.get(keyIndex) > node.keys.get(0));

		// Add the value to the parent
		parent.keys.add(keyIndex, node.keys.get(0));

		// Remove the child from which the parent arose from
		// Add the new children to the parent
		parent.children.set(keyIndex, node.children.get(0));
		parent.children.add(keyIndex + 1, node.children.get(1));

		// Once a node has become too large, split it
		if (node.keys.size() >= 5)
			node = split(node);
	}

	// Split a node that has reached the max number of keys
	private static BNode split(BNode node) {
		// System.out.println("Splitting: " + node.keys.toString());

		// Creating new parent node with the middle key of the original
		BNode newParent = new BNode(node.parent);
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

		// Give the children to the respective node, if any.
		// We know that each one will have a child based off the BTree properties
		if (!node.children.isEmpty()) {
			leftChild.children.add(node.children.get(0));
			node.children.get(0).parent = leftChild;
			leftChild.children.add(node.children.get(1));
			node.children.get(1).parent = leftChild;
			leftChild.children.add(node.children.get(2));
			node.children.get(2).parent = leftChild;

			rightChild.children.add(node.children.get(3));
			node.children.get(3).parent = rightChild;
			rightChild.children.add(node.children.get(4));
			node.children.get(4).parent = rightChild;
			rightChild.children.add(node.children.get(5));
			node.children.get(5).parent = rightChild;
		}
		// Add new node to the previous parent, as long as it wasn't root
		if (node.parent != null)
			insertNode(newParent, node.parent);
		else {
			// System.out.println("NEW parent at: " + newParent.keys.toString());
			root = newParent;
		}

		return newParent;
	}
}