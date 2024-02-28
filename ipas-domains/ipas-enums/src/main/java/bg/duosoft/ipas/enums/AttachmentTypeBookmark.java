package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum AttachmentTypeBookmark {
    BIBLIOGRAPHIC_DATA("BIBLIOGRAPHIC_DATA", "Bibliographic data"),
    ABSTRACT("ABSTRACT", "Abstract"),
    DESCRIPTION("DESCRIPTION", "Description"),
    CLAIMS("CLAIMS", "Claims"),
    DRAWINGS("DRAWINGS", "Drawings");

    private String code;
    private String description;

    AttachmentTypeBookmark(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }


    public static AttachmentTypeBookmark selectByCode(String code) {
        AttachmentTypeBookmark bookmark = Arrays.stream(AttachmentTypeBookmark.values())
                .filter(c -> c.code().equals(code))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(bookmark))
            throw new RuntimeException("Cannot find AttachmentTypeBookmark with code: " + code);

        return bookmark;
    }

    public static AttachmentTypeBookmark selectByDescription(String description) {
        AttachmentTypeBookmark bookmark = Arrays.stream(AttachmentTypeBookmark.values())
                .filter(c -> c.description().equals(description))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(bookmark))
            throw new RuntimeException("Cannot find AttachmentTypeBookmark with code: " + description);

        return bookmark;
    }

}
