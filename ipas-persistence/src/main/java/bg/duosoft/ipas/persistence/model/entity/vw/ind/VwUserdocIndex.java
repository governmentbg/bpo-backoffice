package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.util.search.IpDocPKBridge;
import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.IpProcBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 09.03.2021
 * Time: 14:27
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "VW_USERDOC_INDEX", schema = "EXT_CORE")
@Cacheable(value = false)
public class VwUserdocIndex implements Serializable, VwIndex {
    @EmbeddedId
    private IpDocPK pk;


    private IpFilePK filePk;

    @Column(name = "EXTERNAL_SYSTEM_ID")
    private String externalSystemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    private Date filingDate;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    private IpProcPK procPk;

    @Column(name = "USERDOC_TYP")
    private String userdocTyp;
}
