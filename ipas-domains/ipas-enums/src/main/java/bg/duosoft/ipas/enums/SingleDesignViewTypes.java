package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum SingleDesignViewTypes {
    GENERAL(1, "General"),
    PERSPECTIVE(2, "Perspective"),
    FRONT(3, "Front"),
    BACK(4, "Back"),
    LEFT(5, "Left"),
    RIGHT(6, "Right"),
    TOP(7, "Top"),
    BOTTOM(8, "Bottom"),
    PARTIAL(9, "Partial");

    SingleDesignViewTypes(Integer viewTypeId, String viewTypeName) {
        this.viewTypeId = viewTypeId;
        this.viewTypeName = viewTypeName;
    }
    private Integer viewTypeId;
    private String viewTypeName;

    public Integer viewTypeId() {
        return viewTypeId;
    }

    public String viewTypeName() {
        return viewTypeName;
    }

    public static Integer selectViewTypeIdByName(String viewTypeName){
        SingleDesignViewTypes singleDesignViewTypes = Arrays.stream(SingleDesignViewTypes.values())
                .filter(v -> v.viewTypeName().equals(viewTypeName))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(singleDesignViewTypes))
            throw new RuntimeException("Cannot find View Type Id with description: " + viewTypeName);

        return singleDesignViewTypes.viewTypeId();
    }
}

