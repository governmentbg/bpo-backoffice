package bg.duosoft.ipas.integration.decisiondesktop.vars;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.userdoc.*;
import bg.duosoft.ipas.core.model.userdoc.grounds.CMarkGroundData;
import bg.duosoft.ipas.core.model.userdoc.grounds.CSingleDesignGroundData;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.service.file.FileRecordalService;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.DesignImageWrapperVariable;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.EarlierMarkVariable;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

import static bg.duosoft.ipas.integration.decisiondesktop.vars.CommonVariablesUtils.*;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.05.2021
 * Time: 12:22
 */
@Component
public class UserdocVariablesCreator {

    @Autowired
    private UserdocService userdocService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private MarkService markService;

    @Autowired
    private DesignService designService;

    @Autowired
    private FileRecordalService fileRecordalService;

    private static final String LICENSE_EXPIRATION_DATE = "LICENSE_EXPIRATION_DATE";
    private static final String LICENSE_EXPIRATION_DATE_TYPE = "LICENSE_EXPIRATION_DATE_TYPE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";

    private static final String APPEAL_DECISION_DATE = "CLAIM_CONTESTED_DECISION_DATE";
    private static final String APPEAL_DECISION_NUMBER = "CLAIM_CONTESTED_DECISION_NUMBER";
    private static final String BANKRUPTCY_COURT_NAME = "BANKRUPTCY_COURT_NAME";
    private static final String BANKRUPTCY_CASE_NUMBER = "BANKRUPTCY_CASE_NUMBER";

    private static final String SERVICE_SCOPE = "SERVICE_SCOPE";

    public Map<String, Object> createUserdocDataVariables(CDocumentId documentId, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();
        CUserdoc userdoc = userdocService.findUserdoc(documentId, true);

        if(userdoc != null) {
            variables.putAll(createUserdocOrderVariables(userdoc, isUserdocParent));
            variables.putAll(createFileRecordalVariables(userdoc, isUserdocParent));

            variables.put(getUserdocFinalVariable(Variables.USERDOC_APPLICATION_NUMBER, isUserdocParent), getUserdocApplicationNumber(userdoc));
            variables.put(getUserdocFinalVariable(Variables.USERDOC_APPLICATION_DATE, isUserdocParent), userdoc.getDocument().getFilingDate());

            if(userdoc.getUserdocPersonData() != null && CollectionUtils.isNotEmpty(userdoc.getUserdocPersonData().getPersonList())) {
                variables.putAll(createUserdocPersonsDataVariables(userdoc.getUserdocPersonData().getPersonList(), isUserdocParent));
            }

            if(userdoc.getServicePerson() != null){
                variables.putAll(CommonVariablesUtils.createServicePersonDataVariables(userdoc.getServicePerson(), getUserdocFinalVariable(Variables.USERDOC_VAR_PREFIX, isUserdocParent)));
            }

            if(CollectionUtils.isNotEmpty(userdoc.getUserdocRootGrounds())) {
                variables.putAll(createUserdocGroundsDataVariables(userdoc.getUserdocRootGrounds(), isUserdocParent));
            }

            if(CollectionUtils.isNotEmpty(userdoc.getUserdocExtraData())){
                variables.putAll(createUserdocExtraDataVariables(userdoc.getUserdocExtraData(), isUserdocParent));
                Optional<CUserdocExtraData> scopeExtraOpt = userdoc.getUserdocExtraData().stream().filter(e -> e.getType().getCode().equals(SERVICE_SCOPE)).findFirst();
                if(scopeExtraOpt.isPresent() && isTopProcessForDesign(userdoc)){
                    boolean scopeExtra = scopeExtraOpt.get().getBooleanValue();
                    variables.putAll(createDesignServiceScopeVariables(scopeExtra, userdoc.getSingleDesigns(), isUserdocParent));
                } else if (scopeExtraOpt.isPresent() && isTopProcessForMark(userdoc)){
                    boolean scopeExtra = scopeExtraOpt.get().getBooleanValue();
                    variables.putAll(createMarkServiceScopeVariables(scopeExtra, userdoc.getProtectionData(), isUserdocParent));
                }
            }
            if(userdoc.getApprovedData() != null){
                variables.putAll(createApprovedDataVariables(userdoc.getApprovedData(), userdoc.getApprovedNiceClassList(), isUserdocParent));
            }
            if(userdoc.getProcessSimpleData() != null && userdoc.getProcessSimpleData().getResponsibleUser() != null && userdoc.getProcessSimpleData().getResponsibleUser().getUserName() != null){
                variables.put(getUserdocFinalVariable(Variables.USERDOC_EXPERT_NAME, isUserdocParent), userdoc.getProcessSimpleData().getResponsibleUser().getUserName());
            }
            if (!isUserdocParent && userdoc.getUserdocParentData() != null && userdoc.getUserdocParentData().getUserdocId() != null) {
               variables.putAll(createUserdocDataVariables(userdoc.getUserdocParentData().getUserdocId(), true));
            }
        }
        return variables;
    }

    public Map<String, Object> createFileRecordalVariables(CUserdoc userdoc, boolean isUserdocParent) {
        Map<String, Object> variables = new HashMap<>();
        CFileRecordal recordal = fileRecordalService.selectRecordalByUserdocId(userdoc.getDocumentId());
        if(recordal != null){
            variables.put(getUserdocFinalVariable(Variables.USERDOC_RECORDAL_DATE, isUserdocParent), recordal.getDate());
        }
        return variables;
    }

    private String getUserdocApplicationNumber(CUserdoc userdoc){
        if(StringUtils.isNotEmpty(userdoc.getDocument().getExternalSystemId())){
            return userdoc.getDocument().getExternalSystemId();
        } else {
            if(userdoc.getDocument().getDocumentSeqId() != null && userdoc.getDocument().getDocumentSeqId().getDocSeqNbr() != null
                && userdoc.getDocument().getDocumentSeqId().getDocSeqSeries() != null) {
                CDocumentSeqId id = userdoc.getDocument().getDocumentSeqId();
                return String.format("%s/%s/%s/%s", userdoc.getDocumentId().getDocOrigin(),
                        userdoc.getDocumentId().getDocLog(),
                        id.getDocSeqSeries()+"", id.getDocSeqNbr()+"");
            } else {
                throw new RuntimeException("No document seq number for "+userdoc.getDocumentId().getDocNbr());
            }
        }
    }

    public Map<String, Object> createUserdocPersonsDataVariables(List<CUserdocPerson> userdocPersonList, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();

        Map<UserdocPersonRole, List<CUserdocPerson>> userdocPersonsMap = new HashMap<>();
        userdocPersonList.stream().forEach(uPerson -> {
            if(userdocPersonsMap.get(uPerson.getRole()) == null) {
                userdocPersonsMap.put(uPerson.getRole(), new ArrayList<>());
            }
            userdocPersonsMap.get(uPerson.getRole()).add(uPerson);
        });

        userdocPersonsMap.keySet().stream().forEach(key ->{
                variables.put(getUserdocFinalVariable(mapRoleToVariable(key), isUserdocParent),
                        userdocPersonsMap.get(key).stream().map(uPerson ->
                                new StringBuilder(uPerson.getPerson().getPersonName()).append(WITH_ADDRESS_LOWERCASE_APPENDABLE).append(CommonVariablesUtils.buildPersonAddress(uPerson.getPerson())).toString()
                        ).reduce((p1, p2) -> new StringBuilder(p1).append(AND_APPENDABLE).append(p2).toString()).get()
                );

                variables.put(getUserdocFinalVariable(mapRoleToNameListVariable(key), isUserdocParent),
                        userdocPersonsMap.get(key).stream().map(uPerson ->
                                uPerson.getPerson().getPersonName()
                        ).reduce((p1, p2) -> new StringBuilder(p1).append(AND_APPENDABLE).append(p2).toString()).get()
                );

                variables.put(getUserdocFinalVariable(mapRoleToFormattedVariable(key), isUserdocParent),
                        userdocPersonsMap.get(key).stream().map(uPerson -> {
                                    StringBuilder formattedBuilder = new StringBuilder(new StringBuilder(uPerson.getPerson().getPersonName()));
                                    if (StringUtils.isNotEmpty(uPerson.getPerson().getIndividualIdTxt())) {
                                        formattedBuilder.append(CommonVariablesUtils.EGN_EIK_APPENDABLE).append(uPerson.getPerson().getIndividualIdTxt());
                                    }
                                    formattedBuilder.append(CommonVariablesUtils.ADDRESS_APPENDABLE).append(CommonVariablesUtils.buildPersonAddress(uPerson.getPerson())).toString();
                                    return formattedBuilder.toString();
                                }
                        ).reduce((p1, p2) -> new StringBuilder(p1).append(AND_APPENDABLE).append(p2).toString()).get()
                );
        });

        return variables;
    }

    public Map<String, Object> createUserdocGroundsDataVariables(List<CUserdocRootGrounds> userdocRootGrounds, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();

        List<EarlierMarkVariable> earlierMarksVars = new ArrayList<>();
        for(int i=0; i< userdocRootGrounds.size() && i < 10; i++){
            CUserdocRootGrounds ground = userdocRootGrounds.get(i);
            if(ground.getMarkGroundData() != null) {
                earlierMarksVars.add(buildEarlierMarkVariable(ground.getMarkGroundData()));
            }
            if(CollectionUtils.isNotEmpty(ground.getSingleDesignGroundData())) {
                variables.putAll(createUserdocDesignsGroundDataVariables(ground.getSingleDesignGroundData(), i, isUserdocParent));
            }
        }

        variables.put(getUserdocFinalVariable(Variables.USERDOC_EARLIER_MARKS, isUserdocParent), earlierMarksVars.toArray(new EarlierMarkVariable[0]));

        return variables;
    }

    public EarlierMarkVariable buildEarlierMarkVariable(CMarkGroundData markGroundData){
        EarlierMarkVariable earlierMarkVariable = new EarlierMarkVariable();
        CMark earlierMark = null;

        if(markGroundData.getMarkImportedInd() != null && markGroundData.getMarkImportedInd() && markGroundData.getFilingNumber() != null) {
            CFileId fileId = BasicUtils.createCFileId(markGroundData.getFilingNumber());
            earlierMark = markService.findMark(fileId, true);

            if(earlierMark.getSignData() != null) {
                byte[] imageData = CommonVariablesUtils.getMarkImageData(earlierMark.getSignData().getAttachments());
                if(imageData != null){
                    earlierMarkVariable.setSignImageBytes(imageData);
                }
            }
        }
        StringBuilder niceListBuilder = new StringBuilder();
        StringBuilder niceClassesBuilder = new StringBuilder();
        if(markGroundData.getNiceClassesInd() != null) {
            if (markGroundData.getNiceClassesInd() == false) {
                if (CollectionUtils.isNotEmpty(markGroundData.getUserdocGroundsNiceClasses())) {
                    markGroundData.getUserdocGroundsNiceClasses().stream().forEach(nice -> {
                        niceListBuilder.append(CommonVariablesUtils.buildFormattedNiceClassTerms(nice.getNiceClassCode().intValue(), nice.getNiceClassDescription(), INNER_SEPARATOR));
                        niceClassesBuilder.append(nice.getNiceClassCode()).append(INNER_SEPARATOR);
                    });

                }
            } else if (earlierMark != null) {
                earlierMark.getProtectionData().getNiceClassList().stream().forEach(nice -> {
                    niceListBuilder.append(CommonVariablesUtils.buildFormattedNiceClassTerms(nice.getNiceClassNbr(), nice.getNiceClassDescription(), INNER_SEPARATOR));
                    niceClassesBuilder.append(nice.getNiceClassNbr()).append(INNER_SEPARATOR);
                });
            }
        }
        if(niceListBuilder.length()>0){
            earlierMarkVariable.setNiceList(CommonVariablesUtils.getStringWithoutTrailing(niceListBuilder, INNER_SEPARATOR.length()));
        }
        if(niceClassesBuilder.length()>0){
            earlierMarkVariable.setNiceClasses(CommonVariablesUtils.getStringWithoutTrailing(niceClassesBuilder, INNER_SEPARATOR.length()));
        }

        if(markGroundData.getFilingNumber() != null){
            earlierMarkVariable.setApplicationNumber(markGroundData.getFilingNumber());
        }
        if(markGroundData.getRegistrationNbr()  != null){
            earlierMarkVariable.setRegistrationNumber(markGroundData.getRegistrationNbr());
        }
        if(markGroundData.getFilingDate() != null){
            earlierMarkVariable.setApplicationDate(CommonVariablesUtils.SIMPLE_DATE_FORMAT.format(markGroundData.getFilingDate()));
        }
        if(markGroundData.getNameText() != null){
            earlierMarkVariable.setSignName(markGroundData.getNameText());
        }
        if(markGroundData.getNameData() != null){
            earlierMarkVariable.setSignImageBytes(markGroundData.getNameData());
        }
        if(markGroundData.getRegistrationDate() != null){
            earlierMarkVariable.setRegistrationDate(CommonVariablesUtils.SIMPLE_DATE_FORMAT.format(markGroundData.getRegistrationDate()));
        }
        if(markGroundData.getMarkGroundType() != null && markGroundData.getMarkGroundType().getId() != null){
            switch (markGroundData.getMarkGroundType().getId()){
                case 1: earlierMarkVariable.setType(EarlierMarkVariable.TYPE_NATIONAL); break;
                case 2: earlierMarkVariable.setType(EarlierMarkVariable.TYPE_INTERNATIONAL); break;
                case 3: earlierMarkVariable.setType(EarlierMarkVariable.TYPE_EUROPEAN); break;
            }
        }

        return earlierMarkVariable;
    }

    public Map<String, Object> createUserdocDesignsGroundDataVariables(List<CSingleDesignGroundData> designsGroundData, int index, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();

        //TODO
        return variables;
    }

    public Map<String, Object> createUserdocExtraDataVariables(List<CUserdocExtraData> extraData, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();

        extraData.stream().filter(data -> !data.getType().getCode().equals(LICENSE_EXPIRATION_DATE)).forEach(data -> {
            String code = data.getType().getCode();
            Object value = determineExtraDataValue(data);
            if(data.getType().getCode().equals(LICENSE_EXPIRATION_DATE_TYPE)){
                code = LICENSE_EXPIRATION_DATE;
                if(value instanceof Boolean && ((Boolean)value).booleanValue() == false) {
                    Optional<CUserdocExtraData> dateDataOpt = extraData.stream().filter(e -> e.getType().getCode().equals(LICENSE_EXPIRATION_DATE)).findFirst();
                    if(dateDataOpt.isPresent()){
                        value = determineExtraDataValue(dateDataOpt.get());
                    }
                }
            }

            String variable = mapExtraDataCodeToVariable(code);
            if(variable != null && value != null){
                variables.put(getUserdocFinalVariable(variable, isUserdocParent), value);
            }
        });

        return variables;
    }

    public Map<String, Object> createDesignServiceScopeVariables(boolean isFullScope, List<CUserdocSingleDesign> singleDesigns, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();
        if(isFullScope){
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_DESIGNS_LIST, isUserdocParent), "Всички единични дизайни на промишления дизайн");
        } else if (CollectionUtils.isNotEmpty(singleDesigns)) {
            StringBuilder stringBuilderDesigns = new StringBuilder();
            List<DesignImageWrapperVariable> wrapperVariableList = new ArrayList<>();
            Function<CUserdocSingleDesign, String> getSingleDesignIdFn = design -> {
                String dsNbr = design.getFileId().getFileNbr().toString();
                String singleDesignNumberFirstPart =  dsNbr.substring(0,dsNbr.length() - 3);
                String singleDesignNumberSecondPart =  dsNbr.substring(dsNbr.length() - 3);
                String singleDesignId = singleDesignNumberFirstPart+"-"+singleDesignNumberSecondPart;
                return singleDesignId;
            } ;
            singleDesigns.stream().forEach(design -> {
                String designId = getSingleDesignIdFn.apply(design);

                CPatent singleDesignWithImage = designService.findSingleDesign(design.getFileId().getFileSeq(), design.getFileId().getFileType(),
                        design.getFileId().getFileSeries(), design.getFileId().getFileNbr(), true);
                if(singleDesignWithImage != null && singleDesignWithImage.getTechnicalData() != null && singleDesignWithImage.getTechnicalData().getDrawingList() != null && singleDesignWithImage.getTechnicalData().getDrawingList().size()>0){
                    DesignImageWrapperVariable wrapperVariable = CommonVariablesUtils.buildDesignImageWrapperVariable(singleDesignWithImage.getTechnicalData().getDrawingList(), designId);
                    wrapperVariableList.add(wrapperVariable);
                }
                stringBuilderDesigns.append(designId).append(" ").
                        append(design.getProductTitle()).append(SEPARATOR);
            });
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_DESIGNS_LIST, isUserdocParent), CommonVariablesUtils.getStringWithoutTrailing(stringBuilderDesigns, SEPARATOR.length()));
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_DESIGNS_LIST_COMPLEX, isUserdocParent), CommonVariablesUtils.createLabeledContentArray(singleDesigns, getSingleDesignIdFn, design -> design.getProductTitle()));
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_DESIGNS_IMAGES, isUserdocParent), wrapperVariableList.toArray(DesignImageWrapperVariable[]::new));
        }
        return variables;
    }

    public Map<String, Object> createMarkServiceScopeVariables(boolean isFullScope, CProtectionData protectiopnData, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();
        if(isFullScope){
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_NICE_CLASS_LIST, isUserdocParent), "Всички стоки и/или услуги на марката");
        } else if (protectiopnData != null && CollectionUtils.isNotEmpty(protectiopnData.getNiceClassList())) {
            StringBuilder niceListBuilder = new StringBuilder();
            StringBuilder niceClassesBuilder = new StringBuilder();
            protectiopnData.getNiceClassList().stream().forEach(nice -> {
                niceListBuilder.append(CommonVariablesUtils.buildFormattedNiceClassTerms(nice.getNiceClassNbr(), nice.getNiceClassDescription(), INNER_SEPARATOR));
                niceClassesBuilder.append(nice.getNiceClassNbr()).append(INNER_SEPARATOR);
            });
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_NICE_CLASS_LIST, isUserdocParent), CommonVariablesUtils.getStringWithoutTrailing(niceListBuilder, INNER_SEPARATOR.length()));
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_NICE_CLASS_LIST_COMPLEX, isUserdocParent), CommonVariablesUtils.createLabeledContentArray(protectiopnData.getNiceClassList(), nice -> nice.getNiceClassNbr().toString(), nice -> nice.getNiceClassDescription()));
            variables.put(getUserdocFinalVariable(Variables.USERDOC_SCOPE_NICE_CLASSES, isUserdocParent), CommonVariablesUtils.getStringWithoutTrailing(niceClassesBuilder, INNER_SEPARATOR.length()));
        }
        return variables;
    }

    private Map<String, Object> createApprovedDataVariables(CUserdocApprovedData approvedData, List<CNiceClass> approvedNiceClasses, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();

        if(approvedData.getApprovedAllNice() != null && approvedData.getApprovedAllNice()){
            variables.put(getUserdocFinalVariable(Variables.USERDOC_APPROVED_NICE_LIST, isUserdocParent), "Всички стоки и/или услуги на марката");
        } else if(approvedNiceClasses != null) {
            StringBuilder niceListBuilder = new StringBuilder();
            approvedNiceClasses.stream().forEach(nice -> {
                niceListBuilder.append(CommonVariablesUtils.buildFormattedNiceClassTerms(nice.getNiceClassNbr(), nice.getNiceClassDescription(), INNER_SEPARATOR));
            });
            variables.put(getUserdocFinalVariable(Variables.USERDOC_APPROVED_NICE_LIST, isUserdocParent), CommonVariablesUtils.getStringWithoutTrailing(niceListBuilder, INNER_SEPARATOR.length()));
            variables.put(getUserdocFinalVariable(Variables.USERDOC_APPROVED_NICE_LIST_COMPLEX, isUserdocParent), CommonVariablesUtils.createLabeledContentArray(approvedNiceClasses, nice -> nice.getNiceClassNbr().toString(), nice -> nice.getNiceClassDescription()));
        }
        return variables;
    }

    public Map<String, Object> createUserdocOrderVariables(CUserdoc userdoc, boolean isUserdocParent){
        Map<String, Object> variables = new HashMap<>();
        if(userdoc.getProcessSimpleData() != null && userdoc.getProcessSimpleData().getProcessId() != null) {
            CProcess process = processService.selectProcess(userdoc.getProcessSimpleData().getProcessId(), true);
            if(process.getProcessEventList() != null) {
                COffidoc orderOffidoc = process.getProcessEventList().stream().filter(ev -> (ev.getEventProcessId().getProcessNbr() != process.getProcessId().getProcessNbr())).map(ev -> {
                    CProcess subProcess = processService.selectProcess(ev.getEventProcessId(), true);
                    if(subProcess.getProcessEventList() != null){
                        return subProcess.getProcessEventList().stream().map(subEv -> {
                            if(subEv.getEventAction() != null && subEv.getEventAction().getGeneratedOffidoc() != null &&
                                    subEv.getEventAction().getGeneratedOffidoc().getOffidocType() != null &&
                                    subEv.getEventAction().getGeneratedOffidoc().getOffidocType().getOffidocType() != null &&
                                    subEv.getEventAction().getGeneratedOffidoc().getOffidocType().getOffidocType().equals("ЗПСАНД")){
                                return subEv.getEventAction().getGeneratedOffidoc();
                            }
                            return null;
                        }).filter(cOffidoc -> cOffidoc != null).findFirst().orElse(null);
                    }
                    return null;
                }).filter(offidoc -> offidoc != null && offidoc.getExternalSystemId() != null).sorted((o1, o2) -> o2.getPrintDate().compareTo(o1.getPrintDate())).findFirst().orElse(null);
                if(orderOffidoc != null){
                    variables.put(getUserdocFinalVariable(Variables.USERDOC_LAST_ORDER_NUMBER, isUserdocParent), orderOffidoc.getExternalSystemId());
                }
            }
        }
        return variables;
    }

    private String mapRoleToVariable(UserdocPersonRole role){
        switch (role){
            case PAYEE: return Variables.USERDOC_PAYEE_LIST;
            case APPLICANT: return Variables.USERDOC_APPLICANT_LIST;
            case CREDITOR: return Variables.USERDOC_CREDITOR_LIST;
            case GRANTEE: return Variables.USERDOC_GRANTEE_LIST;
            case GRANTOR: return Variables.USERDOC_GRANTOR_LIST;
            case NEW_CORRESPONDENCE_ADDRESS: return Variables.USERDOC_NEW_CA_LIST;
            case NEW_REPRESENTATIVE: return Variables.USERDOC_NEW_REPRESENTATIVE_LIST;
            case NEW_OWNER: return Variables.USERDOC_NEW_OWNER_LIST;
            case OLD_CORRESPONDENCE_ADDRESS: return Variables.USERDOC_OLD_CA_LIST;
            case OLD_OWNER: return Variables.USERDOC_OLD_OWNER_LIST;
            case OLD_REPRESENTATIVE: return Variables.USERDOC_OLD_REPRESENTATIVE_LIST;
            case PAYER: return Variables.USERDOC_PAYER_LIST;
            case PLEDGER: return Variables.USERDOC_PLEDGER_LIST;
            case REPRESENTATIVE: return Variables.USERDOC_REPRESENTATIVE_LIST;
            default: throw new RuntimeException("ERROR converting userdoc person role to string");
        }
    }

    private String mapRoleToNameListVariable(UserdocPersonRole role){
        switch (role){
            case PAYEE: return Variables.USERDOC_PAYEE_NAME_LIST;
            case APPLICANT: return Variables.USERDOC_APPLICANT_NAME_LIST;
            case CREDITOR: return Variables.USERDOC_CREDITOR_NAME_LIST;
            case GRANTEE: return Variables.USERDOC_GRANTEE_NAME_LIST;
            case GRANTOR: return Variables.USERDOC_GRANTOR_NAME_LIST;
            case NEW_CORRESPONDENCE_ADDRESS: return Variables.USERDOC_NEW_CA_NAME_LIST;
            case NEW_REPRESENTATIVE: return Variables.USERDOC_NEW_REPRESENTATIVE_NAME_LIST;
            case NEW_OWNER: return Variables.USERDOC_NEW_OWNER_NAME_LIST;
            case OLD_CORRESPONDENCE_ADDRESS: return Variables.USERDOC_OLD_CA_NAME_LIST;
            case OLD_OWNER: return Variables.USERDOC_OLD_OWNER_NAME_LIST;
            case OLD_REPRESENTATIVE: return Variables.USERDOC_OLD_REPRESENTATIVE_NAME_LIST;
            case PAYER: return Variables.USERDOC_PAYER_NAME_LIST;
            case PLEDGER: return Variables.USERDOC_PLEDGER_NAME_LIST;
            case REPRESENTATIVE: return Variables.USERDOC_REPRESENTATIVE_NAME_LIST;
            default: throw new RuntimeException("ERROR converting userdoc person role to string");
        }
    }

    private String mapRoleToFormattedVariable(UserdocPersonRole role){
        switch (role){
            case PAYEE: return Variables.USERDOC_PAYEE_LIST_FORMATTED;
            case APPLICANT: return Variables.USERDOC_APPLICANT_LIST_FORMATTED;
            case CREDITOR: return Variables.USERDOC_CREDITOR_LIST_FORMATTED;
            case GRANTEE: return Variables.USERDOC_GRANTEE_LIST_FORMATTED;
            case GRANTOR: return Variables.USERDOC_GRANTOR_LIST_FORMATTED;
            case NEW_CORRESPONDENCE_ADDRESS: return Variables.USERDOC_NEW_CA_LIST_FORMATTED;
            case NEW_REPRESENTATIVE: return Variables.USERDOC_NEW_REPRESENTATIVE_LIST_FORMATTED;
            case NEW_OWNER: return Variables.USERDOC_NEW_OWNER_LIST_FORMATTED;
            case OLD_CORRESPONDENCE_ADDRESS: return Variables.USERDOC_OLD_CA_LIST_FORMATTED;
            case OLD_OWNER: return Variables.USERDOC_OLD_OWNER_LIST_FORMATTED;
            case OLD_REPRESENTATIVE: return Variables.USERDOC_OLD_REPRESENTATIVE_LIST_FORMATTED;
            case PAYER: return Variables.USERDOC_PAYER_LIST_FORMATTED;
            case PLEDGER: return Variables.USERDOC_PLEDGER_LIST_FORMATTED;
            case REPRESENTATIVE: return Variables.USERDOC_REPRESENTATIVE_LIST_FORMATTED;
            default: throw new RuntimeException("ERROR converting userdoc person role to string");
        }
    }

    private String mapExtraDataCodeToVariable(String code){
        switch (code){
            case EFFECTIVE_DATE: return Variables.USERDOC_EFFECTIVE_DATE;
            case LICENSE_EXPIRATION_DATE: return Variables.USERDOC_LICENCE_EXPIRATION_DATE;
            case APPEAL_DECISION_DATE: return Variables.USERDOC_APPEAL_DECISION_DATE;
            case APPEAL_DECISION_NUMBER: return Variables.USERDOC_APPEAL_DECISION_NUMBER;
            case BANKRUPTCY_COURT_NAME: return Variables.USERDOC_BANKRUPTCY_COURT_NAME;
            case BANKRUPTCY_CASE_NUMBER: return Variables.USERDOC_BANKRUPTCY_CASE_NUMBER;
            default: return null;
        }
    }

    private Object determineExtraDataValue(CUserdocExtraData extraData){
        if(extraData.getType().getIsDate()){
            return extraData.getDateValue();
        } else if(extraData.getType().getIsBoolean()){
            return extraData.getBooleanValue()? extraData.getType().getBooleanTextTrue() : extraData.getType().getBooleanTextFalse();
        } else if(extraData.getType().getIsNumber()){
            return extraData.getNumberValue();
        } else if(extraData.getType().getIsText()){
            return extraData.getTextValue();
        }
        return null;
    }

    private String getUserdocFinalVariable(String originalVar, boolean isParent){
        if(isParent){
            return Variables.USERDOC_PARENT_VAR_PREFIX+originalVar;
        } else {
            return originalVar;
        }

    }

    private boolean isTopProcessForMark(CUserdoc userdoc){
        if(userdoc.getUserdocParentData() != null && userdoc.getUserdocParentData().getTopProcessFileData() != null &&
                userdoc.getUserdocParentData().getTopProcessFileData() != null &&
                userdoc.getUserdocParentData().getTopProcessFileData().getFileId().getFileType() != null){
            String type = userdoc.getUserdocParentData().getTopProcessFileData().getFileId().getFileType();
            if(type.equals(FileType.DIVISIONAL_MARK.code()) || type.equals(FileType.MARK.code()) || type.equals(FileType.INTERNATIONAL_MARK.code()) ||
                    type.equals(FileType.GEOGRAPHICAL_INDICATIONS.code()) || type.equals(FileType.GEOGRAPHICAL_INDICATIONS_V.code()) ||
                    type.equals(FileType.INTERNATIONAL_MARK_I.code()) || type.equals(FileType.INTERNATIONAL_MARK_R.code())){
                return true;
            }
        }
        return false;
    }

    private boolean isTopProcessForDesign(CUserdoc userdoc){
        if(userdoc.getUserdocParentData() != null && userdoc.getUserdocParentData().getTopProcessFileData() != null &&
                userdoc.getUserdocParentData().getTopProcessFileData() != null &&
                userdoc.getUserdocParentData().getTopProcessFileData().getFileId().getFileType() != null){
            String type = userdoc.getUserdocParentData().getTopProcessFileData().getFileId().getFileType();
            if(type.equals(FileType.DIVISIONAL_DESIGN.code()) || type.equals(FileType.DESIGN.code()) || type.equals(FileType.INTERNATIONAL_DESIGN.code())){
                return true;
            }
        }
        return false;
    }

}
