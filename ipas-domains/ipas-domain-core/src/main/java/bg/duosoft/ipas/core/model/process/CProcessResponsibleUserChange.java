package bg.duosoft.ipas.core.model.process;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CProcessResponsibleUserChange implements Serializable {
    private String procTyp;
    private Integer procNbr;
    private Integer changeNbr;
    private Integer userChanged;
    private Date dateChanged;
    private Integer oldResponsibleUserId;
    private Integer newResponsibleUserId;
    private Integer status;
    private Date abdocsUserTargetingProcessedDate;
}