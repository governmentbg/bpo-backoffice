package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

public class DesignUtils {
    public static List<CPatent> getAllSingleDesignsForIndDesign(CPatent patent, DesignService designService) {
        List<CPatent> singleDesigns = designService.getAllSingleDesignsForIndustrialDesign(patent, false);
        if (singleDesigns != null) {
            singleDesigns.forEach(r -> PatentDrawingUtils.initCPatentDrawings(r));
        }
        return singleDesigns;
    }

    public static String formatSingleDesignRegistrationNbr(Long registrationNbr,Integer mainDesignFileNbr,Integer singleDesignFileNbr){
        if (mainDesignFileNbr> DefaultValue.MAX_SINGLE_DESIGN_NBR_LENGTH){
            return String.valueOf(registrationNbr+"-"+singleDesignFileNbr);
        }else{
            String singleDesignFileNbrAsString = String.valueOf(singleDesignFileNbr);
            String singleDesignIndex =  singleDesignFileNbrAsString.substring(singleDesignFileNbrAsString.length() - 3);
            return String.valueOf(registrationNbr+"-"+singleDesignIndex);
        }
    }


    public static String formatSingleDesignIdentityNumber(String singleDesignFilingNumber,String title){
        String formattedSingleDesignFilingNumber = formatSingleDesignIdentityNumber(singleDesignFilingNumber);
        String resultName=formattedSingleDesignFilingNumber;
        if (!StringUtils.isEmpty(title)){
            resultName = resultName.concat(" - ").concat(title);
        }
        return resultName;
    }


    public static String formatSingleDesignIdentityNumber(String singleDesignFilingNumber){
            String[] designPkArray = singleDesignFilingNumber.split("/");
            String singleDesignNbrAsString = designPkArray[3];
            String singleDesignNumberFirstPart =  singleDesignNbrAsString.substring(0,singleDesignNbrAsString.length() - 3);
            String singleDesignNumberSecondPart =  singleDesignNbrAsString.substring(singleDesignNbrAsString.length() - 3);
            if (Long.valueOf(singleDesignNumberSecondPart) > DefaultValue.LENGTH_100){
                return designPkArray[0] + "/" + designPkArray[1] + "/" + designPkArray[2] + "/" + singleDesignNbrAsString;
            }
            return designPkArray[0] + "/" + designPkArray[1] + "/" + designPkArray[2] + "/" + singleDesignNumberFirstPart+"-"+singleDesignNumberSecondPart;

    }

    public static String formatSingleDesignIdentityNumber(Integer mainDesignFileNbr,String singleDesignFilingNumber){
        if (mainDesignFileNbr> DefaultValue.MAX_SINGLE_DESIGN_NBR_LENGTH){
            return singleDesignFilingNumber;
        }else{
            String[] designPkArray = singleDesignFilingNumber.split("/");
            String singleDesignNbrAsString = designPkArray[3];
            String singleDesignNumberFirstPart =  singleDesignNbrAsString.substring(0,singleDesignNbrAsString.length() - 3);
            String singleDesignNumberSecondPart =  singleDesignNbrAsString.substring(singleDesignNbrAsString.length() - 3);
             return designPkArray[0] + "/" + designPkArray[1] + "/" + designPkArray[2] + "/" + singleDesignNumberFirstPart+"-"+singleDesignNumberSecondPart;
        }
    }


}
