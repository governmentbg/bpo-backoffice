package bg.duosoft.ipas.core.mapper.search;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFilesPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.VwIpObjectIndex;
import org.mapstruct.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;


/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 23:43
 */
@Mapper(componentModel = "spring")
public abstract class FileIndexMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "applSubtyp",                          source = "file.applSubTyp")
    @Mapping(target = "applTyp",                             source = "file.applTyp")
    @Mapping(target = "entitlementDate",                     source = "file.entitlementDate")
    @Mapping(target = "expirationDate",                      source = "file.expirationDate")
    @Mapping(target = "pk.fileSeq",                          source = "pk.fileSeq")
    @Mapping(target = "pk.fileTyp",                          source = "pk.fileTyp")
    @Mapping(target = "pk.fileSer",                          source = "pk.fileSer")
    @Mapping(target = "pk.fileNbr",                          source = "pk.fileNbr")
    @Mapping(target = "filingDate",                          source = "file.filingDate")
    @Mapping(target = "procTyp",                             source = "file.procPk.procTyp")
    @Mapping(target = "procNbr",                             source = "file.procPk.procNbr")
    @Mapping(target = "ipProcSimple.pk",                     source = "file.procPk")
    @Mapping(target = "registrationDate",                    source = "file.registrationDate")
    @Mapping(target = "registrationDup",                     source = "file.registrationDup")
    @Mapping(target = "registrationNbr",                     source = "file.registrationNbr")
    @Mapping(target = "ipProcSimple.statusCode",             source = "file.statusCode")
    @Mapping(target = "title",                               source = "file.title")
    @Mapping(target = "titleLang2",                          source = "file.titleLang2")
    public abstract IpFile toEntity(VwIpObjectIndex vw);


    @AfterMapping
    public void afterToEntity(VwIpObjectIndex vw, @MappingTarget IpFile target) {
        String docFiles = vw.getFile().getDocFiles();
        if (!StringUtils.isEmpty(docFiles)) {
            target.setIpDocFilesCollection(new ArrayList<>());
            for (String df : docFiles.split(";")) {
                IpDocFiles idf = new IpDocFiles();
                IpDocFilesPK dfpk = new IpDocFilesPK();
                idf.setPk(dfpk);
                dfpk.setFileSeq(vw.getPk().getFileSeq());
                dfpk.setFileTyp(vw.getPk().getFileTyp());
                dfpk.setFileSer(vw.getPk().getFileSer());
                dfpk.setFileNbr(vw.getPk().getFileNbr());
                String[] parts = df.split("/");
                dfpk.setDocOri(parts[0]);
                dfpk.setDocLog(parts[1]);
                dfpk.setDocSer(Integer.parseInt(parts[2]));
                dfpk.setDocNbr(Integer.parseInt(parts[3]));
                target.getIpDocFilesCollection().add(idf);
            }
        }
    }
}
