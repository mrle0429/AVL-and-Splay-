package dsa.example;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dsa.iface.ISortedMap;
import dsa.impl.SplayTreeMap;

public class SplayTreeSpeedTest {
    public static void main(String[] args) {
        int range = 100000;

        Random r = new Random(0);

        System.out.println("Values: " + range);

        // get a list of 'range' values.
        List<Integer> values = IntStream.range(0, range).boxed().collect(Collectors.toList());

        // shuffle the list into random order
        Collections.shuffle(values, r);

        // create the Splay tree
        ISortedMap<Integer, String> t = new SplayTreeMap<>();

        // record the time I started inserting at
        long start = System.currentTimeMillis();

        // insert all values into the tree
        for (Integer v : values) {
            t.put(v, String.valueOf(v));
        }

        // record the time I stopped inserting at
        long end = System.currentTimeMillis();

        // output the time for inserting
        System.out.println("Inserting: " + (end - start));

        // shuffle the list again (I should not check this in the same order as I inserted everything)
        Collections.shuffle(values, r);

        // record the time I started checking contains(...) at
        start = System.currentTimeMillis();

        // find each value in the tree
        for (int v : values) {
            t.get(v);
        }

        // record the end time and print the time taken for contains
        end = System.currentTimeMillis();
        System.out.println("Contains: " + (end - start));

        // shuffle one more time
        Collections.shuffle(values, r);

        // record the start again
        start = System.currentTimeMillis();

        // get 10% of the values in this list and remove from the tree
        List<Integer> valuesToRemove = values.subList(0, values.size() / 10);
        for (int v : valuesToRemove) {
            t.remove(v);
        }

        // print the last time
        end = System.currentTimeMillis();
        System.out.println("Removing: " + (end - start));
    }
}
