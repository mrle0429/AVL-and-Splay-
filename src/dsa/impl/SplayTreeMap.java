package dsa.impl;

import dsa.iface.ISortedMap;

public class SplayTreeMap<K extends Comparable<K>, V> extends BinarySearchTreeMap<K, V> implements ISortedMap<K, V> {

    public SplayTreeMap() {
        super();
    }

    @Override
    public V get(K k) {
        V value = super.get(k);
        if (value != null) {
            while (root() != null && root().element().key() != k) {
                splay((BTPosition) find(root(), k));
            }
        } else {
            BTPosition find = ((BTPosition) find(root(), k)).parent;
            k = find.element().key();
            while (root() != null && root().element().key() != k) {
                splay((BTPosition) find(root(), k));
            }
        }
        return value;
    }

    @Override
    public V put(K k, V v) {
        V value = super.put(k, v);
        splayToRoot((BTPosition) find(root(), k));
        return value;
    }

    @Override
    public V remove(K k) {
        BTPosition findNode = ((BTPosition) find(root(), k));
        if (!this.isInternal(findNode)) {
            return null;
        } else {
            if (isInternal(findNode.left) && isInternal(findNode.right)) {
                BTPosition replacement;
                for (replacement = findNode.right; isInternal(replacement.left); replacement = replacement.left) {

                }
                BTPosition splayNode = replacement.parent;
                while (root().element().key() != splayNode.element().key()) {
                    splay((BTPosition) find(root(), splayNode.element().key()));
                }
            } else {
                BTPosition splayNode = findNode.parent;
                while (root().element().key() != splayNode.element().key()) {
                    splay((BTPosition) find(root(), splayNode.element().key()));
                }
            }

        }
        V value = super.remove(k);
        return value;
    }

    private void splayToRoot(BTPosition node) {
        while (node != root) {
            splay(node);
        }
    }

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
            // zig-zig
            rotateRight(grand);
            rotateRight(parent);
        } else if (node == parent.right && parent == grand.right) {
            // zig-zig
            rotateLeft(grand);
            rotateLeft(parent);
        } else if (node == parent.right && parent == grand.left) {
            // zig-zag
            rotateLeft(parent);
            rotateRight(grand);
        } else if (node == parent.left && parent == grand.right) {
            // zig-zag
            rotateRight(parent);
            rotateLeft(grand);

        }
    }

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
