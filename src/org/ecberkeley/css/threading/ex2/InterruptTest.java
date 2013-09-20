package org.ecberkeley.css.threading.ex2;


public class InterruptTest {
	public static final int MAX_THREADS = 3;
	
	public static void print(String s){
		System.out.println(Thread.currentThread().getName()+": "+s);		
	}

	
	public static void main(String[] args) throws InterruptedException {
		Runnable r;
		Thread[] threads = new Thread[MAX_THREADS];
		for (int i=0; i<MAX_THREADS; i++){
			final int ii=i;
			r = new Runnable(){
				public void run() {
					String id = ""+ii;
					while (true){
						try{
							print("Runnable RUNNING "+id+" isInterrupted: "+Thread.currentThread().isInterrupted()); 
							for (int j=1;j<5000;j++){
								String[] s = new String[j*1000];
								s[j]="hello"+j;
								if (j%100==0){
									System.out.print(".");
									if (Thread.currentThread().isInterrupted()){
										print("isInterrupted");  
										break; //if you don't break or return here, 
										       //  then you won't get the interrupted exception
										       //  until you call sleep.
									}
								}
							}
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
		Thread.sleep(2000);

		for (int i=0; i<MAX_THREADS; i++){
			threads[i].interrupt();
		}
		Thread.sleep(5000);
		print("main exiting");
	}

}
