package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocPatentData;
import bg.duosoft.ipas.persistence.model.entity.patent_data.IpUserdocPatentData;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserdocPatentDataMapper {

    @Mapping(source = "titleBg", target = "titleBg")
    @Mapping(source = "descriptionPagesCount", target = "descriptionPagesCount")
    @Mapping(source = "claimsCount", target = "claimsCount")
    @Mapping(source = "drawingsCount", target = "drawingsCount")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocPatentData toCore(IpUserdocPatentData patentData);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpUserdocPatentData toEntity(CUserdocPatentData patentData);
}
