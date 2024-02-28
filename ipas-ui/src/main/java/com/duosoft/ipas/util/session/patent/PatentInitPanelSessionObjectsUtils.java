package com.duosoft.ipas.util.session.patent;

import bg.duosoft.ipas.core.model.file.CParisPriority;
import bg.duosoft.ipas.core.model.patent.*;
import com.duosoft.ipas.util.session.HttpSessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PatentInitPanelSessionObjectsUtils {

    public static void initRightsDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent) {
        List<CParisPriority> patentPriorities = null;
        if (patent.getFile().getPriorityData() != null && patent.getFile().getPriorityData().getParisPriorityList() != null) {
            patentPriorities = patent.getFile().getPriorityData().getParisPriorityList();
        } else {
            patentPriorities = new ArrayList<>();
        }
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_PRIORITIES, sessionIdentifier, patentPriorities, request);
    }

    public static void initIpcDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent) {
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_IPCS, sessionIdentifier, patent.getTechnicalData().getIpcClassList(), request);
    }

    public static void initCpcDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent) {
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_CPCS, sessionIdentifier, patent.getTechnicalData().getCpcClassList(), request);
    }

    public static void initPersonPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent) {
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_OWNERS, sessionIdentifier, patent.getFile().getOwnershipData(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_REPRESENTATIVES, sessionIdentifier, patent.getFile().getRepresentationData(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_SERVICE_PERSON, sessionIdentifier, patent.getFile().getServicePerson(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_INVENTORS, sessionIdentifier, patent.getAuthorshipData(), request);
    }


    public static void initPublishedDrawingsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent){
            List<CDrawing> cDrawingList=patent.getTechnicalData().getDrawingList();
            HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_DRAWINGS, sessionIdentifier, cDrawingList, request);
    }

    public static void initDesignDrawingsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier){
        List<CPatent> singleDesigns = HttpSessionUtils.getSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS, sessionIdentifier, request);
        List<CPatent> singleDesignsOnEdit = singleDesigns;
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_SINGLE_DESIGNS_ON_EDIT, sessionIdentifier, singleDesignsOnEdit, request);
    }

    public static void initClaimsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent){
        CTechnicalData cTechnicalData = patent.getTechnicalData();
        List<CClaim> cClaimList;

        if (Objects.isNull(cTechnicalData) || Objects.isNull(cTechnicalData.getClaimList())) {
            cClaimList = new ArrayList<>();
        } else {
            cClaimList = cTechnicalData.getClaimList();
        }

        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_CLAIMS, sessionIdentifier, cClaimList, request);
    }

    public static void initCitationsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent) {
        CTechnicalData cTechnicalData = patent.getTechnicalData();
        List<CPatentCitation> citationsList;

        if (Objects.isNull(cTechnicalData) || Objects.isNull(cTechnicalData.getCitationList())) {
            citationsList = new ArrayList<>();
        } else {
            citationsList = cTechnicalData.getCitationList();
        }

        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_CITATIONS, sessionIdentifier, citationsList, request);
    }

    public static void initMainDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CPatent patent) {
        List<CPatentAttachment> patentAttachments=new ArrayList<>();
        if (Objects.nonNull(patent.getPatentDetails())){
            patentAttachments=patent.getPatentDetails().getPatentAttachments();
        }
        HttpSessionUtils.setAndCloneSessionAttribute(PatentSessionObject.SESSION_PATENT_ATTACHMENTS, sessionIdentifier, patentAttachments, request);
    }

}
