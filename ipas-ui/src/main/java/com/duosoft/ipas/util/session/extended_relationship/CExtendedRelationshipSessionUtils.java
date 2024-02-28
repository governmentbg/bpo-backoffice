package com.duosoft.ipas.util.session.extended_relationship;

import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;

import javax.servlet.http.HttpServletRequest;

public class CExtendedRelationshipSessionUtils {

    public static CRelationshipExtended getSessionRelationshipExtended(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getRelationshipExtended();
            case PATENT:
                return PatentSessionUtils.getSessionPatent(request, sessionIdentifier).getRelationshipExtended();
            default:
                return null;
        }
    }
}
