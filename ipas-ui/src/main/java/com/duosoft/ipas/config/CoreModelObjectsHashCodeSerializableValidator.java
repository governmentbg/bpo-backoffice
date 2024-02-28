package com.duosoft.ipas.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 18.3.2020 г.
 * Time: 14:18
 * Class that checks
 * - if All the classes inside the bg.duosoft.ipas.core.model package override the hashCode method
 * - if All the classes inside the bg.duosoft.ipas.core.model implements {@link Serializable} interface
 */
@Configuration
@Slf4j
public class CoreModelObjectsHashCodeSerializableValidator {


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */

    private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;

    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException {
        CoreModelObjectsHashCodeSerializableValidator v = new CoreModelObjectsHashCodeSerializableValidator();
        v.validateCObjectsNotImplementingHashCode();
        v.validateCObjectsNotImplementingSerializable();
    }

    @PostConstruct
    public void validateCObjectsNotImplementingHashCode() throws IOException, ClassNotFoundException, NoSuchMethodException {
        List<Class> classes = getClasses();
        for (Class c : classes) {
            if (!c.equals(c.getMethod("hashCode").getDeclaringClass())) {
                log.warn(String.format("%s does not override the hashCode method. It's declared inside %s", c, c.getMethod("hashCode").getDeclaringClass()));
            }

        }
    }
    @PostConstruct
    public void validateCObjectsNotImplementingSerializable() throws IOException, ClassNotFoundException {
        List<Class> classes = getClasses();
        for (Class c : classes) {
            if (!Serializable.class.isAssignableFrom(c)) {
                log.warn(String.format("%s does not implement serializable", c));
            }
        }

    }
    private List<Class> getClasses() throws IOException, ClassNotFoundException {
        List<Class> classes = getClasses("bg.duosoft.ipas.core.model");
        return classes
                .stream()
                .filter(c -> !(c.getDeclaringClass() != null && (c.getDeclaringClass().getSimpleName() + "Builder").equals(c.getSimpleName())))//ignoring builders
                .filter(c -> !c.isInterface())//ignoring interfaces
                .collect(Collectors.toList());
    }

}
