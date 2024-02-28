package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.nomenclature.ApplicationTypeService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.util.session.HttpSessionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicationTypeUtils {



    public static Map<String, String> getApplicationTypeMap(HttpServletRequest request, ApplicationTypeService applicationTypeService, String sessionIdentifier) {
        String fullSessionIdentifier = HttpSessionUtils.findMainObjectFullSessionIdentifier(request, sessionIdentifier);
        String filingNumber = HttpSessionUtils.getFilingNumberFromSessionObject(fullSessionIdentifier);
        if (StringUtils.isEmpty(filingNumber))
            throw new RuntimeException("Filing number is empty for identifier: " + sessionIdentifier);

        return getApplicationTypesByFilingNumber(applicationTypeService, filingNumber);
    }

    public static Map<String, String> getApplicationTypeMap(ApplicationTypeService applicationTypeService, CFile cFile) {
        String filingNumber = CoreUtils.createFilingNumber(cFile.getFileId(), false);
        if (StringUtils.isEmpty(filingNumber))
            throw new RuntimeException("Filing number is empty !");

        return getApplicationTypesByFilingNumber(applicationTypeService, filingNumber);
    }

    //TODO Other objects
    private static Map<String, String> getApplicationTypesByFilingNumber(ApplicationTypeService applicationTypeService, String filingNumber) {
        CFileId id = BasicUtils.createCFileId(filingNumber);
        if (Objects.isNull(id))
            throw new RuntimeException("Cannot create file id from string " + filingNumber);

        String objectIndication = null;
        List<String> fileTypes = new ArrayList<>();

        FileType fileType = FileType.selectByCode(id.getFileType());
        switch (fileType) {
            case INTERNATIONAL_MARK_I :
            case INTERNATIONAL_MARK_R :
            case INTERNATIONAL_MARK_B :
                fileTypes.add(FileType.INTERNATIONAL_MARK_I.code());
                fileTypes.add(FileType.INTERNATIONAL_MARK_R.code());
                fileTypes.add(FileType.INTERNATIONAL_MARK_B.code());
                objectIndication = DefaultValue.MARK_OBJECT_INDICATION;
                break;
            case MARK:
            case DIVISIONAL_MARK:
                fileTypes.add(FileType.MARK.code());
                fileTypes.add(FileType.DIVISIONAL_MARK.code());
                objectIndication = DefaultValue.MARK_OBJECT_INDICATION;
                break;
            case GEOGRAPHICAL_INDICATIONS:
                fileTypes.add(FileType.GEOGRAPHICAL_INDICATIONS.code());
                objectIndication = DefaultValue.MARK_OBJECT_INDICATION;
                break;
            case ACP:
                fileTypes.add(FileType.ACP.code());
                objectIndication = DefaultValue.MARK_OBJECT_INDICATION;
                break;
            case PATENT:
                fileTypes.add(FileType.PATENT.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
            case EU_PATENT:
                fileTypes.add(FileType.EU_PATENT.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
            case DESIGN:
                fileTypes.add(FileType.DESIGN.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
            case INTERNATIONAL_DESIGN:
                fileTypes.add(FileType.INTERNATIONAL_DESIGN.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
            case UTILITY_MODEL:
                fileTypes.add(FileType.UTILITY_MODEL.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
            case PLANTS_AND_BREEDS:
                fileTypes.add(FileType.PLANTS_AND_BREEDS.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
            case SPC:
                fileTypes.add(FileType.SPC.code());
                objectIndication = DefaultValue.PATENT_OBJECT_INDICATION;
                break;
        }

        if (Objects.isNull(objectIndication))
            throw new RuntimeException("Object indication for application types is empty ! Session identifier: " + filingNumber);
        if (CollectionUtils.isEmpty(fileTypes))
            throw new RuntimeException("File type list is empty for application types ! Session identifeir: " + filingNumber);

        return applicationTypeService.getApplicationTypesMap(objectIndication, fileTypes);
    }

}
