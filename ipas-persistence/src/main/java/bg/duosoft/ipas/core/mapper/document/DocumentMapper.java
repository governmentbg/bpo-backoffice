package bg.duosoft.ipas.core.mapper.document;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {
        DocumentIdMapper.class,
        FileIdMapper.class
})
public abstract class DocumentMapper {

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Autowired
    private DocumentIdMapper documentIdMapper;

    @Mapping(target = "rowVersion", source = "rowVersion")
    @Mapping(target = "documentId", source = "pk")
    @Mapping(target = "filingDate", source = "filingDate")
    @Mapping(target = "receptionWcode", source = "receptionWcode")
    @Mapping(target = "externalOfficeCode", source = "externalOfficeCode")
    @Mapping(target = "externalOfficeFilingDate", source = "externalOfficeFilingDate")
    @Mapping(target = "externalSystemId", source = "externalSystemId")
    @Mapping(target = "indNotAllFilesCapturedYet", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getIndNotAllFilesCapturedYet()))")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "applSubtyp", source = "applSubtyp")
    @Mapping(target = "receptionDate", source = "receptionDate")
    @Mapping(target = "documentSeqId.docSeqName", source = "docSeqTyp.docSeqName")
    @Mapping(target = "documentSeqId.docSeqType", source = "docSeqTyp.docSeqTyp")
    @Mapping(target = "documentSeqId.docSeqNbr", source = "docSeqNbr")
    @Mapping(target = "documentSeqId.docSeqSeries", source = "docSeqSeries")
    @Mapping(target = "receptionUserId", source = "receptionUserId")
    @Mapping(target = "indFaxReception", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getIndFaxReception()))")
    @Mapping(target = "extraData.dataFlag1", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getDataFlag1()))")
    @Mapping(target = "extraData.dataFlag2", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getDataFlag2()))")
    @Mapping(target = "extraData.dataFlag3", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getDataFlag3()))")
    @Mapping(target = "extraData.dataFlag4", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getDataFlag4()))")
    @Mapping(target = "extraData.dataFlag5", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(ipDoc.getDataFlag5()))")
    @Mapping(target = "extraData.dataDate1", source = "dataDate1")
    @Mapping(target = "extraData.dataDate2", source = "dataDate2")
    @Mapping(target = "extraData.dataDate3", source = "dataDate3")
    @Mapping(target = "extraData.dataDate4", source = "dataDate4")
    @Mapping(target = "extraData.dataDate5", source = "dataDate5")
    @Mapping(target = "extraData.dataNbr1", source = "dataNbr1")
    @Mapping(target = "extraData.dataNbr2", source = "dataNbr2")
    @Mapping(target = "extraData.dataNbr3", source = "dataNbr3")
    @Mapping(target = "extraData.dataNbr4", source = "dataNbr4")
    @Mapping(target = "extraData.dataNbr5", source = "dataNbr5")
    @Mapping(target = "extraData.dataText1", source = "dataText1")
    @Mapping(target = "extraData.dataText2", source = "dataText2")
    @Mapping(target = "extraData.dataText3", source = "dataText3")
    @Mapping(target = "extraData.dataText4", source = "dataText4")
    @Mapping(target = "extraData.dataText5", source = "dataText5")
    @Mapping(target = "extraData.dataCodeTyp1", source = "dataCodeTyp1")
    @Mapping(target = "extraData.dataCodeTyp2", source = "dataCodeTyp2")
    @Mapping(target = "extraData.dataCodeTyp3", source = "dataCodeTyp3")
    @Mapping(target = "extraData.dataCodeTyp4", source = "dataCodeTyp4")
    @Mapping(target = "extraData.dataCodeTyp5", source = "dataCodeTyp5")
    @Mapping(target = "extraData.dataCodeId1", source = "dataCodeId1")
    @Mapping(target = "extraData.dataCodeId2", source = "dataCodeId2")
    @Mapping(target = "extraData.dataCodeId3", source = "dataCodeId3")
    @Mapping(target = "extraData.dataCodeId4", source = "dataCodeId4")
    @Mapping(target = "extraData.dataCodeId5", source = "dataCodeId5")
    @Mapping(target = "fileId", source = "file.pk")
    @Mapping(target = "dailyLogDate", source = "ipDailyLog.pk.dailyLogDate")
    @Mapping(target = "appType", source = "applTyp")
    @BeanMapping(ignoreByDefault = true)
    public abstract CDocument toCore(IpDoc ipDoc);

    @Mapping(target = "indFaxReception", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getIndFaxReception()))")
    @Mapping(target = "indNotAllFilesCapturedYet", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getIndNotAllFilesCapturedYet()))")
    @Mapping(target = "dataFlag1", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getExtraData().getDataFlag1()))")
    @Mapping(target = "dataFlag2", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getExtraData().getDataFlag2()))")
    @Mapping(target = "dataFlag3", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getExtraData().getDataFlag3()))")
    @Mapping(target = "dataFlag4", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getExtraData().getDataFlag4()))")
    @Mapping(target = "dataFlag5", expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(doc.getExtraData().getDataFlag5()))")
    @Mapping(target = "ipDailyLog.pk.dailyLogDate", source = "doc.dailyLogDate")
    @Mapping(target = "ipDailyLog.pk.docLog", source = "doc.documentId.docLog")
    @Mapping(target = "ipDailyLog.pk.docOri", source = "doc.documentId.docOrigin")
    @Mapping(target = "ipUserdoc.pk", source = "documentId")
    @Mapping(target = "applTyp", source = "appType")
    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpDoc toEntity(CDocument doc);

    @AfterMapping
    protected void afterToCore(IpDoc source, @MappingTarget CDocument target) {
        IpUserdoc ipUserdoc = source.getIpUserdoc();
        if (Objects.nonNull(ipUserdoc)) {
            target.setUserdoc(true);

            CDocumentId cDocumentId = documentIdMapper.toCore(source.getPk());
            CUserdocType cUserdocType = userdocTypesService.selectUserdocTypeByDocId(cDocumentId);
            target.setCUserdocType(cUserdocType);
        }
    }

    @AfterMapping
    protected void afterToEntity(@MappingTarget IpDoc target, CDocument source) {
        CFileId fileId = source.getFileId();
        if (Objects.isNull(fileId) || Objects.isNull(source.getFileId().getFileNbr()))
            target.setFile(null);

    }
}
