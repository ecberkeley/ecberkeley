package org.ecberkeley.css.generics;

import org.ecberkeley.css.generics.animals.Animal;
import org.ecberkeley.css.generics.animals.Cat;

import java.util.ArrayList;
import java.util.List;

public class GenericsWildcardTest {
	public boolean print(List<? extends Animal> list) {
    	//for (Iterator iter=list.iterator(); iter.hasNext();){
    		//list.add(new Cat());
    	//}
    	return true;	
    }
	
	
	
	public void testPrintGen(){
		List<Cat> list = new ArrayList<Cat>();
		list.add(new Cat());
		printGenericList(list, new Cat());
		System.out.println("List: "+list);
	}
	
	public <T extends Animal> boolean printGenericList(List<T> list, T pet) {
    	list.add(pet);
    	return true;	
    }

	
	
	
	
	
	
	
	
	public <T extends Animal> boolean copyTo(
			                             T[] array,
			                             List<T> list) {
    	for (int i=0; i<array.length; i++){
    		list.add(array[i]);
    	}
    	return true;	
    }
	
	
	public static void main(String [] args){
		GenericsWildcardTest t = new GenericsWildcardTest();
		t.testPrintGen();
		
	}
}
