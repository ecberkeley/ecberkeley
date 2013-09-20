package org.ecberkeley.css.collections;

import org.ecberkeley.css.collections.samplebeans.BinaryTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class InterviewTest {
    public List<String> intersect(List<String> listA, List<String> listB) {
        List<String> result = new ArrayList<String>();
        Set<String> set = new HashSet<String>();
        set.addAll(listA);
        for (String s : listB) {
            if (set.contains(s)) {
                result.add(s);
            }
        }
        return result;
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    private List<String> populate(int size) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            result.add(randomString(16));
        }
        return result;
    }

    static class TwoLists {
        List<String> listA;
        List<String> listB;
    }

    public TwoLists setupLists(int SIZE, int NUM_SHARED) {
        TwoLists two = new TwoLists();
        two.listA = populate(SIZE);
        two.listB = populate(SIZE);

        Random rndPos = new Random();
        for (int i = 0; i < NUM_SHARED; i++) {
            int j = rndPos.nextInt(SIZE);
            int k = rndPos.nextInt(SIZE);
            String s = two.listA.get(j);
            two.listB.add(k, s);
        }
        return two;
    }

    public void testSort(int SIZE, int NUM_SHARED) {
        TwoLists two = setupLists(SIZE, NUM_SHARED);

        long start = System.currentTimeMillis();
        Collections.sort(two.listA);
        Collections.sort(two.listB);
        List<String> res = marchingCompare(two.listA, two.listB);

        long end = System.currentTimeMillis();
        System.out.println("SORTING.   SIZE: " + SIZE + " time: " + (end - start) + " size: " + res.size());
    }

    public void testBinaryTree(int SIZE, int NUM_SHARED) {
        TwoLists two = setupLists(SIZE, NUM_SHARED);

        long start = System.currentTimeMillis();

        BinaryTree btA = new BinaryTree("dddd");
        //BinaryTree btB = new BinaryTree("dddd");
        for (String s : two.listA) {
            btA.add(s);
        }
        long mid = System.currentTimeMillis();
        System.out.println("BinaryTree.   SIZE: " + SIZE + " time to populate: " + (mid - start) );
        List<String> result = new ArrayList<String>();
        for (String s: two.listB){
            if (btA.search(s)!=null){
                result.add(s);
            }
        }
        btA.printTree();
        //System.out.println("search: " + btA.search("ccccc"));
        long end = System.currentTimeMillis();
        System.out.println("BinaryTree.   SIZE: " + SIZE + " time: " + (end - start) + " size: " + result.size());
    }

    private List<String> marchingCompare(List<String> listA, List<String> listB) {
        List<String> result = new ArrayList<String>();

        int b = 0;
        int c = 0;
        String sb;
        int size = listB.size();

        for (String s : listA) {
            sb = listB.get(b);
            c = s.compareTo(sb);

            if (c == 0) {
                result.add(s);
            } else {
                while (true) {
                    if (c == 0) {
                        result.add(s);
                        b++;   //bump past this one in b list, too.
                        if (b >= size) {
                            return result;
                        }
                        break;
                    } else if (c < 0) {    // s < sb, means go get next x
                        break;
                    } else if (c > 0) {    // s > sb, means bump to next sb and try again.
                        b++;
                        if (b >= size) {
                            return result;
                        }
                        sb = listB.get(b);
                        c = s.compareTo(sb);
                        continue;
                    }
                }
            }
        }
        return result;
    }

    public void test(int SIZE, int NUM_SHARED) {
        TwoLists two = setupLists(SIZE, NUM_SHARED);

        long start = System.currentTimeMillis();
        List<String> result = intersect(two.listA, two.listB);
        long end = System.currentTimeMillis();
        System.out.println("result list: " + result);
        System.out.println("SIZE: " + SIZE + " time: " + (end - start) + " result list size: " + result.size());
    }

    public static void main(String[] args) {
        int SIZE = 100;
        int NUM_SHARED = 40;
        //(new InterviewTest()).test(SIZE, NUM_SHARED);
       // (new InterviewTest()).testSort(SIZE, NUM_SHARED);
        (new InterviewTest()).testBinaryTree(SIZE, NUM_SHARED);
    }

}
