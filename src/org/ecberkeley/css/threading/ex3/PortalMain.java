package org.ecberkeley.css.threading.ex3;

public class PortalMain {

	public PortalMain() {
	}
	public void service(){
		Observer o = new Observer(5);
		//o.start();
		//while (o.workersRemaining()>0){
			//try {
				//o.wait(30*1000);
				o.doWork(5*1000);
			//} catch (InterruptedException e){
				//nothing.
			//}
			//System.out.println("o still working");
		//}
		System.out.println("o is has "+o.workersRemaining()+" workers remaining, and finished: "+o.getList());
	}

	public void service2(){
		System.out.println("service2...");	
		Observer o = new Observer(5);
		o.setThreadTimeout(8*1000);
		o.start();
		synchronized(o){
			try {
				o.wait(3*1000);
				if (!o.allDone()){
					o.interrupt();					
				}
			} catch (InterruptedException e2) {
				System.out.println("service2 interrupted");				
			}
			
		}
		System.out.println("service2 DONE");				
		
		System.out.println("o is has "+o.workersRemaining()+" workers remaining, and finished: "+o.getList());
		try {
			Thread.sleep(20*1000);
		} catch (InterruptedException e3) {
			
		}
	}

	public static void main(String[] args) {
		PortalMain p = new PortalMain();
		//p.service();
		p.service2();
	}

}
