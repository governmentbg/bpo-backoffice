package bg.duosoft.ipas.core.mapper.acp;


import bg.duosoft.ipas.core.mapper.person.PersonAddressMapper;
import bg.duosoft.ipas.core.model.acp.CAcpPersonsData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import org.mapstruct.*;

import java.util.Objects;


@Mapper(componentModel = "spring", uses = {AcpReprsMapper.class, PersonAddressMapper.class})
public abstract class AcpPersonDataMapper {


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "representationData.representativeList", source = "acpRepresentatives")
    @Mapping(target = "infringerPerson", source = "acpInfringerPerson.infringerPerson")
    @Mapping(target = "servicePerson", source = "acpServicePerson.servicePerson")
    public abstract void toCore(IpMark mark, @MappingTarget CAcpPersonsData cAcpPersonsData);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract void toEntity(CAcpPersonsData cAcpPersonsData, @MappingTarget IpMark mark);


    @AfterMapping
    protected void afterToEntity(CAcpPersonsData cAcpPersonsData, @MappingTarget IpMark mark) {
        if (Objects.nonNull(mark.getAcpServicePerson()) && Objects.isNull(mark.getAcpServicePerson().getServicePerson())) {
            mark.setAcpServicePerson(null);
        }

        if (Objects.nonNull(mark.getAcpInfringerPerson()) && Objects.isNull(mark.getAcpInfringerPerson().getInfringerPerson())) {
            mark.setAcpInfringerPerson(null);
        }
    }

}
