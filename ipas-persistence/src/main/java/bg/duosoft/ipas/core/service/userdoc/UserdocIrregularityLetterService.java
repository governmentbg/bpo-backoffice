package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.util.CAttachment;

import java.util.List;

public interface UserdocIrregularityLetterService {
    CReceptionResponse acceptWipoIrregularityLetters(String parentDocumentNumber, Integer registrationNumber, String registrationDup, CUserdoc userdoc, List<CAttachment> attachments);
}
