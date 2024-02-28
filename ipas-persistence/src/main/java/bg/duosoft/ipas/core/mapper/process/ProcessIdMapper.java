package bg.duosoft.ipas.core.mapper.process;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProcessIdMapper {

    @Mapping(source = "procNbr", target = "processNbr")
    @Mapping(source = "procTyp", target = "processType")
    @BeanMapping(ignoreByDefault = true)
    CProcessId toCore(IpProcPK ipProcPK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    IpProcPK toEntity(CProcessId fileId);

}
