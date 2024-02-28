package bg.duosoft.ipas.core.service.impl.userdoc;

import bg.duosoft.abdocs.model.DocCorrespondents;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.ipas.core.mapper.userdoc.UserdocOldDocumentMapper;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocOldDocument;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.core.service.userdoc.UserdocOldDocumentService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPanelService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.integration.abdocs.converter.CorrespondentConverter;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocOldDocumentRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserdocOldDocumentServiceImpl implements UserdocOldDocumentService {
    @Autowired
    private IpUserdocOldDocumentRepository ipUserdocOldDocumentRepository;
    @Autowired
    private UserdocOldDocumentMapper userdocOldDocumentMapper;
    @Autowired
    private CorrespondentConverter correspondentConverter;
    @Autowired
    private ReceptionService receptionService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserdocTypesService userdocTypesService;
    @Autowired
    private UserdocTypeConfigService userdocTypeConfigService;
    @Autowired
    private UserdocPanelService userdocPanelService;

    @Override
    public List<CUserdocOldDocument> selectAll() {
        return userdocOldDocumentMapper.toCoreList(ipUserdocOldDocumentRepository.findAll());
    }

    @Override
    public CReceptionResponse insertOldUserdoc(CUserdocOldDocument oldDocument, Document abdocsDocument, CFileId parentFileId, Boolean registerInAbdocs) {
        CReception reception = createReceptionObject(oldDocument, abdocsDocument, parentFileId, registerInAbdocs);
        CReceptionResponse receptionResponse = receptionService.createReception(reception);

        if (Objects.nonNull(oldDocument.getResponsibleUserId())) {
            CProcessId processId = processService.selectUserdocProcessId(receptionResponse.getDocId());
            processService.updateResponsibleUser(oldDocument.getResponsibleUserId(), processId.getProcessType(), processId.getProcessNbr());
        }
        return receptionResponse;
    }

    private CReception createReceptionObject(CUserdocOldDocument oldDocument, Document abdocsDocument, CFileId fileId, Boolean registerInAbdocs) {
        CReception receptionForm = new CReception();
        receptionForm.setEntryDate(DateUtils.removeHoursFromDate(oldDocument.getFilingDate()));
        receptionForm.setOriginalExpected(false);
        receptionForm.setSubmissionType(SubmissionType.COUNTER.code());
        receptionForm.setUserdoc(new CReceptionUserdoc());
        receptionForm.getUserdoc().setUserdocType(oldDocument.getNewUserdocType());
        receptionForm.getUserdoc().setWithoutCorrespondents(Objects.isNull(abdocsDocument) ? true : UserdocUtils.isUserdocWithoutCorrespondents(userdocTypesService.selectUserdocTypeById(oldDocument.getNewUserdocType()), userdocPanelService));
        fillDocflowData(abdocsDocument, registerInAbdocs, oldDocument, receptionForm);
        userdocTypeConfigService.defineUpperProc(receptionForm.getUserdoc(), fileId);
        //Init persons
        if (Objects.nonNull(abdocsDocument)) {
            List<CUserdocPerson> userdocPersons = new ArrayList<>();
            COwnershipData ownershipData = new COwnershipData();
            ownershipData.setOwnerList(new ArrayList<>());
            for (DocCorrespondents docCorrespondent : abdocsDocument.getDocCorrespondents()) {
                if (Objects.nonNull(docCorrespondent)) {
                    COwner owner = new COwner();
                    CPerson person = correspondentConverter.convertToCPerson(docCorrespondent);
                    if (Objects.isNull(person.getNationalityCountryCode())) {
                        person.setNationalityCountryCode(DefaultValue.BULGARIA_CODE);
                    }
                    if (Objects.isNull(person.getResidenceCountryCode())) {
                        person.setResidenceCountryCode(person.getNationalityCountryCode());
                    }
                    owner.setPerson(person);
                    ownershipData.getOwnerList().add(owner);
                }
            }
            receptionForm.setOwnershipData(ownershipData);
            receptionForm.getUserdoc().setUserdocPersons(userdocPersons);
        }

        return receptionForm;
    }

    private void fillDocflowData(Document abdocsDocument, Boolean registerInAbdocs, CUserdocOldDocument oldDocument,  CReception receptionForm) {
        if (registerInAbdocs) {
            receptionForm.setRegisterInDocflowSystem(true);
            receptionForm.getUserdoc().setExternalRegistrationNumber(oldDocument.getExternalSystemId());
        } else {
            receptionForm.setRegisterInDocflowSystem(false);
            receptionForm.setDocflowSystemId(abdocsDocument.getDocId());
        }
    }


}
