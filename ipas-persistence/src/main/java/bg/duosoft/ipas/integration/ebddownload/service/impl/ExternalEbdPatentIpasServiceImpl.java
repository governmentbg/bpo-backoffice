package bg.duosoft.ipas.integration.ebddownload.service.impl;

import _int.wipo.ipas.ipasservices.IpasServices;
import bg.bpo.ebd.ebddpersistence.entity.EbdDHistory;
import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.bpo.ebd.ebddpersistence.entity.EbdDPublication;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.service.nomenclature.LawService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.EuPatentReceptionType;
import bg.duosoft.ipas.integration.ebddownload.config.EbddownloadConfigurations;
import bg.duosoft.ipas.integration.ebddownload.converter.ExternalEbddownloadToIpasPatentConvertor;
import bg.duosoft.ipas.services.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: Georgi
 * Date: 15.6.2020 г.
 * Time: 16:00
 */
@Service
@Slf4j
public class ExternalEbdPatentIpasServiceImpl {
    @Autowired
    private IpasActionService ipasActionService;
    @Autowired
    private IpasSqlService ipasSqlService;
    @Autowired
    private ExternalEbddownloadToIpasPatentConvertor ebddownloadToIpasPatentConvertor;
    @Autowired
    private EbddownloadConfigurations ebddownloadConfigurations;


    public void processExternalIpasReception(EbdDPatent ebdDPatent, CFileId fileId, CReception receptionForm) throws IpasServiceException {
        insertPublicationsAsNoteActions(ebdDPatent.getPublications(), fileId);
        insertHistoryAsActions(ebdDPatent.getHistories(), fileId);
        String finalActionType = getSpecialFinalActionType(receptionForm);
        if (Objects.nonNull(finalActionType)){
            insertSpecialFinalAction(ebdDPatent, fileId, finalActionType);
        }
    }
    private IpasFileNumber generateIpasFileNumber(CFileId cFileId) {
        return new IpasFileNumber(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries(), cFileId.getFileNbr());
    }
    private void insertPublicationsAsNoteActions(Set<EbdDPublication> publications, CFileId fileId) throws IpasServiceException {
        log.trace("Inserting publications as notes..");
        if (publications != null) {
            int cnt = 1;
            for (EbdDPublication p : publications) {
                InsertActionRequest rq = ebddownloadToIpasPatentConvertor.createInsertAction(p, generateIpasFileNumber(fileId));
                ipasActionService.insertNoteAction(rq);
                log.trace(String.valueOf(cnt++));
            }
        }
        log.trace("End of inserting publication notes");
    }
    /**
     * @param histories
     * @param fileId
     * @return - warnings after insertng (if any). If there is a normal action for a given history record, an attempt to be inserted is made. If there is a problem inserting it, a normal action is getting inserted, and a warning is generated!
     * @throws IpasServiceException
     */
    private List<String> insertHistoryAsActions(Set<EbdDHistory> histories, CFileId fileId) throws IpasServiceException {
        List<String> warnings = new ArrayList<>();
        log.trace("Inserting history actions..");
        if (histories != null) {
            int cnt = 1;
            for (EbdDHistory h : histories) {
                insertHistoryAction(h, generateIpasFileNumber(fileId), null);
                log.trace(cnt++ + "");
            }
        }
        log.trace("End of inserting history actions");
        return warnings.size() == 0 ? null : warnings;
    }

    private String insertHistoryAction(EbdDHistory h, IpasFileNumber patent, Date manualDueDate) throws IpasServiceException {
        if (!ebddownloadConfigurations.hasAnyActionType(h.getIdoper()))
            throw new RuntimeException("Unknown action type for idoper = " + h.getIdoper());

        String res = null;
        try {
            if (ebddownloadConfigurations.hasNormalActionType(h.getIdoper())) {
                //trying to insert normal action...
                InsertActionRequest a = ebddownloadToIpasPatentConvertor.createInsertNormalAction(h, patent, manualDueDate);
                ipasActionService.insertAction(a);
            }
        } catch (Exception e) {
            log.warn("Cannot insert normal action for operation " + h.getIdoper(), e);
            res = "Cannot insert normal action for operation " + h.getIdoper();
        }

        if (ebddownloadConfigurations.hasNoteActionType(h.getIdoper())) {
            InsertActionRequest a = ebddownloadToIpasPatentConvertor.createInsertNoteAction(h, patent);
            ipasActionService.insertNoteAction(a);
        }
        return res;

    }

    private String getSpecialFinalActionType(CReception reception) {
        EuPatentReceptionType t = reception.getEuPatent().getUserdocType() == null ? null : EuPatentReceptionType.selectByCode(reception.getEuPatent().getUserdocType());
        if (t == null) {
            return null;
        }
        switch (t) {
            case VALIDATION:
                return IpasActionService.ACTION_TYPE_EUROPEAN_PATENT_REQUEST_FOR_VALIDATION_CODE;
            case TEMPORARY_PROTECTION:
                return IpasActionService.ACTION_TYPE_EUROPEAN_PATENT_REQUEST_FOR_TEMPORARY_PROTECTION_CODE;
            default:
                throw new RuntimeException("Cannot generate special final action for userdocType: " + t);
        }
    }
    private void insertSpecialFinalAction(EbdDPatent ebdDPatent, CFileId fileId, String actionType) throws IpasServiceException {
        log.trace("Inserting special final status...");
        InsertActionRequest specialFinalAction = ebddownloadToIpasPatentConvertor.generateSpecialFinalAction(ebdDPatent, generateIpasFileNumber(fileId), actionType);
        ipasActionService.insertSpecialAction(specialFinalAction);
        log.debug("Trying to reset responsible user to null, if possible");
        String sql = "UPDATE p\n" +
                "SET RESPONSIBLE_USER_ID = null\n" +
                "from IP_PROC p\n" +
                "join CF_STATUS s on s.STATUS_CODE = p.STATUS_CODE and s.PROC_TYP = p.PROC_TYP\n" +
                "where FILE_SEQ = '{FILE_SEQ}' and FILE_TYP = '{FILE_TYP}' and FILE_SER = {FILE_SER} and FILE_NBR = {FILE_NBR} and IND_RESPONSIBLE_REQ = 'N'";
        Map<String, String> params = Map.of("FILE_SEQ", fileId.getFileSeq(), "FILE_TYP", fileId.getFileType(), "FILE_SER", fileId.getFileSeries().toString(), "FILE_NBR", fileId.getFileNbr().toString());
        ipasSqlService.executeSql(sql, params);//polzvam pak Ipas-koto API za execute-vane na SQLa, tyj kato i insert-a minava prez nego.


        log.trace("End of inserting special final status");
    }
}
