package bg.duosoft.ipas.core.service.offidoc;


import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.offidoc.COffidocPublishedDecision;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;

public interface OffidocPublishedDecisionService {
    COffidocPublishedDecision findById(COffidocId offidocId, Boolean loadFileContent);

    void update(COffidocId offidocId,  COffidocPublishedDecision offidocDecision);

    void delete(COffidocId offidocId);
}
