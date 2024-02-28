package bg.duosoft.ipas.persistence.repository.entity.doc;


import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFilesPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface IpDocFilesRepository extends BaseRepository<IpDocFiles, IpDocFilesPK> {

    @Query("SELECT DISTINCT new bg.duosoft.ipas.persistence.model.entity.file.IpFilePK (f.pk.fileSeq,f.pk.fileTyp,f.pk.fileSer,f.pk.fileNbr)" +
            " FROM IpDocFiles f where f.pk.docOri = ?1 AND f.pk.docLog = ?2 AND f.pk.docSer = ?3 AND f.pk.docNbr = ?4")
    IpFilePK selectMainObjectIdOfUserdoc(String docOri, String docLog, Integer docSer, Integer docNbr);

}
