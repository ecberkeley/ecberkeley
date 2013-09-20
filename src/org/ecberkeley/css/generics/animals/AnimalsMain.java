package org.ecberkeley.css.generics.animals;

import java.util.ArrayList;
import java.util.List;

public class AnimalsMain {

    public static void insertCats(List<? super Cat> list) {
        list.add(new Cat());
        list.add(new HouseCat());
        list.add(new Cheetah());
        // illegal: Cat aCat = list.get(0);
        // illegal: list.add(new Dog());
        // illegal:   list.add(new Animal());
    }

    public static void insertAnimals(List<? super Animal> list) {
        list.add(new Cat());
        list.add(new HouseCat());
        list.add(new Cheetah());
        list.add(new Dog());
        list.add(new Animal());
        //illegal:  for (Animal a: list){         }
    }

    public static void processElements(List<? extends Cat> list) {
        // illegal: list.add(new Cat());
        for (Cat aCat : list) {
            System.out.println(aCat.meow());
        }
    }

    private static final String PAD="\r\n\t\t";



    private static String wrap(Object o) {
        return o.toString().replaceAll(",", ","+PAD);
    }

    public static void main(String[] args) throws Exception {
        List<Object> listObjects = new ArrayList<Object>();
        insertCats(listObjects);                        // signature: insertCats(List<? super Cat> list)
        System.out.println("inserted into List<Object>: "+PAD + wrap(listObjects));

        List<Animal> listOAnimals = new ArrayList<Animal>();
        insertAnimals(listOAnimals);              // signature: insertCats(List<? super Cat> list)
        System.out.println("inserted into List<Animal>: "+PAD + wrap(listOAnimals));
        insertCats(listOAnimals);
        System.out.println("inserted MORE into List<Animal>:"+PAD + wrap(listOAnimals));
        // illegal: processElements(listOAnimals);

        List<Cat> listOCats = new ArrayList<Cat>();
        insertCats(listOCats);
        System.out.println("inserted into List<Cat>:" + PAD + wrap(listOCats));
        processElements(listOCats);
    }
}
