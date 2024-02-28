package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.file.FileMapper;
import bg.duosoft.ipas.core.mapper.patent.DrawingMapper;
import bg.duosoft.ipas.core.mapper.patent.PatentMapper;
import bg.duosoft.ipas.core.mapper.patent.PatentRelationshipsMapperHelper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.*;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionEuPatent;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.model.util.CEuPatentsReceptionIds;
import bg.duosoft.ipas.core.service.file.FileRelationshipsService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.impl.IntellectualPropertyServiceImpl;
import bg.duosoft.ipas.core.service.impl.reception.ReceptionResponseWrapper;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.core.service.patent.PatentIpcClassesService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.InternalReceptionService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.patent.PatentInsertionValidator;
import bg.duosoft.ipas.core.validation.patent.PatentRecordalAuthorizationValidator;
import bg.duosoft.ipas.core.validation.patent.PatentValidator;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpUserodocEFilingData;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLawApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.patent_data.IpUserdocPatentData;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.doc.IpDocRepository;
import bg.duosoft.ipas.persistence.repository.entity.efiling.UserdocEfilingDataRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawApplicationSubTypeRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentDrawingRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionRequestRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.UserdocPatentDataRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.patent.PatentUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class PatentServiceImpl extends IntellectualPropertyServiceImpl implements PatentService {

    @Autowired
    private UserdocPatentDataRepository userdocPatentDataRepository;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private ProcessService processService;

    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private PatentMapper patentMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private IpFileRepository ipFileRepository;

    @Autowired
    private ReceptionRequestRepository receptionRequestRepository;

    @Autowired
    private IpPatentDrawingRepository ipPatentDrawingRepository;

    @Autowired
    private PatentIpcClassesService patentIpcClassesService;

    @Autowired
    private DrawingMapper drawingMapper;

    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private InternalReceptionService internalReceptionService;

    @Autowired
    private LogChangesService logChangesService;

    @Autowired
    private FileIdMapper fileIdMapper;

    @Autowired
    private UserdocEfilingDataRepository userdocEfilingDataRepository;

    @Autowired
    private IpDocRepository ipDocRepository;

    @Autowired
    private FileRelationshipsService fileRelationshipsService;

    @Autowired
    private IpUserdocRepository ipUserdocRepository;

    @Autowired
    private PatentRelationshipsMapperHelper patentRelationshipsMapperHelper;

    @Autowired
    private EbdPatentService ebdPatentService;

    @Override
    public CPatent findPatent(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean loadFileContent) {
        IpFilePK pk = new IpFilePK(fileSeq, fileTyp, fileSer, fileNbr);
        IpPatent ipPatent = getIpPatent(pk);
        if (Objects.isNull(ipPatent))
            return null;

        return toPatent(ipPatent, loadFileContent);
    }

    @Override
    public CPatent findPatent(CFileId id, boolean loadFileContent) {
        if (Objects.isNull(id))
            return null;

        return findPatent(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr(), loadFileContent);
    }

    @Override
    @IpasValidatorDefinition({
            PatentValidator.class
    })
    public synchronized void updatePatent(CPatent patent) {
        proceedUpdatePatent(patent);
    }

    @Override
    @IpasValidatorDefinition({
            PatentRecordalAuthorizationValidator.class
    })
    public void updatePatentOnUserdocAuthorization(CPatent patent) {
        proceedUpdatePatent(patent);
    }

    private void proceedUpdatePatent(CPatent patent) {
        //original core patenta se chete predi fillPatentCommonData, za da ne se pravi obry6tenie kym bazata danni, tyj kato fillPatentCommonData detach-va chast ot IpPatent-a i ako tozi red se premesti otdolu, shte se napravi novo obry6tenie kym bazata za da go select-ne!!!!!!!
        CPatent originalCorePatent = patentMapper.toCore(getIpPatent(fileIdMapper.toEntity(patent.getFile().getFileId())), true);

        // Init common patent data.
        IpPatent patentToBeUpdated = fillPatentCommonData(patent);

        //Init persons
        mergeOrInsertPersons(patentToBeUpdated);
        //Check if the relationship has been changed by the other object in this relationship. If so then break relationship.
        checkRelationships1OnPatentUpdate(patentToBeUpdated);

        entityManager.merge(patentToBeUpdated);
        log.debug(String.format("Updated patent with pk = %s ", patent.getFile().getFileId()));

        // Patents and Eu patents - update spc files related to this type of objects
        if (patentToBeUpdated.getFile().getPk().getFileTyp().equals(FileType.PATENT.code()) || patentToBeUpdated.getFile().getPk().getFileTyp().equals(FileType.EU_PATENT.code())) {
            editSpcListOnPatentUpdate(patentToBeUpdated);
            log.debug(String.format("Updated all spc objects related to patent with pk = %s ", patent.getFile().getFileId()));
        }
        logChangesService.insertPatentLogChanges(originalCorePatent, patent);
        log.debug(String.format("Updated mark with pk = %s ", patent.getFile().getFileId()));
    }

    private void checkRelationships1OnPatentUpdate(IpPatent patentToBeUpdated) {
        List<IpFileRelationship> ipFileRelationships1 = new ArrayList<>();
        patentToBeUpdated.getFile().getIpFileRelationships1().stream().forEach(relationship1 -> {
            IpFilePK pk = new IpFilePK(relationship1.getPk().getFileSeq2(), relationship1.getPk().getFileTyp2(), relationship1.getPk().getFileSer2(), relationship1.getPk().getFileNbr2());
            IpPatent relationshipPatent = getIpPatent(pk);
            relationshipPatent.getFile().getIpFileRelationships2().stream().forEach(relationship2 -> {
                if (relationship1.getPk().equals(relationship2.getPk())) {
                    ipFileRelationships1.add(relationship1);
                }
            });
        });
        patentToBeUpdated.getFile().setIpFileRelationships1(ipFileRelationships1);
    }


    private void removeIpcClassesFromSpc(IpPatent spcToBeUpdated,IpPatent patentToBeUpdated) {
        spcToBeUpdated.getIpPatentIpcClasses().removeIf(ipc -> {
            boolean ipcRemove = true;
            for (IpPatentIpcClasses ipPatentIpcClass : patentToBeUpdated.getIpPatentIpcClasses()) {
                if (ipPatentIpcClass.getCfClassIpc().equals(ipc.getCfClassIpc())) {
                    ipcRemove = false;
                    break;
                }
            }
            return ipcRemove;
        });

    }

    private void removeCpcClassesFromSpc(IpPatent spcToBeUpdated,IpPatent patentToBeUpdated) {
        spcToBeUpdated.getIpPatentCpcClasses().removeIf(cpc -> {
            boolean cpcRemove = true;
            for (IpPatentCpcClasses ipPatentCpcClass : patentToBeUpdated.getIpPatentCpcClasses()) {
                if (ipPatentCpcClass.getCfClassCpc().equals(cpc.getCfClassCpc())) {
                    cpcRemove = false;
                    break;
                }
            }
            return cpcRemove;
        });

    }

    private void editSpcListOnPatentUpdate(IpPatent patentToBeUpdated) {
        if (!Objects.isNull(patentToBeUpdated.getFile().getIpFileRelationships1()) && !CollectionUtils.isEmpty(patentToBeUpdated.getFile().getIpFileRelationships1())) {
            patentToBeUpdated.getFile().getIpFileRelationships1().stream().forEach(r -> {
                // Check if relationship is SPC type
                if (r.getRelationshipType().getRelationshipTyp().equals(RelationshipType.SPC_MAIN_PATENT_TYPE)) {
                    IpFilePK pk = new IpFilePK(r.getPk().getFileSeq2(), r.getPk().getFileTyp2(), r.getPk().getFileSer2(), r.getPk().getFileNbr2());
                    IpPatent spcToBeUpdated = getIpPatent(pk);
                    CPatent originalSpc = patentMapper.toCore(spcToBeUpdated, true);
                    //Remove ipc classes from SPCs if ipc class is not contained in main patent
                    removeIpcClassesFromSpc(spcToBeUpdated,patentToBeUpdated);
                    //Remove cpc classes from SPCs if ipc class is not contained in main patent
                    removeCpcClassesFromSpc(spcToBeUpdated,patentToBeUpdated);
                    //Calculate expiration date for SPC

                    if (Objects.isNull(spcToBeUpdated.getEntitlementDate()) && Objects.isNull(spcToBeUpdated.getExpirationDate())) {
                        Optional<CfLawApplicationSubtype> cfLawApplicationSubtypeOptional = cfLawApplicationSubTypeRepository.findByLawCodeApplicationTypeAndSubtype(spcToBeUpdated.getFile().getLawCode(), spcToBeUpdated.getFile().getApplTyp(), spcToBeUpdated.getFile().getApplSubtyp());
                        if (cfLawApplicationSubtypeOptional.isEmpty()) {
                            spcToBeUpdated.setExpirationDate(null);
                        } else {
                            CfLawApplicationSubtype cfLawApplicationSubtype = cfLawApplicationSubtypeOptional.get();
                            Integer registrationYear = cfLawApplicationSubtype.getRegistrationYear();
                            if (Objects.isNull(registrationYear)) {
                                spcToBeUpdated.setExpirationDate(null);
                            }
                            spcToBeUpdated.setExpirationDate(DateUtils.convertToDate(DateUtils.convertToLocalDatTime(patentToBeUpdated.getExpirationDate()).plusYears(registrationYear)));
                        }
                        spcToBeUpdated.setEntitlementDate(patentToBeUpdated.getExpirationDate());
                    }

                    // Set more fields
                    spcToBeUpdated.setTitle(patentToBeUpdated.getTitle());
                    spcToBeUpdated.setEnglishTitle(patentToBeUpdated.getEnglishTitle());
                    CPatent changedSpc = patentMapper.toCore(spcToBeUpdated, true);//changedSpc is getting created right before the rowVersion update. If only the rowVersion is changed, no logChange record will be generated!!!

                    spcToBeUpdated.setRowVersion(spcToBeUpdated.getRowVersion() + 1);
                    entityManager.merge(spcToBeUpdated);

                    logChangesService.insertPatentLogChanges(originalSpc, changedSpc);
                }
            });
        }

    }

    protected IpPatent fillPatentCommonData(CPatent patent) {
        log.debug("Trying to update patent with pk = " + patent.getFile().getFileId());
        IpPatent patentToBeUpdated = patentMapper.toEntity(patent, true);
        IpPatent originalPatent = getIpPatent(patentToBeUpdated.getPk());
        if (Objects.isNull(originalPatent))
            throw new RuntimeException("Patent does not exist...");

        validateIpObjectRowVersion(originalPatent, patentToBeUpdated);

        entityManager.detach(originalPatent);
        IpFile file = originalPatent.getFile();
        patentToBeUpdated.setFile(file);
        fileMapper.fillIpFileFields(patent.getFile(), file);
        patentRelationshipsMapperHelper.extendedRelationshipToFileRelationship(patent.getRelationshipExtended(), patentToBeUpdated, fileService);
        patentToBeUpdated.setDocLog(file.getIpDoc().getPk().getDocLog());
        patentToBeUpdated.setDocNbr(file.getIpDoc().getPk().getDocNbr());
        patentToBeUpdated.setDocSer(file.getIpDoc().getPk().getDocSer());
        incrementIpObjectRowVersion(patentToBeUpdated);
        return patentToBeUpdated;
    }

    @Override
    @IpasValidatorDefinition({
            PatentValidator.class,
            PatentInsertionValidator.class
    })
    public synchronized void insertPatent(CPatent patent) throws IpasValidationException {
        insertPatentAfterValidation(patent);
    }

    protected void insertPatentAfterValidation(CPatent patent) {
        log.debug("Inserting new patent with pk = " + patent.getFile().getFileId());
        IpPatent ipPatent = preparePatentReception(patent);
        IpFilePK pk = ipPatent.getFile().getPk();
        ipPatentRepository.save(ipPatent);
        receptionRequestRepository.updateReceptionRequestStatus(pk.getFileSeq(), pk.getFileTyp(), pk.getFileSer(), pk.getFileNbr());
        log.debug(String.format("Inserted new patent with pk = %s ", patent.getFile().getFileId()));
    }

    @Override
    @IpasValidatorDefinition(PatentInsertionValidator.class)
    public synchronized CPatent insertEuPatent(CPatent patent) throws IpasValidationException {
        log.debug("Inserting new eu patent with pk = " + patent.getFile().getFileId());
        IpPatent ipPatent = preparePatentReception(patent);
        IpPatent result = ipPatentRepository.save(ipPatent);
        log.debug(String.format("Inserted new eu patent with pk = %s ", patent.getFile().getFileId()));
        return patentMapper.toCore(result, true);
    }

    @Override
    public CDrawing selectDrawing(CFileId cFileId, Integer drawingNumber) {
        if (Objects.isNull(cFileId) || null == drawingNumber) {
            return null;
        }
        IpPatentDrawingsPK pk = new IpPatentDrawingsPK(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries(), cFileId.getFileNbr(), drawingNumber);
        IpPatentDrawings ipPatentDrawings = ipPatentDrawingRepository.findById(pk).orElse(null);
        if (Objects.isNull(ipPatentDrawings)) {
            return null;
        }
        return drawingMapper.toCore(ipPatentDrawings, true);
    }


    private IpPatent preparePatentReception(CPatent patent) {
        IpPatent patentToBeSaved = patentMapper.toEntity(patent, true);
        patentToBeSaved.setRowVersion(DefaultValue.ROW_VERSION);

        IpFile originalFile = ipFileRepository.findById(patentToBeSaved.getFile().getPk()).orElse(null);

        validateFileRowVersion(originalFile, patentToBeSaved.getFile());
        entityManager.detach(originalFile);

        CProcessId processId = new CProcessId(originalFile.getProcTyp(), originalFile.getProcNbr());
        patent.getFile().setProcessId(processId);

        patentToBeSaved.setFile(originalFile);
        fileMapper.fillIpFileFields(patent.getFile(), originalFile);
        patentToBeSaved.setDocLog(originalFile.getIpDoc().getPk().getDocLog());
        patentToBeSaved.setDocNbr(originalFile.getIpDoc().getPk().getDocNbr());
        patentToBeSaved.setDocSer(originalFile.getIpDoc().getPk().getDocSer());

        Date captureDate = new Date();
        Integer captureUserId = SecurityUtils.getLoggedUserId();
        patentToBeSaved.getFile().setCaptureDate(captureDate);
        patentToBeSaved.getFile().setCaptureUserId(captureUserId);
        patentToBeSaved.setCaptureDate(captureDate);
        patentToBeSaved.setCaptureUser(new IpUser(captureUserId));

        incrementIpFileRowVersion(patentToBeSaved.getFile());
        mergeOrInsertPersons(patentToBeSaved);
        return patentToBeSaved;
    }

    protected void mergeOrInsertPersons(IpPatent patent) {
        super.mergeOrInsertPersons(patent);

        if (patent.getIpPatentInventors() != null) {
            patent.getIpPatentInventors().stream().forEach(this::updateIpPersonAddresses);
        }
    }

    private CPatent toPatent(IpPatent Patent, boolean loadFileContent) {
        return patentMapper.toCore(Patent, loadFileContent);
    }

    private IpPatent getIpPatent(IpFilePK pk) {
        IpPatent ipPatent = ipPatentRepository.findById(pk).orElse(null);
        return ipPatent;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public boolean isPatentExists(CFileId id) {
        Integer count = ipPatentRepository.countById(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr());
        return !(Objects.isNull(count) || 0 == count);
    }

    @Override
    public void updateRowVersion(CFileId id) {
        if (Objects.nonNull(id)) {
            ipPatentRepository.updateRowVersion(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr());
        }
    }

    public void deletePatent(CFileId fileId) {
        deletePatent(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
    }

    @Override
    public void deletePatent(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        ipPatentRepository.deletePatent(fileSeq, fileType, fileNbr, fileSer);
    }

    private void setEntitlementDateOnAcceptPatent(CPatent patent) {
        if (Objects.nonNull(patent.getFile().getRegistrationData()) && Objects.isNull(patent.getFile().getRegistrationData().getEntitlementDate())) {
            patent.getFile().getRegistrationData().setEntitlementDate(fileRelationshipsService.getPatentLikeObjectEntitlementDateFromRelationships(patent.getFile().getRelationshipList(), patent.getRelationshipExtended(), patent.getFile().getFilingData().getFilingDate()));
        }
    }

    @Override
    public void saveEuPatentFromRelationshipIfMissing(CPatent patent) {
        if (!CollectionUtils.isEmpty(patent.getFile().getRelationshipList())) {
            for (CRelationship relationship : patent.getFile().getRelationshipList()) {
                if (relationship.getRelationshipRole().equals(RelationshipDirection.RELATIONSHIP_CONTAINS_SUPER_OBJECTS_ROLE)
                        && relationship.getFileId().getFileType().equals(FileType.EU_PATENT.code())
                        && (relationship.getRelationshipType().equals(RelationshipType.PARALLEL_PATENT_TYPE)
                        || relationship.getRelationshipType().equals(RelationshipType.TRANSFORMED_NATIONAL_PATENT_TYPE))) {
                    boolean fileExist = fileService.isFileExist(relationship.getFileId());
                    if (!fileExist) {
                        CEbdPatent euPatent = ebdPatentService.selectByFileNumber(relationship.getFileId().getFileNbr());
                        if (Objects.nonNull(euPatent)) {
                            ebdPatentService.save(euPatent, true);
                        }
                    }
                }
            }
        }

    }

    /**
     * za receptionResponseWrapper vij komentara na {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)}
     *
     * @param patent
     * @param attachments
     * @param userdocReceptions
     * @param receptionResponseWrapper
     */


    private void fillSpcIpcClassesOnAccept(CPatent patent) {
        List<CRelationship> relationshipList = patent.getFile().getRelationshipList();
        if (!CollectionUtils.isEmpty(relationshipList) && patent.getFile().getFileId().getFileType().equals(FileType.SPC.code())) {
            List<CPatentIpcClass> spcIpcClasses = new ArrayList<>();
            for (CRelationship relationship : relationshipList) {
                if (relationship.getRelationshipType().equals(RelationshipType.SPC_MAIN_PATENT_TYPE)) {
                    CFileId mainPatentId = relationship.getFileId();
                    List<CPatentIpcClass> mainPatentIpcList = patentIpcClassesService.findByObjectId(mainPatentId.getFileSeq(), mainPatentId.getFileType(), mainPatentId.getFileSeries(), mainPatentId.getFileNbr());
                    if (!CollectionUtils.isEmpty(mainPatentIpcList)) {
                        spcIpcClasses.addAll(mainPatentIpcList);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(spcIpcClasses)) {
                patent.getTechnicalData().setIpcClassList(spcIpcClasses);
            }

        }
    }

    public void acceptPatent(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception receptionPatent = constructPatentReceptionObject(patent, attachments, userdocReceptions, true);
        CReceptionResponse patentReceptionResponse = internalReceptionService.createReception(receptionPatent);
        receptionResponseWrapper.setReceptionResponse(patentReceptionResponse);
        CFile cFileFromReception = fileService.findById(patentReceptionResponse.getFileId());
        patent.getFile().setFileId(patentReceptionResponse.getFileId());
        patent.getFile().setProcessId(cFileFromReception.getProcessId());
        patent.getFile().setProcessSimpleData(cFileFromReception.getProcessSimpleData());
        patent.getFile().setFilingData(cFileFromReception.getFilingData());
        fillSpcIpcClassesOnAccept(patent);
        transferRegistrationDataFromReceptionToFile(cFileFromReception, patent.getFile());
        setEntitlementDateOnAcceptPatent(patent);
        insertPatent(patent);

    }

    private void fillEpoPatentAttachments(CPatent euDbPatent, CPatent patent) {
        if (Objects.isNull(euDbPatent.getPatentDetails())) {
            euDbPatent.setPatentDetails(new CPatentDetails());
        }
        if (Objects.isNull(euDbPatent.getPatentDetails().getPatentAttachments())) {
            euDbPatent.getPatentDetails().setPatentAttachments(new ArrayList<>());
        }
        if (Objects.nonNull(patent.getPatentDetails()) && !CollectionUtils.isEmpty(patent.getPatentDetails().getPatentAttachments())) {
            for (CPatentAttachment patentAttachment : patent.getPatentDetails().getPatentAttachments()) {
                patentAttachment.setId(PatentUtils.generateAttachmentIdOnAdd(euDbPatent.getPatentDetails().getPatentAttachments(), patentAttachment.getAttachmentType().getId()));
            }
            euDbPatent.getPatentDetails().getPatentAttachments().addAll(patent.getPatentDetails().getPatentAttachments());
        }
    }


    private void updateEuPatentUserdocAfterReception(CDocumentId docId, CPatent patent) {
        IpDocPK ipDocPK = new IpDocPK(docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
        updateUserdocEfilingDataOnEuPatentAccept(ipDocPK, patent.getPatentEFilingData());
        updateUserdocPatentDataOnEuPatentAccept(ipDocPK, patent.getPatentDetails(), patent.getTechnicalData().getTitle());
    }

    private void updateUserdocPatentDataOnEuPatentAccept(IpDocPK ipDocPK, CPatentDetails patentDetails, String title) {
        if (Objects.nonNull(patentDetails)) {
            IpUserdocPatentData ipUserdocPatentData = new IpUserdocPatentData();
            ipUserdocPatentData.setPk(ipDocPK);
            ipUserdocPatentData.setClaimsCount(patentDetails.getClaims());
            ipUserdocPatentData.setDrawingsCount(patentDetails.getDrawings());
            ipUserdocPatentData.setDescriptionPagesCount(patentDetails.getDescriptionPages());
            ipUserdocPatentData.setTitleBg(title);
            userdocPatentDataRepository.save(ipUserdocPatentData);
        }

    }

    private void updateUserdocEfilingDataOnEuPatentAccept(IpDocPK ipDocPK, CEFilingData ceFilingData) {
        IpUserodocEFilingData ipUserodocEFilingData = new IpUserodocEFilingData();
        ipUserodocEFilingData.setPk(ipDocPK);
        ipUserodocEFilingData.setLogUserName(ceFilingData.getLogUserName());
        ipUserodocEFilingData.setEsUserName(ceFilingData.getEsUserName());
        ipUserodocEFilingData.setEsUser(ceFilingData.getEsUser());
        ipUserodocEFilingData.setEsUserEmail(ceFilingData.getEsUserEmail());
        ipUserodocEFilingData.setEsValidFrom(ceFilingData.getEsValidFrom());
        ipUserodocEFilingData.setEsValidTo(ceFilingData.getEsValidTo());
        ipUserodocEFilingData.setEsDate(ceFilingData.getEsDate());
        userdocEfilingDataRepository.save(ipUserodocEFilingData);
    }

    private CFileId euPatentUpdatesAfterReception(CReceptionResponse euPatentReceptionResponse, CPatent patent) {
        CProcess newUserdocProcess = processService.selectUserdocProcess(euPatentReceptionResponse.getDocId(), false);
        CFileId euDbPatentFileId = processService.selectTopProcessFileId(newUserdocProcess.getProcessId());
        CPatent euDbPatent = findPatent(euDbPatentFileId, true);
        boolean copyDataFromNewPatent = true;
        List<CProcess> existingUserdocProcesses = processService.selectSubUserdocProcessesRelatedToIpObjectProcess(euDbPatent.getFile().getProcessId(), false);

        if (!CollectionUtils.isEmpty(existingUserdocProcesses)) {
            for (CProcess existingUserdocProcess : existingUserdocProcesses) {
                if ((existingUserdocProcess.getProcessOriginData().getUserdocType().equals(EuPatentReceptionType.VALIDATION.code())
                        || existingUserdocProcess.getProcessOriginData().getUserdocType().equals(EuPatentReceptionType.TEMPORARY_PROTECTION.code())
                        || existingUserdocProcess.getProcessOriginData().getUserdocType().equals(EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()))
                        && newUserdocProcess.getCreationDate().after(existingUserdocProcess.getCreationDate())) {
                    copyDataFromNewPatent = false;
                    break;
                }
            }
        }

        if (copyDataFromNewPatent) {
            euDbPatent.getFile().setServicePerson(patent.getFile().getServicePerson());
            euDbPatent.getFile().setRepresentationData(patent.getFile().getRepresentationData());
            euDbPatent.setPatentDetails(patent.getPatentDetails());
            euDbPatent.setPatentEFilingData(patent.getPatentEFilingData());
            euDbPatent.getTechnicalData().setTitle(patent.getTechnicalData().getTitle());
        } else {
            fillEpoPatentAttachments(euDbPatent, patent);
        }
        setEntitlementDateOnAcceptPatent(euDbPatent);
        updatePatent(euDbPatent);
        updateEuPatentUserdocAfterReception(euPatentReceptionResponse.getDocId(), patent);
        return euDbPatentFileId;
    }

    /**
     * vij {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za komentar na receptionResponseWrapper
     *
     * @param patent
     * @param attachments
     * @param userdocReceptions
     * @param userdocType
     * @param objectNumber
     * @param receptionResponseWrapper
     * @return
     */
    public CEuPatentsReceptionIds acceptEuPatent(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions, String userdocType, Integer objectNumber, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception receptionEuPatent = constructEuPatentReceptionObject(patent, attachments, userdocReceptions, userdocType, objectNumber);
        CReceptionResponse euPatentReceptionResponse = internalReceptionService.createReception(receptionEuPatent);
        receptionResponseWrapper.setReceptionResponse(euPatentReceptionResponse);
        CEuPatentsReceptionIds euPatentsReceptionIds = new CEuPatentsReceptionIds();
        euPatentsReceptionIds.setEuPatentFileId(euPatentUpdatesAfterReception(euPatentReceptionResponse, patent));
        euPatentsReceptionIds.setFinalUserdocReceptionResponse(euPatentReceptionResponse);
        return euPatentsReceptionIds;
    }


    @Override
    public long count() {
        return ipPatentRepository.count();
    }

    private CReception constructEuPatentReceptionObject(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions, String userdocType, Integer objectNumber) {
        CReception receptionForm = constructPatentReceptionObject(patent, attachments, userdocReceptions, true);
        receptionForm.setEuPatent(new CReceptionEuPatent());
        receptionForm.getEuPatent().setObjectNumber(objectNumber);
        receptionForm.getEuPatent().setUserdocType(userdocType);
        return receptionForm;
    }

    protected CReception constructPatentReceptionObject(CPatent patent, List<CAttachment> attachments, List<CReception> userdocReceptions, boolean registerInDocflowSystem) {
        String applicationType = patent.getFile().getFilingData().getApplicationType();
        if (Objects.nonNull(applicationType)
                && !applicationType.equalsIgnoreCase(ApplTyp.DESIGN_APP_TYPE) && !applicationType.equalsIgnoreCase(ApplTyp.NATIONAL_PATENT_TYPE)
                && !applicationType.equalsIgnoreCase(ApplTyp.INTERNATIONAL_DESIGN_APP_TYPE)
                && !applicationType.equalsIgnoreCase(ApplTyp.UTILITY_MODEL_TYPE) && !applicationType.equalsIgnoreCase(ApplTyp.DIVIDED_NATIONAL_DESIGN) && !applicationType.equalsIgnoreCase(ApplTyp.BREEDS_VARIETIES_TYPE) && !applicationType.equalsIgnoreCase(ApplTyp.SPC_TYPE)) {
            throw new RuntimeException("Unrecognized application typ: " + applicationType);
        }
        CReception receptionForm = new CReception();
        receptionForm.setUserdocReceptions(userdocReceptions);
        receptionForm.setAttachments(attachments);
        receptionForm.setFile(new CReceptionFile());
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(patent.getFile().getFilingData().getFilingDate()));
        receptionForm.getFile().setApplicationType(patent.getFile().getFilingData().getApplicationType());
        receptionForm.getFile().setApplicationSubType(patent.getFile().getFilingData().getApplicationSubtype());
        if (patent.getFile().getFileId() != null && patent.getFile().getFileId().getFileNbr() != null) {
            receptionForm.getFile().setFileId(patent.getFile().getFileId());
        }
        receptionForm.getFile().setTitle(patent.getTechnicalData().getTitle());
        receptionForm.setOriginalExpected(false);
        if (Objects.nonNull(applicationType) && applicationType.equalsIgnoreCase(ApplTyp.DIVIDED_NATIONAL_DESIGN)) {
            receptionForm.setSubmissionType(SubmissionType.DIVIDED.code());
        } else {
            receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
        }

        receptionForm.setRegisterInDocflowSystem(registerInDocflowSystem);
        receptionForm.setOwnershipData(registerInDocflowSystem ? patent.getFile().getOwnershipData() : null);
        receptionForm.setRegisterReceptionRequest(registerInDocflowSystem);
        receptionForm.setRepresentationData(patent.getFile().getRepresentationData());
        return receptionForm;
    }

    @Override
    public boolean isMainEpoPatentRequestForValidation(CDocumentId documentId) {
        return callDocumentFunction(documentId, ipUserdocRepository::isMainEpoPatentRequestForValidation);
    }
}