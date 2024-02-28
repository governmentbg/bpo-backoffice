package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.enums.NextProcessActionType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CNextProcessAction implements Serializable {
    private String actionType;
    private String actionTypeName;
    private Integer automaticActionWcode;
    private String actionTypeGroup;
    private String actionTypeListCode;
    private Integer restrictLawCode;
    private String restrictFileTyp;
    private String restrictApplicationType;
    private String restrictApplicationSubType;
    private String userdocListCodeInclude;
    private String userdocListCodeExclude;
    private String statusCode;
    private String statusName;
    private NextProcessActionType processActionType;
    private Boolean containNotes;
    private Boolean containManualDueDate;
    private Boolean calculateTermFromActionDate;
    private String generatedOffidoc;
}