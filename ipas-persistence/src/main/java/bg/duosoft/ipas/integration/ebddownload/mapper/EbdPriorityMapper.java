package bg.duosoft.ipas.integration.ebddownload.mapper;

import bg.bpo.ebd.ebddpersistence.entity.EbdDPriority;
import bg.duosoft.ipas.core.model.file.CParisPriority;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 24.2.2020 г.
 * Time: 15:34
 */
@Mapper(componentModel = "spring")
public abstract class EbdPriorityMapper {
    @Mapping(target = "applicationId",      source = "noprio")
    @Mapping(target = "countryCode",        source = "idcountry")
    @Mapping(target = "priorityDate",       source = "dtprio")
    @Mapping(target = "notes",              source = "rmprio")
    @Mapping(target = "priorityStatus",     source = "stprio")
    @BeanMapping(ignoreByDefault = true)
    public abstract CParisPriority toCore(EbdDPriority priority);
}
