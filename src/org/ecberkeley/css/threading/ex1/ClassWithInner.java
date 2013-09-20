package org.ecberkeley.css.threading.ex1;

public class ClassWithInner {
    public void go() {
        try {
            transferWater();
        } catch (Exception e) {
            //nothing
        }
    }

    public synchronized void transferWater() throws Exception {
        try {
            System.out.println("try");
            String s = null;
            s.length();
            //} catch (Exception e) {
            //	System.out.println("Exception e catch");
            //	throw e;
        } finally {
            System.out.println("finally");

        }
        System.out.println("leaving transferWater()");
    }

    private int var = 3;

    public abstract class InnerClass {
        public abstract void output();
    }

    public void innerClassTest() {
        var = 3;
        InnerClass ic = new InnerClass() {
            public void output() {
                System.out.println("Value of enclosing var is: " + var);

                synchronized (ClassWithInner.this) {     //forces sync on containing class "this" pointer, rather than on inner class.  Just a syntax example, no real reason here.
                    System.out.println("this:" + this.getClass().toString() + " ref: " + this);
                    System.out.println("Class.this:" + ClassWithInner.this.getClass().getCanonicalName() + " ref: " + ClassWithInner.this);
                    //Class.super works here, but just calls to Object.toString(), so doesn't look different.
                    System.out.println("Class.super:" + ClassWithInner.super.getClass().getCanonicalName() + " ref: " + ClassWithInner.super.toString());
                }
            }
        };

        var = 4;
        ic.output();
        var = 5;
        ic.output();

        MidClass mc = new MidClass();
        System.out.println("MidClass mc.toString()::"+mc.toString());

    }

    class MidClass {
        public UnderMidClass umc =  new UnderMidClass();
        public String toString() {
            return "[I'm MidClass "+MidClass.super.toString()+" I contain: \r\r\t{"+umc+"}]";
        }

        class UnderMidClass {
            public InnermostClass imc = new InnermostClass();
            public String toString() {
               return "[I'm UnderMidClass "+UnderMidClass.super.toString()+" I contain: \r\n\t\t{"+imc+"}]";
            }

            class InnermostClass {
                public String toString() {
                    return "[I'm InnermostClass "+InnermostClass.super.toString()+"]";
                }
            }
        }

    }
}
