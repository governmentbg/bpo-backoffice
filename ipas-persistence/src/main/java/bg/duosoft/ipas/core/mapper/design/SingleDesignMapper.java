package bg.duosoft.ipas.core.mapper.design;

import bg.duosoft.ipas.core.mapper.common.FileSeqTypSerNbrMapper;
import bg.duosoft.ipas.core.mapper.patent.*;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.persistence.model.entity.FileSeqTypSerNbrPK;
import bg.duosoft.ipas.persistence.model.entity.design.SingleDesign;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                SingleDesignFileMapper.class,
                DrawingMapper.class,
                PatentLocarnoClassesMapper.class,
        })
public abstract class SingleDesignMapper {

    @Autowired
    private FileSeqTypSerNbrMapper fileSeqTypSerNbrMapper;

    @Mapping(target = "technicalData.title", source = "title")
    @Mapping(target = "technicalData.englishTitle", source = "englishTitle")
    @Mapping(target = "patentContainsDrawingList", expression = "java(singleDesign.getSingleDesignDrawings() != null)")
    @Mapping(target = "indReadDrawingList", expression = "java(loadFileContent)")
    @Mapping(target = "technicalData.drawingList", source = "singleDesignDrawings")
    @Mapping(target = "file", source = "file")
    @Mapping(target = "technicalData.locarnoClassList", source = "ipPatentLocarnoClasses")
    @Mapping(target = "rowVersion", source = "rowVersion")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatent toCore(SingleDesign singleDesign, @Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "cfApplicationSubtype.pk.applTyp",                    source = "file.filingData.applicationType")
    @Mapping(target = "cfApplicationSubtype.pk.applSubtyp",                 source = "file.filingData.applicationSubtype")
    @Mapping(target = "filingDate",                    source = "file.filingData.filingDate")
    @Mapping(target = "receptionDate",                 source = "file.filingData.receptionDate")
    @Mapping(target = "lawCode",                       source = "file.filingData.lawCode")
    @Mapping(target = "docLog",                        source = "file.filingData.receptionDocument.documentId.docLog")
    @Mapping(target = "docNbr",                        source = "file.filingData.receptionDocument.documentId.docNbr")
    @Mapping(target = "docSer",                        source = "file.filingData.receptionDocument.documentId.docSeries")
    public abstract SingleDesign toEntity(CPatent cSingleDesign, @Context Boolean loadFileContent);

    @AfterMapping
    protected void fillPrimaryKeys(@MappingTarget SingleDesign target, CPatent source) {
        List<FileSeqTypSerNbrPK> pks = new ArrayList<>();
        target.setPk(new IpFilePK());
        pks.add(target.getPk());

        if (target.getSingleDesignDrawings() != null) {
            target.getSingleDesignDrawings().stream().forEach(drawing->{
                pks.add(drawing.getPk());
                if (drawing.getSingleDesignExtended()!=null){
                    pks.add(drawing.getSingleDesignExtended().getPk());
                }
            });
        }

        if (target.getIpPatentLocarnoClasses() != null) {
            target.getIpPatentLocarnoClasses().stream().forEach(locarnoClass-> pks.add(locarnoClass.getPk()));
        }

        pks.forEach(r -> fileSeqTypSerNbrMapper.toEntity(source.getFile().getFileId(), r));
    }
}
