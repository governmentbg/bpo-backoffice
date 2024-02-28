package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CSettlement implements Serializable {

    private Integer id;
    private Integer municipalityid;
    private Integer districtid;
    private String code;
    private CMunicipality municipalitycode;
    private CDistrict districtcode;
    private String municipalitycode2;
    private String districtcode2;
    private String name;
    private String typename;
    private String settlementname;
    private String typecode;
    private String mayoraltycode;
    private String category;
    private String altitude;
    private String alias;
    private String description;
    private Boolean isdistrict;
    private Boolean isactive;
    private Integer version;
    private String settlementnameen;
    private Integer postalcode;

}
