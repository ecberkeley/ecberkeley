package org.ecberkeley.css.generics;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GetPutCollections {

    //============ Test Class Hierarchy =========================
    static class Fruit {
        public static int g_idx = 0;
        public int id = g_idx++;

        public String toString() {
            return "Fruit-" + (id);
        }
    }

    static class Orange extends Fruit {
        public String toString() {
            return "Orange-" + (id);
        }
    }
    //=====================================================

    public static void print(String msg) {
        System.out.println(msg);
    }

    public static <U> void test() {
    }

    static <T> void printAllGenericWildcard(List<? extends T> c) {
        print("printAllGeneric: class of c: "+c.getClass().getCanonicalName());
        for (T el : c) {
            print("? extends T: " + el);
        }
    }

    static <T> void printAllGeneric(List<T> c) {
        print("printAllGeneric: class of c: "+c.getClass().getCanonicalName());
        for (T el : c) {
            print("T: " + el);
        }
    }

    static void printAllString(List<String> c) {
         c.add("added from printAllString");
        for (String el : c) {
            print("String: " + el);
        }
    }

    static void printAll(List c) {
         c.add("added from printAll");
        for (Object el : c) {
            print("Raw: " + el);
        }
    }

    static void wideningTest() {
        print("====WideningTest");
        List<String> l = new LinkedList<String>();
        l.add("s1");
        l.add("s2");

        printAll(l);

        print("====WideningTest with String");
        printAllString(l);

        print("====WideningTest with Generic");
        printAllGeneric(l);

        print("====WideningTest with GenericWildcard");
        printAllGenericWildcard(l);


        List<Fruit> fruitList = new LinkedList<Fruit>();
        fruitList.add(new Fruit());
        fruitList.add(new Fruit());
        fruitList.add(new Orange());
        fruitList.add(new Orange());
        fruitList.add(new Orange());
        print("====WideningTest with raw List of Fruit");
        printAll(fruitList);
        print("====WideningTest with Generic List of Fruit");
        printAllGeneric(fruitList);
        print("====WideningTest with GenericWildcard List of Fruit");
        printAllGenericWildcard(fruitList);

        //Can't do this:
         //    List<? extends Fruit> fruitListWild = new LinkedList<Fruit>();
        // this line fails:
        //    fruitListWild.add(new Fruit());

        List<? super Fruit> fruitListWild = new LinkedList<Fruit>();
        fruitListWild.add(new Fruit());
        fruitListWild.add(new Orange());
        print("fruitListWild.get(0) "+fruitListWild.get(0));
    }

    static void wildcardTest() {
        List<Number> collNum = new LinkedList<Number>();
        List<?> collAny = collNum;
        collNum.add(3L);
        collNum.add(3.14);
        print("" + collAny.get(0));

        List<? super Long> collLong = new LinkedList<Long>();
        List<?> collAny2 = collLong;
        collLong.add(3L);
        //BAD:  collLong.add(3.14);
        print("" + collAny.get(0));
    }

    public static void main(String[] args) throws Exception {
        wideningTest();
    }
}
