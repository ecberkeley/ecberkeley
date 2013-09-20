package org.ecberkeley.css.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**  This class generically walks the hierarchy of any Class, looking for annotations that you specify.
 *
 *   See also:  org\jboss\resteasy\core\ResourceMethodRegistry.java  for a specific way to look for JAX-RS annotations.
 *   For an implementation that walks JAX-RS annotations see: http://issues.collectionspace.org/browse/CSPACE-3553
 *
 *  @author Laramie Crocker
 */
public class AnnotationInspector {

    public static boolean hasAnnotations(Class<?> c, Class <? extends java.lang.annotation. Annotation> annotationClass ) {
        if (c.isAnnotationPresent(annotationClass)) {
            System.out.println("c.getAnnotation(): " + c.getAnnotation(annotationClass));
            System.out.println("c.isAnnotationPresent("+annotationClass.getName()+")");
        }
        for (Method method : c.isInterface() ? c.getMethods() : c.getDeclaredMethods()) {
            System.out.println("method: " + method);
            if (method.isAnnotationPresent(annotationClass)) {
                System.out.println("method.isAnnotationPresent("+annotationClass.getName()+")");
            }
            for (Annotation ann : method.getAnnotations()) {
                System.out.println("Annotation: " + ann);
            }
        }
        return false;
    }

    public static Class walkResources(Class clazz, Class <? extends java.lang.annotation. Annotation> annotationClass) {
        System.out.println("ENTER getSubResourceClass");
        // check class & superclasses for annotations of the passed in annotationsClass type
        for (Class<?> actualClass = clazz; keepLookingForTopClass(actualClass); actualClass = actualClass.getSuperclass()) {
            if (hasAnnotations(actualClass, annotationClass)) {
                System.out.println("FOUND: " + actualClass);
            }
        }
        // ok, no @Path or @HttpMethods so look in interfaces.
        for (Class intf : clazz.getInterfaces()) {
            if (hasAnnotations(intf, annotationClass)) {
                System.out.println("FOUND interfacef: " + intf);
            }
        }
        System.out.println("LEAVE getSubResourceClass");
        return null;
    }

    private static boolean keepLookingForTopClass(Class<?> actualClass) {
        return actualClass != null && actualClass != Object.class;
    }

    public static void main(String args){
        hasAnnotations(CDCConcreteClass.class, CDCAnnotation.class);
    }
}
