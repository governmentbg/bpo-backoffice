package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidocPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OffidocIdMapper {

    @Mapping(source = "offidocNbr", target = "offidocNbr")
    @Mapping(source = "offidocOri", target = "offidocOrigin")
    @Mapping(source = "offidocSer", target = "offidocSeries")
    @BeanMapping(ignoreByDefault = true)
    COffidocId toCore(IpOffidocPK ipOffidocPK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    IpOffidocPK toEntity(COffidocId cOffidocId);

}
