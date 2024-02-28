package com.duosoft.ipas.util.session.userdoc;

import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import com.duosoft.ipas.util.session.HttpSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class UserdocSessionUtils {

    public static CUserdoc getSessionUserdoc(HttpServletRequest request, String sessionIdentifier) {
        CUserdoc userdoc = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC, sessionIdentifier, request);
        if (Objects.isNull(userdoc))
            throw new SessionObjectNotFoundException("Empty userdoc object ! ID: " + sessionIdentifier);
        return userdoc;
    }

    public static CProcessParentData getSessionUserdocParentData(HttpServletRequest request, String sessionIdentifier) {
        CProcessParentData processParentData = HttpSessionUtils.getSessionAttribute(UserdocSessionObjects.SESSION_USERDOC_PROCESS_PARENT_DATA, sessionIdentifier, request);
        if (Objects.isNull(processParentData))
            throw new SessionObjectNotFoundException("Empty processParentData object ! ID: " + sessionIdentifier);
        return processParentData;
    }

    public static void removeAllUserdocSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, UserdocSessionObjects.getAllSessionObjectNames());
    }

    public static void removeNiceClassesAndSingleDesignsSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, UserdocSessionObjects.SESSION_USERDOC_NICE_CLASSES);
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, UserdocSessionObjects.SESSION_USERDOC_SINGLE_DESIGNS);
    }

}
