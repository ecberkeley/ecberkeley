package org.ecberkeley.css.threading.ex3;

public class WorkerThread extends Thread {
	public WorkerThread(Observer caller){
		this.caller = caller;
	}
	private Observer caller;
	private String payload = "";
	public String getPayload(){
		return payload;		
	}
	public void run(){
		java.util.Random r = new java.util.Random();
		try {
			Thread.sleep(r.nextInt(20000));
		} catch (InterruptedException e){
			System.out.println("WorkerThread interrupted.");
			return; //don't call callback.
		}
		payload = "WorkerThread "+Thread.currentThread().getName()+" DONE.";
		caller.callback(this);	
	}
}
