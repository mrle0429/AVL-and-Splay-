package dsa.impl;

import dsa.iface.IEntry;
import dsa.iface.ISortedMap;

public class AVLTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K, V> implements ISortedMap<K, V> {

    public AVLTreeMap() {
        super();
    }

    @Override
    public V get(K k) {
        V value = super.get(k);
        return value;
    }

    @Override
    public V put(K k, V v) {
        V value = super.put(k, v);
        AVLPosition current = (AVLPosition) super.find(root(), k);
        // 插入节点后更改节点的高度
        while (current != null) {
            current.updateHeight();
            current = (AVLPosition) current.parent;
        }
        // 找不平衡节点
        AVLPosition unbalanced = findUnbalancedNode((AVLPosition) super.find(root(), k));

        // 没有找到不平衡的节点
        if (unbalanced == null) {
            return value;
        } else {  // 找到不平衡节点
            restructure(unbalanced);
        }
        return value;
    }


    @Override
    public V remove(K k) {
        AVLPosition current = (AVLPosition) super.find(root(), k);
        current = (AVLPosition) current.parent;
        V value = super.remove(k);


        while (current != null) {
            if (!isBalanced(current)) {
                restructure(current);
            }
            current = (AVLPosition) current.parent;
        }


        return value; // <-- REMOVE: this is here just so that the class will compile.
    }

    public void restructure(AVLPosition unbalanced) {
        AVLPosition X, Y, Z;
        X = unbalanced;
        boolean isLeftHeavy = ((AVLPosition) unbalanced.left).height > ((AVLPosition) unbalanced.right).height;
        if (isLeftHeavy) {
            Y = (AVLPosition) unbalanced.left;
            Z = (((AVLPosition) Y.left).height) > (((AVLPosition) Y.right).height) ? (AVLPosition) Y.left : (AVLPosition) Y.right;
            if (X.left == Y && Y.left == Z) {
                rotateRight(X);
            }
            if (X.left == Y && Y.right == Z) {
                rotateLeft(Y);
                rotateRight(X);
            }

        } else {
            Y = (AVLPosition) unbalanced.right;
            Z = (height((AVLPosition) Y.left) > height((AVLPosition) Y.right)) ? (AVLPosition) Y.left : (AVLPosition) Y.right;
            if (X.right == Y && Y.right == Z) {
                rotateLeft(X);
            }
            if (X.right == Y && Y.left == Z) {
                rotateRight(Y);
                rotateLeft(X);
            }
        }
    }

    public void singleRotate(AVLPosition x) {
        AVLPosition y = (AVLPosition) x.parent;
        AVLPosition z = (AVLPosition) y.parent;

        // 根据x, y, z的键值顺序确定旋转方向
        if (x == y.left && y == z.left) {
            rotateRight(z);
        } else if (x == y.right && y == z.right) {
            rotateLeft(z);
        }
    }

    public void doubleRotate(AVLPosition x) {
        AVLPosition y = (AVLPosition) x.parent;
        AVLPosition z = (AVLPosition) y.parent;

        // 根据x, y, z的键值顺序确定旋转方向
        if (x == y.left && y == z.right) {
            rotateRight(y);
            rotateLeft(z);
        } else if (x == y.right && y == z.left) {
            rotateLeft(y);
            rotateRight(z);
        }
    }

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


    // YOU SHOULD NEED TO ENTER ANYTHING BELOW THIS

    public boolean isBalanced(AVLPosition position) {
        if (position == null) {
            return true;
        }

        int leftHeight = ((AVLPosition) position.left).height;
        int rightHeight = ((AVLPosition) position.right).height;

        return Math.abs(leftHeight - rightHeight) <= 1;
    }

    public AVLPosition findUnbalancedNode(AVLPosition position) {
        if (position == null) {
            return null;
        }

        if (!isBalanced(position)) {
            return position;
        }

        return findUnbalancedNode((AVLPosition) position.parent);

    }

    private int height(AVLPosition position) {
        if (position == null) {
            return -1;
        }
        return position.height;
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
