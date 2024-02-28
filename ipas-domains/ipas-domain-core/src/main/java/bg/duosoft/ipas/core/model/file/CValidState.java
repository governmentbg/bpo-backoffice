package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class CValidState implements Serializable {
    private static final long serialVersionUID = 5966912999585812901L;
    private String countryCode;
    private String stateCode;
    private String stateName;
    private String stateStatus;
    private String stateStatusName;
}


