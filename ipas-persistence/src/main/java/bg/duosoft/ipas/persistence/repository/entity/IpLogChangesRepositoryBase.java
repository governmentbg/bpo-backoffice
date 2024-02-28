package bg.duosoft.ipas.persistence.repository.entity;

import bg.duosoft.ipas.persistence.model.entity.IpFileLogChanges;
import bg.duosoft.ipas.persistence.model.entity.file.IpLogChangesPK;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * User: Georgi
 * Date: 4.9.2020 г.
 * Time: 14:09
 */
@NoRepositoryBean
public interface IpLogChangesRepositoryBase<T extends IpFileLogChanges & Serializable> extends BaseRepository<T, IpLogChangesPK> {
    Optional<Integer> getMaxLogChangeNumber(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    List<T> getLogChanges(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    T getLogChange(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, Integer logChangeNbr);
}
