package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.util.security.SecurityUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import com.duosoft.ipas.util.session.patent.PatentSessionObject;
import com.duosoft.ipas.util.session.patent.PatentSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class PatentLikeObjectsUtils {

    public static boolean isSessionPatentEdited(HttpServletRequest request, String sessionIdentifier){
        int sessionPatentHash = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT_HASH, sessionIdentifier, request);
        CPatent sessionPatent = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT, sessionIdentifier, request);
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);

        if (Objects.isNull(sessionPatent)){
            throw new RuntimeException("Session patent is null ! Cannot make comparison !");
        }
        if (Objects.isNull(singleDesigns)){
            return sessionPatent.hashCode() != sessionPatentHash;
        }else{
            return Objects.hash(sessionPatent,singleDesigns) != sessionPatentHash;
        }
    }

    public static int generateSessionPatentHash(HttpServletRequest request, String sessionIdentifier){
        CPatent sessionPatent = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_PATENT, sessionIdentifier, request);
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        if (Objects.isNull(sessionPatent)){
            throw new RuntimeException("Cannot generate hash number because session patent is null!");
        }
        if (Objects.isNull(singleDesigns)){
            return sessionPatent.hashCode();
        }
        else{
            return Objects.hash(sessionPatent,singleDesigns);
        }
    }
}
