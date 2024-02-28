package bg.duosoft.ipas.core.service.impl.application;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.mark.MarkMapper;
import bg.duosoft.ipas.core.mapper.patent.PatentMapper;
import bg.duosoft.ipas.core.mapper.process.ProcessIdMapper;
import bg.duosoft.ipas.core.model.IpasApplicationSearchResult;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFilingData;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReceptionCorrespondent;
import bg.duosoft.ipas.core.model.reception.CReceptionRequest;
import bg.duosoft.ipas.core.service.application.SearchApplicationService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.reception.ReceptionRequestService;
import bg.duosoft.ipas.enums.ApplicationSearchType;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.enums.RepresentativeType;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.file.FileTypeUtils;
import bg.duosoft.ipas.util.process.ProcessTypeUtils;
import bg.duosoft.ipas.util.reception.ReceptionCorrespondentUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@LogExecutionTime
public class SearchApplicationServiceImpl implements SearchApplicationService {

    @Autowired
    private FileService fileService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReceptionRequestService receptionRequestService;

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private IpProcRepository ipProcRepository;

    @Autowired
    private MarkMapper markMapper;

    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    private ProcessIdMapper processIdMapper;

    @Autowired
    private FileIdMapper fileIdMapper;

    @Override
    public List<IpasApplicationSearchResult> selectApplications(List<FileType> fileTypes, String searchText, ApplicationSearchType applicationSearchType, boolean includeReceptions) {
        if (CollectionUtils.isEmpty(fileTypes))
            return null;

        String code = fileTypes.get(0).code();
        if (FileTypeUtils.isMarkFileType(code)) {
            return selectMarkApplicationsBySearchType(applicationSearchType, searchText, fileTypes, includeReceptions);
        }
        if (FileTypeUtils.isPatentFileType(code)) {
            return selectPatentApplicationBySearchType(applicationSearchType, searchText, fileTypes, includeReceptions);
        }
        throw new RuntimeException("Invalid file type " + code);
    }

    @Override
    public IpasApplicationSearchResult selectApplication(CFileId id) {
        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case MARK:
            case DIVISIONAL_MARK:
            case GEOGRAPHICAL_INDICATIONS:
            case INTERNATIONAL_MARK_I:
            case INTERNATIONAL_MARK_R:
            case ACP:
            case INTERNATIONAL_MARK_B:
                return selectMarkApplicationsById(id);
            case PLANTS_AND_BREEDS:
            case SPC:
            case UTILITY_MODEL:
            case DESIGN:
            case EU_PATENT:
            case PATENT:
                return selectPatentApplicationById(id);
            default:
                throw new RuntimeException("Invalid file type " + fileType.code());
        }
    }

    private IpasApplicationSearchResult selectMarkApplicationsById(CFileId id) {
        if (Objects.isNull(id))
            return null;

        IpFilePK ipFilePK = fileIdMapper.toEntity(id);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        if (Objects.isNull(ipMark))
            return null;

        return convertMark(ipMark);
    }

    private IpasApplicationSearchResult selectPatentApplicationById(CFileId id) {
        if (Objects.isNull(id))
            return null;

        IpFilePK ipFilePK = fileIdMapper.toEntity(id);
        IpPatent ipPatent = ipPatentRepository.findById(ipFilePK).orElse(null);
        if (Objects.isNull(ipPatent))
            return null;

        return convertPatent(ipPatent);
    }

    private List<IpasApplicationSearchResult> selectMarkApplicationsBySearchType(ApplicationSearchType applicationSearchType, String searchText, List<FileType> fileTypes, boolean includeReceptions) {
        List<String> fileTypeCodes = fileTypes.stream().map(r -> r.code()).collect(Collectors.toList());
        switch (applicationSearchType) {
            case FILE_NUMBER: {
                List<IpasApplicationSearchResult> ipasApplicationSearchResults = new ArrayList<>();
                if (includeReceptions) {
                    addMarkReceptionFilesToResult(searchText, fileTypeCodes, ipasApplicationSearchResults);
                }

                List<IpMark> result = ipMarkRepository.findAllByPk_FileNbrAndPk_FileTypIn(Integer.valueOf(searchText), fileTypeCodes);
                if (!CollectionUtils.isEmpty(result)) {
                    List<IpasApplicationSearchResult> markResult = convertMarkList(result);
                    if (!CollectionUtils.isEmpty(markResult)) {
                        ipasApplicationSearchResults.addAll(markResult);
                    }
                }
                return ipasApplicationSearchResults;
            }
            case REGISTRATION_NUMBER:
                List<IpMark> result;
                Integer searchTextAsNumber = Integer.valueOf(searchText);
                if (FileType.getInternationalMarkFileTypes().stream().anyMatch(ic -> fileTypeCodes.contains(ic))) {
                    result = ipMarkRepository.findMarkOnSearchTypeRegNumberAndFileTypeInternationalMark(searchTextAsNumber,'%'+String.valueOf(searchTextAsNumber)+'%');
                } else {
                    result = ipMarkRepository.findAllByFile_RegistrationNbrAndPk_FileTypIn(searchTextAsNumber, fileTypeCodes);
                }
                if (CollectionUtils.isEmpty(result))
                    return null;

                return convertMarkList(result);
            default:
                throw new RuntimeException("Invalid search type ! " + applicationSearchType.code());
        }
    }


    private List<IpasApplicationSearchResult> selectPatentApplicationBySearchType(ApplicationSearchType applicationSearchType, String searchText, List<FileType> fileTypes, boolean includeReceptions) {
        List<String> fileTypeCodes = fileTypes.stream().map(FileType::code).collect(Collectors.toList());
        switch (applicationSearchType) {
            case FILE_NUMBER: {
                List<IpasApplicationSearchResult> ipasApplicationSearchResults = new ArrayList<>();
                if (includeReceptions) {
                    addPatentReceptionFilesToResult(searchText, fileTypeCodes, ipasApplicationSearchResults);
                }

                List<IpPatent> result = ipPatentRepository.findAllByPk_FileNbrAndPk_FileTypIn(Integer.valueOf(searchText), fileTypeCodes);
                if (!CollectionUtils.isEmpty(result)) {
                    List<IpasApplicationSearchResult> patentResult = convertPatentList(result);
                    if (!CollectionUtils.isEmpty(patentResult)) {
                        ipasApplicationSearchResults.addAll(patentResult);
                    }
                }
                return ipasApplicationSearchResults;
            }
            case REGISTRATION_NUMBER:
                List<IpPatent> result = ipPatentRepository.findAllByFile_RegistrationNbrAndPk_FileTypIn(Integer.valueOf(searchText), fileTypeCodes);
                if (CollectionUtils.isEmpty(result))
                    return null;

                return convertPatentList(result);
            default:
                throw new RuntimeException("Invalid search type ! " + applicationSearchType.code());
        }
    }

    private IpasApplicationSearchResult convertMark(IpMark ipMark) {
        CMark mark = markMapper.toCore(ipMark, false);
        CFile file = mark.getFile();

        IpProcPK ipProcPK = processIdMapper.toEntity(file.getProcessId());
        IpProc ipProc = ipProcRepository.findById(ipProcPK).orElse(null);
        if (Objects.isNull(ipProc))
            return null;

        CFilingData filingData = file.getFilingData();
        CPerson servicePerson = file.getServicePerson();

        return new IpasApplicationSearchResult(
                file.getFileId(), mark.getSignData().getMarkName(), getOwners(file), getRepresentatives(file), servicePerson,
                ipProc.getStatusCode().getStatusName(), filingData.getFilingDate(), file.getRegistrationData(), file.getProcessId(), mark.getSignData().getSignType() == MarkSignType.FIGURATIVE, false);
    }

    private IpasApplicationSearchResult convertPatent(IpPatent ipPatent) {
        CPatent patent = patentMapper.toCore(ipPatent, false);
        CFile file = patent.getFile();
        IpProcPK ipProcPK = processIdMapper.toEntity(file.getProcessId());
        IpProc ipProc = ipProcRepository.findById(ipProcPK).orElse(null);
        if (Objects.isNull(ipProc))
            return null;

        CFilingData filingData = file.getFilingData();
        return new IpasApplicationSearchResult(
                file.getFileId(), patent.getTechnicalData().getTitle(), getOwners(file), getRepresentatives(file), file.getServicePerson(),
                ipProc.getStatusCode().getStatusName(), filingData.getFilingDate(), file.getRegistrationData(), file.getProcessId(), false, ProcessTypeUtils.isPatentInSecretStatus(file, statusService));
    }

    private List<IpasApplicationSearchResult> convertMarkList(List<IpMark> marks) {
        return marks.stream()
                .map(this::convertMark)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<IpasApplicationSearchResult> convertPatentList(List<IpPatent> patents) {
        return patents.stream()
                .map(this::convertPatent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<CPerson> getOwners(CFile file) {
        List<CPerson> owners = new ArrayList<>();
        List<COwner> ownerList = file.getOwnershipData().getOwnerList();
        if (!CollectionUtils.isEmpty(ownerList)) {
            owners.addAll(ownerList.stream().map(COwner::getPerson).collect(Collectors.toList()));
        }
        return owners;
    }

    private List<CRepresentative> getRepresentatives(CFile file) {
        List<CRepresentative> representatives = new ArrayList<>();
        List<CRepresentative> representativeList = file.getRepresentationData().getRepresentativeList();
        if (!CollectionUtils.isEmpty(representativeList)) {
            List<CRepresentative> representativePersons = representativeList.stream()
                    .filter(cRepresentative -> !RepresentativeType.CORRESPONDENCE_ADDRESS.getValue().equals(cRepresentative.getRepresentativeType()))
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(representativePersons))
                representatives.addAll(representativePersons);
        }
        return representatives;
    }

    private void addMarkReceptionFilesToResult(String searchText, List<String> fileTypes, List<IpasApplicationSearchResult> ipasApplicationSearchResults) {
        List<CFile> receptionFiles = fileService.selectMarkReceptionFiles(fileTypes, Integer.valueOf(searchText));
        if (!CollectionUtils.isEmpty(receptionFiles)) {
            for (CFile receptionFile : receptionFiles) {
                ipasApplicationSearchResults.add(convertReceptionFile(receptionFile));
            }
        }
    }

    private void addPatentReceptionFilesToResult(String searchText, List<String> fileTypes, List<IpasApplicationSearchResult> ipasApplicationSearchResults) {
        List<CFile> receptionFiles = fileService.selectPatentReceptionFiles(fileTypes, Integer.valueOf(searchText));
        if (!CollectionUtils.isEmpty(receptionFiles)) {
            for (CFile receptionFile : receptionFiles) {
                ipasApplicationSearchResults.add(convertReceptionFile(receptionFile));
            }
        }
    }

    private IpasApplicationSearchResult convertReceptionFile(CFile file) {
        CFileId fileId = file.getFileId();

        List<CPerson> owners = new ArrayList<>();
        List<CRepresentative> representatives = new ArrayList<>();
        boolean isFigurative = false;
        CReceptionRequest receptionRequest = receptionRequestService.selectReceptionByFileId(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr());
        if (Objects.nonNull(receptionRequest)) {
            isFigurative = receptionRequest.getName().equalsIgnoreCase(DefaultValue.EMPTY_OBJECT_NAME);

            List<CReceptionCorrespondent> correspondents = receptionRequest.getCorrespondents();
            List<CReceptionCorrespondent> representativeCorrespondents = ReceptionCorrespondentUtils.selectRepresentativeCorrespondents(correspondents);
            if (!CollectionUtils.isEmpty(representativeCorrespondents)) {
                representatives = representativeCorrespondents.stream()
                        .map(c -> {
                            CPerson person = personService.selectPersonByPersonNumberAndAddressNumber(c.getPersonNbr(), c.getAddressNbr());
                            if (Objects.isNull(person)) {
                                return null;
                            }
                            return new CRepresentative(c.getRepresentativeType(), person);
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
            List<CReceptionCorrespondent> ownerCorrespondentss = ReceptionCorrespondentUtils.selectApplicantCorrespondents(correspondents);
            if (!CollectionUtils.isEmpty(ownerCorrespondentss)) {
                owners = ownerCorrespondentss.stream()
                        .map(c -> personService.selectPersonByPersonNumberAndAddressNumber(c.getPersonNbr(), c.getAddressNbr()))
                        .collect(Collectors.toList());
            }
        }

        return IpasApplicationSearchResult.builder()
                .fileId(fileId)
                .description(file.getTitle())
                .owners(owners)
                .representatives(representatives)
                .servicePerson(file.getServicePerson())
                .status(statusService.getStatusMap().get(file.getProcessId().getProcessType() + "-" + file.getProcessSimpleData().getStatusCode()))
                .filingDate(file.getFilingData().getFilingDate())
                .registrationData(file.getRegistrationData())
                .processId(file.getProcessId())
                .isFigurative(isFigurative)
                .isPatentInSecretStatus(ProcessTypeUtils.isPatentInSecretStatus(file, statusService))
                .build();
    }

}
