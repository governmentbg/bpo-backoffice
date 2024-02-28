package bg.duosoft.ipas.enums;

import java.util.Arrays;
import java.util.Objects;

public enum GeographicalIndType {

    GO_INDICATION("ГО", "ГУ","Geographic indication"),
    ORIGIN("ГО", "НП","Origin name");


    GeographicalIndType(String applTyp, String applSubTyp,String description) {
        this.applTyp = applTyp;
        this.applSubTyp = applSubTyp;
        this.description =description;
    }

    private String applTyp;
    private String applSubTyp;
    private String description;

    public String applTyp() {
        return applTyp;
    }
    public String applSubTyp() {
        return applSubTyp;
    }
    public String description() {
        return description;
    }


    public static GeographicalIndType selectByDescription(String description){
        GeographicalIndType geographicalIndType = Arrays.stream(GeographicalIndType.values())
                .filter(c -> c.description.equals(description))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(geographicalIndType))
            throw new RuntimeException("Cannot find GeographicalIndType with description: " + description);

        return geographicalIndType;
    }

}
