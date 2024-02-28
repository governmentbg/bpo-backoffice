package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CAttachmentType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentTypeBookmarks;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

@Mapper(componentModel = "spring", uses = {AttachmentTypeBookmarksMapper.class})
public abstract class AttachmentTypeMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "attachmentNameSuffix", source = "attachmentNameSuffix")
    @Mapping(target = "attachmentOrder", source = "attachmentOrder")
    @Mapping(target = "attachmentFileTypes", source = "attachmentFileTypes")
    @Mapping(target = "attachmentExtension", source = "attachmentExtension")
    @Mapping(target = "bookmarks", source = "bookmarks")
    @BeanMapping(ignoreByDefault = true)
    public abstract CAttachmentType toCore(CfAttachmentType bookmarks);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAttachmentType toEntity(CAttachmentType cfAttachmentType);

    @AfterMapping
    protected void afterToEntity(@MappingTarget CfAttachmentType target, CAttachmentType source) {
        if (!CollectionUtils.isEmpty(target.getBookmarks())){
            for (CfAttachmentTypeBookmarks bookmark:target.getBookmarks()) {
                bookmark.getPk().setAttachmentTypeId(target.getId());
            }
        }
    }

}
