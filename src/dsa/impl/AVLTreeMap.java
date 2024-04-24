package dsa.impl;

import dsa.iface.IEntry;
import dsa.iface.ISortedMap;

/**
 * An implementation of a self-balancing AVL tree-based map.
 * <p>
 * This AVLTreeMap extends BinarySearchTreeMap and implements ISortedMap.
 * It maintains the balance of the tree to ensure efficient put, remove,
 * and get operations.
 *
 * @param <K> The type of keys maintained by this map
 * @param <V> The type of mapped values
 * @author Le Liu
 */
public class AVLTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K, V> implements ISortedMap<K, V> {

    public AVLTreeMap() {
        super();
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param k The key whose associated value is to be retrieved
     * @return The value associated with the specified key, or null if the key is not found
     */
    @Override
    public V get(K k) {
        V value = super.get(k);  // There is no difference with the get method in BinarySearchTreeMap
        return value;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * Balances the tree after insertion if necessary.
     *
     * @param k The key with which the specified value is to be associated
     * @param v The value to be associated with the specified key
     * @return The previous value associated with the key k, or null if the key was not in the map.
     */
    @Override
    public V put(K k, V v) {
        V value = super.put(k, v);  // Insert the node. Same like the put method in BinarySearchTreeMap

        // Adjust the height of nodes after insertion and rebalance if necessary
        AVLPosition insertedPosition = (AVLPosition) super.find(root(), k);
        checkAndBalanceUpwards(insertedPosition);
        return value;
    }

    /**
     * Remove the key k and its associated value from the map.
     * Balances the tree after deletion if necessary.
     *
     * @param k The key whose mapping is to be removed from the map
     * @return The value associated with the key k, or null if the key was not in the map.
     */
    @Override
    public V remove(K k) {
        AVLPosition current = (AVLPosition) super.find(root(), k);  // Find the node to be removed

        if (!isInternal(current)) {                                 // Node to be removed doesn't exist
            return null;
        }

        AVLPosition beginNode;                                // Begin isBalance the check from this node.
                                                             // It is the parent of the node(actual) to be removed

        if (isInternal(current.left) && isInternal(current.right)) {  // Node to be removed has two Internal children
            AVLPosition replacement;                                  // Actual node to be removed
            // Find the next bigger node after k.
            for (replacement = (AVLPosition) current.right; isInternal(replacement.left);
                 replacement = (AVLPosition) replacement.left) {
            }

            beginNode = (AVLPosition) replacement.parent;
        } else {                                                    // Node to be removed has at least one External child
            beginNode = (AVLPosition) current.parent;
        }

        V value = super.remove(k);                // Remove node with key k

        checkAndBalanceUpwards(beginNode);      // Adjust the height of nodes after deletion and rebalance if necessary
        return value;

    }


    /**
     * Checks and balances the tree upwards from the given position.
     *
     * @param position The position from which to start checking and balancing
     */
    public void checkAndBalanceUpwards(AVLPosition position) {
        while (position != null) {
            position.updateHeight();       // Update the height of the current position
            if (!isBalanced(position)) {   // Update the height of the current position
                restructure(position);     // If not balanced, restructure the tree
            }
            position = (AVLPosition) position.parent;
        }
    }

    /**
     * Performs a left rotation around the given position.
     *
     * @param x The position around which to perform the rotation
     */
    public void rotateLeft(AVLPosition x) {
        AVLPosition y = (AVLPosition) x.right;

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

        x.updateHeight();
        y.updateHeight();

    }

    /**
     * Performs a right rotation around the given position.
     *
     * @param x The position around which to perform the rotation
     */
    public void rotateRight(AVLPosition x) {
        AVLPosition y = (AVLPosition) x.left;

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

        x.updateHeight();
        y.updateHeight();

    }

    /**
     * Restores balance at the given unbalanced position by performing rotations.
     *
     * @param unbalanced The unbalanced position to restructure
     */
    public void restructure(AVLPosition unbalanced) {
        AVLPosition x, y, z;
        x = unbalanced;

        // Determine the type of imbalance and the positions x, y, and z
        boolean isLeftHeavy = ((AVLPosition) x.left).height > ((AVLPosition) x.right).height;
        y = isLeftHeavy ? (AVLPosition) x.left : (AVLPosition) x.right;

        // if y has children of equal heights, we choose the side
        //that will result in a single rotation
        if(((AVLPosition) y.left).height == ((AVLPosition) y.right).height) {
            z = isLeftHeavy ? (AVLPosition) y.left : (AVLPosition) y.right;
        } else {
            isLeftHeavy = ((AVLPosition) y.left).height > ((AVLPosition) y.right).height;
            z = isLeftHeavy ? (AVLPosition) y.left : (AVLPosition) y.right;
        }

        // Perform rotations based on the type of imbalance
        if (x.left == y && y.left == z) {  // left-left
            rotateRight(x);
        }
        if (x.left == y && y.right == z) {  // left-right
            rotateLeft(y);
            rotateRight(x);
        }
        if (x.right == y && y.right == z) {  // right-right
            rotateLeft(x);
        }
        if (x.right == y && y.left == z) {  // right-left
            rotateRight(y);
            rotateLeft(x);
        }

    }

    /**
     * Checks if the subtree rooted at the given position is balanced.
     *
     * @param position The root position of the subtree to check
     * @return True if the subtree is balanced, false otherwise
     */
    public boolean isBalanced(AVLPosition position) {
        if (position == null) {
            return true;
        }

        // Calculate heights of left and right subtrees
        int leftHeight = ((AVLPosition) position.left).height;
        int rightHeight = ((AVLPosition) position.right).height;

        // Check if the difference in heights is within the acceptable range
        return Math.abs(leftHeight - rightHeight) <= 1;
    }


    /**
     * Define a subclass of BTPosition so that we can also store the height
     * of each position in its object.
     * <p>
     * This will be more efficient than calculating the height every time
     * we need it, but we will need to update heights whenever we change
     * the structure of the tree.
     */
    class AVLPosition extends AbstractBinaryTree<IEntry<K, V>>.BTPosition {
        // store the height of this position, so that we can test for balance
        public int height = 0;

        /**
         * Constructor - create a new AVL node
         *
         * @param element The element to store in the node.
         * @param parent  The parent node of this node (or {@code null} if this is the root)
         */
        AVLPosition(IEntry<K, V> element, AbstractBinaryTree.BTPosition parent) {
            super(element, parent);
        }

        public void updateHeight() {
            int leftHeight = (this.left == null) ? -1 : ((AVLPosition) this.left).height;
            int rightHeight = (this.right == null) ? -1 : ((AVLPosition) this.right).height;
            this.height = 1 + Math.max(leftHeight, rightHeight);
        }
    }

    @Override
    protected AbstractBinaryTree<IEntry<K, V>>.BTPosition newPosition(IEntry<K, V> element, AbstractBinaryTree<IEntry<K, V>>.BTPosition parent) {
        return new AVLPosition(element, parent);
    }
}
