package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LastActionsResult {
    private String fileSeq;
    private String fileType;
    private Integer fileSer;
    private Integer fileNum;
    private String statusCode;
    private String statusName;
    private Date actionDate;
    private String filingNumber;
    private String actionName;
    private String responsibleUserName;
    private String documentNumber;
    private String documentType;
    private String captureUserName;
}
