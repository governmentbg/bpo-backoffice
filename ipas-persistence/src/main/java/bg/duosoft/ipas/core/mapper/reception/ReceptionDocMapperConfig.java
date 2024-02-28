package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import org.mapstruct.BeanMapping;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 15:57
 */
@MapperConfig
public abstract class ReceptionDocMapperConfig {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion",                 constant = "1")
    @Mapping(target = "filingDate",                 source = "entryDate")
    @Mapping(target = "indFaxReception",            source = "originalExpected")
    @Mapping(target = "receptionDate",              expression = "java(new java.util.Date())")
    @Mapping(target = "receptionWcode",             source = ".",             qualifiedByName = "receptionWcodeMapper")
    @Mapping(target = "externalSystemId",           source = "externalSystemId")
    @Mapping(target = "receptionUserId",            source = ".",             qualifiedByName = "loggedUserMapper")
    public abstract void receptionDocMapperBase(CReception receptionForm, @MappingTarget IpDoc target);
}
