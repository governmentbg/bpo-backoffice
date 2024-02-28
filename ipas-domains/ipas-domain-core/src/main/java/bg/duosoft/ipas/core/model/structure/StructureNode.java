package bg.duosoft.ipas.core.model.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 13:52
 */
@Getter
@Setter
@EqualsAndHashCode
public class StructureNode implements Serializable {
    private OfficeStructureId officeStructureId;

    private String name;

    private Boolean active;

    private CUser signatureUser;

}
