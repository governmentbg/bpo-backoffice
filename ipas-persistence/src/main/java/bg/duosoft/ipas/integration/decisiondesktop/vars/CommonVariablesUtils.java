package bg.duosoft.ipas.integration.decisiondesktop.vars;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.person.COwner;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.enums.AttachmentType;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.DesignImageVariable;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.DesignImageWrapperVariable;
import bg.duosoft.ipas.integration.decisiondesktop.model.backoffice.LabeledContentVariable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 27.05.2021
 * Time: 12:13
 */
public class CommonVariablesUtils {

    public static final String EGN_EIK_APPENDABLE = ", ЕГН/ЕИК: ";
    public static final String ADDRESS_APPENDABLE = ", адрес: ";
    public static final String WITH_ADDRESS_LOWERCASE_APPENDABLE = ", с адрес: ";
    public static final String SEPARATOR = "; ";
    public static final String INNER_SEPARATOR = ", ";
    public static final String NICE_CLASS_APPENDABLE = "Клас ";
    public static final String AND_APPENDABLE = " и ";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static Map<String, Object> createGeneralVariables(){
        Map<String, Object> variables = new HashMap<>();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Sofia"));
        Date today = calendar.getTime();
        variables.put(Variables.GENERAL_DATE_TODAY, today);

        return variables;
    }

    public static Map<String, Object> createFileDataVariables(CFile file){
        Map<String, Object> variables = new HashMap<>();

        variables.put(Variables.FILE_APPLICATION_DATE, file.getFilingData().getFilingDate());
        variables.put(Variables.FILE_APPLICATION_NUMBER, file.getFileId().getFileNbr());

        if(file.getRegistrationData() != null){
            if(file.getRegistrationData().getRegistrationId() != null) {
                variables.put(Variables.FILE_REGISTRATION_NUMBER, file.getRegistrationData().getRegistrationId().getRegistrationNbr());
            }
            variables.put(Variables.FILE_REGISTRATION_DATE, file.getRegistrationData().getRegistrationDate());
            variables.put(Variables.FILE_EXPIRY_DATE, file.getRegistrationData().getExpirationDate());
        }

        if(file.getOwnershipData() != null && CollectionUtils.isNotEmpty(file.getOwnershipData().getOwnerList())){
            variables.putAll(CommonVariablesUtils.createOwnershipDataVariables(file.getOwnershipData().getOwnerList(), Variables.FILE_VAR_PREFIX));
        }
        if(file.getRepresentationData() != null && CollectionUtils.isNotEmpty(file.getRepresentationData().getRepresentativeList())){
            variables.putAll(CommonVariablesUtils.createRepresentationDataVariables(file.getRepresentationData().getRepresentativeList(), Variables.FILE_VAR_PREFIX));
        }

        if(file.getServicePerson() != null){
            variables.putAll(CommonVariablesUtils.createServicePersonDataVariables(file.getServicePerson(), Variables.FILE_VAR_PREFIX));
        }
        if(file.getPriorityData() != null && file.getPriorityData().getEarliestAcceptedParisPriorityDate() != null) {
            variables.put(Variables.FILE_PRIORITY_DATE, file.getPriorityData().getEarliestAcceptedParisPriorityDate());
        }

        if(file.getProcessSimpleData() != null && file.getProcessSimpleData().getResponsibleUser() != null && file.getProcessSimpleData().getResponsibleUser().getUserName() != null){
            variables.put(Variables.FILE_EXPERT_NAME, file.getProcessSimpleData().getResponsibleUser().getUserName());
        }

        return variables;
    }

    public static Map<String, Object> createOwnershipDataVariables(List<COwner> owners, String varsPrefix){
        Map<String, Object> variables = new HashMap<>();
        if(owners.size()>0 && owners.get(0) != null && owners.get(0).getPerson() != null){
            COwner firstOwner = owners.get(0);
            variables.put(varsPrefix + Variables.OWNER_NAME, firstOwner.getPerson().getPersonName());
            variables.put(varsPrefix + Variables.OWNER_ADDRESS, buildPersonAddress(firstOwner.getPerson()));
        }

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilderFormatted = new StringBuilder();
        owners.stream().filter(owner -> owner.getPerson() != null).forEach(owner -> {
            stringBuilder.append(owner.getPerson().getPersonName());
            stringBuilder.append(WITH_ADDRESS_LOWERCASE_APPENDABLE);
            stringBuilder.append(buildPersonAddress(owner.getPerson()));
            stringBuilder.append(AND_APPENDABLE);

            stringBuilderFormatted.append(owner.getPerson().getPersonName());
            if(StringUtils.isNotEmpty(owner.getPerson().getIndividualIdTxt())) {
                stringBuilderFormatted.append(EGN_EIK_APPENDABLE).append(owner.getPerson().getIndividualIdTxt());
            }
            stringBuilderFormatted.append(ADDRESS_APPENDABLE).append(buildPersonAddress(owner.getPerson()));
            stringBuilderFormatted.append(SEPARATOR);
        });

        variables.put(varsPrefix + Variables.OWNER_LIST, getStringWithoutTrailing(stringBuilder, AND_APPENDABLE.length()));
        variables.put(varsPrefix + Variables.OWNER_NAME_LIST, owners.stream().filter(owner -> owner.getPerson() != null).
                map(owner-> owner.getPerson().getPersonName()).reduce((n1, n2) ->  new StringBuilder(n1).append(SEPARATOR).append(n2).toString()));
        variables.put(varsPrefix + Variables.OWNER_LIST_FORMATTED, getStringWithoutTrailing(stringBuilderFormatted, SEPARATOR.length()));

        return variables;
    }

    public static Map<String, Object> createRepresentationDataVariables(List<CRepresentative> representatives, String varsPrefix){
        Map<String, Object> variables = new HashMap<>();
        if(representatives.size()>0 && representatives.get(0) != null && representatives.get(0).getPerson() != null){
            CRepresentative firstRepresentative = representatives.get(0);
            variables.put(varsPrefix + Variables.REPRESENTATIVE_NAME, firstRepresentative.getPerson().getPersonName());
            variables.put(varsPrefix + Variables.REPRESENTATIVE_ADDRESS, buildPersonAddress(firstRepresentative.getPerson()));
        }

        StringBuilder stringBuilder = new StringBuilder();
        representatives.stream().filter(rep -> rep.getPerson() != null).forEach(rep -> {
            stringBuilder.append(rep.getPerson().getPersonName());
            if(rep.getPerson().hasAgentCode()) {
                stringBuilder.append(" (").append(rep.getPerson().getAgentCode()).append(")");
            }
            stringBuilder.append(WITH_ADDRESS_LOWERCASE_APPENDABLE);
            stringBuilder.append(buildPersonAddress(rep.getPerson()));
            stringBuilder.append(AND_APPENDABLE);
        });

        variables.put(varsPrefix + Variables.REPRESENTATIVE_LIST, getStringWithoutTrailing(stringBuilder, AND_APPENDABLE.length()));

        return variables;
    }

    public static Map<String, Object> createServicePersonDataVariables(CPerson servicePerson, String prefix){
        Map<String, Object> variables = new HashMap<>();
        if(servicePerson != null) {
            variables.put(prefix+Variables.SERVICE_PERSON_NAME, servicePerson.getPersonName());
            variables.put(prefix+Variables.SERVICE_PERSON_ADDRESS, CommonVariablesUtils.buildPersonAddress(servicePerson));
        }
        return variables;
    }

    public static String buildPersonAddress(CPerson person){
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotEmpty(person.getAddressStreet())){
            stringBuilder.append(person.getAddressStreet());
        }
        if(StringUtils.isNotEmpty(person.getCityName())) {
            if(stringBuilder.length()>0){
                stringBuilder.append(INNER_SEPARATOR);
            }
            if(StringUtils.isNotEmpty(person.getZipCode())){
                stringBuilder.append(person.getZipCode()).append(" ");
            }
            stringBuilder.append(person.getCityName());
        }
        if(StringUtils.isNotEmpty(person.getResidenceCountryCode()) && !person.getResidenceCountryCode().equals("BG")){
            if(StringUtils.isNotEmpty(person.getStateName())){
                if(stringBuilder.length()>0){
                    stringBuilder.append(INNER_SEPARATOR);
                }
                stringBuilder.append(person.getStateName());
            }
            if(stringBuilder.length()>0){
                stringBuilder.append(INNER_SEPARATOR);
            }
            stringBuilder.append(person.getResidenceCountryCode());
        }
        if(StringUtils.isEmpty(person.getCityName()) && StringUtils.isNotEmpty(person.getZipCode())){
            if(stringBuilder.length()>0){
                stringBuilder.append(INNER_SEPARATOR);
            }
            stringBuilder.append(person.getZipCode());
        }

        return stringBuilder.toString();
    }

    public static String buildMarkImageBase64(List<CMarkAttachment> markAttachments){
        if(markAttachments != null && markAttachments.size()>0) {
            CMarkAttachment attachment = markAttachments.stream().filter(att ->
                    att.getAttachmentType().equals(AttachmentType.IMAGE)).findFirst().orElse(null);
            if (attachment != null) {
               return buildMarkImageBase64(attachment.getData());
            }
        }
        return null;
    }

    public static String buildMarkImageBase64(byte[] data){
        String encoded = Base64.getEncoder().encodeToString(data);
        String image = new StringBuilder("data:").append("image/png").
                append(";base64,").append(encoded).toString();
        return image;
    }

    public static byte[] getMarkImageData (List<CMarkAttachment> markAttachments){
        if(markAttachments != null && markAttachments.size()>0) {
            CMarkAttachment attachment = markAttachments.stream().filter(att ->
                    att.getAttachmentType().equals(AttachmentType.IMAGE)).findFirst().orElse(null);
            if (attachment != null) {
                return attachment.getData();
            }
        }
        return null;
    }

    public static String getStringWithoutTrailing(StringBuilder sb, int trailingSize){
        if(sb.length() > trailingSize){
            return sb.substring(0, sb.length()-trailingSize);
        } else {
            return sb.toString();
        }
    }

    public static StringBuilder buildFormattedNiceClassTerms(Integer niceClass, String terms, String trailing){
        return new StringBuilder(NICE_CLASS_APPENDABLE).append(niceClass).append(": ").append(terms).append(trailing);
    }

    public static DesignImageWrapperVariable buildDesignImageWrapperVariable(List<CDrawing> designDrawings, String singleDesignId) {
        DesignImageWrapperVariable wrapperVariable = new DesignImageWrapperVariable();
        wrapperVariable.setDesignId(singleDesignId);
        List<DesignImageVariable> designImageVariables = designDrawings.stream().filter(drawing -> drawing.getDrawingData() != null).map(drawing -> {
            DesignImageVariable designImageVariable = new DesignImageVariable();
            designImageVariable.setImageBytes(drawing.getDrawingData());
            if(drawing.getSingleDesignExtended() != null && drawing.getSingleDesignExtended().getImageViewType() != null) {
                designImageVariable.setType(drawing.getSingleDesignExtended().getImageViewType().getViewTypeName());
            }
            return designImageVariable;
        }).collect(Collectors.toList());
        wrapperVariable.setDesignImageVariables(designImageVariables.toArray(DesignImageVariable[]::new));
        return wrapperVariable;
    }

    public static<T> LabeledContentVariable[] createLabeledContentArray(List<T> list, Function<T, String> labelFn, Function<T, String> contentFn){
        List<LabeledContentVariable> labeledContentVariableList = new ArrayList<>();

        if(list != null) {
            labeledContentVariableList = list.stream().map(e -> new LabeledContentVariable(labelFn.apply(e), contentFn.apply(e))).collect(Collectors.toList());
        }

        return labeledContentVariableList.toArray(LabeledContentVariable[]::new);
    }
}
