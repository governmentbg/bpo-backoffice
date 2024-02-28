package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CDistrict implements Serializable {

    private Integer id;
    private String code;
    private String code2;
    private String secondlevelregioncode;
    private String name;
    private String mainsettlementcode;
    private String alias;
    private String description;
    private Boolean isactive;
    private Integer version;
    private String nameen;

}
