package org.ecberkeley.css.threading.ex1;

public class ThreadMasterWait {
	private int threadsWorking = 0;
	public synchronized void go(){
		SleeperThread t1 = new SleeperThread(1, 3, this);
		threadsWorking++;
		t1.start();
	
		SleeperThread t2 = new SleeperThread(2, 10, this);
		threadsWorking++;
		t2.start();
	
		try {
			System.out.println("main thread: ["+Thread.currentThread().getName());
			while (threadsWorking >0){
				//System.out.println("joining 1...");
				//t1.join();
				System.out.println("waiting...");
				wait();
				threadsWorking--;
				System.out.println("one thread done.  remaining: "+threadsWorking+" ["+Thread.currentThread().getName());
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted while trying to join t1");
		}
		System.out.println("done waiting.  ["+Thread.currentThread().getName());
	}
	public synchronized void done(){
		System.out.println("before notifyAll  ["+Thread.currentThread().getName());
		notifyAll();		
		System.out.println("after notifyAll ["+Thread.currentThread().getName());
	}
	
	public static class SleeperThread extends Thread {
		private int id;
		private int loops;
		private ThreadMasterWait caller;
		public SleeperThread(int id, int loops, ThreadMasterWait caller){
			this.id = id;
			this.loops = loops;
			this.caller = caller;
		}
		public void run(){
			for (int i = 0; (i<loops); i++){
				System.out.println("Running "+id+" step: "+i);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}				
			}
			done();
			System.out.println(""+id+" at end of run() method. ["+Thread.currentThread().getName());
		}
		public synchronized void done(){
			caller.done();
		}
	}
}
