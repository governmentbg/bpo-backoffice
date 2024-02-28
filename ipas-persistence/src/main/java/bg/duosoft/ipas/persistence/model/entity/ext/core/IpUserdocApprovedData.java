package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Raya
 * 04.09.2020
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_APPROVED_DATA", schema = "EXT_CORE")
public class IpUserdocApprovedData implements Serializable {

    @EmbeddedId
    private IpDocPK pk;

    @Column(name = "APPROVED_ALL_NICE")
    private String approvedAllNice;


}
