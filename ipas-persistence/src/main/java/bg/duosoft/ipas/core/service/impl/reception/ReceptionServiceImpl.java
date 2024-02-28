package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.abdocs.exception.AbdocsServiceException;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
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
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl;
import bg.duosoft.ipas.core.service.impl.patent.DesignServiceImpl;
import bg.duosoft.ipas.core.service.impl.patent.PatentServiceImpl;
import bg.duosoft.ipas.core.service.impl.reception.InternalReceptionServiceImpl;
import bg.duosoft.ipas.core.service.impl.userdoc.UserdocServiceImpl;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 11.08.2021
 * Time: 12:39
 */
@Service
@Slf4j
//ne trqbva da se slaga @Transactional anotacia!!!!

public class ReceptionServiceImpl implements ReceptionService {
    @Autowired
    private MarkServiceImpl markService;
    @Autowired
    private PatentServiceImpl patentService;
    @Autowired
    private DesignServiceImpl designService;
    @Autowired
    private UserdocServiceImpl userdocService;

    @Autowired
    private InternalReceptionServiceImpl receptionService;
    @Autowired
    private AbdocsService abdocsServiceAdmin;
    @Autowired
    private FileService fileService;
    @Autowired
    private DocService docService;
    /*
          tyj kato v tozi klas nqma @Transactional pri izlizane ot {@link MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)}
          se pravi commit na tranzakciqta i ako neshto se schupi,tuk se trie syzdadeniq v acceptTrademark dokument v delovodnata. Za da znam koi dokument e syzdaden v delovodnata
          se polzva obekta ReceptionResponseWrapper, koito se update va acceptTrademark vednaga sled kato se napravi reception!
          Tozi komentar vaji za vsichki acceptXXX methods!!!
     */
    public synchronized CReceptionResponse acceptTrademark(CMark mark, List<CAttachment> attachments) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            markService.acceptTrademark(mark, attachments, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept trademark", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    @Override
    public synchronized CReceptionResponse acceptInternationalTrademark(CMark mark, List<CAttachment> attachments) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            markService.acceptInternationalTrademark(mark, attachments, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept international trademark", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    public synchronized CMark createDividedMark(CFileId orignalFileId, List<COwner> newOwners, List<CNiceClass> newNiceClasses,boolean isUserdocInit) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            return markService.createDividedMark(orignalFileId, newOwners, newNiceClasses,isUserdocInit, rrw);
        } catch (Exception e) {
            log.error("Cannot create divided mark", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }
    @Override
    public synchronized CReceptionResponse acceptPatent(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            patentService.acceptPatent(patent, attachments, userdocReceptions, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept patent", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }


    @Override
    public synchronized CEuPatentsReceptionIds acceptEuPatent(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions, String userdocType, Integer objectNumber) {
        /**
         * pri reception na EP moje da se okaje che patenta ne sy6testvuva v IPAS, togava toj se vyvejda no tova stava prez IPAS Api-to, t.e. v druga tranzakciq
         * zatova pri accept pyrvo se proverqva dali patenta sy6testvuva v IPAS i ako grymne greshka i patenta ne e sy6testvuval, to novosyzdadeniq patent v drugata tranzakciq se iztriva
         * iztriva se i dokumenta v delovodnata sistema.
         */
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        List<CFile> files = fileService.findAllByFileNbrAndFileType(objectNumber, Arrays.asList(FileType.EU_PATENT.code()));
        boolean patentExists = files != null && files.size() == 1;
        try {
            return patentService.acceptEuPatent(patent, attachments, userdocReceptions, userdocType, objectNumber, rrw);
        } catch (Exception e) {
            log.error("Cannot accept eupatent", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            if (!patentExists) {
                files = fileService.findAllByFileNbrAndFileType(objectNumber, Arrays.asList(FileType.EU_PATENT.code()));
                if (files != null && files.size() == 1) {
                    CFileId fileId = files.get(0).getFileId();
                    String externalSystemId = docService.selectExternalSystemId(fileService.selectFileDocumentId(fileId));
                    try {
                        patentService.deletePatent(files.get(0).getFileId());//ako patenta ne sy6testvuva predi reception-a, no sy6testvuva sled tova i nestho e grymnalo, togava trqbva da se iztrie i patenta
                    } catch (Exception ex) {
                        log.error("Cannot delete the patent in ipas " + fileId.createFilingNumber(), ex);
                        e.addSuppressed(ex);
                    }
                    try {
                        Integer documentId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(externalSystemId);
                        if (documentId != null) {
                            abdocsServiceAdmin.deleteDocumentHierarchy(documentId);
                        }
                    } catch (AbdocsServiceException ex) {
                        log.error("Cannot delete master document from abdocs", ex);
                        e.addSuppressed(ex);
                    }
                }

            }
            throw e;
        }
    }

    @Override
    public synchronized CReceptionResponse acceptDesign(CPatent design, List<CPatent> singleDesigns, List<CAttachment> attachments, List<CReception> userdocReceptions, boolean registerInDocflowSystem) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            designService.acceptDesign(design, singleDesigns, attachments, userdocReceptions, registerInDocflowSystem, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept design", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    @Override
    public synchronized CReceptionResponse acceptUserdoc(CUserdoc userdoc, List<CAttachment> attachments, CFileId affectedId, Boolean relateRequestToObject,CDocumentId parentDocumentId) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            userdocService.acceptUserdoc(userdoc, attachments, affectedId,relateRequestToObject,parentDocumentId, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept design", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    @Override
    public synchronized CReceptionResponse acceptInternationalRegistrationUserdoc(CUserdoc parentUserdoc, CAcceptInternationalRegistrationRequest registrationRequest) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            userdocService.acceptInternationalRegistrationUserdoc(parentUserdoc, registrationRequest, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept international registration userdoc", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    @Override
    public synchronized CReceptionResponse acceptAdditionalMadridEfilingUserdoc(CDocumentId parentDocumentId, CUserdoc additionalUserdoc) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            userdocService.acceptAdditionalMadridEfilingUserdoc(parentDocumentId, additionalUserdoc, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept additional madrid efiling userdoc", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    @Override
    public synchronized String acceptUserdocWhenParentIdIsMissing(CUserdoc userdoc, List<CAttachment> attachments, Integer relatedObjectIdentifier) {
        return userdocService.acceptUserdocWhenParentIdIsMissing(userdoc, attachments, relatedObjectIdentifier);
    }

    @Override
    public synchronized CReceptionResponse acceptInternationalMarkUserdoc(CUserdoc userdoc, List<CAttachment> attachments, CFileId affectedId, Boolean relateRequestToObject, CDocumentId parentDocumentId) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            userdocService.acceptInternationalMarkUserdoc(userdoc, attachments, affectedId, relateRequestToObject, parentDocumentId, rrw);
            return rrw.getReceptionResponse();
        } catch (Exception e) {
            log.error("Cannot accept international mark userdoc", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }

    public synchronized CReceptionResponse createReception(CReception reception) throws IpasValidationException {
        return receptionService.createReception(reception);
    }
    public synchronized CPatent createDividedDesign(CFileId fileId, List<CFileId> newSingleDesignIds, List<COwner> newOwners,boolean isUserdocInit) {
        ReceptionResponseWrapper rrw = new ReceptionResponseWrapper();
        try {
            return designService.createDividedDesign(fileId, newSingleDesignIds, newOwners,isUserdocInit, rrw);
        } catch (Exception e) {
            log.error("Cannot create divided design", e);
            deleteAbdocsDocumentHierarchy(e, rrw);
            throw e;
        }
    }


    private void deleteAbdocsDocumentHierarchy(Exception e, ReceptionResponseWrapper rrw) {
        if (rrw.getReceptionResponse() != null && rrw.getReceptionResponse().getDocflowDocumentId() != null) {
            try {
                log.debug("Trying to delete document hierarchy for document: " +rrw.getReceptionResponse().getDocflowDocumentId() + " External system Id:" + rrw.getReceptionResponse().getExternalSystemId());
                abdocsServiceAdmin.deleteDocumentHierarchy(rrw.getReceptionResponse().getDocflowDocumentId());
            } catch (Exception ex) {
                log.error("Document hierarchy is not deleted ! ID: " + rrw.getReceptionResponse().getDocflowDocumentId(), ex);
                e.addSuppressed(ex);
            }
        }
    }
}
