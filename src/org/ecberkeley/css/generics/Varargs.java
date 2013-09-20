package org.ecberkeley.css.generics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Varargs {
    public static <T> void addAll(List<T> list, T... arr){

    }
    public static <T> List<T> toList(T... arr){
        List<T> list = new ArrayList<T>();
        list.size();
        return list;
    }

    public static void main(String[]args){
        List<Integer> list = Varargs.toList();
        List<Serializable> lis2t = Varargs.toList(1, (Serializable)"two");

    }
}
