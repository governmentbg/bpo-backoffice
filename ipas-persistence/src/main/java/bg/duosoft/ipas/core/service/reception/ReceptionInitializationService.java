package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;

public interface ReceptionInitializationService {

    CMark initMark(CFile cFile, CReceptionRequest receptionRequest);

    CPatent initPatent(CFile cFile, CReceptionRequest receptionRequest);

}
