package dsa.example;

import dsa.iface.IBinaryTree;
import dsa.iface.IEntry;
import dsa.iface.IPosition;
import dsa.iface.ISortedMap;
import dsa.impl.AVLTreeMap;
import dsa.impl.BinarySearchTreeMap;
import dsa.impl.TreePrinter;

/**
 * Adjusted by David Lillis
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
 * Remove 6 values from the AVL Tree, which should cause 3 trinode restructurings:
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
 * @author David Lillis
 */
public class AVLTreeStructureTest {

   public static void main( String[] args ) {

      // I want to insert the following values into an AVL tree, in order.
      // For this test, the values (which don't affect the performance in any way)
      //   will be the same as the keys).
      int[] AVLOrder = new int[] { 30, 77, 54, 32, 41, 19, 15, 28, 31, 13, 25 };


      
      // Inserting them in this order into a Binary Search Tree (BST) should give me
      //   the same result as the final AVL tree (remember a BST does not restructure)
      //   ... so in this case, 32 will always be the root (since I don't remove anything)
      //   , 30 will be its left child, etc...
      int[] BSTOrder  = new int[] { 30, 19, 32, 15, 28, 31, 54, 13, 25, 41,77 };
      
      // create my two trees
      ISortedMap<Integer,Integer> m1 = new AVLTreeMap<>();
      ISortedMap<Integer,Integer> m2 = new BinarySearchTreeMap<>();

      // insert the values into the two trees 
      for ( int v : AVLOrder )
         m1.put( v, v );

      for ( int v : BSTOrder )
         m2.put( v, v );

      // treat them as trees
      IBinaryTree<IEntry<Integer,Integer>> t1 = (IBinaryTree<IEntry<Integer,Integer>>) m1;
      IBinaryTree<IEntry<Integer,Integer>> t2 = (IBinaryTree<IEntry<Integer,Integer>>) m2;

      System.out.println("AVL Tree after insertions:");
      TreePrinter.printTree( t1 );
      TreePrinter.printTree( t2 );
      System.out.println( "Is the AVL Tree in the expected shape after Insertions? " + ( areEqual( t1, t1.root(), t2, t2.root() ) ? "YES! :-D" : "No! :-(" ) );

      // remove some values from the AVL tree
      m1.remove( 1 );     // 1 is not in the tree, so nothing should happen
      m1.remove( 31 );    // 31 is a leaf. Need to right-left rotate at 32
      m1.remove( 28 );    // 28 has one Internal child. No rotation needed.
      m1.remove( 54 );    // 54 has tow Internal children. Need to left-right rotate at 77
      m1.remove( 32 );    // 32 is a leaf. No rotation needed.
      m1.remove( 77 );    // 77 is a leaf. Need to left-left rotate at 30(root).
      IBinaryTree<IEntry<Integer,Integer>> t3 = (IBinaryTree<IEntry<Integer,Integer>>) m1;
      System.out.println("AVL Tree after removing 1, 31, 28, 54, 32, 77:");
      TreePrinter.printTree( t3 );


   }

   // check if two subtrees are equal (have the same shape and the same keys).
   // to check a whole tree, pass in the tree roots as the IPosition objects.
   private static <K extends Comparable<K>,V> boolean areEqual(IBinaryTree<IEntry<K,V>> t1, IPosition<IEntry<K,V>> p1, IBinaryTree<IEntry<K,V>> t2, IPosition<IEntry<K,V>> p2 ) {
      // they're both external nodes, so they are equal.
      if ( t1.isExternal( p1 ) && t2.isExternal( p2 ) )
         return true;
      // they are both internal, have the same element, and their left and right subtrees are also equal.
      else if ( t1.isInternal( p1 ) && t2.isInternal( p2 ) ) {
         return p1.element().key().equals( p2.element().key() ) && areEqual( t1, t1.left( p1 ), t2, t2.left( p2 ) ) && areEqual( t1, t1.right( p1 ), t2, t2.right( p2 ) );
      }
      // one is internal and the other is external: not the same tree.
      else {
         return false;
      }
   }
}
