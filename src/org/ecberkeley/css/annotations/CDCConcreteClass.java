package org.ecberkeley.css.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Laramie Crocker
 */
@Retention(RetentionPolicy.RUNTIME)
abstract @interface CDCAnnotation {
	public String problem() default "problem";
	public String solution() default "solution";

}

@CDCAnnotation(problem="My class problem")
public class CDCConcreteClass {



    @CDCAnnotation(problem="My problem")
	public void foo(){
		
	}

}