package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.Data;

import java.io.Serializable;

@Data
public class CStatusId implements Serializable {
    private static final long serialVersionUID = 7086502833252679594L;
    private String processType;
    private String statusCode;

}