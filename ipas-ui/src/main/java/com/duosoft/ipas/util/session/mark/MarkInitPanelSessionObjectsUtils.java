package com.duosoft.ipas.util.session.mark;

import bg.duosoft.ipas.core.model.acp.CAcpAffectedObject;
import bg.duosoft.ipas.core.model.acp.CAcpCheckReason;
import bg.duosoft.ipas.core.model.acp.CAcpTakenItem;
import bg.duosoft.ipas.core.model.acp.CAcpViolationPlace;
import bg.duosoft.ipas.core.model.file.CParisPriority;
import bg.duosoft.ipas.core.model.mark.*;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarkInitPanelSessionObjectsUtils {

    public static void initClaimsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CParisPriority> markPriorities;
        if (mark.getFile().getPriorityData() != null && mark.getFile().getPriorityData().getParisPriorityList() != null)
            markPriorities = mark.getFile().getPriorityData().getParisPriorityList();
        else
            markPriorities = new ArrayList<>();

        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_PRIORITIES, sessionIdentifier, markPriorities, request);
    }

    public static void initNiceClassesPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CNiceClass> niceClasses;
        if (mark.getProtectionData() != null && mark.getProtectionData().getNiceClassList() != null)
            niceClasses = mark.getProtectionData().getNiceClassList();
        else
            niceClasses = new ArrayList<>();

        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_NICE_CLASSES, sessionIdentifier, niceClasses, request);
    }


    public static void initAcpCheckPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CAcpCheckReason> acpCheckReasons = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mark.getAcpCheckReasons())) {
            acpCheckReasons = mark.getAcpCheckReasons();
        }
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_CHECK_DATA, sessionIdentifier, acpCheckReasons, request);
    }


    public static void initAcpTakenItemsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CAcpTakenItem> items = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mark.getAcpTakenItems())) {
            items = mark.getAcpTakenItems();
        }
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_TAKEN_ITEMS, sessionIdentifier, items, request);
    }

    public static void initAcpViolationPlacesPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CAcpViolationPlace> violationPlaces = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mark.getAcpViolationPlaces())) {
            violationPlaces = mark.getAcpViolationPlaces();
        }
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_VIOLATION_PLACES, sessionIdentifier, violationPlaces, request);
    }

    public static void initAcpAffectedObjectsPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CAcpAffectedObject> acpAffectedObjects = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mark.getAcpAffectedObjects())) {
            acpAffectedObjects = mark.getAcpAffectedObjects();
        }
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_ACP_AFFECTED_OBJECTS, sessionIdentifier, acpAffectedObjects, request);
    }

    public static void initMainDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CMarkAttachment> markAttachments = new ArrayList<>();
        CSignData signData = mark.getSignData();
        if (Objects.nonNull(signData) && Objects.nonNull(signData.getAttachments())) {
            markAttachments = signData.getAttachments();
        }

        List<CMarkUsageRule> usageRules = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mark.getUsageRules())) {
            usageRules.addAll(mark.getUsageRules());
        }

        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_USAGE_RULES, sessionIdentifier, usageRules, request);
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_ATTACHMENTS, sessionIdentifier, markAttachments, request);
    }

    public static void initPersonPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_OWNERS, sessionIdentifier, mark.getFile().getOwnershipData(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_REPRESENTATIVES, sessionIdentifier, mark.getFile().getRepresentationData(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_SERVICE_PERSON, sessionIdentifier, mark.getFile().getServicePerson(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_ACP_SERVICE_PERSON, sessionIdentifier, mark.getFile().getAcpPersonsData().getServicePerson(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_ACP_INFRINGER, sessionIdentifier, mark.getFile().getAcpPersonsData().getInfringerPerson(), request);
        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_ACP_REPRESENTATIVES, sessionIdentifier, mark.getFile().getAcpPersonsData().getRepresentationData(), request);
    }

    public static void initInternationalDataPanelSessionObjects(HttpServletRequest request, String sessionIdentifier, CMark mark) {
        List<CNiceClass> niceClasses;
        if (mark.getMarkInternationalReplacement() != null && mark.getMarkInternationalReplacement().getReplacementNiceClasses() != null)
            niceClasses = mark.getMarkInternationalReplacement().getReplacementNiceClasses();
        else
            niceClasses = new ArrayList<>();

        HttpSessionUtils.setAndCloneSessionAttribute(MarkSessionObjects.SESSION_MARK_INTL_REPLACEMENT_NICE_CLASSES, sessionIdentifier, niceClasses, request);
    }
}
