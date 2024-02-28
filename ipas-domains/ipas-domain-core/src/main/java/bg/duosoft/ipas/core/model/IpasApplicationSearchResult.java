package bg.duosoft.ipas.core.model;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.file.CRegistrationData;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpasApplicationSearchResult implements Serializable {
    private CFileId fileId;
    private String description;
    private List<CPerson> owners;
    private List<CRepresentative> representatives;
    private CPerson servicePerson;
    private String status;
    private Date filingDate;
    private CRegistrationData registrationData;
    private CProcessId processId;
    private boolean isFigurative;
    private boolean isPatentInSecretStatus;

    public List<CPerson> getRepresentativePersons(){
        if (Objects.isNull(this.representatives) || this.representatives.size() < 1)
            return null;

        return this.representatives.stream()
                .filter(cRepresentative -> Objects.nonNull(cRepresentative.getPerson()))
                .map(CRepresentative::getPerson)
                .collect(Collectors.toList());
    }
}
