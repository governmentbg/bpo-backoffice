package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.*;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwMarkIndex;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 13.03.2021
 * Time: 0:16
 */
@Mapper(componentModel = "spring", uses = {FileIndexMapper.class})
public abstract class MarkIndexMapper extends IpObjectIndexMapperBase implements IndexMapper<IpMark, VwMarkIndex>{

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk",                                      source = "pk")
    @Mapping(target = "signWcode",                               source = "signWcode")
    @Mapping(target = "intregn",                                 source = "intregn")
    @Mapping(target = "file",                                    source = ".")
    //ima problem pri mapvaneto na mainOwner/servicePerson - generira se nqkakyv obsht kod, kojto se vika i za 2te mapvaniq i realno za mainOwner se slaga servicePerson-a....Zatova mapvaneto se pravi v aftermapping
//    @Mapping(target = "mainOwner.ipPerson.personName",                      source = "file.mainOwnerPersonName")
//    @Mapping(target = "servicePerson.ipPerson.personName",                  source = "file.servicePersonName")
    public abstract IpMark toEntity(VwMarkIndex vw);

    @AfterMapping
    public void afterToEntity(VwMarkIndex vw, @MappingTarget IpMark target) {
        mapServiceAndOwnerPersons(vw, target);
        if (Objects.equals(1, vw.getImg())) {
            target.setLogo(new IpLogo());
        }
        if (!ObjectUtils.isEmpty(vw.getNiceClasses())) {
            target.setIpMarkNiceClasses(Arrays.stream(vw.getNiceClasses().split(";")).map(r -> toNiceClass(target.getPk(), r)).collect(Collectors.toList()));
        }
        target.setOwners(mapRelatedPersons(vw.getOwnerPersonNumbers(), IpMarkOwners::new));
        target.setRepresentatives(mapRelatedPersons(vw.getRepresentativePersonNumbers(), IpMarkReprs::new));
    }
    private IpMarkNiceClasses toNiceClass(IpFilePK filePk, String niceClass) {
        IpMarkNiceClasses c = new IpMarkNiceClasses();
        IpMarkNiceClassesPK pk = new IpMarkNiceClassesPK();
        pk.setFileSeq(filePk.getFileSeq());
        pk.setFileTyp(filePk.getFileTyp());
        pk.setFileSer(filePk.getFileSer());
        pk.setFileNbr(filePk.getFileNbr());
        String[] parts = niceClass.split("/");
        pk.setNiceClassCode(Long.valueOf(parts[0]));
        pk.setNiceClassStatusWcode(parts[1]);
        c.setPk(pk);
        return c;
    }
}
