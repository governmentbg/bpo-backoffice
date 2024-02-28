package bg.duosoft.ipas.core.mapper.patent;

import bg.duosoft.ipas.core.model.patent.CAttachmentTypeBookmarks;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfAttachmentTypeBookmarks;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class AttachmentTypeBookmarksMapper {

    @Mapping(target = "bookmarkName",  source = "pk.bookmarkName")
    @Mapping(target = "bookmarkRequired",  source = "bookmarkRequired")
    @Mapping(target = "bookmarkOrder",  source = "bookmarkOrder")
    @BeanMapping(ignoreByDefault = true)
    public abstract CAttachmentTypeBookmarks toCore(CfAttachmentTypeBookmarks cfAttachmentTypeBookmarks);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfAttachmentTypeBookmarks toEntity(CAttachmentTypeBookmarks cAttachmentTypeBookmarks);


    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CAttachmentTypeBookmarks> toCoreList(List<CfAttachmentTypeBookmarks> bookmarks);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfAttachmentTypeBookmarks> toEntityList(List<CAttachmentTypeBookmarks> bookmarks);
}
