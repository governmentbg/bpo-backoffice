package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToUiPanel;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {StringToBooleanMapper.class})
public abstract class UserdocToUiPanelMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "panel", source = "pk.panel")
    @Mapping(target = "indRecordal", source = "userdocUiPanel.indRecordal")
    @Mapping(target = "name", source = "userdocUiPanel.name")
    @Mapping(target = "nameEn", source = "userdocUiPanel.nameEn")
    public abstract CUserdocPanel toCore(CfUserdocTypeToUiPanel cfUserdocTypeToUiPanel);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    @Mapping(target = "userdocUiPanel.panel", source = "panel")
    @Mapping(target = "userdocUiPanel.indRecordal", source = "indRecordal")
    public abstract CfUserdocTypeToUiPanel toEntity(CUserdocPanel cUserdocPanel);

}
