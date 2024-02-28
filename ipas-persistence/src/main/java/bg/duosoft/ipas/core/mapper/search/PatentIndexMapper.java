package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentInventors;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentOwners;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentReprs;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwPatentIndex;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 13.03.2021
 * Time: 0:16
 */
@Mapper(componentModel = "spring", uses = {FileIndexMapper.class})
public abstract class PatentIndexMapper extends IpObjectIndexMapperBase implements IndexMapper<IpPatent, VwPatentIndex>{

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk",                                             source = "pk")
    @Mapping(target = "file",                                           source = ".")
    //ima problem pri mapvaneto na mainOwner/servicePerson - generira se nqkakyv obsht kod, kojto se vika i za 2te mapvaniq i realno za mainOwner se slaga servicePerson-a....Zatova mapvaneto se pravi v aftermapping
//    @Mapping(target = "mainOwner.ipPerson.personName",                  source = "file.mainOwnerPersonName")
//    @Mapping(target = "servicePerson.ipPerson.personName",              source = "file.servicePersonName")
    @Mapping(target = "plantData.proposedDenomination",                 source = "plant.proposedDenomination")
    @Mapping(target = "plantData.proposedDenominationEng",              source = "plant.proposedDenominationEng")
    @Mapping(target = "plantData.publDenomination",                     source = "plant.publDenomination")
    @Mapping(target = "plantData.publDenominationEng",                  source = "plant.publDenominationEng")
    @Mapping(target = "plantData.apprDenomination",                     source = "plant.apprDenomination")
    @Mapping(target = "plantData.apprDenominationEng",                  source = "plant.apprDenominationEng")
    @Mapping(target = "plantData.rejDenomination",                      source = "plant.rejDenomination")
    @Mapping(target = "plantData.rejDenominationEng",                   source = "plant.rejDenominationEng")
    @Mapping(target = "plantData.features",                             source = "plant.features")
    @Mapping(target = "plantData.stability",                            source = "plant.stability")
    @Mapping(target = "plantData.testing",                              source = "plant.testing")
    @Mapping(target = "plantData.plantNumenclature.taxonCode",          source = "plant.taxonCode")
    @Mapping(target = "plantData.plantNumenclature.commonClassifyBul",  source = "plant.commonClassifyBul")
    @Mapping(target = "plantData.plantNumenclature.commonClassifyEng",  source = "plant.commonClassifyEng")
    @Mapping(target = "plantData.plantNumenclature.latinClassify",      source = "plant.latinClassify")
    @Mapping(target = "spcExtended.bgPermitDate",                       source = "spc.bgPermitDate")
    @Mapping(target = "spcExtended.bgPermitNumber",                     source = "spc.bgPermitNumber")
    @Mapping(target = "spcExtended.euPermitDate",                       source = "spc.euPermitDate")
    @Mapping(target = "spcExtended.euPermitNumber",                     source = "spc.euPermitNumber")
    public abstract IpPatent toEntity(VwPatentIndex vw);

    @AfterMapping
    public void afterToEntity(VwPatentIndex vw, @MappingTarget IpPatent target) {
        mapServiceAndOwnerPersons(vw, target);
        target.setOwners(mapRelatedPersons(vw.getOwnerPersonNumbers(), IpPatentOwners::new));
        target.setRepresentatives(mapRelatedPersons(vw.getRepresentativePersonNumbers(), IpPatentReprs::new));
        target.setIpPatentInventors(mapRelatedPersons(vw.getInventorPersonNumbers(), IpPatentInventors::new));
    }
}
