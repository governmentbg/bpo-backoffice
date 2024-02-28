package bg.duosoft.ipas.core.mapper.file;

import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipExtended;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RelationshipExtendedMapper {

    @Mapping(target = "applicationType",       source = "applicationType")
    @Mapping(target = "filingNumber",       source = "filingNumber")
    @Mapping(target = "filingDate",       source = "filingDate")
    @Mapping(target = "registrationNumber",       source = "registrationNumber")
    @Mapping(target = "registrationCountry",       source = "registrationCountry")
    @Mapping(target = "cancellationDate",       source = "cancellationDate")
    @Mapping(target = "registrationDate",       source = "registrationDate")
    @Mapping(target = "priorityDate",       source = "priorityDate")
    @Mapping(target = "serveMessageDate",       source = "serveMessageDate")
    @Mapping(target = "relationshipType",       source = "relationshipType.relationshipTyp")
    @BeanMapping(ignoreByDefault = true)
    public abstract CRelationshipExtended toCore(IpFileRelationshipExtended source);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract IpFileRelationshipExtended toEntity(CRelationshipExtended source);
}
