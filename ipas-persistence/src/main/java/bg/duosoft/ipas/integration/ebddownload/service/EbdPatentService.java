package bg.duosoft.ipas.integration.ebddownload.service;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatentSearchResult;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;

import java.util.Date;
import java.util.List;

public interface EbdPatentService {

    List<CEbdPatentSearchResult> searchEbdPatents(String fileNumber, String registrationNumber);

    CEbdPatent selectByFileNumber(Integer fileNumber);

    CEbdPatent selectByRegistationNumber(Integer registrationNumber);

    CEbdPatent updateEbdPatentFileNumber(String id, Long fileNumber);

    boolean save(CEbdPatent patent, boolean isAdminRole);
}
