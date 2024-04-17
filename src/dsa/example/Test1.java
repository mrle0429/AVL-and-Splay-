package dsa.example;

import dsa.iface.IBinaryTree;
import dsa.iface.IEntry;
import dsa.iface.ISortedMap;
import dsa.impl.AVLTreeMap;
import dsa.impl.TreePrinter;

/**
 * Simple class to compare the structure of an AVL tree with the
 *    expected outcome.
 *
 * Inserts 11 values into an AVL Tree, which should cause 5 trinode restructurings:
 * - 1 double rotation (right-left) at the root when 54 is inserted.
 * - 1 single rotation (right-right) when 41 is inserted.
 * - 1 single rotation (left-left) at the root when 19 is inserted.
 * - 1 single rotation (left-left) at 30 when 15 is inserted.
 * - 1 double rotation (left-right) at the root when 25 is inserted.
 *
 * Remove 5 values from the AVL Tree, which should cause 3 trinode restructurings:
 * - 1 double rotation (right-left) at the 32 when 31 is removed.
 * - no rotation when 28 is removed.
 * - 1 double rotation (left-right) at the 77 when 54 is removed.
 * - no rotation when 32 is removed.
 * - 1 single rotation (left-left) at the root when 77 is removed.
 *
 * A Binary Search Tree is constructed that has the expected final shape for the AVL tree.
 *
 * Result: AVL tree is the correct shape.
 *
 * @author Le Liu
 */

public class Test1 {

    public static void main(String[] args) {
        // 创建 AVL 树

        int[] AVLOrder = new int[] { 30, 77, 54, 32, 41, 19, 15, 28, 31, 13, 25 };



        ISortedMap<Integer,Integer> m1 = new AVLTreeMap<>();
        for ( int v : AVLOrder )
            m1.put( v, v );


        IBinaryTree<IEntry<Integer,Integer>> t1 = (IBinaryTree<IEntry<Integer,Integer>>) m1;

        System.out.println("AVL Tree after insertions:");
        TreePrinter.printTree(t1);

        m1.remove(31); // 31两个节点均为外部节点，删除后执行右左旋转
        System.out.println("AVL Tree after removing 31:");
        IBinaryTree<IEntry<Integer,Integer>> t2 = (IBinaryTree<IEntry<Integer,Integer>>) m1;
        TreePrinter.printTree(t2);

        m1.remove(28);  // 28有一个节点是外部节点，删除后无需旋转
        m1.remove(54); // 54有一个节点是外部节点，删除后执行左右旋转
        m1.remove(32);
        m1.remove(77); // 移除后执行左左旋转
        System.out.println("AVL Tree after removing 28, 54, 32, 77:");
        IBinaryTree<IEntry<Integer,Integer>> t3 = (IBinaryTree<IEntry<Integer,Integer>>) m1;
        TreePrinter.printTree(t3);

    }
}
