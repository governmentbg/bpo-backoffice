package com.duosoft.ipas.util.session.file;

import bg.duosoft.ipas.core.model.file.CFile;
import com.duosoft.ipas.enums.SessionObjectType;
import com.duosoft.ipas.util.session.SessionObjectUtils;
import com.duosoft.ipas.util.session.mark.MarkSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;

import javax.servlet.http.HttpServletRequest;

public class CFileSessionUtils {

    public static CFile getSessionFile(HttpServletRequest request, String sessionIdentifier) {
        SessionObjectType sessionObjectType = SessionObjectUtils.getSessionObjectType(sessionIdentifier, request);
        switch (sessionObjectType) {
            case MARK:
                return MarkSessionUtils.getSessionMark(request, sessionIdentifier).getFile();
            case PATENT:
                return PatentSessionUtils.getSessionPatent(request, sessionIdentifier).getFile();
            default:
                throw new RuntimeException("Cannot find CFile for object with identifier " + sessionIdentifier);
        }
    }
}
