package bg.duosoft.ipas.util.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.grounds.CEarlierRightTypes;
import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import bg.duosoft.ipas.core.model.util.CUserdocRootGroundsExt;
import bg.duosoft.ipas.core.service.userdoc.UserdocRootGroundService;
import bg.duosoft.ipas.enums.EarlierRightTypes;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.GroundMarkTypes;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class UserdocGroundsUtils {

    public static Integer getNewGroundId(List<CUserdocRootGrounds> rootGrounds){
        Integer maxGroundId=0;
        if (!Objects.isNull(rootGrounds) && !CollectionUtils.isEmpty(rootGrounds)){
            for (CUserdocRootGrounds ground:rootGrounds) {
                maxGroundId = ground.getRootGroundId() > maxGroundId ? ground.getRootGroundId():maxGroundId;
            }
        }
        return ++maxGroundId;
    }

    public static boolean isNonNationalMarkSubSectionVisible(Integer groundMarkType){
        if (!Objects.isNull(groundMarkType) && (Objects.equals(groundMarkType, GroundMarkTypes.INTERNATIONAL_MARK.code())
                || Objects.equals(groundMarkType, GroundMarkTypes.PUBLIC_MARK.code()))){
            return true;
        }
        return false;
    }

    public static String constructMarkGroundFilingNumber(CUserdocRootGrounds rootGrounds) {
        if (Objects.isNull(rootGrounds) || Objects.isNull(rootGrounds.getMarkGroundData())){
            return null;
        }
        return Objects.isNull(rootGrounds.getMarkGroundData().getMarkImportedInd()) || rootGrounds.getMarkGroundData().getMarkImportedInd() == false ? rootGrounds.getMarkGroundData().getFilingNumber() : rootGrounds.getMarkGroundData().getFilingNumber().split("/")[3];
    }


    public static boolean isNationalMarkSubSectionVisible(Integer groundMarkType){
        if (!Objects.isNull(groundMarkType) && Objects.equals(groundMarkType, GroundMarkTypes.NATIONAL_MARK.code())){
            return true;
        }
        return false;
    }


    public static boolean isMarkSubSectionVisible(Integer earlierRightTypeId){

        if (!Objects.isNull(earlierRightTypeId) && (Objects.equals(earlierRightTypeId, EarlierRightTypes.EARLIER_MARK_LAW_12_2.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.WELL_KNOWN_MARK_LAW_12_2.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.FAMOUS_MARK_LAW_12_3.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.UNREGISTERED_MARK_USED_IN_TRADE_LAW_12_4.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.MARK_REQUESTED_BY_AGENT_12_6.code()))){
            return true;
        }
        return false;
    }

    public static boolean isApplicantAuthoritiesSectionVisible(Integer earlierRightTypeId){
        if(!Objects.isNull(earlierRightTypeId)){
            return true;
        }
        return false;
    }

    public static boolean isMandatoryDescriptionSubSectionVisible(Integer earlierRightTypeId){
        if (!Objects.isNull(earlierRightTypeId) && (Objects.equals(earlierRightTypeId, EarlierRightTypes.BAD_REQUEST_LAW_12_5.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.TRADING_COMPANY_12_7.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.COPYRIGHT.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.INDUSTRIAL_PROPERTY_RIGHT.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.NAME_AND_PORTRAIT_RIGHT.code()) ||
                Objects.equals(earlierRightTypeId, EarlierRightTypes.PLANT_AND_BREEDS_RIGHT.code()))){
            return true;
        }
        return false;
    }

    public static boolean isGeographicalIndicationsSubSectionVisible(Integer earlierRightTypeId){
        if (!Objects.isNull(earlierRightTypeId) && Objects.equals(earlierRightTypeId, EarlierRightTypes.EARLIER_REGISTERED_GEOGRAPH_12_8.code())){
            return true;
        }
        return false;
    }


    public static void loadUserdocGroundEmptyNameData(CUserdoc sessionUserdoc, UserdocRootGroundService userdocRootGroundService){
        List<CUserdocRootGrounds> rootGrounds = sessionUserdoc.getUserdocRootGrounds();
        if (!CollectionUtils.isEmpty(rootGrounds)){
            for (CUserdocRootGrounds rootGround: rootGrounds) {
                if (!(rootGround instanceof CUserdocRootGroundsExt)){
                    if (Objects.nonNull(rootGround.getMarkGroundData())){
                        rootGround.getMarkGroundData().setNameData(userdocRootGroundService.findById(rootGround.getRootGroundId(), sessionUserdoc.getDocumentId().getDocOrigin(),
                                sessionUserdoc.getDocumentId().getDocLog(), sessionUserdoc.getDocumentId().getDocSeries(), sessionUserdoc.getDocumentId().getDocNbr()).getMarkGroundData().getNameData());
                    }

                }
            }
        }
    }

    public static boolean userdocGroundsEditEnabled(boolean editEnabled,List<CUserdocRootGrounds> rootGrounds,String newestVersion){
        if (!editEnabled){
            return  false;
        }else if (Objects.isNull(rootGrounds) || CollectionUtils.isEmpty(rootGrounds)){
            return true;
        }
        else{
            return rootGrounds.get(0).getUserdocSubGrounds().get(0).getVersion().equals(newestVersion);
        }
    }

    public static boolean hasGroundImg(CEarlierRightTypes earlierRightType,Boolean markImported){
        if (Objects.isNull(earlierRightType) || (Objects.nonNull(markImported) && markImported)){
            return false;
        }else if (earlierRightType.getId().equals(EarlierRightTypes.EARLIER_MARK_LAW_12_2.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.FAMOUS_MARK_LAW_12_3.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.UNREGISTERED_MARK_USED_IN_TRADE_LAW_12_4.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.MARK_REQUESTED_BY_AGENT_12_6.code())){
            return true;
        }
        else{
            return false;
        }
    }


    public static boolean hasGroundIpObject(CEarlierRightTypes earlierRightType){
        if (Objects.isNull(earlierRightType)){
            return false;
        }else if (earlierRightType.getId().equals(EarlierRightTypes.EARLIER_MARK_LAW_12_2.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.FAMOUS_MARK_LAW_12_3.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.UNREGISTERED_MARK_USED_IN_TRADE_LAW_12_4.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.MARK_REQUESTED_BY_AGENT_12_6.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.EARLIER_REGISTERED_GEOGRAPH_12_8.code())){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean hasGroundMandatoryCommonText(CEarlierRightTypes earlierRightType){
        if (Objects.isNull(earlierRightType)){
            return false;
        }else if (earlierRightType.getId().equals(EarlierRightTypes.BAD_REQUEST_LAW_12_5.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.TRADING_COMPANY_12_7.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.COPYRIGHT.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.NAME_AND_PORTRAIT_RIGHT.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.PLANT_AND_BREEDS_RIGHT.code()) ||
                earlierRightType.getId().equals(EarlierRightTypes.INDUSTRIAL_PROPERTY_RIGHT.code())){
            return true;
        }
        else{
            return false;
        }
    }

}
