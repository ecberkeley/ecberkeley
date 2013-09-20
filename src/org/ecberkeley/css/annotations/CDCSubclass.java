package org.ecberkeley.css.annotations;

/**
 * @author Laramie Crocker
 */
@CDCAnnotation(problem="My subclass problem")
public class CDCSubclass extends CDCConcreteClass {
    @CDCAnnotation(problem="My subclass method problem")
	public void foo(){
	}

}
