package com.duosoft.ipas.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassFinder {

    public static final String BASE_CORE_PACKAGE = "bg.duosoft.ipas.core.model";
    public static final String IPAS_ENUM_PACKAGE = "bg.duosoft.ipas.enums";

    public static List<Class<?>> selectClassesInPackage(String basePackage) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ImmutableSet<ClassPath.ClassInfo> topLevelClasses = ClassPath.from(loader).getTopLevelClasses();

        List<Class<?>> matchedClasses = new ArrayList<>();
        for (final ClassPath.ClassInfo info : topLevelClasses) {
            if (info.getName().startsWith(basePackage)) {
                final Class<?> clazz = info.load();
                matchedClasses.add(clazz);
            }
        }
        return matchedClasses;
    }

    public static boolean isClassExistInCoreBasePackage(Class<?> clazz) throws IOException {
        List<Class<?>> classes = selectClassesInPackage(BASE_CORE_PACKAGE);
        String simpleName = clazz.getName();
        Class<?> foundClass = classes.stream().filter(aClass -> aClass.getName().equals(simpleName)).findFirst().orElse(null);
        return !Objects.isNull(foundClass);
    }

    public static List<Class<?>> selectEnumClasses() throws IOException {
        return selectClassesInPackage(IPAS_ENUM_PACKAGE);
    }

}
