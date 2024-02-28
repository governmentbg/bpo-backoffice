package bg.duosoft.ipas;


import bg.duosoft.ipas.utils.ClassFinder;
import bg.duosoft.ipas.utils.CoreUtils;
import bg.duosoft.ipas.utils.FileUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;


public class ExecuteGenerator {

    public static final String CORE_OBJECTS_BASE_PACKAGE = "bg.duosoft.ipas.core.model";
    public static final String REST_OBJECTS_BASE_PACKAGE = "bg.duosoft.ipas.rest.model";
    public static String DEFAULT_GENERATED_CLASSES_FOLDER = "generate/classes/";
    private static final String GENERATE_ALL_CLASSES_KEYWORD = "ALL";

    public static void main(String[] args) throws IOException {
        validateArgs(args);
        String enteredClasses = getClasses(args);
        String generatedClassesFolder = getGeneratedClassesFolder(args);

        List<String> objects;
        if (Objects.isNull(enteredClasses) || enteredClasses.length() < 1 || enteredClasses.equalsIgnoreCase(GENERATE_ALL_CLASSES_KEYWORD)) {
            objects = selectAllCoreClasses();
        } else {
            objects = Arrays.asList(enteredClasses.split(","));
            validateEnteredClasses(objects);
        }

        List<Class<?>> classes = ClassFinder.selectClassesInPackage(CORE_OBJECTS_BASE_PACKAGE);
        if (CoreUtils.isEmpty(classes))
            throw new RuntimeException("There aren't classes in selected base package !");

        FileUtils.deleteOldGeneratedFiles(generatedClassesFolder);

        for (String object : objects) {
            Class<?> selectedClass = classes.stream()
                    .filter(aClass -> aClass.getName().endsWith(object.trim()))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(selectedClass)) {
                FileUtils.generateRestClassJavaFileAndSaveIt(generatedClassesFolder, REST_OBJECTS_BASE_PACKAGE, selectedClass, null);
            }
        }
    }
    private static String getClasses(String[] args) {
        String classes = _getArgValue("classes", args);
        if (classes == null) {
            Scanner scanner = new Scanner(System.in);  // Create a Scanner object
            System.out.println(CoreUtils.ANSI_PURPLE + "Enter comma separated CORE classes, which exists in package '" + CORE_OBJECTS_BASE_PACKAGE + "' (example: CMark, CPatent, CUserdoc) or keyword 'all' to generate all classes..." + CoreUtils.ANSI_RESET);
            System.out.print("Enter classes here: ");
            classes = scanner.nextLine();  // Read user input
        }
        return classes;
    }
    private static String getGeneratedClassesFolder(String[] args) {
        String generatedClassesFolder = _getArgValue("target", args);
        if (generatedClassesFolder == null) {
            generatedClassesFolder = DEFAULT_GENERATED_CLASSES_FOLDER;
        }
        if (!generatedClassesFolder.endsWith("/") || !generatedClassesFolder.endsWith("\\")) {
            generatedClassesFolder += "/";
        }
        return generatedClassesFolder;
    }
    private static String _getArgValue(String arg, String[] args) {
        if (args != null && args.length != 0) {
            for (int i = 0; i < args.length; i+=2) {
                if (args[i].equals("-" + arg)) {
                    return args[i+1];
                }
            }
        }
        return null;
    }
    private static void validateArgs(String[] args) {
        if (args != null && args.length > 0) {
            if (args.length % 2 != 0) {
                System.out.println("Usage:");
                System.out.println("Possible parameters:");
                System.out.println("-target <target folder> - If not specified, the default target will be used " + DEFAULT_GENERATED_CLASSES_FOLDER);
                System.out.println("-classes <list of classes to be generated> - If not specified, a prompt will be shown!");
                System.exit(0);
            }
        }
    }

    private static void validateEnteredClasses(List<String> objects) throws IOException {
        List<Class<?>> classes = ClassFinder.selectClassesInPackage(CORE_OBJECTS_BASE_PACKAGE);
        for (String object : objects) {
            Class<?> selectedClass = classes.stream()
                    .filter(aClass -> aClass.getName().endsWith(object.trim()))
                    .findFirst()
                    .orElse(null);

            if (Objects.isNull(selectedClass))
                throw new RuntimeException("Entered class " + object + " doesn't exist in '" + CORE_OBJECTS_BASE_PACKAGE + "'. Please enter valid core classes !");
        }
    }

    private static List<String> selectAllCoreClasses() throws IOException {
        List<Class<?>> classes = ClassFinder.selectClassesInPackage(CORE_OBJECTS_BASE_PACKAGE);
        return classes.stream().map(aClass -> aClass.getSimpleName()).collect(Collectors.toList());
    }

}
