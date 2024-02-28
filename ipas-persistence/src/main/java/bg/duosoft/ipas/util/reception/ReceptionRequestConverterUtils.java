package bg.duosoft.ipas.util.reception;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionEuPatent;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocRequest;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.SubmissionType;
import bg.duosoft.ipas.util.DefaultValue;

public class ReceptionRequestConverterUtils {

    private static final int STATUS_SAVED_IN_IPAS = 1;

    public static ReceptionUserdocRequest convertToUserdocReceptionRequest(Integer externalId, String externalSystemId, CReception receptionForm,
                                                                           CReceptionResponse receiveUserdocResponse, SubmissionType submissionType, CFileId euPatentFileId) {
        ReceptionUserdocRequest receptionUserdocRequest = new ReceptionUserdocRequest();
        receptionUserdocRequest.setDocOri(receiveUserdocResponse.getDocId().getDocOrigin());
        receptionUserdocRequest.setDocLog(receiveUserdocResponse.getDocId().getDocLog());
        receptionUserdocRequest.setDocSer(receiveUserdocResponse.getDocId().getDocSeries());
        receptionUserdocRequest.setDocNbr(receiveUserdocResponse.getDocId().getDocNbr());
        receptionUserdocRequest.setDocSeqTyp(receiveUserdocResponse.getDocSeqId().getDocSeqType());
        receptionUserdocRequest.setDocSeqNbr(receiveUserdocResponse.getDocSeqId().getDocSeqNbr());
        receptionUserdocRequest.setDocSeqSer(receiveUserdocResponse.getDocSeqId().getDocSeqSeries());
        receptionUserdocRequest.setExternalId(externalId);
        receptionUserdocRequest.setExternalSystemId(externalSystemId);
        receptionUserdocRequest.setOriginalExpected(receptionForm.getOriginalExpected());
        receptionUserdocRequest.setFilingDate(receptionForm.getEntryDate());
        receptionUserdocRequest.setNotes(receptionForm.getNotes());

        String objectNumber = "";
        if (receptionForm.isUserdocRequest()) {
            CReceptionUserdoc userdoc = receptionForm.getUserdoc();
            receptionUserdocRequest.setUserdocType(userdoc.getUserdocType());
            objectNumber = userdoc.getFileNumberOrDocumentNumber();
        } else if (receptionForm.isEuPatentRequest()) {
            CReceptionEuPatent euPatent = receptionForm.getEuPatent();
            receptionUserdocRequest.setUserdocType(euPatent.getUserdocType());
            objectNumber = euPatentFileId.createFilingNumber();
        }

        String[] split = objectNumber.split("/");
        if (split.length != 4)
            throw new RuntimeException("Related object number is wrong !");

        receptionUserdocRequest.setRelatedObjectSeq(split[0]);
        receptionUserdocRequest.setRelatedObjectTyp(split[1]);
        receptionUserdocRequest.setRelatedObjectSer(Integer.valueOf(split[2]));
        receptionUserdocRequest.setRelatedObjectNumber(Integer.valueOf(split[3]));
        receptionUserdocRequest.setSubmissionType(submissionType);

        return receptionUserdocRequest;
    }

    public static ReceptionRequest convertToReceptionRequest(Integer externalId, CReception receptionForm, SubmissionType submissionType, CFileId fileId) {
        ReceptionRequest receptionRequest = new ReceptionRequest();
        receptionRequest.setId(null);
        receptionRequest.setFilingDate(receptionForm.getEntryDate());

        receptionRequest.setName(receptionForm.isFileRequest() ? (receptionForm.getFile().isEmptyTitle() ? DefaultValue.EMPTY_OBJECT_NAME : receptionForm.getFile().getTitle()) : null);

        receptionRequest.setSubmissionType(submissionType);
        receptionRequest.setExternalId(externalId);

        receptionRequest.setFileSeq(fileId.getFileSeq());
        receptionRequest.setFileType(fileId.getFileType());
        receptionRequest.setFileSer(fileId.getFileSeries());
        receptionRequest.setFileNbr(fileId.getFileNbr());
        receptionRequest.setOriginalExpected(receptionForm.getOriginalExpected());
        receptionRequest.setStatus(null);
        receptionRequest.setNotes(receptionForm.getNotes());

        return receptionRequest;
    }

}
