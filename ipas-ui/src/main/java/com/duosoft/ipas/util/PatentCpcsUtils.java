package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import com.duosoft.ipas.util.json.PatentCpcData;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PatentCpcsUtils {
    public static void sortCpcList(List<CPatentCpcClass> cPatentCpcClasses) {
        long orderedCpcQualif = 1;
        for (int i = 0; i < cPatentCpcClasses.size(); i++) {
            cPatentCpcClasses.get(i).setCpcQualification(String.valueOf(orderedCpcQualif));
            orderedCpcQualif++;
        }
    }

    public static boolean cpcSwapPositionsValidation(CPatentCpcClass obj, CPatentCpcClass obj1) {

        return !(Objects.equals(obj.getCpcQualification(), obj1.getCpcQualification()) && Objects.equals(obj.getCpcEdition(), obj1.getCpcEdition())
                && Objects.equals(obj.getCpcSection(), obj1.getCpcSection()) && Objects.equals(obj.getCpcClass(), obj1.getCpcClass())
                && Objects.equals(obj.getCpcSubclass(), obj1.getCpcSubclass()) && Objects.equals(obj.getCpcGroup(), obj1.getCpcGroup())
                && Objects.equals(obj.getCpcSubgroup(), obj1.getCpcSubgroup()));
    }


    public static void swapCpcPosition(List<CPatentCpcClass> cpcList, PatentCpcData patentCpcData, boolean isHigherPosition) {
        for (int i = 0; i < cpcList.size(); i++) {
            CPatentCpcClass cPatentCpcClass = cpcList.get(i);
            if (cPatentCpcClass.getCpcEdition().equals(patentCpcData.getCpcEdition()) && cPatentCpcClass.getCpcSection().equals(patentCpcData.getCpcSection())
                    && cPatentCpcClass.getCpcClass().equals(patentCpcData.getCpcClass()) && cPatentCpcClass.getCpcSubclass().equals(patentCpcData.getCpcSubclass())
                    && cPatentCpcClass.getCpcGroup().equals(patentCpcData.getCpcGroup()) && cPatentCpcClass.getCpcSubgroup().equals(patentCpcData.getCpcSubgroup())
                    && cPatentCpcClass.getCpcQualification().equals(patentCpcData.getCpcQualification())) {

                if (isHigherPosition == true) {
                    Collections.swap(cpcList, i, i + 1);
                    break;
                } else {
                    Collections.swap(cpcList, i, i - 1);
                    break;
                }
            }
        }

    }

}
