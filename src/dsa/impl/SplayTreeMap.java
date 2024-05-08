package dsa.impl;

import dsa.iface.ISortedMap;

/**
 * An implementation of a Splay Tree Map, a self-adjusting binary search tree.
 * <p>
 * This SplayTreeMap extends BinarySearchTreeMap and implements ISortedMap.
 * It maintains the balance of the tree to ensure efficient put, remove,
 * and get operations.
 *
 * @param <K> The type of keys in the map, must be Comparable.
 * @param <V> The type of values in the map.
 * @author Le Liu
 */
public class SplayTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K, V> implements ISortedMap<K, V> {

    public SplayTreeMap() {
        super();
    }

    /**
     * Get the value associated with the key k in the map.
     * Splay the accessed node to the root.
     * If the key is not in the map, splay the parent of ending external node.
     *
     * @param k The key whose associated value is to be retrieved
     * @return The value associated with the key k, or null if the key is not in the map.
     */
    @Override
    public V get(K k) {
        V value = super.get(k);
        if (value != null) {        // key is exist
            splayToRoot((BTPosition) find(root(), k));  // splay the accessed node to the root
        } else {                                        // key is not exist
            BTPosition splayNode = ((BTPosition) find(root(), k)).parent;
            // splay the parent of ending external node
            splayToRoot(splayNode);
        }
        return value;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * Splay the new node containing the key inserted.
     *
     * @param k The key with which the specified value is to be associated.
     * @param v The value to be associated with the specified key.
     * @return The previous value associated with the key k, or null if the key was not in the map.
     */
    @Override
    public V put(K k, V v) {
        V value = super.put(k, v);    // Insert the node. Same like the put method in BinarySearchTreeMap
        splayToRoot((BTPosition) find(root(), k));  // Splay the new node containing the key inserted.
        return value;
    }

    /**
     * Remove the key k and its associated value from the map.
     * Splay the parent of the internal node that was actually removed from the tree
     *
     * @param k The key whose mapping is to be removed from the map.
     * @return The value associated with the key k, or null if the key was not in the map.
     */
    @Override
    public V remove(K k) {
        V value;
        BTPosition findNode = ((BTPosition) find(root(), k));  // Find the node to remove

        if (!this.isInternal(findNode)) {    // Node to be removed doesn't exist
            BTPosition splayNode = findNode.parent;
            splayToRoot(splayNode);          // splay the parent of the ending external node
            return null;
        } else {
            if (isInternal(findNode.left) && isInternal(findNode.right)) {
                // Node to be removed has two Internal children
                BTPosition replacement;
                // Find the next bigger node after k.
                for (replacement = findNode.right; isInternal(replacement.left);
                     replacement = replacement.left) {
                }

                BTPosition splayNode = replacement.parent;  // splay the parent of the node(actual) to be removed
                value = super.remove(k);
                splayToRoot(splayNode);
            } else {                       // Node to be removed has at least one external child
                BTPosition splayNode = findNode.parent;  // splay the parent of the node(actual) to be removed
                value = super.remove(k);
                splayToRoot(splayNode);
            }
        }
        return value;
    }

    /**
     * Splay the node to the root.
     *
     * @param node The node to splay to the root.
     */
    private void splayToRoot(BTPosition node) {
        while (node != root) {
            splay(node);
        }
    }

    /**
     * A helper method to perform a splay operation.
     *
     * @param node The node to splay.
     */
    private void splay(BTPosition node) {
        BTPosition parent = node.parent;
        BTPosition grand = parent.parent;
        if (grand == null) {
            // zig
            if (node == parent.left) {
                rotateRight(parent);
            } else {
                rotateLeft(parent);
            }
        } else if (node == parent.left && parent == grand.left) {
            // zig-zig (left-left)
            rotateRight(grand);
            rotateRight(parent);
        } else if (node == parent.right && parent == grand.right) {
            // zig-zig (right-right)
            rotateLeft(grand);
            rotateLeft(parent);
        } else if (node == parent.right && parent == grand.left) {
            // zig-zag (left-right)
            rotateLeft(parent);
            rotateRight(grand);
        } else if (node == parent.left && parent == grand.right) {
            // zig-zag (right-left)
            rotateRight(parent);
            rotateLeft(grand);

        }
    }

    /**
     * Performs a left rotation around the given position.
     *
     * @param x The position around which to perform the rotation
     */
    private void rotateLeft(BTPosition x) {
        BTPosition y = x.right;

        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        if (x.parent == null) {
            this.root = y;
            y.parent = null;
        } else if (x == x.parent.left) {
            x.parent.left = y;
            y.parent = x.parent;
        } else {
            x.parent.right = y;
            y.parent = x.parent;
        }
        y.left = x;
        x.parent = y;
    }

    /**
     * Performs a right rotation around the given position.
     *
     * @param x The position around which to perform the rotation
     */
    private void rotateRight(BTPosition x) {
        BTPosition y = x.left;

        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }

        if (x.parent == null) {
            root = y;
            y.parent = null;
        } else if (x == x.parent.left) {
            x.parent.left = y;
            y.parent = x.parent;
        } else {
            x.parent.right = y;
            y.parent = x.parent;
        }
        y.right = x;
        x.parent = y;
    }
}
