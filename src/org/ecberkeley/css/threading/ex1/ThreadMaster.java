package org.ecberkeley.css.threading.ex1;

public class ThreadMaster {
	public Thread thr;
	public void go(){
		thr = Thread.currentThread();
		SleeperThread t1 = new SleeperThread(1, null/*this*/);
		t1.start();
		//SleeperThread t2 = new SleeperThread(2, null);
		//t2.start();
		try {
			System.out.println("trying to join unstarted thread...");
			t1.join();
			//System.out.println("trying to wait on unstarted thread...");
			//t1.wait();
			
		} catch (InterruptedException e) {
			System.out.println("Interrupted while trying to join t1");
		}
		System.out.println("All started.");
	}
	
	public static class SleeperThread extends Thread {
		private int id;
		private ThreadMaster caller;
		public SleeperThread(int id, ThreadMaster caller){
			this.id = id;
			this.caller=caller;
		}
		public void run(){
			for (int i = 0; (i<10); i++){
				System.out.println("Running "+id+" step: "+i);
				try {
					Thread.sleep(100);
					if (i==4 && caller!=null) {
						System.out.println("joining caller...");
						caller.thr.join();
						System.out.println("joined caller.");					
					}
				} catch (Exception e) {
				}	
				
			}
		}
	}
}
