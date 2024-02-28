package bg.duosoft.ipas.core.model.person;

import bg.duosoft.ipas.enums.PartnershipType;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CPartnershipType implements Serializable {
    private PartnershipType type;
    private String name;
    private String nameEn;
}


