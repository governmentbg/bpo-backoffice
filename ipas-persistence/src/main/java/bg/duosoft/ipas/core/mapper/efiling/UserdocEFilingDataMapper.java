package bg.duosoft.ipas.core.mapper.efiling;

import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpUserodocEFilingData;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserdocEFilingDataMapper {

    @Mapping(target = "logUserName",  source = "logUserName")
    @Mapping(target = "esUser",  source = "esUser")
    @Mapping(target = "esUserName",  source = "esUserName")
    @Mapping(target = "esUserEmail",  source = "esUserEmail")
    @Mapping(target = "esValidFrom",  source = "esValidFrom")
    @Mapping(target = "esValidTo",  source = "esValidTo")
    @Mapping(target = "esDate",  source = "esDate")
    @BeanMapping(ignoreByDefault = true)
    public abstract CEFilingData toCore(IpUserodocEFilingData eFilingData);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserodocEFilingData toEntity(CEFilingData ceFilingData);
}
