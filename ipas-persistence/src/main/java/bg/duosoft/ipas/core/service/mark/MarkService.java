package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;


import java.util.List;


public interface MarkService {

    CMark findMark(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean readLogo);

    CMark findMark(CFileId id, boolean readLogo);

    void updateMark(CMark mark) throws IpasValidationException;

    void updateMarkOnUserdocAuthorization(CMark mark) throws IpasValidationException;

    void updateMarkInternal(CMark mark) throws IpasValidationException;

    void insertMark(CMark mark) throws IpasValidationException;

    CLogo selectMarkLogo(CFileId id);

    boolean isMarkExists(CFileId id);

    void updateRowVersion(CFileId id);

    long count();

    List<String> selectInternationalMarkIds();
}