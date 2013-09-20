package org.ecberkeley.css.lang;

/** Purpose of this class is to show how to move 
 *   shared initialization code out of constructors. */
public class Initialization {
    
    public Initialization() {
        init(DEFAULT_NAME, DEFAULT_AGE);
    }
    public Initialization(String name){
        init(name, DEFAULT_AGE);
    }
    public Initialization(String name, int age){
        init(name, age);
    }
    
    private void init(String name, int age){
        this.name = name; 
        this.age = age;
        //Potentially lots more work here...
    }
    
    public static final String DEFAULT_NAME = "anonymous";
    public static final int DEFAULT_AGE = 0;
    
    private String name = null;
    public String getName(){
    	return name;
    }
    
    private int age = 0;
    public int getAge(){
        return age;
    }
    
    private String pubField = "";
    public String getPubField() {
		return pubField;
	}
	public void setPubField(String pubField) {
		this.pubField = pubField;
	}
  
    private static void print(String arg){
        System.out.println(arg);  
    }
  
    public static void main(String[] args) {
       print("Hello, World!");
       if (args.length >= 1){
    	   String name = args[0];
    	   Initialization foo;
           if (args.length >= 2){
               int i = Integer.parseInt(args[1]);
               foo = new Initialization(name, i);
           } else {
        	   foo = new Initialization(name); 
           }
           print("initialized object: "+foo);
              
       } else {
    	   print("Usage: java org.ecberkeley.css.lang.Initialization {name}");
    	   print("    where {name} is a String");
       }
       System.exit(1);
    }
	

}



