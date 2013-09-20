package org.ecberkeley.css.lang;

public class LangTests {
	private int secret = 99; // private member available to inner class, but NOT

	public class InnerClass {
		public int mojo = 3;

		public void print() {
			System.out.println("mojo: " + mojo + " outer.secret: " + secret);
		}
	}

	public static class StaticClass {
		private int mojo = 3;

		public void print() {
			System.out.println("mojo: " + mojo );
		}
	}


	static {
		StaticClass sc = new StaticClass();
		int i = sc.mojo;
	}

	
	public static void print(String msg) {
		System.out.println("" + msg);
	}

	public static String overload(String s) {
		print("in String overload(String) " + s);
		return "in String overload(String) " + s;
	}

	public static int overload(int i) {
		print("in int overload(int) " + i);
		return 3;
	}

	public static void overload() {
		print("in void overload()");
	}

	public static void testOverloads() {
		print("overloads...");
		LangTests.overload();
		LangTests.overload(4);
		LangTests.overload("t");
	}

	public static void testInnerClasses() {
		print("inner class InnerClass...");
		LangTests lt = new LangTests();
		InnerClass ic = lt.new InnerClass();
		ic.mojo = 10;
		ic.print();
		ic.mojo = 11;
		ic.print();
		InnerClass ic2 = lt.new InnerClass();
		ic2.mojo = 20;
		ic2.print();

		StaticClass sc = new StaticClass();
		print("inner static class StaticClass...");
		sc.mojo = 10;
		sc.print();
		sc.mojo = 11;
		sc.print();
		StaticClass sc2 = new StaticClass();
		sc2.mojo = 20;
		sc2.print();
	}
	
	public static void processVarArgs(Object... args) {
		for (Object o : args) {
			System.out.println("Object ... incoming arg == " + o + " cn: "+o.getClass().getName());
		}
	}
		
		public static void processVarArgs(String id, String... args) {
		for (String o : args) {
			System.out.println("String ... id:"+id+" incoming arg == " + o + " cn: "+o.getClass().getName());
		}
	}
	
	public enum PersonalityType {ARTISAN("SP"), GUARDIAN("SJ"), 
		                         RATIONAL("NT"), IDEALIST("NF");
		private String letters;
		PersonalityType(String letters){
			this.letters = letters;
		}
		public String toString(){
			return this.name()+'('+letters+')';
		}
	}
	



	public static void main(String[] args) {
		testOverloads();
		testInnerClasses();
		print("\r\n=== trying varargs: (Object...) ======");
		processVarArgs("string arg", 
				new java.util.Date(), 42, 
				new java.util.Vector<String>());
		
		print("\r\n=== trying varargs: (String, Object...) ======");
		processVarArgs("ID", 
				        "string arg", 
						"hellow",
						"orld");
		
		for(PersonalityType type : PersonalityType.values()){
			System.out.println("t:"+type+" t.getClass: "+type.getClass().getName());
		}
	}

	

}
