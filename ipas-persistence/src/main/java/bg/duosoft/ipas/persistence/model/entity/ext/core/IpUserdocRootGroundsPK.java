package bg.duosoft.ipas.persistence.model.entity.ext.core;


import lombok.*;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IpUserdocRootGroundsPK implements Serializable {

    @Column(name = "ROOT_GROUND_ID")
    private Integer rootGroundId;

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

}
