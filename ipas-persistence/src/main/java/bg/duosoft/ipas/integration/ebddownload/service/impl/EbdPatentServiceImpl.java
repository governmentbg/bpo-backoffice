package bg.duosoft.ipas.integration.ebddownload.service.impl;

import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.bpo.ebd.ebddpersistence.service.EbdDPatentService;
import bg.bpo.ebd.ebddpersistence.service.EbdDSearchRequest;
import bg.bpo.ebd.ebddpersistence.service.EbdDSearchService;
import bg.duosoft.abdocs.model.Document;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.ebddownload.CEbdPatentSearchResult;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionEuPatent;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.reception.ReceptionService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.Status;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.integration.ebddownload.mapper.EbdPatentMapper;
import bg.duosoft.ipas.integration.ebddownload.mapper.EbdPatentSearchResultMapper;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import bg.duosoft.ipas.util.person.OwnerUtils;
import bg.duosoft.ipas.util.person.SearchPersonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EbdPatentServiceImpl implements EbdPatentService {

    @Autowired
    private EbdDPatentService ebdDPatentService;

    @Autowired
    private EbdPatentMapper ebdPatentMapper;

    @Autowired
    private EbdDSearchService ebdDSearchService;
    @Autowired
    private EbdPatentSearchResultMapper ebdPatentSearchResultMapper;

    @Autowired
    private DailyLogService dailyLogService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;


    @Autowired
    private ProcessService processService;

    @Autowired
    private PatentService patentService;

    @Override
    public List<CEbdPatentSearchResult> searchEbdPatents(String fileNumber, String registrationNumber) {
        EbdDSearchRequest req = new EbdDSearchRequest(fileNumber, registrationNumber == null ? null : registrationNumber.toString());
        return ebdPatentSearchResultMapper.toCoreList(ebdDSearchService.findEbdDPatents(req));
    }


    @Override
    public CEbdPatent selectByFileNumber(Integer fileNumber) {
        if (Objects.isNull(fileNumber))
            return null;

        EbdDPatent ebdDPatentById = ebdDPatentService.getEbdDPatentById(String.valueOf(fileNumber));
        if (Objects.isNull(ebdDPatentById))
            return null;

        return ebdPatentMapper.toCore(ebdDPatentById);
    }

    @Override
    public CEbdPatent selectByRegistationNumber(Integer registrationNumber) {
        if (Objects.isNull(registrationNumber))
            return null;

        EbdDPatent ebdDPatentByRegistrationNumber = ebdDPatentService.getEbdDPatentByRegistrationNumber(String.valueOf(registrationNumber));
        if (Objects.isNull(ebdDPatentByRegistrationNumber))
            return null;

        return ebdPatentMapper.toCore(ebdDPatentByRegistrationNumber);
    }

    @Override
    public CEbdPatent updateEbdPatentFileNumber(String id, Long fileNumber) {
        if (StringUtils.isEmpty(id) || Objects.isNull(fileNumber))
            return null;

        EbdDPatent ebdDPatent = ebdDPatentService.updateEndDPatentFileNbr(id, fileNumber);
        if (Objects.isNull(ebdDPatent))
            return null;

        return ebdPatentMapper.toCore(ebdDPatent);
    }

    @Override
    public boolean save(CEbdPatent patent, boolean isAdminRole) {
        CFileId fileId = EuPatentUtils.generateEUPatentCFileId(patent);
        CReception receptionForm = null;
        
        try {
            Date workingDate = dailyLogService.getWorkingDate();
            receptionForm = createReceptionFormObject(patent, workingDate);
            setAbdocsDocumentIdIfExists(fileId, receptionForm,isAdminRole);
            receptionService.createReception(receptionForm);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

        try {
            CProcessId euProcessId = processService.selectFileProcessId(fileId);
            processService.updateStatusCodeAndDateById(Status.EU_WITHOUT_REQUEST_FOR_VALIDATION,euProcessId);
        }catch (Exception e) {
            setAbdocsDocumentIdIfExists(fileId, receptionForm,isAdminRole);
            patentService.deletePatent(fileId);
            if (Objects.nonNull(receptionForm.getDocflowSystemId())){
                abdocsServiceAdmin.deleteDocumentHierarchy(receptionForm.getDocflowSystemId());
            }
            log.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    private void setAbdocsDocumentIdIfExists(CFileId fileId, CReception receptionForm, boolean isAdminRole) {
        Integer documentId = null;
        if (isAdminRole){
            documentId = abdocsServiceAdmin.selectDocumentIdByRegistrationNumber(fileId.createFilingNumber());
        }else{
             documentId = abdocsService.selectDocumentIdByRegistrationNumber(fileId.createFilingNumber());
        }

        if (documentId != null) {
            receptionForm.setDocflowSystemId(documentId);
        }
    }

    private CReception createReceptionFormObject(CEbdPatent patent, Date workingDate) {
        CReception receptionForm = new CReception();

        receptionForm.setSubmissionType(SubmissionType.COUNTER.code());
        receptionForm.setFile(new CReceptionFile());
        receptionForm.getFile().setTitle(patent.getTitle());
        receptionForm.getFile().setApplicationType(applicationTypeService.selectApplicationTypeByFileType(FileType.EU_PATENT.code()));
        receptionForm.setOriginalExpected(false);
        receptionForm.setEntryDate(workingDate);
        receptionForm.setRegisterReceptionRequest(true);
        receptionForm.setRegisterInDocflowSystem(true);

        CReceptionEuPatent receptionEuPatent = new CReceptionEuPatent();
        receptionEuPatent.setObjectNumber(Integer.valueOf(patent.getFilingNumber()));
        receptionForm.setEuPatent(receptionEuPatent);

        COwnershipData ownershipData = new COwnershipData();
        ownershipData.setOwnerList(new ArrayList<>());

        List<CPerson> owners = patent.getOwners();
        if (!CollectionUtils.isEmpty(owners)) {
            for (CPerson person : owners) {
                CCriteriaPerson cCriteriaPerson = SearchPersonUtils.createPersonSearchCriteria(person);
                List<CPerson> persons = personService.findPersons(cCriteriaPerson);
                if (!CollectionUtils.isEmpty(persons) && persons.size() == DefaultValue.ONE_RESULT_SIZE) {
                    CPerson existingPerson = persons.get(DefaultValue.FIRST_RESULT);
                    ownershipData.getOwnerList().add(OwnerUtils.convertToOwner(ownershipData, existingPerson));
                } else {
                    CPerson newPerson = personService.mergeOrInsertPersonAddress(person);
                    ownershipData.getOwnerList().add(OwnerUtils.convertToOwner(ownershipData, newPerson));
                }
            }
        }

        receptionForm.setOwnershipData(ownershipData);
        return receptionForm;
    }
}
