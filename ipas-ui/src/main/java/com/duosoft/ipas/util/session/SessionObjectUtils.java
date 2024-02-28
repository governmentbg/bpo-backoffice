package com.duosoft.ipas.util.session;

import bg.duosoft.ipas.util.offidoc.OffidocUtils;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.mark.MarkSessionObjects;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionObjects;
import com.duosoft.ipas.util.session.offidoc.OffidocSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;
import com.duosoft.ipas.util.session.reception.ReceptionSessionUtils;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionObjects;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class SessionObjectUtils {

    public static SessionObjectType getSessionObjectType(String sessionIdentifier, HttpServletRequest request) {
        if (isSessionObjectReception(sessionIdentifier))
            return SessionObjectType.RECEPTION;

        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        if (isSessionObjectMark(fullSessionIdentifier))
            return SessionObjectType.MARK;
        else if (isSessionObjectPatent(fullSessionIdentifier))
            return SessionObjectType.PATENT;
        else if (isSessionObjectUserdoc(fullSessionIdentifier))
            return SessionObjectType.USERDOC;
        else if (isSessionObjectOffidoc(fullSessionIdentifier))
            return SessionObjectType.OFFIDOC;

        throw new RuntimeException("Unknown session object type! Session identifier: " + sessionIdentifier);
    }

    public static boolean isSessionObjectMark(String fullSessionIdentifier) {
        return fullSessionIdentifier.startsWith(MarkSessionObjects.SESSION_MARK);
    }

    public static boolean isSessionObjectPatent(String fullSessionIdentifier) {
        return fullSessionIdentifier.startsWith(PatentSessionObject.SESSION_PATENT);
    }

    public static boolean isSessionObjectUserdoc(String fullSessionIdentifier) {
        return fullSessionIdentifier.startsWith(UserdocSessionObjects.SESSION_USERDOC);
    }

    public static boolean isSessionObjectOffidoc(String fullSessionIdentifier) {
        return fullSessionIdentifier.startsWith(OffidocSessionObjects.SESSION_OFFIDOC);
    }

    public static boolean isSessionObjectReception(String fullSessionIdentifier) {
        return fullSessionIdentifier.startsWith(ReceptionSessionUtils.SESSION_RECEPTION_FORM);
    }

    public static String getIpObjectPrefixByFullSessionIdentifier(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return "mark";
            case PATENT:
                return "patent";
            case USERDOC:
                return "userdoc";
            case OFFIDOC:
                return "offidoc";
            default:
                throw new RuntimeException("Intellectual property prefix cannot be found for session identifier " + sessionIdentifier);
        }
    }

    public static boolean hasFreeSpaceForSessionObject(HttpServletRequest request, int maxCount) {
        int count = HttpSessionUtils.getMainSessionObjectsCount(request);
        return count < maxCount;
    }

    public static void removeAllSessionObjectsRelatedToIpObject(HttpServletRequest request, String filingNumber) {
        if (!StringUtils.isEmpty(filingNumber)) {
            String fullSessionIdentifier = HttpSessionUtils.findExistingMainSessionObjectIdentifier(request, filingNumber);
            if (!StringUtils.isEmpty(fullSessionIdentifier)) {
                String sessionIdentifier = HttpSessionUtils.getFilingNumberAndUniqueIdentifierFromSessionObject(fullSessionIdentifier);
                SessionObjectType sessionObjectType = getSessionObjectType(sessionIdentifier, request);
                switch (sessionObjectType) {
                    case MARK:
                        MarkSessionUtils.removeAllMarkSessionObjects(request, sessionIdentifier);
                        break;
                    case PATENT:
                        PatentSessionUtils.removeAllPatentSessionObjects(request, sessionIdentifier);
                        break;
                    case OFFIDOC:
                        OffidocSessionUtils.removeAllOffidocSessionObjects(request, sessionIdentifier);
                        break;
                    case USERDOC:
                        UserdocSessionUtils.removeAllUserdocSessionObjects(request, sessionIdentifier);
                        break;
                }
            }
        }
    }

}
