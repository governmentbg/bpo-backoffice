package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Raya
 * 23.09.2020
 */
@Entity
@Table(name = "CF_NICE_CLASS_LIST", schema = "EXT_CORE")
@Getter
@Setter
@EqualsAndHashCode
public class CfNiceClassList implements Serializable {

    @Id
    @Column(name = "NICE_CLASS_CODE")
    private Integer niceClassCode;

    @Column(name = "ALPHA_LIST")
    private String alphaList;

    @Column(name = "HEADING")
    private String heading;

}
