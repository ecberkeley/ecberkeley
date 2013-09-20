package org.ecberkeley.css.threading.ex1;

public class ThreadMasterMandatoryJobs {
	public synchronized void go(){
		SleeperThread t1 = new SleeperThread(0, 3);
		t1.start();
	
		SleeperThread t2 = new SleeperThread(1, 10);
		t2.start();
	
		SleeperThread t3 = new SleeperThread(2, 10);
		t3.start();
	
		try {
			System.out.println("main thread: ["+Thread.currentThread().getName());
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted while trying to join t1");
		}
		System.out.println("done waiting.  ["+Thread.currentThread().getName());
	}
	
	
	public static class SleeperThread extends Thread {
		private int id;
		private int loops;
		public SleeperThread(int id, int loops){
			this.id = id;
			this.loops = loops;
		}
		public void run(){
			for (int i = 0; (i<loops); i++){
				System.out.println("Running "+id+" step: "+i);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}				
			}
			System.out.println(""+id+" at end of run() method. ["+Thread.currentThread().getName());
		}
	}

	
}
	
