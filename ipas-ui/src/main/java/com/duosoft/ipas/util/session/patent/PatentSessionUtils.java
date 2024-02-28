package com.duosoft.ipas.util.session.patent;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.exception.SessionObjectNotFoundException;
import com.duosoft.ipas.util.PatentLikeObjectsUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class PatentSessionUtils {

    public static CPatent getSessionPatent(HttpServletRequest request, String sessionIdentifier) {
        CPatent patent = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT, sessionIdentifier,request);
        if (Objects.isNull(patent))
            throw new SessionObjectNotFoundException("Empty patent object ! ID: " + sessionIdentifier);
        return patent;
    }

    public static void setSessionAttributePatentHash(HttpServletRequest request, String sessionIdentifier){
        PatentSessionUtils.removePatentHashSessionObject(request,sessionIdentifier);
        int sessionPatentHash = PatentLikeObjectsUtils.generateSessionPatentHash(request,sessionIdentifier);
        HttpSessionUtils.setSessionAttribute(PatentSessionObject.SESSION_PATENT_HASH, sessionIdentifier, sessionPatentHash, request);
    }

    public static void removeAllPatentSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request,sessionIdentifier,PatentSessionObject.getAllSessionObjectNames());
    }

    public static void removePatentDetailPanelSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                PatentSessionObject.SESSION_PATENT_ATTACHMENTS,PatentSessionObject.SESSION_PATENT_NEW_ATTACHMENT);
    }

    public static void removePatentIpcSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                PatentSessionObject.SESSION_PATENT_IPCS);
    }

    public static void removePatentCpcSessionObjects(HttpServletRequest request, String sessionIdentifier) {
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                PatentSessionObject.SESSION_PATENT_CPCS);
    }

    public static void removePatentDrawingsSessionObjects(HttpServletRequest request, String sessionIdentifier){
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                PatentSessionObject.SESSION_PATENT_NEW_DRAWING, PatentSessionObject.SESSION_PATENT_DRAWINGS);
    }
    public static void removeDesignDrawingsSessionObjects(HttpServletRequest request, String sessionIdentifier){
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT);
    }

    public static void removePatentHashSessionObject(HttpServletRequest request, String sessionIdentifier){
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier, PatentSessionObject.SESSION_PATENT_HASH);
    }

    public static void removePatentClaimsSessionObjects(HttpServletRequest request, String sessionIdentifier){
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                PatentSessionObject.SESSION_PATENT_CLAIMS);
    }

    public static void removePatentCitationsSessionObjects(HttpServletRequest request, String sessionIdentifier){
        HttpSessionUtils.removeSessionAttributes(request, sessionIdentifier,
                PatentSessionObject.SESSION_PATENT_CITATIONS);
    }
}
