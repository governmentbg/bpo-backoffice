package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_OFFIDOC_ABDOCS_DOCUMENT", schema = "EXT_CORE")
public class IpOffidocAbdocsDocument implements Serializable {

    @EmbeddedId
    private IpOffidocPK offidocId;

    @Column(name = "ACSTRE_DOCUMENT_ID")
    private String acstreDocumentId;

    @Column(name = "ABDOCS_DOCUMENT_ID")
    private Integer abdocsDocumentId;

    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;

    @Column(name = "CLOSEST_MAIN_PARENT_OBJECT_ID")
    private String closestMainParentObjectId;

    @Column(name = "CLOSEST_MAIN_PARENT_OBJECT_TYPE")
    private String closestMainParentObjectType;

    @Column(name = "DIRECT_PARENT_OBJECT_ID")
    private String directParentObjectId;

    @Column(name = "DIRECT_PARENT_OBJECT_TYPE")
    private String directParentObjectType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NOTIFICATION_FINISHED_DATE")
    private Date notificationFinishedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EMAIL_NOTIFICATION_READ_DATE")
    private Date emailNotificationReadDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PORTAL_NOTIFICATION_READ_DATE")
    private Date portalNotificationReadDate;

}
