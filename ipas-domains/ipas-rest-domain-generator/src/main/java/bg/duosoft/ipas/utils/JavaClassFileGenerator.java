package bg.duosoft.ipas.utils;

import bg.duosoft.ipas.ExecuteGenerator;
import bg.duosoft.ipas.core.model.annotation.RestGenerationIgnore;
import bg.duosoft.ipas.model.JavaFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class JavaClassFileGenerator {

    //Common
    private static final String BLANK_SPACE = " ";

    //Imports
    private static final String LOMBOK_IMPORT = "import lombok.*;";
    private static final String JACKSON_IMPORT = "import com.fasterxml.jackson.annotation.*;";
    private static final String JAVA_UTIL_DATE_IMPORT = "import java.util.Date;";
    private static final String JAVA_UTIL_LIST_IMPORT = "import java.util.List;";
    private static final String JAVA_UTIL_ARRAYLIST_IMPORT = "import java.util.ArrayList;";
    private static final String JAVA_UTIL_SET_IMPORT = "import java.util.Set;";
    private static final String JAVA_UTIL_HASHSET_IMPORT = "import java.util.HashSet;";
    private static final String JAVA_MATH_BIG_DECIMAL_IMPORT = "import java.math.BigDecimal;";

    //Annotations
    private static final String LOMBOK_DATA_ANNOTATION = "@Data";
    private static final String LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION = "@AllArgsConstructor";
    private static final String LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION = "@NoArgsConstructor";
    private static final String JSON_IGNORE_PROPERTIES_ANNOTATION = "@JsonIgnoreProperties(ignoreUnknown = true)";
    private static final String JSON_INCLUDE_NON_NULL_ANNOTATION = "@JsonInclude(JsonInclude.Include.NON_NULL)";
    private static final String JSON_FORMAT_DATE_ANNOTATION = "@JsonFormat(pattern = \"yyyy-MM-dd'T'HH:mm:ssZ\")";

    //Objects
    private static final String JAVA_UTIL_DATE = "java.util.Date";
    private static final String JAVA_UTIL_LIST = "java.util.List";
    private static final String JAVA_UTIL_SET = "java.util.Set";
    private static final String JAVA_MATH_BIG_DECIMAL = "java.math.BigDecimal";
    private static final String JAVA_LANG_OBJECT = "java.lang.Object";

    public static JavaFile generateJavaClassFile(String generatedClassesFolder, Class<?> originalClass, String restBasePackage) throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean hasExtendedClass = false;
        Class<?> superclass = originalClass.getSuperclass();
        if (Objects.nonNull(superclass)) {
            if (!superclass.getName().equals(JAVA_LANG_OBJECT)) {
                hasExtendedClass = true;
            }
        }

        Class<?>[] interfaces = originalClass.getInterfaces();
        boolean hasInterfaces = interfaces.length != 0;
        String simpleName = originalClass.getSimpleName();
        boolean isInterface = originalClass.isInterface();
        List<Field> fields = Arrays
                .stream(originalClass.getDeclaredFields())
                .filter(r -> !Modifier.isStatic(r.getModifiers()))
                .filter(r -> r.getAnnotationsByType(RestGenerationIgnore.class).length == 0)
                .collect(Collectors.toList());
        sb.append("package ")
                .append(restBasePackage).append(";\n\n")
                .append(JACKSON_IMPORT).append("\n");

        if (!isInterface) {
            sb.append(LOMBOK_IMPORT).append("\n\n")
                    .append(LOMBOK_DATA_ANNOTATION).append("\n");
            if (fields.size() > 0) {
                sb.append(LOMBOK_ALL_ARGS_CONSTRUCTOR_ANNOTATION).append("\n");
            }
            sb.append(LOMBOK_NO_ARGS_CONSTRUCTOR_ANNOTATION).append("\n");
        }

        sb.append(JSON_IGNORE_PROPERTIES_ANNOTATION).append("\n")
                .append(JSON_INCLUDE_NON_NULL_ANNOTATION).append("\n")
                .append("public ")
                .append(originalClass.isInterface() ? "interface " : "class ")
                .append(FileUtils.generateRestClassName(simpleName))
                .append(hasExtendedClass ? " extends " + FileUtils.generateRestClassName(superclass.getSimpleName()) : "")
                .append(hasInterfaces ? addInterfaces(interfaces) : "")
                .append(" {\n");

        boolean isContainDateClass = false;
        boolean isContainListClass = false;
        boolean isContainSetClass = false;
        boolean isContainBigDecimal = false;
        Set<String> enumPackages = new HashSet<>();

//        Field[] fields = originalClass.getDeclaredFields();
        List<Class<?>> rClassImports = new ArrayList<>();
        Map<String, List<Field>> collectionFields = new HashMap<>();
        for (Field field : fields) {
            String modifiers = Modifier.toString(field.getModifiers());
            String name = field.getName();
            Class<?> type = field.getType();

            if (type.getName().equals(JAVA_UTIL_DATE)) {
                sb.append("\t").append(JSON_FORMAT_DATE_ANNOTATION).append("\n");
                isContainDateClass = true;
            }
            if (type.getName().equals(JAVA_UTIL_LIST)) {
                isContainListClass = true;
                collectionFields.computeIfAbsent(JAVA_UTIL_LIST, (k) -> new ArrayList<>());
                collectionFields.get(JAVA_UTIL_LIST).add(field);
            }
            if (type.getName().equals(JAVA_UTIL_SET)) {
                isContainSetClass = true;
                collectionFields.computeIfAbsent(JAVA_UTIL_SET, (k) -> new ArrayList<>());
                collectionFields.get(JAVA_UTIL_SET).add(field);
            }
            if (type.getName().equals(JAVA_MATH_BIG_DECIMAL)) {
                isContainBigDecimal = true;
            }
            if (isIpasEnum(type)) {
                enumPackages.add(type.getPackage().getName());
            }

            sb.append("\t")
                    .append(modifiers)
                    .append(BLANK_SPACE)
                    .append(isListOrSetType(type) ? generateGenericType(generatedClassesFolder, field, restBasePackage, originalClass) : FileUtils.generateRestClassName(type.getSimpleName()))
                    .append(BLANK_SPACE)
                    .append(name).append(";\n");

            addGenericTypesToImportList(rClassImports, field, type);
            if (ClassFinder.isClassExistInCoreBasePackage(type)) {
                rClassImports.add(type);
                FileUtils.generateRestClassJavaFileAndSaveIt(generatedClassesFolder, restBasePackage, type, originalClass);
            }
        }

        if (hasExtendedClass) {
            if (ClassFinder.isClassExistInCoreBasePackage(superclass)) {
                rClassImports.add(superclass);
            } else {
                String importInterface = "import " + superclass.getName() + ";";
                sb.insert(sb.indexOf(LOMBOK_IMPORT), importInterface + "\n");
            }
        }

        if (isContainDateClass) {
            sb.insert(sb.indexOf(LOMBOK_IMPORT), JAVA_UTIL_DATE_IMPORT + "\n");
        }
        if (isContainListClass) {
            sb.insert(sb.indexOf(LOMBOK_IMPORT), JAVA_UTIL_LIST_IMPORT + "\n");
            sb.insert(sb.indexOf(LOMBOK_IMPORT), JAVA_UTIL_ARRAYLIST_IMPORT + "\n");
        }
        if (isContainSetClass) {
            sb.insert(sb.indexOf(LOMBOK_IMPORT), JAVA_UTIL_SET_IMPORT + "\n");
            sb.insert(sb.indexOf(LOMBOK_IMPORT), JAVA_UTIL_HASHSET_IMPORT + "\n");
        }
        if (enumPackages.size() > 0) {
            for (String ep : enumPackages) {
                sb.insert(sb.indexOf(LOMBOK_IMPORT), "import " + ep + ".*;\n");
            }

        }
        if (isContainBigDecimal) {
            sb.insert(sb.indexOf(LOMBOK_IMPORT), JAVA_MATH_BIG_DECIMAL_IMPORT + "\n");
        }

        if (rClassImports.size() > 0) {
            for (Class<?> rClassImport : rClassImports) {
                String coreClassSimpleName = rClassImport.getSimpleName();
                String restClassSimpleName = FileUtils.generateRestClassName(coreClassSimpleName);
                String coreClassName = rClassImport.getName();

                String importPackageString = coreClassName.replaceAll(ExecuteGenerator.CORE_OBJECTS_BASE_PACKAGE, ExecuteGenerator.REST_OBJECTS_BASE_PACKAGE).replaceAll(coreClassSimpleName, restClassSimpleName);
                String importString = "import " + importPackageString + ";";

                if (!importPackageString.equals(restBasePackage + "." + restClassSimpleName)) {
                    if (!sb.toString().contains(importString)) {
                        sb.insert(sb.indexOf(LOMBOK_IMPORT), importString + "\n");
                    }
                }
            }
        }

        if (hasInterfaces) {
            for (Class<?> anInterface : interfaces) {
                String importInterface;
                if (ClassFinder.isClassExistInCoreBasePackage(anInterface)) {
                    importInterface = "import " + anInterface.getName().replaceFirst(ExecuteGenerator.CORE_OBJECTS_BASE_PACKAGE, ExecuteGenerator.REST_OBJECTS_BASE_PACKAGE) + ";";
                } else {
                    importInterface = "import " + anInterface.getName() + ";";
                }
                sb.insert(sb.indexOf(LOMBOK_IMPORT), importInterface + "\n");

            }
        }

        if (collectionFields.size() > 0) {
            for (String type : collectionFields.keySet()) {
                for (Field field : collectionFields.get(type)) {
                    sb.append("\tpublic " + generateGenericTypeName(field) + " " + generateGetterName(field.getName()) + "() {\n");
                    sb.append("\t\tif (" + field.getName() + " == null) {\n");
                    sb.append("\t\t\t" + field.getName() + " = new " + getCollectionImplementation(type) + "<>();\n");
                    sb.append("\t\t}\n");
                    sb.append("\t\treturn " + field.getName() + ";\n");
                    sb.append("\t}\n");
                }
            }
        }


        sb.append("}\n");
        return new JavaFile(sb.toString(), FileUtils.generateJavaFileName(simpleName));
    }
    private static String getCollectionImplementation(String type) {
        switch (type) {
            case JAVA_UTIL_LIST:
                return "ArrayList";
            case JAVA_UTIL_SET:
                return "HashSet";
            default:
                throw new RuntimeException("Unknown list type");
        }
    }
    private static String generateGetterName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    private static void addGenericTypesToImportList(List<Class<?>> rClassImports, Field field, Class<?> type) throws IOException {
        if (isListOrSetType(type)) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            if (ClassFinder.isClassExistInCoreBasePackage(genericClass)) {
                rClassImports.add(genericClass);
            }
        }
    }


    private static String generateGenericType(String generatedClassesFolder, Field field, String restBasePackage, Class<?> originalClass) throws IOException {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

        if (ClassFinder.isClassExistInCoreBasePackage(genericClass)) {
            FileUtils.generateRestClassJavaFileAndSaveIt(generatedClassesFolder, restBasePackage, genericClass, originalClass);
        }
        return generateGenericTypeName(field);
    }
    private static String generateGenericTypeName(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        String name = FileUtils.generateRestClassName(genericClass.getSimpleName());
        return isList(field.getType()) ? "List<" + name + ">" : "Set<" + name + ">";
    }

    private static boolean isListOrSetType(Class<?> type) {
        return type.getName().equals(JAVA_UTIL_LIST) || type.getName().equals(JAVA_UTIL_SET);
    }

    private static boolean isList(Class<?> type) {
        return type.getName().equals(JAVA_UTIL_LIST);
    }

    private static boolean isSet(Class<?> type) {
        return type.getName().equals(JAVA_UTIL_SET);
    }

    private static boolean isIpasEnum(Class<?> type) throws IOException {
        List<Class<?>> classes = ClassFinder.selectEnumClasses();
        for (Class<?> aClass : classes) {
            if (aClass.getName().equals(type.getName())) {
                return true;
            }
        }
        return false;
    }

    private static String addInterfaces(Class<?>[] classes) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Class<?>> interfaces = Arrays.asList(classes);
        if (interfaces.size() > 0) {
            stringBuilder.append(" implements");
            String collect = interfaces.stream().map(aClass -> " " + aClass.getSimpleName()).collect(Collectors.joining(","));
            stringBuilder.append(collect);
        }
        return stringBuilder.toString();
    }

}
