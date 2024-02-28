package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.mapper.design.SingleDesignMapper;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRelationship;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.impl.IntellectualPropertyServiceImpl;
import bg.duosoft.ipas.core.service.impl.reception.ReceptionResponseWrapper;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.InternalReceptionService;
import bg.duosoft.ipas.core.validation.config.*;
import bg.duosoft.ipas.core.validation.design.DesignValidator;
import bg.duosoft.ipas.core.validation.ipobject.RelationshipsValidator;
import bg.duosoft.ipas.core.validation.patent.PatentInsertionValidator;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.exception.DesignDivisionException;
import bg.duosoft.ipas.persistence.model.entity.design.SingleDesign;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.repository.entity.design.SingleDesignFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.design.SingleDesignRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@LogExecutionTime
public class DesignServiceImpl extends IntellectualPropertyServiceImpl implements DesignService {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private PatentServiceImpl patentService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private FileService fileService;

    @Autowired
    private IpProcRepository procRepository;

    @Autowired
    private SingleDesignRepository singleDesignRepository;


    @Autowired
    private SingleDesignFileRepository singleDesignFileRepository;

    @Autowired
    private SingleDesignMapper singleDesignMapper;

    @Autowired
    private InternalReceptionService internalReceptionService;

    @Autowired
    private IpasValidatorCreator validatorCreator;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LogChangesService logChangesService;

    @Override
    public CPatent findSingleDesign(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean loadFileContent) {
        IpFilePK pk = new IpFilePK(fileSeq, fileTyp, fileSer, fileNbr);
        SingleDesign singleDesign = singleDesignRepository.findById(pk).orElse(null);
        return singleDesignMapper.toCore(singleDesign, loadFileContent);
    }

    public List<CPatent> getAllSingleDesignsForIndustrialDesign(CPatent patent, boolean loadFileContent) {
        List<CRelationship> cRelationshipList = patent.getFile().getRelationshipList();
        List<CPatent> singleDesigns = new ArrayList<>();
        if (!Objects.isNull(cRelationshipList) && !CollectionUtils.isEmpty(cRelationshipList)) {
            cRelationshipList.stream().forEach(r -> {
                if (r.getRelationshipRole().equals("1") && r.getRelationshipType().equals(RelationshipType.DESIGN_TYPE)) {
                    CFileId designId = r.getFileId();
                    CPatent singleDesign = findSingleDesign(designId.getFileSeq(), designId.getFileType(), designId.getFileSeries(), designId.getFileNbr(), loadFileContent);
                    singleDesigns.add(singleDesign);
                }
            });
        }
        return singleDesigns;
    }

    @Override
    @IpasValidatorDefinition({
            DesignValidator.class,
            PatentInsertionValidator.class
    })
    public synchronized void insertDesigns(CPatent design, List<CPatent> singleDesigns) {
        if (Objects.nonNull(singleDesigns) && !CollectionUtils.isEmpty(singleDesigns) && Objects.isNull(singleDesigns.get(0).getFile().getFileId())) {
            singleDesignActionsOnAccept(design, singleDesigns);
        } else {
            singleDesignsUpdates(singleDesigns);
        }
        fillMainDesignRelationshipListWithSingleDesigns(design, singleDesigns);
        patentService.insertPatentAfterValidation(design);
        validateMainDesignRelationships(design);
        executeCommonActionsAfterInsertOrUpdate(design, singleDesigns);
    }


    private void fillDesignParentRelationship(CPatent newDesign, CPatent dividedFromDesign) {
        newDesign.getFile().setRelationshipList(new ArrayList<>());
        CRelationship newRelationship = new CRelationship();
        newRelationship.setRelationshipType(RelationshipType.DIVISIONAL_DESIGN_TYPE);
        newRelationship.setRelationshipRole("2");
        newRelationship.setFileId(dividedFromDesign.getFile().getFileId());
        newDesign.getFile().getRelationshipList().add(newRelationship);
    }


    private boolean validSingleDesignIdsOnDivision(List<CPatent> originalSingleDesigns, List<CFileId> newSingleDesignIds) {
        if (CollectionUtils.isEmpty(newSingleDesignIds)) {
            return false;
        }
        if (CollectionUtils.isEmpty(originalSingleDesigns)) {
            throw new RuntimeException("Create divided design action - parent single designs and newSingleDesignIds are incompatible!");
        }
        newSingleDesignIds.stream().forEach(pk -> {
            CPatent filteredSingleDesign = originalSingleDesigns.stream().
                    filter(originSingleDesign -> originSingleDesign.getFile().getFileId().equals(pk)).findFirst().orElse(null);
            if (Objects.isNull(filteredSingleDesign)) {
                throw new RuntimeException("Create divided design action - parent single designs and newSingleDesignIds are incompatible!");
            }
        });
        return true;
    }


    private void originalDesignUpdatesOnDivision(CPatent originalDesign, List<CPatent> originalSingleDesigns, List<CFileId> newSingleDesignIds) {
        CPatent originalDesignCopy = (CPatent) SerializationUtils.clone(originalDesign);
        List<CPatent> singleDesignsCopy = new ArrayList<>();
        for (CPatent singleDesign : originalSingleDesigns) {
            singleDesignsCopy.add((CPatent) SerializationUtils.clone(singleDesign));
        }
        newSingleDesignIds.stream().forEach(newSingleDesignId -> {
            singleDesignsCopy.removeIf(r -> r.getFile().getFileId().equals(newSingleDesignId));
            originalDesignCopy.getFile().getRelationshipList().removeIf(r -> r.getRelationshipType().equals(RelationshipType.DESIGN_TYPE) && r.getFileId().equals(newSingleDesignId));
        });
        mainDesignUpdate(originalDesignCopy);
        validateMainDesignRelationships(originalDesignCopy);
        logChangesService.insertDesignLogChanges(originalDesign, originalDesignCopy, originalSingleDesigns, singleDesignsCopy);
    }


    private void fillSingleDesignRelationshipsOnDivision(CPatent newDesign, List<CFileId> newSingleDesignIds) {
        newSingleDesignIds.stream().forEach(pk -> {
            CRelationship newRelationship = new CRelationship();
            newRelationship.setRelationshipType(RelationshipType.DESIGN_TYPE);
            newRelationship.setRelationshipRole("1");
            newRelationship.setFileId(pk);
            newDesign.getFile().getRelationshipList().add(newRelationship);
        });
    }

    /**
     * vij komentara v {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za ReceptionResponseWrapper
     * @param fileId
     * @param newSingleDesignIds
     * @param newOwners
     * @param isUserdocInit
     * @param receptionResponseWrapper
     * @return
     */
    public CPatent createDividedDesign(CFileId fileId, List<CFileId> newSingleDesignIds, List<COwner> newOwners,boolean isUserdocInit, ReceptionResponseWrapper receptionResponseWrapper) {
        if (Objects.isNull(fileId)) {
            throw new DesignDivisionException("File ID is mandatory for design division !");
        }

        if (!FileType.DESIGN.code().equals(fileId.getFileType())) {
            throw new DesignDivisionException("Inserted file id is not for design ! File ID: " + fileId);
        }

        CPatent design = patentService.findPatent(fileId, true);
        if (Objects.isNull(design)) {
            throw new DesignDivisionException("Design doesn't exist for ID: " + fileId);
        }

        return createDividedDesign(design, newSingleDesignIds, newOwners,isUserdocInit, receptionResponseWrapper);
    }

    private CPatent createDividedDesign(CPatent originalDesign, List<CFileId> newSingleDesignIds, List<COwner> newOwners,boolean isUserdocInit, ReceptionResponseWrapper receptionResponseWrapper) {

        CPatent newDesign = (CPatent) SerializationUtils.clone(originalDesign);
        Date dateNow = DateUtils.convertToDate(LocalDate.now());
        newDesign.getFile().getFilingData().setApplicationType(ApplTyp.DIVIDED_NATIONAL_DESIGN);
        newDesign.getFile().getFilingData().setApplicationSubtype(ApplSubTyp.DIVIDED_NATIONAL_DESIGN_SUB_TYPE);
//        newDesign.getFile().getFilingData().setFilingDate(dateNow);
        newDesign.setRowVersion(DefaultValue.INCREMENT_VALUE);
        newDesign.getFile().setRowVersion(DefaultValue.INCREMENT_VALUE);
        newDesign.setPatentEFilingData(null);
        newDesign.getFile().setFileId(null);
        CReception receptionDesign = patentService.constructPatentReceptionObject(newDesign, null, null, true);
        receptionDesign.setRegisterAsAdministrator(true);
        CReceptionResponse designReceptionResponse = internalReceptionService.createReception(receptionDesign);
        receptionResponseWrapper.setReceptionResponse(designReceptionResponse);
        CFile cFileFromReception = fileService.findById(designReceptionResponse.getFileId());
        newDesign.getFile().setFileId(designReceptionResponse.getFileId());
        newDesign.getFile().setProcessId(cFileFromReception.getProcessId());
        newDesign.getFile().setProcessSimpleData(cFileFromReception.getProcessSimpleData());
        newDesign.getFile().getFilingData().setReceptionDate(dateNow);
        newDesign.getFile().getFilingData().setCaptureDate(dateNow);
        newDesign.getFile().getFilingData().setCaptureUserId(Long.valueOf(SecurityUtils.getLoggedUserId()));
        newDesign.getFile().getFilingData().setExternalSystemId(designReceptionResponse.getExternalSystemId());
        newDesign.getFile().getFilingData().setReceptionDocument(cFileFromReception.getFilingData().getReceptionDocument());
        newDesign.getFile().getFilingData().getReceptionDocument().setFilingDate(newDesign.getFile().getFilingData().getFilingDate());
        newDesign.getFile().setFileRecordals(cFileFromReception.getFileRecordals());
        fillDesignParentRelationship(newDesign, originalDesign);
        if (!CollectionUtils.isEmpty(newOwners)) {
            newDesign.getFile().setOwnershipData(new COwnershipData());
            newDesign.getFile().getOwnershipData().setOwnerList(newOwners);
        }
        List<CPatent> originalSingleDesigns = getAllSingleDesignsForIndustrialDesign(originalDesign, false);
        if (isUserdocInit && validSingleDesignIdsOnDivision(originalSingleDesigns, newSingleDesignIds)) {
            originalDesignUpdatesOnDivision(originalDesign, originalSingleDesigns, newSingleDesignIds);
            fillSingleDesignRelationshipsOnDivision(newDesign, newSingleDesignIds);
        }
        patentService.insertPatentAfterValidation(newDesign);
        validateMainDesignRelationships(newDesign);
        processService.updateResponsibleUser(SecurityUtils.getLoggedUserId(), newDesign.getFile().getProcessId().getProcessType(), newDesign.getFile().getProcessId().getProcessNbr());
        procRepository.updateStatusCodeAndDateById(originalDesign.getFile().getProcessSimpleData().getStatusCode(),newDesign.getFile().getProcessId().getProcessType(),newDesign.getFile().getProcessId().getProcessNbr());
        return newDesign;

    }

    @Override
    @IpasValidatorDefinition({
            DesignValidator.class
    })
    public synchronized void updateDesigns(CPatent mainDesign, List<CPatent> singleDesigns) {
        CPatent oldMainDesign = patentService.findPatent(mainDesign.getFile().getFileId(), true);
        List<CPatent> oldSingleDesigns = getAllSingleDesignsForIndustrialDesign(oldMainDesign, true);
        singleDesignsUpdates(singleDesigns);
        fillMainDesignRelationshipListWithSingleDesigns(mainDesign, singleDesigns);
        mainDesignUpdate(mainDesign);
        validateMainDesignRelationships(mainDesign);
        deleteSingleDesigns(oldMainDesign, singleDesigns);
        executeCommonActionsAfterInsertOrUpdate(mainDesign, singleDesigns);
        logChangesService.insertDesignLogChanges(oldMainDesign, mainDesign, oldSingleDesigns, singleDesigns);
    }

    private void executeCommonActionsAfterInsertOrUpdate(CPatent mainDesign, List<CPatent> singleDesigns) {
        CFileId fileId = mainDesign.getFile().getFileId();
        IpPatent ipPatent = ipPatentRepository.findById(new IpFilePK(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr())).orElse(null);
        if (Objects.nonNull(ipPatent)) {
            singleDesigns.forEach(singleDesign -> {
                CFileId singleDesignFileId = singleDesign.getFile().getFileId();
                singleDesignRepository.updateSingleDesignServiceAndMainPerson(ipPatent.getServicePerson().getPk().getPersonNbr(), ipPatent.getServicePerson().getPk().getAddrNbr(),
                        ipPatent.getMainOwner().getPk().getPersonNbr(), ipPatent.getMainOwner().getPk().getAddrNbr(), singleDesignFileId.getFileSeq(), singleDesignFileId.getFileType(), singleDesignFileId.getFileSeries(), singleDesignFileId.getFileNbr());
            });

        }
    }

    /**
     * vij komentara v {@link bg.duosoft.ipas.core.service.impl.mark.MarkServiceImpl#acceptTrademark(CMark, List, ReceptionResponseWrapper)} za komentara po ReceptionResponseWrapper
     * @param design
     * @param singleDesigns
     * @param attachments
     * @param userdocReceptions
     * @param registerInDocflowSystem
     * @param receptionResponseWrapper
     */
    public void acceptDesign(CPatent design, List<CPatent> singleDesigns, List<CAttachment> attachments, List<CReception> userdocReceptions, boolean registerInDocflowSystem, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception receptionPatent = patentService.constructPatentReceptionObject(design, attachments, userdocReceptions, registerInDocflowSystem);
        CReceptionResponse patentReceptionResponse = internalReceptionService.createReception(receptionPatent);
        receptionResponseWrapper.setReceptionResponse(patentReceptionResponse);
        CFile cFileFromReception = fileService.findById(patentReceptionResponse.getFileId());
        design.getFile().setFileId(patentReceptionResponse.getFileId());
        design.getFile().setProcessId(cFileFromReception.getProcessId());
        design.getFile().setProcessSimpleData(cFileFromReception.getProcessSimpleData());
        design.getFile().setFilingData(cFileFromReception.getFilingData());
        transferRegistrationDataFromReceptionToFile(cFileFromReception, design.getFile());
        insertDesigns(design, singleDesigns);
    }


    private void singleDesignActionsOnAccept(CPatent design, List<CPatent> singleDesigns) {
        CFileId mainDesignFileId = design.getFile().getFileId();
        Integer countDesignId = 1;
        if (Objects.nonNull(singleDesigns) && !CollectionUtils.isEmpty(singleDesigns)) {
            for (CPatent singleDesign : singleDesigns) {
                fillSingleDesignId(mainDesignFileId, singleDesign, countDesignId);
                countDesignId++;
                SingleDesign singleDesignToBeUpdated = singleDesignMapper.toEntity(singleDesign, true);
                singleDesignReception(singleDesign);
                fillSingleDesignMissingDataAfterReception(singleDesignToBeUpdated);
                singleDesignToBeUpdated.setRowVersion(singleDesignToBeUpdated.getRowVersion() == null ? 1 : singleDesignToBeUpdated.getRowVersion() + 1);
                entityManager.merge(singleDesignToBeUpdated);
            }
        }
    }

    private void fillSingleDesignId(CFileId mainDesignFileId, CPatent singleDesign, Integer countDesignId) {
        CFileId cFileId = new CFileId();
        cFileId.setFileSeq(mainDesignFileId.getFileSeq());
        cFileId.setFileSeries(mainDesignFileId.getFileSeries());
        String singleDesignFileType;
        if (mainDesignFileId.getFileType().equals(FileType.DESIGN.code())) {
            singleDesignFileType = FileType.SINGLE_DESIGN.code();
        } else if (mainDesignFileId.getFileType().equals(FileType.INTERNATIONAL_DESIGN.code())) {
            singleDesignFileType = FileType.INTERNATIONAL_SINGLE_DESIGN.code();
        } else {
            throw new RuntimeException("Unsupported mainDesignFileType:" + mainDesignFileId.getFileType());
        }
        cFileId.setFileType(singleDesignFileType);
        String constructedFileNbr = String.valueOf(mainDesignFileId.getFileNbr()).concat("000");
        cFileId.setFileNbr(Integer.valueOf(constructedFileNbr) + countDesignId);
        singleDesign.getFile().setFileId(cFileId);
    }


    private void mainDesignUpdate(CPatent mainDesign) {
        IpPatent mainDesignToBeUpdated = patentService.fillPatentCommonData(mainDesign);
        patentService.mergeOrInsertPersons(mainDesignToBeUpdated);
        entityManager.merge(mainDesignToBeUpdated);
    }

    private void deleteSingleDesigns(CPatent mainDesign, List<CPatent> singleDesigns) {
        if (Objects.nonNull(mainDesign.getFile().getRelationshipList())) {
            mainDesign.getFile().getRelationshipList().stream().forEach(rel -> {
                CPatent singleDesign = singleDesigns.stream().filter(design -> design.getFile().getFileId().equals(rel.getFileId())).findFirst().orElse(null);
                if (Objects.isNull(singleDesign) && rel.getRelationshipType().equals(RelationshipType.DESIGN_TYPE)) {
                    patentService.deletePatent(rel.getFileId().getFileSeq(), rel.getFileId().getFileType(), rel.getFileId().getFileSeries(), rel.getFileId().getFileNbr());
                }
            });
        }
    }

    private void singleDesignsUpdates(List<CPatent> singleDesigns) {
        singleDesigns.stream().forEach(singleDesign -> {
            SingleDesign singleDesignToBeUpdated = singleDesignMapper.toEntity(singleDesign, true);
            String newStatusCode = singleDesignToBeUpdated.getFile().getIpProcSimple().getStatusCode();
            if (!Objects.isNull(singleDesign.isReception()) && singleDesign.isReception() == true) {
                singleDesignReception(singleDesign);
                fillSingleDesignMissingDataAfterReception(singleDesignToBeUpdated);
            }
            singleDesignToBeUpdated.setRowVersion(singleDesignToBeUpdated.getRowVersion() == null ? 1 : singleDesignToBeUpdated.getRowVersion() + 1);
            IpFilePK singleDesignToBeUpdatedPk = singleDesignToBeUpdated.getPk();
            CStatus dbStatus = statusService.getStatus(singleDesignToBeUpdatedPk.getFileSeq(), singleDesignToBeUpdatedPk.getFileTyp(), singleDesignToBeUpdatedPk.getFileSer(), singleDesignToBeUpdatedPk.getFileNbr());
            if (Objects.nonNull(dbStatus) && !dbStatus.getStatusId().getStatusCode().equals(newStatusCode)) {
                procRepository.updateStatusCodeAndDateById(newStatusCode, singleDesignToBeUpdated.getFile().getProcTyp(), singleDesignToBeUpdated.getFile().getProcNbr());
            }
            entityManager.merge(singleDesignToBeUpdated);
        });
    }

    private void fillSingleDesignMissingDataAfterReception(SingleDesign singleDesignToBeUpdated) {
        singleDesignToBeUpdated.setFile(singleDesignFileRepository.findById(singleDesignToBeUpdated.getPk()).orElse(null));
        singleDesignToBeUpdated.setDocLog(singleDesignToBeUpdated.getFile().getIpDoc().getPk().getDocLog());
        singleDesignToBeUpdated.setDocSer(singleDesignToBeUpdated.getFile().getIpDoc().getPk().getDocSer());
        singleDesignToBeUpdated.setDocNbr(singleDesignToBeUpdated.getFile().getIpDoc().getPk().getDocNbr());
        singleDesignToBeUpdated.setReceptionDate(singleDesignToBeUpdated.getFile().getIpDoc().getReceptionDate());
        singleDesignToBeUpdated.setLawCode(singleDesignToBeUpdated.getFile().getLawCode());
        singleDesignToBeUpdated.setFilingDate(singleDesignToBeUpdated.getFile().getFilingDate());
    }

    private void singleDesignReception(CPatent singleDesign) {
        CReception receptionForm = new CReception();
        receptionForm.setFile(new CReceptionFile());
        CFileId fileId = singleDesign.getFile().getFileId();
        receptionForm.getFile().setFileId(new CFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr()));
        receptionForm.getFile().setApplicationType(singleDesign.getFile().getFilingData().getApplicationType());
        receptionForm.getFile().setApplicationSubType(singleDesign.getFile().getFilingData().getApplicationSubtype());
        receptionForm.setEntryDate(DateUtils.convertToDate(LocalDate.now().atTime(1, 0, 0)));
        receptionForm.setSubmissionType(SubmissionType.COUNTER.code());
        receptionForm.setOriginalExpected(false);
        receptionForm.getFile().setTitle(singleDesign.getTechnicalData().getTitle());
        receptionForm.setRegisterReceptionRequest(false);
        receptionForm.setRegisterInDocflowSystem(false);
        internalReceptionService.createReception(receptionForm);
    }

    private void validateMainDesignRelationships(CPatent mainDesign) {
        IpasValidator<CFile> validator = validatorCreator.create(false, RelationshipsValidator.class);
        List<ValidationError> errors = validator.validate(mainDesign.getFile());
        if (!Objects.isNull(errors) && !CollectionUtils.isEmpty(errors)) {
            throw new IpasValidationException(errors);
        }
    }

    private void fillMainDesignRelationshipListWithSingleDesigns(CPatent mainDesign, List<CPatent> singleDesigns) {
        if (CollectionUtils.isEmpty(mainDesign.getFile().getRelationshipList())) {
            mainDesign.getFile().setRelationshipList(new ArrayList<>());
        } else {
            mainDesign.getFile().getRelationshipList().removeIf(r -> r.getRelationshipType().equals(FileType.DESIGN.code()));
        }
        if (!CollectionUtils.isEmpty(singleDesigns)) {
            singleDesigns.stream().forEach(singleDesign -> {
                CRelationship newRelationship = new CRelationship();
                newRelationship.setRelationshipType(RelationshipType.DESIGN_TYPE);
                newRelationship.setRelationshipRole("1");
                newRelationship.setFileId(singleDesign.getFile().getFileId());
                mainDesign.getFile().getRelationshipList().add(newRelationship);
            });
        }
    }

}
