package org.ecberkeley.css.generics;

public class GenericsTest {
	private ThreadLocal<String> tl;
	private void addVars(){
		/* This works.  It just shows that you can parameterize on any class, e.g. the Class class, which is pretty weird.
		for (int i=0; i<5; i++){
			ThreadLocal<Class> tl = new ThreadLocal<Class>();
			tl.set(GenericsTest.class);
			print(tl.get().toString());
		}
		*/
		/* This works. It shows that you can create multiple threadlocals.
		for (int i=0; i<3; i++){
			ThreadLocal<String> tl = new ThreadLocal<String>();
			tl.set("I am #"+i);
			print(tl.get().toString());
		}
		*/
		tl = new ThreadLocal<String>();
	}
	private void reset(String var){
		tl.set(var);		
	}
	private void dump(){
		print(Thread.currentThread().getName()+":\t"+tl.get());
	}
	public static void main(String[] args) {
		for (int i=0;i<4;i++){
			final int ii = i;
			Thread t = new Thread(){
				public void run(){
					GenericsTest g = new GenericsTest();
					g.addVars();
					g.reset("My value is #1");
					g.dump();
					try {Thread.sleep(2000);} catch (InterruptedException ie){}
					if (ii%2==0) g.reset("My value is #2");
					g.dump();
					
				}
			};
			t.start();
		}
	}
	static void print(String s){
		System.out.println(s); 
	}

}
