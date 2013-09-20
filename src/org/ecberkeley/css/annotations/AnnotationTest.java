package org.ecberkeley.css.annotations;

/**
 * @author Laramie Crocker
 */
public class AnnotationTest {

    public static void main(String[]args){
        System.out.println("LOOKING FOR ANNOTATIONS");
		CDCConcreteClass c = new CDCSubclass();
		Class cc = c.getClass();
		for (Object o: cc.getAnnotations()){
			System.out.println(o.toString());
		}
        AnnotationInspector.hasAnnotations(CDCSubclass.class, CDCAnnotation.class);
        AnnotationInspector.walkResources(CDCSubclass.class, CDCAnnotation.class);
		System.out.println("DONE");
	}

}
