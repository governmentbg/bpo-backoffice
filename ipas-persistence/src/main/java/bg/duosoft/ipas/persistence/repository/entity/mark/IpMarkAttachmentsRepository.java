package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkAttachment;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.List;

public interface IpMarkAttachmentsRepository extends BaseRepository<IpMarkAttachment, Integer> {

    List<IpMarkAttachment> findAllByFileSeqAndFileTypAndFileSerAndFileNbr(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);
}
