package com.duosoft.ipas.controller.ipobjects.userdoc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.mark.CProtectionData;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataType;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.core.service.patent.DesignService;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.util.json.JsonUtil;
import bg.duosoft.ipas.util.userdoc.UserdocExtraDataUtils;
import bg.duosoft.ipas.util.userdoc.UserdocUtils;
import com.duosoft.ipas.controller.ipobjects.common.nice_class.NiceClassController;
import com.duosoft.ipas.controller.ipobjects.userdoc.single_design.UserdocSingleDesignController;
import com.duosoft.ipas.enums.UserdocPanel;
import com.duosoft.ipas.util.json.UserdocExtraData;
import com.duosoft.ipas.util.json.UserdocPanelData;
import com.duosoft.ipas.util.session.userdoc.UserdocSessionUtils;
import com.duosoft.ipas.webmodel.NiceListType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class ExtraDataBaseController {

    @Autowired
    protected MarkService markService;

    @Autowired
    private DesignService designService;

    @Autowired
    private PatentService patentService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    public abstract String getPanelPage();

    @PostMapping("/edit-panel")
    public String editPanel(HttpServletRequest request, Model model, @RequestParam Boolean isCancel, @RequestParam(required = false) String data, @RequestParam String sessionIdentifier) {
        CUserdoc sessionUserdoc = UserdocSessionUtils.getSessionUserdoc(request, sessionIdentifier);
        processExtraData(isCancel, data, sessionUserdoc);
        model.addAttribute("userdoc", sessionUserdoc);
        return getPanelPage();
    }

    private void processExtraData(Boolean isCancel, String data, CUserdoc sessionUserdoc) {
        if (!isCancel) {
            updateUserdocExtraData(data, sessionUserdoc);
        }
    }

    protected void processExtraDataNiceClassesOrDesigns(HttpServletRequest request, Boolean isCancel, String data, String sessionIdentifier, CUserdoc sessionUserdoc) {
        if (!isCancel) {
            updateUserdocExtraData(data, sessionUserdoc);
            UserdocExtraData formData = JsonUtil.readJson(data, UserdocExtraData.class);
            updateUserdocNiceClassesOrDesigns(request, sessionIdentifier, sessionUserdoc, formData);
        }
    }

    private void updateUserdocNiceClassesOrDesigns(HttpServletRequest request, String sessionIdentifier, CUserdoc sessionUserdoc, UserdocExtraData formData) {
        CProcessParentData userdocParentData = sessionUserdoc.getUserdocParentData();
        CFileId fileId = UserdocUtils.selectUserdocMainObject(userdocParentData);
        if (UserdocUtils.isUserdocMainObjectMark(userdocParentData)) {
            setNiceClassesToSessionUserdoc(sessionUserdoc, request, sessionIdentifier, formData.getServiceScope(), fileId);
        } else if (UserdocUtils.isUserdocMainObjectDesign(userdocParentData)) {
            setSingleDesignsToSessionUserdoc(sessionUserdoc, request, sessionIdentifier, formData.getServiceScope(), fileId);
        }
    }

    private void setNiceClassesToSessionUserdoc(CUserdoc userdoc, HttpServletRequest request, String sessionIdentifier, Boolean isAllNiceClassesIncluded, CFileId markId) {
        if (userdoc.getProtectionData() == null) {
            userdoc.setProtectionData(new CProtectionData());
        }
        List<CNiceClass> niceClassList = NiceClassController.selectOriginalOrSessionNiceClasses(request, sessionIdentifier, isAllNiceClassesIncluded, markService, markId, NiceListType.USERDOC_REQUESTED);
        userdoc.getProtectionData().setNiceClassList(niceClassList);
    }

    private void setSingleDesignsToSessionUserdoc(CUserdoc userdoc, HttpServletRequest request, String sessionIdentifier, Boolean isAllIncluded, CFileId fileId) {
        List<CUserdocSingleDesign> singleDesignsList = UserdocSingleDesignController.selectOriginalOrSessionSingleDesigns(request, sessionIdentifier, isAllIncluded, fileId, designService, patentService, applicationTypeService);
        userdoc.setSingleDesigns(singleDesignsList);
    }

    protected void updateUserdocExtraData(String json, CUserdoc sessionUserdoc) {
        UserdocExtraData formData = JsonUtil.readJson(json, UserdocExtraData.class);
        UserdocPanelData panelData = JsonUtil.readJson(json, UserdocPanelData.class);
        UserdocPanel panel = UserdocPanel.selectByCode(panelData.getUserdocPanelName());

        Map<CUserdocExtraDataType, CUserdocExtraData> extraDataMap = sessionUserdoc.getUserdocExtraData().stream().collect(Collectors.toMap(CUserdocExtraData::getType, c -> c));
        List<CUserdocExtraDataType> panelTypes = UserdocExtraDataUtils.selectTypes(sessionUserdoc, panel.name());
        if (Objects.nonNull(panelTypes)) {
            if (Objects.nonNull(formData)) {
                Field[] fields = formData.getClass().getDeclaredFields();
                for (Field field : fields) {
                    JsonProperty annotation = field.getAnnotation(JsonProperty.class);
                    if (Objects.nonNull(annotation)) {
                        CUserdocExtraDataType type = panelTypes.stream().filter(t -> t.getCode().equalsIgnoreCase(annotation.value())).findFirst().orElse(null);
                        if (Objects.nonNull(type)) {
                            CUserdocExtraData data = new CUserdocExtraData();
                            data.setType(type);
                            data.setDocumentId(sessionUserdoc.getDocumentId());
                            try {
                                field.setAccessible(true);
                                if (type.getIsText()) {
                                    data.setTextValue((String) field.get(formData));
                                } else if (type.getIsDate()) {
                                    data.setDateValue((Date) field.get(formData));
                                } else if (type.getIsNumber()) {
                                    data.setNumberValue((Integer) field.get(formData));
                                } else if (type.getIsBoolean()) {
                                    data.setBooleanValue((Boolean) field.get(formData));
                                } else {
                                    throw new RuntimeException("Invalid data value type! " + type.toString());
                                }

                                extraDataMap.put(type, data);

                            } catch (IllegalAccessException e) {
                                log.error(e.getMessage(), e);
                                throw new RuntimeException(e.getMessage(), e);
                            }
                        }
                    }
                }
                sessionUserdoc.setUserdocExtraData(extraDataMap.values()
                        .stream()
                        .filter(data -> Objects.nonNull(data.getTextValue()) || Objects.nonNull(data.getDateValue()) || Objects.nonNull(data.getNumberValue()) || Objects.nonNull(data.getBooleanValue()))
                        .collect(Collectors.toList()));
            }
        }
    }

}
