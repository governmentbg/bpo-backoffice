package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.service.UserdocExtraDataService;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocPersonService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.core.service.userdoc.config.UserdocTypeConfigService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.util.default_value.DefaultValueUtils;
import bg.duosoft.ipas.util.person.RepresentativeUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that calls LocalUserdocReceptionServiceImpl, to save the Original IPAS tables, after that the NEW tables (EXT_CORE.USERDOC_PERSONS are processed)
 */
@Slf4j
@Service
@Transactional
@LogExecutionTime
public class UserdocReceptionServiceImpl {

    private static final String RENEWAL_PANEL = "Renewal";

    private static final List<Integer> excludedResponsibleUsersOnInheritance = Arrays.asList(1, 3001);

    @Autowired
    private UserdocPersonService userdocPersonService;

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private UserdocExtraDataService userdocExtraDataService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private SearchApplicationService searchApplicationService;

    @Autowired
    private MarkService markService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private DefaultValueUtils defaultValueUtils;
    @Autowired
    private LocalUserdocReceptionServiceImpl localUserdocReceptionService;

    @Autowired
    private UserdocTypeConfigService userdocTypeConfigService;

    public CReceptionResponse insertUserdocReception(CReception receptionForm) {
        CReceptionResponse resp = localUserdocReceptionService.insertUserdocReception(receptionForm);
        processAdditionalUserdocReceptionData(receptionForm, resp);
        return resp;
    }

    private void processAdditionalUserdocReceptionData(CReception receptionForm, CReceptionResponse response) {
        userdocTypeConfigService.defineResponsibleUserOnReception(response.getDocId(),receptionForm.getSubmissionType());
        processPersons(receptionForm, response.getDocId());
        processRenewalExpirationDate(response.getDocId());
    }

    private void processPersons(CReception receptionForm, CDocumentId documentId) {
        COwnershipData cOwnershipData = receptionForm.getOwnershipData();
        if (Objects.nonNull(cOwnershipData)) {
            List<COwner> ownerList = cOwnershipData.getOwnerList();
            if (!CollectionUtils.isEmpty(ownerList)) {
                for (COwner cOwner : ownerList) {
                    CUserdocPerson applicant = UserdocPersonUtils.convertToUserdocPerson(cOwner.getPerson(), UserdocPersonRole.APPLICANT, false, null,null);
                    userdocPersonService.savePerson(documentId, applicant);
                }
            }
        }

        CRepresentationData representationData = receptionForm.getRepresentationData();
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (!CollectionUtils.isEmpty(representativeList)) {
                for (CRepresentative cRepresentative : representativeList) {
                    RepresentativeType representativeType = RepresentativeUtils.convertRepresentativeTypeValueToEnum(cRepresentative.getRepresentativeType());
                    CUserdocPerson representative = UserdocPersonUtils.convertToUserdocPerson(cRepresentative, UserdocPersonRole.REPRESENTATIVE, false, null);
                    userdocPersonService.savePerson(documentId, representative);
                }
            }
        }
        if (!CollectionUtils.isEmpty(receptionForm.getUserdoc().getUserdocPersons())) {
            for (CUserdocPerson p : receptionForm.getUserdoc().getUserdocPersons()) {
                userdocPersonService.savePerson(documentId, p);
            }
        }

        executeTakeFromOwners(receptionForm, documentId);
        executeTakeFromRepresentatives(receptionForm, documentId);
    }

    private void processRenewalExpirationDate(CDocumentId docId) {
        CUserdoc userdoc = userdocService.findUserdoc(docId.getDocOrigin(), docId.getDocLog(), docId.getDocSeries(), docId.getDocNbr());
        if (Objects.nonNull(userdoc)) {
            List<CUserdocPanel> panels = userdoc.getUserdocType().getPanels();
            Optional<CUserdocPanel> any = panels.stream().filter(cUserdocPanel -> cUserdocPanel.getPanel().equalsIgnoreCase(RENEWAL_PANEL)).findAny();
            if (any.isPresent()) {
                CFile file = UserdocUtils.selectUserdocMainObjectFile(userdoc, markService, patentService);
                if (Objects.nonNull(file)) {
                    Date renewalExpirationDate = defaultValueUtils.getRenewalExpirationDate(file);
                    if (Objects.nonNull(renewalExpirationDate)) {
                        userdocExtraDataService.save(docId, UserdocExtraDataTypeCode.RENEWAL_NEW_EXPIRATION_DATE.name(), CUserdocExtraDataValue.builder().dateValue(renewalExpirationDate).build());
                    }
                }
            }
        }
    }

    private CFileId getMasterFileId(CReception reception) {
        return reception.getUserdoc().isRelatedToFile() ? reception.getUserdoc().getFileId() : getMasterFileId(reception.getUserdoc().getDocumentId());
    }

    private List<CPerson> selectOwnersOfRelatedObject(CFileId relatedObjectFileId) {

        if (Objects.isNull(relatedObjectFileId))
            return null;

        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(relatedObjectFileId);
        if (Objects.isNull(ipasApplicationSearchResult))
            return null;

        return ipasApplicationSearchResult.getOwners();
    }

    private CFileId getMasterFileId(CDocumentId documentId) {
        CProcess userdocProcess = processService.selectUserdocProcess(documentId, false);
        CFileId res = processService.selectTopProcessFileId(userdocProcess.getProcessId());
        return res;
    }

    private FileType selectUserdocObjectNumberFileType(String objectNumber) {
        if (StringUtils.isEmpty(objectNumber))
            throw new RuntimeException("Affected document is empty !");

        String[] split = objectNumber.split("/");
        if (split.length != 4)
            throw new RuntimeException("Affected document id is wrong!");

        return FileType.selectByCode(split[1]);
    }

    private void executeTakeFromOwners(CReception receptionForm, CDocumentId documentId) {
        CReceptionUserdoc receptionUserdoc = receptionForm.getUserdoc();
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(receptionUserdoc.getUserdocType());
        if (Objects.nonNull(cUserdocType)) {
            List<CUserdocPersonRole> roles = cUserdocType.getRoles().stream()
                    .filter(userdocPersonRole -> Objects.nonNull(userdocPersonRole.getIndTakeFromOwner()))
                    .filter(CUserdocPersonRole::getIndTakeFromOwner)
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(roles)) {
                List<CPerson> owners = selectOwnersOfRelatedObject(getMasterFileId(receptionForm));
                if (!CollectionUtils.isEmpty(owners)) {
                    for (CUserdocPersonRole role : roles) {
                        List<CUserdocPerson> userdocPersonList = UserdocPersonUtils.convertToUserdocPerson(owners, role.getRole(),null);
                        userdocPersonService.savePersonList(documentId, userdocPersonList);
                    }
                }
            }
        }
    }

    private void executeTakeFromRepresentatives(CReception receptionForm, CDocumentId documentId) {
        CReceptionUserdoc receptionUserdoc = receptionForm.getUserdoc();
        CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeById(receptionUserdoc.getUserdocType());
        if (Objects.nonNull(cUserdocType)) {
            List<CUserdocPersonRole> roles = cUserdocType.getRoles().stream()
                    .filter(userdocPersonRole -> Objects.nonNull(userdocPersonRole.getIndTakeFromRepresentative()))
                    .filter(CUserdocPersonRole::getIndTakeFromRepresentative)
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(roles)) {
                List<CRepresentative> representatives = selectRepresentativesOfRelatedObject(getMasterFileId(receptionForm));
                if (!CollectionUtils.isEmpty(representatives)) {
                    for (CUserdocPersonRole role : roles) {
                        List<CUserdocPerson> userdocPersonList = UserdocPersonUtils.convertToUserdocPerson(representatives, role.getRole());
                        userdocPersonService.savePersonList(documentId, userdocPersonList);
                    }
                }
            }
        }
    }

    private List<CRepresentative> selectRepresentativesOfRelatedObject(CFileId relatedObjectFileId) {
        if (Objects.isNull(relatedObjectFileId))
            return null;

        IpasApplicationSearchResult ipasApplicationSearchResult = searchApplicationService.selectApplication(relatedObjectFileId);
        if (Objects.isNull(ipasApplicationSearchResult))
            return null;

        return ipasApplicationSearchResult.getRepresentatives();
    }


}
