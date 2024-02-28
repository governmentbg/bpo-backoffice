package bg.duosoft.ipas.core.model.acp;

import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.person.CPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAcpPersonsData implements Serializable {
    private CRepresentationData representationData;
    private CPerson servicePerson;
    private CPerson infringerPerson;
}
