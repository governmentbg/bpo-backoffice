package com.duosoft.ipas.util.session.efiling;

import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;

import javax.servlet.http.HttpServletRequest;

public class CEFilingDataSessionUtils {

    public static CEFilingData getSessionEFilingData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getMarkEFilingData();
            case PATENT:
                return PatentSessionUtils.getSessionPatent(request, sessionIdentifier).getPatentEFilingData();
            case USERDOC:
                return UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier).getUserdocEFilingData();
            default:
                return null;
        }
    }

}
