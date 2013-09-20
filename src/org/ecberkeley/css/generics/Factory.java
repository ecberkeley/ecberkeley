package org.ecberkeley.css.generics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Factory<T> {
    T t;
    public  List<T> make(int size) {

        Object array = Array.newInstance(t.getClass(), size);
        return Arrays.asList((T[]) array);    // warning: unchecked cast
    }
}
