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
@Table(name = "CF_SETTLEMENT", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfSettlement implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "municipalityid")
    private Integer municipalityid;

    @Column(name = "districtid")
    private Integer districtid;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "municipalitycode", referencedColumnName = "code")
    private CfMunicipality municipalitycode;

    @ManyToOne
    @JoinColumn(name = "districtcode", referencedColumnName = "code")
    private CfDistrict districtcode;

    @Column(name = "municipalitycode2")
    private String municipalitycode2;

    @Column(name = "districtcode2")
    private String districtcode2;

    @Column(name = "name")
    private String name;

    @Column(name = "typename")
    private String typename;

    @Column(name = "settlementname")
    private String settlementname;

    @Column(name = "typecode")
    private String typecode;

    @Column(name = "mayoraltycode")
    private String mayoraltycode;

    @Column(name = "category")
    private String category;

    @Column(name = "altitude")
    private String altitude;

    @Column(name = "alias")
    private String alias;

    @Column(name = "description")
    private String description;

    @Column(name = "isdistrict")
    private Boolean isdistrict;

    @Column(name = "isactive")
    private Boolean isactive;

    @Column(name = "version")
    private Integer version;

    @Column(name = "settlementnameen")
    private String settlementnameen;

    @Column(name = "postalcode")
    private Integer postalcode;

}
