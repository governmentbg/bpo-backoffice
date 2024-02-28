package com.duosoft.ipas.util;

import bg.duosoft.ipas.enums.ApplSubTyp;

public class MarkUsageRulesUtils {

    public static boolean showUsageRules(String applicationSubType){
       return  applicationSubType.equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_COLLECTIVE)
                || applicationSubType.equals(ApplSubTyp.NATIONAL_MARK_SUB_TYPE_CERTIFIED)
                || applicationSubType.equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_COLLECTIVE)
                || applicationSubType.equals(ApplSubTyp.DIVIDED_NATIONAL_MARK_SUB_TYPE_CERTIFIED);
    }
}
