package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.core.mapper.userdoc.UserdocPanelMapper;
import bg.duosoft.ipas.core.mapper.userdoc.UserdocPersonRoleMapper;
import bg.duosoft.ipas.core.mapper.userdoc.userdoc_type.config.UserdocTypeConfigMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPanel;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.enums.UserdocGroup;
import bg.duosoft.ipas.persistence.model.entity.ext.core.*;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BaseUserdocTypeMapper {

    @Autowired
    private UserdocPanelMapper userdocPanelMapper;

    @Autowired
    private UserdocPersonRoleMapper userdocPersonRoleMapper;

    @Autowired
    private UserdocTypeConfigMapper userdocTypeConfigMapper;

    @AfterMapping
    protected void afterToCore(@MappingTarget CUserdocType target, CfUserdocType source) {
        List<CfUserdocTypeToPersonRole> personRoles = source.getPersonRoles();
        if (!CollectionUtils.isEmpty(personRoles)) {
            List<CfUserdocPersonRole> collect = personRoles.stream().map(CfUserdocTypeToPersonRole::getUserdocPersonRole).collect(Collectors.toList());
            List<CUserdocPersonRole> cUserdocPersonRoles = userdocPersonRoleMapper.toCoreList(collect);
            target.setRoles(cUserdocPersonRoles);
        }

        if(Objects.nonNull(source.getUserdocTypeConfig())){
            target.setUserdocTypeConfig(userdocTypeConfigMapper.toCore(source.getUserdocTypeConfig()));
        }

        List<CfUserdocTypeToUiPanel> panels = source.getPanels();
        if (!CollectionUtils.isEmpty(panels)) {
            List<CfUserdocUiPanel> panelList = panels.stream().map(CfUserdocTypeToUiPanel::getUserdocUiPanel).collect(Collectors.toList());
            List<CUserdocPanel> cUserdocPanels = userdocPanelMapper.toCoreList(panelList);
            target.setPanels(cUserdocPanels);
        }

        List<CfUserdocInvalidationRelation> invalidationRelations = source.getInvalidationRelations();
        if (!CollectionUtils.isEmpty(invalidationRelations)) {
            List<String> invalidatedUserdocTypes = invalidationRelations.stream().map(cfUserdocInvalidationRelation -> cfUserdocInvalidationRelation.getPk().getInvalidatedUserdocType()).collect(Collectors.toList());
            target.setInvalidatedUserdocTypes(invalidatedUserdocTypes);
        }

        String userdocGroupName = source.getUserdocGroupName();
        if (!StringUtils.isEmpty(userdocGroupName)) {
            UserdocGroup userdocGroup = UserdocGroup.selectByCode(userdocGroupName);
            if (Objects.nonNull(userdocGroup))
                target.setUserdocGroup(userdocGroup);
        }

    }

}
