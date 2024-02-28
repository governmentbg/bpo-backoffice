package bg.duosoft.ipas.utils;

import bg.duosoft.ipas.ExecuteGenerator;
import bg.duosoft.ipas.model.JavaFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class FileUtils {
    private static final char CORE_OBJECTS_CLASS_NAME_PREFIX = 'C';
    private static final char REST_OBJECTS_CLASS_NAME_PREFIX = 'R';
    public static String GENERATED_MAPPERS_FOLDER = "generate/mappers/";
    public static final String JAVA_FILE_SUFFIX = ".java";

    public static void generateRestClassJavaFileAndSaveIt( String generatedClassesFolder, String restBasePackage, Class<?> aClass, Class<?> originalClass) throws IOException {
        if (Objects.isNull(originalClass) || !aClass.getName().equals(originalClass.getName())) {
            boolean isFileExist = isFileAlreadyGenerated(aClass, generatedClassesFolder);
            if (!isFileExist) {
                String saveFolder = generateSaveFolderPath(generatedClassesFolder, aClass);
                JavaFile javaFile = JavaClassFileGenerator.generateJavaClassFile(generatedClassesFolder, aClass, generateJavaFilePackage(generatedClassesFolder, saveFolder));
                FileUtils.saveFile(javaFile, saveFolder);
            }
        }
    }

    private static String generateJavaFilePackage(String generatedClassesFolder, String saveFolder) {
        String string = saveFolder.replaceFirst(generatedClassesFolder, "").replaceAll("/", "\\.");
        if (string.trim().equals("")) {
            return ExecuteGenerator.REST_OBJECTS_BASE_PACKAGE;
        } else {
            String textWithoutEndDot = string.substring(0, string.length() - 1);
            return ExecuteGenerator.REST_OBJECTS_BASE_PACKAGE + "." + textWithoutEndDot;
        }
    }

    private static String generateSaveFolderPath(String generatedClassesFolder, Class<?> aClass) {
        String simpleName = aClass.getSimpleName();
        String savePackage = aClass.getName()
                .replaceFirst(ExecuteGenerator.CORE_OBJECTS_BASE_PACKAGE, "")
                .replaceAll(simpleName, "").replaceAll("\\.", "/");

        String saveFolder;
        if (savePackage.trim().equals("")) {
            saveFolder = generatedClassesFolder;
        } else {
            savePackage = savePackage.replaceFirst("/", "");
            saveFolder = generatedClassesFolder + savePackage;
        }
        return saveFolder;
    }

    private static void saveFile(JavaFile javaFile, String folder) throws FileNotFoundException {
        File file = new File(folder + javaFile.getName());
        file.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(file)) {
            out.println(javaFile.getContent());
            System.out.println("Generate class: " + CoreUtils.ANSI_GREEN + javaFile.getName() + CoreUtils.ANSI_RESET);
        }
    }

    private static boolean isFileAlreadyGenerated(Class<?> aClass, String folder) {
        String javaFileName = generateJavaFileName(aClass.getSimpleName());
        File[] generatedJavaFiles = new File(folder).listFiles();
        if (Objects.nonNull(generatedJavaFiles) && generatedJavaFiles.length > 0) {
            for (File file : generatedJavaFiles) {
                String name = file.getName();
                if (name.equals(javaFileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String generateJavaFileName(String name) {
        return generateRestClassName(name) + FileUtils.JAVA_FILE_SUFFIX;
    }

    public static String generateRestClassName(String coreClassSimpleName) {
        char firstChar = coreClassSimpleName.toCharArray()[0];
        char secondChar = coreClassSimpleName.toCharArray()[1];
        boolean upperCase2 = Character.isUpperCase(secondChar);
        if (firstChar == CORE_OBJECTS_CLASS_NAME_PREFIX && upperCase2) {
            return REST_OBJECTS_CLASS_NAME_PREFIX + coreClassSimpleName.substring(1);
        } else {
            return coreClassSimpleName;
        }
    }

    public static void deleteOldGeneratedFiles(String folder) {
        File file = new File(folder);
        deleteDir(file);
        System.out.println("Directory " + CoreUtils.ANSI_YELLOW + folder + CoreUtils.ANSI_RESET + " was deleted...");
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

}
