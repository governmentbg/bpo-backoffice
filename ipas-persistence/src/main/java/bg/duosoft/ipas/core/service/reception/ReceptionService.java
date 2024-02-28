package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.international_registration.CAcceptInternationalRegistrationRequest;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.model.util.CEuPatentsReceptionIds;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 11.08.2021
 * Time: 12:40
 */
public interface ReceptionService {

    CReceptionResponse createReception(CReception reception) throws IpasValidationException;

    CReceptionResponse acceptTrademark(CMark mark, List<CAttachment> attachments);

    CReceptionResponse acceptInternationalTrademark(CMark mark, List<CAttachment> attachments);

    CMark createDividedMark(CFileId fileId, List<COwner> newOwners, List<CNiceClass> newNiceClasses,boolean isUserdocInit);

    CReceptionResponse acceptPatent(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions);

    CEuPatentsReceptionIds acceptEuPatent(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions, String userdocType, Integer objectNumber);

    CReceptionResponse acceptDesign(CPatent design, List<CPatent> singleDesigns, List<CAttachment> attachments, List<CReception> userdocReceptions, boolean registerInDocflowSystem);

    CPatent createDividedDesign(CFileId fileId, List<CFileId> newSingleDesignIds, List<COwner> newOwners,boolean isUserdocInit);

    CReceptionResponse acceptUserdoc(CUserdoc userdoc, List<CAttachment> attachments, CFileId affectedId, Boolean relateRequestToObject,CDocumentId parentDocumentId);

    CReceptionResponse acceptInternationalRegistrationUserdoc(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest);

    CReceptionResponse acceptAdditionalMadridEfilingUserdoc(CDocumentId parentDocumentId, CUserdoc additionalUserdoc);

    String acceptUserdocWhenParentIdIsMissing(CUserdoc userdoc, List<CAttachment> attachments,Integer relatedObjectIdentifier);

    CReceptionResponse acceptInternationalMarkUserdoc(CUserdoc userdoc, List<CAttachment> attachments, CFileId affectedId, Boolean relateRequestToObject, CDocumentId parentDocumentId);
}
