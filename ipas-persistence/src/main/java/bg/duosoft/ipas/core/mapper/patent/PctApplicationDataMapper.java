package bg.duosoft.ipas.core.mapper.patent;

/**
 * User: Georgi
 * Date: 11.2.2020 г.
 * Time: 17:33
 */

import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPctApplicationData;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

/**
 * tozi klas ne raboti sys CPctApplicationData a s CPatent, za da moje v aftermapping-a ako nqma popylneni nikakvi danni za CPctApplicationData, da go napravi na null!!!!
 * Inache ako beshe IPPatent to CPctApplicationData v mappingTarget-a nqma kak da zanulq CPctApplicationData. Probvah da go napravq da return-va null, no generiraniq kod stava
 *         CPctApplicationData target = afterToCoreMapping( cPctApplicationData );
 *         if ( target != null ) {
 *             return target;
 *         }
 *         return cPctApplicationData;
 *
 * men me ustroivashe samo afterToCoreMapping( cPctApplicationData ), togava PatentMapper-a shteshe da moje da setne null za CPctApplicationData
 */
@Mapper(componentModel = "spring")
public abstract class PctApplicationDataMapper {
    @Mapping(target = "pctApplicationData.pctPhase", source = "pctPhase")
    @Mapping(target = "pctApplicationData.pctApplicationId", source = "pctApplId")
    @Mapping(target = "pctApplicationData.pctApplicationDate", source = "pctApplDate")
    @Mapping(target = "pctApplicationData.pctPublicationCountryCode", source = "pctPublCountry")
    @Mapping(target = "pctApplicationData.pctPublicationDate", source = "pctPublDate")
    @Mapping(target = "pctApplicationData.pctPublicationType", source = "pctPublTyp")
    @Mapping(target = "pctApplicationData.pctPublicationId", source = "pctPublId")
    @BeanMapping(ignoreByDefault = true)
    public abstract CPatent toCore(IpPatent source, @MappingTarget CPatent target);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract void toEntity(CPatent source, @MappingTarget IpPatent target);

    @AfterMapping
    public void afterToCoreMapping(@MappingTarget CPatent target) {
        CPctApplicationData pct = target.getPctApplicationData();
        if (
                pct.getPctPhase() == null &&
                StringUtils.isEmpty(pct.getPctApplicationId()) &&
                pct.getPctApplicationDate() == null &&
                StringUtils.isEmpty(pct.getPctPublicationId()) &&
                StringUtils.isEmpty(pct.getPctPublicationType()) &&
                StringUtils.isEmpty(pct.getPctPublicationCountryCode()) &&
                pct.getPctApplicationDate() == null
        ) {
            target.setPctApplicationData(null);
        }
    }

}
