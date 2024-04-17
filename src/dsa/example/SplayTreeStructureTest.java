package dsa.example;

import dsa.iface.IBinaryTree;
import dsa.iface.IEntry;
import dsa.iface.ISortedMap;
import dsa.impl.SplayTreeMap;
import dsa.impl.TreePrinter;

public class SplayTreeStructureTest {
    public static void main(String[] args) {
        int[] splayOrder = new int[] { 8, 3, 10, 5, 2, 4};

        ISortedMap<Integer, Integer> m1 = new SplayTreeMap<>();

        for (int v : splayOrder) {
            m1.put(v, v);
        }
//        m1.get(11);
//        m1.remove(4);
//        m1.remove(5);

        IBinaryTree<IEntry<Integer, Integer>> t1 = (IBinaryTree<IEntry<Integer, Integer>>) m1;

        TreePrinter.printTree(t1);

    }
}
