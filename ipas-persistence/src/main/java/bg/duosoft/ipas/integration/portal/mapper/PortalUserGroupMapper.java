package bg.duosoft.ipas.integration.portal.mapper;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.integration.portal.model.PortalUserGroup;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUserGroup;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class PortalUserGroupMapper extends BaseObjectMapper<PortalUserGroup, CPortalUserGroup> {

    @AfterMapping
    protected void afterToCore(PortalUserGroup source, @MappingTarget CPortalUserGroup target) {

    }

}
