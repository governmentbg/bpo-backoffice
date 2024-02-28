package bg.duosoft.ipas.core.service.impl.reception.ebd;

import bg.bpo.ebd.ebddpersistence.entity.EbdDLicense;
import bg.bpo.ebd.ebddpersistence.entity.EbdDOpposition;
import bg.bpo.ebd.ebddpersistence.entity.EbdDPatent;
import bg.bpo.ebd.ebddpersistence.service.EbdDPatentService;
import bg.duosoft.abdocs.model.DocFileVisibility;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.model.patent.CTechnicalData;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.util.CAttachment;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.nomenclature.ClassCpcService;
import bg.duosoft.ipas.core.service.nomenclature.ClassIpcService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.reception.InternalReceptionService;
import bg.duosoft.ipas.persistence.repository.entity.reception.ReceptionRequestRepository;
import bg.duosoft.ipas.util.DataConverter;
import bg.duosoft.ipas.util.security.SecurityUtils;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pravi local ipas reception.
 * 1. Ako se pravi reception na userdoc, to go insertva direktno
 * 2. Ako se pravi reception na cql patent, to se pravi insert samo na neshtata, koito mogat da se insertnat prez nashite services
 * - reception na evropejskiq patent
 * - insert na evropejskiq patent
 * - reception na userdocs - licenzii + opozicii
 * - reception na final userdoc
 * Actions,  se insertvat prez IpasAPI-to. Tova stava prez  {@link 0ExternalEbdPatentIpasServiceImpl}
 * Edin slynchev den, kogato pochnem da vyvejdame actions prez nashite services, trqva da se razkara ExternalEbdPatentIpasServiceImpl i negoviq kod da vleze tuk. Togava moje bi shte trqbva da se napravi i obedinenie s {@link EuPatentReceptionServiceImpl}, koito v momenta pravi call-ovete kym toq service i external service-a!!!
 */
@Component
@Slf4j
@Transactional
@LogExecutionTime
class LocalEbdPatentIpasServiceImpl {

    private static final String MISSING_IPC_CLASS_NAME = "??????????";
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PatentService patentService;

    @Autowired
    private ClassIpcService classIpcService;
    @Autowired
    private ClassCpcService classCpcService;
    @Autowired
    private InternalReceptionService internalReceptionService;

    @Autowired
    private EbddownloadToIpasPatentConvertor ebddownloadToIpasPatentConvertor;

    @Autowired
    private DailyLogService dailyLogService;
    @Autowired
    private EbdDPatentService ebdDPatentService;
    @Autowired
    private ReceptionRequestRepository receptionRequestRepository;
    @Autowired
    private AbdocsService abdocsService;

    public CReceptionResponse insertUserdoc(CFileId fileId, CReception receptionForm) {
        return _insertUserdoc(fileId, receptionForm);
    }


    /**
     * Otvarq se NOVA tranzakciq, tyj kato v EuPatentReceptionServiceImpl se vika ExternalEbdPatentIpasServiceImpl, koeto pravi otdelni call-ove kym IpasAPI-to, koito stavat v otdelna tranzakciq
     * TODO:Edin den v svetloto bydeshte kogato insert na actions stava prez nashiq IPAS, tova requires_new trqbva da izchezne!!!
     *
     * @param receptionForm
     * @return
     * @throws EbdReceptionException
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = EbdReceptionException.class)//Otvarq se nova tranzakciq, koqto se rollback-va pri EbdReceptionException!!!!
    public ReceiveEuropeanPatentResult transferEuropeanPatentFromEbdToIpas(EbdDPatent ebdDPatent, CReception receptionForm) throws EbdReceptionException {
        Date entitlementDate = ebdDPatentService.selectEntitlementDate(receptionForm.getEuPatent().getObjectNumber().toString());

        String applicationNumber = ebdDPatent.getIdappli();
        log.debug("Start inserting application..." + applicationNumber);

        InsertPatentResponse insertPatentResponse = insertPatent(ebdDPatent, entitlementDate, receptionForm);//tuk moje da se hvyrli exception ako e vyveden reception-a, no ima problem pri vyvejdane na patenta!!!!
        try {
            CFileId fileId = insertPatentResponse.getPatentReceptionResponse().getFileId();
            insertLicenseUserDocuments(ebdDPatent.getLicenses(), fileId, receptionForm);
            insertOppositionUserDocuments(ebdDPatent.getOppositions(), fileId, receptionForm);


            CReceptionResponse finalUserdocCreationResponse = null;
            if (Objects.nonNull(receptionForm.getEuPatent().getUserdocType())) {
                finalUserdocCreationResponse = _insertUserdoc(fileId, receptionForm);
            }


            //po princip dokumentite trqbva da se registrirat v delovodnata v ReceptionService-a, no pri EP e po-razlichno - ako neshto grymne pri registrirane na dokumentite v delovodnata, v ReceptionService-a nqma da moje das erevertne INSERT-a na patenta v IPAS,
            //zatova attachmentite se vyvejdat zaedno s osnovniq patent!!! Edin slynchev den kogato sprem da polzvame IpasAPI-to, tozi kod trqbva da se razkara ot tuk i da otide v ReceptionService-a!!!!
            registerAttachmentsInDocflowSystem(receptionForm, insertPatentResponse.getPatentReceptionResponse());

            //inserting userdocs, related to the patent, if any!
            //po princip tova trqbva da e v ReceptionService-a, no pri EP e malko po-razlichno i vsichko stava v otdelni tranzakcii, ako neshto izgyrmi vposledstvie pri insert na userdoc-ovete, ne moje da se revertne inserta na EP, zatova
            //userdoc-ovete se vyvejdat v sy6tata tranzakciq v koqto i osnovnia patent!!!!
            if (!CollectionUtils.isEmpty(receptionForm.getUserdocReceptions())) {
                finalUserdocCreationResponse.setUserdocReceptionResponses(new ArrayList<>());
                for (CReception r : receptionForm.getUserdocReceptions()) {
                    r.getUserdoc().setFileId(fileId);
                    internalReceptionService.createReception(r);
                }
            }
            em.flush();
            return new ReceiveEuropeanPatentResult(insertPatentResponse.patentReceptionResponse, finalUserdocCreationResponse);
        } catch (Exception e) {//ako se hvyrli exception pri reception na  neshto sled kato se napravi insert na patenta, se rethrow-va EbdReceptionException v koito ima docflowSystemId-to, za da moje call-era da iztrie docflow object-a
            throw new EbdReceptionException(insertPatentResponse.getPatentReceptionResponse().getDocflowDocumentId(), e);
        }


    }

    //edin slynchev den kogato sprem da polzvame IpasAPI-to za insert na chast ot dannite za EP, tova trqbva da otide v ReceptionService, kakto e za vsichki drugi obekti/userdocs, s izkliuchenie na insert na EP
    private void registerAttachmentsInDocflowSystem(CReception reception, CReceptionResponse res) {
        if (reception.isRegisterInDocflowSystem() && !org.apache.commons.collections.CollectionUtils.isEmpty(reception.getAttachments())) {
            Integer docflowDocumentId = res.getDocflowDocumentId();
            if (docflowDocumentId == null) {
                throw new RuntimeException("Dcoflow document id Should not be null!");
            }
            for (CAttachment att : reception.getAttachments()) {
                abdocsService.uploadFileToExistingDocument(docflowDocumentId, att.getData(), att.getFileName(), att.getDescription(), false, DocFileVisibility.PublicAttachedFile);
            }
        }
    }

    private InsertPatentResponse insertPatent(EbdDPatent ebdDPatent, Date entitlementDate, CReception receptionForm) throws EbdReceptionException {
        log.trace("Creating CPatent ...");
        CPatent cPatent = ebddownloadToIpasPatentConvertor.convertPatent(ebdDPatent, entitlementDate, receptionForm);
        log.trace("End of creating CPatent ...");


        updateIpcClassesEditions(ebdDPatent, cPatent);
        updateCpcClassesEditions(ebdDPatent, cPatent);
        log.trace("Creating patent reception");
        CReceptionResponse euPatentReceptionResponse = internalReceptionService.createReception(ebddownloadToIpasPatentConvertor.createPatentFileReceptionRequest(ebdDPatent, receptionForm));
        log.trace("End  of creating patent reception..." + euPatentReceptionResponse);
        try {
            log.trace("Inserting patent");
            cPatent.getFile().getFilingData().setReceptionDate(dailyLogService.getWorkingDate());//tozi kod e tuk, imache ne minavat validaciite na insertEuPatent
            cPatent.getFile().getFilingData().setReceptionUserId(SecurityUtils.getLoggedUserId().longValue());//
            cPatent.getFile().getFilingData().setExternalSystemId(euPatentReceptionResponse.getExternalSystemId());

            CPatent result = patentService.insertEuPatent(cPatent);

            if (receptionForm.isRegisterReceptionRequest()) {//updating receptionRequestStatus
                CFileId pk = result.getFile().getFileId();
                receptionRequestRepository.updateReceptionRequestStatus(pk.getFileSeq(), pk.getFileType(), pk.getFileSeries(), pk.getFileNbr());
            }
            log.trace("End of inserting patent");
            return new InsertPatentResponse(euPatentReceptionResponse, result);
        } catch (Exception e) {
            throw new EbdReceptionException(euPatentReceptionResponse.getDocflowDocumentId(), e);
        }

    }

    /**
     *
     * tyrsi ipc classes po section, classCode, subClassCode, groupCode, subGroup
     *   ako nqma nito edin zapis, to slaga edition = 9 i vyvejda takyv zapis
     *   ako ima tochno edin zapis, to polzva negoviq edition
     *   ako ima poveche ot edin zapis ili ne promenq edition, ako sy6testvuva zapis s tozi edition i slaga posledniq edition
     *
     * @param ebdDPatent
     * @param cPatent
     */
    private void updateIpcClassesEditions(EbdDPatent ebdDPatent, CPatent cPatent) {
        CTechnicalData technicalData = cPatent.getTechnicalData();
        if (Objects.nonNull(technicalData)) {
            List<CPatentIpcClass> ipcClassList = technicalData.getIpcClassList();
            if (!CollectionUtils.isEmpty(ipcClassList)) {
                for (CPatentIpcClass ipcClass : ipcClassList) {
                    String edition = ipcClass.getIpcEdition();
                    String section = ipcClass.getIpcSection();
                    String classCode = ipcClass.getIpcClass();
                    String subClassCode = ipcClass.getIpcSubclass();
                    String groupCode = ipcClass.getIpcGroup();
                    String subGroup = ipcClass.getIpcSubgroup();
                    if (StringUtils.isEmpty(edition)
                            || StringUtils.isEmpty(section)
                            || StringUtils.isEmpty(classCode)
                            || StringUtils.isEmpty(subClassCode)
                            || StringUtils.isEmpty(groupCode)
                            || StringUtils.isEmpty(subGroup)) {
                        throw new RuntimeException("IpClass PK contains empty values ! Patent: " + ebdDPatent.getIdappli());
                    }


                    List<CIpcClass> existingIpcClass = classIpcService.findBySectionClassSubclassGroupAndSubgroup(section, classCode, subClassCode, groupCode, subGroup);
                    if (CollectionUtils.isEmpty(existingIpcClass)) {
                        ipcClass.setIpcVersionCalculated("9");
                        ipcClass.setIpcEdition("9");
                        ipcClass.setIpcSymbolDescription(MISSING_IPC_CLASS_NAME);
                        classIpcService.save(ipcClass);
                    } else if (existingIpcClass.size() == 1) {
                        ipcClass.setIpcEdition(existingIpcClass.get(0).getIpcEdition());
                    } else {
                        Set<Integer> editions = existingIpcClass
                                .stream()
                                .map(e -> e.getIpcEdition())
                                .map(e -> DataConverter.parseInteger(e, null))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());
                        Integer ed = DataConverter.parseInteger(edition, null);
                        if (!editions.contains(ed)) {
                            ipcClass.setIpcEdition(editions.stream().sorted((f1, f2) -> Integer.compare(f2, f1)).findFirst().orElseThrow().toString());
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * tyrsi cpc classes po section, classCode, subClassCode, groupCode, subGroup
     *   ako nqma nito edin zapis, to slaga edition = 9 i vyvejda takyv zapis
     *   ako ima tochno edin zapis, to polzva negoviq edition
     *   ako ima poveche ot edin zapis ili ne promenq edition, ako sy6testvuva zapis s tozi edition i slaga posledniq edition
     *
     * @param ebdDPatent
     * @param cPatent
     */
    private void updateCpcClassesEditions(EbdDPatent ebdDPatent, CPatent cPatent) {
        CTechnicalData technicalData = cPatent.getTechnicalData();
        if (Objects.nonNull(technicalData)) {
            List<CPatentCpcClass> cpcClassList = technicalData.getCpcClassList();
            if (!CollectionUtils.isEmpty(cpcClassList)) {
                for (CPatentCpcClass cpcClass : cpcClassList) {
                    String edition = cpcClass.getCpcEdition();
                    String section = cpcClass.getCpcSection();
                    String classCode = cpcClass.getCpcClass();
                    String subClassCode = cpcClass.getCpcSubclass();
                    String groupCode = cpcClass.getCpcGroup();
                    String subGroup = cpcClass.getCpcSubgroup();

                    if (StringUtils.isEmpty(edition)
                            || StringUtils.isEmpty(section)
                            || StringUtils.isEmpty(classCode)
                            || StringUtils.isEmpty(subClassCode)
                            || StringUtils.isEmpty(groupCode)
                            || StringUtils.isEmpty(subGroup)) {
                        throw new RuntimeException("CpClass PK contains empty values ! Patent: " + ebdDPatent.getIdappli());
                    }


                    List<CCpcClass> existingCpcClass = classCpcService.findBySectionClassSubclassGroupAndSubgroup(section, classCode, subClassCode, groupCode, subGroup);
                    if (CollectionUtils.isEmpty(existingCpcClass)) {
                        cpcClass.setCpcVersionCalculated("9");
                        cpcClass.setCpcEdition("9");
                        cpcClass.setCpcName(MISSING_IPC_CLASS_NAME);
                        classCpcService.save(cpcClass);
                    } else if (existingCpcClass.size() == 1) {
                        cpcClass.setCpcEdition(existingCpcClass.get(0).getCpcEdition());
                    } else {
                        Set<Integer> editions = existingCpcClass
                                .stream()
                                .map(e -> e.getCpcEdition())
                                .map(e -> DataConverter.parseInteger(e, null))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());
                        Integer ed = DataConverter.parseInteger(edition, null);
                        if (!editions.contains(ed)) {
                            cpcClass.setCpcEdition(editions.stream().sorted((f1, f2) -> Integer.compare(f2, f1)).findFirst().orElseThrow().toString());
                        }
                    }
                }
            }
        }
    }

    private void insertLicenseUserDocuments(Set<EbdDLicense> licenses, CFileId fileId, CReception receptionForm) {
        log.trace("Inserting licenses...");
        if (licenses != null) {
            for (EbdDLicense l : licenses) {
                CReception request = ebddownloadToIpasPatentConvertor.generateInsertUserdocumentRequest(l, fileId, receptionForm);
                CReceptionResponse createReceptionResponse = internalReceptionService.createReception(request);
            }
        }
        log.trace("End of inserting licenses...");
    }

    private void insertOppositionUserDocuments(Set<EbdDOpposition> oppositions, CFileId fileId, CReception receptionForm) {
        log.trace("Inserting oppositions...");
        if (oppositions != null) {
            for (EbdDOpposition opposition : oppositions) {
                CReception request = ebddownloadToIpasPatentConvertor.generateInsertUserdocumentRequest(opposition, fileId, receptionForm);
                CReceptionResponse createReceptionResponse = internalReceptionService.createReception(request);
            }
        }
        log.trace("End of inserting oppositions...");
    }

    private CReceptionResponse _insertUserdoc(CFileId fileId, CReception receptionForm) {
        log.trace("Inserting Request for Validation");
        CReception udRq = ebddownloadToIpasPatentConvertor.generateUserdocRequest(fileId, receptionForm);
        CReceptionResponse res = internalReceptionService.createReception(udRq);
        log.trace("End of inserting Request for Validation");
        return res;
    }

    @Data
    @AllArgsConstructor
    private static class InsertPatentResponse {
        private CReceptionResponse patentReceptionResponse;
        private CPatent patent;
    }
}
