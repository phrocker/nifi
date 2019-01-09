package org.apache.nifi.processor;

import org.apache.nifi.nar.NarClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class JniClassLoader  {

    private long nativePtr;

    private NarClassLoader loader;


    private ConcurrentHashMap<String,Class<?>> classes = new ConcurrentHashMap<>();


    public JniClassLoader(){

    }

    public void setClassDir(String directory, ClassLoader parent) throws IOException, ClassNotFoundException {
        loader = new NarClassLoader(new File(directory),parent);
    }

    public Class getClass(final String className) throws ClassNotFoundException {
        return loader.loadClass(className);
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
