package org.apache.nifi.processor;

import org.apache.nifi.nar.NarClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JniClassLoader  {

    private long nativePtr;

    private NarClassLoader loader;


    private ConcurrentHashMap<String,Class<?>> classes = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String,Class<?>> onScheduledMethod = new ConcurrentHashMap<>();


    public JniClassLoader(){

    }

    public void setClassDir(String directory, ClassLoader parent) throws IOException, ClassNotFoundException {
        loader = new NarClassLoader(new File(directory),parent);
    }

    public Class getClass(final String className) throws ClassNotFoundException {
        return loader.loadClass(className);
    }

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation

            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }

    public Object createObject(final String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class clazz = classes.get(className);
        System.out.println("Finding " + className);
        if (null == clazz){
            clazz = loader.loadClass(className);
            classes.put(className,clazz);
        }
        if (clazz == null){
            System.out.println("Could not find " + className);
        }
        else{
            System.out.println("Found " + clazz.getCanonicalName());
            System.out.println("can create? " +  clazz.newInstance() == null);
        }

        return clazz.newInstance();
    }

}
