package bg.duosoft.ipas.core.model.offidoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class COffidocAbdocsDocument implements Serializable {
    private COffidocId offidocId;
    private String acstreDocumentId;
    private Integer abdocsDocumentId;
    private String registrationNumber;
    private String closestMainParentObjectId;
    private String closestMainParentObjectType;
    private String directParentObjectId;
    private String directParentObjectType;
    private Date notificationFinishedDate;
    private Date emailNotificationReadDate;
    private Date portalNotificationReadDate;
}