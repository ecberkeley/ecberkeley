package org.ecberkeley.css.threading.ex2;

public class JoinTest {

	public static final int MAX_THREADS = 3;
	
	public static void print(String s){
		System.out.println(Thread.currentThread().getName()+": "+s);		
	}
	
	private StringBuffer results = new StringBuffer();
	
	public void callback(String msg){
		synchronized(results){
			results.append(msg);
		}
	}

	public void doit() throws InterruptedException {
		Runnable r;
		Thread[] threads = new Thread[MAX_THREADS];
		for (int i=0; i<MAX_THREADS; i++){
			final int ii=i;
			final JoinTest jt = this;
			r = new Runnable(){
				public void run() {
					String id = ""+ii;
					while (true){
						try{
							print("Runnable RUNNING "+id+" isInterrupted: "+Thread.currentThread().isInterrupted()); 
							for (int j=1;j<200;j++){
								String[] s = new String[j*1000];
								s[j]="hello"+j;
								if (j%20==0){
									System.out.print(".");
									
								}
								if (Thread.currentThread().isInterrupted()){
									print("isInterrupted");  
									return; //if you don't break or return here, 
									       //  then you won't get the interrupted exception
									       //  until you call sleep.
								}
							}
							jt.callback("Thread looped: "+Thread.currentThread().getName());
							Thread.sleep(500);
						} catch (InterruptedException e) {
							print("Runnable interrupted"+" isInterrupted: "+Thread.currentThread().isInterrupted());
							return;
						}
					}
				}
			};
			 
			threads[i] = new Thread(r);
			threads[i].setDaemon(true);
			threads[i].start();

			print("Created runnable "+ii);
		}
		Thread.sleep(5000);
		
		for (int i=0; i<MAX_THREADS; i++){
			threads[i].interrupt();
		}
		Thread.sleep(5000);
		print(results.toString());
		Thread t = threads[0];
		t.join();
		print("Joined thread[0] status: "+t+" isAlive: "+t.isAlive());

	}
	public static void main(String[] args) throws InterruptedException {
		JoinTest j = new JoinTest();
		j.doit();
		print("main exiting");
	}

}

