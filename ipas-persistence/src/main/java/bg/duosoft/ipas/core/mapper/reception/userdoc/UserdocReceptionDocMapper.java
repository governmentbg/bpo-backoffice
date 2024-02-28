package bg.duosoft.ipas.core.mapper.reception.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionDocMapperConfig;
import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Georgi
 * Date: 8.6.2020 г.
 * Time: 15:55
 */
@Mapper(componentModel = "spring", uses = {StringToBooleanMapper.class, ReceptionMapperHelper.class}, config = ReceptionDocMapperConfig.class)
public abstract class UserdocReceptionDocMapper  extends BaseObjectMapper<IpDoc, CReception> {
    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "receptionDocMapperBase")
    @Mapping(target = "indNotAllFilesCapturedYet",              expression = "java(\"N\")")
    @Mapping(target = "notes",                                  source = "notes")
    public abstract IpDoc toEntity(CReception receptionForm);
    @AfterMapping
    public void afterToEntity(CReception source, @MappingTarget IpDoc target) {
    }
}
