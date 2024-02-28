package com.duosoft.ipas.webmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by Raya
 * 05.09.2020
 */
@Getter
@Setter
public class NiceConfig {

    private NiceListType niceListType;
    private boolean hasAddition;
    private boolean hasDeclaration;
    private boolean hasFetch;
    private boolean hasReloadOriginal;
    private boolean hasReloadRequested;
    private boolean hasShowHide;
    private boolean hasDeleteAll;
    private boolean hasViewAll;
    private boolean hasEdit;
    private boolean hasTranslate;

    public boolean allowEdit(Boolean isAllNiceClassesIncluded) {
        if (!hasEdit && Objects.nonNull(isAllNiceClassesIncluded) && isAllNiceClassesIncluded) {
            return false;
        }

        return true;
    }
}
