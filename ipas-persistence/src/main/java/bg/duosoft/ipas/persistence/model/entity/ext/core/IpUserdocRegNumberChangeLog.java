package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocRegNumberChangeLog implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "OLD_REGISTRATION_NUMBER")
    private String oldRegistrationNumber;

    @Column(name = "NEW_REGISTRATION_NUMBER")
    private String newRegistrationNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    private Date date;

    @Column(name = "USERNAME")
    private String username;

}
