package dsa.example;

import dsa.iface.IBinaryTree;
import dsa.iface.IEntry;
import dsa.iface.IPosition;
import dsa.iface.ISortedMap;
import dsa.impl.BinarySearchTreeMap;
import dsa.impl.SplayTreeMap;
import dsa.impl.TreePrinter;

/**
 * The class to test the structure of a Splay Tree with the expected outcome.
 * To test the key operations, get(), put(), and remove().
 * This test data is based on the Splay Tree example in 04b Lecture Notes by David Lillis.
 * <p>
 * - put(8): There is one node in the tree. So no splay.
 * - put(3): 2 Nodes in the tree. Need to Zig. 3 be the new root.
 * - put(10): 3 Nodes in the tree. Need to Zig-Zig. 10 be the new root.
 * - put(5): Need to Zig-Zag. Then Zig. 5 be the new root.
 * - put(2): Need to Zig-Zig. 2 be the new root.
 * - put(4): Need to Zig-Zag. Then Zig. 4 be the new root.
 * - get(11): 11 is not in the tree. So splay the parent of the ending external node(10).
 * Zig-Zig. 10 be the new root.
 * - remove(4): 4 has one internal child. Splay the parent of 4. Need to Zig. 5 be the new root.
 * - remove(5): 5 has two internal children. Splay the parent of actual node be removed(10). 10 be the new root.
 */
public class SplayTreeStructureTest {
    public static void main(String[] args) {
        int[] splayOrder = new int[]{8, 3, 10, 5, 2, 4};

        ISortedMap<Integer, Integer> m1 = new SplayTreeMap<>();

        for (int v : splayOrder) {
            m1.put(v, v);
        }
        System.out.println("Splay Tree after insertions:");
        IBinaryTree<IEntry<Integer, Integer>> t1 = (IBinaryTree<IEntry<Integer, Integer>>) m1;
        TreePrinter.printTree(t1);

        // Test Tree. From PPT 04b Lecture Notes.
        int[] BSTOrderT2 = new int[]{4, 2, 5, 3, 10, 8};
        ISortedMap<Integer, Integer> m2 = new BinarySearchTreeMap<>();
        for (int v : BSTOrderT2) {
            m2.put(v, v);
        }
        IBinaryTree<IEntry<Integer, Integer>> t2 = (IBinaryTree<IEntry<Integer, Integer>>) m2;
        System.out.println("Is the Splay Tree in the expected shape after Insertions? " + (areEqual(t1, t1.root(), t2, t2.root()) ? "YES! :-D" : "No! :-("));
        System.out.println();


        m1.get(11);
        System.out.println("Splay Tree after get(11):");
        IBinaryTree<IEntry<Integer, Integer>> t3 = (IBinaryTree<IEntry<Integer, Integer>>) m1;
        TreePrinter.printTree(t3);

        // Test Tree. From PPT 04b Lecture Notes.
        int[] BSTOrderT4 = new int[]{10, 5, 4, 8, 2, 3};
        ISortedMap<Integer, Integer> m4 = new BinarySearchTreeMap<>();
        for (int v : BSTOrderT4) {
            m4.put(v, v);
        }
        IBinaryTree<IEntry<Integer, Integer>> t4 = (IBinaryTree<IEntry<Integer, Integer>>) m4;
        System.out.println("Is the Splay Tree in the expected shape after get(11)? " + (areEqual(t3, t3.root(), t4, t4.root()) ? "YES! :-D" : "No! :-("));
        System.out.println();

        m1.remove(4);
        m1.remove(5);
        System.out.println("Splay Tree after remove(4) and remove(5):");
        IBinaryTree<IEntry<Integer, Integer>> t5 = (IBinaryTree<IEntry<Integer, Integer>>) m1;
        TreePrinter.printTree(t5);

        // Test Tree. From PPT 04b Lecture Notes.
        int[] BSTOrderT6 = new int[]{10, 8, 2, 3};
        ISortedMap<Integer, Integer> m6 = new BinarySearchTreeMap<>();
        for (int v : BSTOrderT6) {
            m6.put(v, v);
        }
        IBinaryTree<IEntry<Integer, Integer>> t6 = (IBinaryTree<IEntry<Integer, Integer>>) m6;
        System.out.println("Is the Splay Tree in the expected shape after remove(4) and remove(5)? " + (areEqual(t5, t5.root(), t6, t6.root()) ? "YES! :-D" : "No! :-("));


    }

    // check if two subtrees are equal (have the same shape and the same keys).
    // to check a whole tree, pass in the tree roots as the IPosition objects.
    private static <K extends Comparable<K>, V> boolean areEqual(IBinaryTree<IEntry<K, V>> t1, IPosition<IEntry<K, V>> p1, IBinaryTree<IEntry<K, V>> t2, IPosition<IEntry<K, V>> p2) {
        // they're both external nodes, so they are equal.
        if (t1.isExternal(p1) && t2.isExternal(p2))
            return true;
            // they are both internal, have the same element, and their left and right subtrees are also equal.
        else if (t1.isInternal(p1) && t2.isInternal(p2)) {
            return p1.element().key().equals(p2.element().key()) && areEqual(t1, t1.left(p1), t2, t2.left(p2)) && areEqual(t1, t1.right(p1), t2, t2.right(p2));
        }
        // one is internal and the other is external: not the same tree.
        else {
            return false;
        }
    }
}
