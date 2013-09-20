package org.ecberkeley.css.threading.ex2;

//import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;

class ThreadedBucket {
	public static final int MIN = 0;
	public static final int MAX = 3;
	private int count = MIN;
	private String id;
	
	public ThreadedBucket(String id){
		this.id = id;
	}
	private String indent(int threadIndent){
		String TAB="     ";
		int TABWIDTH=TAB.length();
		StringBuffer b = new StringBuffer(threadIndent*TABWIDTH);
		for (int i=0; i<threadIndent; i++)	{
			b.append(TAB);
		}
		return b.toString();
	}
	public void print(String s, int threadIndent){
		System.out.println(Thread.currentThread().getName()+":"+id+": "+indent(threadIndent)+s);
	}
	private boolean testMin(int threadIndent){
		print("testMin", threadIndent);
		return (count <= MIN);
	}
	private boolean testMax(int  threadIndent){
		print("testMax", threadIndent);
		return (count >= MAX);
	}
	public synchronized void dec(int threadIndent) throws InterruptedException {
		print("dec enter", threadIndent);
		int enter = count;
		while (testMin(threadIndent)) {
			print("dec wait"+count, threadIndent);
			wait();
			print("dec done wait. count:"+count, threadIndent);
		}
		int wake = count;
		count--;
		String s = "";
		for(int i=1; i<10000; i++){
			s = s + " "+i;
		}
		notifyAll();
		print("dec "+enter+"->"+wake+"==>"+count, threadIndent);
	}
	public synchronized void inc(int threadIndent) throws InterruptedException {
		print("inc enter", threadIndent);
		int enter = count;
		while (testMax(threadIndent)) {
			print("inc wait"+count, threadIndent);
			wait();
			print("inc done wait. count:"+count, threadIndent);
		}
		int wake = count;
		count++;
		String s = "";
		for(int i=1; i<10000; i++){
			s = s + " "+i;
		}
		notifyAll();
		print("inc "+enter+"->"+wake+"==>"+count, threadIndent);
	}
}

class Runner extends Thread {
	public static final int INC = 1;
	public static final int DEC = -1;
	private int direction = INC;
	private String id;
	private ThreadedBucket bucket;
	int threadIndent = 0;
	
	public void print(String s){
		System.out.println(Thread.currentThread().getName()+":"+id+": "+s);
	}
	public Runner(String id, int direction, int threadIndent, ThreadedBucket bucket){
		super();
		setDaemon(true);
		this.id = id;
		this.direction = direction;
		this.bucket = bucket;
		this.threadIndent = threadIndent;
	}
	public void run(){
		while (true) {
			try {
				if (direction==INC){
					bucket.inc(threadIndent);
					Thread.currentThread().sleep(200);
				} else {
					bucket.dec(threadIndent);
					Thread.currentThread().sleep(100);
				}
					
			} catch (InterruptedException e){
				print("interrupted in "+id);
			}
		}
	}
	
}

public class TestThreads {

	public static void main(String[] args) throws InterruptedException {
		ThreadedBucket bucket = new ThreadedBucket("bucket");
		bucket.inc(0);
		bucket.inc(0);
		(new Runner("I1", Runner.INC, 1, bucket)).start();
		(new Runner("I2", Runner.INC, 2, bucket)).start();
		(new Runner("D1", Runner.DEC, 3, bucket)).start();
		(new Runner("D2", Runner.DEC, 4, bucket)).start();
		(new Runner("D3", Runner.DEC, 5, bucket)).start();
		(new Runner("D4", Runner.DEC, 6, bucket)).start();
		Thread.currentThread().sleep(10000); //wait 10 seconds for threads to run, then exit.
		System.out.println("main exit.");
	}

}
