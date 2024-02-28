package bg.duosoft.ipas.core.model.reception;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class CReceptionUserdoc implements Serializable {

    private CFileId fileId;//if the userdoc is linked directly to the ip object
    private CDocumentId documentId;//if the userdoc is linked to another userdoc
    private String userdocType;
    private String externalRegistrationNumber;
    private boolean withoutCorrespondents;
    private List<CUserdocPerson> userdocPersons = new ArrayList<>();

    public boolean isRelatedToFile() {
        return fileId != null && documentId == null;
    }

    public boolean isRelatedToUserdoc() {
        return fileId == null && documentId != null;
    }

    public String getFileNumberOrDocumentNumber() {
        return isRelatedToFile() ? fileId.createFilingNumber() : documentId.createFilingNumber();
    }

    public void addPerson(CUserdocPerson p) {
        if (userdocPersons == null) {
            userdocPersons = new ArrayList<>();
        }
        userdocPersons.add(p);
    }
    public List<CUserdocPerson> getPersonsByRole(UserdocPersonRole role) {
        return userdocPersons == null ? null : userdocPersons.stream().filter(r -> r.getRole() == role).collect(Collectors.toList());
    }

}
