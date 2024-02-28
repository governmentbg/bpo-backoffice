package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import com.duosoft.ipas.util.json.PatentIpcData;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PatentIpcsUtils {


    public static void sortIpcList(List<CPatentIpcClass> cPatentIpcClasses) {
        long orderedIpcQualif = 1;
        for (int i = 0; i < cPatentIpcClasses.size(); i++) {
            cPatentIpcClasses.get(i).setIpcQualification(String.valueOf(orderedIpcQualif));
            orderedIpcQualif++;
        }
    }

    public static boolean ipcSwapPositionsValidation(CPatentIpcClass obj, CPatentIpcClass obj1) {

        return !(Objects.equals(obj.getIpcQualification(), obj1.getIpcQualification()) && Objects.equals(obj.getIpcEdition(), obj1.getIpcEdition())
                && Objects.equals(obj.getIpcSection(), obj1.getIpcSection()) && Objects.equals(obj.getIpcClass(), obj1.getIpcClass())
                && Objects.equals(obj.getIpcSubclass(), obj1.getIpcSubclass()) && Objects.equals(obj.getIpcGroup(), obj1.getIpcGroup())
                && Objects.equals(obj.getIpcSubgroup(), obj1.getIpcSubgroup()));
    }


    public static void swapIpcPosition(List<CPatentIpcClass> ipcList, PatentIpcData patentIpcData, boolean isHigherPosition) {
        for (int i = 0; i < ipcList.size(); i++) {
            CPatentIpcClass cPatentIpcClass = ipcList.get(i);
            if (cPatentIpcClass.getIpcEdition().equals(patentIpcData.getIpcEdition()) && cPatentIpcClass.getIpcSection().equals(patentIpcData.getIpcSection())
                    && cPatentIpcClass.getIpcClass().equals(patentIpcData.getIpcClass()) && cPatentIpcClass.getIpcSubclass().equals(patentIpcData.getIpcSubclass())
                    && cPatentIpcClass.getIpcGroup().equals(patentIpcData.getIpcGroup()) && cPatentIpcClass.getIpcSubgroup().equals(patentIpcData.getIpcSubgroup())
                    && cPatentIpcClass.getIpcQualification().equals(patentIpcData.getIpcQualification())) {

                if (isHigherPosition == true) {
                    Collections.swap(ipcList, i, i + 1);
                    break;
                } else {
                    Collections.swap(ipcList, i, i - 1);
                    break;
                }
            }
        }

    }

}
