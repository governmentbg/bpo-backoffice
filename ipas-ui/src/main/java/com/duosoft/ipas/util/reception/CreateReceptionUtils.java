package com.duosoft.ipas.util.reception;

import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CUserdocReceptionRelation;
import bg.duosoft.ipas.core.service.doc.DocService;
import bg.duosoft.ipas.core.service.file.FileService;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeGroupService;
import bg.duosoft.ipas.core.service.reception.ReceptionTypeService;
import bg.duosoft.ipas.core.service.reception.UserdocReceptionRelationService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.general.BasicUtils;
import com.duosoft.ipas.util.CoreUtils;
import com.duosoft.ipas.webmodel.ReceptionForm;
import com.duosoft.ipas.webmodel.RelatedUserdocObjectDetails;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateReceptionUtils {

    public static List<CUserdocReceptionRelation> getUserdocTypes(ReceptionForm receptionForm, UserdocService userdocService, ReceptionTypeService receptionTypeService, UserdocReceptionRelationService userdocReceptionRelationService) {
        String objectNumber = receptionForm.getUserdoc().getObjectNumber();
        if (StringUtils.isEmpty(objectNumber))
            return null;

        String type = objectNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR)[1];
        if (FileType.USERDOC.code().equalsIgnoreCase(type)) {
            CDocumentId cDocumentId = BasicUtils.createCDocumentId(objectNumber);
            String userdocType = userdocService.selectUserdocTypeByDocId(cDocumentId);
            return userdocReceptionRelationService.selectUserdocsByMainType(userdocType, true);
        } else {
            return userdocReceptionRelationService.selectUserdocsByMainType(type, true);
        }

    }

    public static RelatedUserdocObjectDetails fillRelatedUserdocObjectDetails(ReceptionForm receptionForm, FileService fileService, UserdocService userdocService, DocService docService) {
        String objectNumber = receptionForm.getUserdoc().getObjectNumber();
        if (StringUtils.isEmpty(objectNumber))
            return null;

        RelatedUserdocObjectDetails details = new RelatedUserdocObjectDetails();
        String type = objectNumber.split(DefaultValue.IPAS_OBJECT_ID_SEPARATOR)[1];
        if (FileType.USERDOC.code().equalsIgnoreCase(type)) {
            details.setUserdoc(true);
            CDocumentId cDocumentId = BasicUtils.createCDocumentId(objectNumber);
            if (Objects.nonNull(cDocumentId)) {
                CFileId cFileId = userdocService.selectMainObjectIdOfUserdoc(cDocumentId);
                details.setFilingNumber(CoreUtils.createFilingNumber(cFileId, false));

                CDocument cDocument = docService.selectDocument(cDocumentId);
                details.setUserdocType(cDocument.getCUserdocType().getUserdocType());
                details.setUserdocName(cDocument.getCUserdocType().getUserdocName());
                details.setExternalSystemId(cDocument.getExternalSystemId());
            }
        } else {
            CFileId cFileId = BasicUtils.createCFileId(objectNumber);
            if (Objects.nonNull(cFileId)) {
                CFile file = fileService.findById(cFileId.getFileSeq(), cFileId.getFileType(), cFileId.getFileSeries(), cFileId.getFileNbr());
                details.setTitle(file.getTitle());
            }
        }

        return details;
    }


}
