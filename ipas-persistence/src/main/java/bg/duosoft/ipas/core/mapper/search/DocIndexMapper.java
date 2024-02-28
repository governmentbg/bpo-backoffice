package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwDocIndex;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwUserdocIndex;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 11.03.2021
 * Time: 12:46
 */
@Mapper(componentModel = "spring")
public abstract class DocIndexMapper implements IndexMapper<IpDoc, VwDocIndex>{
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "pk",                             source = "pk")
    @Mapping(target = "docSeqNbr",                      source = "docSeqNbr")
    @Mapping(target = "docSeqTyp.docSeqTyp",            source = "docSeqTyp")
    @Mapping(target = "externalSystemId",               source = "externalSystemId")
    @Mapping(target = "filingDate",                     source = "filingDate")
    @Mapping(target = "proc.pk",                        source = "procPk")
    @Mapping(target = "proc.statusCode",                source = "statusCode")
    @Mapping(target = "proc.userdocTyp",                source = "userdocTyp")
    public abstract IpDoc toEntity(VwDocIndex vw);


    //poluchava se zaciklqne ako subProc-a se mapne s @Mapping anotacii, tyj kato klasovete na subproc i proc sa IpProcSimple, zatova se pravi v aftermapping
    @AfterMapping
    public void afterToEntity(VwDocIndex vw, @MappingTarget IpDoc target) {
        if (vw.getFilePk() != null) {

            if (target.getProc() == null) {
                target.setProc(new IpProcSimple());
            }
            if (target.getProc().getSubProc() == null) {
                target.getProc().setSubProc(new IpProcSimple());
            }

            IpProcSimple subproc = target.getProc().getSubProc();
            IpFilePK fpk = vw.getFilePk();
            subproc.setFileSeq(fpk.getFileSeq());
            subproc.setFileTyp(fpk.getFileTyp());
            subproc.setFileSer(fpk.getFileSer());
            subproc.setFileNbr(fpk.getFileNbr());
            subproc.setStatusCode(vw.getFileStatusCode());
            subproc.setPk(new IpProcPK());
            subproc.getPk().setProcNbr(vw.getFileProcNbr());
            subproc.getPk().setProcTyp(vw.getFileProcTyp());
        }
    }
}
