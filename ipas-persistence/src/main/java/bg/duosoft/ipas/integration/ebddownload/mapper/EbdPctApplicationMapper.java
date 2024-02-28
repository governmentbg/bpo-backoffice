package bg.duosoft.ipas.integration.ebddownload.mapper;

import bg.bpo.ebd.ebddpersistence.entity.EbdDPct;
import bg.duosoft.ipas.core.model.patent.CPctApplicationData;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: Georgi
 * Date: 24.2.2020 г.
 * Time: 15:18
 */
@Mapper(componentModel = "spring")
public abstract class EbdPctApplicationMapper {
    @Mapping(target = "pctApplicationId",           source = "pctApplicationId")
    @Mapping(target = "pctApplicationDate",         source = "pctApplicationDate")
    @Mapping(target = "pctPhase",                   source = "pctPhase")
    @Mapping(target = "pctPublicationId",           source = "pctPublicationId")
    @Mapping(target = "pctPublicationDate",         source = "pctPublicationDate")
    @Mapping(target = "pctPublicationCountryCode",  source = "pctPublicationCountryCode")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPctApplicationData toCore(EbdDPct e);
}
