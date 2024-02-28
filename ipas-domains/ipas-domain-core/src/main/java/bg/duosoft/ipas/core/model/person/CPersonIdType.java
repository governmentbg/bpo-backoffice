package bg.duosoft.ipas.core.model.person;

import lombok.Data;

import java.io.Serializable;

@Data
public class CPersonIdType implements Serializable {

    private Integer rowVersion;
    private String personIdTyp;
    private String personIdName;
    private Boolean indGeneralId;
    private Boolean indIndividualId;

}


