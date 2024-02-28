package com.duosoft.ipas.util.factory;

import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.UserdocType;
import com.duosoft.ipas.webmodel.NiceConfig;
import com.duosoft.ipas.webmodel.NiceListType;

/**
 * Created by Raya
 * 05.09.2020
 */
public class NiceConfigFactory {

    private static NiceConfig objectNiceConfig = buildObjectNiceConfig();
    private static NiceConfig irMarkNiceConfig = buildIRMarkNiceConfig();
    private static NiceConfig objectIntlReplacementConfig = buildObjectIntlReplacementNiceConfig();
    private static NiceConfig userdocRequestedNiceConfig = buildUserdocRequestedNiceConfig();
    private static NiceConfig userdocZMRRequestedNiceConfig = buildUserdocZMRRequestedNiceConfig();
    private static NiceConfig userdocApprovedNiceConfig = buildUserdocApprovedNiceConfig();

    public static NiceConfig get(NiceListType type, String fileType, String userdocType){
        switch(type){
            case OBJECT_LIST:
                if(fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_I.code()) || fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_R.code()) || fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_B.code())){
                    return irMarkNiceConfig;
                } else {
                    return objectNiceConfig;
                }
            case OBJECT_INTL_REPLACEMENT_LIST: return objectIntlReplacementConfig;
            case USERDOC_REQUESTED: return userdocType.equals(UserdocType.MARK_INTERNATIONAL_REGISTRATION_REQUEST) ?  userdocZMRRequestedNiceConfig : userdocRequestedNiceConfig;
            case USERDOC_APPROVED: return userdocApprovedNiceConfig;
        }
        throw new RuntimeException("No NiceConfig can be returned");
    }

    public static NiceConfig get(String typeStr, String fileType, String userdocType){
        NiceListType type = NiceListType.valueOf(typeStr);
        return get(type, fileType, userdocType);
    }

    private static NiceConfig buildObjectNiceConfig(){
        NiceConfig niceConfig = new NiceConfig();
        niceConfig.setNiceListType(NiceListType.OBJECT_LIST);
        niceConfig.setHasFetch(true);
        niceConfig.setHasDeclaration(true);
        niceConfig.setHasAddition(true);
        niceConfig.setHasReloadOriginal(false);
        niceConfig.setHasReloadRequested(false);
        niceConfig.setHasShowHide(false);
        niceConfig.setHasDeleteAll(false);
        niceConfig.setHasViewAll(false);
        niceConfig.setHasEdit(true);
        return niceConfig;
    }

    private static NiceConfig buildIRMarkNiceConfig(){
        NiceConfig niceConfig = new NiceConfig();
        niceConfig.setNiceListType(NiceListType.OBJECT_LIST);
        niceConfig.setHasFetch(true);
        niceConfig.setHasDeclaration(false);
        niceConfig.setHasAddition(true);
        niceConfig.setHasReloadOriginal(false);
        niceConfig.setHasReloadRequested(false);
        niceConfig.setHasShowHide(false);
        niceConfig.setHasDeleteAll(false);
        niceConfig.setHasViewAll(false);
        niceConfig.setHasEdit(true);
        return niceConfig;
    }

    private static NiceConfig buildObjectIntlReplacementNiceConfig(){
        NiceConfig niceConfig = new NiceConfig();
        niceConfig.setNiceListType(NiceListType.OBJECT_INTL_REPLACEMENT_LIST);
        niceConfig.setHasFetch(false);
        niceConfig.setHasDeclaration(false);
        niceConfig.setHasAddition(false);
        niceConfig.setHasReloadOriginal(true);
        niceConfig.setHasReloadRequested(false);
        niceConfig.setHasShowHide(true);
        niceConfig.setHasDeleteAll(true);
        niceConfig.setHasViewAll(false);
        niceConfig.setHasEdit(true);
        return niceConfig;
    }

    private static NiceConfig buildUserdocRequestedNiceConfig(){
        NiceConfig niceConfig = new NiceConfig();
        niceConfig.setNiceListType(NiceListType.USERDOC_REQUESTED);
        niceConfig.setHasFetch(false);
        niceConfig.setHasDeclaration(false);
        niceConfig.setHasAddition(false);
        niceConfig.setHasReloadOriginal(true);
        niceConfig.setHasReloadRequested(false);
        niceConfig.setHasShowHide(true);
        niceConfig.setHasDeleteAll(true);
        niceConfig.setHasViewAll(false);
        niceConfig.setHasEdit(true);
        return niceConfig;
    }

    public static NiceConfig buildUserdocZMRRequestedNiceConfig(){
        NiceConfig niceConfig = new NiceConfig();
        niceConfig.setNiceListType(NiceListType.USERDOC_REQUESTED);
        niceConfig.setHasFetch(false);
        niceConfig.setHasDeclaration(false);
        niceConfig.setHasAddition(false);
        niceConfig.setHasReloadOriginal(true);
        niceConfig.setHasReloadRequested(false);
        niceConfig.setHasShowHide(true);
        niceConfig.setHasDeleteAll(true);
        niceConfig.setHasViewAll(true);
        niceConfig.setHasEdit(false);
        niceConfig.setHasTranslate(true);
        return niceConfig;
    }

    private static NiceConfig buildUserdocApprovedNiceConfig(){
        NiceConfig niceConfig = new NiceConfig();
        niceConfig.setNiceListType(NiceListType.USERDOC_APPROVED);
        niceConfig.setHasFetch(false);
        niceConfig.setHasDeclaration(false);
        niceConfig.setHasAddition(false);
        niceConfig.setHasReloadOriginal(true);
        niceConfig.setHasReloadRequested(true);
        niceConfig.setHasShowHide(true);
        niceConfig.setHasDeleteAll(true);
        niceConfig.setHasViewAll(false);
        niceConfig.setHasEdit(true);
        return niceConfig;
    }
}
