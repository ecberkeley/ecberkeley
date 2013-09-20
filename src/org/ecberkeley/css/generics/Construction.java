package org.ecberkeley.css.generics;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class Construction {

    static class MyObject {
        public static String myStatic = "helloS";
        public String myMember = "helloM";
        public String toString(){
            return "MyObject:"+myMember;
        }

        public MyObject() {
            System.out.println("in MyObject()");
            myMember = "helloMfromCon";
        }
    }

    public static <T> void test1(Class<T> c) throws Exception {
        Object o = c.newInstance();
        System.out.println("newInstance created: MyObject");
        Class[] params = {};
        //Class[] classArray = (Arrays.<Class>asList(java.lang.String.class)).toArray(new Class[0]);
        Constructor noarg = c.getConstructor(params);
        Object[] EMPTYARGS = new Object[0];
        noarg.newInstance(EMPTYARGS);

        Constructor[] ca = o.getClass().getDeclaredConstructors();
        for (Constructor cc : ca) {
            if (cc.getParameterTypes().length == 0) {
                cc.newInstance(EMPTYARGS);
            }
        }

    }

    public static void main(String[] args) throws Exception {
           test1(MyObject.class);
    }
}
