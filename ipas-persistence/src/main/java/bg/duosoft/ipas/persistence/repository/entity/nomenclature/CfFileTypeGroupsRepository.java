package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileTypeGroup;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;

import java.util.List;

public interface CfFileTypeGroupsRepository extends BaseRepository<CfFileTypeGroup, String> {
    public List<CfFileTypeGroup> getByUserdocFlag(String userdocFlag);

}
