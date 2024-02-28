package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.ApplicationSubTypesMapper;
import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.miscellaneous.LocarnoClassesMapper;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.mark.CUserdocSingleDesign;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocSingleDesign;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocSingleDesignLocarnoClasses;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        StringToBooleanMapper.class,
        ApplicationSubTypesMapper.class,
        UserdocSingleDesignLocarnoClassesMapper.class
})
public abstract class UserdocSingleDesignMapper {

    @Autowired
    private LocarnoClassesMapper locarnoClassesMapper;

    @Mapping(source = "pk.docOri", target = "documentId.docOrigin")
    @Mapping(source = "pk.docLog", target = "documentId.docLog")
    @Mapping(source = "pk.docSer", target = "documentId.docSeries")
    @Mapping(source = "pk.docNbr", target = "documentId.docNbr")
    @Mapping(source = "pk.fileSeq", target = "fileId.fileSeq")
    @Mapping(source = "pk.fileTyp", target = "fileId.fileType")
    @Mapping(source = "pk.fileSer", target = "fileId.fileSeries")
    @Mapping(source = "pk.fileNbr", target = "fileId.fileNbr")
    @Mapping(source = "productTitle", target = "productTitle")
    @Mapping(source = "cfApplicationSubtype", target = "applicationSubType")
    @Mapping(source = "locarnoClasses", target = "locarnoClasses")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocSingleDesign toCore(IpUserdocSingleDesign ipUserdocSingleDesign);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "cfApplicationSubtype.pk.applTyp", source = "applicationSubType.applicationType")
    @Mapping(target = "cfApplicationSubtype.pk.applSubtyp", source = "applicationSubType.applicationSubType")
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocSingleDesign toEntity(CUserdocSingleDesign userdocSingleDesign);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUserdocSingleDesign> toEntityList(List<CUserdocSingleDesign> userdocSingleDesigns);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CUserdocSingleDesign> toCoreList(List<IpUserdocSingleDesign> ipUserdocSingleDesigns);

    @AfterMapping
    protected void afterToCore(IpUserdocSingleDesign source, @MappingTarget CUserdocSingleDesign target) {

    }

    @AfterMapping
    protected void afterToEntity(CUserdocSingleDesign source, @MappingTarget IpUserdocSingleDesign target) {
        CDocumentId documentId = source.getDocumentId();
        CFileId fileId = source.getFileId();

        List<IpUserdocSingleDesignLocarnoClasses> locarnoClasses = target.getLocarnoClasses();
        if (!CollectionUtils.isEmpty(locarnoClasses)) {
            for (IpUserdocSingleDesignLocarnoClasses locarnoClass : locarnoClasses) {
                locarnoClass.getPk().setDocOri(documentId.getDocOrigin());
                locarnoClass.getPk().setDocLog(documentId.getDocLog());
                locarnoClass.getPk().setDocSer(documentId.getDocSeries());
                locarnoClass.getPk().setDocNbr(documentId.getDocNbr());
                locarnoClass.getPk().setFileSeq(fileId.getFileSeq());
                locarnoClass.getPk().setFileTyp(fileId.getFileType());
                locarnoClass.getPk().setFileSer(fileId.getFileSeries());
                locarnoClass.getPk().setFileNbr(fileId.getFileNbr());

                CfClassLocarnoPK cfClassLocarnoPK = new CfClassLocarnoPK();
                cfClassLocarnoPK.setLocarnoClassCode(locarnoClass.getPk().getLocarnoClassCode());
                cfClassLocarnoPK.setLocarnoEditionCode(locarnoClass.getLocarnoEditionCode());
                locarnoClass.getCfClassLocarno().setPk(cfClassLocarnoPK);


            }
        }
    }

}
