package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_OLD_DOCUMENT", schema = "EXT_CORE")
public class IpUserdocOldDocument implements Serializable{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_SER")
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    private Integer fileNbr;

    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

    @Column(name = "FILING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filingDate;

    @Column(name = "NEW_USERDOC_TYPE")
    private String newUserdocType;

    @Column(name = "RESPONSIBLE_USER_ID")
    private Integer responsibleUserId;

    @Column(name = "REGISTER_IN_ABDOCS")
    private Boolean registerInAbdocs;
}
