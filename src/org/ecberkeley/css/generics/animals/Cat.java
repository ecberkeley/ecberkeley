package org.ecberkeley.css.generics.animals;

import org.ecberkeley.css.generics.animals.Animal;

public class Cat extends Animal {
    public Cat(){
         System.out.println("in Cat constructor: " + this.getClass().getName() +" this: "+ this);
    }
    public String meow(){
        return "Meeeeow!";
    }
}