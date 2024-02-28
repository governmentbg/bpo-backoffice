package bg.duosoft.ipas.core.service.impl.reception.ebd;

import bg.bpo.ebd.ebddpersistence.entity.*;
import bg.duosoft.ipas.IpasServiceUtils;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.patent.*;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionFile;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.nomenclature.LawService;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.integration.ebddownload.mapper.EbdPersonBaseMapper;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.eupatent.EuPatentUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EbddownloadToIpasPatentConvertor {
    @Autowired
    private EbdPersonBaseMapper ebdPersonBaseMapper;

    @Autowired
    private ApplicationTypeService applicationTypeService;
    @Autowired
    private LawService lawService;

    public CPatent convertPatent(EbdDPatent ebdDPatent, Date entitlementDate, CReception receptionForm) {
        CPatent cPatent = new CPatent();
        cPatent.setRowVersion(DefaultValue.ROW_VERSION);

        CFileId fileId = EuPatentUtils.generateEUPatentCFileId(ebdDPatent);

        CFile file = new CFile();
        file.setRowVersion(DefaultValue.ROW_VERSION);
        file.setFileId(fileId);
        file.setFilingData(new CFilingData());
        file.setOwnershipData(new COwnershipData());
        file.getOwnershipData().setOwnerList(new ArrayList<>());
        file.setRegistrationData(new CRegistrationData());

        // setting application type and sub-type
        String at = applicationTypeService.selectApplicationTypeByFileType(FileType.EU_PATENT.code());
        String st = applicationTypeService.getDefaultApplicationSubtype(at);
        file.getFilingData().setApplicationType(at);
        file.getFilingData().setApplicationSubtype(st);
        file.getFilingData().setLawCode(lawService.getLawIdByApplicationTypeAndSubtype(at, st));
        file.getFilingData().setFilingDate(ebdDPatent.getDtappli());
        if (Objects.nonNull(receptionForm.getSubmissionType()) && receptionForm.getSubmissionType().equals(SubmissionType.ELECTRONIC.code())){
            file.getFilingData().setPublicationTyp(FilePublicationTyp.ELECTRONIC.code());
        }else{
            file.getFilingData().setPublicationTyp(FilePublicationTyp.PAPER.code());
        }
        
        cPatent.setFile(file);

        boolean serviceConfigured = fillAgents(receptionForm, file);
        fillOwners(receptionForm, file, ebdDPatent.getOwners(), serviceConfigured);
        fillInventors(ebdDPatent, cPatent);
        fillPriorities(ebdDPatent, cPatent, file);

        CTechnicalData technicalData = new CTechnicalData();
        technicalData.setTitle(receptionForm.getFile().getTitle());
        technicalData.setEnglishTitle(ebdDPatent.getEngtitle());

        cPatent.setTechnicalData(technicalData);

        Set<EbdDClassin> classins = ebdDPatent.getClassins();
        if (!CollectionUtils.isEmpty(classins)) {
            CTechnicalData technicalData1 = cPatent.getTechnicalData();
            if (Objects.isNull(technicalData1.getIpcClassList()))
                technicalData1.setIpcClassList(new ArrayList<>());

            fillClassins(technicalData1.getIpcClassList(), ebdDPatent.getClassins());
        }

        Set<EbdDCpcClass> cpcClasses = ebdDPatent.getCpcClasses();
        if (!CollectionUtils.isEmpty(cpcClasses)) {
            CTechnicalData technicalData1 = cPatent.getTechnicalData();
            if (Objects.isNull(technicalData1.getCpcClassList()))
                technicalData1.setCpcClassList(new ArrayList<>());

            fillCpcClasses(technicalData1.getCpcClassList(), cpcClasses);
        }


        CRegistrationId registrationId = new CRegistrationId();

        file.getRegistrationData().setRegistrationId(registrationId);
        String dup = ebdDPatent.getExtidappli().replaceFirst("^EP\\d+\\.", "");
        registrationId.setRegistrationDup(dup);

        if (!StringUtils.isEmpty(ebdDPatent.getIdpatent())) {
            registrationId.setRegistrationNbr(Long.valueOf(ebdDPatent.getIdpatent()));
        }

        if (ebdDPatent.getDtptexpi() != null) {
            file.getRegistrationData().setExpirationDate(ebdDPatent.getDtptexpi());
        }
        if (ebdDPatent.getDtgrant() != null) {
            file.getRegistrationData().setRegistrationDate(ebdDPatent.getDtgrant());
        }


        if (ebdDPatent.getPct() != null) {
            CPctApplicationData ipasPct = new CPctApplicationData();
            cPatent.setPctApplicationData(ipasPct);
            EbdDPct pct = ebdDPatent.getPct();

            if (pct.getPctPhase() != null) {
                ipasPct.setPctPhase(Long.valueOf(pct.getPctPhase()));
            }
            if (pct.getPctApplicationDate() != null) {
                ipasPct.setPctApplicationDate(pct.getPctApplicationDate());
            }
            ipasPct.setPctApplicationId(pct.getPctApplicationId());
            ipasPct.setPctPublicationCountryCode(pct.getPctPublicationCountryCode());
            ipasPct.setPctPublicationId(pct.getPctPublicationId());
            if (pct.getPctApplicationDate() != null) {
                ipasPct.setPctPublicationDate(pct.getPctPublicationDate());
            }
        }

        /*
            [14:59:08] Svetlozar.Tonev: а за датата от която започва срокът на закрила на патент:
            - ако има PCT - dtpctappli;
            - ако не - dtappli.
            [15:00:56] Svetlozar.Tonev: обаче има едно усложнение:
            - ако заявката е разделена, то child-заявките имат за начало на закрила така изчисленото за техния parent;
            [15:01:50] Svetlozar.Tonev: информацията за това е в division - таблицата
         */
        file.getRegistrationData().setEntitlementDate(entitlementDate);

        return cPatent;
    }

    private void fillPriorities(EbdDPatent ebdDPatent, CPatent result, CFile file) {
        if (ebdDPatent.getPriorities() != null) {
            file.setPriorityData(new CPriorityData());
            file.getPriorityData().setParisPriorityList(new ArrayList<>());

            for (EbdDPriority priority : ebdDPatent.getPriorities()) {
                CParisPriority p = toParisPriority(priority);
                //okaza se che ima dublirashti se priorities i zatova trqbva da se ignorirat!!!!
                if (!checkPriorityExists(result.getFile().getPriorityData().getParisPriorityList(), p)) {
                    file.getPriorityData().getParisPriorityList().add(p);
                }

            }
            file.getPriorityData().setHasParisPriorityData(!CollectionUtils.isEmpty(ebdDPatent.getPriorities()));
        }
    }

    private boolean fillAgents(CReception receptionForm, CFile file) {
        boolean serviceConfigured = false;
        CRepresentationData representationData = receptionForm.getRepresentationData();
        if (Objects.nonNull(representationData)) {
            List<CRepresentative> representativeList = representationData.getRepresentativeList();
            if (!CollectionUtils.isEmpty(representativeList)) {
                file.setRepresentationData(new CRepresentationData());
                file.getRepresentationData().setRepresentativeList(new ArrayList<>());

                int cnt = 0;
                for (CRepresentative cRepresentative : representativeList) {
                    if (cnt++ == 0) {
                        file.setServicePerson(cRepresentative.getPerson());
                        serviceConfigured = true;
                    }
                    file.getRepresentationData().getRepresentativeList().add(cRepresentative);
                }

            }
        }
        return serviceConfigured;
    }

    private void fillInventors(EbdDPatent ebdDPatent, CPatent result) {
        if (ebdDPatent.getInventors() != null) {
            result.setAuthorshipData(new CAuthorshipData());
            result.getAuthorshipData().setAuthorList(new ArrayList<>());

            List<CPerson> inventors = new ArrayList<>();
            for (EbdDInventor inv : ebdDPatent.getInventors()) {
                CAuthor author = new CAuthor();
                CPerson person = ebdPersonBaseMapper.toCore(inv);

                if (checkPersonExists(inventors, person)) {
                    continue;
                }

                inventors.add(person);

                author.setPerson(person);
                author.setAuthorSeq(Long.valueOf(inv.getOrd()));
                result.getAuthorshipData().getAuthorList().add(author);
            }

        }
    }

    private void fillOwners(CReception receptionForm, CFile file, Set<EbdDOwner> ebdDOwners, boolean serviceConfigured) {
        List<CPerson> owners = receptionForm.getOwnershipData().getOwnerList().stream()
                .map(COwner::getPerson)
                .collect(Collectors.toList());

        List<COwner> collect = owners.stream()
                .map(cPerson -> {
                    COwner cOwner = new COwner();
                    cOwner.setPerson(cPerson);
                    return cOwner;
                })
                .collect(Collectors.toList());

        file.getOwnershipData().setOwnerList(collect);

        if (!serviceConfigured) {
            EbdDOwner mainOwner = ebdDOwners.stream()
                    .filter(ebdDOwner -> ebdDOwner.getMainAddress() == 1)
                    .findFirst().orElse(CollectionUtils.isEmpty(ebdDOwners) ? null : ebdDOwners.stream().limit(1).findFirst().get());

            if (Objects.nonNull(mainOwner)) {
                CPerson servicePerson = owners.stream()
                        .filter(cPerson -> cPerson.getPersonName().equals(mainOwner.getFullName()) && cPerson.getAddressStreet().equals(mainOwner.getAddress()))
                        .findFirst().orElse(null);

                if (Objects.nonNull(servicePerson))
                    file.setServicePerson(servicePerson);

            }
        }

    }

    private void fillClassins(List<CPatentIpcClass> cPatentIpcClasses, Set<EbdDClassin> classins) {
        for (EbdDClassin classin : classins) {
            CPatentIpcClass ipcClass = new CPatentIpcClass();
            ipcClass.setIpcQualification(classin.getId().getOdclass().toString());
            ipcClass.setIpcEdition(classin.getIpcversion().toString());
            ipcClass.setIpcSection(classin.getIpcSectionCode());
            ipcClass.setIpcClass(classin.getIpcClassCode());
            ipcClass.setIpcSubclass(classin.getIpcSubclassCode());

            if (StringUtils.isEmpty(classin.getIpcGroupCode()) && StringUtils.isEmpty(classin.getIpcSubgroupCode())) {
                ipcClass.setIpcGroup("0");
                ipcClass.setIpcSubgroup("0");
            } else {
                ipcClass.setIpcGroup(classin.getIpcGroupCode());
                ipcClass.setIpcSubgroup(classin.getIpcSubgroupCode());
            }

            cPatentIpcClasses.add(ipcClass);
        }
    }

    private void fillCpcClasses(List<CPatentCpcClass> cPatentCpcClasses, Set<EbdDCpcClass> cpcClasses) {
        for (EbdDCpcClass ebdCpcClass : cpcClasses) {
            CPatentCpcClass cpcClass = new CPatentCpcClass();
            cpcClass.setCpcQualification(ebdCpcClass.getId().getOdclass().toString());
            cpcClass.setCpcEdition(ebdCpcClass.getCpcversion().toString());
            cpcClass.setCpcSection(ebdCpcClass.getCpcSectionCode());
            cpcClass.setCpcClass(ebdCpcClass.getCpcClassCode());
            cpcClass.setCpcSubclass(ebdCpcClass.getCpcSubclassCode());
            cpcClass.setCpcGroup(ebdCpcClass.getCpcGroupCode());
            cpcClass.setCpcSubgroup(ebdCpcClass.getCpcSubgroupCode());
            cPatentCpcClasses.add(cpcClass);
        }
    }
    public CReception generateUserdocRequest(CFileId fileId, CReception receptionForm) {
        CReception udr = new CReception();
        udr.setUserdoc(new CReceptionUserdoc());
        udr.getUserdoc().setUserdocType(receptionForm.getEuPatent().getUserdocType());
        udr.getUserdoc().setFileId(fileId);
        udr.setEntryDate(receptionForm.getEntryDate());
        udr.setOriginalExpected(receptionForm.getOriginalExpected());
        udr.setSubmissionType(receptionForm.getSubmissionType());
        udr.setOwnershipData(receptionForm.getOwnershipData());
        udr.setRepresentationData(receptionForm.getRepresentationData());
        udr.setRegisterInDocflowSystem(true);
        udr.setRegisterReceptionRequest(true);
        //receptionForm.notes otivat vyv finalUserdoc -> notes. Tam otiva i title-a na EP-to!
        String notes = Arrays.asList(receptionForm.getFile().getTitle(), receptionForm.getNotes()).stream().filter(org.apache.commons.lang3.StringUtils::isNotEmpty).collect(Collectors.joining("\n"));
        udr.setNotes(notes);
        return udr;
    }

    public CReception generateInsertUserdocumentRequest(EbdDLicense l, CFileId fileId, CReception receptionForm) {
        CReception userdocumentRequest = new CReception();
        if (l.getDtliexpi() != null) {
            userdocumentRequest.setNotes("Срок до: " + IpasServiceUtils.formatDate(l.getDtliexpi()));
        } else {
            userdocumentRequest.setNotes(receptionForm.getFile().getTitle());
        }

        String userdocType;
        if (l.getTylicense() != null && l.getTylicense() == 1) {
            userdocType = UserdocType.EP_NONEXCLUSIVE_LICENSE_TYPE;
        } else if (l.getTylicense() != null && l.getTylicense() == 2) {
            userdocType = UserdocType.EP_EXCLUSIVE_LICENSE_TYPE;
        } else {
            throw new RuntimeException("Unknown tylicense..." + l.getTylicense());
        }
        userdocumentRequest.setUserdoc(new CReceptionUserdoc());
        userdocumentRequest.getUserdoc().setUserdocType(userdocType);
        userdocumentRequest.setEntryDate(l.getDteffect());
        userdocumentRequest.getUserdoc().setFileId(fileId);
        userdocumentRequest.setRegisterInDocflowSystem(true);
        userdocumentRequest.setRegisterReceptionRequest(false);
        userdocumentRequest.setOriginalExpected(false);//Tova beshe hardcode-nato v DocCreationCoverter.convertEuPatentProcessUserdoc
        userdocumentRequest.setSubmissionType(receptionForm.getSubmissionType());

        CPerson grantee = ebdPersonBaseMapper.toCore(l.getPerson());
        userdocumentRequest.getUserdoc().addPerson(UserdocPersonUtils.convertToUserdocPerson(grantee, UserdocPersonRole.GRANTEE, null, null, null));
        return userdocumentRequest;
    }
    public CReception createPatentFileReceptionRequest(EbdDPatent patent, CReception euPatentReception) {
        CReception res = new CReception();
        res.setDocflowSystemId(euPatentReception.getDocflowSystemId());
        res.setExternalSystemId(euPatentReception.getExternalSystemId());
        res.setRegisterInDocflowSystem(true);
        res.setOriginalExpected(false);//Tova beshe hardcode-nato v DocCreationCoverter.setReceivedOriginal
        res.setFile(new CReceptionFile());
        res.getFile().setFileId(EuPatentUtils.generateEUPatentCFileId(patent));
        res.getFile().setApplicationType(applicationTypeService.selectApplicationTypeByFileType(FileType.EU_PATENT.code()));
        res.getFile().setApplicationSubType(applicationTypeService.getDefaultApplicationSubtype(res.getFile().getApplicationType()));
        res.getFile().setTitle(euPatentReception.getFile().getTitle());
        res.setSubmissionType(euPatentReception.getSubmissionType());
        res.setEntryDate(euPatentReception.getEntryDate());
        res.setOwnershipData(euPatentReception.getOwnershipData());
        res.setRepresentationData(euPatentReception.getRepresentationData());
        return res;
    }


    public CReception generateInsertUserdocumentRequest(EbdDOpposition o, CFileId fileId, CReception epoPatentReceptionRequest) {
        CReception res = new CReception();
        res.setNotes(epoPatentReceptionRequest.getFile().getTitle());
        res.setUserdoc(new CReceptionUserdoc());
        res.getUserdoc().setFileId(fileId);
        res.setEntryDate(o.getDtopposi());
        res.getUserdoc().setUserdocType(UserdocType.EP_OPPOSITION);
        res.setRegisterReceptionRequest(false);
        res.setRegisterInDocflowSystem(true);
        res.setOriginalExpected(false);//Tova beshe hardcode-nato v DocCreationCoverter.convertEuPatentProcessUserdoc
        res.setSubmissionType(epoPatentReceptionRequest.getSubmissionType());
        CPerson applicant = ebdPersonBaseMapper.toCore(o.getPerson());
        res.setOwnershipData(new COwnershipData());
        res.getOwnershipData().setOwnerList(new ArrayList<>());
        COwner owr = new COwner();
        owr.setPerson(applicant);
        res.getOwnershipData().getOwnerList().add(owr);
        return res;
    }



    private static CParisPriority toParisPriority(EbdDPriority priority) {

        CParisPriority pp = new CParisPriority();
        pp.setApplicationId(priority.getNoprio());
        pp.setCountryCode(priority.getIdcountry());
        pp.setPriorityDate(priority.getDtprio());
        pp.setNotes(priority.getRmprio());

        boolean isAccepted = priority.getStprio() != null && priority.getStprio() == 1;
        pp.setPriorityStatus(isAccepted ? 1 : 2);
        return pp;
    }

    private static boolean checkPriorityExists(List<CParisPriority> cParisPriorities, CParisPriority prio) {
        if (CollectionUtils.isEmpty(cParisPriorities))
            return false;

        for (CParisPriority cPrio : cParisPriorities) {
            try {
                if (cPrio.equals(prio)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private static boolean checkPersonExists(List<CPerson> persons, CPerson person) {

        for (CPerson p : persons) {
            try {
                if (p.equals(person)) {
                    return true;
                }
            } catch (Exception var5) {
                return false;
            }
        }

        return false;
    }
}
