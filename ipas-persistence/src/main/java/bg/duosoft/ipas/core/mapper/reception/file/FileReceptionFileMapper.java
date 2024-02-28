package bg.duosoft.ipas.core.mapper.reception.file;

import bg.duosoft.ipas.core.mapper.file.FileIdMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.enums.FilePublicationTyp;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.SubmissionType;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfLawApplicationSubTypeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Objects;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 11:23
 */
@Mapper(componentModel = "spring", uses = {FileReceptionDocMapper.class, FileIdMapper.class, ReceptionMapperHelper.class})
public abstract class FileReceptionFileMapper {

    @Autowired
    private CfLawApplicationSubTypeRepository cfLawApplicationSubTypeRepository;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "filingDate", source = "entryDate")
    @Mapping(target = "ipDoc", source = ".")
    @Mapping(target = "pk", source = "file.fileId")
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "fileSourceWcode", expression = "java(1L)")
    @Mapping(target = "applTyp", source = "file.applicationType")
    @Mapping(target = "applSubtyp", source = ".", qualifiedByName = "applicationSubtypeMapper")
    @Mapping(target = "title", source = "file.title")
    public abstract IpFile toEntity(CReception core);

    @AfterMapping
    public void afterToEntity(CReception source, @MappingTarget IpFile target) {
        target.setLawCode(cfLawApplicationSubTypeRepository.findByApplicationTypeAndSubtype(target.getApplTyp(), target.getApplSubtyp()).getPk().getLawCode());
        target.getIpDoc().setFile(target);//linking the ipDoc's file to the target!!!
        target.setIpFileRelationships1(new ArrayList<>());
        target.setIpFileRelationships2(new ArrayList<>());
        if (Objects.nonNull(source.getSubmissionType()) && (source.getSubmissionType().equals(SubmissionType.ELECTRONIC.code()) || source.getSubmissionType().equals(SubmissionType.IMPORT.code()))) {
            target.setPublicationTyp(FilePublicationTyp.ELECTRONIC.code());
        } else {
            target.setPublicationTyp(FilePublicationTyp.PAPER.code());
        }

        FileType fileType = FileType.selectByCode(target.getPk().getFileTyp());

        boolean avoidSettingEntitlementDate = (Objects.nonNull(source.getSubmissionType()) && source.getSubmissionType() == SubmissionType.ELECTRONIC.code()
                && (fileType == FileType.PATENT || fileType == FileType.EU_PATENT || fileType == FileType.UTILITY_MODEL)) || (fileType == FileType.PLANTS_AND_BREEDS);

        if (!avoidSettingEntitlementDate) {
            target.setEntitlementDate(target.getFilingDate());
        }
    }
}
