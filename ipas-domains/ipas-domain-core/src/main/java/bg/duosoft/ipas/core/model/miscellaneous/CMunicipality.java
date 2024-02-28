package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CMunicipality implements Serializable {

    private Integer id;
    private String code;
    private Integer districtid;
    private String code2;
    private String mainsettlementcode;
    private String category;
    private String name;
    private String alias;
    private String description;
    private Boolean isactive;
    private Integer version;
    private String nameen;

}
