package org.ecberkeley.css.lang;

import java.util.Date;
import java.util.List;
import java.util.Collections;
//import static java.util.Math.*;

public class Statics {
    public Statics() {
    }
    
    public static class InnerClass {
        private final int privInt = 3;
        private static int privStaticCounter = 0;
        public static String MOJO = "Nixon";
        public int doSomething(){ return privInt;}
    }
    public static class InnerClassWStatic {
        private static final int PRIV = 3;
        private static int privStaticCounter = 0;
        
        public static String FINBAR = "McGrath";
        public static int doSomething(){ return PRIV;}
    }

    public static final String DEFAULT_NAME = "anonymous";
    public static final String HTTP = "http://";
    public static final String PROTO = HTTP+"://";
    
    public static int publicInt = 0;
    
    private static int gCounter = 0;
    
    
    public static int usefulTransform(int p){
        return p+9;
    }
    
    private static void print(String arg){
        System.out.println(arg);  
    }
   
    
    private static Statics lazySingleton = null;
    public static Statics lazyInstance(){
        if (lazySingleton == null){
            lazySingleton = new Statics();
        }
        return lazySingleton;
    }
    
    private static final Date fieldLoadedAt = new Date();
    
    private static Date loadedAt;
    private static List myEmptyList;
    
    static {
          loadedAt = new Date();
          myEmptyList = java.util.Collections.emptyList();
          gCounter++;
    }
    

    //======== main=======================
    
    public static void main(String[] args) {
        print("Hello, World!");
        print("InnerClass.doSomething() : "+ (new InnerClass()).doSomething());
        print("InnerClass.MOJO (instance): "+ (new InnerClass()).MOJO);
        print("InnerClass.MOJO   (static): "+ Statics.InnerClass.MOJO);
        print("Statics.InnerClass.privStaticCounter after increment: "
                  +(++Statics.InnerClass.privStaticCounter));
        
        print("InnerClassWStatic.doSomething() : "+InnerClassWStatic.doSomething());
        print("InnerClassWStatic.FINBAR : "+Statics.InnerClassWStatic.FINBAR);
        print("Statics.InnerClassWStatic.privStaticCounter after increment: "
                  +(++Statics.InnerClassWStatic.privStaticCounter));
        
        print("class loaded at: "+loadedAt);
        
        print("gCounter: "+gCounter);
        print("gCounter after increment: "+(++gCounter));
        print("gCounter after increment: "+(++Statics.gCounter));
        
        if (args.length>= 2){
               int i = Integer.parseInt(args[1]); 
               print("A useful transform: "+Statics.usefulTransform(i));
       }    
       System.exit(1);  //optional, it will happen anyway without other threads.
    }
    
    
    
    /* Sample Output:
    =============================
        Hello, World!
		InnerClass.doSomething() : 3
		InnerClass.MOJO (instance): Nixon
		InnerClass.MOJO   (static): Nixon
		Statics.InnerClass.privStaticCounter after increment: 1
		InnerClassWStatic.doSomething() : 3
		InnerClassWStatic.FINBAR : McGrath
		Statics.InnerClassWStatic.privStaticCounter after increment: 1
		class loaded at: Tue Nov 13 22:34:49 PST 2012
		gCounter: 1
		gCounter after increment: 2
		gCounter after increment: 3
     */

}



