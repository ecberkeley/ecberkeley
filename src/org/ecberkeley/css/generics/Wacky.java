package org.ecberkeley.css.generics;


class Sequence<E> {
    private E[] theSequence;
    private int idx;

    // (non-static) inner class
    public class Iterator {
        boolean hasNext() {
            return (theSequence != null && idx < theSequence.length);
        }

        E getNext() {
            if (idx >= theSequence.length) return theSequence[idx];
            else return theSequence[idx++];
        }
    }

    public Iterator getIterator() {
        return this.new Iterator();
    }
}

public class Wacky {
    private static <T> void print(Sequence<T> seq) {
        Sequence<T>.Iterator iter = seq.getIterator();
        while (iter.hasNext()) System.out.println(iter.getNext());

        Sequence<T>.Iterator iter2 = seq.new Iterator();
        while (iter2.hasNext()) System.out.println(iter2.getNext());
    }

    public static void main(String[] args) {
        try {
            Sequence<String> seq1 = new Sequence<String>();
            print(seq1);
            Sequence<Long> seq2 = new Sequence<Long>();
            print(seq2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
