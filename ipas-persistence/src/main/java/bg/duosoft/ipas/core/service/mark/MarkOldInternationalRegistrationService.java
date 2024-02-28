package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.mark.CMarkOldInternationalRegistration;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;

import java.util.List;

public interface MarkOldInternationalRegistrationService {
    List<CMarkOldInternationalRegistration> selectUnprocessed();
    CReceptionResponse insertOldInternationalRegistration(CMarkOldInternationalRegistration oldRegistration, CFile mainMark);
    void save(CMarkOldInternationalRegistration oldRegistration);
}
