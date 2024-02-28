package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.Collection;
import java.util.List;

public interface CfFileTypeRepository extends BaseRepository<CfFileType, String> {
    List<CfFileType> findAllByFileTypInOrderByFileTypeName(Collection<String> fileTypes);
}
