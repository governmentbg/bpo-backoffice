package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocExtraDataToUiPanel;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocExtraDataType;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocUiPanel;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserdocPanelMapper {

    @Autowired
    private UserdocExtraDataTypeMapper userdocExtraDataTypeMapper;

    @Mapping(source = "panel", target = "panel")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "nameEn", target = "nameEn")
    @Mapping(expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getTextAsBoolean(cfUserdocUiPanel.getIndRecordal()))", target = "indRecordal")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocPanel toCore(CfUserdocUiPanel cfUserdocUiPanel);

    @InheritInverseConfiguration
    @Mapping(expression = "java(bg.duosoft.ipas.core.mapper.MapperHelper.getBooleanAsText(userdocPanel.getIndRecordal()))", target = "indRecordal")
    @BeanMapping(ignoreByDefault = true)
    public abstract CfUserdocUiPanel toEntity(CUserdocPanel userdocPanel);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CfUserdocUiPanel> toEntityList(List<CUserdocPanel> userdocPanels);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CUserdocPanel> toCoreList(List<CfUserdocUiPanel> cfUserdocUiPanels);

    @AfterMapping
    protected void afterToCore(@MappingTarget CUserdocPanel target, CfUserdocUiPanel source) {
        List<CfUserdocExtraDataToUiPanel> extraDataTypes = source.getExtraDataTypes();
        if (!CollectionUtils.isEmpty(extraDataTypes)) {
            List<CfUserdocExtraDataType> collect = extraDataTypes.stream()
                    .map(CfUserdocExtraDataToUiPanel::getExtraDataType)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            target.setExtraDataTypes(userdocExtraDataTypeMapper.toCoreList(collect));
        }

    }

}
