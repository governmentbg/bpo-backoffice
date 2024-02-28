package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwUserdocIndex;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 11.03.2021
 * Time: 12:46
 */
@Mapper(componentModel = "spring")
public abstract class UserdocIndexMapper implements IndexMapper<IpUserdoc, VwUserdocIndex>{
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk",                             source = "pk")
//    @Mapping(target = "ipDoc.proc.subProc.fileSeq",     source = "filePk.fileSeq")
//    @Mapping(target = "ipDoc.proc.subProc.fileTyp",     source = "filePk.fileTyp")
//    @Mapping(target = "ipDoc.proc.subProc.fileSer",     source = "filePk.fileSer")
//    @Mapping(target = "ipDoc.proc.subProc.fileNbr",     source = "filePk.fileNbr")
    @Mapping(target = "ipDoc.proc.pk",                  source = "procPk")
    @Mapping(target = "ipDoc.proc.userdocTyp",          source = "userdocTyp")
    @Mapping(target = "ipDoc.externalSystemId",         source = "externalSystemId")
    @Mapping(target = "ipDoc.filingDate",               source = "filingDate")
    @Mapping(target = "ipDoc.proc.statusCode",          source = "statusCode")
    public abstract IpUserdoc toEntity(VwUserdocIndex vw);


    //poluchava se zaciklqne ako subProc-a se mapne s @Mapping anotacii, tyj kato klasovete na subproc i proc sa IpProcSimple, zatova se pravi v aftermapping
    @AfterMapping
    public void afterToEntity(VwUserdocIndex vw, @MappingTarget IpUserdoc target) {
        if (target.getIpDoc().getProc() == null) {
            target.getIpDoc().setProc(new IpProcSimple());
        }
        if (target.getIpDoc().getProc().getSubProc() == null) {
            target.getIpDoc().getProc().setSubProc(new IpProcSimple());
        }
        IpProcSimple subproc = target.getIpDoc().getProc().getSubProc();
        subproc.setFileSeq(vw.getFilePk().getFileSeq());
        subproc.setFileTyp(vw.getFilePk().getFileTyp());
        subproc.setFileSer(vw.getFilePk().getFileSer());
        subproc.setFileNbr(vw.getFilePk().getFileNbr());
    }
}
