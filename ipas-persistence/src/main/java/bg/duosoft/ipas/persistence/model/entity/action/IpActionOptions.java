package bg.duosoft.ipas.persistence.model.entity.action;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_ACTION_OPTIONS", schema = "IPASPROD")
@IdClass(IpActionOptionsPK.class)
@Cacheable(value = false)
public class IpActionOptions implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "PROC_TYP")
    private String procTyp;

    @Id
    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Id
    @Column(name = "ACTION_NBR")
    private Integer actionNbr;

    @Id
    @Column(name = "LIST_CODE")
    private String listCode;

    @Id
    @Column(name = "OPTION_NBR")
    private String optionNbr;

    @Column(name = "ACTION_OPTION_TEXT")
    private String actionOptionText;

}
