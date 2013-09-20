package org.ecberkeley.css.threading.ex5;

public class BlockOnThis extends Thread {
    public static int i = 0;
    public String name;
    public boolean odd = true;

    public boolean blockOnClass = false;

    public void run() {
        while (true) {
            block();
        }
    }

    public void block() {
        if (blockOnClass) {
            synchronized (getClass()) {
                blockInner();
            }
        } else {
            synchronized (this) {
                blockInner();
            }
        }
    }

    public void blockInner() {
        int locali = BlockOnThis.i;
        System.out.println(name + " enter-block: " + i);
        for (int c = 0; c < 100000; c++) {
            String foo = "a" + c; //waste time
        }
        try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
        }
        if (odd) {
            locali++;
            BlockOnThis.i++;
        } else {
            BlockOnThis.i = BlockOnThis.i + 2;
            locali = locali + 2;
        }
        int msgi = BlockOnThis.i;
        String expected = locali == msgi ? "" + msgi : " another thread has cut in. BlockOnThis.i: " + msgi + " locali: " + locali;
        System.out.println(name + " leave-block: " + expected);
    }

    public static void testBlockOnThis(boolean blockOnClass) {

        BlockOnThis t1 = new BlockOnThis();
        t1.blockOnClass = blockOnClass;
        t1.name = "t1111";
        t1.start();

        BlockOnThis t2 = new BlockOnThis();
        t2.blockOnClass = blockOnClass;
        t2.odd = false;
        t2.name = "t2222";
        t2.start();
    }

    public static void test2(boolean blockOnClass) {
        BlockOnThisSubclass tt2 = new BlockOnThisSubclass();
        tt2.blockOnClass = blockOnClass;
        tt2.name = "tt2";
        tt2.odd = true;
        tt2.start();
    }

    public static void test3(boolean blockOnClass) {
        BlockOnThisSubclass t3 = new BlockOnThisSubclass();
        t3.blockOnClass = blockOnClass;
        t3.name = "t3";
        t3.odd = true;
        t3.start();
    }

    public static void main(String[] args) throws Exception {
        boolean blockOnClass = false;
        if (args.length > 1 && args[1].equalsIgnoreCase("-blockOnClass")) {
            blockOnClass = true;
        }
        if (args[0].equalsIgnoreCase("-test1")) {
            testBlockOnThis(blockOnClass);
        } else if (args[0].equalsIgnoreCase("-test2")) {
            test2(blockOnClass);
        } else if (args[0].equalsIgnoreCase("-test3")) {
            test3(blockOnClass);
        } else {
            System.out.println("Usage: org.ecberkeley.css.threading.ex5.BlockOnThis [-test1|-test2|test3] {-blockOnClass} ");
        }
    }

}
