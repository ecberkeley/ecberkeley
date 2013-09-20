package org.ecberkeley.css.threading;

import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadLocalEx extends Thread {

    // Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return nextId.getAndAdd(100);
        }
    };

    // Returns the current thread's unique ID, assigning it if necessary
    public static int get() {
        return threadId.get();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                int was = get();
                threadId.set(threadId.get()+1);
                print("was:"+was+"-->"+get());
            } catch (InterruptedException e) {
                 return;
            }
        }
    }

    public static void print(String msg) {
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }

    public static void main(String[] args) {
        ThreadLocalEx t1 = new ThreadLocalEx();
        ThreadLocalEx t2 = new ThreadLocalEx();
        ThreadLocalEx t3 = new ThreadLocalEx();
        t1.start();
        t2.start();

        final Map<String, Object> m = new TreeMap<String, Object>();
        m.put("keyA", "StA");
        m.put("keyB", "StB");
        m.put("keyC", "StC");
        m.put("keyD", "StD");
        m.put("keyO", new Object());
        m.put("keyO", new Integer(3));
        String[] sa = m.keySet().toArray(new String[0]);
        Object[] sa2 = m.values().toArray(new Object[0]);


        print("arr "+ Arrays.deepToString(sa));
        print("Arrays.toString(arr) "+ Arrays.toString(sa));
        print("Arrays.toString(sa2) "+ Arrays.toString(sa2));

        try {
            Thread.sleep(1000);
            print("t1: " + t1.get());
            Thread.sleep(1000);

            print("t2: " + t2.get());
            t1.interrupt();
            t2.interrupt();
            t2.join();
            print("Note that main doesn't get() the same value as is accessible inside the worker threads, because those use their own ThreadLocal.");
            print("t2 join: "+t2.get());
        } catch (InterruptedException e) {

        }

    }
}
