package bg.duosoft.ipas.core.validation.userdoc.grounds;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.grounds.CGroundNiceClasses;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocSubGrounds;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.enums.EarlierRightTypes;
import bg.duosoft.ipas.enums.GroundCategoriesTypes;
import bg.duosoft.ipas.enums.GroundMarkTypes;
import bg.duosoft.ipas.enums.LegalGroundTypes;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import de.danielbechler.util.Collections;
import org.aspectj.weaver.IClassFileProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RootGroundsValidator implements IpasValidator<CUserdoc> {
    public static final Integer MOTIVES_LENGTH = 4096;
    //PANELS
    public static final String  DESIGN_ANNULMENT_PANEL = "design-annulment-request";
    public static final String  OPPOSITION_PANEL = "Opposition";
    public static final String  INVALIDITY_PANEL = "Invalidity";
    public static final String  LAST_SUB_GROUND_VERSION = "2019";

    @Autowired
    private MarkService markService;

    @Autowired
    private MessageSource messageSource;


    private boolean isPanelWithEarlierTypeData(String panel,CUserdocRootGrounds rootGround){
        return (panel.equalsIgnoreCase(OPPOSITION_PANEL) || panel.equalsIgnoreCase(DESIGN_ANNULMENT_PANEL) ||
                (Objects.nonNull(rootGround.getMarkGroundData()) && Objects.nonNull(rootGround.getMarkGroundData().getLegalGroundCategory())
                        && Objects.nonNull(rootGround.getMarkGroundData().getLegalGroundCategory().getCode())
                        && rootGround.getMarkGroundData().getLegalGroundCategory().getCode().equalsIgnoreCase(GroundCategoriesTypes.RELATIVE_GROUNDS.code())
                        && panel.equalsIgnoreCase(INVALIDITY_PANEL)));
    }

    private boolean isDesignAnnulmentMarkDataEnabled(CUserdocRootGrounds rootGround){
        if (!CollectionUtils.isEmpty(rootGround.getUserdocSubGrounds())){
            CUserdocSubGrounds subGround = rootGround.getUserdocSubGrounds().stream().filter(r -> r.getLegalGroundTypeId().equals(LegalGroundTypes.EARLIER_MARK.code())).findFirst().orElse(null);
            return Objects.nonNull(subGround);
        }
        return false;
    }

    @Override
    public List<ValidationError> validate(CUserdoc obj, Object... additionalArgs) {
        List<ValidationError> errors = new ArrayList<>();

        if (Objects.nonNull(obj.getUserdocRootGrounds()) && !CollectionUtils.isEmpty(obj.getUserdocRootGrounds())){

            CUserdocSubGrounds checkSubGroundVersionObject = obj.getUserdocRootGrounds().get(0).getUserdocSubGrounds().get(0);

            if (!StringUtils.isEmpty(checkSubGroundVersionObject.getVersion()) && !checkSubGroundVersionObject.getVersion().equals(LAST_SUB_GROUND_VERSION)){
             return errors;
            }


            CFileId cFileId = UserdocUtils.selectUserdocMainObject(obj.getUserdocParentData());
            CMark parentMark = markService.findMark(cFileId,false);

            obj.getUserdocRootGrounds().stream().forEach(rootGround->{
                obj.getUserdocType().getPanels().stream().forEach(panel->{
                    errors.addAll(hasLegalCategoriesDataErrors(rootGround,panel.getPanel()));
                    if (isPanelWithEarlierTypeData(panel.getPanel(),rootGround)){
                        if (Objects.isNull(rootGround.getEarlierRightType()) || Objects.isNull(rootGround.getEarlierRightType().getId())){
                            errors.add(ValidationError.builder().pointer("earlier.right.type").messageCode("required.field").build());
                        }else{
                            boolean designAnnulmentMarkDataEnabled = isDesignAnnulmentMarkDataEnabled(rootGround);
                            errors.addAll(hasApplicantAuthorityDataErrors(rootGround,panel.getPanel(),designAnnulmentMarkDataEnabled));
                            errors.addAll(hasMandatoryMarkDataErrors(rootGround,designAnnulmentMarkDataEnabled));
                            errors.addAll(hasNiceClassesDataErrors(rootGround,parentMark,designAnnulmentMarkDataEnabled));
                            errors.addAll(hasMandatoryCommonTextErrors(rootGround));
                            errors.addAll(hasMandatoryGIDataErrors(rootGround));
                        }
                    }
                });
                errors.addAll(hasCommonMandatoryDataErrors(rootGround));
            });
        }
        return errors;
    }
    private List<ValidationError> hasNiceClassesDataErrors(CUserdocRootGrounds rootGround,CMark parentMark,boolean designAnnulmentMarkDataEnabled){
        List<ValidationError> errors = new ArrayList<>();

        if (designAnnulmentMarkDataEnabled || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.EARLIER_REGISTERED_GEOGRAPH_12_8.code()) || (rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.EARLIER_MARK_LAW_12_2.code())
                || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.WELL_KNOWN_MARK_LAW_12_2.code()) || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.FAMOUS_MARK_LAW_12_3.code())
                || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.UNREGISTERED_MARK_USED_IN_TRADE_LAW_12_4.code()) || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.MARK_REQUESTED_BY_AGENT_12_6.code()))){

            if (Objects.isNull(rootGround.getMarkGroundData().getNiceClassesInd())){
                errors.add(ValidationError.builder().pointer("ground.niceClassesInd").messageCode("required.field").build());
            }else if(Objects.equals(rootGround.getMarkGroundData().getNiceClassesInd(),false) && Collections.isEmpty(rootGround.getMarkGroundData().getUserdocGroundsNiceClasses())){
                errors.add(ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes").build());
            }else{
                if (!Collections.isEmpty(rootGround.getMarkGroundData().getUserdocGroundsNiceClasses())){
                    for (CGroundNiceClasses niceClass:  rootGround.getMarkGroundData().getUserdocGroundsNiceClasses()){
                        if (Objects.isNull(niceClass.getNiceClassCode())){
                            errors.add(ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes.mandatory.class").build());
                            break;
                        }
                        if (Objects.isNull(niceClass.getNiceClassDescription()) || niceClass.getNiceClassDescription().isEmpty()){
                            errors.add(ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes.mandatory.description").build());
                        }
//                        if (!Objects.isNull(parentMark.getProtectionData()) && !Objects.isNull(parentMark.getProtectionData().getNiceClassList())){
//                            CNiceClass cNiceClass = parentMark.getProtectionData().getNiceClassList().stream().filter(markNiceClass -> niceClass.getNiceClassCode().equals(markNiceClass.getNiceClassNbr().longValue())).findFirst().orElse(null);
//                            if (Objects.isNull(cNiceClass)){
//                                String message = messageSource.getMessage("root.ground.nice.classes.not.contained.in.mark", new String[]{niceClass.getNiceClassCode().toString()}, LocaleContextHolder.getLocale());
//                                errors.add(ValidationError.builder().pointer("nice.classes.table.errors").message(message).build());
//                            }
//                        }
                        List<ValidationError> res = rootGround.getMarkGroundData().getUserdocGroundsNiceClasses()
                                .stream()
                                .filter(d -> d!=niceClass)
                                .filter(d -> Objects.equals(d.getNiceClassCode(), niceClass.getNiceClassCode()))
                                .map(d -> ValidationError.builder().pointer("nice.classes.table.errors").messageCode("root.ground.nice.classes.not.unique").build())
                                .collect(Collectors.toList());
                        errors.addAll(res);
                    }
                }
            }
        }
        return errors;
    }

    private List<ValidationError> hasCommonMandatoryDataErrors(CUserdocRootGrounds rootGround){
        List<ValidationError> errors = new ArrayList<>();
        if (!Objects.isNull(rootGround.getMotives()) && rootGround.getMotives().length()>MOTIVES_LENGTH){
            String message = messageSource.getMessage("invalid.extra.data.text1.symbols.length", new String[]{MOTIVES_LENGTH.toString()}, LocaleContextHolder.getLocale());
            errors.add(ValidationError.builder().pointer("ground.motives").message(message).build());
        }
        if (Objects.isNull(rootGround.getUserdocSubGrounds()) || CollectionUtils.isEmpty(rootGround.getUserdocSubGrounds())){
            errors.add(ValidationError.builder().pointer("ground.sub.types").messageCode("root.ground.sub.types.mandatory").build());
        }
        return errors;
    }

    private List<ValidationError> hasLegalCategoriesDataErrors(CUserdocRootGrounds rootGround,String panel){
        List<ValidationError> errors = new ArrayList<>();
        if (panel.equalsIgnoreCase(INVALIDITY_PANEL) && (Objects.isNull(rootGround.getMarkGroundData()) ||Objects.isNull(rootGround.getMarkGroundData().getLegalGroundCategory())
                || Objects.isNull(rootGround.getMarkGroundData().getLegalGroundCategory().getCode()))){
            errors.add(ValidationError.builder().pointer("ground.legal.categories").messageCode("required.field").build());
        }
        return errors;
    }

    private List<ValidationError> hasApplicantAuthorityDataErrors(CUserdocRootGrounds rootGround,String panel,boolean designAnnulmentMarkDataEnabled){
        List<ValidationError> errors = new ArrayList<>();

        if ((!panel.equals(DESIGN_ANNULMENT_PANEL) || designAnnulmentMarkDataEnabled) && (Objects.isNull(rootGround.getApplicantAuthority()) || Objects.isNull(rootGround.getApplicantAuthority().getId()))){
            errors.add(ValidationError.builder().pointer("applicant.authority").messageCode("required.field").build());
        }
        return errors;
    }


    private List<ValidationError> hasMandatoryGIDataErrors(CUserdocRootGrounds rootGround){
        List<ValidationError> errors = new ArrayList<>();
        if (rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.EARLIER_REGISTERED_GEOGRAPH_12_8.code())){
            if (Objects.isNull(rootGround.getMarkGroundData().getGeographicalIndTyp())){
                errors.add(ValidationError.builder().pointer("ground.geographicalIndTyp").messageCode("required.field").build());
            }
        }

        return errors;
    }

    private List<ValidationError> hasMandatoryMarkDataErrors(CUserdocRootGrounds rootGround,boolean designAnnulmentMarkDataEnabled){
        List<ValidationError> errors = new ArrayList<>();
        if (designAnnulmentMarkDataEnabled || (rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.EARLIER_MARK_LAW_12_2.code())
                || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.WELL_KNOWN_MARK_LAW_12_2.code())
                || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.FAMOUS_MARK_LAW_12_3.code())
                || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.UNREGISTERED_MARK_USED_IN_TRADE_LAW_12_4.code())
                || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.MARK_REQUESTED_BY_AGENT_12_6.code()))){

            if (Objects.isNull(rootGround.getMarkGroundData().getMarkGroundType()) || Objects.isNull(rootGround.getMarkGroundData().getMarkGroundType().getId())){
                errors.add(ValidationError.builder().pointer("mark.ground.type").messageCode("required.field").build());
            }else{
                if (rootGround.getMarkGroundData().getMarkGroundType().getId().equals(GroundMarkTypes.NATIONAL_MARK.code()) && rootGround.getMarkGroundData().getMarkImportedInd()){
                    if (Objects.isNull(rootGround.getMarkGroundData().getFilingNumber()) || rootGround.getMarkGroundData().getFilingNumber().isEmpty()){
                        errors.add(ValidationError.builder().pointer("ground.national.mark").messageCode("required.field").build());
                    }
                }
                if (!rootGround.getMarkGroundData().getMarkImportedInd()){
                    if ((Objects.isNull(rootGround.getMarkGroundData().getNameText()) || rootGround.getMarkGroundData().getNameText().isEmpty()) && Objects.isNull(rootGround.getMarkGroundData().getNameData())){
                        errors.add(ValidationError.builder().pointer("ground.name.text.on.mark.earlier.type").messageCode("root.ground.mandatory.name.or.data").build());
                    }
                }

            }
        }
        return errors;
    }
    private List<ValidationError> hasMandatoryCommonTextErrors(CUserdocRootGrounds rootGround){
        List<ValidationError> errors = new ArrayList<>();
        if((Objects.isNull(rootGround.getGroundCommonText()) || rootGround.getGroundCommonText().isEmpty()) &&
                (rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.BAD_REQUEST_LAW_12_5.code())
                        || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.TRADING_COMPANY_12_7.code())
                        || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.COPYRIGHT.code())
                        || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.INDUSTRIAL_PROPERTY_RIGHT.code())
                        || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.NAME_AND_PORTRAIT_RIGHT.code())
                        || rootGround.getEarlierRightType().getId().equals(EarlierRightTypes.PLANT_AND_BREEDS_RIGHT.code()))){
            errors.add(ValidationError.builder().pointer("ground-common-text").messageCode("required.field").build());
            return errors;
        }
        return  errors;
    }


}
