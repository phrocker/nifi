package org.apache.nifi.processor;

import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.nar.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JniClassLoader  {

    private static final Logger logger = LoggerFactory
            .getLogger(JniClassLoader.class);


    private long nativePtr;

    private NarClassLoader loader;

    private ConcurrentHashMap<String,Class<?>> classes = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Map.Entry<String,String>,Method> onScheduledMethod = new ConcurrentHashMap<>();

    public JniClassLoader(){

    }

    public void setNarLocation(final String narDirectory,final String narWriteBase,final String docsDir, ClassLoader parent) throws IOException, ClassNotFoundException{
        // unpack the nar
        List<File> paths = new ArrayList<>();
        System.out.println("dir is " + narWriteBase);
        ExtensionMapping mapping = JniUnpacker.unpackNars(new File(narDirectory),new File(narWriteBase),new File(docsDir), paths);

        String narPath = narWriteBase + "/nifi-standard-nar-1.8.0.nar-unpacked/";
        System.out.println("dir is " + narPath + " " + mapping.getProcessorNames().size());
        ExtensionManager.getAllBundles()
        setClassDir(narPath,parent);
    }

    public void setClassDir(String directory, ClassLoader parent) throws IOException, ClassNotFoundException {
        loader = new NarClassLoader(new File(directory),parent);
    }

    public Class getClass(final String className) throws ClassNotFoundException {
        return loader.loadClass(className);
    }

    public static List<Method> getAnnotatedMethods(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) {
            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            if (methods.isEmpty())
                klass = klass.getSuperclass();
            else
                break;
        }
        return methods;
    }


    public String getMethod(final String className, final String annotation){
        return onScheduledMethod.get(new AbstractMap.SimpleImmutableEntry<>(className,annotation)).toString();
    }

    public Object createObject(final String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class clazz = classes.get(className);
        if (null == clazz){
            logger.debug("Looking for {}", className);
            clazz = loader.loadClass(className);
            classes.put(className,clazz);
        }
        if (clazz == null){
            logger.warn("Could not find {}", className);
       }
        else{
            List<Method> methods = getAnnotatedMethods(clazz, OnScheduled.class);
            methods.stream().forEach(mthd -> onScheduledMethod.put(new AbstractMap.SimpleImmutableEntry<>(className,"OnScheduled"),mthd));
        }

        return clazz.newInstance();
    }

}
