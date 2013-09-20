package org.ecberkeley.css.threading.ex1;

public class Main {

	public Main() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	throws Exception {

		ClassWithInner classWithInner = new ClassWithInner();
		classWithInner.go();
		classWithInner.innerClassTest();

		//(new ThreadMaster()).go();
		
		//(new ThreadMasterWait()).go();
		
		(new ThreadMasterMandatoryJobs()).go();
	}

}
