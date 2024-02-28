package bg.duosoft.ipas.core.model.userdoc;

import lombok.Data;

import java.io.Serializable;

@Data
public class CUserdocExtraDataType implements Serializable {
    private String code;
    private String title;
    private String titleEn;
    private Boolean isText;
    private Boolean isNumber;
    private Boolean isDate;
    private Boolean isBoolean;
    private String booleanTextTrue;
    private String booleanTextFalse;
    private String booleanTextTrueEn;
    private String booleanTextFalseEn;
}


