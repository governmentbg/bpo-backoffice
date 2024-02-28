package bg.duosoft.ipas.core.service.impl.mark;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.mapper.mark.MarkOldInternationalRegistrationMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CMarkOldInternationalRegistration;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataValue;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.core.service.mark.MarkOldInternationalRegistrationService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.reception.MissingAbdocsDocumentService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpMarkOldInternationalRegistration;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkOldInternationalRegistrationRepository;
import bg.duosoft.ipas.util.abdocs.AbdocsUtils;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MarkOldInternationalRegistrationServiceImpl implements MarkOldInternationalRegistrationService {

    private final AbdocsService abdocsService;
    private final ReceptionService receptionService;
    private final MarkService markService;
    private final UserdocService userdocService;
    private final UserdocTypeConfigService userdocTypeConfigService;
    private final IpMarkOldInternationalRegistrationRepository ipMarkOldInternationalRegistrationRepository;
    private final MarkOldInternationalRegistrationMapper markOldInternationalRegistrationMapper;
    private final MissingAbdocsDocumentService missingAbdocsDocumentService;
    private final ErrorLogService errorLogService;
    private final MessageSource messageSource;

    @Override
    public List<CMarkOldInternationalRegistration> selectUnprocessed() {
        return markOldInternationalRegistrationMapper.toCoreList(ipMarkOldInternationalRegistrationRepository.selectUnprocessed());
    }

    @Override
    public CReceptionResponse insertOldInternationalRegistration(CMarkOldInternationalRegistration oldRegistration, CFile mainMark) {
        Document abdocsDocument = StringUtils.isEmpty(oldRegistration.getExternalSystemId()) ? null : abdocsService.selectDocumentByRegistrationNumber(oldRegistration.getExternalSystemId());
        CReception reception = createReceptionObject(oldRegistration, abdocsDocument, mainMark);
        CReceptionResponse response = receptionService.createReception(reception);
        updateUserdocInternationalRegistrationDetails(response.getDocId(), oldRegistration);
        updateMarkInternationalRegistrationDetails(mainMark.getFileId(), oldRegistration);
        if (Objects.nonNull(abdocsDocument)) {
            oldRegistration.setHasAbdocsRecord(true);
            save(oldRegistration);
            try {
                AbdocsUtils.updateDocumentParent(abdocsDocument.getDocId(), mainMark.getFileId(), missingAbdocsDocumentService, abdocsService);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                String actionTitle = messageSource.getMessage("error.action.abdocs.change.parent", null, LocaleContextHolder.getLocale());
                String customMessage = messageSource.getMessage("error.abdocs.change.parent", new String[]{abdocsDocument.getRegUri(), mainMark.getFileId().createFilingNumber()}, LocaleContextHolder.getLocale());
                String instruction = messageSource.getMessage("instruction.abdocs.change.parent", new String[]{abdocsDocument.getRegUri()}, LocaleContextHolder.getLocale());
                errorLogService.createNewRecord(ErrorLogAbout.ABDOCS, actionTitle, e.getMessage(), customMessage, true, instruction, ErrorLogPriority.MEDIUM);
            }
        }

        return response;
    }

    private void updateUserdocInternationalRegistrationDetails(CDocumentId documentId, CMarkOldInternationalRegistration oldRegistration) {
        CUserdoc userdoc = userdocService.findUserdoc(documentId);

        if (StringUtils.hasText(oldRegistration.getBgReference())) {
            userdoc.getDocument().getExtraData().setDataText1(oldRegistration.getBgReference() + "/" + oldRegistration.getWipoReference());
        }

        if (Objects.nonNull(oldRegistration.getIntlRegNbr())) {
            UserdocExtraDataUtils.setUserdocExtraDataProperty(userdoc, UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_NUMBER.name(), CUserdocExtraDataValue.builder().textValue(oldRegistration.getIntlRegNbr()).build());
            UserdocExtraDataUtils.setUserdocExtraDataProperty(userdoc, UserdocExtraDataTypeCode.INTERNATIONAL_REGISTRATION_DATE.name(), CUserdocExtraDataValue.builder().dateValue(oldRegistration.getIntlRegDate()).build());

        }
        userdocService.updateUserdoc(userdoc, false);
    }

    private void updateMarkInternationalRegistrationDetails(CFileId fileId, CMarkOldInternationalRegistration oldRegistration) {
        if (Objects.nonNull(oldRegistration.getIntlRegNbr())) {
            CMark mark = markService.findMark(fileId, false);
            mark.getMadridApplicationData().setInternationalFileNumber(oldRegistration.getIntlRegNbr());
            mark.getMadridApplicationData().setIntFilingDate(oldRegistration.getIntlRegDate());
            markService.updateMarkInternal(mark);
        }
    }

    private CReception createReceptionObject(CMarkOldInternationalRegistration oldRegistration, Document abdocsDocument, CFile mainMark) {
        CReception receptionForm = new CReception();
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(oldRegistration.getReceivedDate()));
        receptionForm.setNotes(createOwnerNotes(oldRegistration));
        receptionForm.setOriginalExpected(false);
        receptionForm.setSubmissionType(SubmissionType.IMPORT.code());
        receptionForm.setUserdoc(new CReceptionUserdoc());
        receptionForm.getUserdoc().setUserdocType(UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST);
        receptionForm.getUserdoc().setWithoutCorrespondents(true);
        userdocTypeConfigService.defineUpperProc(receptionForm.getUserdoc(), mainMark.getFileId());
        receptionForm.setOwnershipData(constructDefaultWIPOOwner());
        fillDocflowData(abdocsDocument, receptionForm, oldRegistration);
        return receptionForm;
    }

    private void fillDocflowData(Document abdocsDocument, CReception receptionForm, CMarkOldInternationalRegistration oldRegistration) {
        if (Objects.nonNull(abdocsDocument)) {
            receptionForm.setDocflowSystemId(abdocsDocument.getDocId());
        } else if (StringUtils.hasText(oldRegistration.getBgReference())) {
            Date abdocsDate = Objects.nonNull(oldRegistration.getIntlRegDate()) ? oldRegistration.getIntlRegDate() : oldRegistration.getReceivedDate();
            String externalSystemId = oldRegistration.getBgReference()+"/"+DateUtils.formatDate(abdocsDate);
            receptionForm.getUserdoc().setExternalRegistrationNumber(externalSystemId);
            receptionForm.setRegisterInDocflowSystem(true);
            log.info("OLD INTERNATIONAL REGISTRATION {} - MISSING ABDOCS RECORD: BG_REFERENCE = {} , RECEIVED = {}. WILL BE REGISTERED IN ABDOCS WITH: {} ",oldRegistration.getId(), oldRegistration.getBgReference(), oldRegistration.getReceivedDate(), externalSystemId);
        } else {
            receptionForm.setRegisterReceptionRequest(false);
            receptionForm.setRegisterInDocflowSystem(false);
            log.info("OLD INTERNATIONAL REGISTRATION {} - MISSING ABDOCS RECORD: BG_REFERENCE = {} , RECEIVED = {} ",oldRegistration.getId(), oldRegistration.getBgReference(), oldRegistration.getReceivedDate());
        }
    }

    private String createOwnerNotes(CMarkOldInternationalRegistration oldRegistration) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Подател: ").append(oldRegistration.getHolderName()).append(" ").append(oldRegistration.getHolderAddress());
        return stringBuilder.toString();
    }

    private COwnershipData constructDefaultWIPOOwner() {
        CPerson person = new CPerson();
        person.setPersonName("WIPO");
        person.setAddressStreet("34, chemin des Colombettes");
        person.setCityName("Geneva");
        person.setZipCode("CH-1211");
        person.setResidenceCountryCode("CH");
        person.setNationalityCountryCode("CH");
        person.setIndCompany(true);

        COwner owner = new COwner();
        owner.setPerson(person);

        COwnershipData ownershipData = new COwnershipData();
        ownershipData.setOwnerList(new ArrayList<>());
        ownershipData.getOwnerList().add(owner);
        return ownershipData;
    }

    @Override
    public void save(CMarkOldInternationalRegistration oldRegistration) {
        if (Objects.isNull(oldRegistration)) {
            return;
        }

        IpMarkOldInternationalRegistration ipMarkOldInternationalRegistration = markOldInternationalRegistrationMapper.toEntity(oldRegistration);
        ipMarkOldInternationalRegistrationRepository.save(ipMarkOldInternationalRegistration);
    }
}
