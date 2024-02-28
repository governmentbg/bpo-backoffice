package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocApprovedData;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocApprovedData;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by Raya
 * 05.09.2020
 */
@Mapper(componentModel = "spring", uses = {
    StringToBooleanMapper.class
})
public abstract class UserdocApprovedDataMapper {

    @Mapping(source = "approvedAllNice", target = "approvedAllNice")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocApprovedData toCore(IpUserdocApprovedData ipUserdocApprovedData);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocApprovedData toEntity(CUserdocApprovedData cNiceClass);


}
