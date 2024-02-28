package bg.duosoft.ipas.persistence.repository.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentAttachment;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentAttachmentPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface IpPatentAttachmentRepository extends BaseRepository<IpPatentAttachment, IpPatentAttachmentPK> {

    @Query(value = "select max(ID) from ext_core.IP_PATENT_ATTACHMENT where FILE_SEQ = ?1 and FILE_TYP = ?2\n" +
            "and FILE_SER = ?3 and FILE_NBR = ?4 and ATTACHMENT_TYPE_ID = ?5", nativeQuery = true)
    Integer getMaxIdByFileIdAndAttachmentType(String fileSeq, String fileType, Integer fileSer, Integer fileNbr,Integer attachmentType);
}
