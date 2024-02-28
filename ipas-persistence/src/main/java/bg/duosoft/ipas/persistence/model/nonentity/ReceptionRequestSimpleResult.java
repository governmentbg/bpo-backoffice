package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionRequestSimpleResult {
    private Integer id;
    private String fileSeq;
    private String fileType;
    private Integer fileSer;
    private Integer fileNbr;
    private Integer externalId;
    private Boolean originalExpected;
    private Date filingDate;
    private String name;
    private String notes;
    private Integer status;
    private Date createDate;
    private String submissionTypeName;
    private String procId;
    private String title;
    private String priorityRequest;
}
