package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.CFileTypeGroup;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileTypeGroup;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 15.02.2022
 * Time: 11:08
 */
@Mapper(componentModel = "spring", uses = {StringToBooleanMapper.class})
public abstract class FileTypeGroupMapper  {
    @Mapping(target = "groupCode", source = "groupCode")
    @Mapping(target = "groupName", source = "groupName")
    @Mapping(target = "userdocFlag", source = "userdocFlag")
    @BeanMapping(ignoreByDefault = true)
    public abstract CFileTypeGroup toCore(CfFileTypeGroup cfFileTypeGroup);


    @AfterMapping
    protected void afterMapping(CfFileTypeGroup source, @MappingTarget CFileTypeGroup target) {
        target.setFileTypes(Arrays.stream(source.getFileTypes().split(",")).map(r -> r.trim()).collect(Collectors.toList()));
    }
}
