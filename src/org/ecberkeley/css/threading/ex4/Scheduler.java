package org.ecberkeley.css.threading.ex4;

import java.util.LinkedList;

/** This section tests the semantics of annonymous inner classes implementing an interface.  Runnable is an interface that you only need to override run() of.
 */
interface Foo {
	public void bar();
}

class FooBar {
	public static void test(){
		Foo foo = new Foo(){
			public void bar(){
				System.out.println("in FooBar with Foo");
			}
		};
		foo.bar();
	}
	static {
		test();
	}
}
/*END FooBar section*/

public final class Scheduler extends Thread {
	private LinkedList queue = new LinkedList();
	public static final int MAX_THREADS = 10;
	private volatile int nThreadsAvail = MAX_THREADS;
	public Scheduler() {
	}
	
	public static void print(String s){
		System.out.println(Thread.currentThread().getName()+": "+s);		
	}
	
	public void run(){//TODO: make this code work where available threads at a time would be > 1.
		while (true){
			try {
				while (queue.peek()!=null){
				    runOne();  //simply returns if none are found, so a slipped condition would be OK.  Thus we don't sync this call.
				}
				synchronized(this){
					wait();
				}				
			} catch (Throwable e) {//nothing -- run forever
				print("exception: "+e); 
			}
		}
	}

	/** Safe to call from multiple threads - accepts....
	 */
	public synchronized void accept(Runnable r){
		print("accept");
		queue.add(r);
		print("accepted. queue.size: "+queue.size()+". notify");
		notifyAll();
	}
	
	private synchronized void runOne(){
		print("LOOKING_FOR_RUNNABLES");
		Runnable r = (Runnable)queue.peek();
		if (r != null) {
			r = (Runnable)queue.poll();					
			if (r!=null){
				Thread t = new WorkerThread(r, this);
				t.start();
			}
		}
		print("Found runnable");
	}
	
	public synchronized void threadDone(Runnable r){
		print("One Done"); 
		//nThreadsAvail++;
		queue.add(r);
		notifyAll();		
	}
	
	class WorkerThread extends Thread {
		Runnable slave;
		Scheduler caller;
		public WorkerThread(Runnable r, Scheduler s){
			super(r);
			this.slave = r;
			caller = s;
		}
		public void run(){
			try {
				print("slave.run ");
				slave.run();
			} finally {
				caller.threadDone(slave);
			}
			print("slave exiting");
		}
	}
	
	
		

	public static void main(String[] args) throws InterruptedException {
		Scheduler s = new Scheduler();
		s.setDaemon(true);
		s.start();
		Runnable r;
		for (int i=0; i<MAX_THREADS; i++){
			final int ii=i;
			r = new Runnable(){
				public void run() {
					String id = ""+ii;
					for(int q=0;q<10;q++){
						try{
							print("Runnable RUNNING "+id); 
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.out.println("Runnable interrupted");
						}
					}
					print("runnable "+ii+" ran.");
				}
			};
			print("calling accept with runnable "+ii);
			s.accept(r);
		}
		Thread.sleep(10000);
		print("main exiting");
	}
}