package com.duosoft.ipas.util.session.mark;

import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import com.duosoft.ipas.util.session.HttpSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class MarkSessionUtils {

    public static CMark getSessionMark(HttpServletRequest request, String sessionIdentifier) {
        CMark mark = HttpSessionUtils.getSessionAttribute(MarkSessionObjects.SESSION_MARK, sessionIdentifier, request);
        if (Objects.isNull(mark))
            throw new SessionObjectNotFoundException("Empty mark object ! ID: " + sessionIdentifier);
        return mark;
    }

    public static void removeAllMarkSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, MarkSessionObjects.getAllSessionObjectNames());
    }

    public static void removeMarkDetailPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                MarkSessionObjects.SESSION_MARK_ATTACHMENTS);
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                MarkSessionObjects.SESSION_MARK_USAGE_RULES);
    }

    public static void removeAcpAffectedObjectsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                MarkSessionObjects.SESSION_MARK_ACP_AFFECTED_OBJECTS);
    }


    public static void removeAcpCheckPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                MarkSessionObjects.SESSION_MARK_ACP_CHECK_DATA);
    }
    public static void removeAcpViolationPlacesPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                MarkSessionObjects.SESSION_MARK_ACP_VIOLATION_PLACES);
    }

    public static void removeAcpTakenItemsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                MarkSessionObjects.SESSION_MARK_ACP_TAKEN_ITEMS);
    }
}
