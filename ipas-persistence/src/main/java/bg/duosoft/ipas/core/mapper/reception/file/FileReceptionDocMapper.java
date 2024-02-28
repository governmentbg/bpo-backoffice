package bg.duosoft.ipas.core.mapper.reception.file;
import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.mapper.reception.ReceptionDocMapperConfig;
import bg.duosoft.ipas.core.mapper.reception.ReceptionMapperHelper;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import org.mapstruct.*;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 11:41
 */
@Mapper(componentModel = "spring", uses = {StringToBooleanMapper.class, ReceptionMapperHelper.class}, config = ReceptionDocMapperConfig.class)
public abstract class FileReceptionDocMapper extends BaseObjectMapper<IpDoc, CReception> {
    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "receptionDocMapperBase")
    @Mapping(target = "applTyp",                    source = "file.applicationType")
    @Mapping(target = "notes",                      source = "notes")
    @Mapping(target = "applSubtyp",                 source = ".",             qualifiedByName = "applicationSubtypeMapper")
    public abstract IpDoc toEntity(CReception receptionForm);

}
