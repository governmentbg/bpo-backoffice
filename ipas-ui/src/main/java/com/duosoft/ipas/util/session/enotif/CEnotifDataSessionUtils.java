package com.duosoft.ipas.util.session.enotif;


import bg.duosoft.ipas.core.model.mark.CEnotifMark;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CEnotifDataSessionUtils {
    public static List<CEnotifMark> getEnotifsData(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getEnotifMarks();
            default:
                return null;
        }
    }
}
