package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.CPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CUserdocMainObjectData implements Serializable {
    private CFileId fileId;
    private List<CPerson> mainObjectOwners;

    public boolean haveRepresentativePersonsInOwnersList() {
        if (Objects.isNull(this.mainObjectOwners) || this.mainObjectOwners.size() < 1)
            return false;

        CPerson existingRepresentative = this.mainObjectOwners.stream()
                .filter(cPerson -> Objects.nonNull(cPerson.getAgentCode()))
                .findFirst()
                .orElse(null);

        return Objects.nonNull(existingRepresentative);
    }

}
