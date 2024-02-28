package bg.duosoft.ipas.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum FileType {
    MARK("N"),
    PATENT("P"),
    INTERNATIONAL_MARK("M"),
    SPC("S"),
    EU_PATENT("T"),
    UTILITY_MODEL("U"),
    GEOGRAPHICAL_INDICATIONS("Г"),
    GEOGRAPHICAL_INDICATIONS_V("В"),
    DESIGN("Д"),
    SINGLE_DESIGN("Е"),
    INTERNATIONAL_NP("Н"),
    DIVISIONAL_DESIGN("Р"),
    //    changed to Cyrillic
    PLANTS_AND_BREEDS("С"),
    INTERNATIONAL_SINGLE_DESIGN("У"),
    INTERNATIONAL_DESIGN("Х"),
    DIVISIONAL_MARK("D"),
    INTERNATIONAL_MARK_I("I"),
    INTERNATIONAL_MARK_R("R"),
    INTERNATIONAL_MARK_B("B"),
    ACP("A"),
    USERDOC("E");

    FileType(String code) {
        this.code = code;
    }

    private String code;

    public String code() {
        return code;
    }

    public static FileType selectByCode(String code) {
        FileType fileType = Arrays.stream(FileType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(fileType))
            throw new RuntimeException("Cannot find FileType with code: " + code);

        return fileType;
    }

    public static List<FileType> getMarkFileTypes() {
        return Arrays.asList(MARK, DIVISIONAL_MARK);
    }


    public static List<String> getAcpAffectedObjectFileTypes() {
        return Arrays.asList(MARK.code(), PATENT.code(), EU_PATENT.code(), UTILITY_MODEL.code(), GEOGRAPHICAL_INDICATIONS.code(), DESIGN.code(),
                PLANTS_AND_BREEDS.code(), DIVISIONAL_MARK.code());
    }

    public static List<String> getNationalMarkFileTypes() {
        return Arrays.asList(MARK.code(), DIVISIONAL_MARK.code());
    }

    public static List<String> getInternationalMarkFileTypes() {
        return Arrays.asList(INTERNATIONAL_MARK_I.code(), INTERNATIONAL_MARK_R.code(), INTERNATIONAL_MARK_B.code());
    }

    public static List<String> getPatentRelatedFileTypes() {
        return Arrays.asList(PATENT.code(), EU_PATENT.code(), UTILITY_MODEL.code()
                , DESIGN.code(), SPC.code(), PLANTS_AND_BREEDS.code());
    }

    public static List<String> getMarkRelatedFileTypes() {
        return Arrays.asList(MARK.code(),
                DIVISIONAL_MARK.code(),
                GEOGRAPHICAL_INDICATIONS.code(),
                INTERNATIONAL_MARK_I.code(),
                INTERNATIONAL_MARK_R.code(),
                INTERNATIONAL_MARK_B.code());
    }

    public static List<String> getLinkedFileTypes(String fileType) {
        if (Objects.isNull(fileType)) {
            return null;
        }
        List<String> linkedFileTypes = new ArrayList<>();
        linkedFileTypes.add(fileType);
        if (fileType.equals(MARK.code())) {
            linkedFileTypes.add(DIVISIONAL_MARK.code());
        }
        if (fileType.equals(INTERNATIONAL_MARK_I.code())) {
            linkedFileTypes.add(INTERNATIONAL_MARK_R.code());
            linkedFileTypes.add(INTERNATIONAL_MARK_B.code());
        }
        return linkedFileTypes;
    }

    public static List<String> getFileTypesWithPublicUrl() {
        return Arrays.asList(MARK.code(),
                DIVISIONAL_MARK.code(),
                GEOGRAPHICAL_INDICATIONS.code(),
                SPC.code(),
                EU_PATENT.code(),
                PATENT.code(),
                UTILITY_MODEL.code(),
                DESIGN.code(),
                DIVISIONAL_DESIGN.code(),
                PLANTS_AND_BREEDS.code());
    }

    public static List<String> getAllFileTypes() {
        return Arrays.stream(FileType.values()).map(en -> en.code).collect(Collectors.toList());
    }

}
