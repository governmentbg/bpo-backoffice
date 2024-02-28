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
@Table(name = "IP_ERROR_LOG", schema = "EXT_CORE")
public class IpErrorLog implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ABOUT")
    private String about;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(name = "CUSTOM_MESSAGE")
    private String customMessage;

    @Column(name = "NEED_MANUAL_FIX")
    private Boolean needManualFix;

    @Column(name = "INSTRUCTION")
    private String instruction;

    @Column(name = "RESOLVED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateResolved;

    @Column(name = "RESOLVED_USERNAME")
    private String usernameResolved;

    @Column(name = "RESOLVED_COMMENT")
    private String commentResolved;

    @Column(name = "PRIORITY")
    private String priority;

}
