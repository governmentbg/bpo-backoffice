package com.duosoft.ipas.util.factory;

import bg.duosoft.ipas.core.model.util.LocalizationProperties;
import bg.duosoft.ipas.enums.FileType;

import static bg.duosoft.ipas.core.model.util.LocalizationProperties.ENGLISH_LANG;
import static bg.duosoft.ipas.core.model.util.LocalizationProperties.BULGARIAN_LANG;
import static bg.duosoft.ipas.core.model.util.LocalizationProperties.BULGARIAN_OFFICE;

/**
 * Created by Raya
 * 01.09.2020
 */
public class LocalizationPropertiesFactory {

    private static LocalizationProperties bulgarianForBGOffice = new LocalizationProperties(BULGARIAN_OFFICE, BULGARIAN_LANG);
    private static LocalizationProperties englishForBGOffice = new LocalizationProperties(BULGARIAN_OFFICE, ENGLISH_LANG);

    public static LocalizationProperties get(String fileType){
        if(fileType != null) {
            if (fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_I.code())
                    || fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_R.code())
                    || fileType.equalsIgnoreCase(FileType.INTERNATIONAL_MARK_B.code())) {
                return englishForBGOffice;
            } else{
                return bulgarianForBGOffice;
            }
        }

        throw new RuntimeException("Can not determine localization properties for null fileType");
    }
}
