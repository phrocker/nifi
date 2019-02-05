package org.apache.nifi.processor;

import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.bundle.Bundle;
import org.apache.nifi.bundle.BundleDetails;
import org.apache.nifi.components.ConfigurableComponent;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.init.ConfigurableComponentInitializer;
import org.apache.nifi.init.ConfigurableComponentInitializerFactory;
import org.apache.nifi.nar.*;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.reporting.ReportingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JniClassLoader  {

    private static final Logger logger = LoggerFactory
            .getLogger(JniClassLoader.class);


    private long nativePtr;

    private NarClassLoader loader;

    private ConcurrentHashMap<String,Class<?>> classes = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Map.Entry<String,String>,Method> onScheduledMethod = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String,JniComponent> componentMap = new ConcurrentHashMap<>();

    private List<JniBundle> bundles = new ArrayList<>();

    public JniClassLoader(){

    }

    public void setNarLocation(final String narDirectory,final String narWriteBase,final String docsDir, ClassLoader parent) throws IOException, ClassNotFoundException{
        // unpack the nar
        List<File> paths = new ArrayList<>();
        System.out.println("dir is " + narWriteBase);
        ExtensionMapping mapping = JniUnpacker.unpackNars(new File(narDirectory),new File(narWriteBase),new File(docsDir), paths);

        String narPath = narWriteBase + "/nifi-standard-nar-1.8.0.nar-unpacked/";
        System.out.println("dir is " + narPath + " " + mapping.getProcessorNames().size());
        setClassDir(narPath,parent);
        // get the property descriptors
        final BundleDetails details = NarBundleUtil.fromNarDirectory(new File(narPath));

        List<JniComponent> components = discoverExtensions(new Bundle(details, loader));

        componentMap.putAll(
                components.stream().collect(Collectors.toMap(JniComponent::getType,jniComponent -> jniComponent)));

        for(JniComponent component : components){
            System.out.println("for " + component.getType() + " we have " + component.getDescriptors().size());

        }

        bundles.add(new JniBundle(details,components));
    }

    public void setClassDir(String directory, ClassLoader parent) throws IOException, ClassNotFoundException {
        loader = new NarClassLoader(new File(directory),parent);
    }

    public Class getClass(final String className) throws ClassNotFoundException {
        return loader.loadClass(className);
    }

    public List<JniBundle> getBundles(){
        return Collections.unmodifiableList(bundles);
    }

    /**
     * Loads all FlowFileProcessor, FlowFileComparator, ReportingTask class types that can be found on the bootstrap classloader and by creating classloaders for all NARs found within the classpath.
     * @param bundle the bundles to scan through in search of extensions
     */
    public static List<JniComponent> discoverExtensions(final Bundle bundle) {
        // get the current context class loader
        ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();

        List<JniComponent> components = new ArrayList<>();

            // Must set the context class loader to the nar classloader itself
            // so that static initialization techniques that depend on the context class loader will work properly
            final ClassLoader ncl = bundle.getClassLoader();
            Thread.currentThread().setContextClassLoader(ncl);
            components.addAll( loadProcessors(bundle) );


        // restore the current context class loader if appropriate
        if (currentContextClassLoader != null) {
            Thread.currentThread().setContextClassLoader(currentContextClassLoader);
        }
        return components;
    }

    /**
     * Loads extensions from the specified bundle.
     *
     * @param bundle from which to load extensions
     */
    @SuppressWarnings("unchecked")
    private static List<JniComponent> loadProcessors(final Bundle bundle) {
        List<JniComponent> components = new ArrayList<>();
        final ServiceLoader<?> serviceLoader = ServiceLoader.load(Processor.class, bundle.getClassLoader());
        Iterator<?> sli = serviceLoader.iterator();
            while(sli.hasNext()){
                try {
                Object o = sli.next();
                // create a cache of temp ConfigurableComponent instances, the initialize here has to happen before the checks below
                if (o instanceof ConfigurableComponent) {

                        final ConfigurableComponent configurableComponent = (ConfigurableComponent) o;
                        initializeTempComponent(configurableComponent);
                        final Processor processor = Processor.class.cast(configurableComponent);
                        if (processor != null) {
                            List<PropertyDescriptor> descriptors = processor.getPropertyDescriptors();
                            components.add(JniComponent.JniComponentBuilder.create(processor.getClass().getCanonicalName()).addProperties(descriptors).build());

                        }

                }
                }catch(Throwable e){
                    logger.info("Ignoring ");
                }


            }
            return components;
    }

    private static void initializeTempComponent(final ConfigurableComponent configurableComponent) {
        ConfigurableComponentInitializer initializer = null;
        try {
            initializer = ConfigurableComponentInitializerFactory.createComponentInitializer(configurableComponent.getClass());
            initializer.initialize(configurableComponent);
        } catch (final InitializationException e) {
            logger.warn(String.format("Unable to initialize component %s due to %s", configurableComponent.getClass().getName(), e.getMessage()));
        }
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
