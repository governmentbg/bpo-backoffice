package com.duosoft.ipas.util;

import com.duosoft.ipas.webmodel.ReceptionForm;
import com.duosoft.ipas.webmodel.ReceptionMarkForm;

import java.util.Objects;

public class FigurativeMarkUtils {

    public static boolean isFigurativeMark(ReceptionForm receptionForm) {
        if (ReceptionTypeUtils.isMark(receptionForm)) {
            ReceptionMarkForm mark = receptionForm.getMark();
            if (Objects.isNull(mark))
                return false;

            Boolean figurativeMark = mark.getFigurativeMark();
            return Objects.nonNull(figurativeMark) && figurativeMark;
        } else {
            return false;
        }
    }

}
