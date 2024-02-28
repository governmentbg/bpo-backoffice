package com.duosoft.ipas.util.session.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import com.duosoft.ipas.util.session.HttpSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class OffidocSessionUtils {

    public static COffidoc getSessionOffidoc(HttpServletRequest request, String sessionIdentifier) {
        COffidoc offidoc = HttpSessionUtils.getSessionAttribute(OffidocSessionObjects.SESSION_OFFIDOC, sessionIdentifier, request);
        if (Objects.isNull(offidoc))
            throw new SessionObjectNotFoundException("Empty offidoc object ! ID: " + sessionIdentifier);
        return offidoc;
    }

    public static void removeAllOffidocSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, OffidocSessionObjects.getAllSessionObjectNames());
    }

}
