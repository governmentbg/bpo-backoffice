package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_MUNICIPALITY", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfMunicipality implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "districtid")
    private Integer districtid;

    @Column(name = "code2")
    private String code2;

    @Column(name = "mainsettlementcode")
    private String mainsettlementcode;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    @Column(name = "description")
    private String description;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "version")
    private Integer version;

    @Column(name = "nameen")
    private String nameen;
}
