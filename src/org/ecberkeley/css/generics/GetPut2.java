package org.ecberkeley.css.generics;

public class GetPut2 {

    //============== Generic Class ========================
    public static class GenStruct<T> {
        T result;

        public void set(T t) {
            result = t;
        }

        public T get() {
            return result;
        }
    }
    //==================================================

    public static <U> void test(GenStruct<U> genStruct){
          System.out.println(""+genStruct.get());
    }

    public static <U extends Number> void test2(GenStruct<U> genStruct){
          System.out.println(""+genStruct.get());
    }



    public static void main(String[] args) throws Exception {
        GenStruct<String> genStruct = new  GenStruct<String>();
        genStruct.set("Mojo");
        System.out.println("genStructObj: " + genStruct.get());

        GenStruct<Object> genStructObj = new  GenStruct<Object>();
        genStructObj.set(new Integer(3));
        System.out.println("genStructObj: " + genStructObj.get());

        GetPut2.test(genStructObj);

         GenStruct<Integer> genStructInt = new  GenStruct<Integer>();
        genStructObj.set(new Integer(3));
        System.out.println("genStructObj: " + genStructObj.get());
        GetPut2.test2(genStructInt);

    }

}
