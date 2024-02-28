package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum MarkSignType {
    PATTERN("A", "Десен","Pattern"),
    COLOUR("C", "Комбинация от цветове","Colour"),
    WORD("D", "Словна","Word"),
    OTHER("E", "Други","Other"),
    FIGURATIVE("F", "Фигуративна","Figurative"),
    HOLOGRAM("H", "Холограмна","Hologram"),
    COMBINED("M", "Комбинирана","Combined"),
    POSITION("P", "Позиционна","Position"),
    SOUND("S", "Звукова","Sound"),
    THREE_D("T", "Триизмерна","3-D"),
    MOTION("I", "Съдържаща движение","Motion"),
    MULTIMEDIA("U", "Мултимедийна","Multimedia");

    MarkSignType(String code, String description,String descriptionEn) {
        this.code = code;
        this.description = description;
        this.descriptionEn =descriptionEn;
    }

    private String code;
    private String description;
    private String descriptionEn;

    public String code() {
        return code;
    }
    public String description() {
        return description;
    }
    public String descriptionEn() {
        return descriptionEn;
    }

    public static MarkSignType selectByDescriptionEn(String descriptionEn){
        MarkSignType markSignType = Arrays.stream(MarkSignType.values())
                .filter(c -> c.descriptionEn().equals(descriptionEn))
                .findFirst()
                .orElse(null);
        return markSignType;
    }

    public static MarkSignType selectByCode(String code) {
        MarkSignType markSignType = Arrays.stream(MarkSignType.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(markSignType))
            throw new RuntimeException("Cannot find MarkSignType with code: " + code);

        return markSignType;
    }
}
