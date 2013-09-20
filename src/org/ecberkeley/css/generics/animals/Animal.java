package org.ecberkeley.css.generics.animals;

public class Animal{
    {
        System.out.println("in PRE initialization block: " + this.getClass().getName() +" this: "+ this);
    }
    public Animal(){
           System.out.println("in constructor: " + this.getClass().getName() +" this: "+ this);
    }
    {
        System.out.println("in initialization block: " + this.getClass().getName() +" this: "+ this);
    }
    static {
         System.out.println("in Animal static initialization block.");
    }
    {
        System.out.println("in LAST initialization block: " + this.getClass().getName() +" this: "+ this);
    }
}