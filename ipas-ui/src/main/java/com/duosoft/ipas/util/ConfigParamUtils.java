package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;

public class ConfigParamUtils {
    public static String getLastIpcVersion(ConfigParamService configParamService) {
        return configParamService.selectExtConfigByCode(configParamService.IPC_LATEST_VERSION).getValue();
    }

    public static String getLastCpcVersion(ConfigParamService configParamService) {
        return configParamService.selectExtConfigByCode(configParamService.CPC_LATEST_VERSION).getValue();
    }
}
