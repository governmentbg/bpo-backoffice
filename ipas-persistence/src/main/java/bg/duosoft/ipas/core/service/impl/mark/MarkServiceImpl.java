package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.mapper.file.FileMapper;
import bg.duosoft.ipas.core.mapper.mark.LogoMapper;
import bg.duosoft.ipas.core.mapper.mark.MarkMapper;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.mark.CEnotifMark;
import bg.duosoft.ipas.core.model.mark.CLogo;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.impl.IntellectualPropertyServiceImpl;
import bg.duosoft.ipas.core.service.impl.reception.ReceptionResponseWrapper;
import bg.duosoft.ipas.core.service.logging.LogChangesService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.reception.InternalReceptionService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.mark.*;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.exception.MarkDivisionException;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.*;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.enotif.EnotifRepository;
import bg.duosoft.ipas.persistence.repository.entity.file.IpFileRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpLogoRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkAttachmentsRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpNameRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionRequestRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;


@Service
@Transactional
@Slf4j
public class MarkServiceImpl extends IntellectualPropertyServiceImpl implements MarkService {
    private final Object lockUpdate = new Object();

    @Autowired
    private IpProcRepository procRepository;

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @Autowired
    private MarkMapper markMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private LogChangesService logChangesService;

    @Autowired
    private IpNameRepository ipNameRepository;

    @Autowired
    private IpFileRepository ipFileRepository;

    @Autowired
    private EnotifRepository enotifRepository;

    @Autowired
    private ReceptionRequestRepository receptionRequestRepository;

    @Autowired
    private IpLogoRepository ipLogoRepository;

    @Autowired
    private IpMarkAttachmentsRepository ipMarkAttachmentsRepository;

    @Autowired
    private LogoMapper logoMapper;

    @Autowired
    private InternalReceptionService internalReceptionService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private FileService fileService;

    @Override
    @LogExecutionTime
    public CMark findMark(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, boolean addLogo) {
        IpFilePK pk = new IpFilePK(fileSeq, fileTyp, fileSer, fileNbr);
        IpMark ipMark = getIpMark(pk);
        if (Objects.isNull(ipMark))
            return null;

        return toMark(ipMark, addLogo);
    }

    @Override
    public CMark findMark(CFileId id, boolean readLogo) {
        if (Objects.isNull(id))
            return null;

        return findMark(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr(), readLogo);
    }

    @Override
    @IpasValidatorDefinition({
            MarkValidator.class
    })
    public void updateMark(CMark mark) {
        synchronized (lockUpdate) {
            proceedUpdateMark(mark);
        }
    }

    @Override
    @IpasValidatorDefinition({
            MarkRecordalAuthorizationValidator.class
    })
    public void updateMarkOnUserdocAuthorization(CMark mark) {
        synchronized (lockUpdate) {
            proceedUpdateMark(mark);
        }
    }

    @Override
    @IpasValidatorDefinition({
            MarkPartialInternalValidator.class
    })
    public void updateMarkInternal(CMark mark) {
        synchronized (lockUpdate) {
            proceedUpdateMark(mark);
        }
    }

    private void proceedUpdateMark(CMark mark) {
        log.debug("Trying to update mark with pk = " + mark.getFile().getFileId());
        IpMark markToBeUpdated = markMapper.toEntity(mark);
        loadAttachments(mark, markToBeUpdated);//TODO

        IpMark originalMark = getIpMark(markToBeUpdated.getPk());
        if (Objects.isNull(originalMark)) {
            throw new RuntimeException("Mark does not exist...");
        }
        validateIpObjectRowVersion(originalMark, markToBeUpdated);

        CMark originalCoreMark = toMark(originalMark, true);
        //TODO:Dali dolnata chast s update-a ne trqbva da e v sinhronizirasht blok??? Na teoriq 2 thread-a mogat da proverqt dali row versiqta im e OK i dvata da updatenat versiqta s 1ca i da im mine koda za update... Gledam che i IPAS-a taka pravqt. Nqmat sihronizacia, ama tova ne e gotino.... Zasega sym sinhroniziral methoda!
        entityManager.detach(originalMark);
        IpFile file = originalMark.getFile();
        markToBeUpdated.setFile(file);
        fileMapper.fillIpFileFields(mark.getFile(), file);//v detachnatiq file, se updatevat poletata doshli ot request-a

        markToBeUpdated.setName(ipNameRepository.getOrInsertIpName(markToBeUpdated.getName()));//getting IpName by markName/markNameLang2 or inserting a new one

        mergeOrInsertPersons(markToBeUpdated);//persons are getting persisted separately, because IpMark.servicePerson, IpMark.mainOwner, IpMarkOwners.ipPersonAddresses and IpMarkReprs.ipPersonAddresses have now cascade annotations and they are not getting persisted on em.merge(ipMark)!!!!
        incrementIpObjectRowVersion(markToBeUpdated);
        entityManager.merge(markToBeUpdated);

        logChangesService.insertMarkLogChanges(originalCoreMark, mark);
        changeAbdocsSubjectOnMarkUpdate(originalMark, markToBeUpdated);
        log.debug(String.format("Updated mark with pk = %s ", mark.getFile().getFileId()));
    }

    private void changeAbdocsSubjectOnMarkUpdate(IpMark originalMark, IpMark updatedMark) {
        IpFilePK pk = originalMark.getFile().getPk();
        String filingNumber = BasicUtils.createFilingNumber(pk.getFileSeq(), pk.getFileTyp(), pk.getFileSer(), pk.getFileNbr());
        try {
            IpName originalName = originalMark.getName();
            IpName updatedName = updatedMark.getName();
            if (Objects.nonNull(originalName) && Objects.nonNull(updatedName)) {
                String originalNameString = originalName.getMarkName();
                String updatedNameString = updatedName.getMarkName();
                if (StringUtils.hasText(originalNameString) && StringUtils.hasText(updatedNameString)) {
                    if (!originalNameString.equals(updatedNameString)) {
                        Integer documentId = abdocsService.selectDocumentIdByRegistrationNumber(filingNumber);
                        if (Objects.nonNull(documentId)) {
                            abdocsService.updateSubject(documentId, updatedNameString);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("Mark subject is not changed in abdocs ! Filing number: " + filingNumber);
        }
    }

    @Override
    @IpasValidatorDefinition({
            MarkValidator.class
    })
    public synchronized void insertMark(CMark mark) throws IpasValidationException {
        log.debug("Inserting new mark with pk = " + mark.getFile().getFileId());
        IpMark ipMark = prepareMarkReception(mark);
        ipMarkRepository.save(ipMark);
        IpFilePK pk = ipMark.getFile().getPk();
        receptionRequestRepository.updateReceptionRequestStatus(pk.getFileSeq(), pk.getFileTyp(), pk.getFileSer(), pk.getFileNbr());
        log.debug(String.format("Inserted new mark with pk = %s ", mark.getFile().getFileId()));
    }

    private CMark toMark(IpMark mark, boolean addLogo) {
        return markMapper.toCore(mark, addLogo);
    }

    private IpMark getIpMark(IpFilePK pk) {
        IpMark ipMark = ipMarkRepository.findById(pk).orElse(null);
        return ipMark;
    }

    private IpMark prepareMarkReception(CMark cMark) {
        IpMark markToBeSaved = markMapper.toEntity(cMark);
        markToBeSaved.setRowVersion(DefaultValue.ROW_VERSION);

        markToBeSaved.setName(ipNameRepository.getOrInsertIpName(markToBeSaved.getName()));//getting IpName by markName/markNameLang2 or inserting a new one

        //--> Getting reception file from database, set it to mark and fill all fields coming from request
        IpFile originalFile = ipFileRepository.findById(markToBeSaved.getFile().getPk()).orElse(null);
        if (Objects.isNull(originalFile))
            throw new RuntimeException("Reception IP_FILE is empty ! ID: " + cMark.getFile().getFileId());
        validateFileRowVersion(originalFile, markToBeSaved.getFile());

        entityManager.detach(originalFile);
        markToBeSaved.setFile(originalFile);
        fileMapper.fillIpFileFields(cMark.getFile(), originalFile);
        //<--

        Date captureDate = new Date();
        Integer captureUserId = SecurityUtils.getLoggedUserId();
        markToBeSaved.setCaptureDate(captureDate);
        markToBeSaved.setCaptureUser(new IpUser(captureUserId));
        markToBeSaved.getFile().setCaptureDate(captureDate);
        markToBeSaved.getFile().setCaptureUserId(captureUserId);
        if(Objects.nonNull(markToBeSaved.getName()) && Objects.nonNull(markToBeSaved.getName().getMarkName())){
            markToBeSaved.getFile().setTitle(markToBeSaved.getName().getMarkName());
        }
        //TODO Is method mergeOrInsertPersons necessary here ?
        mergeOrInsertPersons(markToBeSaved);
        incrementIpFileRowVersion(markToBeSaved.getFile());
        return markToBeSaved;
    }


    @Override
    public CLogo selectMarkLogo(CFileId cFileId) {
        IpFilePK pk = new IpFilePK(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries(), cFileId.getFileNbr());
        IpLogo ipLogo = ipLogoRepository.findById(pk).orElse(null);
        if (Objects.isNull(ipLogo))
            return null;

        return logoMapper.toCore(ipLogo, true);
    }

    private void loadAttachments(CMark mark, IpMark ipMark) {
        if (Objects.nonNull(ipMark)) {
            IpLogo logo = ipMark.getLogo();
            if (Objects.nonNull(logo)) {
                if (null == logo.getLogoData() || 0 == logo.getLogoData().length) {
                    IpFilePK pk = ipMark.getFile().getPk();
                    IpLogo ipLogo = ipLogoRepository.findById(pk).orElse(null);
                    if (Objects.nonNull(ipLogo))
                        logo.setLogoData(ipLogo.getLogoData());
                }
            }

            List<IpMarkAttachment> attachments = ipMark.getAttachments();
            if (!CollectionUtils.isEmpty(attachments)) {
                for (IpMarkAttachment attachment : attachments) {
                    if (null == attachment.getData() || 0 == attachment.getData().length) {
                        Integer id = attachment.getId();
                        if (Objects.nonNull(id)) {
                            IpMarkAttachment databaseAttachment = ipMarkAttachmentsRepository.findById(id).orElse(null);
                            if (Objects.nonNull(databaseAttachment)) {
                                attachment.setData(databaseAttachment.getData());
                            }
                        }
                    }
                }
            }

        }

        //reloads the attachments to the core object too. Otherwise the logChangeService finds differences between old and new mark, and adds a log message on every mark save!!!
        mark.getSignData().setAttachments(markMapper.createCoreAttachments(ipMark, true));
    }

    @Override
    public boolean isMarkExists(CFileId id) {
        Integer count = ipMarkRepository.countById(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr());
        return !(Objects.isNull(count) || 0 == count);
    }

    @Override
    public void updateRowVersion(CFileId id) {
        if (Objects.nonNull(id)) {
            ipMarkRepository.updateRowVersion(id.getFileSeq(), id.getFileType(), id.getFileSeries(), id.getFileNbr());
        }
    }


    private void defineDividedMarkApplicationSubType(CMark mark){
        String applicationSubType = mark.getFile().getFilingData().getApplicationSubtype();
        if (applicationSubType.equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_INDIVIDUAL) || applicationSubType.equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_OLD_SUB_TYPE)){
            mark.getFile().getFilingData().setApplicationSubtype(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_INDIVIDUAL);
        }
        else if (applicationSubType.equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_COLLECTIVE)){
            mark.getFile().getFilingData().setApplicationSubtype(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_COLLECTIVE);
        }
        else if (applicationSubType.equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_CERTIFIED)){
            mark.getFile().getFilingData().setApplicationSubtype(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_CERTIFIED);
        }else if(applicationSubType.equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_INDIVIDUAL) ||
                applicationSubType.equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_COLLECTIVE)||
                applicationSubType.equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_CERTIFIED)){
            mark.getFile().getFilingData().setApplicationSubtype(applicationSubType);
        }
        else{
            throw new RuntimeException("Unrecognized application sub type : "+applicationSubType);
        }
    }

    /**
     * za parametyra receptionResponseWrapper, vij komentara na {@link #acceptTrademark(CMark, List, ReceptionResponseWrapper)}
     * @param fileId
     * @param newOwners
     * @param newNiceClasses
     * @param isUserdocInit
     * @param receptionResponseWrapper
     * @return
     */

    public CMark createDividedMark(CFileId fileId, List<COwner> newOwners, List<CNiceClass> newNiceClasses,boolean isUserdocInit, ReceptionResponseWrapper receptionResponseWrapper) {
        if (Objects.isNull(fileId)) {
            throw new MarkDivisionException("File ID is required for mark division !");
        }

        String fileType = fileId.getFileType();
        if (!(FileType.MARK.code().equals(fileType) || FileType.DIVISIONAL_MARK.code().equals(fileType))) {
            throw new MarkDivisionException("Inserted file id is not for mark ! File ID: " + fileId);
        }

        CMark mark = findMark(fileId, true);
        if (Objects.isNull(mark)) {
            throw new MarkDivisionException("Mark doesn't exist for ID: " + fileId);
        }

        return createDividedMark(mark, newOwners, newNiceClasses,isUserdocInit, receptionResponseWrapper);
    }


    private void additionalActionsOnCreateDividedMark(CMark originalMark,CMark dividedMark,List<CNiceClass> newNiceClasses,List<COwner> newOwners,boolean isUserdocInit){

      if (isUserdocInit){

          if (CollectionUtils.isEmpty(newNiceClasses)){
              throw new MarkDivisionException("There aren't new nice classes specified for the divisional mark!");
          }

          CMark originalMarkCopy = (CMark) SerializationUtils.clone(originalMark);
          removeNiceClassesFromOriginalMark(originalMark, newNiceClasses, originalMarkCopy);
          if (CollectionUtils.isEmpty(originalMarkCopy.getProtectionData().getNiceClassList())) {
              throw new MarkDivisionException("The division has been interrupted! There aren't nice classes left in the original mark! ID: " + originalMark.getFile().getFileId());
          }
          updateMark(originalMarkCopy);
          if (!CollectionUtils.isEmpty(newOwners)) {
              dividedMark.getFile().setOwnershipData(new COwnershipData());
              dividedMark.getFile().getOwnershipData().setOwnerList(newOwners);
          }
          dividedMark.getProtectionData().setNiceClassList(newNiceClasses);
      }else{
          dividedMark.getProtectionData().setNiceClassList(originalMark.getProtectionData().getNiceClassList());
          if (Objects.nonNull(originalMark.getFile().getOwnershipData()) && !CollectionUtils.isEmpty(originalMark.getFile().getOwnershipData().getOwnerList())){
              dividedMark.getFile().setOwnershipData(new COwnershipData());
              dividedMark.getFile().getOwnershipData().setOwnerList(originalMark.getFile().getOwnershipData().getOwnerList());
          }
      }

    }

    private void removeNiceClassesFromOriginalMark(CMark originalMark, List<CNiceClass> newNiceClasses, CMark originalMarkCopy) {
        for (CNiceClass newNiceClass: newNiceClasses) {
            CNiceClass exactMatch = originalMark.getProtectionData().getNiceClassList().stream()
                    .filter(exactMatchNiceClassPredicate(newNiceClass))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(exactMatch)){
                originalMarkCopy.getProtectionData().getNiceClassList().removeIf(exactMatchNiceClassPredicate(exactMatch));
            }
        }
    }

    private Predicate<CNiceClass> exactMatchNiceClassPredicate(CNiceClass newNiceClass) {
        return r -> r.getNiceClassNbr().equals(newNiceClass.getNiceClassNbr()) && r.getNiceClassDescription().equalsIgnoreCase(newNiceClass.getNiceClassDescription());
    }

    private CMark createDividedMark(CMark originalMark, List<COwner> newOwners, List<CNiceClass> newNiceClasses, boolean isUserdocInit, ReceptionResponseWrapper receptionResponseWrapper) {
        Date dateNow = DateUtils.convertToDate(LocalDate.now());
        CMark dividedMark = (CMark) SerializationUtils.clone(originalMark);
        CFileId parentMarkId = dividedMark.getFile().getFileId();
        dividedMark.getFile().getFilingData().setApplicationType(ApplTyp.DIVIDED_NATIONAL_MARK_TYPE);
        defineDividedMarkApplicationSubType(dividedMark);
        dividedMark.getFile().getFilingData().setFilingDate(dateNow);
        dividedMark.setRowVersion(DefaultValue.INCREMENT_VALUE);
        dividedMark.getFile().setRowVersion(DefaultValue.INCREMENT_VALUE);
        dividedMark.setMarkEFilingData(null);
        CReception receptionMark = constructMarkReceptionObject(dividedMark,SubmissionType.DIVIDED.code());
        receptionMark.setRegisterAsAdministrator(true);
        CReceptionResponse markReceptionResponse = internalReceptionService.createReception(receptionMark);
        receptionResponseWrapper.setReceptionResponse(markReceptionResponse);
        CFile cFileFromReception = fileService.findById(markReceptionResponse.getFileId());
        dividedMark.getFile().setFileId(markReceptionResponse.getFileId());
        dividedMark.getFile().setProcessId(cFileFromReception.getProcessId());
        dividedMark.getFile().setProcessSimpleData(cFileFromReception.getProcessSimpleData());
        dividedMark.getFile().getFilingData().setFilingDate(originalMark.getFile().getFilingData().getFilingDate());
        dividedMark.getFile().getFilingData().setReceptionDate(dateNow);
        dividedMark.getFile().getFilingData().setCaptureDate(dateNow);
        dividedMark.getFile().getFilingData().setCaptureUserId(Long.valueOf(SecurityUtils.getLoggedUserId()));
        dividedMark.getFile().getFilingData().setExternalSystemId(markReceptionResponse.getExternalSystemId());
        dividedMark.getFile().getFilingData().setReceptionDocument(cFileFromReception.getFilingData().getReceptionDocument());
        dividedMark.getFile().getFilingData().getReceptionDocument().setFilingDate(dividedMark.getFile().getFilingData().getFilingDate());
        dividedMark.getFile().setFileRecordals(cFileFromReception.getFileRecordals());
        IpMark ipMark = markMapper.toEntity(originalMark);
        loadAttachments(dividedMark, ipMark);
        fillDividedMarkRelationship(dividedMark, parentMarkId);
        additionalActionsOnCreateDividedMark(originalMark,dividedMark,newNiceClasses,newOwners,isUserdocInit);
        updateStatusAndResponsibleUserOfDividedMark(originalMark, cFileFromReception);
        insertMark(dividedMark);
        return dividedMark;
    }

    private void updateStatusAndResponsibleUserOfDividedMark(CMark originalMark, CFile cFileFromReception) {
        CProcessId processId = cFileFromReception.getProcessId();
        CProcessSimpleData processSimpleData = originalMark.getFile().getProcessSimpleData();
        procRepository.updateStatusCodeAndDateById(processSimpleData.getStatusCode(), processId.getProcessType(), processId.getProcessNbr());
        procRepository.updateResponsibleUser(processSimpleData.getResponsibleUser().getUserId(),processId.getProcessType(), processId.getProcessNbr());
    }

    @Override
    public List<String> selectInternationalMarkIds() {
        return ipMarkRepository.selectInternationalMarkIds();
    }

    private void fillDividedMarkRelationship(CMark dividedMark,CFileId parentMarkId){
        dividedMark.getFile().setRelationshipList(new ArrayList<>());
        CRelationship newRelationship = new CRelationship();
        newRelationship.setRelationshipType(RelationshipType.DIVISIONAL_MARK_TYPE);
        newRelationship.setRelationshipRole("2");
        newRelationship.setFileId(parentMarkId);
        dividedMark.getFile().getRelationshipList().add(newRelationship);
    }

    /**
     * tyi kato klasa e s anotaciq @Transactional, pri izlizane ot acceptTrademark se pravi commit na tranzakciqta i ako neshto ne e kakto trqbva se hvyrlq exception, no toj e izvyn methoda
     * Ne izmislih kak po drug nachin da polucha nomera na registriraniq v delovodnata sistema dokument, slojih edin parametyr ReceptionResponseWrapper receptionResponseWrapper, v koito se slaga CReceptionResponse
     * koito se poluchava sled reception na dokumenta! Taka ReceptionServiceImpl moje da razbere nomera na registriraniq v deolovodnata dokument i da go iztrie!!! Ne e naj-umnoto reshenie, zashtoto ne e OK v request parametrite da se
     * iznasqt danni ot method-a, no inache nqma kak da stane!!!!
     * @param mark
     * @param attachments
     * @param receptionResponseWrapper - wrapper obekt kojto sydyrja reception obekta koito se syzdava vytre v method-a
     */
    public void acceptTrademark(CMark mark, List<CAttachment> attachments, ReceptionResponseWrapper receptionResponseWrapper) {
        CReception receptionMark = constructMarkReceptionObject(mark,null);
        CReceptionResponse markReceptionResponse = internalReceptionService.createReception(receptionMark);
        receptionResponseWrapper.setReceptionResponse(markReceptionResponse);
        CFile cFileFromReception = fileService.findById(markReceptionResponse.getFileId());
        mark.getFile().setFileId(markReceptionResponse.getFileId());
        mark.getFile().setProcessId(cFileFromReception.getProcessId());
        mark.getFile().setProcessSimpleData(cFileFromReception.getProcessSimpleData());
        mark.getFile().setFilingData(cFileFromReception.getFilingData());
        transferRegistrationDataFromReceptionToFile(cFileFromReception, mark.getFile());
        if(mark.getSignData() != null) {
            if (mark.getSignData().getSignType() != null && mark.getSignData().getSignType().equals(MarkSignType.COMBINED) &&
                    (Objects.isNull(mark.getSignData().getMarkName()) || mark.getSignData().getMarkName().isEmpty())) {
                mark.getSignData().setMarkName(DefaultValue.FIGURATIVE_MARK_CONST);
            }
            mark.getFile().setTitle(mark.getSignData().getMarkName());
        }
        insertMark(mark);
        if (Objects.nonNull(attachments)) {
            attachments.stream().forEach(attachment -> {
                if (Objects.nonNull(attachment.getData()))
                    abdocsService.uploadFileToExistingDocument(markReceptionResponse.getDocflowDocumentId(), attachment.getData(), attachment.getFileName(), attachment.getDescription(), false, DocFileVisibility.PublicAttachedFile);
            });
        }
    }

    /**
     * vij komentara na {@link #acceptTrademark(CMark, List, ReceptionResponseWrapper)}
     * @param mark
     * @param attachments
     * @param receptionResponseWrapper
     */
    public void acceptInternationalTrademark(CMark mark, List<CAttachment> attachments, ReceptionResponseWrapper receptionResponseWrapper) {
        // Check if bordero is defined
        if (CollectionUtils.isEmpty(mark.getEnotifMarks())) {
            throw new RuntimeException("No publications are defined!");
        }

        // Create or increment bordero
        CEnotifMark enotifMark = mark.getEnotifMarks().get(0);
        Enotif enotif = enotifRepository.findById(enotifMark.getEnotif().getGazno()).orElse(null);
        if (Objects.nonNull(enotif)) {
            Integer birtCount = enotif.getBirthCount() + DefaultValue.INCREMENT_VALUE;
            enotifMark.getEnotif().setBirthCount(birtCount);
        } else {
            enotifMark.getEnotif().setBirthCount(1);
        }

        // Accept international trademark
        acceptTrademark(mark, attachments, receptionResponseWrapper);
    }

    @Override
    public long count() {
        return ipMarkRepository.count();
    }



    private CReception constructMarkReceptionObject(CMark mark,Integer submissionType) {
        CReception receptionForm = new CReception();
        receptionForm.setFile(new CReceptionFile());
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(mark.getFile().getFilingData().getFilingDate()));
        if (Objects.isNull(mark.getSignData()) || StringUtils.isEmpty(mark.getSignData().getMarkName())) {
            receptionForm.getFile().setEmptyTitle(true);
        } else {
            receptionForm.getFile().setTitle(mark.getSignData().getMarkName());
            receptionForm.getFile().setEmptyTitle(false);
        }
        receptionForm.setOriginalExpected(false);
        String applicationType = mark.getFile().getFilingData().getApplicationType();
        String applicationSubTyp = mark.getFile().getFilingData().getApplicationSubtype();

        if (Objects.nonNull(applicationType) &&
                (applicationType.equalsIgnoreCase(ApplTyp.INTERNATIONAL_MARK_TYPE_I) || applicationType.equalsIgnoreCase(ApplTyp.INTERNATIONAL_MARK_TYPE_R) || applicationType.equalsIgnoreCase(ApplTyp.INTERNATIONAL_MARK_TYPE_B))) {
            receptionForm.setRegisterReceptionRequest(false);
            if (Objects.nonNull(mark.getFile().getFileId()) && Objects.nonNull(mark.getFile().getFileId().getFileNbr())) {
                receptionForm.getFile().setFileId(mark.getFile().getFileId());
            }
            receptionForm.setSubmissionType(SubmissionType.IMPORT.code());
        } else if (Objects.nonNull(applicationType) && applicationType.equalsIgnoreCase(ApplTyp.NATIONAL_MARK_TYPE)) {
            receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
        }else if (Objects.nonNull(applicationType) && applicationType.equalsIgnoreCase(ApplTyp.DIVIDED_NATIONAL_MARK_TYPE)) {
            if (Objects.nonNull(submissionType)){
                receptionForm.setSubmissionType(submissionType);
            }else{
                receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
            }
        } else if(Objects.nonNull(applicationType) && applicationType.equalsIgnoreCase(ApplTyp.ACP_SIGNAL)) {
            receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
        } else if(Objects.nonNull(applicationType) && applicationType.equalsIgnoreCase(ApplTyp.GEOGRAPHICAL_INDICATION_TYPE)) {
            receptionForm.setSubmissionType(SubmissionType.ELECTRONIC.code());
        }
        else {
            throw new RuntimeException("Unrecognized application typ: " + applicationType);
        }
        receptionForm.getFile().setApplicationType(applicationType);
        receptionForm.getFile().setApplicationSubType(applicationSubTyp);
        receptionForm.setRegisterInDocflowSystem(true);
        receptionForm.setOwnershipData(mark.getFile().getOwnershipData());
        receptionForm.setRepresentationData(mark.getFile().getRepresentationData());
        return receptionForm;
    }

}