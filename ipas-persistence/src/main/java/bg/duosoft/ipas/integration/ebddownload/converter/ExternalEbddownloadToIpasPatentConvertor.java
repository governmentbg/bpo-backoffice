package bg.duosoft.ipas.integration.ebddownload.converter;

import bg.bpo.ebd.ebddpersistence.entity.EbdDHistory;
import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.bpo.ebd.ebddpersistence.entity.EbdDPublication;
import bg.duosoft.ipas.core.service.nomenclature.LawService;
import bg.duosoft.ipas.integration.ebddownload.config.EbddownloadConfigurations;
import bg.duosoft.ipas.services.core.InsertActionRequest;
import bg.duosoft.ipas.services.core.IpasActionService;
import bg.duosoft.ipas.services.core.IpasFileNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Georgi
 * Date: 15.6.2020 г.
 * Time: 16:08
 */
@Component
public class ExternalEbddownloadToIpasPatentConvertor {
    private static final int FIRST_PULICATION_NOSECT = 50;
    private static final int SECOND_PULICATION_NOSECT = 51;
    @Autowired
    private EbddownloadConfigurations ebddownloadConfigurations;


    public InsertActionRequest createInsertAction(EbdDPublication p, IpasFileNumber patentFileId) {

        Calendar actionDate;
        if (p.getDttopubli() != null) {
            actionDate = new GregorianCalendar();
            actionDate.setTime(p.getDttopubli());
        } else {
            actionDate = new GregorianCalendar(p.getYygazette(), p.getNogazette(), 1);
            actionDate.add(Calendar.DATE, -1);
        }
        String notes2 = p.getNogazette() == null ? null : p.getNogazette() + "";
        String notes3 = p.getYygazette() == null ? null : p.getYygazette() + "";
        Integer nosect = null;
        if (p.getNosect() == null || p.getNosect() == 0) {
            if ("1".equals(p.getTypubli())) {
                nosect = FIRST_PULICATION_NOSECT;
            } else if ("2".equals(p.getTypubli())) {
                nosect = SECOND_PULICATION_NOSECT;
            }
        } else {
            nosect = p.getNosect().intValue();
        }
        String notes4 = generatePublicationNotes4(nosect);

        String notes = notes2 + " | " + notes3;
        return new InsertActionRequest(patentFileId, null, null, IpasActionService.ACTION_TYPE_EUROPEAN_PATENT_MIGRATED_PUBLICATION_CODE, actionDate.getTime(), null, notes2, notes3, notes4, null, notes, null, null, null);
    }

    private String generatePublicationNotes4(Integer nosect) {
        String notes4 = null;
        if (nosect != null) {
            String sectionName = ebddownloadConfigurations.getPublicationSectionName(nosect);
            notes4 = nosect + (sectionName == null ? "" : "-" + sectionName);
        }
        return notes4;
    }

    public InsertActionRequest createInsertNormalAction(EbdDHistory h, IpasFileNumber patentFileId, Date manualDueDate) {
        String actionType = ebddownloadConfigurations.getNormalActionTyp(h.getIdoper());
        if (actionType == null) {
            throw new RuntimeException("Unknown action type for idoper = " + h.getIdoper());
        }
        Date actionDate = h.getDtoper();
        Integer actionUser = ebddownloadConfigurations.getIpasUserId(h.getIduseroper());
        return new InsertActionRequest(patentFileId, null, null, actionType, actionDate, null, null, null, null, null, null, null, null, manualDueDate, actionUser, null, null);
    }

    public InsertActionRequest createInsertNoteAction(EbdDHistory h, IpasFileNumber patentFileId) {
        String actionType = ebddownloadConfigurations.getNoteActionTyp(h.getIdoper());
        if (actionType == null) {
            throw new RuntimeException("Unknown action type for idoper = " + h.getIdoper());
        }
        Date actionDate = h.getDtoper();
        String notes1 = h.getOldinfo();
        String notes2 = h.getRmhisto();
        Integer actionUser = ebddownloadConfigurations.getIpasUserId(h.getIduseroper());
        return new InsertActionRequest(patentFileId, null, null, actionType, actionDate, notes1, notes2, null, null, null, null, null, null, actionUser);
    }
    public InsertActionRequest generateSpecialFinalAction(EbdDPatent p, IpasFileNumber patentFileId, String actionType) {
        Date actionDate = new Date();
        Integer responsibleUser = ebddownloadConfigurations.getIpasUserId(p.getIduser());
        if (responsibleUser == null) {
            responsibleUser = 4;//IPASPROD
        }
        String status = ebddownloadConfigurations.getIpasStatus(p.getLgstappli());
        return new InsertActionRequest(patentFileId, null, null, actionType, actionDate, null, null, null, null, null, null, responsibleUser, status, null);
    }

}
